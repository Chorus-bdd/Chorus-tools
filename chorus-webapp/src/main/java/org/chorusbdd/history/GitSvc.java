package org.chorusbdd.history;

import org.chorusbdd.util.StreamUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@NotThreadSafe
public class GitSvc implements Svc {
    private final Repository repository;
    private final Git git;

    public GitSvc(final String directory) throws IOException {
        initializeRepository(directory);

        repository = new FileRepositoryBuilder()
                .setGitDir(new File(directory + "\\.git"))
                .setMustExist(true)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        git = new Git(repository);
    }

    @Override
    public Stream<Modification> log() {
        try {
            final LogCommand logCommand = git.log();
            return StreamUtils.stream(logCommand.call()).map(this::asModification);
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    public Stream<Modification> log(final Path file) {
        try {
            final LogCommand logCommand = git.log();
            //logCommand.addPath("MessageBridge/PublishesAndSubscribes.feature");
            logCommand.addPath(file.toString());
            return StreamUtils.stream(logCommand.call()).map(this::asModification);
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    @Override
    public void commitAll(final String authorName, final String authorEmail, final String comment) {
        try {
            final Git git = new Git(repository);
            git.add()
                    .addFilepattern(".")
                    .call();
            git.commit()
                    .setMessage(comment)
                    .setAuthor(authorName, authorEmail)
                    .call();
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }

    private Modification asModification(RevCommit commit) {
        final PersonIdent author = commit.getAuthorIdent();
        return new ModificationImpl(commit.name(), author.getName(), author.getEmailAddress(),
                                                  author.getWhen(), commit.getFullMessage());
    }

    private Git initializeRepository(final String directory) {
        try {
            return new InitCommand().setDirectory(new File(directory)).call();
        } catch (GitAPIException e) {
            throw new SvcFailedException(e);
        }
    }


    //
    //public static void main(String ... args) throws IOException, GitAPIException {
    //     FileRepositoryBuilder builder = new FileRepositoryBuilder();
    //     Repository repository = builder.setGitDir(new File("c:\\dev\\tmp\\testroot"+"\\.git"))
    //             .setMustExist(true)
    //             .readEnvironment() // scan environment GIT_* variables
    //             .findGitDir() // scan up the file system tree
    //             .build();
    //     System.out.println(repository.toString());
    //
    //     Git git = new Git(repository);
    //     //commitAllChanges(repository);
    //     try {
    //         final LogCommand logCommand = git.log();
    //         //new LogFollowCommand();
    //         //logCommand.addPath("MessageBridge/MV/Record/PublishesAndSubscribesPartialRecords.feature");
    //         //logCommand.addPath("MessageBridge/PublishesAndSubscribes.feature");
    //         //logCommand.
    //         for(RevCommit commit : logCommand.call()) {
    //             printCommitInformation(repository, commit);
    //         }
    //     } catch (NoHeadException e) {
    //         System.out.println("no head exception : " + e);
    //     }
    // }
    //
    // private static void commitAllChanges(final Repository repository) throws IOException, GitAPIException {
    //
    // }
    //
    // private static void printCommitInformation(final Repository repository, final RevCommit commit) throws IOException {
    //     System.out.println("------------");
    //     System.out.println(commit);
    //     System.out.println(commit.getAuthorIdent().getName());
    //     System.out.println(commit.getAuthorIdent().getWhen());
    //     System.out.println(commit.getFullMessage());
    //     //RevWalk walk = new RevWalk(repository);
    //     //RevTree tree = walk.parseTree(commit.getTree());
    //     //printAllFilesInTree(repository, commit);
    //     //printDiff(repository, commit);
    //     printDiffForFile(repository, commit);
    //     //System.out.println(tree);
    //     System.out.println("*************");
    // }
    //
    // private static void printAllFilesInTree(final Repository repository, final RevCommit commit) throws IOException {
    //     final TreeWalk treeWalk = new TreeWalk(repository);
    //     treeWalk.addTree(commit.getTree());
    //     treeWalk.setRecursive(true);
    //     while (treeWalk.next()) {
    //         System.out.println("found: " + treeWalk.getPathString());
    //     }
    // }
    //
    // private static void printDiff(final Repository repository, final RevCommit commit) throws IOException {
    //     final TreeWalk treeWalk = new TreeWalk(repository);
    //     treeWalk.addTree(commit.getTree());
    //     for (RevCommit parentCommit : commit.getParents()) {
    //         treeWalk.addTree(parentCommit.getTree());
    //     }
    //     treeWalk.setFilter(TreeFilter.ANY_DIFF);
    //
    //     treeWalk.setRecursive(true);
    //    while (treeWalk.next()) {
    //        System.out.println("DIFF: " + treeWalk.getPathString());
    //    }
    // }
    // private static void printDiffForFile(final Repository repository, final RevCommit commit) throws IOException {
    //     final TreeWalk treeWalk = new TreeWalk(repository);
    //     treeWalk.addTree(commit.getTree());
    //     for (RevCommit parentCommit : commit.getParents()) {
    //         treeWalk.addTree(parentCommit.getTree());
    //     }
    //     //treeWalk.setFilter(AndTreeFilter.create(PathFilterGroup.createFromStrings("MessageBridge/MV/Record/PublishesAndSubscribesPartialRecords.feature"), TreeFilter.ANY_DIFF));
    //     treeWalk.setFilter(AndTreeFilter.create(PathFilterGroup.createFromStrings("MessageBridge/PublishesAndSubscribes.feature"), TreeFilter.ANY_DIFF));
    //
    //     treeWalk.setRecursive(true);
    //    while (treeWalk.next()) {
    //        System.out.println("DIFF: " + treeWalk.getPathString());
    //    }
    // }

}
