package org.chorusbdd.structure.pakage.command;

import com.google.common.base.MoreObjects;
import org.chorusbdd.structure.feature.FeatureEvents;
import org.chorusbdd.structure.pakage.PakageEvents;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notBlank;

@Immutable
class DeletePakageEvent implements PakageEvents.Delete {
    private final String pakageId;

    DeletePakageEvent(final String pakageId) {
        this.pakageId = notBlank(pakageId);
    }

    @Override
    public String pakageId() {
        return pakageId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pakageId", pakageId)
                .toString();
    }
}
