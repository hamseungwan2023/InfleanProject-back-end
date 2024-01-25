package inflearnproject.anoncom.note.controller;

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
    public ResponseEntity<Page<NoteSendedShowDto>> showSendNotes(@IfLogin LoginUserDto userDto,@RequestParam(value = "page", defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NoteSendedShowDto> notesDto = noteService.findSendedNotes(userDto.getMemberId(), pageable);
        return ResponseEntity.ok().body(notesDto);
    }
}
