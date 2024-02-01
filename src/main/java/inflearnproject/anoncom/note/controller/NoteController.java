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

    @PostMapping("/api/noteWrite")
    public ResponseEntity<List<String>> sendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteAddDto noteDto) {
        List<String> erroredList = noteService.addNote(userDto, noteDto);
        return ResponseEntity.ok().body(erroredList);
    }

    @DeleteMapping("/api/sendNoteDelete")
    public ResponseEntity<List<Long>> deleteSendNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto) {
        List<Long> nonMatchingIds = noteService.deleteSendNote(userDto.getMemberId(), noteDto);
        return ResponseEntity.ok().body(nonMatchingIds);
    }

    @DeleteMapping("/api/receiveNoteDelete")
    public ResponseEntity<List<Long>> deleteReceiveNote(@IfLogin LoginUserDto userDto, @RequestBody NoteDeleteDto noteDto) {
        List<Long> nonMatchingIds = noteService.deleteReceiveNote(userDto.getMemberId(), noteDto);
        return ResponseEntity.ok().body(nonMatchingIds);
    }

    /**
     * 받은 쪽지함 보여주기
     */
    @GetMapping("/api/noteReadReceivedList")
    public ResponseEntity<Page<NoteShowDto>> showReceiverNotes(@IfLogin LoginUserDto userDto, @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @ModelAttribute(value = "cond") NoteSearchCond cond) {
        int pageSize = cond.getUnit() == null ? 15 : cond.getUnit();

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
        int pageSize = cond.getUnit() == null ? 15 : cond.getUnit();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteSendedShowDto> notesDto = noteService.findSendedNotes(userDto.getMemberId(), pageable);
        return ResponseEntity.ok().body(notesDto);
    }

    //TODO noteDetailDto 필드 추가하기
    @GetMapping("/api/noteRead/{noteId}")
    public ResponseEntity<NoteDetailDto> showNoteDetail(@IfLogin LoginUserDto userDto, @PathVariable("noteId") Long noteId) {
        Note note = noteService.findById(userDto.getMemberId(), noteId);
        NoteDetailDto noteDetailDto = new NoteDetailDto(note);
        return ResponseEntity.ok().body(noteDetailDto);
    }

    @PostMapping("/api/keepNote")
    public ResponseEntity<List<Long>> keepNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteKeepDto noteKeepDto) {
        List<Long> nonMatchingIds = noteService.keepNote(userDto.getMemberId(), noteKeepDto);
        return ResponseEntity.ok().body(nonMatchingIds);
    }

    @PostMapping("/api/spamNote")
    public ResponseEntity<List<Long>> spamNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteSpamDto noteSpamDto) {
        List<Long> nonMatchingIds = noteService.spamNote(userDto.getMemberId(), noteSpamDto);
        return ResponseEntity.ok().body(nonMatchingIds);
    }

    @PostMapping("/api/declareNote")
    public ResponseEntity<List<Long>> declareNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteDeclareDto noteDeclareDto) {
        List<Long> nonMatchingIds = noteService.declareNote(userDto.getMemberId(), noteDeclareDto);
        return ResponseEntity.ok().body(nonMatchingIds);
    }
}
