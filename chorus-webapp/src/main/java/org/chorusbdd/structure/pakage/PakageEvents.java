package org.chorusbdd.structure.pakage;

public interface PakageEvents {

    interface Store {
        String pakageId();
    }

    interface Move {
        String targetId();
        String destinationId();
        String logMessage();
    }

    interface Delete {
        String pakageId();
        String logMessage();
    }

    Store store(String pakageId);
    Move move(String fromId, String toId);
    Delete delete(String id);
}
