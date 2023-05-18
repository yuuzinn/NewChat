package project.newchat.chatmsg.service;

import java.util.List;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatmsg.dto.ChatMsgDto;

public interface ChatMsgService {

  ChatMsgResponse sendMessage(ChatMsgRequest message, Long userId, Long roomId);

  List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId);
}
