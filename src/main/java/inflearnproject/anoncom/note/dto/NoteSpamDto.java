package inflearnproject.anoncom.note.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteSpamDto {

    private List<Long> spamNotes = new ArrayList<>();
}
