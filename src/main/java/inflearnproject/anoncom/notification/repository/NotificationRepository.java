package inflearnproject.anoncom.notification.repository;

import inflearnproject.anoncom.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
