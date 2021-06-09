package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.UserDto;
import ecma.ai.hrapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.ws.spi.http.HttpContext;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;


    //Yangi user qo'shish
    //MANAGER,DIREKTOR //PreAuthorize
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody UserDto userDto) throws MessagingException {
        ApiResponse response = userService.add(userDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    public HttpEntity<?> get() {
        ApiResponse response = userService.getUser();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
