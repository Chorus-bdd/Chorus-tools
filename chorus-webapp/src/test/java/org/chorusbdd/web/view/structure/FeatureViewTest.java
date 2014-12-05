package org.chorusbdd.web.view.structure;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FeatureViewTest {

    @Test
    public void defaultsPropertiesToEmptyStrings() {
        final FeatureView fv = new FeatureView();
        assertThat(fv.getId(), is(""));
        assertThat(fv.getName(), is(""));
        assertThat(fv.getText(), is(""));
        assertThat(fv.getLink(), is(""));
        assertThat(fv.getMd5(), is(""));
    }

    @Test
    public void hasProperties() {
        final FeatureView fv = new FeatureView();
        fv.setId("**ID");
        fv.setName("**NAME");
        fv.setText("**BODY");
        fv.setLink("**LINK");
        fv.setMd5("**MD5");

        assertThat(fv.getId(), is("**ID"));
        assertThat(fv.getName(), is("**NAME"));
        assertThat(fv.getText(), is("**BODY"));
        assertThat(fv.getLink(), is("**LINK"));
        assertThat(fv.getMd5(), is("**MD5"));
    }
}
