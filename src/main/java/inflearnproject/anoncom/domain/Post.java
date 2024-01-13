package inflearnproject.anoncom.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@ToString(of = {"id","title","category","content","user","userLike","userDisLike","views"})
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String category;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private int userLike; //좋아요

    private int userDisLike; //싫어요

    //TODO : 나중에 수정
    private int finalLike = userLike - userDisLike; //좋아요 - 싫어요

    private int views; //조회수

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    // 댓글 개수를 저장하지 않는 필드
    @Transient
    private int commentCount;

    public int returnCommentCount() {
        return comments.size();
    }

    public void putUser(UserEntity user){
        this.user = user;
        user.getPosts().add(this);
    }

    public void updateValues(String title, String category, String content){
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
