package inflearnproject.anoncom.user.exception;

public class NotActiveUser extends RuntimeException{
    public NotActiveUser() {
    }

    public NotActiveUser(String message) {
        super(message);
    }
}
