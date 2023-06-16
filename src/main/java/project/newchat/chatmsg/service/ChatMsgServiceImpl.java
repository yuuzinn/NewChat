package project.newchat.chatmsg.service;

import static project.newchat.common.type.ErrorCode.NOT_FOUND_ROOM;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.newchat.chatmsg.domain.ChatMsg;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatmsg.dto.ChatMsgDto;
import project.newchat.chatmsg.repository.ChatMsgCustomRepository;
import project.newchat.chatmsg.repository.ChatMsgRepository;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.repository.ChatRoomRepository;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatMsgServiceImpl implements ChatMsgService {

  private final ChatMsgRepository chatMsgRepository;
  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMsgCustomRepository chatMsgCustomRepository;

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
    return response;
  }

  @Override
  public List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<ChatMsg> chatMsgsList = chatMsgCustomRepository.findChatRoomIdByChatMsg(roomId, lastId);
    return chatMsgsList.stream().map(chatMsg -> new ChatMsgDto(
        chatMsg.getId(),
        chatMsg.getUser().getId(),
        chatMsg.getUser().getNickname(),
        chatMsg.getMessage(),
        chatMsg.getSendTime())).collect(Collectors.toList());
  }
}
