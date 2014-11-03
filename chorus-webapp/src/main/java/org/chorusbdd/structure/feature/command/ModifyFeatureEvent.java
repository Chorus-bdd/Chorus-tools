package org.chorusbdd.structure.feature.command;

import com.google.common.base.MoreObjects;
import org.chorusbdd.structure.feature.FeatureEvents;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class ModifyFeatureEvent implements FeatureEvents.Modify {
    private final String featureId;
    private final String text;
    private final String optimisticMd5;

    ModifyFeatureEvent(final String featureId, final String text, final String optimisticMd5) {
        this.featureId = notBlank(featureId);
        this.text = notNull(text);
        this.optimisticMd5 = notNull(optimisticMd5);
    }

    @Override
    public String featureId() {
        return featureId;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String optimisticMd5() {
        return optimisticMd5;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("featureId", featureId)
                .add("text", text)
                .add("optimisticMd5", optimisticMd5)
                .toString();
    }
}
