package org.chorusbdd.structure.feature;

import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.StructureIOImpl;
import org.chorusbdd.structure.feature.command.FeatureCommandsImpl;
import org.chorusbdd.structure.feature.command.FeatureEventsImpl;
import org.chorusbdd.structure.feature.query.FeatureRepositoryImpl;

import static org.apache.commons.lang3.Validate.notNull;

public class FeaturesImpl implements Features {
    private final FeatureEvents events;
    private final FeatureRepository repository;
    private final FeatureCommands commands;

    public FeaturesImpl(final FeatureEvents events, final FeatureRepository repository, final FeatureCommands commands) {
        this.events = notNull(events);
        this.repository = notNull(repository);
        this.commands = notNull(commands);
    }

    @Deprecated // wire up properly
    public FeaturesImpl() {
        StructureIO structureIO = new StructureIOImpl();
        this.events = new FeatureEventsImpl();
        this.repository = new FeatureRepositoryImpl(structureIO);
        this.commands = new FeatureCommandsImpl(structureIO);
    }

    @Override
    public FeatureRepository repository() {
        return repository;
    }

    @Override
    public FeatureEvents events() {
        return events;
    }

    @Override
    public FeatureCommands commands() {
        return commands;
    }
}
