package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResPostDetailDto {

    private Long id;
    private String title;
    private PostCategory category;

    private LocalDateTime createdAt;
    private Long writeId;
    private String writeNickname;
    private int rank;
    private boolean writerActive;

    private int views;
    private int like;
    private int dislike;
    private int finalLike;

    private String content;
    private int contentCount;
    private LocationType location;

    public ResPostDetailDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.writeId = post.getUser().getId();
        this.writeNickname = post.getUser().getNickname();
        this.rank = post.getUser().getRank();
        this.writerActive = post.getUser().isActive();
        this.views = post.getViews();
        this.like = post.getUserLike();
        this.dislike = post.getUserDisLike();
        this.content = post.getContent();
        this.finalLike = post.getFinalLike();
        this.contentCount = post.getCommentCount();
        this.location = post.getLocation();
    }
}
