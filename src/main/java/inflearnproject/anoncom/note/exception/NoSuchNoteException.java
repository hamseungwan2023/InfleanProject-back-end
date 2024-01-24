package inflearnproject.anoncom.note.exception;

public class NoSuchNoteException extends RuntimeException{
    public NoSuchNoteException() {
    }

    public NoSuchNoteException(String message) {
        super(message);
    }

}
