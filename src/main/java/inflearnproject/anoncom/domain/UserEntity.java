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

    @OneToMany(mappedBy = "user")
    private List<PostReaction> postReactions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReComment> reComments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommentReaction> commentReactions = new ArrayList<>();

    private boolean isActive;
    public UserEntity(ReqUserJoinFormDto reqUserJoinFormDto){
        this.nickname = reqUserJoinFormDto.getNickname();
        this.username = reqUserJoinFormDto.getUsername();
        this.password = reqUserJoinFormDto.getPassword();
        this.email = reqUserJoinFormDto.getEmail();
        this.location = reqUserJoinFormDto.getLocation();
        this.info = reqUserJoinFormDto.getInfo();
        this.isActive = true;
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

    public void setActiveFalse() {
        this.isActive = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAdminInfo(){
        this.nickname = "adminNick";
        this.password = "adminPass";
        this.username = "adminUser";

    }
}
