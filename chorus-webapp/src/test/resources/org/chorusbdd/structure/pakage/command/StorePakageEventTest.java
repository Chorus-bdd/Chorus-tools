package org.chorusbdd.structure.pakage.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StorePakageEventTest {

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageId() {
        new StorePakageEvent(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyPakageId() {
        new StorePakageEvent("");
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final StorePakageEvent event = new StorePakageEvent("pakage-id-123");

        // verify
        assertThat(event.pakageId(), is("pakage-id-123"));
    }
}
