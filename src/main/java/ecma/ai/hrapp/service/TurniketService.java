package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.Company;
import ecma.ai.hrapp.entity.Turniket;
import ecma.ai.hrapp.payload.TurniketDto;
import ecma.ai.hrapp.repository.CompanyRepository;
import ecma.ai.hrapp.repository.TuniketRepository;
import ecma.ai.hrapp.repository.UserRepository;
import ecma.ai.hrapp.security.JwtProvider;
import org.springframework.stereotype.Service;
import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketService {
    @Autowired
    TuniketRepository tuniketRepository;

    @Autowired
    Checker checker;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MailSender mailSender;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(TurniketDto turniketDto) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();


        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))
                || roles.contains(new Role(2, RoleName.ROLE_MANAGER))) {

            Optional<Company> optionalCompany = companyRepository.findById(turniketDto.getCompanyId());
            if (!optionalCompany.isPresent())
                return new ApiResponse("Company not found!", false);

            Turniket turniket = new Turniket();
            turniket.setCompany(optionalCompany.get());
            turniket.setOwner(taskOwner);
            assert !turniketDto.isEnabled();
            turniket.setEnabled(turniketDto.isEnabled());
            Turniket saved = tuniketRepository.save(turniket);
            mailSender.mailTextTurniketStatus(saved.getOwner().getEmail(), saved.isEnabled());
            return new ApiResponse("Turniket succesfully created!", true);
        }
        return new ApiResponse("You haven't got permission to add turnikey",false);
    }

    //TURNIKETNING HUQUQINI O'ZGARTIRISH MUMKIN
    public ApiResponse edit(String number, TurniketDto turniketDto) throws MessagingException {
        Optional<Turniket> optionalTurniket = tuniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Turniket not found!", false);

        Turniket turniket = optionalTurniket.get();
        turniket.setEnabled(turniketDto.isEnabled());
        Turniket saved = tuniketRepository.save(turniket);
        mailSender.mailTextTurniketStatus(saved.getOwner().getEmail(), saved.isEnabled());
        return new ApiResponse("Turniket succesfully edited!", true);
    }

    public ApiResponse delete(String number) {
        Optional<Turniket> optionalTurniket = tuniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("Turniket not found!", false);


        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = null;
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }
        boolean check = checker.check(role);

        if (!check)
            return new ApiResponse("You have no such right!", false);

        tuniketRepository.delete(optionalTurniket.get());
        return new ApiResponse("Turniket deleted!", true);
    }

}
