package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.declareNote.repository.DeclareBulkRepository;
import inflearnproject.anoncom.domain.DeclareNote;
import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.dto.*;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.note.repository.NoteBulkRepository;
import inflearnproject.anoncom.note.repository.NoteDSLRepository;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.spam.repository.SpamBulkRepository;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final DeclareBulkRepository declareBulkRepository;
    private final NoteDSLRepository noteDSLRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteBulkRepository noteBulkRepository;
    private final SpamRepository spamRepository;
    private final SpamBulkRepository spamBulkRepository;

    public List<String> addNote(LoginUserDto userDto, NoteAddDto noteDto) {
        List<String> erroredList = new ArrayList<>();
        UserEntity sender = userRepository.findByEmail(userDto.getEmail());

        // 모든 수신자를 한 번의 쿼리로 조회
        List<String> receiverNicknames = noteDto.getReceiverNicknames();
        List<UserEntity> receivers = userRepository.findByNicknameIn(receiverNicknames);

        findErrorNicknames(receivers, receiverNicknames, erroredList);

        List<Note> notesToSave = receivers.stream()
                .map(receiver -> Note.builder()
                        .receiver(receiver)
                        .sender(sender)
                        .content(noteDto.getContent())
                        .build())
                .collect(Collectors.toList());

        if (!notesToSave.isEmpty()) {
            noteBulkRepository.batchInsertNotes(notesToSave);  // 일괄 저장
        }

        return erroredList;
    }

    private void findErrorNicknames(List<UserEntity> receivers, List<String> receiverNicknames, List<String> erroredList) {
        Set<String> existReceiverNicknames = receivers.stream()
                .map(UserEntity::getNickname)
                .collect(Collectors.toSet());
        for (String receiverNickname : receiverNicknames) {
            if (!existReceiverNicknames.contains(receiverNickname)) {
                erroredList.add(receiverNickname);
            }
        }
    }

    /**
     * 보낸 사람이 쪽지 삭제
     */
    public void deleteSendNote(NoteDeleteDto noteDto) {
        List<Note> findNoteIds = noteRepository.findByIdIn(noteDto.getDeleteNoteIds());
        List<Long> noteIds = findNoteIds.stream().map(note -> note.getId()).toList();
        noteRepository.bulkNoteSenderDelete(noteIds);
    }

    public void deleteReceiveNote(NoteDeleteDto noteDto) {
        List<Note> findNoteIds = noteRepository.findByIdIn(noteDto.getDeleteNoteIds());
        List<Long> noteIds = findNoteIds.stream().map(note -> note.getId()).toList();
        noteRepository.bulkNoteReceiverDelete(noteIds);
    }

    public Page<NoteShowDto> findReceivedNotes(Long receiverId, NoteSearchCond cond, Pageable pageable) {
        return noteDSLRepository.findReceivedNotes(receiverId, cond, pageable);
    }

    public Page<NoteSendedShowDto> findSendedNotes(Long senderId, Pageable pageable) {
        return noteDSLRepository.findSendedNotes(senderId, pageable);
    }

    public void keepNote(NoteKeepDto noteKeepDto) {
        List<Note> findNoteIds = noteRepository.findByIdIn(noteKeepDto.getNoteKeeps());
        List<Long> noteIds = findNoteIds.stream().map(note -> note.getId()).toList();
        noteRepository.bulkNoteKeep(noteIds);
    }

    public void spamNote(Long userId, NoteSpamDto noteSpamDto) {

        List<Note> spamNotes = noteRepository.findByIdIn(noteSpamDto.getSpamNotes());

        UserEntity declaring = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        // 스팸 선언된 노트의 발신자 ID set
        Set<Long> senderIds = spamNotes.stream().map(note -> note.getSender().getId()).collect(Collectors.toSet());

        // 이미 스팸으로 선언된 (선언자, 발신자) 쌍을 찾습니다.
        List<Spam> existingSpams = spamRepository.findByDeclaringAndDeclaredIds(declaring.getId(), senderIds);
        //스팸처리된 유저들의 id set
        Set<Long> alreadyDeclaredSenderIds = existingSpams.stream().map(spam -> spam.getDeclared().getId()).collect(Collectors.toSet());
        //spamTrue로 바꿔줄 리스트
        List<Long> spamToTrues = new ArrayList<>();

        // 새로운 스팸 선언을 저장할 리스트
        List<Spam> spamsToSave = new ArrayList<>();
        for (Note note : spamNotes) {
            UserEntity declared = note.getSender();
            // 이미 스팸으로 선언되지 않은 경우에만 새 스팸 선언을 추가합니다.
            if (!alreadyDeclaredSenderIds.contains(declared.getId())) {
                Spam spam = Spam.builder()
                        .declaring(declaring)
                        .declared(declared)
                        .build();
                spamsToSave.add(spam);
            }
            spamToTrues.add(note.getId()); // 노트를 스팸으로 표시합니다.
        }

        // 새로운 스팸 선언을 데이터베이스에 저장합니다.
        spamBulkRepository.batchInsertSpams(spamsToSave);
        //쪽지들의 spam true로 바꿔준다
        noteRepository.updateSpamTrue(spamToTrues);
    }

    public Note findById(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new NoSuchNoteException("해당 쪽지는 존재하지 않습니다.")
        );
        note.receiverReadTrue();
        return note;
    }

    public void declareNote(NoteDeclareDto noteDeclareDto) {
        List<Note> declareNotes = noteRepository.findByIdIn(noteDeclareDto.getDeclareNotes());

        List<Long> noteIds = declareNotes.stream().map(note -> note.getId()).toList();
        List<DeclareNote> createdDeclareNotes = new ArrayList<>();
        noteRepository.updateDeclareTrue(noteIds);
        for (Note note : declareNotes) {
            DeclareNote declareNote = DeclareNote.builder()
                    .note(note)
                    .build();
            createdDeclareNotes.add(declareNote);
        }
        declareBulkRepository.batchInsertNotes(createdDeclareNotes);
    }
}
