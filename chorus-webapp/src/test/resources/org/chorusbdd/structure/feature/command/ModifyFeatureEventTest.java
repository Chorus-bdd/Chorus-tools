package org.chorusbdd.structure.feature.command;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModifyFeatureEventTest {

    private static final String FEATURE_ID = "--featureid--";
    private static final String TEXT = "--text--";
    private static final String OPTIMISTIC_MD5 = "--md5--";

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureId() {
        new ModifyFeatureEvent(null, TEXT, OPTIMISTIC_MD5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyFeatureId() {
        new ModifyFeatureEvent("", TEXT, OPTIMISTIC_MD5);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullText() {
        new ModifyFeatureEvent(FEATURE_ID, null, OPTIMISTIC_MD5);
    }

    @Test
    public void allowsBlankText() {
        final ModifyFeatureEvent event = new ModifyFeatureEvent(FEATURE_ID, "", OPTIMISTIC_MD5);
        assertThat(event.text(), is(""));
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullMd5() {
        new ModifyFeatureEvent(FEATURE_ID, TEXT, null);
    }

    @Test
    public void allowsBlankMd5() {
        final ModifyFeatureEvent event = new ModifyFeatureEvent(FEATURE_ID, TEXT, "");
        assertThat(event.optimisticMd5(), is(""));
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final ModifyFeatureEvent event = new ModifyFeatureEvent(FEATURE_ID, TEXT, OPTIMISTIC_MD5);

        // verify
        assertThat(event.featureId(), is(FEATURE_ID));
        assertThat(event.text(), is(TEXT));
        assertThat(event.optimisticMd5(), is(OPTIMISTIC_MD5));
    }
}

