package org.chorusbdd.structure.pakage;

public interface PakageEvents {

    interface Store {
        String pakageId();
    }

    interface Move {
        String targetId();
        String destinationId();
    }

    interface Delete {
        String pakageId();
    }

    Store store(String pakageId);
    Move move(String fromId, String toId);
    Delete delete(String id);
}
