package org.chorusbdd.web.view;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class ErrorEntity {
    private String message = "";

    public ErrorEntity() {
        // do nothing
    }

    @JsonView(BaseView.class)
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setMessage(final Exception e) {
        this.message = e.getMessage();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }
}
