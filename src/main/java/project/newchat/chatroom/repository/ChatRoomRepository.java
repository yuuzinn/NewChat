package project.newchat.chatroom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.chatroom.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findById(Long aLong);

  @Query("SELECT c FROM ChatRoom c LEFT JOIN Heart h ON c.id = h.chatRoom.id " +
      "GROUP BY c.id " +
      "ORDER BY COUNT(h.user.id) DESC")
  Page<ChatRoom> findAllByOrderByHearts(Pageable pageable);

  Optional<ChatRoom> findChatRoomById(Long id);

  @Query("select c from ChatRoom c where c.roomCreator=:userId")
  Page<ChatRoom> findAllByUserId(@Param("userId")Long userId,Pageable pageable); //

  Page<ChatRoom> findAllByUserChatRoomsUserId(Long userId,Pageable pageable);

  @Query("SELECT c FROM ChatRoom c WHERE c.id IN :ids ORDER BY c.createdAt DESC")
  Page<ChatRoom> findChatRoomByInId(@Param("ids") List<Long> ids, Pageable pageable);

  @Query("SELECT c FROM ChatRoom c WHERE c.title LIKE CONCAT('%', :title, '%')")
  Page<ChatRoom> findChatRoomWithPartOfTitle(@Param("title") String title, Pageable pageable);
}
