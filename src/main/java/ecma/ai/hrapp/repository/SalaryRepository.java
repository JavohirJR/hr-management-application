package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<PaidSalary, UUID> {
    Optional<PaidSalary> findByOwnerAndPeriod(User owner, Month period);
    List<PaidSalary> findAllByOwner(User user);
    List<PaidSalary> findAllByPeriod(Month period);
}
