package project.newchat.chatmsg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.newchat.chatmsg.domain.ChatMsg;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
  void deleteChatMsgByChatRoom_Id(Long chatRoomId);
}
