package inflearnproject.anoncom.refreshToken.exception;

public class NoRefreshTokenException extends RuntimeException {
    public NoRefreshTokenException() {
    }

    public NoRefreshTokenException(String message) {
        super(message);
    }
}
