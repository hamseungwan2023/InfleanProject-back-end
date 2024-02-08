package inflearnproject.anoncom.notification.dto;

import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAddDto {

    private Long id;
    private String title;
    private PostCategory category;
    private String content;
    private LocationType location;

}
