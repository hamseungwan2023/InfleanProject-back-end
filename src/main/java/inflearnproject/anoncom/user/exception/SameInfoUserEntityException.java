package inflearnproject.anoncom.user.exception;

public class SameInfoUserEntityException extends RuntimeException{

    public SameInfoUserEntityException() {
        super();
    }

    public SameInfoUserEntityException(String message) {
        super(message);
    }
}
