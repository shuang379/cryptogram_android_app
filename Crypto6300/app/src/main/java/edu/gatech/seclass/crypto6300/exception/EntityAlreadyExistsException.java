package edu.gatech.seclass.crypto6300.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
