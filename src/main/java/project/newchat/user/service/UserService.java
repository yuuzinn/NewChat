package project.newchat.user.service;

import org.springframework.stereotype.Service;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.TestUserRequest;
import project.newchat.user.domain.request.UpdateRequest;
import project.newchat.user.domain.request.UserRequest;

import javax.servlet.http.HttpSession;
import project.newchat.user.domain.response.UserSearchResponse;
import project.newchat.user.dto.UserDto;

@Service
public interface UserService {

  /**
   * 사용자 회원가입
   */
  UserDto signUp(UserRequest user);

  User signUpTest(UserRequest user);

  UserDto signUpTe2(TestUserRequest test);

  /**
   * 로그인
   */
  User login(LoginRequest user);

  /**
   * 사용자 업데이트 (닉네임)
   */
  void update(Long userId, UpdateRequest updateRequest);

  /**
   * 로그아웃
   */
  void logout(Long userId);

  UserSearchResponse searchUserByNickname(Long userId, String nickname);
}
