package project.newchat.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
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
        userService.signUp(userRequest);
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody @Valid UserRequest userRequest,
            HttpSession session) {
        User login = userService.login(userRequest);
        session.setAttribute("user", login.getId());
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("success");
    }
}
