package escuelaing.edu.co.Patrones.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import escuelaing.edu.co.Patrones.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
