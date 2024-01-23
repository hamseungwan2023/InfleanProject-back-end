package inflearnproject.anoncom.note.service;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.repository.NoteRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteSenderService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNoteToReceiver(LoginUserDto userDto, String receiverNickname, String content) {
        // 쪽지 전송 로직
        UserEntity receiver = userRepository.findByNickname(receiverNickname).orElseThrow(
                () -> new NoUserEntityException(receiverNickname)
        );
        if(!receiver.isActive()){
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
}
