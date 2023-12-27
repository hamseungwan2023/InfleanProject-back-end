package inflearnproject.anoncom.domain;

import inflearnproject.anoncom.user.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter
public class UserEntity extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String username;

    private String password;

    private LocalDateTime joinedAt;

    private String location;

    public UserEntity(UserFormDto userFormDto){
        this.nickname = userFormDto.getNickname();
        this.username = userFormDto.getUsername();
        this.password = userFormDto.getPassword();
        this.email = userFormDto.getEmail();
    }

    public void changeToBCryptPassword(String bcryptPassword) {
        this.password = bcryptPassword;
    }
}
