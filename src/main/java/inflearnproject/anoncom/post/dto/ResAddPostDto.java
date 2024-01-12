package inflearnproject.anoncom.post.dto;

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
    private Long userId;
    private int finalLike;

    public static ResAddPostDto buildResPostDto(UserEntity user, Post post){
        ResAddPostDto dto = ResAddPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .userId(user.getId())
                .finalLike(post.getUserLike() - post.getUserDisLike())
                .build();
        return dto;
    }
}
