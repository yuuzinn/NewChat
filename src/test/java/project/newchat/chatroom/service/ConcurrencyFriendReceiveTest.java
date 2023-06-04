package project.newchat.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.friend.repository.FriendRepository;
import project.newchat.friend.service.FriendService;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.service.UserService;
import project.newchat.userchatroom.repository.UserChatRoomRepository;

@SpringBootTest
public class ConcurrencyFriendReceiveTest {
  ExecutorService executorService;
  CountDownLatch countDownLatch;

  @BeforeEach
  void beforeEach() {
    executorService = Executors.newFixedThreadPool(70);
    countDownLatch = new CountDownLatch(70);
  }

  @Autowired
  private UserService userService;

  @Autowired
  private FriendService friendService;

  @Autowired
  private FriendRepository friendRepository;

  @Test
  @DisplayName("친구요청 수락 시 친구 수 증가, 50명 제한")
  void friend_receive_success() throws InterruptedException {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 70; i++) {
      UserRequest user = new UserRequest(i + "아이디", "12345", "test");
      User user1 = userService.signUpTest(user);
      users.add(user1);
    }

    for (int i = 1; i < 70; i++) {
      friendService.addFriend(i+1L, 1L);
    }


    IntStream.range(0, 70).forEach(e -> executorService.submit(() -> {
      try {
        try {
          friendService.receive(1L, users.get(e).getId());
        } catch (Exception exception) {
          assertThat(exception).isNull();
        }
      } finally {
        countDownLatch.countDown();
      }
    }));

    countDownLatch.await();

    Long aLong = friendRepository.countByFromUserIdOrToUserIdAndAccept(1L);
    assertThat(aLong).isEqualTo(50);
  }

}