package project.newchat.chatmsg.repository;


import java.util.List;
import project.newchat.chatmsg.domain.ChatMsg;

public interface ChatMsgCustomRepository {
  List<ChatMsg> findChatRoomIdByChatMsg(Long chatMsg, Long lastId);
}
