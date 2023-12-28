package inflearnproject.anoncom.refreshToken.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotEmpty
    String refreshToken;
}
