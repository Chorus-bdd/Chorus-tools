package org.chorusbdd.web.view;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class BinaryResultView {
    private boolean succeeded;
    private String message;

    public BinaryResultView() {
        // do nothing
    }

    public boolean getSucceeded() {
        return this.succeeded;
    }

    public void setSucceeded(final boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setMessage(final Exception e) {
        this.message = e.getMessage();
    }
}
