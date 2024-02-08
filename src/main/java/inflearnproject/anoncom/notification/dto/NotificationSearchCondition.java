package inflearnproject.anoncom.notification.dto;

import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import lombok.Data;

@Data
public class NotificationSearchCondition {

    private PostCategory category;
    private LocationType location;
}
