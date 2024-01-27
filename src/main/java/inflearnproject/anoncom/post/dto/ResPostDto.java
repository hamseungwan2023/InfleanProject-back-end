package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResPostDto {

    private Long id;
    private String title;
    private String category;

    private LocalDateTime createdAt;
    private String writer;
    private boolean writerActive;
    private int rank;
    private int finalLike;
    private int contentCount;
    private String location;
    public ResPostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.writer = post.getUser().getNickname();
        this.writerActive = post.getUser().isActive();
        this.rank = post.getUser().getRank();
        this.finalLike = post.getFinalLike();
        this.contentCount = post.getComments().size();
        this.location = post.getLocation();
    }
}
