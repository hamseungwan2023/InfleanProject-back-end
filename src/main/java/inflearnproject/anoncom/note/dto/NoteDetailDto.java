package inflearnproject.anoncom.note.dto;

import inflearnproject.anoncom.domain.Note;
import lombok.Data;

@Data
public class NoteDetailDto {

    private String nickname;
    private String content;

    public NoteDetailDto(Note note) {
        this.nickname = note.getSender().getNickname();
        this.content = note.getContent();
    }
}
