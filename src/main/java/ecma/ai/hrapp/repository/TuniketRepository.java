package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Company;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TuniketRepository extends JpaRepository<Turniket, UUID> {
    Optional<Turniket> findByNumber(String number);

    Optional<Turniket> findAllByOwner(User user);
//    Optional<User> findByEmail(String email);
}
