package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResPostDetailDto {

    private Long id;
    private String title;
    private String category;

    private LocalDateTime createdAt;
    private UserEntity user;

    private int views;
    private int like;
    private int dislike;
    private int finalLike;

    private String content;

    public static ResPostDetailDto buildResPostDetailDto(Post post){
        return ResPostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .user(post.getUser())
                .views(post.getViews())
                .like(post.getUserLike())
                .dislike(post.getUserDisLike())
                .finalLike(post.getFinalLike())
                .content(post.getContent())
                .build();
    }
}
