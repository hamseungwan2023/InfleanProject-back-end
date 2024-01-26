package inflearnproject.anoncom.declareNote.dto;

import inflearnproject.anoncom.declareNote.repository.DeclareNoteRepository;
import inflearnproject.anoncom.domain.DeclareNote;
import inflearnproject.anoncom.domain.Note;
import lombok.Data;

@Data
public class DeclareShowDto {

    private Long id;
    private String content;
    private String declaringUserNickname;
    private String declaredUserNickname;

    public DeclareShowDto(DeclareNote declareNote){
        this.id = declareNote.getId();
        this.content = declareNote.getNote().getContent();
        this.declaringUserNickname = declareNote.getNote().getReceiver().getNickname();
        this.declaredUserNickname = declareNote.getNote().getSender().getNickname();
    }
}
