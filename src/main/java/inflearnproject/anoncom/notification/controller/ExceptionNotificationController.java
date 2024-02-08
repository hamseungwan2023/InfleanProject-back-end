package inflearnproject.anoncom.notification.controller;

import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.notification.exception.NotExistNotificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = NotificationController.class)
public class ExceptionNotificationController {

    @ExceptionHandler(NotExistNotificationException.class)
    public ResponseEntity<ErrorDTO> handleNotExistNotificationException(NotExistNotificationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
