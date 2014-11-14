package org.chorusbdd.structure.pakage;


import org.chorusbdd.structure.FileSystemDatabase;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureDao;
import org.chorusbdd.structure.pakage.query.PakageImpl;
import org.chorusbdd.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.util.FileUtils.listFiles;

@Immutable
class PakageDaoImpl implements PakageDao {
    private static final Logger LOG = LoggerFactory.getLogger(PakageDaoImpl.class);

    private final FileSystemDatabase paths;
    private final FeatureDao featureDao;

    PakageDaoImpl(final FileSystemDatabase pathService, final FeatureDao featureDao) {
        this.paths = notNull(pathService);
        this.featureDao = notNull(featureDao);
    }

    // ------------------------------------------------------ Public Operations

    @Override
    public Pakage readRootPakage() {
        return readPakage(paths.rootId());
    }

    @Override
    public boolean pakageExists(final String id) {
        return pakageExists(paths.idToPakagePath(id));
    }

    private boolean pakageExists(final Path path) {
        return exists(path) && isDirectory(path);
    }

    @Override
    public Pakage readPakage(final String id) {
        final Path path = paths.idToPakagePath(id);
        return readPakage(id, path);
    }

    @Override
    public void writePakage(final String id) {
        try {
            final Path path = paths.idToPakagePath(id);
            LOG.info("Writing package '{}'", path);
            Files.createDirectories(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void movePakage(final String targetId, final String destinationId)  {
        try {
            final Path target = paths.idToPakagePath(targetId);
            final Path destination = paths.idToPakagePath(destinationId);
            LOG.info("Moving package '{}' to '{}'", target, destination);
            Files.move(target, destination);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePakage(final String id) {
        try {
            final Path path = paths.idToPakagePath(id);
            LOG.info("Deleting package '{}'", path);
            FileUtils.deleteDirectoryTree(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String parent(final String id) {
        return paths.pathToId(paths.idToPakagePath(id).getParent());
    }

    // ----------------------------------------------------- Private Operations

    private Pakage readPakage(final Path path) {
        final String id = paths.pathToId(path);
        return readPakage(id, path);
    }

    private Pakage readPakage(final String id, final Path path) {
        final String humanName = packageHumanName(path, id);
        final Stream<Pakage> subpackages = readSubpackagesRecursively(path);
        final Stream<Feature> features = featureDao.readFeaturesInPakage(id);
        return new PakageImpl(id, humanName, subpackages, features);
    }

    private Stream<Pakage> readSubpackagesRecursively(final Path path)  {
        try {
            return listFiles(path).filter(this::pakageExists).map(this::readPakage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // -------------------------------------------------------------- Utilities

    private String packageHumanName(final Path path, final String id) {
        if (paths.isRootId(id)) {
            return "";
        }
        return camelToLowerSentenceCase(fileName(path));
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
