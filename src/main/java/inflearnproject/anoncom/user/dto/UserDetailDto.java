package inflearnproject.anoncom.user.dto;

import inflearnproject.anoncom.domain.UserEntity;
import lombok.Data;

@Data
public class UserDetailDto {

    private String nickname;
    private String location;
    private int rank;

    public UserDetailDto(UserEntity user){
        this.nickname = user.getNickname();
        this.location = user.getLocation();
        this.rank = user.getRank();
    }
}
