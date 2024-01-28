package inflearnproject.anoncom.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;

    private boolean deleted; //삭제된 상태면 true, 삭제 안 된 상태면 false

    public void setReCommentInfo(UserEntity user, Post post, Comment comment){
        this.user = user;
        this.post = post;
        this.comment = comment;
    }

    public void addComment(Comment comment){
        this.comment = comment;
        comment.getReComments().add(this);
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void delete(){
        this.deleted = true;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
