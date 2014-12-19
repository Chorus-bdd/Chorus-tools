package org.chorusbdd.structure.feature;

public interface Features {
    FeatureRepository repository();
    FeatureEvents events();
    FeatureCommands commands();
}
