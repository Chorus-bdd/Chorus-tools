package org.chorusbdd.handlers;

import org.chorusbdd.FeatureService;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.lang.System.currentTimeMillis;

@Handler("Chorus Webapp")
public class ChorusWebAppHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChorusWebAppHandler.class);
    public static final int WAIT_FOR_STARTUP_TIMEOUT_MS = 20000;
    public static final int WAIT_FOR_STARTUP_POLL_DELAY_MS = 1000;

    private static String testRoot = "C:\\dev\\tmp\\znew1";
    private FeatureService featureService = new FeatureService();

    public ChorusWebAppHandler() {
        // do nothing
    }

    @Step("create an empty test root")
    public void emptyTheTestRoot() throws IOException {
        if (featureService.isRunning()) {
            LOG.warn("Unable to delete the test root (if present) because chorus is running");
            return;
        }
        createNewTestRoot();
    }

    @Step("wait for chorus-web to start")
    public void waitForApp() throws InterruptedException {
        final long start_ms = currentTimeMillis();
        while (!featureService.isRunning()) {
            if (currentTimeMillis() - start_ms > WAIT_FOR_STARTUP_TIMEOUT_MS) {
                throw new RuntimeException("Chorus app failed to start before timeout");
            }
            Thread.sleep(WAIT_FOR_STARTUP_POLL_DELAY_MS);
        }
    }

    // -------------------------------------------------------- Private methods

    private void createNewTestRoot() throws IOException {
        deleteDirectoryTree(Paths.get(testRoot));
        Files.createDirectory(Paths.get(testRoot));
    }

    private static void deleteDirectoryTree(final Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attr) throws IOException {
                    Files.setAttribute(dir, "dos:readonly", false); // i hate you windows
                    dir.toFile().setWritable(true);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    Files.setAttribute(file, "dos:readonly", false); // i really hate you windows
                    file.toFile().setWritable(true);
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
                    if (e != null) {
                        throw e;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}

