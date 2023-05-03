package project.newchat.user.service;

import org.springframework.stereotype.Service;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;

@Service
public interface UserService {
    User signUp(UserRequest user);

}
