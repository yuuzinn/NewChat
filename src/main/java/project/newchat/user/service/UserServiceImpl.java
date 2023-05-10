package project.newchat.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.newchat.exception.CustomException;
import project.newchat.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User signUp(UserRequest user) {
        Optional<User> email = userRepository.findByEmail(user.getEmail());
        if (email.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USER_ID, user.getEmail());
        }
        User userSave = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
        return userRepository.save(userSave);
    }

    @Override
    public User login(UserRequest user) {
        return userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(() ->
                        new CustomException(ErrorCode.INCONSISTENCY_USER_ID_PASSWORD));
    }


}
