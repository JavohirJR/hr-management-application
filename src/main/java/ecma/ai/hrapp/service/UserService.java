package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.component.PasswordGenerator;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.repository.RoleRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Checker checker;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;

    public ApiResponse add(UserDto userDto) throws MessagingException {
        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse("Role id not found!", false);

        boolean check = checker.check(optionalRole.get().getName().name());//

        if (!check) {
            return new ApiResponse("Dostup netu!", false);
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse("Already exists!", false);
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPosition(userDto.getPosition());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        user.setRoles(roleSet);

        String password = passwordGenerator.genRandomPassword(8);
        user.setPassword(passwordEncoder.encode(password));

        String code = UUID.randomUUID().toString();
        user.setVerifyCode(code);

        userRepository.save(user);

        //mail xabar yuborish kk
        boolean addStaff = mailSender.mailTextAddStaff(userDto.getEmail(), code, password);

        if (addStaff) {
            return new ApiResponse("User qo'shildi! va emailga xabar ketdi!", true);
        } else {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public ApiResponse getUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User owner = optionalUser.get();

        Set<Role> roles = owner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))
                ||owner.getPosition().contains("hr".toUpperCase())) {
            List<User> all = userRepository.findAll();
            return new ApiResponse("All users list for director", true, all);
        }
        return new ApiResponse("Error", false);
    }
}
