package org.chorusbdd.history;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GitSvcTest {

    interface Committer {
        String name();
        String email();
    }
    private Committer BEAR = new Committer() {
        public String name() { return "Bear"; }
        public String email() { return "rawr@test.com"; }
    };
    private Committer FOX = new Committer() {
        public String name() { return "Fox"; }
        public String email() { return "foxxy@test.com"; }
    };

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
    public void newFileCreatesRevisionInLog() throws IOException {
        createAndCommitFile("A FileName", "the contents", "comment: file one was created", FOX);
        final List<Revision> log = gitSvc.log().collect(Collectors.toList());

        assertThat(log.size(), is(1));
        assertThat(log.get(0).authorName(), is(FOX.name()));
        assertThat(log.get(0).authorEmailAddress(), is(FOX.email()));
        assertThat(log.get(0).comment(), is("comment: file one was created"));
    }

    @Test
    public void modifyingFileCreatesRevisionInLog() throws IOException {
        // prepare
        createAndCommitFile("myFile", "the contents", "comment: file created", BEAR);
        modifyAndCommitFile("myFile", "the modified contents", "comment: file modified", FOX);

        // run
        final List<Revision> log = gitSvc.log().collect(Collectors.toList());

        // verify
        assertThat(log.size(), is(2));
        assertThat(log.get(0).authorName(), is(FOX.name()));
        assertThat(log.get(0).authorEmailAddress(), is(FOX.email()));
        assertThat(log.get(0).comment(), is("comment: file modified"));
        assertThat(log.get(1).authorName(), is(BEAR.name()));
        assertThat(log.get(1).authorEmailAddress(), is(BEAR.email()));
        assertThat(log.get(1).comment(), is("comment: file created"));
    }

    @Test
    public void deletingFileCreatesRevisionInLog() throws IOException {
        // prepare
        createAndCommitFile("myFile", "the contents", "comment: file created", BEAR);
        modifyAndCommitFile("myFile", "the modified contents", "comment: file modified", FOX);
        deleteAndCommitFile("myFile", "comment: file deleted", FOX);

        // run
        final List<Revision> log = gitSvc.log().collect(Collectors.toList());

        // verify
        assertThat(log.size(), is(3));
        assertThat(log.get(0).comment(), is("comment: file deleted"));
        assertThat(log.get(1).comment(), is("comment: file modified"));
        assertThat(log.get(2).comment(), is("comment: file created"));
    }

    @Test
    public void logFollowsSingleFileWhenModified() throws IOException {
        // prepare
        createAndCommitFile("FileName1", "file 1 contents", "comment: file one created", BEAR);
        createAndCommitFile("FileName2", "file 2 contents", "comment: file two created", BEAR);
        modifyAndCommitFile("FileName1", "modified file 1 contents", "comment: file one modified", BEAR);
        modifyAndCommitFile("FileName2", "modified file 2 contents", "comment: file two modified", BEAR);

        // run
        final List<Revision> log = gitSvc.log(relativePath("FileName1")).collect(Collectors.toList());

        // verify
        assertThat(log.size(), is(2));
        assertThat(log.get(0).comment(), is("comment: file one modified"));
        assertThat(log.get(1).comment(), is("comment: file one created"));
    }

    @Test
    public void logFollowsSingleFileWhenRenamed() throws IOException {
        // prepare
        createAndCommitFile("FileName1", "file 1 contents", "comment: file one created", BEAR);
        createAndCommitFile("FileName2", "file 2 contents", "comment: file two created", FOX);
        renameAndCommitFile("FileName2", "FileName3", "comment: renamed from fn2 to fn3", FOX);
        modifyAndCommitFile("FileName1", "modified file 1 contents", "comment: file one modified", BEAR);
        modifyAndCommitFile("FileName3", "modified renamed file contents", "comment: file three modified", FOX);

        // run
        final List<Revision> log = gitSvc.log(relativePath("FileName3")).collect(Collectors.toList());

        // verify
        assertThat(log.size(), is(3));
        assertThat(log.get(0).comment(), is("comment: file three modified"));
        assertThat(log.get(1).comment(), is("comment: renamed from fn2 to fn3"));
        assertThat(log.get(2).comment(), is("comment: file two created"));
    }

    @Test
    public void retrievesFileInRevision() throws IOException {
         // prepare
        createAndCommitFile("FileName1", "file 1 contents", "comment: created", BEAR);
        modifyAndCommitFile("FileName1", "modified file 1 contents", "comment: modified", BEAR);
        modifyAndCommitFile("FileName1", "modified again contents", "comment:  modified again", FOX);

        // run
        final List<Revision> log = gitSvc.log(relativePath("FileName1")).collect(Collectors.toList());
        final String contents_rev0 = gitSvc.retrieve(log.get(0).id(), relativePath("FileName1"));
        final String contents_rev1 = gitSvc.retrieve(log.get(1).id(), relativePath("FileName1"));
        final String contents_rev2 = gitSvc.retrieve(log.get(2).id(), relativePath("FileName1"));

        // verify
        assertThat(contents_rev0, is("modified again contents"));
        assertThat(contents_rev1, is("modified file 1 contents"));
        assertThat(contents_rev2, is("file 1 contents"));
    }

    @Test
    public void revisionChangesetIncludesAllFilesChanged() throws IOException {
        // prepare
        createAndCommitFile("FileName1", "file1 contents", "comment: file one created", BEAR);
        createAndCommitFile("FileName2a", "file2 contents", "comment: file two created", FOX);
        renameAndCommitFile("FileName2a", "FileName2b", "comment: renamed file", FOX);
        createFile("FileName3", "file3 contents");
        modifyFile("FileName1", "modified file1 contents");
        commit("comment: multiple files changes", BEAR);
        deleteAndCommitFile("FileName1", "comment: file1 deleted", FOX);

        // run
        final List<Revision> log = gitSvc.log().collect(Collectors.toList());
        final Set<Path> changeset_rev0 = gitSvc.changesetForRevision(log.get(0).id());
        final Set<Path> changeset_rev1 = gitSvc.changesetForRevision(log.get(1).id());
        final Set<Path> changeset_rev2 = gitSvc.changesetForRevision(log.get(2).id());
        final Set<Path> changeset_rev3 = gitSvc.changesetForRevision(log.get(3).id());
        final Set<Path> changeset_rev4 = gitSvc.changesetForRevision(log.get(4).id());

        // verify
        assertThat(changeset_rev0, hasSize(1));
        assertThat(changeset_rev0, containsInAnyOrder(Paths.get("FileName1")));

        assertThat(changeset_rev1, hasSize(2));
        assertThat(changeset_rev1, containsInAnyOrder(Paths.get("FileName1"), Paths.get("FileName3")));

        assertThat(changeset_rev2, hasSize(2));
        assertThat(changeset_rev2, containsInAnyOrder(Paths.get("FileName2a"), Paths.get("FileName2b")));

        assertThat(changeset_rev3, hasSize(1));
        assertThat(changeset_rev3, containsInAnyOrder(Paths.get("FileName2a")));

        assertThat(changeset_rev4, hasSize(1));
        assertThat(changeset_rev4, containsInAnyOrder(Paths.get("FileName1")));
    }


    private Path firstItem(final Set<Path> changeset_rev0) {
        return changeset_rev0.iterator().next();
    }


    // ---------------------------------------------------------------- Utility

    private Path absolutePath(final String fileName) {
        return new File(rootFolder, fileName).toPath();
    }

    private Path relativePath(final String fileName) {
        return Paths.get(fileName);
    }

    private void createAndCommitFile(final String filename, final String contents, final String comment, final Committer committer) throws IOException {
        createFile(filename, contents);
        commit(comment, committer);
    }

    private void modifyAndCommitFile(final String filename, final String contents, final String comment, final Committer committer) throws IOException {
        modifyFile(filename, contents);
        commit(comment, committer);
    }

    private void renameAndCommitFile(final String fromFilename, final String toFilename, final String comment, final Committer committer) throws IOException {
        renameFile(fromFilename, toFilename);
        commit(comment, committer);
    }
    private void deleteAndCommitFile(final String filename, final String comment, final Committer committer) throws IOException {
        deleteFile(filename);
        commit(comment, committer);
    }

    private void createFile(final String fileName, final String contents) throws IOException {
        final File file = new File(rootFolder, fileName);
        file.createNewFile();
        final BufferedWriter writer = Files.newBufferedWriter(file.toPath());
        writer.write(contents);
        writer.flush();
        writer.close();
    }

    private void modifyFile(final String fileName, final String contents) throws IOException {
        final BufferedWriter writer = Files.newBufferedWriter(absolutePath(fileName));
        writer.write(contents);
        writer.flush();
        writer.close();
    }

    private void renameFile(final String fromFilename, final String toFilename) throws IOException {
        final Path target = new File(rootFolder, fromFilename).toPath();
        final Path destination = new File(rootFolder, toFilename).toPath();
           Files.move(target, destination);
    }

    private void deleteFile(final String filename) throws IOException {
        final Path target =  new File(rootFolder, filename).toPath();
        Files.delete(target);
    }

    private void commit(final String comment, final Committer committer) {
        gitSvc.commitAll(committer.name(), committer.email(), comment);
    }
}
