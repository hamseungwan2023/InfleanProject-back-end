package inflearnproject.anoncom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResUserLoginDto {

    private String accessToken;
    private String refreshToken;

    private Long memberId;
    private String nickname;
}