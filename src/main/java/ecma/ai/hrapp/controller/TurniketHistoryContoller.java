package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.TurniketHistoryDto;
import ecma.ai.hrapp.service.TurniketHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Timestamp;

@RestController
@RequestMapping("/turniketHistory")
public class TurniketHistoryContoller {

    @Autowired
    TurniketHistoryService turniketHistoryService;

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody TurniketHistoryDto turniketHistoryDto){
        ApiResponse response = turniketHistoryService.add(turniketHistoryDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping("/getAllByDate")
    public HttpEntity<?> editSalary(@RequestParam Timestamp startTime, @RequestParam Timestamp endTime, @RequestParam String number) throws MessagingException {
        ApiResponse response = turniketHistoryService.getAllByDate(number, startTime, endTime);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> deleteSalary(@RequestParam String number){
        ApiResponse response = turniketHistoryService.getAll(number);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
