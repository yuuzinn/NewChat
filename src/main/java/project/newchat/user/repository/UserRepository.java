package project.newchat.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.newchat.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByEmailAndPassword(String email, String password);
}
