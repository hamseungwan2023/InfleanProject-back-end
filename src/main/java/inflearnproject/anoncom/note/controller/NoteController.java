package inflearnproject.anoncom.note.controller;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.note.dto.NoteAddDto;
import inflearnproject.anoncom.note.dto.NoteDeleteDto;
import inflearnproject.anoncom.note.dto.NoteShowDto;
import inflearnproject.anoncom.note.service.NoteService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/api/noteWrite")
    public ResponseEntity<List<String>> sendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteAddDto noteDto){
        List<String> erroredList = noteService.addNote(userDto, noteDto);
        return ResponseEntity.ok().body(erroredList);
    }

    @DeleteMapping("/api/sendNoteDelete")
    public ResponseEntity<List<Long>> deleteSendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto){
        List<Long> erroredList = noteService.deleteSendNote(noteDto);
        return ResponseEntity.ok().body(erroredList);
    }

    @DeleteMapping("/api/receiveNoteDelete")
    public ResponseEntity<List<Long>> deleteReceiveNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto){
        List<Long> erroredList = noteService.deleteReceiveNote(noteDto);
        return ResponseEntity.ok().body(erroredList);
    }

    @GetMapping("/api/noteReadList")
    public ResponseEntity<?> showReceiverNotes(@IfLogin LoginUserDto userDto,@RequestParam(value = "page", defaultValue = "0") int page){
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteShowDto> notesDto = noteService.findNotes(userDto.getMemberId(), pageable);
        return ResponseEntity.ok().body(notesDto);
    }

}
