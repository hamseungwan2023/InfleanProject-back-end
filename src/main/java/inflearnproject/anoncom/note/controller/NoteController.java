package inflearnproject.anoncom.note.controller;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.note.dto.*;
import inflearnproject.anoncom.note.service.NoteService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    /**
     * 받은 쪽지함 보여주기
     */
    @GetMapping("/api/noteReadReceivedList")
    public ResponseEntity<Page<NoteShowDto>> showReceiverNotes(@IfLogin LoginUserDto userDto,@RequestParam(value = "page", defaultValue = "0") int page,
                                                               @ModelAttribute(value = "cond") NoteSearchCond cond){

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteShowDto> notesDto = noteService.findReceivedNotes(userDto.getMemberId(), cond,pageable);
        return ResponseEntity.ok().body(notesDto);
    }

    /**
     * 내가 보낸 쪽지들 보여주기
     */
    @GetMapping("/api/noteReadSendedList")
    public ResponseEntity<Page<NoteSendedShowDto>> showSendNotes(@IfLogin LoginUserDto userDto,@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @ModelAttribute(value = "cond") NoteSearchCond cond){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteSendedShowDto> notesDto = noteService.findSendedNotes(userDto.getMemberId(), pageable);
        return ResponseEntity.ok().body(notesDto);
    }

    @GetMapping("/api/noteRead/{noteId}")
    public ResponseEntity<NoteDetailDto> showNoteDetail(@IfLogin LoginUserDto userDto, @PathVariable("noteId") Long noteId){
        Note note = noteService.findById(noteId);
        NoteDetailDto noteDetailDto = new NoteDetailDto(note);
        return ResponseEntity.ok().body(noteDetailDto);
    }

    @PostMapping("/api/keepNote")
    public ResponseEntity<List<Long>> keepNotes(@IfLogin LoginUserDto userDto,@RequestBody NoteKeepDto noteKeepDto){
        List<Long> keepNotes = noteService.keepNote(noteKeepDto);
        return ResponseEntity.ok().body(keepNotes);
    }

    @PostMapping("/api/spamNote")
    public ResponseEntity<List<Long>> spamNotes(@IfLogin LoginUserDto userDto, @RequestBody NoteSpamDto noteSpamDto){
        List<Long> spamNotes = noteService.spamNote(noteSpamDto);
        return ResponseEntity.ok().body(spamNotes);
    }
}
