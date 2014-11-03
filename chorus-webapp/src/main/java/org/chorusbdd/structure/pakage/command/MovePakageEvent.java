package org.chorusbdd.structure.pakage.command;

import com.google.common.base.MoreObjects;
import org.chorusbdd.structure.pakage.PakageEvents;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notBlank;

@Immutable
class MovePakageEvent implements PakageEvents.Move {
    private final String fromId;
    private final String toId;

    MovePakageEvent(final String fromId, final String toId) {
        this.fromId = notBlank(fromId);
        this.toId = notBlank(toId);
    }

    @Override
    public String targetId() {
        return fromId;
    }

    @Override
    public String destinationId() {
        return toId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fromId", fromId)
                .add("toId", toId)
                .toString();
    }
}
