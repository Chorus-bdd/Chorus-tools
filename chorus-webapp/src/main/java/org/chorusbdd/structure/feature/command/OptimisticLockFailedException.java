package org.chorusbdd.structure.feature.command;

public class OptimisticLockFailedException extends Exception {
    static final long serialVersionUID = 7034412190745766939L;

    public OptimisticLockFailedException(final String message) {
        super(message);
    }
}
