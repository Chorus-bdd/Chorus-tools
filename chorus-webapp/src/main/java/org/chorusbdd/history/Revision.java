package org.chorusbdd.history;

import java.util.Date;

public interface Revision {
    String id();

    String authorName();

    String authorEmailAddress();

    Date dateTime();

    String comment();
}
