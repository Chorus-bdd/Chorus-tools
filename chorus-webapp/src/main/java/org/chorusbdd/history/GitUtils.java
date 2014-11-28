package org.chorusbdd.history;

import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class GitUtils {

    //
    //public static void main(String ... args) throws IOException, GitAPIException {
    //    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    //    Repository repository = builder.setGitDir(new File("c:\\dev\\tmp\\testroot"+"\\.git"))
    //            .setMustExist(true)
    //            .readEnvironment() // scan environment GIT_* variables
    //            .findGitDir() // scan up the file system tree
    //            .build();
    //    System.out.println(repository.toString());
    //
    //    Git git = new Git(repository);
    //    //commitAllChanges(repository);
    //    try {
    //        final LogCommand logCommand = git.log();
    //        //new LogFollowCommand();
    //        //logCommand.addPath("MessageBridge/MV/Record/PublishesAndSubscribesPartialRecords.feature");
    //        //logCommand.addPath("MessageBridge/PublishesAndSubscribes.feature");
    //        //logCommand.
    //        for(RevCommit commit : logCommand.call()) {
    //            printCommitInformation(repository, commit);
    //        }
    //    } catch (NoHeadException e) {
    //        System.out.println("no head exception : " + e);
    //    }
    //}

    public static Set<String> changeset(final Repository repository, final String revision) throws IOException {
        final RevCommit commit = asCommit(repository, revision);
        final TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.reset();
        try {
            treeWalk.addTree(commit.getTree());
            if (commit.getParentCount() != 0) {
                RevCommit parent = asCommit(repository, commit.getParent(0).getId());
                treeWalk.addTree(parent.getTree());
            }
            treeWalk.setFilter(TreeFilter.ANY_DIFF);
            treeWalk.setRecursive(true);
            return asSet(treeWalk);
        } finally {
            treeWalk.release();
        }
    }

    public static Set<String> filesAtRevision(final Repository repository, final String revision) throws IOException {
        final RevCommit commit = asCommit(repository, revision);
        final TreeWalk treeWalk = new TreeWalk(repository);
        try {
            treeWalk.addTree(commit.getTree());
            treeWalk.setRecursive(true);
            return asSet(treeWalk);
        } finally {
            treeWalk.release();
        }
    }

    public static String fileAtRevision(final Repository repository, final String revision, final String path) throws IOException {
        final ObjectId revisionId = repository.resolve(revision);
        final ObjectReader reader = repository.newObjectReader();
        try {
            final RevWalk revisionWalker = new RevWalk(reader);
            final RevTree tree = revisionWalker.parseCommit(revisionId).getTree();
            return readFile(path, reader, tree);
        } finally {
            reader.release();
        }
    }

    public static RevCommitList<RevCommit> logWithFollow(final Repository repository, final String path) throws IOException {
        final RevWalk revWalk = new RevWalk(repository);
        try {
            revWalk.markStart(revWalk.parseCommit(head(repository)));
            revWalk.setTreeFilter(followFilter(repository, path.replace("\\", "/")));
            return commitList(revWalk);
        } finally {
            revWalk.release();
        }
    }

    private static Set<String> asSet(final TreeWalk treeWalk) throws IOException {
        final Set<String> changeset = new LinkedHashSet<>();
        while (treeWalk.next()) {
            changeset.add(treeWalk.getPathString());
        }
        return changeset;
    }

    private static String readFile(final String path, final ObjectReader reader, final RevTree tree) throws IOException {
        final TreeWalk treeWalker = TreeWalk.forPath(reader, path, tree);
        try {
            if (treeWalker == null) {
                return "";
            }
            byte[] data = reader.open(treeWalker.getObjectId(0)).getBytes();
            return new String(data, "utf-8");
        } finally {
            treeWalker.release();
        }
    }

    private static FollowFilter followFilter(final Repository repository, final String path) {
        final Config config = new Config(repository.getConfig());
        config.setInt("diff", null, "renameLimit", Integer.MAX_VALUE);
        config.setString("diff", null, "renames", "copies");
        final DiffConfig diffConfig = config.get(DiffConfig.KEY);
        return FollowFilter.create(path, diffConfig);
    }

    private static ObjectId head(final Repository repository) throws IOException {
        return repository.resolve("HEAD");
    }

    public static RevCommit headCommit(final Repository repository) throws IOException {
        final RevWalk walk = new RevWalk(repository);
        try {
            return walk.parseCommit(head(repository));
        } finally {
            walk.release();
        }
    }

    public static RevCommit asCommit(final Repository repository, final String revision) throws IOException {
        return asCommit(repository, repository.resolve(revision));
    }

    public static RevCommit asCommit(final Repository repository, final ObjectId revisionId) throws IOException {
        final RevWalk walk = new RevWalk(repository);
        try {
            return walk.parseCommit(revisionId);
        } finally {
            walk.release();
        }
    }

    private static RevCommitList<RevCommit> commitList(final RevWalk revWalk) throws IOException {
        final RevCommitList<RevCommit> list = new RevCommitList<>();
        list.source(revWalk);
        list.fillTo(Integer.MAX_VALUE);
        return list;
    }

    // ------------------------------------------------------- Printing helpers

    public static void printAll(final Repository repository, final Iterable<RevCommit> commits) {
        commits.forEach(c -> {
            System.out.println("-- COMMIT --");
            printCommitInformation(c);
            System.out.println("-- ALL FILES --");
            printAllFilesInTree(repository, c);
            System.out.println("-- DIFF --");
            printChangedFiles(repository, c);
            System.out.println("*************\n\n");
        });

    }

    public static void printCommitInformation(final RevCommit commit) {
        System.out.println(commit);
        System.out.println(commit.getAuthorIdent().getName());
        System.out.println(commit.getAuthorIdent().getWhen());
        System.out.println(commit.getFullMessage());
    }

    public static void printAllFilesInTree(final Repository repository, final RevCommit commit)  {
        try {
            filesAtRevision(repository, commit.getId().name()).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printChangedFiles(final Repository repository, final RevCommit commit) {
        try {
            changeset(repository, commit.getId().name()).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //public static void printDiffForFile(final Repository repository, final RevCommit commit) throws IOException {
    //    final TreeWalk treeWalk = new TreeWalk(repository);
    //    treeWalk.addTree(commit.getTree());
    //    for (RevCommit parentCommit : commit.getParents()) {
    //        treeWalk.addTree(parentCommit.getTree());
    //    }
    //    //treeWalk.setFilter(AndTreeFilter.create(PathFilterGroup.createFromStrings("MessageBridge/MV/Record/PublishesAndSubscribesPartialRecords.feature"), TreeFilter.ANY_DIFF));
    //    treeWalk.setFilter(AndTreeFilter.create(PathFilterGroup.createFromStrings("MessageBridge/PublishesAndSubscribes.feature"), TreeFilter.ANY_DIFF));
    //    treeWalk.setRecursive(true);
    //   while (treeWalk.next()) {
    //       System.out.println("DIFF: " + treeWalk.getPathString());
    //   }
    //}
}
