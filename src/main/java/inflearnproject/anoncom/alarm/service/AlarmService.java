package inflearnproject.anoncom.alarm.service;

import inflearnproject.anoncom.alarm.repository.AlarmRepository;
import inflearnproject.anoncom.domain.Alarm;
import inflearnproject.anoncom.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public void addAlarm(UserEntity to, String content) {
        Alarm alarm = Alarm.builder()
                .user(to)
                .message(content)
                .isRead(false)
                .build();

        alarmRepository.save(alarm);
    }
}
