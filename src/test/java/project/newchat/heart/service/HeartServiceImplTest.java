package project.newchat.heart.service;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.chatroom.service.ChatRoomService;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.heart.domain.Heart;
import project.newchat.heart.repository.HeartRepository;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.repository.UserRepository;
import project.newchat.user.service.UserService;
import project.newchat.userchatroom.domain.UserChatRoom;
import project.newchat.userchatroom.repository.UserChatRoomRepository;

@SpringBootTest
@Transactional
class HeartServiceImplTest {
  @Autowired
  private HeartService heartService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChatRoomService chatRoomService;
  @Autowired
  private ChatRoomRepository chatRoomRepository;
  @Autowired
  private UserChatRoomRepository userChatRoomRepository;
  @Autowired
  private HeartRepository heartRepository;

  @Test
  @DisplayName("채팅방 좋아요 등록 성공")
  void heart_success () {
    // given
    UserRequest user = new UserRequest("test@test.com", "1234", "test");
    userService.signUpTest(user);
    ChatRoomRequest chatRoomRequest = new ChatRoomRequest("test", 8, null);
    ChatRoom room = chatRoomService.createRoom(chatRoomRequest, 1L);
    // then
    heartService.heart(1L, room.getId());
    List<Heart> byUserId = heartRepository.findByUserId(1L);
    // when
    assertThat(byUserId.size()).isEqualTo(1);
  }
  @Test
  @DisplayName("채팅방 좋아요 실패 - 좋아요 중복")
  void heart_failed () {
    // given
    UserRequest user = new UserRequest("test@test.com", "1234", "test");
    userService.signUpTest(user);

    ChatRoom test = ChatRoom.builder()
        .roomCreator(1L)
        .title("test")
        .isPrivate(false)
        .password(null)
        .userCountMax(8)
        .createdAt(now())
        .build();
    chatRoomRepository.save(test);
    // then
    heartService.heart(1L, test.getId());
    // when
    CustomException exception = assertThrows(CustomException.class, () -> {
      heartService.heart(1L, test.getId());
    });
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_HEART_TO_ROOM);
  }
}