package inflearnproject.anoncom.note.controller;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.note.dto.*;
import inflearnproject.anoncom.note.service.NoteService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
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
    private final int pageSize = 5;

    @PostMapping("/api/noteWrite")
    public ResponseEntity<List<String>> sendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteAddDto noteDto) {
        List<String> erroredList = noteService.addNote(userDto, noteDto);
        return ResponseEntity.ok().body(erroredList);
    }

    @DeleteMapping("/api/sendNoteDelete")
    public ResponseEntity<?> deleteSendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto) {
        noteService.deleteSendNote(noteDto);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/api/receiveNoteDelete")
    public ResponseEntity<?> deleteReceiveNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto) {
        noteService.deleteReceiveNote(noteDto);
        return ResponseEntity.ok().body("ok");
    }

    /**
     * 받은 쪽지함 보여주기
     */
    @GetMapping("/api/noteReadReceivedList")
    public ResponseEntity<Page<NoteShowDto>> showReceiverNotes(@IfLogin LoginUserDto userDto, @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @ModelAttribute(value = "cond") NoteSearchCond cond) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteShowDto> notesDto = noteService.findReceivedNotes(userDto.getMemberId(), cond, pageable);
        return ResponseEntity.ok().body(notesDto);
    }

    /**
     * 내가 보낸 쪽지들 보여주기
     */
    @GetMapping("/api/noteReadSendedList")
    public ResponseEntity<Page<NoteSendedShowDto>> showSendNotes(@IfLogin LoginUserDto userDto, @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @ModelAttribute(value = "cond") NoteSearchCond cond) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteSendedShowDto> notesDto = noteService.findSendedNotes(userDto.getMemberId(), pageable);
        return ResponseEntity.ok().body(notesDto);
    }

    //TODO noteDetailDto 필드 추가하기
    @GetMapping("/api/noteRead/{noteId}")
    public ResponseEntity<NoteDetailDto> showNoteDetail(@IfLogin LoginUserDto userDto, @PathVariable("noteId") Long noteId) {
        Note note = noteService.findById(noteId);
        NoteDetailDto noteDetailDto = new NoteDetailDto(note);
        return ResponseEntity.ok().body(noteDetailDto);
    }

    @PostMapping("/api/keepNote")
    public ResponseEntity<?> keepNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteKeepDto noteKeepDto) {
        noteService.keepNote(noteKeepDto);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/api/spamNote")
    public ResponseEntity<?> spamNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteSpamDto noteSpamDto) {
        noteService.spamNote(userDto.getMemberId(), noteSpamDto);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/api/declareNote")
    public ResponseEntity<?> declareNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteDeclareDto noteDeclareDto) {
        noteService.declareNote(noteDeclareDto);
        return ResponseEntity.ok().body("ok");
    }
}
