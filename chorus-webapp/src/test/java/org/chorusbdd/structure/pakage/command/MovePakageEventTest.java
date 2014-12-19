package org.chorusbdd.structure.pakage.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MovePakageEventTest {

    private static final String FROM_ID = "--from id--";
    private static final String TO_ID = "--to id--";

    @Test(expected=NullPointerException.class)
    public void disallowsNullFromId() {
        new MovePakageEvent(null, TO_ID);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyFromId() {
        new MovePakageEvent("", TO_ID);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullToId() {
        new MovePakageEvent(FROM_ID, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyToId() {
        new MovePakageEvent(FROM_ID, "");
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final MovePakageEvent event = new MovePakageEvent(FROM_ID, TO_ID);

        // verify
        assertThat(event.targetId(), is(FROM_ID));
        assertThat(event.destinationId(), is(TO_ID));
    }

    @Test
    public void logMessageReportsFromAndToPackage() {
        final MovePakageEvent event = new MovePakageEvent("FOO.BAR", "TOO.FAR.MAH");
        assertThat(event.logMessage(), is("Moved package 'FOO.BAR' to 'TOO.FAR.MAH'"));
    }
}
