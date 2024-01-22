package inflearnproject.anoncom.post.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PostSearchCondition {

    private String category;

    private String content;

    private String title;
}
