package ecma.ai.hrapp.payload;

import ecma.ai.hrapp.entity.enums.Month;
import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class PaidSalaryDTO {
    @Email
    @NotNull
    private String userEmail;

    @NotNull
    private double amount;

    @Enumerated
    private Month month;
}
