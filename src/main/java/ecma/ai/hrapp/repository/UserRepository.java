package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndVerifyCode(@Email String email, String verifyCode);


}
