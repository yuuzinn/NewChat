package project.newchat.friend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.friend.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
  Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

  @Query(value = "select count(*) from friend where (from_user_id = :id or to_user_id = :id) and accept = true", nativeQuery = true)
  Long countByFromUserIdOrToUserIdAndAccept(@Param("id") Long id);
}
