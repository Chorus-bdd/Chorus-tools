package org.chorusbdd.structure.pakage.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.history.Svc;
import org.chorusbdd.structure.pakage.PakageCommands;
import org.chorusbdd.structure.pakage.PakageDao;
import org.chorusbdd.structure.pakage.PakageEvents;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class PakageCommandsImpl implements PakageCommands {
    public static final String UNKNOWN_USER_NAME = "Unknown User";
    public static final String UNKNOWN_USER_EMAIL = "";
    private final PakageDao dao;
    private final Svc svc;

    PakageCommandsImpl(final PakageDao dao, final Svc svc) {
        this.dao = notNull(dao);
        this.svc = notNull(svc);
    }

    @Override
    public void apply(final PakageEvents.Store event) {
        notNull(event);
        dao.writePakage(event.pakageId());
    }

    @Override
    public void apply(final PakageEvents.Move event) {
        notNull(event);
        checkPakageExists(event.targetId());
        checkPakageDoesNotExist(event.destinationId());
        writeParentPakage(event);
        dao.movePakage(event.targetId(), event.destinationId());
        svc.commitAll(UNKNOWN_USER_NAME, UNKNOWN_USER_EMAIL, event.logMessage());
    }

    @Override
    public void apply(final PakageEvents.Delete event) {
        notNull(event);
        checkPakageExists(event.pakageId());
        dao.deletePakage(event.pakageId());
        svc.commitAll(UNKNOWN_USER_NAME, UNKNOWN_USER_EMAIL, event.logMessage());
    }

    private void writeParentPakage(final PakageEvents.Move event) {
        dao.writePakage(dao.parent(event.destinationId()));
    }

    private void checkPakageDoesNotExist(final String id) {
        if (dao.pakageExists(id)) {
            throw new ResourceExistsException("Package '" + id + "' already exists");
        }
    }

    private void checkPakageExists(final String id) {
        if (!dao.pakageExists(id)) {
            throw new ResourceNotFoundException("Expected Package '" + id + "' does not exist");
        }
    }
}
