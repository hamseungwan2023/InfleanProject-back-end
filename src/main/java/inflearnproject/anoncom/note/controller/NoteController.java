package inflearnproject.anoncom.note.controller;

import inflearnproject.anoncom.note.dto.NoteAddDto;
import inflearnproject.anoncom.note.service.NoteService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/api/noteWrite")
    public ResponseEntity<?> sendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteAddDto noteDto){
        List<String> erroredList = noteService.addNote(userDto, noteDto);
        return ResponseEntity.ok().body(erroredList);
    }
}
