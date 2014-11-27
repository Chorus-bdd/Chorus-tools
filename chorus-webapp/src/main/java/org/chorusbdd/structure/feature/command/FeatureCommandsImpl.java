package org.chorusbdd.structure.feature.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.history.Svc;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureCommands;
import org.chorusbdd.structure.feature.FeatureDao;
import org.chorusbdd.structure.feature.FeatureEvents;
import org.chorusbdd.structure.pakage.PakageDao;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Strings.nullToEmpty;
import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class FeatureCommandsImpl implements FeatureCommands {
    private final FeatureDao featureDao;
    private final PakageDao pakageDao;
    private final Svc svc;

    FeatureCommandsImpl(final PakageDao pakageDao, final FeatureDao featureDao, final Svc svc) {
        this.pakageDao = notNull(pakageDao);
        this.featureDao = notNull(featureDao);
        this.svc = notNull(svc);
    }

    @Override
    public void apply(final FeatureEvents.Modify event) throws OptimisticLockFailedException {
        notNull(event);
        writeParentPakage(event.featureId());
        checkOptimisticLock(event.featureId(), nullToEmpty(event.optimisticMd5()));
        featureDao.writeFeature(event.featureId(), event.text());
        svc.commitAll("default user", "default@default.default", event.toString());
    }

    @Override
    public void apply(final FeatureEvents.Delete event) {
        notNull(event);
        checkFeatureExists(event.featureId());
        featureDao.deleteFeature(event.featureId());
        svc.commitAll("default user", "default@default.default", event.toString());
    }

    @Override
    public void apply(final FeatureEvents.Move event) {
        notNull(event);
        checkFeatureExists(event.targetId());
        checkFeatureDoesNotExist(event.destinationId());
        writeParentPakage(event.destinationId());
        featureDao.moveFeature(event.targetId(), event.destinationId());
        svc.commitAll("default user", "default@default.default", event.toString());
    }

    // -------------------------------------------------------- Private Methods

    private void writeParentPakage(final String featureId) {
        final String parentPakage = featureDao.pakage(featureId);
        pakageDao.writePakage(parentPakage);
    }

    private void checkOptimisticLock(final String featureId, final String optimisticMd5) throws OptimisticLockFailedException {
        if (featureDao.featureExists(featureId)) {
            final Feature existing = featureDao.readFeature(featureId);
            if (!optimisticMd5.equals(existing.md5())) {
                throw new OptimisticLockFailedException("File changed on disk since lock creation " + existing.md5() + " " + optimisticMd5);
            }
        } else {
            if (!optimisticMd5.isEmpty()) {
                throw new OptimisticLockFailedException("File deleted on disk since lock creation " + optimisticMd5);
            }
        }
    }

    private void checkFeatureDoesNotExist(final String featureId) {
        if (featureDao.featureExists(featureId)) {
            throw new ResourceExistsException("Feature '" + featureId + "' already exists");
        }
    }

    private void checkFeatureExists(final String featureId) {
        if (!featureDao.featureExists(featureId)) {
            throw new ResourceNotFoundException("Expected Feature '" + featureId + "' does not exist");
        }
    }
}
