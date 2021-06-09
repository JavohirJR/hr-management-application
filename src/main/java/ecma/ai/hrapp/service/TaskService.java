package ecma.ai.hrapp.service;

import ecma.ai.hrapp.component.Checker;
import ecma.ai.hrapp.component.MailSender;
import ecma.ai.hrapp.entity.Role;
import ecma.ai.hrapp.entity.Task;
import ecma.ai.hrapp.entity.User;
import ecma.ai.hrapp.entity.enums.RoleName;
import ecma.ai.hrapp.entity.enums.TaskStatus;
import ecma.ai.hrapp.payload.ApiResponse;
import ecma.ai.hrapp.payload.StatusDTO;
import ecma.ai.hrapp.payload.TaskDTO;
import ecma.ai.hrapp.repository.TaskRepository;
import ecma.ai.hrapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    Checker checker;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailSender mailSender;

    public ApiResponse add(TaskDTO taskDTO) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);

        User taskGiver = optionalUser.get();
        Set<Role> taskGiverRoles = taskGiver.getRoles();
        if (taskGiverRoles.contains(new Role(1, RoleName.ROLE_DIRECTOR)) ||
                taskGiverRoles.contains(new Role(2, RoleName.ROLE_MANAGER))) {

            Optional<User> taskTakerOptional = userRepository.findByEmail(taskDTO.getStaffEmail());
            if (!taskTakerOptional.isPresent())
                return new ApiResponse("The Staff you want to add task is not found!", false);

            User taskTaker = taskTakerOptional.get();
            if (!taskTaker.isEnabled()) return new ApiResponse("The Staff you want to add task is not found!", false);
            Set<Role> taskTakerRoles = taskTaker.getRoles();

            Task task = new Task();
            task.setName(taskDTO.getName());
            task.setTaskGiver(taskGiver);
            task.setDeadline(taskDTO.getDeadline());
            task.setTaskTaker(taskTaker);
            task.setDescription(taskDTO.getDescription());

            if (taskGiverRoles.contains(new Role(1, RoleName.ROLE_DIRECTOR))) {
                Task savedTask = taskRepository.save(task);
                mailSender.addTaskSendMail(taskTaker.getEmail(), savedTask.getId(), taskGiver.getUsername());
                return new ApiResponse("Task added successfully and email send", true);
            }

            if (taskGiverRoles.contains(new Role(2, RoleName.ROLE_MANAGER)) &&
                    (taskTakerRoles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                            || taskTakerRoles.contains(new Role(2, RoleName.ROLE_MANAGER))))
                return new ApiResponse("You cannot add task to this staff", false);
            Task savedTask = taskRepository.save(task);
            mailSender.addTaskSendMail(taskTaker.getEmail(), savedTask.getId(), taskGiver.getUsername());
            return new ApiResponse("Task added successfully and email send", true);
        }
        return new ApiResponse("You cannot add task", false);
    }

    public ApiResponse getMyTakenTasks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {
            List<Task> all = taskRepository.findAll();
            return new ApiResponse("All tasks list for director", true, all);
        }
        List<Task> byTaskTaker = taskRepository.findByTaskTaker(taskOwner);
        if (byTaskTaker.isEmpty()) return new ApiResponse("Tasks is not found!", false);
        return new ApiResponse("Success", true, byTaskTaker);
    }

    public ApiResponse getMyGivenTasks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();
        Set<Role> roles = taskOwner.getRoles();

        if (roles.contains(new Role(1, RoleName.ROLE_DIRECTOR))
                && roles.contains(new Role(2, RoleName.ROLE_MANAGER))
                && roles.contains(new Role(3, RoleName.ROLE_STAFF))) {
            List<Task> all = taskRepository.findAll();
            return new ApiResponse("All tasks list for director", true, all);
        }
        if (roles.contains(new Role(3, RoleName.ROLE_STAFF))) return new ApiResponse("You cant add task", false);

        List<Task> byTaskGiver = taskRepository.findByTaskGiver(taskOwner);
        if (byTaskGiver.isEmpty()) return new ApiResponse("Tasks is not found!", false);
        return new ApiResponse("Success", true, byTaskGiver);
    }

    public ApiResponse setStatus(UUID id, StatusDTO statusDTO) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);
        User taskOwner = optionalUser.get();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task is not found", false);
        Task task = optionalTask.get();

        if (!task.getTaskTaker().equals(taskOwner))
            return new ApiResponse("You cannot change task status", false);

        if (statusDTO.getStatus().equals("start")){
            task.setStatus(TaskStatus.IN_PROGRESS);
            Task savedTask = taskRepository.save(task);
            mailSender.startTaskSendMail(task.getTaskGiver().getEmail(), savedTask.getId(), taskOwner.getUsername());
            return new ApiResponse("Task status is changed to IN_PROGRESS ",true);
        }
        if (statusDTO.getStatus().equals("completed")){
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedDate(new Timestamp(System.currentTimeMillis()));
            Task savedTask = taskRepository.save(task);
            mailSender.completedTaskSendMail(task.getTaskGiver().getEmail(), savedTask.getId(), taskOwner.getUsername());
            return new ApiResponse("Task status is changed to COMPLETED ",true);
        }

        return new ApiResponse("Error", false);
    }

    public ApiResponse getTask(UUID id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse("User is not found", false);

        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(task -> new ApiResponse("You task ", true, task)).orElseGet(() -> new ApiResponse("Task is not found", false));
    }
}
