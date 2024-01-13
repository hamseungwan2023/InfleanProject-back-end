package inflearnproject.anoncom.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResAddPostDto {

    private long id;
    private String title;
    private String category;
    private LocalDateTime createdAt;
    private UserEntity user;
    private int finalLike;
    private String content;

    public static ResAddPostDto buildResPostDto(UserEntity user, Post post){
        ResAddPostDto dto = ResAddPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .user(user)
                .finalLike(post.getUserLike() - post.getUserDisLike())
                .content(post.getContent())
                .build();
        return dto;
    }

    @QueryProjection
    public ResAddPostDto(Long id, String title, String category, LocalDateTime createdAt, UserEntity user, int finalLike, String content){
        this.id = id;
        this.title = title;
        this.category = category;
        this.createdAt = createdAt;
        this.user = user;
        this.finalLike = finalLike;
        this.content = content;
    }
}
