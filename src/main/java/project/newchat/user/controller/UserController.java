package project.newchat.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.util.ResponseUtils;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.dto.UserDto;
import project.newchat.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(
            @RequestBody @Valid UserRequest userRequest) {
        UserDto user = userService.signUp(userRequest);
        return ResponseUtils.ok("회원가입 성공", user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody @Valid LoginRequest userRequest,
            HttpSession session) {
        User login = userService.login(userRequest);
        session.setAttribute("user", login.getId());
        return ResponseEntity.ok().body("로그인 성공");
    }

    @PostMapping("/logout")
    @LoginCheck
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}
