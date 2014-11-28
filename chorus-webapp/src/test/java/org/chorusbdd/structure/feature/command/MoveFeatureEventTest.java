package org.chorusbdd.structure.feature.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MoveFeatureEventTest {

    private static final String FROM_ID = "--from id--";
    private static final String TO_ID = "--to id--";

    @Test(expected=NullPointerException.class)
    public void disallowsNullFromId() {
        new MoveFeatureEvent(null, TO_ID);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyFromId() {
        new MoveFeatureEvent("", TO_ID);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullToId() {
        new MoveFeatureEvent(FROM_ID, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyToId() {
        new MoveFeatureEvent(FROM_ID, "");
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final MoveFeatureEvent event = new MoveFeatureEvent(FROM_ID, TO_ID);

        // verify
        assertThat(event.targetId(), is(FROM_ID));
        assertThat(event.destinationId(), is(TO_ID));
    }

    @Test
    public void logMessageReportsFromAndToFeature() {
        final MoveFeatureEvent event = new MoveFeatureEvent("FOO.BAR", "TOO.FAR.MAH");
        assertThat(event.logMessage(), is("Moved feature 'FOO.BAR' to 'TOO.FAR.MAH'"));
    }
}
