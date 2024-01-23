package inflearnproject.anoncom.note.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteAddDto {

    @Min(2)
    private String content;

    private List<String> receiverNicknames = new ArrayList<>();
}
