package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.LoginDto;
import ecma.ai.hrapp.security.JwtProvider;
import ecma.ai.hrapp.service.MyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@Valid @RequestParam String email, @RequestParam String code){
        ApiResponse apiResponse = myAuthService.verifyEmail(email, code);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    @PostMapping("/login")
    public HttpEntity<?> logInToSystem(@RequestBody LoginDto loginDto) {

        try {
            //shunaqa tizim odami bormi tekshirish
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User user = (User)authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Login yoki Parolz notogri!");
        }

    }
}
