package project.newchat.chatmsg.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.newchat.chatmsg.domain.ChatMsg;
@Repository
@RequiredArgsConstructor
public class ChatMsgCustomRepositoryImpl implements ChatMsgCustomRepository{

  private final EntityManager entityManager;

  /*
  select c.user_id, c.message, c.send_time
  from chat_msg c
  where chat_room_id = 1
  order by c.chat_msg_id asc ;
   */
  @Override
  public List<ChatMsg> findChatRoomIdByChatMsg(Long chatMsg, Long lastId) {
    String first = "select c from ChatMsg c where c.chatRoom.id =: chatMsg order by c.id asc";
    String paging = "select c from ChatMsg c where c.chatRoom.id =: chatMsg and c.id > :lastId order by c.id asc";

    TypedQuery<ChatMsg> query = null;

    if (lastId == null) {
      query = entityManager
          .createQuery(first, ChatMsg.class)
          .setParameter("chatMsg", chatMsg);
    } else {
      query = entityManager
          .createQuery(paging, ChatMsg.class)
          .setParameter("chatMsg", chatMsg)
          .setParameter("lastId", lastId);
    }
    return query
        .setMaxResults(10)
        .getResultList();
  }
}
