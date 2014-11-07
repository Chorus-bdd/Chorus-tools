package org.chorusbdd.history;

import org.chorusbdd.history.ModificationImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ModificationImplTest {

    @Mock Date mockDate;

    @Test
    public void hasPropertiesSetByConstructor() {
        final ModificationImpl modification = new ModificationImpl("id-prop", "autohor-name-prop", "author-email-prop", mockDate, "comment-prop");

        assertThat(modification.id(), is("id-prop"));
        assertThat(modification.authorName(), is("autohor-name-prop"));
        assertThat(modification.authorEmailAddress(), is("author-email-prop"));
        assertThat(modification.dateTime(), is(mockDate));
        assertThat(modification.comment(), is("comment-prop"));
    }

}
