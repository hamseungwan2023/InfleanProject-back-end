package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.Note;
import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSpamService {

    private final UserRepository userRepository;
    private final SpamRepository spamRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSpam(Long deleteSpamId) {

        Spam spam = spamRepository.findById(deleteSpamId).get();

        spamRepository.delete(spam);
    }
}
