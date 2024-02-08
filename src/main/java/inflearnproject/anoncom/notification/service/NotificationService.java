package inflearnproject.anoncom.notification.service;

import inflearnproject.anoncom.domain.Notification;
import inflearnproject.anoncom.notification.dto.NotificationAddDto;
import inflearnproject.anoncom.notification.dto.NotificationSearchCondition;
import inflearnproject.anoncom.notification.repository.NotificationDSLRepository;
import inflearnproject.anoncom.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationDSLRepository notificationDSLRepository;

    public void addNotification(NotificationAddDto notificationAddDto) {
        Notification notification = Notification.builder()
                .title(notificationAddDto.getTitle())
                .category(notificationAddDto.getCategory())
                .content(notificationAddDto.getContent())
                .location(notificationAddDto.getLocation())
                .build();
        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(NotificationSearchCondition cond) {
        return notificationDSLRepository.findNotificationsByCondition(cond);
    }
}
