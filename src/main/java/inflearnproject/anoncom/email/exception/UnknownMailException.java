package inflearnproject.anoncom.email.exception;

public class UnknownMailException extends RuntimeException{
    public UnknownMailException() {
    }

    public UnknownMailException(String message) {
        super(message);
    }
}
