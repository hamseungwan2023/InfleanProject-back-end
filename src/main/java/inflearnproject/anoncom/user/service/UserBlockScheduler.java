package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBlockScheduler {

    private final UserRepository userRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void unblockUsers() {
        List<UserEntity> usersToUnblock = userRepository.findAllByIsBlockedTrueAndBlockUntilBefore(LocalDateTime.now());
        for (UserEntity user : usersToUnblock) {
            user.blockFalse();
        }
    }
}