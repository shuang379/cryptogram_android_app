package edu.gatech.seclass.crypto6300.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
