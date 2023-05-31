package project.newchat.friend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.newchat.friend.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

  Optional<Friend> findByUserIdAndToUserIdAndIsFriend(Long userId, Long toUserId, Boolean isFriend);

  Optional<Friend> findByUserIdAndToUserId(Long userId, Long toUserId);

  Long countByUserId(Long userId);
}
