package inflearnproject.anoncom.note.controller;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = NoteController.class)
public class ExceptionNoteController {

    @ExceptionHandler(NoSuchNoteException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchNoteException(NoSuchNoteException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(NotSameUserException.class)
    public ResponseEntity<ErrorDTO> handleNotSameUserException(NotSameUserException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(exception.getMessage()));
    }
}
