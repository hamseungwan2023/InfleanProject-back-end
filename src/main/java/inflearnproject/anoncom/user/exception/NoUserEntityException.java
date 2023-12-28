package inflearnproject.anoncom.user.exception;

public class NoUserEntityException extends RuntimeException{

    public NoUserEntityException() {
        super();
    }

    public NoUserEntityException(String message) {
        super(message);
    }
}
