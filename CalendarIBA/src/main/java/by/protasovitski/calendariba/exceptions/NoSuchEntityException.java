package by.protasovitski.calendariba.exceptions;

public class NoSuchEntityException extends Exception {

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityException(Throwable cause) {
        super(cause);
    }
}
