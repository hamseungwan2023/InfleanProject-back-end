package inflearnproject.anoncom.notification.controller;

import inflearnproject.anoncom.domain.Notification;
import inflearnproject.anoncom.notification.dto.NotificationAddDto;
import inflearnproject.anoncom.notification.dto.NotificationSearchCondition;
import inflearnproject.anoncom.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity<?> addNotification(@RequestBody NotificationAddDto notificationAddDto) {
        notificationService.addNotification(notificationAddDto);
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/notification")
    public ResponseEntity<?> getNotifications(
            @Valid @ModelAttribute(value = "findPostContent") NotificationSearchCondition cond) {
        List<Notification> notifications = notificationService.getNotifications(cond);
        return ResponseEntity.ok().body(notifications);
    }
}
