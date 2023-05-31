package project.newchat.friend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.newchat.friend.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

  Optional<Friend> findByUserIdAndToUserIdAndIsFriend(Long userId, Long toUserId, Boolean isFriend);

  Optional<Friend> findByUserIdAndToUserId(Long userId, Long toUserId);

  Long countByUserId(Long userId);

  @Query("SELECT f FROM Friend f LEFT JOIN FETCH f.user u WHERE f.toUserId = :userId")
  List<Friend> findFriendByUserId(@Param("userId") Long userId, Pageable pageable);
}
