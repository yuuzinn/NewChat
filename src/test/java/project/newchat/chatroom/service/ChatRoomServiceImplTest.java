package project.newchat.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.service.UserService;

@SpringBootTest
@Transactional
class ChatRoomServiceImplTest {

  @Autowired
  private ChatRoomService chatRoomService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChatRoomRepository chatRoomRepository;


  @Test
  @DisplayName("채팅방 생성")
  void createRoom_success() {
    UserRequest userRequest = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest);

    LoginRequest loginRequest = new LoginRequest("test1234@naver.com", "12345");
    User login = userService.login(loginRequest);

    Long id = login.getId();

    ChatRoomRequest chatRoomRequest = new ChatRoomRequest("testTitle", 8, null);
    chatRoomService.createRoom(chatRoomRequest, id);

    assertThat(chatRoomRequest.getTitle()).isEqualTo("testTitle");
    assertThat(chatRoomRequest.getUserCountMax()).isEqualTo(8);
  }
  @Test
  @DisplayName("동일 채팅방 두 번 이상 입장 시도(실패가 되어야 한다.)")
  void continue_joinRoom_failed() {
    UserRequest user = new UserRequest("test@test.com", "1234", "test");
    userService.signUpTest(user);

    UserRequest user2 = new UserRequest("test2@test.com", "1234", "test");
    userService.signUpTest(user2);

    ChatRoom test = ChatRoom.builder()
        .roomCreator(1L)
        .title("test")
        .userCountMax(8)
        .build();

    chatRoomRepository.save(test);
    chatRoomService.joinRoom(1L, 2L, null);

    CustomException exception = assertThrows(CustomException.class, () ->
        chatRoomService.joinRoom(1L, 2L, null));
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_JOIN_ROOM);
    assertThat(exception.getErrorMessage()).isEqualTo("이미 채팅방에 입장해 있습니다.");
  }
}