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
        noteRepository.bulkNoteDelete(noteIds);
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
