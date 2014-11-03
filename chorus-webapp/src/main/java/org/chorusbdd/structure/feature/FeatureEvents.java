package org.chorusbdd.structure.feature;

public interface FeatureEvents {

    interface Modify {
        String featureId();
        String text();
        String optimisticMd5();
    }

    interface Move {
        String targetId();
        String destinationId();
    }

    interface Delete {
        String featureId();
    }

    Modify modify(String featureId, String text, String optimisticMd5);
    Move move(String fromId, String toId);
    Delete delete(String featureId);
}
