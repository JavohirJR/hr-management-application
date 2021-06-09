package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.TurniketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TuniketHistoryRepository extends JpaRepository<TurniketHistory, UUID> {
//    Optional<User> findByEmail(String email);
}
