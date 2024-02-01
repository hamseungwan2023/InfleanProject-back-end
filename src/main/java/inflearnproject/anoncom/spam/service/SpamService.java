package inflearnproject.anoncom.spam.service;

import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
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

    public List<Spam> searchSpamUser(Long memberId) {
        UserEntity declaring = userRepository.findById(memberId).get();
        return spamRepository.findAllByDeclaring(declaring);
    }

    public List<Long> deleteSpamNote(Long userId, DeleteSpamDto deleteSpamDto) {

        List<Spam> deleteSpams = spamRepository.findByIn(userId, deleteSpamDto.getDeleteSpamIds());

        List<Long> matchingNoteIds = new ArrayList<>();
        List<Long> nonMatchingNoteIds = new ArrayList<>();

        for (Spam spam : deleteSpams) {
            if (spam.getDeclaring().getId().equals(userId)) {
                matchingNoteIds.add(spam.getId());
            } else {
                nonMatchingNoteIds.add(spam.getId());
            }
        }
        spamRepository.deleteSpams(userId, matchingNoteIds);
        return nonMatchingNoteIds;
    }
}
