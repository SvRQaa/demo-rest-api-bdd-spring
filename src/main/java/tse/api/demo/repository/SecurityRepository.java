package tse.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import tse.api.demo.model.Security;

@Component
public interface SecurityRepository extends JpaRepository<Security, Long> {
    Security findByName(String name);

}
