package ecma.ai.hrapp.service;

import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyAuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public ApiResponse verifyEmail(String email, String verifyCode) {
        Optional<User> optionalUser = userRepository.findByEmailAndVerifyCode(email, verifyCode);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            user.setEnabled(true);
            user.setVerifyCode(null);
            userRepository.save(user);
            return new ApiResponse("Account is verified", true);
        }
        return new ApiResponse("Verify code is already verified", false);
    }
}
