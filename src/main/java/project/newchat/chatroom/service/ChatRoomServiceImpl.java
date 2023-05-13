package project.newchat.chatroom.service;


import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;
import project.newchat.user.service.UserService;
import project.newchat.userchatroom.domain.UserChatRoom;
import project.newchat.userchatroom.repository.UserChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  private final UserRepository userRepository;

  private final UserChatRoomRepository userChatRoomRepository;

  @Override
  @Transactional
  public void createRoom(ChatRoomRequest chatRoomRequest, Long userId) {
    // 유저정보조회
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));//
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
  @Transactional
  public void joinRoom(Long roomId, Long userId) {
    // 유저 조회
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // room 조회
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

    // user_chatroom 현재 인원 카운트
    Long currentUserCount = userChatRoomRepository.countByChatRoomId(roomId);

    // chatroom 입장
    if (currentUserCount >= chatRoom.getUserCountMax()) {
      throw new CustomException(ErrorCode.ROOM_USER_FULL);
    }

    UserChatRoom userChatRoom = UserChatRoom.builder()
        .user(findUser)
        .chatRoom(chatRoom)
        .build();
    userChatRoomRepository.save(userChatRoom);

  }
}
