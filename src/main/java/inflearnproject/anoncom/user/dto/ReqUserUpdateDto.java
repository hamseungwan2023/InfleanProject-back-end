package inflearnproject.anoncom.user.dto;

import inflearnproject.anoncom.user.util.ConditionalNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReqUserUpdateDto {

    @NotBlank
    @Length(min = 4, max = 10)
    private String nickname;

    @ConditionalNotBlank
    private String password;
    
    @ConditionalNotBlank
    private String newPassword;
}
