package project.newchat.friend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.friend.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
  Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
  Optional<Friend> findByFromUserIdAndToUserIdAndAccept(Long fromUserId, Long toUserId, Boolean accept);

  @Query(value = "select count(*) from friend where (from_user_id = :id or to_user_id = :id) and accept = true", nativeQuery = true)
  Long countByFromUserIdOrToUserIdAndAccept(@Param("id") Long id);
  // fromUserId 로 toUserId 값을 찾아낼 것 (자신의 ID가 들어가야함.)
  List<Friend> findFriendByFromUserIdAndAccept(Long fromUserId, Boolean accept);
  // toUserId 로 fromUserId 값을 찾아낼 것 (자신의 ID가 들어가야함.)
  List<Friend> findFriendByToUserIdAndAccept(Long toUserId, Boolean accept);
}
