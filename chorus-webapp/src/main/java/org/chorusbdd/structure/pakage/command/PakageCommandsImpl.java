package org.chorusbdd.structure.pakage.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.pakage.PakageCommands;
import org.chorusbdd.structure.pakage.PakageEvents;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
public class PakageCommandsImpl implements PakageCommands {
    private final StructureIO sio;

    public PakageCommandsImpl(final StructureIO structureIO) {
        this.sio = notNull(structureIO);
    }

    @Override
    public void apply(final PakageEvents.Store event) {
        notNull(event);
        final Path path = sio.pakageIdToPath(event.pakageId());
        sio.writePakage(path);
    }

    @Override
    public void apply(final PakageEvents.Move event) {
        notNull(event);
        final Path target = sio.pakageIdToPath(event.targetId());
        final Path destination = sio.pakageIdToPath(event.destinationId());
        checkPakageExists(target);
        checkPakageDoesNotExist(destination);
        sio.writePakage(destination.getParent());
        sio.movePakage(target, destination);
    }

    @Override
    public void apply(final PakageEvents.Delete event) {
        notNull(event);
        final Path path = sio.pakageIdToPath(event.pakageId());
        checkPakageExists(path);
        sio.deletePakage(path);
    }

    private void checkPakageDoesNotExist(final Path path) {
        if (sio.existsAndIsAPakage(path)) {
            throw new ResourceExistsException("Package at path='" + path + "' already exists");
        }
    }

    private void checkPakageExists(final Path path) {
        if (!sio.existsAndIsAPakage(path)) {
            throw new ResourceNotFoundException("Expected Package path='" + path + "' does not exist");
        }
    }
}
