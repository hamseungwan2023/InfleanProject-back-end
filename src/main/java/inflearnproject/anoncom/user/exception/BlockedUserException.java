package inflearnproject.anoncom.user.exception;

public class BlockedUserException extends RuntimeException{
    public BlockedUserException() {
    }

    public BlockedUserException(String message) {
        super(message);
    }
}
