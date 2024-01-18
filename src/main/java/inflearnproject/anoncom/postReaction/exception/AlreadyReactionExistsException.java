package inflearnproject.anoncom.postReaction.exception;

public class AlreadyReactionExistsException extends RuntimeException{

    public AlreadyReactionExistsException() {
    }

    public AlreadyReactionExistsException(String message) {
        super(message);
    }
}
