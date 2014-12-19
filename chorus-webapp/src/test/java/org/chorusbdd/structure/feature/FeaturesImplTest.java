package org.chorusbdd.structure.feature;

import org.chorusbdd.structure.feature.query.FeatureImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class FeaturesImplTest {

    @Mock FeatureEvents events;
    @Mock FeatureRepository repository;
    @Mock FeatureCommands commands;

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureEvents() {
        new FeaturesImpl(null, repository, commands);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullRepository() {
        new FeaturesImpl(events, null, commands);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullCommands() {
        new FeaturesImpl(events, repository, null);
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final FeaturesImpl event = new FeaturesImpl(events, repository, commands);

        // verify
        assertThat(event.repository(), is(repository));
        assertThat(event.events(), is(events));
        assertThat(event.commands(), is(commands));
    }
}
