package project.newchat.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
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


  @Test
  @DisplayName("채팅방 생성")
  void createRoom_success() {
    UserRequest userRequest = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest);

    LoginRequest loginRequest = new LoginRequest("test1234@naver.com", "12345");
    User login = userService.login(loginRequest);

    Long id = login.getId();

    ChatRoomRequest chatRoomRequest = new ChatRoomRequest("testTitle", 8);
    chatRoomService.createRoom(chatRoomRequest, id);

    assertThat(chatRoomRequest.getTitle()).isEqualTo("testTitle");
    assertThat(chatRoomRequest.getUserCountMax()).isEqualTo(8);
  }
}