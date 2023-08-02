package project.newchat.userchatroom.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.userchatroom.domain.UserChatRoom;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

  @Query("select count(*) from UserChatRoom u where u.chatRoom.id = :roomId")
  Long countNonLockByChatRoomId(@Param("roomId")Long roomId); // test 용도

  void deleteUserChatRoomByChatRoom_Id(Long chatRoomId);

  void deleteUserChatRoomByUserId(Long userId);

  @Query("select u.user.id from UserChatRoom u where u.chatRoom.id = ?1")
  List<Long> findUserChatRoomByChatRoom_Id(Long chatRoomId);

  List<UserChatRoom> findUserChatRoomByChatRoomId(Long roomId);

  List<UserChatRoom> findUserByChatRoomId(Long roomId);

  Optional<UserChatRoom> findByUserId(Long userId);;
}
