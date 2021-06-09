package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.TurniketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, UUID> {
    List<TurniketHistory> findAllByTurniketAndTimeIsBetween(Turniket turniket, Timestamp startTime, Timestamp endTime);

    List<TurniketHistory> findAllByTurniket(Turniket turniket);

}
