package org.chorusbdd.structure.feature.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DeleteFeatureEventTest {

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureId() {
        new DeleteFeatureEvent(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyFeatureId() {
        new DeleteFeatureEvent("");
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final DeleteFeatureEvent event = new DeleteFeatureEvent("feature-id-123");

        // verify
        assertThat(event.featureId(), is("feature-id-123"));
    }

    @Test
    public void logMessageReportsDeletedFeature() {
        final DeleteFeatureEvent event = new DeleteFeatureEvent("FOO.BAR");
        assertThat(event.logMessage(), is("Deleted feature 'FOO.BAR'"));
    }
}
