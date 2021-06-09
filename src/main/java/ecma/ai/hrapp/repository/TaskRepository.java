package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
//    Optional<User> findByEmail(String email);
    List<Task> findByTaskTaker(User taskTaker);

    List<Task> findByTaskGiver(User taskOwner);
}
