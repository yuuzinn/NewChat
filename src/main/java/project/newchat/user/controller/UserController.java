package project.newchat.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Object> signUp (
            @RequestPart(value = "data")UserRequest userRequest) {
        userService.signUp(userRequest);
        return ResponseEntity.ok().body("success");
    }
}
