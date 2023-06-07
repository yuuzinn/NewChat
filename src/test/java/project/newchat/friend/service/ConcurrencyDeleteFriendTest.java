package project.newchat.friend.service;

import static project.newchat.common.type.ErrorCode.NOT_FOUND_FRIEND;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.service.UserService;

@SpringBootTest
@Transactional
class ConcurrencyDeleteFriendTest {

  @Autowired
  private FriendService friendService;
  @Autowired
  private UserService userService;
  ExecutorService executorService;
  CountDownLatch countDownLatch;

  @BeforeEach
  void beforeEach() {
    executorService = Executors.newFixedThreadPool(2);
    countDownLatch = new CountDownLatch(2);
  }

  @Test
  @DisplayName("두 명 동시에 서로 친구삭제 불가능, 한 명만 가능하다.")
  void one_user_DeleteFriend_success() throws InterruptedException {
    UserRequest user1Request = new UserRequest("user1", "123", "1");
    UserRequest user2Request = new UserRequest("user2", "123", "2");

    User user1 = userService.signUpTest(user1Request);
    User user2 = userService.signUpTest(user2Request);

    friendService.addFriend(user1.getId(), user2.getId());

    CountDownLatch latch = new CountDownLatch(2);

    executorService.submit(() -> {
      try {
        friendService.delete(user1.getId(), user2.getId());
      } catch (Exception exception) {
        Assertions.assertThat(exception).isEqualTo(new CustomException(NOT_FOUND_FRIEND));

      } finally {
        latch.countDown();
      }
    });

    executorService.submit(() -> {
      try {
        friendService.addFriend(user2.getId(), user1.getId());
      } catch (Exception exception) {
        Assertions.assertThat(exception).isEqualTo(new CustomException(NOT_FOUND_FRIEND));

      } finally {
        latch.countDown();
      }
    });
    latch.await();
  }
}