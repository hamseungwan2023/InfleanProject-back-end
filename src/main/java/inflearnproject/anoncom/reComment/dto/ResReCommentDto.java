package inflearnproject.anoncom.reComment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResReCommentDto {

    private Long id; //대댓글 id

    private Long parentCommentId; //부모 댓글 id

    private String parentCommentNickname; //부모 댓글 닉네임

    private Long writerId; //대댓글 작성자 id
    private String writerNickname; //대댓글 작성자 닉네임
    private int rank; //대댓글 작성자 랭크
    private LocalDateTime createdAt; //대댓글 작성 날짜
    private String content; //대댓글 내용
    private boolean deleted;
    @QueryProjection
    public ResReCommentDto(Long id, Long parentCommentId, String parentCommentNickname, Long writerId,
                           String writerNickname, int rank, LocalDateTime createdAt, String content,boolean deleted) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.parentCommentNickname = parentCommentNickname;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.rank = rank;
        this.createdAt = createdAt;
        this.content = content;
        this.deleted = deleted;
    }
}
