package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqAddPostDto {

    @Length(min = 2)
    private String title;

    @NotNull
    private String category;

    @Length(min = 10)
    private String content;


    public static Post buildPost(ReqAddPostDto postDto) {
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
