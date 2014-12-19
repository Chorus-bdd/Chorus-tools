package org.chorusbdd.structure;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public interface FileSystemDatabase {

    Path rootPath();
    boolean isPakagePath(Path p);
    boolean isFeaturePath(Path p);

    Path idToPakagePath(String id);
    Path idToFeaturePath(String id);

    String rootId();
    boolean isRootId(String id);
    String pathToId(Path p);    // determine from extension

    List<String> idComponents(String id);
}
