package project.newchat.user.controller;

import static project.newchat.common.type.ResponseMessage.CREATE_USER;
import static project.newchat.common.type.ResponseMessage.LOGIN_SUCCESS;
import static project.newchat.common.type.ResponseMessage.LOGOUT_SUCCESS;
import static project.newchat.common.type.ResponseMessage.USER_UPDATE_SUCCESS;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.type.ResponseMessage;
import project.newchat.common.util.ResponseUtils;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.TestUserRequest;
import project.newchat.user.domain.request.UpdateRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.dto.UserDto;
import project.newchat.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/signUp")
  public ResponseEntity<Object> signUp(
      @RequestBody @Valid UserRequest userRequest) {
    UserDto user = userService.signUp(userRequest);
    return ResponseUtils.ok(CREATE_USER, user);
  }

  @PostMapping("/signUp2") // 서버 터트리기 테스트
  public ResponseEntity<Object> signUpTest(
      @RequestBody @Valid TestUserRequest userRequest) {
    UserDto user = userService.signUpTe2(userRequest);
    return ResponseUtils.ok(CREATE_USER, user);
  }

  @PostMapping("/login")
  public ResponseEntity<Object> login(
      @RequestBody @Valid LoginRequest userRequest,
      HttpSession session) {
    User login = userService.login(userRequest);
    session.setAttribute("user", login.getId());
    return ResponseUtils.ok(LOGIN_SUCCESS);
  }

  @PostMapping("/logout")
  @LoginCheck
  public ResponseEntity<Object> logout(HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    userService.logout(userId);
    session.invalidate();
    return ResponseUtils.ok(LOGOUT_SUCCESS);
  }

  @PatchMapping("/update")
  @LoginCheck
  public ResponseEntity<Object> update(
      @RequestBody @Valid UpdateRequest updateRequest,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    userService.update(userId, updateRequest);
    return ResponseUtils.ok(USER_UPDATE_SUCCESS);
  }
}
