package inflearnproject.anoncom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDeleteFormDto {

    private String username;
    private String password;
}
