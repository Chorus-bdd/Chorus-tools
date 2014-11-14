package org.chorusbdd.structure.feature;


import org.chorusbdd.history.Svc;
import org.chorusbdd.structure.FileSystemDatabase;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.structure.feature.query.FeatureImpl;
import org.chorusbdd.util.CheckableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.util.FileUtils.listFiles;

@Immutable
class FeatureDaoImpl implements FeatureDao {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureDaoImpl.class);

    private static final Predicate<Path> EXISTS = (p) -> exists(p);
    private static final Predicate<Path> NOT_A_DIRECTORY = (p) -> !isDirectory(p);

    private final FileSystemDatabase fsdb;
    private final Svc svc;

    FeatureDaoImpl(final FileSystemDatabase pathService, final Svc svc) {
        this.svc = notNull(svc);
        this.fsdb = notNull(pathService);
    }

    // ------------------------------------------------------------- Operations

    @Override
    public boolean featureExists(final String id) {
        return featureExists(fsdb.idToFeaturePath(id));
    }

    @Override
    public Feature readFeature(final String id) {
        return readFeature(fsdb.idToFeaturePath(id));
    }

    @Override
    public Stream<Feature> readFeaturesInPakage(final String pakageId) {
        try {
            final Path path = fsdb.idToPakagePath(pakageId);
            return listFiles(path).filter(this::featureExists).map(this::readFeature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeFeature(final String id, final String text) throws OptimisticLockFailedException {
        try {
            final Path path = fsdb.idToPakagePath(id);
            LOG.info("Writing feature '{}'", path);
            Files.write(path, text.getBytes(Charset.forName("UTF-8")));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFeature(final String id) {
        try {
            final Path path = fsdb.idToPakagePath(id);
            LOG.info("Deleting feature '{}'", path);
            Files.delete(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveFeature(final String targetId, final String destinationId)  {
        try {
            final Path target = fsdb.idToPakagePath(targetId);
            final Path destination = fsdb.idToPakagePath(destinationId);
            LOG.info("Moving feature '{}' to '{}'", target, destination);
            Files.move(target, destination);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String pakage(final String featureId) {
        return fsdb.pathToId(fsdb.idToFeaturePath(featureId).getParent());
    }

// -------------------------------------------------------- Factory Methods

    private boolean featureExists(final Path path) {
        return EXISTS.and(NOT_A_DIRECTORY).and(fsdb::isFeaturePath).test(path);
    }

    private Feature readFeature(final Path path) {
        final CheckableFile checkableFile = new CheckableFile(path, Charset.forName("UTF-8"));
        return newFeature(path, checkableFile::contents, checkableFile::md5);
    }

    private Feature newFeature(final Path path, final Supplier<String> textSupplier, final Supplier<String> md5Supplier) {
        final String id = fsdb.pathToId(path);
        final String pakageId = fsdb.pathToId(path.getParent());
        return new FeatureImpl(id, pakageId, humanName(id), textSupplier, md5Supplier);
    }

    // -------------------------------------------------------- Factory Methods


    private String humanName(final String id) {
        return camelToLowerSentenceCase(lastItem(fsdb.idComponents(id)));
    }

    private <T> T lastItem(final List<T> list) {
        return list.get(list.size() - 1);
    }

    private String camelToLowerSentenceCase(final String value) {
        return value
                .replaceAll("\\W+", " ")
                .replaceAll("([a-z\\d])([A-Z])", "$1 $2");
    }
}
