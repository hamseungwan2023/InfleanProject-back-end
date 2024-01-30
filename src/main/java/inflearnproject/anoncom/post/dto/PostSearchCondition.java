package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.user.util.ConditionalNotBlank;
import lombok.Data;

@Data
public class PostSearchCondition {

    private String category;

    @ConditionalNotBlank(min = 2)
    private String content;

    @ConditionalNotBlank(min = 2)
    private String title;

    private String location;

    @ConditionalNotBlank(min = 2)
    private String titleContent;

    private String orderBy;


}
