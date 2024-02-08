package inflearnproject.anoncom.domain;

import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.enumType.PostCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private PostCategory category;

    private String content;

    @Enumerated(EnumType.STRING)
    private LocationType location;

}
