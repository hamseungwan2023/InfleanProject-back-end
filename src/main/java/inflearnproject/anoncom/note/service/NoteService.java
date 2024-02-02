package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.declareNote.repository.DeclareBulkRepository;
import inflearnproject.anoncom.declareNote.repository.DeclareNoteRepository;
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
import inflearnproject.anoncom.user.exception.NoUserEntityException;
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

import static inflearnproject.anoncom.error.ExceptionMessage.*;

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
    private final DeclareNoteRepository declareNoteRepository;

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
    public List<Long> deleteSendNote(Long userId, NoteDeleteDto noteDto) {
        List<Note> notes = noteRepository.findByIdIn(noteDto.getDeleteNoteIds());
        List<Long> matchingNoteIds = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Note note : notes) {
            if (note.getSender().getId().equals(userId)) {
                matchingNoteIds.add(note.getId());
            } else {
                nonMatchingNoteIds.add(note.getId());
            }
        }

        if (!matchingNoteIds.isEmpty()) {
            noteRepository.bulkNoteSenderDelete(matchingNoteIds);
        }

        return nonMatchingNoteIds;
    }

    public List<Long> deleteReceiveNote(Long userId, NoteDeleteDto noteDto) {
        List<Note> notes = noteRepository.findByIdIn(noteDto.getDeleteNoteIds());
        List<Long> matchingNoteIds = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Note note : notes) {
            if (note.getReceiver().getId().equals(userId)) {
                matchingNoteIds.add(note.getId());
            } else {
                nonMatchingNoteIds.add(note.getId());
            }
        }

        if (!matchingNoteIds.isEmpty()) {
            noteRepository.bulkNoteReceiverDelete(matchingNoteIds);
        }

        return nonMatchingNoteIds;
    }

    public Page<NoteShowDto> findReceivedNotes(Long receiverId, NoteSearchCond cond, Pageable pageable) {
        return noteDSLRepository.findReceivedNotes(receiverId, cond, pageable);
    }

    public Page<NoteSendedShowDto> findSendedNotes(Long senderId, Pageable pageable) {
        return noteDSLRepository.findSendedNotes(senderId, pageable);
    }

    public List<Long> keepNote(Long userId, NoteKeepDto noteKeepDto) {
        List<Note> findNotes = noteRepository.findByIdIn(noteKeepDto.getNoteKeeps());

        List<Long> matchingNoteIds = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Note note : findNotes) {
            if (note.getReceiver().getId().equals(userId)) {
                matchingNoteIds.add(note.getId());
            } else {
                nonMatchingNoteIds.add(note.getId());
            }
        }

        if (!matchingNoteIds.isEmpty()) {
            noteRepository.bulkNoteKeep(matchingNoteIds);
        }

        return nonMatchingNoteIds;
    }

    public List<Long> spamNote(Long userId, NoteSpamDto noteSpamDto) {

        List<Note> spamNotes = noteRepository.findByIdIn(noteSpamDto.getSpamNotes());

        UserEntity declaring = userRepository.findById(userId).orElseThrow(() -> new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE));

        List<Note> matchingNotes = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Note note : spamNotes) {
            if (note.getReceiver().getId().equals(userId)) {
                matchingNotes.add(note);
            } else {
                nonMatchingNoteIds.add(note.getId());
            }
        }

        Set<Long> senderIds = matchingNotes.stream().map(note -> note.getSender().getId()).collect(Collectors.toSet());

        List<Spam> existingSpams = spamRepository.findByDeclaringAndDeclaredIds(declaring.getId(), senderIds);

        Set<Long> alreadyDeclaredSenderIds = existingSpams.stream().map(spam -> spam.getDeclared().getId()).collect(Collectors.toSet());

        List<Long> spamToTrues = new ArrayList<>();


        List<Spam> spamsToSave = new ArrayList<>();
        addNewSpamsAndToTrue(matchingNotes, alreadyDeclaredSenderIds, declaring, spamsToSave, spamToTrues);

        spamBulkRepository.batchInsertSpams(spamsToSave);

        noteRepository.updateSpamTrue(spamToTrues);

        return nonMatchingNoteIds;
    }

    private static void addNewSpamsAndToTrue(List<Note> matchingNotes, Set<Long> alreadyDeclaredSenderIds, UserEntity declaring, List<Spam> spamsToSave, List<Long> spamToTrues) {
        for (Note note : matchingNotes) {
            UserEntity declared = note.getSender();
            Long noteId = note.getId();

            if (!alreadyDeclaredSenderIds.contains(declared.getId())) {
                Spam spam = Spam.builder()
                        .declaring(declaring)
                        .declared(declared)
                        .build();
                spamsToSave.add(spam);
                alreadyDeclaredSenderIds.add(declared.getId());
            }

            if (!note.isSpam()) {
                spamToTrues.add(noteId);
            }
        }
    }

    public Note findById(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new NoSuchNoteException(NO_SUCH_NOTE)
        );
        if (note.getReceiver().getId().equals(userId)) {
            note.receiverReadTrue();
            note.putReceiverDate();
        } else if (!note.getSender().getId().equals(userId)) {
            throw new NotSameUserException(NOT_SAME_USER);
        }
        return note;
    }

    public List<Long> declareNote(Long userId, NoteDeclareDto noteDeclareDto) {
        List<Note> declareNotes = noteRepository.findByIdIn(noteDeclareDto.getDeclareNotes());

        List<Long> matchingNoteIds = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Note note : declareNotes) {
            if (note.getReceiver().getId().equals(userId)) {
                matchingNoteIds.add(note.getId());
            } else {
                nonMatchingNoteIds.add(note.getId());
            }
        }
        noteRepository.updateDeclareTrue(matchingNoteIds);
        List<Long> alreadyDeclareExists = declareNoteRepository.findByNoteId(userId);
        insertDeclareNotes(userId, declareNotes, alreadyDeclareExists);

        return nonMatchingNoteIds;
    }

    private void insertDeclareNotes(Long userId, List<Note> declareNotes, List<Long> alreadyDeclareExists) {
        List<Note> validNotes = declareNotes.stream().filter(note -> note.getReceiver().getId().equals(userId)).toList();
        List<DeclareNote> createdDeclareNotes = new ArrayList<>();
        for (Note note : validNotes) {
            DeclareNote declareNote = DeclareNote.builder()
                    .note(note)
                    .build();

            if (!alreadyDeclareExists.contains(note.getId())) {
                createdDeclareNotes.add(declareNote);
            }
        }
        declareBulkRepository.batchInsertNotes(createdDeclareNotes);
    }

    public Long getNotReadReceivedNotesSize(Long receiverId, NoteSearchCond cond) {
        return noteDSLRepository.findNotReadReceivedNotes(receiverId, cond);
    }
}
