package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResPostDto {

    private Long id;
    private String title;
    private String category;

    private LocalDateTime createdAt;
    private UserEntity user;
    private int finalLike;

    public ResPostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.user = post.getUser();
        this.finalLike = post.getFinalLike();
    }
}
