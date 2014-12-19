package org.chorusbdd.history;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RenameCallback;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GitUtils {

    public static List<DiffEntry> changeset(final Repository repository, final String revision) throws IOException {
        final RevCommit commit = asCommit(repository, revision);
        final AbstractTreeIterator parentTreeParser;
        if (commit.getParentCount() == 0) {
            parentTreeParser = new CanonicalTreeParser();
        } else {
            final RevCommit parent = asCommit(repository, commit.getParent(0).getId());
            parentTreeParser = treeParser(repository, parent);
        }
        final AbstractTreeIterator newTreeParser = treeParser(repository, commit);
        try {
            final List<DiffEntry> result = new Git(repository).diff()
                    .setOldTree(parentTreeParser)
                    .setNewTree(newTreeParser)
                    .call();
            return result;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
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
        final Map<String, String> filesNameAtRevision = filesNamesAtRevision(repository, toUnixSlashes(path));
        final ObjectId revisionId = repository.resolve(revision);
        final ObjectReader reader = repository.newObjectReader();
        try {
            final RevWalk revisionWalker = new RevWalk(reader);
            final RevTree tree = revisionWalker.parseCommit(revisionId).getTree();
            if (!filesNameAtRevision.containsKey(revision)) {
                return null;
            }
            return readFile(toUnixSlashes(filesNameAtRevision.get(revision)), reader, tree);
        } finally {
            reader.release();
        }
    }

    public static RevCommitList<RevCommit> logWithFollow(final Repository repository, final String path) throws IOException {
        final RevWalk revWalk = new RevWalk(repository);
        try {
            revWalk.markStart(revWalk.parseCommit(head(repository)));
            revWalk.setTreeFilter(followFilter(repository, toUnixSlashes(path)));
            return commitList(revWalk);
        } finally {
            revWalk.release();
        }
    }

    // -------------------------------------------------------- Private Methods

    private static Set<String> asSet(final TreeWalk treeWalk) throws IOException {
        final Set<String> changeset = new LinkedHashSet<>();
        while (treeWalk.next()) {
            changeset.add(treeWalk.getPathString());
        }
        return changeset;
    }

    private static String readFile(final String path, final ObjectReader reader, final RevTree tree) throws IOException {
        final TreeWalk treeWalker = TreeWalk.forPath(reader, path, tree);
        if (treeWalker == null) {
            return null;
        }
        try {
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

    private static RevCommit headCommit(final Repository repository) throws IOException {
        final RevWalk walk = new RevWalk(repository);
        try {
            return walk.parseCommit(head(repository));
        } finally {
            walk.release();
        }
    }

    private static RevCommit asCommit(final Repository repository, final String revision) throws IOException {
        return asCommit(repository, repository.resolve(revision));
    }

    private static RevCommit asCommit(final Repository repository, final ObjectId revisionId) throws IOException {
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

    private static AbstractTreeIterator treeParser(final Repository repository, final RevCommit commit) throws IOException {
        final RevWalk walk = new RevWalk(repository);
        final RevTree tree = walk.parseTree(commit.getTree().getId());
        final CanonicalTreeParser treeParser = new CanonicalTreeParser();
        final ObjectReader repositoryReader = repository.newObjectReader();
        try {
            treeParser.reset(repositoryReader, tree.getId());
        } finally {
            repositoryReader.release();
        }
        walk.dispose();
        return treeParser;
    }

    private static Map<String, String> filesNamesAtRevision(final Repository repository, final String path) throws IOException {
        final Queue<String> names = filesPreviousNames(repository, toUnixSlashes(path));
        final Queue<String> commits = commitLog(repository, toUnixSlashes(path));
        final Map<String, String> commitName = new HashMap<>();

        String filename = names.poll();
        for (final String commit : commits) {
            if (fileExistsInRevision(repository, commit, filename)) {
                commitName.put(commit, filename);
            } else {
                final List<DiffEntry> changeset = changeset(repository, commit);
                if (!isFileDeleted(filename, changeset)) {
                    filename = names.poll();
                    if (fileExistsInRevision(repository, commit, filename)) {
                        commitName.put(commit, filename);
                    }
                }
            }
        }
        return commitName;
    }

    private static boolean isFileDeleted(final String filename, final List<DiffEntry> changeset) {
        for (DiffEntry diffEntry : changeset) {
            if (diffEntry.getChangeType() == DiffEntry.ChangeType.DELETE && filename.equals(diffEntry.getOldPath())) {
                return true;
            }
        }
        return false;
    }

    private static Queue<String> commitLog(final Repository repository, final String path) throws IOException {
        final Queue<String> fileCommits = new ArrayDeque<>();
        final RevWalk revWalk = new RevWalk(repository);
        try {
            revWalk.markStart(revWalk.parseCommit(head(repository)));
            revWalk.setTreeFilter(followFilter(repository, toUnixSlashes(path)));
            for (final RevCommit commit : revWalk) {
                fileCommits.add(commit.getId().getName());
            }
            return fileCommits;
        } finally {
            revWalk.release();
        }
    }

    private static Queue<String> filesPreviousNames(final Repository repository, final String path) throws IOException {
        final Queue<String> filePreviousNames = new ArrayDeque<>();
        filePreviousNames.add(toUnixSlashes(path));
        final RevWalk revWalk = new RevWalk(repository);
        try {
            revWalk.markStart(revWalk.parseCommit(head(repository)));
            final FollowFilter filter = followFilter(repository, toUnixSlashes(path));
            filter.setRenameCallback(new RenameCallback() {
                @Override
                public void renamed(final DiffEntry entry) {
                    filePreviousNames.add(entry.getOldPath());
                }
            });
            revWalk.setTreeFilter(filter);
            revWalk.iterator();
            return filePreviousNames;
        } finally {
            revWalk.release();
        }
    }

    private static boolean fileExistsInRevision(final Repository repository, final String revision, final String path) throws IOException {
        final ObjectId revisionId = repository.resolve(revision);
        final ObjectReader reader = repository.newObjectReader();
        try {
            final RevWalk revisionWalker = new RevWalk(reader);
            final RevTree tree = revisionWalker.parseCommit(revisionId).getTree();
            return TreeWalk.forPath(reader, path, tree) != null;
        } finally {
            reader.release();
        }
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

    private static String toUnixSlashes(final String path) {
        return path.replace("\\", "/");
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
