package org.chorusbdd.exception;

public class OperationFailedException extends RuntimeException {
    static final long serialVersionUID = 3154098190745766939L;

    public OperationFailedException(String message) {
        super(message);
    }
}
