package ecma.ai.hrapp.component.checkPositionPayload;

import ecma.ai.hrapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPosition {
    private User user;
    private String position;
}
