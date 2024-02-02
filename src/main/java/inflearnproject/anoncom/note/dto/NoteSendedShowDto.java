package inflearnproject.anoncom.note.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteSendedShowDto {

    private Long id;

    private Long receiverId;
    private String receiverNickname;
    private String content;

    private LocalDateTime sendDate;
    private LocalDateTime receiveDate;
    private boolean isReceiverRead;

    @QueryProjection
    public NoteSendedShowDto(Long id, Long receiverId, String receiverNickname, String content, LocalDateTime sendDate, LocalDateTime receiveDate, boolean isReceiverRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.receiverNickname = receiverNickname;
        this.content = content;
        this.sendDate = sendDate;
        this.receiveDate = null;
        this.isReceiverRead = isReceiverRead;
    }
}
