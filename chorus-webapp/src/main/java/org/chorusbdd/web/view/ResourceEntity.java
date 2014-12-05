package org.chorusbdd.web.view;

import com.fasterxml.jackson.annotation.JsonView;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class ResourceEntity {

    private String id = "";
    private String message = "";
    private String location = "";

    public ResourceEntity() {
        // do nothing
    }

    @JsonView(BaseView.class)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @JsonView(BaseView.class)
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @JsonView(BaseView.class)
    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }
}
