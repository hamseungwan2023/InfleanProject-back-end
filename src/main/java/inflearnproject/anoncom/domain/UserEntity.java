package inflearnproject.anoncom.domain;

import inflearnproject.anoncom.user.dto.UserJoinFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    private String location;

    @ManyToMany
    @JoinTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    public UserEntity(UserJoinFormDto userJoinFormDto){
        this.nickname = userJoinFormDto.getNickname();
        this.username = userJoinFormDto.getUsername();
        this.password = userJoinFormDto.getPassword();
        this.email = userJoinFormDto.getEmail();
    }

    public void changeToBCryptPassword(String bcryptPassword) {
        this.password = bcryptPassword;
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
