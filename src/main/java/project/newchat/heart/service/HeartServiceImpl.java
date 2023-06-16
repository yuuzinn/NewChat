package project.newchat.heart.service;

import static project.newchat.common.type.ErrorCode.ALREADY_HEART_TO_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_EXIST_ROOM_HEART;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.heart.domain.Heart;
import project.newchat.heart.repository.HeartRepository;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {

  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final HeartRepository heartRepository;

  @Override
  public void heart(Long userId, Long roomId) {
    Optional<User> user = userRepository
        .findById(userId);
    Optional<ChatRoom> room = chatRoomRepository
        .findById(roomId);
    if (user.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER);
    }
    if (room.isEmpty()) {
      throw new CustomException(NOT_FOUND_ROOM);
    }
    Optional<Heart> userToRoomHeart = heartRepository
        .findByUserIdAndChatRoomId(userId, roomId);
    if (userToRoomHeart.isPresent()) {
      throw new CustomException(ALREADY_HEART_TO_ROOM);
    }
    Heart build = Heart.builder()
        .user(user.get())
        .chatRoom(room.get())
        .build();
    heartRepository.save(build);
  }

  @Override
  @Transactional
  public void heartDelete(Long userId, Long roomId) {
    Optional<User> user = userRepository
        .findById(userId);
    Optional<ChatRoom> room = chatRoomRepository
        .findById(roomId);
    if (user.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER);
    }
    if (room.isEmpty()) {
      throw new CustomException(NOT_FOUND_ROOM);
    }
    Optional<Heart> userToRoomHeart = heartRepository
        .findByUserIdAndChatRoomId(userId, roomId);
    if (userToRoomHeart.isEmpty()) {
      throw new CustomException(NOT_EXIST_ROOM_HEART);
    }
    Heart heart = userToRoomHeart.get();
    heartRepository.delete(heart);
  }
}
