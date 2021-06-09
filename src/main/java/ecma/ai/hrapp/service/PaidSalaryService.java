package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.PaidSalary;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.Month;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.PaidSalaryDTO;
import ecma.ai.hrapp.repository.SalaryRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaidSalaryService {
    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    UserRepository userRepository;

    public ApiResponse addSalary(PaidSalaryDTO paidSalaryDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            Optional<User> optionalUser1 = userRepository.findByEmail(paidSalaryDTO.getUserEmail());
            if (!optionalUser1.isPresent()) return new ApiResponse("Staff is not found", false);
            User salaryTaker = optionalUser1.get();

            PaidSalary paidSalary = new PaidSalary();
            paidSalary.setAmount(paidSalaryDTO.getAmount());
            paidSalary.setOwner(salaryTaker);
            paidSalary.setPeriod(paidSalaryDTO.getMonth());
            salaryRepository.save(paidSalary);

            return new ApiResponse("Salary to'landi", true);
        }
        return new ApiResponse("You cant add salary", false);
    }

    public ApiResponse getSalaryBuUser(String email) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            Optional<User> optionalUser1 = userRepository.findByEmail(email);
            if (!optionalUser1.isPresent()) return new ApiResponse("Staff is not found", false);
            User salaryTaker = optionalUser1.get();

            List<PaidSalary> allByOwner = salaryRepository.findAllByOwner(salaryTaker);

            return new ApiResponse("Salary to'landi", true, allByOwner);
        }
        return new ApiResponse("You cant add salary", false);
    }

    public ApiResponse getSalaryByMonth(String month) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            return new ApiResponse("List by period", true, salaryRepository.findAllByPeriod(Month.valueOf(month)));
        }
        return new ApiResponse("You cant deleted salary", false);
    }

    public ApiResponse editSalary(String email, PaidSalaryDTO paidSalaryDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            Optional<User> optionalUser1 = userRepository.findByEmail(paidSalaryDTO.getUserEmail());
            if (!optionalUser1.isPresent()) return new ApiResponse("Staff is not found", false);
            User salaryTaker = optionalUser1.get();

            Optional<PaidSalary> byOwnerAndPeriod = salaryRepository.findByOwnerAndPeriod(salaryTaker, paidSalaryDTO.getMonth());

            if (!byOwnerAndPeriod.isPresent()) return new ApiResponse("Oylik mavjud emas!", false);

            if (byOwnerAndPeriod.get().isPaid())
                return new ApiResponse("Bu oylik allaqachon to'langan, uni o'zgartira olmaysiz!", false);



            PaidSalary paidSalary = byOwnerAndPeriod.get();
            paidSalary.setAmount(paidSalaryDTO.getAmount());
            paidSalary.setOwner(salaryTaker);
            paidSalary.setPeriod(paidSalaryDTO.getMonth());
            salaryRepository.save(paidSalary);

            return new ApiResponse("Salary o'zgardi", true);
        }
        return new ApiResponse("You cant edit salary", false);
    }

    public ApiResponse deleteSalary(String email, String month) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            Optional<User> optionalUser1 = userRepository.findByEmail(email);
            if (!optionalUser1.isPresent()) return new ApiResponse("Staff is not found", false);
            User salaryTaker = optionalUser1.get();

            Optional<PaidSalary> byOwnerAndPeriod = salaryRepository.findByOwnerAndPeriod(salaryTaker, Month.valueOf(month.toUpperCase()));

            if (!byOwnerAndPeriod.isPresent()) return new ApiResponse("Oylik mavjud emas!", false);

            if (byOwnerAndPeriod.get().isPaid())
                return new ApiResponse("Bu oylik allaqachon to'langan, uni o'zgartira olmaysiz!", false);

            salaryRepository.delete(byOwnerAndPeriod.get());

            return new ApiResponse("Salary deleted", true);
        }
        return new ApiResponse("You cant deleted salary", false);
    }

    public ApiResponse paidSalary(String email, String month, boolean isPaid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {

            Optional<User> optionalUser1 = userRepository.findByEmail(email);
            if (!optionalUser1.isPresent()) return new ApiResponse("Staff is not found", false);
            User salaryTaker = optionalUser1.get();

            Optional<PaidSalary> byOwnerAndPeriod = salaryRepository.findByOwnerAndPeriod(salaryTaker, Month.valueOf(month.toUpperCase()));

            if (!byOwnerAndPeriod.isPresent()) return new ApiResponse("Oylik mavjud emas!", false);

            if (byOwnerAndPeriod.get().isPaid())
                return new ApiResponse("Bu oylik allaqachon to'langan, uni o'zgartira olmaysiz!", false);

            PaidSalary paidSalary = byOwnerAndPeriod.get();
            paidSalary.setPaid(true);

            return new ApiResponse("Salary paid", true);
        }
        return new ApiResponse("You cant deleted salary", false);
    }

}
