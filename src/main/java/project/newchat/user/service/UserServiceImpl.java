package project.newchat.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import project.newchat.user.domain.User;
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
            throw new IllegalArgumentException("중복된 아이디입니다");
        }
        User userSave = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
        return userRepository.save(userSave);
    }
}
