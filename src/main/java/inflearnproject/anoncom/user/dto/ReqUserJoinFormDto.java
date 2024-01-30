package inflearnproject.anoncom.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqUserJoinFormDto {

    @NotBlank
    @Length(min = 4, max = 10)
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 10)
    private String username;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Email
    private String email;

    @Length(max = 50)
    private String info;

    private String location;

}
