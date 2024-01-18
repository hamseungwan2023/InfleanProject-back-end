package inflearnproject.anoncom.postReaction.controller;

import inflearnproject.anoncom.domain.PostReaction;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.controller.PostController;
import inflearnproject.anoncom.post.exception.NoPostException;
import inflearnproject.anoncom.postReaction.exception.AlreadyReactionExistsException;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = PostReactionController.class)
public class ExceptionPostReactionController {

    @ExceptionHandler(NoUserEntityException.class)
    public ResponseEntity<ErrorDTO> NoUserEntityException(NoUserEntityException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NoPostException.class)
    public ResponseEntity<ErrorDTO> NoPostException(NoPostException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NotIncreaseLikeSelfException.class)
    public ResponseEntity<ErrorDTO> NotIncreaseLikeSelfException(NotIncreaseLikeSelfException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(AlreadyReactionExistsException.class)
    public ResponseEntity<ErrorDTO> AlreadyReactionExistsException(AlreadyReactionExistsException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
