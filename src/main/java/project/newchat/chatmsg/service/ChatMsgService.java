package project.newchat.chatmsg.service;

import java.util.List;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatmsg.dto.ChatMsgDto;

public interface ChatMsgService {

  /**
   * 채팅 보내기
   */
  ChatMsgResponse sendMessage(ChatMsgRequest message, Long userId, Long roomId);

  /**
   * 채팅 조회
   */
  List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId);
}
