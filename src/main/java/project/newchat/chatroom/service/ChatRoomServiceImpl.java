package project.newchat.chatroom.service;


import static java.time.LocalDateTime.now;
import static project.newchat.common.type.ErrorCode.ALREADY_JOIN_ROOM;
import static project.newchat.common.type.ErrorCode.FAILED_GET_LOCK;
import static project.newchat.common.type.ErrorCode.INVALID_REQUEST;
import static project.newchat.common.type.ErrorCode.NEED_TO_PASSWORD;
import static project.newchat.common.type.ErrorCode.NONE_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_HEART;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;
import static project.newchat.common.type.ErrorCode.NOT_ROOM_CREATOR;
import static project.newchat.common.type.ErrorCode.NOT_ROOM_MEMBER;
import static project.newchat.common.type.ErrorCode.REQUEST_SAME_AS_CURRENT_TITLE;
import static project.newchat.common.type.ErrorCode.ROOM_PASSWORD_MISMATCH;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatmsg.repository.ChatMsgRepository;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.controller.request.ChatRoomUpdateRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.dto.ChatRoomDto;
import project.newchat.chatroom.dto.ChatRoomUserDto;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.heart.domain.Heart;
import project.newchat.heart.repository.HeartRepository;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;
import project.newchat.userchatroom.domain.UserChatRoom;
import project.newchat.userchatroom.repository.UserChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  private final ChatMsgRepository chatMsgRepository;

  private final UserRepository userRepository;

  private final UserChatRoomRepository userChatRoomRepository;

  private final HeartRepository heartRepository;

  private final RedissonClient redissonClient;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String BASIC_TOPIC = "CHAT_ROOM";

  @Override
  @Transactional
  public ChatRoom createRoom(ChatRoomRequest chatRoomRequest, Long userId) {
    // 유저정보조회
    User findUser = getUser(userId);
    String password = chatRoomRequest.getPassword();
    // chatroom 생성
    ChatRoom chatRoom = ChatRoom.builder()
        .roomCreator(findUser.getId())
        .title(chatRoomRequest.getTitle())
        .userCountMax(chatRoomRequest.getUserCountMax())
        .createdAt(now())
        .updatedAt(now())
        .isPrivate(false)
        .build();
    if (password != null) {
      chatRoom.setPassword(password);
      chatRoom.setIsPrivate(true);
    }
    ChatRoom save = chatRoomRepository.save(chatRoom);

    // 연관관계 user_chat room 생성
    UserChatRoom userChatRoom = UserChatRoom.builder()
        .user(findUser)
        .chatRoom(save)
        .joinDt(now())
        .build();
    // save
    userChatRoomRepository.save(userChatRoom);
    String topicName = BASIC_TOPIC + save.getId();
    NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
    // Kafka Topic에 구독자 추가
    kafkaTemplate.send(newTopic.name(), "Subscribed");
    return save;
  }

  @Override
  @Transactional
  public void joinRoom(Long roomId, Long userId, ChatRoomRequest chatRoomRequest) {
    RLock lock = redissonClient.getLock("joinRoomLock:" + roomId);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(FAILED_GET_LOCK);
      }
      // 유저 조회
      User findUser = getUser(userId);

      // room 조회
      ChatRoom chatRoom = chatRoomRepository.findById(roomId) // lock (기존)
          .orElseThrow(() -> new CustomException(NOT_FOUND_ROOM));

      // user_chatroom 현재 인원 카운트 (비즈니스 로직)
      Long currentUserCount = userChatRoomRepository.countNonLockByChatRoomId(roomId); // lock (기존)

      if (chatRoom.getIsPrivate() && chatRoomRequest.getPassword() == null) {
        throw new CustomException(NEED_TO_PASSWORD);
      }
      if (chatRoom.getIsPrivate() && !chatRoomRequest.getPassword().equals(chatRoom.getPassword())) {
        throw new CustomException(ROOM_PASSWORD_MISMATCH);
      }

      if (!chatRoom.getIsPrivate() && chatRoom.getPassword() == null) {
        List<Long> userList = userChatRoomRepository
            .findUserChatRoomByChatRoom_Id(roomId);
        if (userList.contains(userId)) {
          throw new CustomException(ALREADY_JOIN_ROOM);
        }

        // chatroom 입장
        if (currentUserCount >= chatRoom.getUserCountMax()) {
          throw new CustomException(ErrorCode.ROOM_USER_FULL);
        }
        // 비밀번호 확인

        UserChatRoom userChatRoom = UserChatRoom.builder()
            .user(findUser)
            .chatRoom(chatRoom)
            .joinDt(now())
            .build();
        UserChatRoom save = userChatRoomRepository.save(userChatRoom);
        String topicName = BASIC_TOPIC + save.getChatRoom().getId();
        kafkaTemplate.send(topicName, "Subscribed"); // 개선점
        // 비즈니스 로직 끝
      }
    } catch (InterruptedException e) {
      throw new CustomException(FAILED_GET_LOCK);
    } finally {
      lock.unlock();
    }
  }

  // 채팅방 전체 조회
  @Override
  @Transactional(readOnly = true)
  public List<ChatRoomDto> getRoomList(Pageable pageable) {
    Page<ChatRoom> all = chatRoomRepository.findAll(pageable);
    return getChatRoomDtos(all);
  }

  // 좋아요 순으로 정렬 후 방 전체 조회
  @Override
  public List<ChatRoomDto> getRoomHeartSortList(Pageable pageable) {
    Page<ChatRoom> all = chatRoomRepository.findAllByOrderByHearts(pageable);
    List<ChatRoom> chatRooms = all.toList();
      return chatRooms.stream()
              .map(ChatRoomDto::of)
              .collect(Collectors.toList());
  }

  // 자신이 생성한 방 리스트 조회
  @Override
  public List<ChatRoomDto> roomsByCreatorUser(Long userId, Pageable pageable) {
    Page<ChatRoom> userCreateAll = chatRoomRepository.findAllByUserId(userId, pageable);
    return getChatRoomDtos(userCreateAll);
  }
  // 자신이 참여한 방 리스트 조회

  @Override
  public List<ChatRoomDto> getUserByRoomPartList(Long userId, Pageable pageable) {
    Page<ChatRoom> userPartAll = chatRoomRepository
        .findAllByUserChatRoomsUserId(userId, pageable);
    return getChatRoomDtos(userPartAll);
  }

  @Override
  @Transactional
  public void outRoom(Long userId, Long roomId) {
    ChatRoom room = getChatRoom(roomId);
    List<UserChatRoom> userByChatRoomId = userChatRoomRepository
        .findUserByChatRoomId(roomId);
    List<Long> userIds = new ArrayList<>();
    for (UserChatRoom userChatRoom : userByChatRoomId) {
      Long id = userChatRoom.getUser().getId();
      userIds.add(id);
    }
    // 만약 방에 없는데 나가기를 시도한 경우
    if (!userIds.contains(userId)) {
      throw new CustomException(INVALID_REQUEST);
    }
    // 방장이 아니라면
    if (!Objects.equals(room.getRoomCreator(), userId)) {
      userChatRoomRepository.deleteUserChatRoomByUserId(userId); // point select???
      return;
    }
    // 방장이라면 방 삭제
    chatMsgRepository.deleteChatMsgByChatRoom_Id(roomId);
    userChatRoomRepository.deleteUserChatRoomByChatRoom_Id(roomId);
    chatRoomRepository.deleteById(roomId);
  }

  @Override
  @Transactional
  public void deleteRoom(Long userId, Long roomId) {
    ChatRoom room = getChatRoom(roomId);
    if (!Objects.equals(room.getRoomCreator(), userId)) {
      throw new CustomException(NOT_ROOM_CREATOR);
    }
    chatMsgRepository.deleteChatMsgByChatRoom_Id(roomId);
    userChatRoomRepository.deleteUserChatRoomByChatRoom_Id(roomId);
    chatRoomRepository.deleteById(roomId);
  }

  @Override
  public void updateRoom(Long roomId, ChatRoomUpdateRequest chatRoomUpdateRequest, Long userId) {
    ChatRoom room = getChatRoom(roomId);
    String currentRoomTitle = room.getTitle();
    if (!room.getRoomCreator().equals(userId)) {
      throw new CustomException(NOT_ROOM_CREATOR);
    }
    if (currentRoomTitle.equals(chatRoomUpdateRequest.getTitle())) {
      throw new CustomException(REQUEST_SAME_AS_CURRENT_TITLE);
    }
    room.update(chatRoomUpdateRequest.getTitle(), now());
    chatRoomRepository.save(room);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChatRoomUserDto> getRoomUsers(Long roomId, Long userId) {
    // 방 정보
    getChatRoom(roomId);
    // 로그인 유저 정보
    getUser(userId);
    // 방에 있는 유저 정보
    List<UserChatRoom> userIds = userChatRoomRepository
        .findUserChatRoomByChatRoomId(roomId);
    // 방에 있지 않은 유저는 볼 수 없음
    List<Long> userIdList = new ArrayList<>();
    for (UserChatRoom chatRoom : userIds) {
      Long id = chatRoom.getUser().getId();
      userIdList.add(id);
    }
    if (!userIdList.contains(userId)) {
      throw new CustomException(NOT_ROOM_MEMBER);
    }
    // DTO 담기
    List<ChatRoomUserDto> chatRoomUserDtos = userIds.stream()
            .map(ChatRoomUserDto::of)
            .collect(Collectors.toList());
    return chatRoomUserDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChatRoomDto> myHeartRoomList(Long userId, Pageable pageable) {
    getUser(userId);
    List<Heart> heart = heartRepository.findByUserId(userId);
    if (heart.isEmpty()) {
      throw new CustomException(NOT_FOUND_HEART);
    }
    List<Long> ids = new ArrayList<>();
    for (Heart heart1 : heart) {
      Long id = heart1.getChatRoom().getId();
      ids.add(id);
    }
    Page<ChatRoom> chatRoomByInId = chatRoomRepository.findChatRoomByInId(ids, pageable);
    List<ChatRoom> chatRoomList = chatRoomByInId.toList();
    List<ChatRoomDto> chatRoomDtos = chatRoomList.stream()
            .map(ChatRoomDto::of)
            .collect(Collectors.toList());
    return chatRoomDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChatRoomDto> searchRoomByTitle(String roomName, Long userId, Pageable pageable) {
    getUser(userId);
    Page<ChatRoom> search = chatRoomRepository.findByTitleContaining(roomName, pageable);

    List<ChatRoom> searchRoomList = search.toList();
    if (searchRoomList.size() == 0) {
      throw new CustomException(NOT_FOUND_ROOM);
    }
    List<ChatRoomDto> chatRoomDtos = searchRoomList.stream()
            .map(ChatRoomDto::of)
            .collect(Collectors.toList());
    return chatRoomDtos;
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }

  private ChatRoom getChatRoom(Long roomId) {
    return chatRoomRepository
        .findChatRoomById(roomId)
        .orElseThrow(() -> new CustomException(NONE_ROOM));
  }

  // 방 조회 DTO 변환 메서드 추출
  private static List<ChatRoomDto> getChatRoomDtos(Page<ChatRoom> all) {
    return all.stream()
        .map(ChatRoomDto::of)
        .collect(Collectors.toList());
  }
}
