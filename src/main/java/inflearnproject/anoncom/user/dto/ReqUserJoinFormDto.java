package inflearnproject.anoncom.user.dto;

import inflearnproject.anoncom.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqUserJoinFormDto {

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

    @Length(max=50)
    private String info;

    private String location;

    public ReqUserJoinFormDto(UserEntity userEntity){
        this.nickname = userEntity.getNickname();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.info = userEntity.getInfo();
        this.location = userEntity.getLocation();
    }
}
