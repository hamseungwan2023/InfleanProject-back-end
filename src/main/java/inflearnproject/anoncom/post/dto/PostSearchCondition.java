package inflearnproject.anoncom.post.dto;

import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import inflearnproject.anoncom.user.util.ConditionalNotBlank;
import lombok.Data;

@Data
public class PostSearchCondition {

    private PostCategory category;

    @ConditionalNotBlank(min = 2)
    private String content;

    @ConditionalNotBlank(min = 2)
    private String title;

    private LocationType location;

    @ConditionalNotBlank(min = 2)
    private String titleContent;

    private String orderBy;


}
