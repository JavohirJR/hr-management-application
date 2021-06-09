package ecma.ai.hrapp.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class TaskDTO {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Timestamp deadline;
    @NotNull
    @Email
    private String staffEmail;
}
