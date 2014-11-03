package org.chorusbdd.web.view.structure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Arrays.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PakageViewTest {

    @Mock FeatureView featureView1;
    @Mock FeatureView featureView2;

    @Mock PakageView pakageView1;
    @Mock PakageView pakageView2;

    @Test
    public void defaultsPropertiesToEmpty() {
        final PakageView pv = new PakageView();
        assertThat(pv.getId(), is(""));
        assertThat(pv.getName(), is(""));
        assertThat(pv.getLink(), is(""));
        assertThat(pv.getFeatures(), notNullValue());
        assertThat(pv.getFeatures().length, is(0));
        assertThat(pv.getSubpackages(), notNullValue());
        assertThat(pv.getSubpackages().length, is(0));
    }

    @Test
    public void hasProperties() {
        final PakageView pv = new PakageView();
        pv.setId("**ID");
        pv.setName("**NAME");
        pv.setLink("**LINK");
        pv.setSubpackages(Stream.of(pakageView1, pakageView2));
        pv.setFeatures(Stream.of(featureView1, featureView2));

        assertThat(pv.getId(), is("**ID"));
        assertThat(pv.getName(), is("**NAME"));
        assertThat(pv.getLink(), is("**LINK"));
        assertThat(asList(pv.getFeatures()), hasItems(featureView1, featureView2));
        assertThat(pv.getFeatures().length, is(2));
        assertThat(asList(pv.getSubpackages()), hasItems(pakageView1, pakageView2));
        assertThat(pv.getSubpackages().length, is(2));
    }
}
