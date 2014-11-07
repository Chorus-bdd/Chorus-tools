package org.chorusbdd.history;

import org.chorusbdd.history.GitSvc;
import org.chorusbdd.history.Modification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GitSvcTest {

    public static final String ROOT_FOLDER = "myFolder";
    @Rule public TemporaryFolder testFolder = new TemporaryFolder();
    private File rootFolder;
    private GitSvc gitSvc;

    @Before
    public void createGitRepository() throws IOException {
        rootFolder = testFolder.newFolder(ROOT_FOLDER);
        gitSvc = new GitSvc(rootFolder.getPath());
    }

    @Test
    public void newFileAppearsInLog() throws IOException {
        createFile("myFile", "this is my tests data");

        gitSvc.commitAll("John", "john@test.com", "my comment");
        final List<Modification> log = gitSvc.log().collect(Collectors.toList());

        assertThat(log.size(), is(1));
        assertThat(log.get(0).authorName(), is("John"));
        assertThat(log.get(0).authorEmailAddress(), is("john@test.com"));
        assertThat(log.get(0).comment(), is("my comment"));
    }

    @Test
    public void modifyFileAppearsInLog() throws IOException {
        createFile("myFile", "this is my tests data");
        gitSvc.commitAll("John", "john@test.com", "my initial comment");

        modifyFile("myFile", "some modified tests data");
        gitSvc.commitAll("Mary", "mary@test.com", "my modification comment");
        final List<Modification> log = gitSvc.log().collect(Collectors.toList());

        assertThat(log.size(), is(2));
        assertThat(log.get(0).authorName(), is("Mary"));
        assertThat(log.get(0).authorEmailAddress(), is("mary@test.com"));
        assertThat(log.get(0).comment(), is("my modification comment"));
        assertThat(log.get(1).authorName(), is("John"));
        assertThat(log.get(1).authorEmailAddress(), is("john@test.com"));
        assertThat(log.get(1).comment(), is("my initial comment"));
    }

    private void createFile(final String fileName, final String contents) throws IOException {
        final BufferedWriter writer = Files.newBufferedWriter(testFolder.newFile(fileName).toPath());
        writer.write(contents);
        writer.flush();
        writer.close();
    }

    private void modifyFile(final String fileName, final String contents) throws IOException {
        final BufferedWriter writer = Files.newBufferedWriter(new File(testFolder.getRoot(), fileName).toPath());
        writer.write(contents);
        writer.flush();
        writer.close();
    }
}
