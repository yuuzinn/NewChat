package project.newchat.chatmsg.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.service.UserService;

@SpringBootTest
@Transactional
class ChatMsgServiceImplTest {

  @Autowired
  private ChatMsgService chatMsgService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChatRoomRepository chatRoomRepository;


  @Test
  @DisplayName("채팅 보내기")
  void sendMsg_success() {
    UserRequest user = new UserRequest("test@test.com", "1234", "test");
    userService.signUpTest(user);

    ChatRoom test = ChatRoom.builder()
        .roomCreator(1L)
        .title("test")
        .userCountMax(8)
        .build();
    chatRoomRepository.save(test);

    ChatMsgRequest message = new ChatMsgRequest("test");

    ChatMsgResponse chatMsgResponse = chatMsgService.sendMessage(message, 1L, 1L);
    assertThat(chatMsgResponse.getMessage()).isEqualTo("test");
    assertThat(chatMsgResponse.getFrom()).isEqualTo("test");
    assertThat(chatMsgResponse.getRoomId()).isEqualTo(1L);
  }
}