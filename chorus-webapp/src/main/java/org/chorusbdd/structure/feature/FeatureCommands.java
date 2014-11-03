package org.chorusbdd.structure.feature;

import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;

public interface FeatureCommands {

    void apply(FeatureEvents.Modify event) throws OptimisticLockFailedException;
    void apply(FeatureEvents.Move event);
    void apply(FeatureEvents.Delete event);
}
