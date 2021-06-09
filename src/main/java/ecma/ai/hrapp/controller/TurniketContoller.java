package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketDto;
import ecma.ai.hrapp.service.TurniketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/turniket")
public class TurniketContoller {

    @Autowired
    TurniketService turniketService;

    @PostMapping("/add")
    public HttpEntity<?> addTurniket(@RequestBody TurniketDto turniketDto) throws MessagingException {
        ApiResponse response = turniketService.add(turniketDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @PutMapping("/edit")
    public HttpEntity<?> editSalary(@RequestBody TurniketDto turniketDto, @RequestParam String number) throws MessagingException {
        ApiResponse response = turniketService.edit(number, turniketDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/delete")
    public HttpEntity<?> deleteSalary(@RequestParam String number){
        ApiResponse response = turniketService.delete(number);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
