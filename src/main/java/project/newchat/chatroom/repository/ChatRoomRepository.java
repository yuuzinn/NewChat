package project.newchat.chatroom.repository;

import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.chatroom.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1000")})
  Optional<ChatRoom> findById(Long aLong);

  @Query("select c from ChatRoom c where c.roomCreator=:userId")
  Page<ChatRoom> findAllByUserId(@Param("userId")Long userId,Pageable pageable); //

  Page<ChatRoom> findAllByUserChatRoomsUserId(Long userId,Pageable pageable);

  @Query(value = "SELECT ROOM_CREATOR FROM chat_room WHERE CHAT_ROOM_ID = ?", nativeQuery = true)
  Long findChatRoomIdByRoomId(Long roomId);
}
