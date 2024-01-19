package inflearnproject.anoncom.commentReaction.controller;

import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.postReaction.exception.AlreadyReactionExistsException;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CommentReactionController.class)
public class ExceptionCommentReactionController {

    @ExceptionHandler(NoUserEntityException.class)
    public ResponseEntity<ErrorDTO> NoUserEntityException(NoUserEntityException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NoCommentException.class)
    public ResponseEntity<ErrorDTO> NoCommentException(NoCommentException exception) {
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
