package org.chorusbdd.structure.pakage.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DeletePakageEventTest {

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageId() {
        new DeletePakageEvent(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyPakageId() {
        new DeletePakageEvent("");
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final DeletePakageEvent event = new DeletePakageEvent("pakage-id-123");

        // verify
        assertThat(event.pakageId(), is("pakage-id-123"));
    }

    @Test
    public void logMessageReportsDeletedPackage() {
        final DeletePakageEvent event = new DeletePakageEvent("FOO.BAR");
        assertThat(event.logMessage(), is("Deleted package 'FOO.BAR'"));
    }
}
