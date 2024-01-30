package inflearnproject.anoncom.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id")
    private Long id;

    private Long userEntityId;

    private String tokenValue;
}
