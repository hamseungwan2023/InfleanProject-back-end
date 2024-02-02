package inflearnproject.anoncom.note.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteShowDto {

    private Long id;

    private Long senderId;
    private String senderNickname;
    private String content;

    private LocalDateTime sendDate;
    private LocalDateTime receiveDate;
    private boolean isReceiverRead;

    @QueryProjection
    public NoteShowDto(Long id, Long senderId, String senderNickname, String content, LocalDateTime sendDate, LocalDateTime receiveDate, boolean isReceiverRead) {
        this.id = id;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.sendDate = sendDate;
        this.receiveDate = receiveDate;
        this.isReceiverRead = isReceiverRead;
    }
}
