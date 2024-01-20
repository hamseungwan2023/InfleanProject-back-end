package inflearnproject.anoncom.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Comment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReComment> reComments = new ArrayList<>();

    private int userLike;

    private int userDisLike;

    private String content;

    private boolean deleted; //삭제된 상태면 true, 삭제 안 된 상태면 false
    public void putUserPost(UserEntity user, Post post){
        this.user = user;
        this.post = post;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void delete(){
        this.deleted = true;
    }

    public boolean isOwnedBy(Long userId) {
        return this.user != null && userId.equals(this.user.getId());
    }

    public void addUserLike(){
        userLike++;
    }

    public void addUserDisLike(){
        userDisLike++;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }
}
