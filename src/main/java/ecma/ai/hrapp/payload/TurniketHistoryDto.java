package ecma.ai.hrapp.payload;

import ecma.ai.hrapp.entity.enums.TurniketType;
import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class TurniketHistoryDto {
    @NotNull
    private String number;

    @NotNull
    @Enumerated
    private TurniketType type;
}
