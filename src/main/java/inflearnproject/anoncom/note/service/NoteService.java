package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.dto.*;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.note.repository.NoteBulkRepository;
import inflearnproject.anoncom.note.repository.NoteDSLRepository;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
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

    private final NoteSenderService noteSenderService;
    private final NoteDSLRepository noteDSLRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteBulkRepository noteBulkRepository;


    public List<String> addNote(LoginUserDto userDto, NoteAddDto noteDto) {
        List<String> erroredList = new ArrayList<>();
        UserEntity sender = userRepository.findByEmail(userDto.getEmail());

        // 모든 수신자를 한 번의 쿼리로 조회
        List<String> receiverNicknames = noteDto.getReceiverNicknames();
        List<UserEntity> receivers = userRepository.findByNicknameIn(receiverNicknames);

        // 수신자가 없거나 비활성화된 경우 오류 목록에 추가
        Set<String> activeReceiverNicknames = receivers.stream()
                .filter(UserEntity::isActive)
                .map(UserEntity::getNickname)
                .collect(Collectors.toSet());
        for (String receiverNickname : receiverNicknames) {
            if (!activeReceiverNicknames.contains(receiverNickname)) {
                erroredList.add(receiverNickname);
            }
        }

        // 활성 수신자에 대한 쪽지 생성 및 저장
        List<Note> notesToSave = receivers.stream()
                .filter(UserEntity::isActive)
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

    public List<Long> deleteSendNote(NoteDeleteDto noteDto) {
        List<Long> deletedList = new ArrayList<>();
        for (Long deleteNoteId : noteDto.getDeleteNoteIds()) {
            try {
                noteSenderService.deleteSendNote(deleteNoteId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                deletedList.add(errorId);
            }
        }
        return deletedList;
    }

    public List<Long> deleteReceiveNote(NoteDeleteDto noteDto) {
        List<Long> deletedList = new ArrayList<>();
        for (Long deleteNoteId : noteDto.getDeleteNoteIds()) {
            try {
                noteSenderService.deleteReceiveNote(deleteNoteId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                deletedList.add(errorId);
            }
        }
        return deletedList;
    }

    public Page<NoteShowDto> findReceivedNotes(Long receiverId, NoteSearchCond cond, Pageable pageable) {
        return noteDSLRepository.findReceivedNotes(receiverId, cond, pageable);
    }

    public Page<NoteSendedShowDto> findSendedNotes(Long senderId, Pageable pageable) {
        return noteDSLRepository.findSendedNotes(senderId, pageable);
    }

    public List<Long> keepNote(NoteKeepDto noteKeepDto) {
        List<Long> keepList = new ArrayList<>();
        for (Long noteKeepId : noteKeepDto.getNoteKeeps()) {
            try {
                noteSenderService.keepNote(noteKeepId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                keepList.add(errorId);
            }
        }
        return keepList;
    }

    public List<Long> spamNote(Long userId, NoteSpamDto noteSpamDto) {

        List<Long> spamList = new ArrayList<>();
        for (Long noteSpamId : noteSpamDto.getSpamNotes()) {
            try {
                noteSenderService.spamNote(userId, noteSpamId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                spamList.add(errorId);
            }
        }
        return spamList;
    }

    public Note findById(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new NoSuchNoteException("해당 쪽지는 존재하지 않습니다.")
        );
        note.receiverReadTrue();
        return note;
    }

    public List<Long> declareNote(NoteDeclareDto noteDeclareDto) {
        List<Long> declareList = new ArrayList<>();
        for (Long declareNoteId : noteDeclareDto.getDeclareNotes()) {
            try {
                noteSenderService.declareNote(declareNoteId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                declareList.add(errorId);
            }
        }
        return declareList;
    }
}
