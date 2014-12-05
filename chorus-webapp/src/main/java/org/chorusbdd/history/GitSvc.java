package org.chorusbdd.history;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.chorusbdd.history.GitUtils.changeset;
import static org.chorusbdd.history.GitUtils.fileAtRevision;
import static org.chorusbdd.history.GitUtils.logWithFollow;
import static org.chorusbdd.history.GitUtils.printAll;
import static org.chorusbdd.util.FileUtils.isSubpath;
import static org.chorusbdd.util.StreamUtils.stream;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType;

@NotThreadSafe
public class GitSvc implements Svc {
    private static final Logger LOG = LoggerFactory.getLogger(GitSvc.class);

    private final Repository repository;
    private final Git git;
    private final Path repositoryPath;

    public GitSvc(final String directory) throws IOException {
        git = initializeRepository(directory);
        repository = git.getRepository();
        repositoryPath = repository.getDirectory().toPath().getParent();
    }

    @Override
    public Stream<Revision> log() {
        try {
            final LogCommand logCommand = git.log();
            return stream(logCommand.call()).map(this::asRevision);
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    public Stream<Revision> log(final Path path) {
        try {
            return stream(logWithFollow(repository, toRelativePath(path).toString())).map(this::asRevision);
        } catch (final IOException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    public String retrieve(final String revision, final Path path) {
        try {
            return fileAtRevision(repository, revision, toRelativePath(path).toString());
        } catch (IOException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    public void commitAll(final String authorName, final String authorEmail, final String comment) {
        try {
            stageChanges(git);
            logStatus(git);
            final ObjectId commitId = commit(authorName, authorEmail, comment);
            LOG.info("{} Committed all staged changes in repository '{}'", commitId.toString(), repository);
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    @SuppressWarnings("Convert2MethodRef")
    public List<FileChange> changesetForRevision(final String revisionName) {
        try {
            return changeset(repository, revisionName).stream()
                    .map(d -> new FileChange(d.getChangeType(), path(d)))
                    .collect(toList());
        } catch (final IOException e) {
            throw new SvcFailedException(e);
        }
    }

    // ------------------------------------------------------------ Git Helpers

    private void logStatus(final Git git) throws GitAPIException {
        final Status status = git.status().call();
        if (!status.getAdded().isEmpty()) { LOG.info("Added files {}", status.getAdded()); }
        if (!status.getChanged().isEmpty()) { LOG.info("Changed files {}", status.getChanged()); }
        if (!status.getRemoved().isEmpty()) { LOG.info("Removed files {}", status.getRemoved()); }
        if (!status.getModified().isEmpty()) { LOG.info("Modified files {}", status.getModified()); }
        if (!status.getMissing().isEmpty()) { LOG.info("Missing files {}", status.getMissing()); }
    }

    private Revision asRevision(final RevCommit commit) {
        final PersonIdent author = commit.getAuthorIdent();
        return new RevisionImpl(commit.name(), author.getName(), author.getEmailAddress(),
                author.getWhen(), commit.getFullMessage());
    }

    private Git initializeRepository(final String directory) {
        try {
            LOG.info("Initializing GIT repository '{}' for SVC", directory);
            return new InitCommand().setDirectory(new File(directory)).call();
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    private void stageChanges(final Git git) throws GitAPIException {
        stageNewFiles(git);
        stageMissingFilesAsDeleted(git);
    }

    private void stageNewFiles(final Git git) throws GitAPIException {
        git.add().addFilepattern(".").call();
    }

    private void stageMissingFilesAsDeleted(final Git git) throws GitAPIException {
        final Status status = git.status().call();
        for (final String missingFile : status.getMissing()) {
            git.rm().addFilepattern(missingFile).call();
        }
    }

    private ObjectId commit(final String authorName, final String authorEmail, final String comment) throws GitAPIException {
        final RevCommit commit = git.commit()
                .setMessage(comment)
                .setAuthor(authorName, authorEmail)
                .call();
        return commit.getId();
    }

    private Path path(final DiffEntry s) {
        final ChangeType changeType = s.getChangeType();
        switch (changeType) {
        		case DELETE: return Paths.get(s.getOldPath());
        		case ADD:    return Paths.get(s.getNewPath());
        		case COPY:   return Paths.get(s.getNewPath());
        		case MODIFY: return Paths.get(s.getNewPath());
        		case RENAME: return Paths.get(s.getNewPath());
                default:
                    throw new RuntimeException("Unrecognized change type: " + changeType);
        }
    }

    private Path toRelativePath(final Path path) {
        if (path.isAbsolute()) {
            if (isSubpath(repositoryPath, path)) {
                return repositoryPath.relativize(path);
            }
            throw new RuntimeException("Path must be relative to the git repository base path");
        }
        return path;
    }

    @SuppressWarnings("UnusedDeclaration") // debugging
    private void printLog() {
        try {
            printAll(repository, git.log().call());
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }
}
