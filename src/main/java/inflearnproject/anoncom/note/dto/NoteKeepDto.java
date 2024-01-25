package inflearnproject.anoncom.note.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteKeepDto {

    private List<Long> noteKeeps = new ArrayList<>();
}
