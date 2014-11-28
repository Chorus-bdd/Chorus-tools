package org.chorusbdd.structure;

import org.apache.commons.lang3.StringUtils;
import org.chorusbdd.util.FileUtils;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.*;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.util.FileUtils.isSubpath;
import static org.chorusbdd.util.FileUtils.tokenize;
import static org.chorusbdd.util.StreamUtils.stream;


// all paths are absolute
// all id's are relative to root path
// all relative paths are relative to root path
@Immutable
class FileSystemDatabaseImpl implements FileSystemDatabase {

    private static final String FEATURE_EXTENSION = ".feature";
    private static final String STEPMACRO_EXTENSION = ".stepmacro";
    private static final String ROOT_ID = "";
    public static final String DELIMITER_REGEX = "\\.";
    public static final String DELIMITER = ".";

    private final Path rootPath;

    FileSystemDatabaseImpl(final String packageRoot) {
        this(Paths.get(packageRoot));
    }

    FileSystemDatabaseImpl(final Path packageRoot) {
        notNull(packageRoot);
        isTrue(packageRoot.isAbsolute(), "package root must be an absolute path");
        this.rootPath = packageRoot;
    }

    // -------------------------------------------------------- Path Operations

    @Override
    public Path rootPath() {
        return rootPath;
    }

    @Override
    public boolean isPakagePath(final Path p) {
        notNull(p);
        return !hasExtension(p);
    }

    @Override
    public boolean isFeaturePath(final Path p) {
        notNull(p);
        final String pathStr = String.valueOf(p);
        return pathStr.endsWith(FEATURE_EXTENSION) ||
                pathStr.endsWith(STEPMACRO_EXTENSION) ;
    }

    @Override
    public String pathToId(final Path p) {
        notNull(p);
        if (p.equals(rootPath())) {
            return ROOT_ID;
        }
        final Path relativePath = p.isAbsolute() ? relativizeToRoot(p) : p;
        final List<String> tokens = tokenize(relativePath);
        replaceLastItem(tokens, stripExtension(fileName(relativePath)));
        return StringUtils.join(tokens, DELIMITER);
    }

    // ---------------------------------------------------------- ID Operations

    @Override
    public String rootId() {
        return ROOT_ID;
    }

    @Override
    public boolean isRootId(final String id) {
        notNull(id);
        return ROOT_ID.equals(id);
    }

    @Override
    public Path idToPakagePath(final String id) {
        notNull(id);
        if (ROOT_ID.equals(id)) {
            return rootPath();
        }
        return rootPath().resolve(delimiterToSlash(id));
    }

    @Override
    public Path idToFeaturePath(final String id) {
        notNull(id);
        checkArgument(!id.isEmpty(), "feature id must not be empty");
        return rootPath().resolve(delimiterToSlash(id) + FEATURE_EXTENSION);
    }

    @Override
    public List<String> idComponents(final String id) {
        notNull(id);
        return asList(id.split(DELIMITER_REGEX));
    }

    // --------------------------------------------------------- Private Methods

    private Path relativizeToRoot(final Path p) {
        if (!isSubpath(rootPath(), p)) {
            throw new IllegalArgumentException("path is not subpath of root");
        }
        return rootPath().relativize(p);
    }

    private void replaceLastItem(final List<String> tokens, final String value) {
        tokens.set(lastIndex(tokens), value);
    }

    private String lastItem(final String[] array) {
        return array[array.length - 1];
    }

    private String delimiterToSlash(final String id) {
        return StringUtils.join(id.split(DELIMITER_REGEX), "/");
    }

    private static int lastIndex(final List<?> list) {
        return list.size() - 1;
    }

    private boolean hasExtension(final Path p) {
        return fileName(p).contains(".");
        //final String fileName = fileName(p);
        //return fileName.endsWith(FEATURE_EXTENSION) ||
        //        fileName.endsWith(STEPMACRO_EXTENSION);
    }

    private String stripExtension(final String fileName) {
        return fileName.split("\\.")[0];
        /*if (fileName.endsWith(FEATURE_EXTENSION)) {
            return StringUtils.removeEnd(fileName, FEATURE_EXTENSION);
        }
        if (fileName.endsWith(STEPMACRO_EXTENSION)) {
            return StringUtils.removeEnd(fileName, STEPMACRO_EXTENSION);
        }
        return fileName;*/
    }

    private String fileName(final Path path) {
        return path.getFileName().toString();
    }
}
