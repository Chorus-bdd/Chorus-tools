package org.chorusbdd.structure.pakage;

import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.StructureIOImpl;
import org.chorusbdd.structure.pakage.command.PakageCommandsImpl;
import org.chorusbdd.structure.pakage.command.PakageEventsImpl;
import org.chorusbdd.structure.pakage.query.PakageRepositoryImpl;

import static org.apache.commons.lang3.Validate.notNull;

public class PakagesImpl implements Pakages {
    private final PakageEvents events;
    private final PakageRepository repository;
    private final PakageCommands commands;

    public PakagesImpl(final PakageEvents events, final PakageRepository repository, final PakageCommands commands) {
        this.events = notNull(events);
        this.repository = notNull(repository);
        this.commands = notNull(commands);
    }

    @Deprecated // wire up properly
    public PakagesImpl() {
        StructureIO structureIO = new StructureIOImpl();
        this.events = new PakageEventsImpl();
        this.repository = new PakageRepositoryImpl(structureIO);
        this.commands = new PakageCommandsImpl(structureIO);
    }

    @Override
    public PakageRepository repository() {
        return repository;
    }

    @Override
    public PakageEvents events() {
        return events;
    }

    @Override
    public PakageCommands commands() {
        return commands;
    }
}
