package inflearnproject.anoncom.user.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReqUserUpdateDto {

    @Min(2)
    private String nickname;

    private String password;

    private String newPassword;
}
