package org.chorusbdd.structure.feature.command;

import org.chorusbdd.structure.feature.FeatureEvents;

import javax.annotation.concurrent.Immutable;

@Immutable
public class FeatureEventsImpl implements FeatureEvents {

    public FeatureEventsImpl() {
        // do nothing
    }

    @Override
    public Modify modify(final String featureId, final String text, final String optimisticMd5) {
        return new ModifyFeatureEvent(featureId, text, optimisticMd5);
    }

    @Override
    public Move move(final String fromId, final String toId) {
        return new MoveFeatureEvent(fromId, toId);
    }

    @Override
    public Delete delete(final String featureId) {
        return new DeleteFeatureEvent(featureId);
    }
}
