package inflearnproject.anoncom.user.dto;

import inflearnproject.anoncom.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFormDto {

    @NotBlank
    @Length(min = 4,max = 10)
    private String nickname;

    @NotBlank
    @Length(min = 8,max = 10)
    private String username;

    @NotBlank
    @Length(min = 8,max = 20)
    private String password;

    @NotBlank
    @Email
    private String email;

    public UserFormDto(UserEntity userEntity){
        this.nickname = userEntity.getNickname();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
    }
}
