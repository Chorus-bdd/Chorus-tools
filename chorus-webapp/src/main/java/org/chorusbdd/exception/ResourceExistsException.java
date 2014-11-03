package org.chorusbdd.exception;

public class ResourceExistsException extends RuntimeException {
    static final long serialVersionUID = 2654098190745766939L;

    public ResourceExistsException(String message) {
        super(message);
    }
}
