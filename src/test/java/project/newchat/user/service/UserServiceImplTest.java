package project.newchat.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        UserRequest userRequest1 = new UserRequest("test1234@naver.com", "1234", "test");
        User user1 = userService.signUp(userRequest1);

        UserRequest userRequest2 = new UserRequest("test12345@naver.com", "1234", "test");
        User user2 = userService.signUp(userRequest2);

        Assertions.assertNotEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    @DisplayName("회원가입 실패_이메일 중복")
    void signUp_failed() {
        UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
        userService.signUp(userRequest1);

        UserRequest userRequest2 = new UserRequest("test1234@naver.com", "1234", "test");

        org.assertj.core.api.Assertions.assertThatThrownBy(()->userService.signUp(userRequest2))
                .isInstanceOf(IllegalArgumentException.class);

    }
}