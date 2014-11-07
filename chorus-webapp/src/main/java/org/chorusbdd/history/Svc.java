package org.chorusbdd.history;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface Svc {

    Stream<Modification> log();
    Stream<Modification> log(Path file);

    void commitAll(String authorName, String authorEmail, String comment);
}
