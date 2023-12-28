package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.user.exception.SameInfoUserEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class ExceptionUserController {

    @ExceptionHandler(SameInfoUserEntityException.class)
    public ResponseEntity<String> handleNoSuchElementFoundException(SameInfoUserEntityException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}