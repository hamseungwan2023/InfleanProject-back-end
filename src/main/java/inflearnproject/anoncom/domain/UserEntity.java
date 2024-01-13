package inflearnproject.anoncom.domain;

import inflearnproject.anoncom.user.dto.ReqUserJoinFormDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter
@ToString(of = {"id","email","nickname","username","password","roles"})
public class UserEntity extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String username;

    private String password;

    private String location;

    private String info;

    private String profileImg;

    private int rank;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    public UserEntity(ReqUserJoinFormDto reqUserJoinFormDto){
        this.nickname = reqUserJoinFormDto.getNickname();
        this.username = reqUserJoinFormDto.getUsername();
        this.password = reqUserJoinFormDto.getPassword();
        this.email = reqUserJoinFormDto.getEmail();
        this.location = reqUserJoinFormDto.getLocation();
        this.info = reqUserJoinFormDto.getInfo();
    }

    public void changeToBCryptPassword(String bcryptPassword) {
        this.password = bcryptPassword;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void setProfileImg(String profileImg){
        this.profileImg = profileImg;
    }

    public void updateRank(){
        this.rank = this.posts.size();
    }
}
