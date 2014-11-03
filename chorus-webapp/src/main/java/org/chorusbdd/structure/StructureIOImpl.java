package org.chorusbdd.structure;


import org.apache.commons.lang3.StringUtils;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.structure.feature.query.FeatureImpl;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.query.PakageImpl;
import org.chorusbdd.util.CheckableFile;
import org.chorusbdd.util.FileUtils;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.chorusbdd.util.FileUtils.listFiles;
import static org.chorusbdd.util.StreamUtils.stream;

@Immutable
public class StructureIOImpl implements StructureIO {
    //private static final Path PACKAGE_ROOT = Paths.get("C:\\dev\\ebond\\ebond2\\frameworks\\messagebridge\\msgbridge-mkv\\test\\component\\feature\\");
    private static final Path PACKAGE_ROOT = Paths.get("C:\\dev\\tmp\\testroot");

    private static final Predicate<Path> EXISTS = (p) -> exists(p);
    private static final Predicate<Path> NOT_A_DIRECTORY = (p) -> !isDirectory(p);
    public static final String FEATURE_EXTENSION = ".feature";
    private static final Predicate<Path> HAS_FEATURE_EXTENSION = (p) -> String.valueOf(p).endsWith(FEATURE_EXTENSION);
    public static final String STEPMACRO_EXTENSION = ".stepmacro";

    public StructureIOImpl() {
        // do nothing
    }

    @Override
    public Path rootPath() {
        return PACKAGE_ROOT;
    }

    // ---------------------------------------------------------- ID Conversion

    @Override
    public String toId(final Path p) {
        requireNonNull(p);
        if (p.equals(rootPath())) {
            return ROOT_ID;
        }
        final Path relativePath = relativize(p);
        final List<String> tokens = tokenize(relativePath);
        tokens.set(lastIndex(tokens), stripExtension(relativePath.getFileName().toString()));
        return StringUtils.join(tokens, ".");
    }

    @Override
    public Path pakageIdToPath(final String id) {
        requireNonNull(id);
        if (ROOT_ID.equals(id)) {
            return rootPath();
        }
        return rootPath().resolve(convertDotsToSlashes(id));
    }

    @Override
    public Path featureIdToPath(final String id) {
        requireNonNull(id);
        checkArgument(!id.isEmpty(), "feature id must not be empty");
        return rootPath().resolve(convertDotsToSlashes(id) + FEATURE_EXTENSION);
    }

    // ------------------------------------------------------------- Path Types

    @Override
    public boolean existsAndIsAFeature(final Path p) {
        checkNotNull(p);
        return EXISTS.and(NOT_A_DIRECTORY).and(HAS_FEATURE_EXTENSION).test(p);
    }

    @Override
    public boolean existsAndIsAFeature(final String featureId) {
        return existsAndIsAFeature(featureIdToPath(featureId));
    }

    @Override
    public boolean existsAndIsAPakage(final Path p) {
        checkNotNull(p);
        return exists(p) && isDirectory(p);
    }

    // ------------------------------------------------------------- Operations

    @Override
    public Feature readFeature(final Path path) {
        final String id = toId(path);
        final String pakageId = toId(path.getParent());
        final String humanName = featureHumanName(path);
        final CheckableFile checkableFile = new CheckableFile(path, Charset.forName("UTF-8"));
        return new FeatureImpl(id, pakageId, humanName, checkableFile::contents, checkableFile::md5);
    }

    @Override
    public Stream<Feature> readFeaturesInFolder(final Path path) {
        try {
            return listFiles(path).filter(this::existsAndIsAFeature).map(this::readFeature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeFeature(final Path path, final String text) throws OptimisticLockFailedException {
        try {
            Files.write(path, text.getBytes(Charset.forName("UTF-8")));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFeature(final Path path) {
        try {
            Files.delete(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveFeature(final Path target, final Path destination)  {
        try {
            Files.move(target, destination);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pakage readPakage(final Path path) {
        final String id = toId(path);
        final String humanName = packageHumanName(path, id);
        return new PakageImpl(id, humanName, readSubpackagesRecursively(path), readFeaturesInFolder(path));
    }


    @Override
    public void writePakage(final Path path) {
        try {
            Files.createDirectories(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void movePakage(final Path target, final Path destination)  {
        try {
            Files.move(target, destination);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePakage(final Path path) {
        try {
            FileUtils.deleteDirectoryTree(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --------------------------------------------------------- Private Methods

    private String packageHumanName(final Path path, final String id) {
        if (ROOT_ID.equals(id)) {
            return "";
        }
        return camelToLowerSentenceCase(fileName(path));
    }

    private String featureHumanName(final Path path) {
        return camelToLowerSentenceCase(stripExtension((fileName(path))));
    }

    private Stream<Pakage> readSubpackagesRecursively(final Path path)  {
        try {
            return listFiles(path).filter(this::existsAndIsAPakage).map(this::readPakage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertDotsToSlashes(final String id) {
        return StringUtils.join(id.split("\\."), "/");
    }

    private static int lastIndex(final List<?> list) {
        return list.size() - 1;
    }

    private String stripExtension(final String fileName) {
        if (fileName.endsWith(FEATURE_EXTENSION)) {
            return StringUtils.removeEnd(fileName, FEATURE_EXTENSION);
        }
        if (fileName.endsWith(STEPMACRO_EXTENSION)){
            return StringUtils.removeEnd(fileName, STEPMACRO_EXTENSION);
        }
        return fileName;
    }

    private List<String> tokenize(final Path path) {
        return stream(path.iterator()).map(Path::toString).collect(toList());
    }

    private Path relativize(final Path p) {
        return rootPath().relativize(p);
    }

    private String fileName(final Path path) {
        return path.getFileName().toString();
    }

    private String camelToLowerSentenceCase(final String value) {
        return value
                .replaceAll("\\W+", " ")
                .replaceAll("([a-z\\d])([A-Z])", "$1 $2");
    }
}
