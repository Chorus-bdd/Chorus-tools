package org.chorusbdd.structure.pakage;

public interface PakageCommands {

    void apply(PakageEvents.Store event);
    void apply(PakageEvents.Move event);
    void apply(PakageEvents.Delete event);
}
