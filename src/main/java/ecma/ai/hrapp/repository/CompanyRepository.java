package ecma.ai.hrapp.repository;

import ecma.ai.hrapp.entity.Company;
import ecma.ai.hrapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
//    Optional<User> findByEmail(String email);
}
