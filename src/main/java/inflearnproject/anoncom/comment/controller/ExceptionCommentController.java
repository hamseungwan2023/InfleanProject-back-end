package inflearnproject.anoncom.comment.controller;

import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.controller.PostController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CommentController.class)
public class ExceptionCommentController {

    @ExceptionHandler(NoCommentException.class)
    public ResponseEntity<ErrorDTO> handleNoCommentException(NoCommentException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("글자 수와 형식에 맞게 작성해주십시오"));
    }

    @ExceptionHandler(NotSameUserException.class)
    public ResponseEntity<ErrorDTO> handleNotSameUserException(NotSameUserException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("글자 수와 형식에 맞게 작성해주십시오"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("글자 수와 형식에 맞게 작성해주십시오"));
    }
}
