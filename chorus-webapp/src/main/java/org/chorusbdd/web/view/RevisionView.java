package org.chorusbdd.web.view;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class RevisionView {
    private String id = "";
    private String authorName = "";
    private String authorEmailAddress = "";
    private String dateTime = "";
    private String comment = "";

    public RevisionView() {}

    public String getAuthorEmailAddress() {
        return authorEmailAddress;
    }

    public void setAuthorEmailAddress(final String authorEmailAddress) {
        this.authorEmailAddress = authorEmailAddress;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
