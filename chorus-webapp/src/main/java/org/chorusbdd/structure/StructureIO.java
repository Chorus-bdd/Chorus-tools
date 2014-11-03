package org.chorusbdd.structure;


import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.command.OptimisticLockFailedException;
import org.chorusbdd.structure.pakage.Pakage;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StructureIO {

    String ROOT_ID = "";

    Path rootPath();

    // ---------------------------------------------------------- ID Conversion

    String toId(Path p);    // determine from extension
    Path pakageIdToPath(String id);
    Path featureIdToPath(String id);

    // ------------------------------------------------------------- Path Types

    boolean existsAndIsAPakage(Path p);
    boolean existsAndIsAFeature(Path p);
    boolean existsAndIsAFeature(String featureId);

    Feature readFeature(Path p);
    void writeFeature(Path p, String text) throws OptimisticLockFailedException;
    void moveFeature(Path target, Path destination);
    void deleteFeature(Path p);

    Pakage readPakage(Path path);
    void writePakage(Path p);
    void movePakage(Path target, Path destination);
    void deletePakage(Path path);

    Stream<Feature> readFeaturesInFolder(Path path);
}
