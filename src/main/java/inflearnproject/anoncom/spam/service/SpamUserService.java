package inflearnproject.anoncom.spam.service;

import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SpamUserService {

    private final SpamRepository spamRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSpam(Long deleteSpamId) {

        Spam spam = spamRepository.findById(deleteSpamId).get();
        spamRepository.delete(spam);
    }
}
