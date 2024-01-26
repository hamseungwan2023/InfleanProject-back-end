package inflearnproject.anoncom.email.controller;

import inflearnproject.anoncom.email.exception.NotSameCodeException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.controller.PostController;
import inflearnproject.anoncom.post.exception.NoPostException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice(assignableTypes = EmailController.class)
public class ExceptionEmailController {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorDTO> handleMessagingException(MessagingException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("예기치 못한 오류가 발생했습니다. 다시 이메일 인증 버튼을 눌러주세요"));
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<ErrorDTO> handleUnsupportedEncodingException(UnsupportedEncodingException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("예기치 못한 오류가 발생했습니다. 다시 이메일 인증 버튼을 눌러주세요"));
    }

    @ExceptionHandler(NotSameCodeException.class)
    public ResponseEntity<ErrorDTO> handleNotSameCodeException(NotSameCodeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
