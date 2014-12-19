package org.chorusbdd.handlers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.chorusbdd.experimental.HttpServiceImpl;
import org.chorusbdd.experimental.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static java.lang.System.currentTimeMillis;
import static org.chorusbdd.experimental.RequestBuilder.ContentType.APPLICATION_URL_ENCODED;

@Handler("Chorus OLD234DFS ")
public class ChorusWebAppHandlerOld {
    private static final Logger LOG = LoggerFactory.getLogger(ChorusWebAppHandlerOld.class);

    private static String testRoot = "C:\\dev\\tmp\\znew1";
    private final HttpServiceImpl httpService;
    private final String baseUrl;
    private Process process;

    public ChorusWebAppHandlerOld() {
        httpService = new HttpServiceImpl();
        baseUrl = "http://localhost:8085";
    }

    @Step("create an empty test root")
    public void emptyTheTestRoot() throws IOException {
        if (isRunning()) {
            LOG.warn("Unable to delete the test root (if present) because chorus is running");
            return;
        }
        deleteDirectoryTree(Paths.get(testRoot));
        Files.createDirectory(Paths.get(testRoot));
    }

    @Step("wait for chorus-web to start")
    public void waitForApp() throws InterruptedException {
        final long start_ms = currentTimeMillis();
        while (!isRunning()) {
            if (currentTimeMillis() - start_ms > 20000) {
                throw new RuntimeException("Chorus app failed to start before timeout");
            }
            Thread.sleep(1000);
        }
    }

    @Step("^the feature (.*) is created with the text \"(.*)\"$")
    public void featureIsCreatedWithTheText(final String featureName, final String featureText) throws IOException {
        final Response response = putFeature(featureName, featureText);

        ChorusAssert.assertEquals( 201, response.statusCode());
        ChorusAssert.assertEquals("Created", response.message());
    }

    @Step("^the feature (.*) is modified with the text \"(.*)\"$")
    public void featureIsModifiedWithTheText(final String featureName, final String featureText) throws IOException, ScriptException {
        final Response response = modifyFeature(featureName, featureText);

        ChorusAssert.assertEquals( 201, response.statusCode());
        ChorusAssert.assertEquals("Created", response.message());
    }

    @Step("^the feature (.*) must have the text \"(.*)\"$")
    public void ensureFeatureHasTheText(final String featureName, final String featureText) throws IOException, ScriptException {
        final Response response = getFeature(featureName);
        final ScriptObjectMirror json = response.jsonBody();

        ChorusAssert.assertEquals(String.valueOf(json.get("text")).trim(), featureText.trim());
    }







    // -- for service

    private Response getFeature(final String featureName) {
        return httpService.get().withUri(baseUrl + "/resource/features/" + featureName).execute();
    }

    private Response modifyFeature(final String featureName, final String featureText) {
        final Response response1 = getFeature(featureName);
        final ScriptObjectMirror json = response1.jsonBody();
        final String md5 = String.valueOf(json.get("md5"));
        return putFeature(featureName, featureText, md5);
    }

    private Response putFeature(final String featureName, final String featureText) {
        final Response response = httpService.put()
                .withUri(baseUrl + "/resource/features/" + featureName)
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("text=" + featureText)
                .execute();
        LOG.debug("Put feature {} got http response: {}", featureName, response);
        return response;
    }

    private Response putFeature(final String featureName, final String featureText, final String md5) {
        final Response response = httpService.put()
                .withUri(baseUrl + "/resource/features/" + featureName)
                .withContentType(APPLICATION_URL_ENCODED)
                .withEntity("text=" + featureText)
                .withHeader("If-Match", md5)
                .execute();
        LOG.debug("Put feature {} got http response: {}", featureName, response);
        return response;
    }

    private boolean isRunning() {
        try {
            final Response response = httpService.get().withUri(baseUrl + "/resource/status").execute();
            //System.out.println(response);
            return "\"OK\"".equals(response.body());
        } catch (Throwable e) {
            return false;
        }
    }



    // -- Other

    public static void deleteDirectoryTree(final Path path) throws IOException {
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

    private static void setPermission(Path path, PosixFilePermission permission)
         throws IOException {
       System.out.println("\nSetting permission for " + path.getFileName());
       PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class);
       PosixFileAttributes attributes = view.readAttributes();

       Set<PosixFilePermission> permissions = attributes.permissions();
       permissions.add(permission);

       view.setPermissions(permissions);
       System.out.println();
     }
}

