package org.chorusbdd.structure.feature;

public interface FeatureRepository {

    Feature findById(String id);

    Feature findAtRevision(String featureId, String revisionId);
}
