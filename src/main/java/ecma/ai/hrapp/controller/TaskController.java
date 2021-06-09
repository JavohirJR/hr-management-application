package ecma.ai.hrapp.controller;

import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.StatusDTO;
import ecma.ai.hrapp.payload.TaskDTO;
import ecma.ai.hrapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public HttpEntity<?> addTask(@Valid @RequestBody TaskDTO taskDTO) throws MessagingException {
        ApiResponse add = taskService.add(taskDTO);
            return ResponseEntity.status(add.isSuccess() ? 200 : 409).body(add);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getTask(@PathVariable UUID id){
        ApiResponse response = taskService.getTask(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/allTasker")
    public HttpEntity<?> getAllTaker(){
        ApiResponse response = taskService.getMyTakenTasks();
            return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
    @GetMapping("/allGiver")
    public HttpEntity<?> getAllGiver(){
        ApiResponse response = taskService.getMyGivenTasks();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PatchMapping("/setStatus/{id}")
    public HttpEntity<?> changeStatus(@PathVariable UUID id, @RequestBody StatusDTO statusDTO) throws MessagingException {
        ApiResponse response = taskService.setStatus(id, statusDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
