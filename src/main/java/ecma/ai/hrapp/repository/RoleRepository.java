package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Integer> {
//    Optional<User> findByEmail(String email);
}
