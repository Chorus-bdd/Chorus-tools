package org.chorusbdd.structure.pakage.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.structure.pakage.PakageDao;
import org.chorusbdd.structure.pakage.PakageEvents;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PakageCommandsImplTest {

    @Mock PakageDao dao;

    // ------------------------------------------------------------ Composition

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageDao() {
        new PakageCommandsImpl(null);
    }

    // ------------------------------------------------------------------ Store

    @Test
    public void storesPackageWhenPackageAlreadyExists() {
        when(dao.pakageExists("foo.bar.far")).thenReturn(true);

        newPakageCommandsImpl().apply(mockStoreEvent("foo.bar.far"));
    }

    @Test
    public void storesPackageWhenPackageDoesNotExist() {
        when(dao.pakageExists("foo.bar.far")).thenReturn(true);

        newPakageCommandsImpl().apply(mockStoreEvent("foo.bar.far"));
        verify(dao).writePakage("foo.bar.far");
    }

    // ------------------------------------------------------------------- Move

    @Test(expected=ResourceNotFoundException.class)
    public void moveThrowsWhenTargetDoesNotExist() {
        // prepare
        when(dao.pakageExists("tar.get.id")).thenReturn(false);
        when(dao.pakageExists("de.st.id")).thenReturn(true);

        // run
        newPakageCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));
    }


    @Test(expected=ResourceExistsException.class)
    public void moveThrowsWhenDestinationExists() {
        // prepare
        when(dao.pakageExists("tar.get.id")).thenReturn(true);
        when(dao.pakageExists("de.st.id")).thenReturn(true);

        // run
        newPakageCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));
    }

    @Test
    public void movesDestinationPackageAndPakageWhenTargetExistsAndDestinationDoesNotExist() {
        // prepare
        when(dao.pakageExists("tar.get.id")).thenReturn(true);
        when(dao.pakageExists("de.st.id")).thenReturn(false);

        // run
        newPakageCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));

        // verify
        InOrder inorder = inOrder(dao);
        inorder.verify(dao).writePakage("de.st.id");
        inorder.verify(dao).movePakage("tar.get.id", "de.st.id");
    }

    // ----------------------------------------------------------------- Delete

    @Test(expected=ResourceNotFoundException.class)
    public void deleteThrowsWhenPackageDoesNotExist() {
        when(dao.pakageExists("foo.bar.far")).thenReturn(false);

        newPakageCommandsImpl().apply(mockDeleteEvent("foo.bar.far"));
    }

    @Test
    public void deletesPackageWhenPackageExists() {
        when(dao.pakageExists("foo.bar.far")).thenReturn(true);

        newPakageCommandsImpl().apply(mockDeleteEvent("foo.bar.far"));
        verify(dao).deletePakage("foo.bar.far");
    }

    // ---------------------------------------------------------------- Helpers

    private PakageCommandsImpl newPakageCommandsImpl() {
        return new PakageCommandsImpl(dao);
    }

    private PakageEvents.Move mockMoveEvent(final String targetId, final String destinationId) {
        final PakageEvents.Move mock = mock(PakageEvents.Move.class);
        when(mock.targetId()).thenReturn(targetId);
        when(mock.destinationId()).thenReturn(destinationId);
        return mock;
    }

    private PakageEvents.Delete mockDeleteEvent(final String id) {
        final PakageEvents.Delete mock = mock(PakageEvents.Delete.class);
        when(mock.pakageId()).thenReturn(id);
        return mock;
    }

    private PakageEvents.Store mockStoreEvent(final String id) {
        final PakageEvents.Store mock = mock(PakageEvents.Store.class);
        when(mock.pakageId()).thenReturn(id);
        return mock;
    }

}
