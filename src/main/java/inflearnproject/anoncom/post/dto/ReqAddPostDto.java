package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqAddPostDto {

    private String title;

    private String category;

    private String content;


    public static Post buildPost(ReqAddPostDto postDto){
        return Post.builder()
                .title(postDto.getTitle())
                .category(postDto.getCategory())
                .content(postDto.getContent())
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .build();
    }
}
