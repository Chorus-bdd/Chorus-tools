package org.chorusbdd.web.view;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class ResultView {
    private boolean succeeded;
    private String message;

    public ResultView() {
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
