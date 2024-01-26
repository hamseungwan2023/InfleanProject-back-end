package inflearnproject.anoncom.email.dto;

import lombok.Data;

@Data
public class EmailResetPasswordDto {

    private String email;
    private String username;
    private String code;
}
