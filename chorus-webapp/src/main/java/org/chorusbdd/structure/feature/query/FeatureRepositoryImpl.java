package org.chorusbdd.structure.feature.query;

import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.util.FileUtils.listFiles;

public class FeatureRepositoryImpl implements FeatureRepository {
    private final StructureIO sio;

    public FeatureRepositoryImpl(final StructureIO structureIo) {
        this.sio = notNull(structureIo);
    }

    @Override
    public Feature findById(final String id) {
        notBlank(id);
        final Path path = sio.featureIdToPath(id);
        if (!sio.existsAndIsAFeature(path)) {
            return null;
        }
        return sio.readFeature(path);
    }
}
