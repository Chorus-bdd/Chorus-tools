package org.chorusbdd.structure.feature.command;

import com.google.common.base.MoreObjects;
import org.chorusbdd.structure.feature.FeatureEvents;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.Validate.notBlank;

@Immutable
class DeleteFeatureEvent implements FeatureEvents.Delete {
    private final String featureId;

    DeleteFeatureEvent(final String featureId) {
        this.featureId = notBlank(featureId);
    }

    @Override
    public String featureId() {
        return featureId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("featureId", featureId)
                .toString();
    }
}
