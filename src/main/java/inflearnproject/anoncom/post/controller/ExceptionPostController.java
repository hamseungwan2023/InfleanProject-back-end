package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.exception.NoPostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static inflearnproject.anoncom.user.exception.ExceptionMessage.BE_RIGHT_LENGTH;

@RestControllerAdvice(assignableTypes = PostController.class)
public class ExceptionPostController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(BE_RIGHT_LENGTH));
    }

    @ExceptionHandler(NoPostException.class)
    public ResponseEntity<ErrorDTO> handleNoPostException(NoPostException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NotSameUserException.class)
    public ResponseEntity<ErrorDTO> handleNotSameUserException(NotSameUserException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
