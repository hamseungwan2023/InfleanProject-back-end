package inflearnproject.anoncom.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResUserLoginDto {

    private String accessToken;
    private String refreshToken;

    private Long memberId;
    private String nickname;
    private int postsCount;
}