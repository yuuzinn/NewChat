package project.newchat.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;


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

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
        userService.signUp(userRequest1);

        User login = userService.login(userRequest1);
        org.assertj.core.api.Assertions.assertThat(userRequest1.getPassword()).isEqualTo(login.getPassword());
        org.assertj.core.api.Assertions.assertThat(userRequest1.getEmail()).isEqualTo(login.getEmail());


    }

    @Test
    @DisplayName("로그인 실패_이메일 비밀번호 불일치")
    void login_failed() {
        UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
        userService.signUp(userRequest1);
        UserRequest failUserRequest1 = new UserRequest("test12234@naver.com", "12345", "test");
        UserRequest failUserRequest2 = new UserRequest("test1234@naver.com", "2", "test");
        UserRequest failUserRequest3 = new UserRequest("22@naver.com", "22", "test");

        org.assertj.core.api.Assertions.assertThatThrownBy(()->userService.login(failUserRequest1))
                .isInstanceOf(IllegalArgumentException.class);
        org.assertj.core.api.Assertions.assertThatThrownBy(()->userService.login(failUserRequest2))
                .isInstanceOf(IllegalArgumentException.class);
        org.assertj.core.api.Assertions.assertThatThrownBy(()->userService.login(failUserRequest3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}