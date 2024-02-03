package inflearnproject.anoncom.domain;

import inflearnproject.anoncom.enumType.LocationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(of = {"id", "email", "nickname", "username", "password", "roles"})
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationType location;

    private String info;

    private String profileImg;

    @Column(name = "userRank")
    private int rank;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<PostReaction> postReactions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<ReComment> reComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<CommentReaction> commentReactions = new ArrayList<>();

    private boolean isActive;

    private LocalDateTime blockUntil;

    private Boolean isBlocked;

    public void changeToBCryptPassword(String bcryptPassword) {
        this.password = bcryptPassword;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateRank() {
        this.rank = this.rank + 1;
    }

    public void setActiveFalse() {
        this.isActive = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void blockUser() {
        this.isBlocked = true;
        this.blockUntil = LocalDateTime.now().plusDays(7);
    }

    public void blockFalse() {
        this.isBlocked = false;
    }

    public void setRandomPassword(String password) {
        this.password = password;
    }
}
