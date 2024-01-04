package inflearnproject.anoncom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor  @Builder
public class UserDeleteFormDto {

    private String username;
    private String password;
}
