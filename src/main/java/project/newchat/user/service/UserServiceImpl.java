package project.newchat.user.service;

import static project.newchat.common.type.ErrorCode.ALREADY_USER_ID;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;
import static project.newchat.common.type.ErrorCode.NOT_SAME_PASSWORD;
import static project.newchat.common.type.ErrorCode.REQUEST_SAME_AS_CURRENT_NICKNAME;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.newchat.common.exception.CustomException;
import project.newchat.user.domain.User;
import project.newchat.user.domain.request.LoginRequest;
import project.newchat.user.domain.request.TestUserRequest;
import project.newchat.user.domain.request.UpdateRequest;
import project.newchat.user.domain.request.UserRequest;
import project.newchat.user.domain.response.UserSearchResponse;
import project.newchat.user.dto.UserDto;
import project.newchat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDto signUp(UserRequest user) {
    Optional<User> email = userRepository.findByEmail(user.getEmail());
    if (email.isPresent()) {
      throw new CustomException(ALREADY_USER_ID, user.getEmail());
    }
    String userPasswordEncode = passwordEncoder.encode(user.getPassword());
    User userSave = User.builder()
        .email(user.getEmail())
        .password(userPasswordEncode)
        .nickname(user.getNickname())
        .createdAt(LocalDateTime.now())
        .status(false)
        .build();

    UserDto userDto = UserDto.builder()
        .email(userSave.getEmail())
        .nickname(userSave.getNickname())
        .build();

    userRepository.save(userSave);
    return userDto;
  }

  @Override
  public UserDto signUpTe2(TestUserRequest user) {
    String randomEmail = generateRandomEmail(); // 랜덤 이메일 생성
    Optional<User> email = userRepository.findByEmail(randomEmail);
    User userSave = User.builder().email(randomEmail).password(user.getPassword())
        .nickname(user.getNickname()).createdAt(LocalDateTime.now()).build();

    UserDto userDto = UserDto.builder().email(userSave.getEmail()).nickname(userSave.getNickname())
        .build();
    userRepository.save(userSave);
    return userDto;
  }
  private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz1234567890"; // 이메일에 사용할 문자

  private String generateRandomEmail() {
    Random rd = new Random();
    String domain = "example.com"; // 이메일 도메인
    int length = 10; // 이메일 길이

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int index = rd.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }

    return sb.toString() + "@" + domain;
  }

  @Override
  public User signUpTest(UserRequest user) {
    Optional<User> email = userRepository.findByEmail(user.getEmail());
    if (email.isPresent()) {
      throw new CustomException(ALREADY_USER_ID, user.getEmail());
    }
    User userSave = User.builder().email(user.getEmail()).password(user.getPassword())
        .nickname(user.getNickname()).createdAt(LocalDateTime.now()).build();

    return userRepository.save(userSave);
  }

  @Override
  public User login(LoginRequest request) {
    String requestPassword = request.getPassword(); // 1244
    Optional<User> user = userRepository.findByEmail(request.getEmail());// db pw
    if (user.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER, request.getEmail());
    }
    String dbPassword = user.get().getPassword();
    if (!isSamePassword(requestPassword, dbPassword)) {
      throw new CustomException(NOT_SAME_PASSWORD);
    }
    user.get().setStatus(true);
    userRepository.save(user.get());
    return userRepository.findUserByEmailAndPassword(request.getEmail(), dbPassword).orElseThrow();
  }

  @Override
  public void update(Long userId, UpdateRequest updateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    String currentNickname = user.getNickname();
    if (currentNickname.equals(updateRequest.getNickname())) {
      throw new CustomException(REQUEST_SAME_AS_CURRENT_NICKNAME); // 현재 닉네임과 바꿀 닉네임이 같을 경우
    }
    user.update(updateRequest.getNickname(), LocalDateTime.now());
    userRepository.save(user);
    }

  @Override
  public void logout(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    user.setStatus(false);
    userRepository.save(user);
  }

  @Override
  public UserSearchResponse searchUserByNickname(Long userId, String nickname) {
    userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Optional<User> searchUser = userRepository.findByNicknameLike(nickname);
    if (searchUser.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER);
    }
    return UserSearchResponse.builder()
        .id(searchUser.get().getId())
        .nickname(nickname)
        .status(searchUser.get().getStatus())
        .build();
  }

  public boolean isSamePassword(String password, String dbUserPassword) {
    return passwordEncoder.matches(password, dbUserPassword);
  }
}
