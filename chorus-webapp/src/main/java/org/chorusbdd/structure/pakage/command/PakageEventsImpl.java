package org.chorusbdd.structure.pakage.command;


import org.chorusbdd.structure.pakage.PakageEvents;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PakageEventsImpl implements PakageEvents {

    public PakageEventsImpl() {
        // do nothing
    }

    @Override
    public Store store(final String featureId) {
        return new StorePakageEvent(featureId);
    }

    @Override
    public Move move(final String fromId, final String toId) {
        return new MovePakageEvent(fromId, toId);
    }

    @Override
    public Delete delete(final String featureId) {
        return new DeletePakageEvent(featureId);
    }
}
