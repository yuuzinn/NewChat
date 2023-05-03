package project.newchat.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.newchat.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailAndPassword(String email, String password);
}
