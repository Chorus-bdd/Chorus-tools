package org.chorusbdd.structure.feature.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureCommands;
import org.chorusbdd.structure.feature.FeatureEvents;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class FeatureCommandsImpl implements FeatureCommands {
    private final StructureIO sio;

    FeatureCommandsImpl(final StructureIO structureIO) {
        this.sio = notNull(structureIO);
    }

    @Override
    public void apply(final FeatureEvents.Modify event) throws OptimisticLockFailedException {
        notNull(event);
        final Path path = toPath(event.featureId());
        sio.writePakage(path.getParent());
        checkOptimisticLock(path, event.optimisticMd5());
        sio.writeFeature(path, event.text());
    }

    @Override
    public void apply(final FeatureEvents.Delete event) {
        notNull(event);
        final Path path = toPath(event.featureId());
        checkFeatureExists(path);
        sio.deleteFeature(path);
    }

    @Override
    public void apply(final FeatureEvents.Move event) {
        notNull(event);
        final Path target = toPath(event.targetId());
        final Path destination = toPath(event.destinationId());
        checkFeatureExists(target);
        checkFeatureDoesNotExist(destination);
        sio.writePakage(destination.getParent());
        sio.moveFeature(target, destination);
    }

    // -------------------------------------------------------- Private Methods

    private Path toPath(final String featureId) {
        return sio.featureIdToPath(featureId);
    }

    private void checkOptimisticLock(final Path path, final String optimisticMd5) throws OptimisticLockFailedException {
        if (sio.existsAndIsAFeature(path)) {
            final Feature existing = sio.readFeature(path);
            if (!optimisticMd5.equals(existing.md5())) {
                throw new OptimisticLockFailedException("File changed on disk since lock creation " + existing.md5() + " " + optimisticMd5);
            }
        }
    }

    private void checkFeatureDoesNotExist(final Path path) {
        if (sio.existsAndIsAFeature(path)) {
            throw new ResourceExistsException("Feature at path='" + path + "' already exists");
        }
    }

    private void checkFeatureExists(final Path path) {
        if (!sio.existsAndIsAFeature(path)) {
            throw new ResourceNotFoundException("Expected Feature path='" + path + "' does not exist");
        }
    }
}
