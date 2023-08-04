package project.newchat.heart.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.newchat.heart.domain.Heart;
@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
  Optional<Heart> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

  List<Heart> findByUserId(Long userId);
}
