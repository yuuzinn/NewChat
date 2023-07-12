package project.newchat.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.UpdateRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.domain.response.UserSearchResponse;
import project.newchat.user.dto.UserDto;


@SpringBootTest
@Transactional
class UserServiceImplTest {

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("회원가입 성공")
  void signUp_success() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "1234", "test");
    UserDto user1 = userService.signUp(userRequest1);

    UserRequest userRequest2 = new UserRequest("test12345@naver.com", "1234", "test");
    UserDto user2 = userService.signUp(userRequest2);

    assertNotEquals(user1.getEmail(), user2.getEmail());
  }

  @Test
  @DisplayName("회원가입 실패_이메일 중복")
  void signUp_failed() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);

    UserRequest userRequest2 = new UserRequest("test1234@naver.com", "1234", "test");

    assertThatThrownBy(() -> userService.signUp(userRequest2))
        .isInstanceOf(CustomException.class);

  }

  @Test
  @DisplayName("회원가입 실패_닉네임 중복")
  void signUp_nickname_failed() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);

    UserRequest userRequest2 = new UserRequest("test12345@naver.com", "1234", "test");

    assertThatThrownBy(() -> userService.signUp(userRequest2)).isInstanceOf(CustomException.class);
  }

  @Test
  @DisplayName("로그인 성공")
  void login_success() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);

    LoginRequest loginRequest = new LoginRequest("test1234@naver.com", "12345");

    User login = userService.login(loginRequest);
    assertThat(userRequest1.getPassword()).isEqualTo(login.getPassword());
    assertThat(userRequest1.getEmail()).isEqualTo(login.getEmail());


  }

  @Test
  @DisplayName("로그인 실패_이메일 비밀번호 불일치")
  void login_failed() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);

    LoginRequest loginRequest2 = new LoginRequest("test1234@naver.com", "2");
    LoginRequest loginRequest3 = new LoginRequest("22@naver.com", "22");

    assertThatThrownBy(() -> userService.login(loginRequest2))
        .isInstanceOf(CustomException.class);
    assertThatThrownBy(() -> userService.login(loginRequest3))
        .isInstanceOf(CustomException.class);
  }

  @Test
  @DisplayName("회원 닉네임 변경 성공")
  void nickname_update_success() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);
    UpdateRequest request = new UpdateRequest();
    request.setNickname("updateNickname");
    userService.update(1L, request);

    assertNotEquals(userRequest1.getNickname(), request.getNickname());
    assertThat(request.getNickname()).isEqualTo("updateNickname");
  }

  @Test
  @DisplayName("회원 닉네임 변경 실패 - 닉네임 중복")
  void nickname_update_failed() {
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);
    UserRequest userRequest2 = new UserRequest("test12345@naver.com", "12345", "updateTest");
    userService.signUp(userRequest2);
    UpdateRequest request = new UpdateRequest();
    request.setNickname("updateTest");
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.update(1L, request));
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_USER_NICKNAME);

  }
  @Test
  @DisplayName("회원 검색 성공")
  void search_user() {
    // given
    UserRequest userRequest1 = new UserRequest("test1234@naver.com", "12345", "test");
    userService.signUp(userRequest1);
    UserRequest userRequest2 = new UserRequest("test12345@naver.com", "12345", "updateTest");
    userService.signUp(userRequest2);
    // then
    UserSearchResponse updateTest = userService.searchUserByNickname(1L, "updateTest");
    // when
    assertThat(updateTest.getNickname()).isEqualTo(userRequest2.getNickname());
  }
}