package inflearnproject.anoncom.email.dto;

import lombok.Data;

@Data
public class EmailVerificationDto {

    private String email;
    private String code;
}
