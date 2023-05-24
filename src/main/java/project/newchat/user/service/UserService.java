package project.newchat.user.service;

import org.springframework.stereotype.Service;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.TestUserRequest;
import project.newchat.user.domain.request.UserRequest;

import javax.servlet.http.HttpSession;
import project.newchat.user.dto.UserDto;

@Service
public interface UserService {

  UserDto signUp(UserRequest user);

  User signUpTest(UserRequest user);

  UserDto signUpTe2(TestUserRequest test);

  User login(LoginRequest user);


}
