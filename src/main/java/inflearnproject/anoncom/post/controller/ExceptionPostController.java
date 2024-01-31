package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.exception.NoPostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = PostController.class)
public class ExceptionPostController {

    @ExceptionHandler(NoPostException.class)
    public ResponseEntity<ErrorDTO> handleNoPostException(NoPostException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NotSameUserException.class)
    public ResponseEntity<ErrorDTO> handleNotSameUserException(NotSameUserException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
