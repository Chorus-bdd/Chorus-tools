package org.chorusbdd.structure.feature;


import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;

import java.util.stream.Stream;

public interface FeatureDao {

    // ------------------------------------------------------------- Path Types

    boolean featureExists(String id);

    Feature readFeature(String id);
    void writeFeature(String id, String text) throws OptimisticLockFailedException;
    void moveFeature(String target, String destination);
    void deleteFeature(String id);

    Stream<Feature> readFeaturesInPakage(String pakageId);
    Feature readFeatureAtRevision(String id, String revisionId);

    String pakage(String featureId);

}
