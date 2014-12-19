package org.chorusbdd.structure.feature;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class FeaturesImpl implements Features {
    private final FeatureEvents events;
    private final FeatureRepository repository;
    private final FeatureCommands commands;

    FeaturesImpl(final FeatureEvents events, final FeatureRepository repository, final FeatureCommands commands) {
        this.events = notNull(events);
        this.repository = notNull(repository);
        this.commands = notNull(commands);
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
