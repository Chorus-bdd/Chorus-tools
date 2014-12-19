package org.chorusbdd.structure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.chorusbdd.util.StreamUtils.stream;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileSystemDatabaseImplTest {

    @Mock Path mockPackageRoot;
    @Rule public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() {
        when(mockPackageRoot.isAbsolute()).thenReturn(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void disallowsRelativeRootPath() {
        final Path mockPath = mock(Path.class);
        when(mockPath.isAbsolute()).thenReturn(false);

        new FileSystemDatabaseImpl(mockPath);
    }

    // -------------------------------------------------------- Path Operations

    @Test
    public void hasRootPathFromConstructor() {
        assertThat(new FileSystemDatabaseImpl(mockPackageRoot).rootPath(), is(mockPackageRoot));
    }

    @Test
    public void pathWithFeatureExtensionIsAFeaturePath() {
        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(mockPackageRoot);

        assertThat(fsdb.isFeaturePath(Paths.get("foo", "bar", "far")), is(false));
        assertThat(fsdb.isFeaturePath(Paths.get("foo", "bar", "far.feature")), is(true));
        assertThat(fsdb.isPakagePath(Paths.get("foo", "bar", "far.feature")), is(false));
    }

    @Test
    public void pathWithStepMacroExtensionIsAFeaturePath() {
        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(mockPackageRoot);

        assertThat(fsdb.isFeaturePath(Paths.get("foo", "bar", "far")), is(false));
        assertThat(fsdb.isFeaturePath(Paths.get("foo", "bar", "far.stepmacro")), is(true));
        assertThat(fsdb.isPakagePath(Paths.get("foo", "bar", "far.stepmacro")), is(false));
    }

    @Test
    public void pathWithNoExtensionIsAPakagePath() {
        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(mockPackageRoot);

        assertThat(fsdb.isPakagePath(Paths.get("foo", "bar", "far")), is(true));
        assertThat(fsdb.isPakagePath(Paths.get("foo", "bar", "far.bar")), is(false));
    }

    // --------------------------------------------------- Path to ID conversion

    @Test
    public void convertsAbsoluteFeaturePathToId() throws IOException {
        final Path rootPath = tempRootFolder();
        final Path featurePath1 = rootPath.resolve("Foo").resolve("Bar").resolve("Far.feature");
        final Path featurePath2 = rootPath.resolve("Far.feature");

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);

        assertThat(fsdb.pathToId(featurePath1), is("Foo.Bar.Far"));
        assertThat(fsdb.pathToId(featurePath2), is("Far"));
    }

    @Test
    public void convertsRelativeFeaturePathToId() throws IOException {
        final Path featurePath1 = Paths.get("Foo", "Bar", "Far.feature");
        final Path featurePath2 = Paths.get("Foo.feature");

        final FileSystemDatabase fsdb = newFileSystemDatabaseImpl();

        assertThat(fsdb.pathToId(featurePath1), is("Foo.Bar.Far"));
        assertThat(fsdb.pathToId(featurePath2), is("Foo"));
    }

    @Test
    public void convertsAbsoluteStepMacroPathToId() throws IOException {
        final Path rootPath = tempRootFolder();
        final Path featurePath1 = rootPath.resolve("Foo").resolve("Bar").resolve("Far.stepmacro");
        final Path featurePath2 = rootPath.resolve("Far.stepmacro");

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);

        assertThat(fsdb.pathToId(featurePath1), is("Foo.Bar.Far"));
        assertThat(fsdb.pathToId(featurePath2), is("Far"));
    }

    @Test
    public void convertsAbsolutePakagePathToId() throws IOException {
        final Path rootPath = tempRootFolder();
        final Path pakagePath1 = rootPath.resolve("Foo").resolve("Bar").resolve("Far");
        final Path pakagePath2 = rootPath.resolve("Foo");

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);

        assertThat(fsdb.pathToId(pakagePath1), is("Foo.Bar.Far"));
        assertThat(fsdb.pathToId(pakagePath2), is("Foo"));
    }

    @Test
    public void convertsRelativePakagePathToId() throws IOException {
        final Path pakagePath1 = Paths.get("Foo", "Bar", "Far");
        final Path pakagePath2 = Paths.get("Foo");

        final FileSystemDatabase fsdb = newFileSystemDatabaseImpl();

        assertThat(fsdb.pathToId(pakagePath1), is("Foo.Bar.Far"));
        assertThat(fsdb.pathToId(pakagePath2), is("Foo"));
    }

    @Test(expected =  RuntimeException.class)
    public void convertThrowsWhenPathIsNotRelativeToTheRootPath() throws IOException {
        final Path rootPath = tempRootFolder();
        final Path differentPath = testFolder.newFolder("Foo").toPath();
        new FileSystemDatabaseImpl(rootPath).pathToId(differentPath);
    }

    @Test
    public void convertsRootPathToRootId() throws IOException {
        final Path rootPath = tempRootFolder();

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);

        assertThat(fsdb.pathToId(rootPath), is(fsdb.rootId()));
    }

    // ---------------------------------------------------------- ID Operations

    @Test
    public void hasBlankRootId() throws IOException {
        final FileSystemDatabaseImpl fsdb = newFileSystemDatabaseImpl();

        assertThat(fsdb.rootId(), is(""));
        assertThat(fsdb.isRootId(""), is(true));
    }

    @Test
    public void splitsAnIdIntoComponents() throws IOException {
        final FileSystemDatabaseImpl fsdb = newFileSystemDatabaseImpl();
        final List<String> tokens = fsdb.idComponents("Foo.Bar.Far");
        assertThat(tokens, hasSize(3));
        assertThat(tokens.get(0), is("Foo"));
        assertThat(tokens.get(1), is("Bar"));
        assertThat(tokens.get(2), is("Far"));
    }

    // --------------------------------------------------- ID to Path conversion

    @Test
    public void convertsRootIdToPackageRootPath() throws IOException {
        final Path rootPath = tempRootFolder();

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);

        assertThat(fsdb.idToPakagePath(fsdb.rootId()), is(fsdb.rootPath()));
    }

    @Test
    public void convertsIdToAbsolutePackagePath() throws IOException {
        final Path rootPath = tempRootFolder();

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);
        final Path pakagePath = fsdb.idToPakagePath("Foo.Bar.Far");

        final List<String> pakagePathTokens = tokenize(pakagePath);
        assertThat(pakagePath.toString().startsWith(rootPath.toString()), is(true));
        assertThat(pop(pakagePathTokens), is("Far"));
        assertThat(pop(pakagePathTokens), is("Bar"));
        assertThat(pop(pakagePathTokens), is("Foo"));
        assertThat(pop(pakagePathTokens), is("ROOT_FOLDER"));
    }

    @Test
    public void convertsIdToAbsoluteFeaturePathWithFeatureExtension() throws IOException {
        final Path rootPath = tempRootFolder();

        final FileSystemDatabase fsdb = new FileSystemDatabaseImpl(rootPath);
        final Path featurePath = fsdb.idToFeaturePath("Foo.Bar.Far");

        final List<String> featurePathTokens = tokenize(featurePath);
        assertThat(featurePath.toString().startsWith(rootPath.toString()), is(true));
        assertThat(pop(featurePathTokens), is("Far.feature"));
        assertThat(pop(featurePathTokens), is("Bar"));
        assertThat(pop(featurePathTokens), is("Foo"));
        assertThat(pop(featurePathTokens), is("ROOT_FOLDER"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void covertThrowsWhenFeatureIdIsBlank() throws IOException {
        newFileSystemDatabaseImpl().idToFeaturePath("");
    }

    // ------------------------------------------------------------ Utilities

    private <T> T pop(List<T> list) {
        return list.remove(list.size() - 1);
    }

    private List<String> tokenize(final Path path) {
        return stream(path.iterator()).map(Path::toString).collect(toList());
    }

    private FileSystemDatabaseImpl newFileSystemDatabaseImpl() throws IOException {
        return new FileSystemDatabaseImpl(tempRootFolder());
    }

    private Path tempRootFolder() throws IOException {
        return testFolder.newFolder("ROOT_FOLDER").toPath();
    }
}
