package org.chorusbdd.structure.feature;

public interface FeatureEvents {

    interface Modify {
        String featureId();
        String text();
        String optimisticMd5();
        String logMessage();
    }

    interface Move {
        String targetId();
        String destinationId();
        String logMessage();
    }

    interface Delete {
        String featureId();
        String logMessage();
    }

    Modify modify(String featureId, String text, String optimisticMd5);
    Move move(String fromId, String toId);
    Delete delete(String featureId);
}
