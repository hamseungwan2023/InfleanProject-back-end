package inflearnproject.anoncom.post.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PostSearchCondition {

    private String category;

    @Min(2)
    private String content;

    @Min(2)
    private String title;
}
