package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.PaidSalaryDTO;
import ecma.ai.hrapp.service.PaidSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.StringJoiner;

@RestController
@RequestMapping("/paid")
public class PaidSalaryContoller {

    @Autowired
    PaidSalaryService paidSalaryService;

    @PostMapping("/add")
    public HttpEntity<?> addSalary(@RequestBody PaidSalaryDTO paidSalaryDTO){
        ApiResponse response = paidSalaryService.addSalary(paidSalaryDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping()
    public HttpEntity<?> getSalaryByUser(@RequestParam String email){
        ApiResponse response = paidSalaryService.getSalaryBuUser(email);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping()
    public HttpEntity<?> getSalaryByMonth(@RequestParam String month){
        ApiResponse response = paidSalaryService.getSalaryByMonth(month);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/edit")
    public HttpEntity<?> editSalary(@RequestBody PaidSalaryDTO paidSalaryDTO, @RequestParam String email){
        ApiResponse response = paidSalaryService.editSalary(email, paidSalaryDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/delete")
    public HttpEntity<?> deleteSalary(@RequestParam String email, @RequestParam String month){
        ApiResponse response = paidSalaryService.deleteSalary(email, month);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/editPaid")
    public HttpEntity<?> paidSalary(@RequestParam String email, @RequestParam String month, @RequestParam boolean isPaid){
        ApiResponse response = paidSalaryService.paidSalary(email, month, isPaid);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
