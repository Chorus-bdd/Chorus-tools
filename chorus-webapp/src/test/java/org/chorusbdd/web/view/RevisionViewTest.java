package org.chorusbdd.web.view;

import org.chorusbdd.web.view.structure.FeatureView;
import org.chorusbdd.web.view.structure.PakageView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RevisionViewTest {

    @Mock FeatureView featureView1;
    @Mock FeatureView featureView2;

    @Mock PakageView pakageView1;
    @Mock PakageView pakageView2;

    @Test
    public void defaultsPropertiesToEmpty() {
        final RevisionView mv = new RevisionView();
        assertThat(mv.getId(), is(""));
        assertThat(mv.getAuthorName(), is(""));
        assertThat(mv.getComment(), is(""));
        assertThat(mv.getDateTime(), is(""));
        assertThat(mv.getAuthorEmailAddress(), is(""));
    }

    @Test
    public void hasProperties() {
        final RevisionView mv = new RevisionView();
        mv.setId("**ID");
        mv.setAuthorName("**authorname");
        mv.setAuthorEmailAddress("**authoremail");
        mv.setComment("**comment");
        mv.setDateTime("**datetime");

        assertThat(mv.getId(),         is("**ID"));
        assertThat(mv.getAuthorName(), is("**authorname"));
        assertThat(mv.getComment(),    is("**comment"));
        assertThat(mv.getDateTime(),         is("**datetime"));
        assertThat(mv.getAuthorEmailAddress(), is("**authoremail"));
    }
}
