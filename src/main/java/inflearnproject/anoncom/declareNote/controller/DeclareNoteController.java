package inflearnproject.anoncom.declareNote.controller;

import inflearnproject.anoncom.declareNote.dto.DeclareShowDto;
import inflearnproject.anoncom.declareNote.dto.DeclareUserDto;
import inflearnproject.anoncom.declareNote.service.DeclareNoteService;
import inflearnproject.anoncom.domain.DeclareNote;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class DeclareNoteController {

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    private final DeclareNoteService declareNoteService;

    @GetMapping("/declareNotes")
    public ResponseEntity<?> showDeclareNotes(@RequestParam(value = "page", defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, defaultPageSize);
        Page<DeclareNote> declareNotes = declareNoteService.showAll(pageable);
        List<DeclareShowDto> dtos = declareNotes.stream().map(DeclareShowDto::new).toList();

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/declareUser")
    public ResponseEntity<?> adminDeclareUser(@RequestBody DeclareUserDto declareUserDto){
        declareNoteService.declareUser(declareUserDto);
        return ResponseEntity.ok().body("ok");
    }
}
