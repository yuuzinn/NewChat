package project.newchat.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.newchat.user.domain.User;
import project.newchat.user.domain.response.UserSearchResponse;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByEmailAndPassword(String email, String password);

  @Query("select u from User u where u.id in (:ids) order by u.status desc ")
  List<User> findUserById(@Param("ids") List<Long> ids, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.nickname LIKE :nickname")
  Optional<User> findByNicknameLike(@Param ("nickname")String nickname);
}
