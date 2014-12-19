package org.chorusbdd.history;

public class SvcFailedException extends RuntimeException {
    static final long serialVersionUID = 2224098190745766939L;

    public SvcFailedException(String message) {
        super(message);
    }

    public SvcFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SvcFailedException(Throwable cause) {
        super(cause);
    }
}
