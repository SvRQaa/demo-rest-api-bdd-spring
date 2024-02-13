package tse.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import tse.api.demo.model.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
