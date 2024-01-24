package inflearnproject.anoncom.note.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteDeleteDto {

    private List<Long> deleteNoteIds = new ArrayList<>();
}
