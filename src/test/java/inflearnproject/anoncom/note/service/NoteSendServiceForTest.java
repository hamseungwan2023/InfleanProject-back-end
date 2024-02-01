package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.declareNote.repository.DeclareNoteRepository;
import inflearnproject.anoncom.domain.DeclareNote;
import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Component
public class NoteSendServiceForTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private DeclareNoteRepository declareNoteRepository;
    @Autowired
    private SpamRepository spamRepository;

    public void sendNoteToReceiver(LoginUserDto userDto, String receiverNickname, String content) {
        // 쪽지 전송 로직
        UserEntity receiver = userRepository.findByNickname(receiverNickname).orElseThrow(
                () -> new NoUserEntityException(receiverNickname)
        );
        if (!receiver.isActive()) {
            throw new NoUserEntityException(receiverNickname);
        }
        UserEntity sender = userRepository.findByEmail(userDto.getEmail());
        Note note = Note.builder()
                .receiver(receiver)
                .sender(sender)
                .content(content)
                .build();
        noteRepository.save(note);
    }


    public void deleteSendNote(Long deleteNoteId) {
        Note note = findNoteById(deleteNoteId);

        note.senderDelete();
        deleteIfAllTrue(note);
    }

    public void deleteReceiveNote(Long deleteNoteId) {
        Note note = findNoteById(deleteNoteId);

        note.receiverDelete();
        deleteIfAllTrue(note);
    }

    private Note findNoteById(Long deleteNoteId) {
        return noteRepository.findById(deleteNoteId).orElseThrow(
                () -> new NoSuchNoteException(String.valueOf(deleteNoteId))
        );
    }

    private void deleteIfAllTrue(Note note) {
        if (note.isSenderDelete() && note.isReceiverDelete()) {
            noteRepository.delete(note);
        }
    }

    public void keepNote(Long noteKeepId) {
        Note note = noteRepository.findById(noteKeepId).orElseThrow(
                () -> new NoSuchNoteException(String.valueOf(noteKeepId))
        );
        note.keepTrue();
    }

//    public void spamNote(Long userId,Long noteSpamId) {
//        Note note = noteRepository.findById(noteSpamId).orElseThrow(
//                () -> new NoSuchNoteException(String.valueOf(noteSpamId))
//        );
//        UserEntity declaring = userRepository.findById(userId).get();
//        UserEntity declared = noteRepository.findById(noteSpamId).get().getSender();
//        Spam spam = Spam.builder()
//                .declaring(declaring)
//                .declared(declared)
//                .build();
//        if(spamRepository.findByDeclaringAndDeclared(declaring,declared).isEmpty()){
//            spamRepository.save(spam);
//        }
//        note.spamTrue();
//    }


    public void declareNote(Long declareNoteId) {
        Note note = noteRepository.findById(declareNoteId).orElseThrow(
                () -> new NoSuchNoteException(String.valueOf(declareNoteId))
        );
        DeclareNote declareNote = DeclareNote.builder()
                .note(note).build();
        declareNoteRepository.save(declareNote);
        note.delcareTrue();
    }
}
