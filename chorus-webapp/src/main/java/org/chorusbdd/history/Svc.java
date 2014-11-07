package org.chorusbdd.history;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

public interface Svc {

    Stream<Revision> log();
    Stream<Revision> log(Path file);

    String retrieve(String revision, Path file);

    void commitAll(String authorName, String authorEmail, String comment);

    Set<Path> changesetForRevision(String revisionName);
}
