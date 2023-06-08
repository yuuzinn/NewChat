package project.newchat.userchatroom.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.userchatroom.domain.UserChatRoom;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

//  @Lock(LockModeType.PESSIMISTIC_WRITE)
//  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
  Long countByChatRoomId(Long roomId); // 비관적 락
  @Query("select count(*) from UserChatRoom u where u.chatRoom.id = :roomId")
  Long countNonLockByChatRoomId(@Param("roomId")Long roomId); // test 용도

  void deleteUserChatRoomByChatRoom_Id(Long chatRoomId);

  void deleteUserChatRoomByUserId(Long userId);

  @Query("select u.user.id from UserChatRoom u where u.chatRoom.id = ?1")
  List<Long> findUserChatRoomByChatRoom_Id(Long chatRoomId);

  List<UserChatRoom> findUserChatRoomByChatRoomId(Long roomId);
}
