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
public class ResPostDto {

    private long id;
    private String title;
    private String category;
    private LocalDateTime createdAt;
    private UserEntity user;
    private int finalLike;

    public static ResPostDto buildResPostDto(UserEntity user, Post post){
        ResPostDto dto = ResPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .user(user)
                .finalLike(post.getUserLike() - post.getUserDisLike())
                .build();
        return dto;
    }
}
