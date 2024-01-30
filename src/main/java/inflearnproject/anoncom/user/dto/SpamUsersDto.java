package inflearnproject.anoncom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpamUsersDto {

    private Long userId;
    private String nickname;

}
