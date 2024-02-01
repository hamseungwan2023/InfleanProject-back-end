package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.declareNote.repository.DeclareNoteRepository;
import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.dto.NoteAddDto;
import inflearnproject.anoncom.note.dto.NoteDeclareDto;
import inflearnproject.anoncom.note.dto.NoteDeleteDto;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static inflearnproject.anoncom.custom.TestServiceUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class NoteServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    NoteService noteService;
    @Autowired
    NoteRepository noteRepository;

    @Autowired
    EntityManager em;

    @Autowired
    DeclareNoteRepository declareNoteRepository;
    @Autowired
    SpamRepository spamRepository;

    private UserEntity user;
    private UserEntity user2;

    @BeforeEach
    void before() {
        user = addUser(userService);
        user2 = addAnotherUser(userService, 2);

        LoginUserDto dto = buildUserDto(user);
        NoteAddDto noteAddDto = new NoteAddDto();
        noteAddDto.setContent("쪽지");
        noteAddDto.setReceiverNicknames(List.of("nickname2"));

        noteService.addNote(dto, noteAddDto);
    }


    @Test
    @DisplayName("쪽지가 대상한테 잘 보내졌는지 확인")
    void add_note_success() {

        LoginUserDto dto = buildUserDto(user);
        NoteAddDto noteAddDto = new NoteAddDto();
        noteAddDto.setContent("쪽지");
        noteAddDto.setReceiverNicknames(List.of("nickname2"));

        noteService.addNote(dto, noteAddDto);
        assertEquals(noteRepository.findAll().size(), 2);
    }

    @Test
    @DisplayName("쪽지가 보낼 때 시간 비교하기 => transactional(requires_new) & 모든 수신자를 한 번의 쿼리로 조회, 물론 한 번의 쿼리가 n배 우세")
    void add_note_compare_time() {

        for (int i = 100; i < 110; i++) {
            addAnotherUser(userService, i);
        }

        LoginUserDto dto = buildUserDto(user);
        NoteAddDto noteAddDto = new NoteAddDto();
        noteAddDto.setContent("쪽지");
        List<String> list = new ArrayList<>();
        for (int i = 100; i < 10000; i++) {
            list.add("nickname" + i);
        }
        noteAddDto.setReceiverNicknames(list);

        LocalDateTime before = LocalDateTime.now();
        noteService.addNote(dto, noteAddDto);
        LocalDateTime after = LocalDateTime.now();

        Duration duration = Duration.between(before, after);
        System.out.println("Duration: " + duration.getSeconds() + " seconds " + duration.getNano() + " nanoseconds");

        LocalDateTime before2 = LocalDateTime.now();
        noteService.addNote(dto, noteAddDto);
        LocalDateTime after2 = LocalDateTime.now();

        Duration duration2 = Duration.between(before2, after2);
        System.out.println("Duration2: " + duration2.getSeconds() + " seconds " + duration2.getNano() + " nanoseconds");
    }

    @Test
    @DisplayName("보낸 사람이 쪽지를 삭제하면 isSenderDelete라는 필드가 true가 되나")
    void delete_send_note() {

        for (int i = 100; i < 110; i++) {
            addAnotherUser(userService, i);
        }

        LoginUserDto dto = buildUserDto(user);
        NoteAddDto noteAddDto = new NoteAddDto();
        noteAddDto.setContent("쪽지");
        List<String> list = new ArrayList<>();
        for (int i = 100; i < 10000; i++) {
            list.add("nickname" + i);
        }
        noteAddDto.setReceiverNicknames(list);
        noteService.addNote(dto, noteAddDto);

        NoteDeleteDto noteDeleteDto = new NoteDeleteDto();
        List<Long> collect = noteRepository.findAll().stream().map(findNote -> findNote.getId()).collect(Collectors.toList());
        noteDeleteDto.setDeleteNoteIds(collect);

        noteService.deleteSendNote(user.getId(), noteDeleteDto);
        for (Note note : noteRepository.findAll()) {
            assertTrue(note.isSenderDelete());
        }
    }


    @Test
    @DisplayName("받은 사람이 쪽지를 삭제하면 isReceiverDelete라는 필드가 true가 되나")
    void delete_receive_note() {

        Long noteId = noteRepository.findAll().get(0).getId();

        NoteDeleteDto noteDeleteDto = new NoteDeleteDto();
        List<Long> deleteNoteIds = new ArrayList<>();
        deleteNoteIds.add(noteId);
        noteDeleteDto.setDeleteNoteIds(deleteNoteIds);

        noteService.deleteReceiveNote(user2.getId(), noteDeleteDto);
        assertTrue(noteRepository.findAll().get(0).isReceiverDelete());
    }


    @Test
    @DisplayName("받은 사람이 쪽지를 신고하면 isReceiverDelete라는 필드가 true가 되나")
    void declare_note() {

        Long noteId = noteRepository.findAll().get(0).getId();

        NoteDeclareDto noteDeclareDto = new NoteDeclareDto();
        List<Long> declareIds = new ArrayList<>();
        declareIds.add(noteId);
        noteDeclareDto.setDeclareNotes(declareIds);

        noteService.declareNote(user2.getId(), noteDeclareDto);

        em.flush();
        em.clear();
        
        assertTrue(noteRepository.findAll().get(0).isDeclaration());
        assertEquals(declareNoteRepository.findAll().size(), 1);
    }

//    @Test
//    @DisplayName("스팸처리하면 차단 레포지토리에 데이터가 증가하나 확인")
//    void add_spam() {
//        NoteSpamDto noteSpamDto = new NoteSpamDto();
//        List<Long> spamNotes = new ArrayList<>();
//        Long noteId = noteRepository.findAll().get(0).getId();
//        spamNotes.add(noteId);
//        noteSpamDto.setSpamNotes(spamNotes);
//        noteServiceForTest.spamNote(user.getId(), noteSpamDto);
//        assertEquals(spamRepository.findAll().size(), 1);
//    }
}