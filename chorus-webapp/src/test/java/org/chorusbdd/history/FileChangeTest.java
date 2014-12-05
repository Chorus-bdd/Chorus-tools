package org.chorusbdd.history;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.eclipse.jgit.diff.DiffEntry.ChangeType.ADD;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.DELETE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FileChangeTest {

    @Mock Path mockPath;

    @Test(expected = NullPointerException.class)
    public void requireNonNullEvent() {
        new FileChange(null, mockPath);
    }

    @Test(expected = NullPointerException.class)
    public void requireNonNullPath() {
        new FileChange(DELETE, null);
    }

    @Test
    public void hasPropertiesFromConstructor_1() {
        final FileChange fileChange = new FileChange(DELETE, mockPath);

        assertThat(fileChange.event(), is(DELETE));
        assertThat(fileChange.path(), is(mockPath));
    }

    @Test
    public void hasPropertiesFromConstructor_2() {
        final FileChange fileChange = new FileChange(ADD, mockPath);

        assertThat(fileChange.event(), is(ADD));
        assertThat(fileChange.path(), is(mockPath));
    }
}
