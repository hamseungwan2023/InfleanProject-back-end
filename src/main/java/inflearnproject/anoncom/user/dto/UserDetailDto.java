package inflearnproject.anoncom.user.dto;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.enumType.LocationType;
import lombok.Data;

@Data
public class UserDetailDto {

    private String nickname;
    private LocationType location;
    private int rank;

    public UserDetailDto(UserEntity user) {
        this.nickname = user.getNickname();
        this.location = user.getLocation();
        this.rank = user.getRank();
    }
}
