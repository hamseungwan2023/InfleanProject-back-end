package inflearnproject.anoncom.alarm.error;

public class NoAlarmException extends RuntimeException {

    public NoAlarmException() {
    }

    public NoAlarmException(String message) {
        super(message);
    }
}
