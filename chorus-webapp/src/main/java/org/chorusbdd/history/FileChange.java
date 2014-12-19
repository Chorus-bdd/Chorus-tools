package org.chorusbdd.history;

import com.google.common.base.MoreObjects;
import org.eclipse.jgit.diff.DiffEntry;

import java.nio.file.Path;

import static org.apache.commons.lang3.Validate.notNull;

public class FileChange {
    private final DiffEntry.ChangeType event;
    private final Path path;

    public FileChange(final DiffEntry.ChangeType event, final Path path) {
        this.event = notNull(event);
        this.path = notNull(path);
    }

    public DiffEntry.ChangeType event() {
        return event;
    }

    public Path path() {
        return path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("event", event)
                .add("path", path)
                .toString();
    }
}
