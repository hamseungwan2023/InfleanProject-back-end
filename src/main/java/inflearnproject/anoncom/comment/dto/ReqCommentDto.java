package inflearnproject.anoncom.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqCommentDto {

    private Long id; //commentId

    private Long postId;

    private Long writerId;
    private String writerNickname;
    private int rank;

    private int like;
    private LocalDateTime createdAt;
    private String content;

    @QueryProjection
    public ReqCommentDto(Long id, Long postId, Long writerId, String writerNickname, int rank, int like, LocalDateTime createdAt, String content){
        this.id = id;
        this.postId = postId;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.rank = rank;
        this.like = like;
        this.createdAt = createdAt;
        this.content = content;
    }
}
