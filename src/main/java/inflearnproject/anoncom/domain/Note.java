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
public class Note extends BaseCreatedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    private String content;

    private boolean isReceiverRead; // 수신자 읽음 여부

    private boolean isSenderDelete; //발신자 삭제 여부

    private boolean isReceiverDelete; //수신자 삭제 여부

    private boolean isSpam; //스팸 여부

    private boolean isDeclaration; //신고 여부

    private boolean isKeep; //보관 여부
}
