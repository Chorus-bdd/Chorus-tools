package org.chorusbdd.structure.pakage;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class PakagesImpl implements Pakages {
    private final PakageEvents events;
    private final PakageRepository repository;
    private final PakageCommands commands;

    PakagesImpl(final PakageEvents events, final PakageRepository repository, final PakageCommands commands) {
        this.events = notNull(events);
        this.repository = notNull(repository);
        this.commands = notNull(commands);
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
