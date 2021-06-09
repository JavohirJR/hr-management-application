package ecma.ai.hrapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Component
public class MailSender {

    @Autowired
    JavaMailSender mailSender;

    public boolean send(String to, String text) throws MessagingException {

        String from = "pdp@gmail.com";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setSubject("Information");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(text);
        mailSender.send(mimeMessage);
        return true;
    }


    public boolean mailTextAddStaff(String email, String code, String pass) throws MessagingException {
        String link = "http://localhost:8080/api/user/verifyEmail?email=" + email + "&code=" + code;

        String text = link + "\n" +
                "Emailni tasdiqlash\n" +
                "Parolingiz: " + pass ;

        return send(email, text);
    }

    public boolean addTaskSendMail(String email, UUID taskId, String taskGiverName) throws MessagingException {
        String link = "http://localhost:8080/api/task/" + taskId;
        String text = "You have been given a task called " + taskGiverName +" \n"
                +link;
        return send(email, text);
    }

    public boolean startTaskSendMail(String email, UUID taskId, String taskTakerName) throws MessagingException {
        String link = "http://localhost:8080/api/task/" + taskId;
        String text = taskTakerName + " started your task "
                +link;
        return send(email, text);
    }


    public boolean completedTaskSendMail(String email, UUID taskId, String taskTakerName) throws MessagingException {
        String link = "http://localhost:8080/api/task/" + taskId;
        String text = taskTakerName + " completed your task "
                +link;
        return send(email, text);
    }

    public boolean mailTextTurniketStatus(String email, boolean enabled) throws MessagingException {
        String stat = "Disabled";
        if (enabled) stat = "Enabled!";
        String text = "Attention! Turniket status has changed. Current status: <b>" + stat + " </b>";
        return send(email, text);
    }
}
