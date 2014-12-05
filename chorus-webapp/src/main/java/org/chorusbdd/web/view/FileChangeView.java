package org.chorusbdd.web.view;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class FileChangeView {
    private String event = "";
    private String id = "";

    public FileChangeView() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(final String event) {
        this.event = event;
    }
}
