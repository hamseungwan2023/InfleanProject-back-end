package inflearnproject.anoncom.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
@ToString(of = {"title","category","content","user","userLike","userDisLike","views"})
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

    private int views; //조회수
    //TODO 댓글 연관관계 매핑 및 댓글의 총 개수
    public void putUser(UserEntity user){
        this.user = user;
        user.getPosts().add(this);
    }
}
