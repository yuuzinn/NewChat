package project.newchat.chatroom.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatmsg.repository.ChatMsgRepository;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.controller.request.ChatRoomUpdateRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.dto.ChatRoomDto;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
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

  private final RedissonClient redissonClient;

  @Override
  @Transactional
  public void createRoom(ChatRoomRequest chatRoomRequest, Long userId) {
    // 유저정보조회
    User findUser = getFindUser(userId);
    // chatroom 생성
    ChatRoom chatRoom = ChatRoom.builder()
        .roomCreator(findUser.getId())
        .title(chatRoomRequest.getTitle())
        .userCountMax(chatRoomRequest.getUserCountMax())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
    ChatRoom save = chatRoomRepository.save(chatRoom);

    // 연관관계 user_chat room 생성
    UserChatRoom userChatRoom = UserChatRoom.builder()
        .user(findUser)
        .chatRoom(save)
        .build();
    // save
    userChatRoomRepository.save(userChatRoom);
  }

  @Override
//  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Transactional
  public void joinRoom(Long roomId, Long userId) {
    RLock lock = redissonClient.getLock("joinRoomLock:" + roomId);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(ErrorCode.FAILED_GET_LOCK);
      }
      // 유저 조회
      User findUser = getFindUser(userId);

      // room 조회
      ChatRoom chatRoom = chatRoomRepository.findById(roomId) // lock (기존)
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

      // user_chatroom 현재 인원 카운트 (비즈니스 로직)
      Long currentUserCount = userChatRoomRepository.countNonLockByChatRoomId(roomId); // lock (기존)

      List<Long> userChatRoomByChatRoomId = userChatRoomRepository
          .findUserChatRoomByChatRoom_Id(roomId);

      if (userChatRoomByChatRoomId.contains(userId)) {
        throw new CustomException(ErrorCode.ALREADY_JOIN_ROOM);
      }

      // chatroom 입장
      if (currentUserCount >= chatRoom.getUserCountMax()) {
        throw new CustomException(ErrorCode.ROOM_USER_FULL);
      }

      UserChatRoom userChatRoom = UserChatRoom.builder()
          .user(findUser)
          .chatRoom(chatRoom)
          .build();
      userChatRoomRepository.save(userChatRoom);
      // 비즈니스 로직 끝
    } catch (InterruptedException e) {
      throw new CustomException(ErrorCode.FAILED_GET_LOCK);
    } finally {
      lock.unlock();
    }
  }

  // 채팅방 전체 조회

  @Override
  @Transactional
  public List<ChatRoomDto> getRoomList(Pageable pageable) {
    Page<ChatRoom> all = chatRoomRepository.findAll(pageable);
    return getChatRoomDtos(all);
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
    // 방장이 아니라면
    if (!Objects.equals(room.getRoomCreator(), userId)) {
      userChatRoomRepository.deleteUserChatRoomByUserId(userId);
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
      throw new CustomException(ErrorCode.NOT_ROOM_CREATOR);
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
      throw new CustomException(ErrorCode.NOT_ROOM_CREATOR);
    }
    if (currentRoomTitle.equals(chatRoomUpdateRequest.getTitle())) {
      throw new CustomException(ErrorCode.REQUEST_SAME_AS_CURRENT_TITLE);
    }
    room.update(chatRoomUpdateRequest.getTitle(), LocalDateTime.now());
    chatRoomRepository.save(room);
  }

  private User getFindUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
  }

  private ChatRoom getChatRoom(Long roomId) {
    return chatRoomRepository
        .findChatRoomById(roomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NONE_ROOM));
  }

  // 방 조회 DTO 변환 메서드 추출
  private static List<ChatRoomDto> getChatRoomDtos(Page<ChatRoom> all) {
    return all.stream()
        .map(ChatRoomDto::of)
        .collect(Collectors.toList());
  }
}
