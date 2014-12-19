package org.chorusbdd.history;

import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class RevisionImpl implements Revision {
    private final String id;
    private final String authorName;
    private final String authorEmailAddress;
    private final Date dateTime;
    private final String comment;

    RevisionImpl(final String id, final String authorName, final String authorEmailAddress, final Date dateTime, final String comment) {
        this.id = notNull(id);
        this.authorName = notNull(authorName);
        this.authorEmailAddress = notNull(authorEmailAddress);
        this.dateTime = notNull(dateTime);
        this.comment = notNull(comment);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String authorName() {
        return authorName;
    }

    @Override
    public String authorEmailAddress() {
        return authorEmailAddress;
    }

    @Override
    public Date dateTime() {
        return dateTime;
    }

    @Override
    public String comment() {
        return comment;
    }



    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("authorName", authorName)
                .add("authorEmailAddress", authorEmailAddress)
                .add("dateTime", dateTime)
                .add("comment", comment)
                .toString();
    }
}
