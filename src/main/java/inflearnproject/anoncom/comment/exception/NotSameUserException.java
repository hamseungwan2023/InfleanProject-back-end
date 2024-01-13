package inflearnproject.anoncom.comment.exception;

public class NotSameUserException extends RuntimeException{

    public NotSameUserException() {
        super();
    }

    public NotSameUserException(String message) {
        super(message);
    }
}
