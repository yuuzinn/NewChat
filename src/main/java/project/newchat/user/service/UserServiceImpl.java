package project.newchat.user.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.repository.UserRepository;

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
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(userSave);
    }

    @Override
    public User login(LoginRequest user) {
        return userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(() ->
                        new CustomException(ErrorCode.INCONSISTENCY_USER_ID_PASSWORD));
    }


}
