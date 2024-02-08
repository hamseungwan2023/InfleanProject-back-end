package inflearnproject.anoncom.notification.dto;

import inflearnproject.anoncom.domain.Notification;
import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationShortDto {
    private Long id;
    private String title;
    private PostCategory category;
    private LocationType location;

    public NotificationShortDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.category = notification.getCategory();
        this.location = notification.getLocation();
    }
}
