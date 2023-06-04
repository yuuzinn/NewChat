package project.newchat.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.newchat.friend.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
