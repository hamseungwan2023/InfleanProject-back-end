package inflearnproject.anoncom.spam.service;

import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.dto.DeleteSpamDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SpamService {

    private final UserRepository userRepository;
    private final SpamRepository spamRepository;
    private final SpamUserService spamUserService;

    public List<Spam> searchSpamUser(Long memberId) {
        UserEntity declaring = userRepository.findById(memberId).get();
        return spamRepository.findAllByDeclaring(declaring);
    }

    public List<Long> deleteSpamNote(DeleteSpamDto deleteSpamDto) {

        List<Long> deleteSpams = new ArrayList<>();
        for (Long deleteSpamId : deleteSpamDto.getDeleteSpamIds()) {
            try {
                spamUserService.deleteSpam(deleteSpamId);
            } catch (NoSuchNoteException e) {
                long errorId = Long.parseLong(e.getMessage());
                deleteSpams.add(errorId);
            }
        }
        return deleteSpams;
    }
}
