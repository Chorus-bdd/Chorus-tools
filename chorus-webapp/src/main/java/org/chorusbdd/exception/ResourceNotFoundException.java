package org.chorusbdd.exception;

public class ResourceNotFoundException extends RuntimeException {
    static final long serialVersionUID = 7654098190745766939L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
