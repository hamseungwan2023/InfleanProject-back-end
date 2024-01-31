package inflearnproject.anoncom.domain;

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
public class Spam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spam_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaring_id")
    private UserEntity declaring;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declared_id")
    private UserEntity declared;
}
