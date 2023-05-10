package project.newchat.user.service;

import org.springframework.stereotype.Service;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.UserRequest;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {
    User signUp(UserRequest user);

    User login(LoginRequest user);



}
