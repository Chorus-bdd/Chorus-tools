package org.chorusbdd.structure.pakage.query;

import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.query.FeatureImpl;
import org.chorusbdd.structure.pakage.Pakage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PakageImplTest {

    @Mock Feature mockFeature1;
    @Mock Feature mockFeature2;
    @Mock Feature mockFeature3;

    @Mock Pakage mockPakage1;
    @Mock Pakage mockPakage2;
    @Mock Pakage mockPakage3;

    Stream<Feature> mockFeatures;
    Stream<Pakage> mockSubpackages;

    @Before
    public void setup() {

    }



    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageId() {
        new PakageImpl(null, "human name", Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyPakageId() {
        new PakageImpl("", "human name", Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullHumanName() {
        new PakageImpl("package-id-123", null, Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyHumanName() {
        new PakageImpl("package-id-123", "", Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullSubpackageStream() {
        new PakageImpl("package-id-123", "human name", null, Stream.of(mockFeature1,mockFeature2));
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureStream() {
        new PakageImpl("package-id-123", "human name", Stream.of(mockPakage1,mockPakage2), null);
        new PakageImpl("package-id-123", "human name", Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final PakageImpl pakage = new PakageImpl("package-id-123", "human name", Stream.of(mockPakage1,mockPakage2), Stream.of(mockFeature1,mockFeature2));

        // verify
        assertThat(pakage.id(), is("package-id-123"));
        assertThat(pakage.humanName(), is("human name"));
        assertThat(pakage.subpackages().size(), is(2));
        assertThat(pakage.subpackages(), hasItems(mockPakage1, mockPakage2));
        assertThat(pakage.features().size(), is(2));
        assertThat(pakage.features(), hasItems(mockFeature1, mockFeature2));
    }

}
