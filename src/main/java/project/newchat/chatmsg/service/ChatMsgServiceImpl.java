package project.newchat.chatmsg.service;

import static project.newchat.common.type.ErrorCode.NOT_FOUND_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.chatmsg.domain.ChatMsg;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatmsg.dto.ChatMsgDto;
import project.newchat.chatmsg.repository.ChatMsgCustomRepository;
import project.newchat.chatmsg.repository.ChatMsgRepository;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.kafka.Producers;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;
import project.newchat.userchatroom.domain.UserChatRoom;
import project.newchat.userchatroom.repository.UserChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatMsgServiceImpl implements ChatMsgService {

  private final ChatMsgRepository chatMsgRepository;
  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMsgCustomRepository chatMsgCustomRepository;

  private final UserChatRoomRepository userChatRoomRepository;

  private final String BASIC_TOPIC = "CHAT_ROOM";
  private final Producers producers;

  @Override
  public ChatMsgResponse sendMessage(ChatMsgRequest message, Long userId, Long roomId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_ROOM));
    // 채팅 메시지 생성
    ChatMsg chatMsg = ChatMsg.builder()
        .message(message.getMessage())
        .sendTime(LocalDateTime.now())
        .user(findUser)
        .chatRoom(chatRoom)
        .build();
    // Response
    ChatMsgResponse response = ChatMsgResponse.builder()
        .roomId(roomId)
        .from(findUser.getNickname())
        .message(message.getMessage())
        .sendTime(chatMsg.getSendTime())
        .build();
    chatMsgRepository.save(chatMsg);
    String topicName = BASIC_TOPIC + chatRoom.getId();
    producers.produceMessage(topicName, message.getMessage());
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<ChatMsg> chatMsgsList = chatMsgCustomRepository.findChatRoomIdByChatMsg(roomId, lastId);
    Optional<UserChatRoom> joinUser = userChatRoomRepository.findByUserId(userId);
    if (joinUser.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER);
    }
    LocalDateTime joinDt = joinUser.get().getJoinDt();
    List<ChatMsgDto> chatMsgDtos = new ArrayList<>();
    for (ChatMsg chatMsg : chatMsgsList) {
      ChatMsgDto build = ChatMsgDto.builder()
          .chatMsgId(chatMsg.getId())
          .nickname(chatMsg.getUser().getNickname())
          .sendTime(chatMsg.getSendTime())
          .message(chatMsg.getMessage())
          .userId(chatMsg.getUser().getId())
          .build();
      if (build.getSendTime().isAfter(joinDt)) {
        chatMsgDtos.add(build);
      }
    }
    return chatMsgDtos;
  }
}
