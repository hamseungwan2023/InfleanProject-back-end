package inflearnproject.anoncom.note.dto;

import inflearnproject.anoncom.domain.Note;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDetailDto {

    private String nickname;
    private String content;
    private LocalDateTime readDate;

    public NoteDetailDto(Note note) {
        this.nickname = note.getSender().getNickname();
        this.content = note.getContent();
        this.readDate = note.getReceiveDate();
    }
}
