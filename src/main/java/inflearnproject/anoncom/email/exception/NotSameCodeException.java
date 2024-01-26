package inflearnproject.anoncom.email.exception;

public class NotSameCodeException extends RuntimeException{
    public NotSameCodeException() {
    }

    public NotSameCodeException(String message) {
        super(message);
    }
}
