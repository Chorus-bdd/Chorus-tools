package org.chorusbdd.structure.pakage.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.pakage.PakageEvents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PakageCommandsImplTest {

    @Mock StructureIO sio;
    @Mock PakageEvents.Store mockStoreEvent;
    @Mock PakageEvents.Move mockMoveEvent;
    @Mock PakageEvents.Delete mockDeleteEvent;


    @Mock Path mockPakagePath;
    @Mock Path mockTargetPath;
    @Mock Path mockDestPath;
    @Mock Path mockPakageParentPath;
    @Mock Path mockTargetParentPath;
    @Mock Path mockDestParentPath;

    private final String PAKAGE_ID  = "foo.bar.far";
    private final String TARGET_ID = "tar.get.id";
    private final String DESTINATION_ID = "de.st.id";

    @Before
    public void mockEventIds() {
        when(mockStoreEvent.pakageId()).thenReturn(PAKAGE_ID);
        when(mockDeleteEvent.pakageId()).thenReturn(PAKAGE_ID);
        when(mockMoveEvent.targetId()).thenReturn(TARGET_ID);
        when(mockMoveEvent.destinationId()).thenReturn(DESTINATION_ID);
    }

    @Before
    public void mockPath() {
        when(mockPakagePath.getParent()).thenReturn(mockPakageParentPath);
        when(mockDestPath.getParent()).thenReturn(mockDestParentPath);
        when(mockTargetPath.getParent()).thenReturn(mockTargetParentPath);
    }

    // ------------------------------------------------------------ Composition

    @Test(expected=NullPointerException.class)
    public void disallowsNullStructureIO() {
        new PakageCommandsImpl(null);
    }

    // ------------------------------------------------------------------ Store

    @Test
    public void storesPackageWhenPackageAlreadyExists() {
        mockPakageExists();

        newPakageCommandsImpl().apply(mockStoreEvent);
    }

    @Test
    public void storesPackageWhenPackageDoesNotExist() {
        mockPakageExists();

        newPakageCommandsImpl().apply(mockStoreEvent);
        verify(sio).writePakage(mockPakagePath);
    }

    // ------------------------------------------------------------------- Move

    @Test(expected=ResourceNotFoundException.class)
    public void moveThrowsWhenTargetDoesNotExist() {
        // prepare
        mockTargetDoesNotExist();
        mockDestinationExists();

        // run
        newPakageCommandsImpl().apply(mockMoveEvent);
    }


    @Test(expected=ResourceExistsException.class)
    public void moveThrowsWhenDestinationExists() {
        // prepare
        mockTargetExists();
        mockDestinationExists();

        // run
        newPakageCommandsImpl().apply(mockMoveEvent);
    }

    @Test
    public void movesDestinationPackageAndPakageWhenTargetExistsAndDestinationDoesNotExist() {
        // prepare
        mockTargetExists();
        mockDestinationDoesNotExist();

        // run
        newPakageCommandsImpl().apply(mockMoveEvent);

        // verify
        InOrder inorder = inOrder(sio);
        inorder.verify(sio).writePakage(mockDestParentPath);
        inorder.verify(sio).movePakage(mockTargetPath, mockDestPath);
    }

    // ----------------------------------------------------------------- Delete

    @Test(expected=ResourceNotFoundException.class)
    public void deleteThrowsWhenPackageDoesNotExist() {
        mockPakageDoesNotExist();

        newPakageCommandsImpl().apply(mockDeleteEvent);
    }

    @Test
    public void deletesPackageWhenPackageExists() {
        mockPakageExists();

        newPakageCommandsImpl().apply(mockDeleteEvent);
        verify(sio).deletePakage(mockPakagePath);
    }

    // ---------------------------------------------------------------- Helpers

    private PakageCommandsImpl newPakageCommandsImpl() {
        return new PakageCommandsImpl(sio);
    }

    private void mockPakageDoesNotExist() {
        when(sio.pakageIdToPath(PAKAGE_ID)).thenReturn(mockPakagePath);
        when(sio.existsAndIsAPakage(mockPakagePath)).thenReturn(false);
    }

    private void mockPakageExists() {
        when(sio.pakageIdToPath(PAKAGE_ID)).thenReturn(mockPakagePath);
        when(sio.existsAndIsAPakage(mockPakagePath)).thenReturn(true);
    }

    private void mockTargetDoesNotExist() {
        when(sio.pakageIdToPath(TARGET_ID)).thenReturn(mockTargetPath);
        when(sio.existsAndIsAPakage(mockTargetPath)).thenReturn(false);
    }

    private void mockTargetExists() {
        when(sio.pakageIdToPath(TARGET_ID)).thenReturn(mockTargetPath);
        when(sio.existsAndIsAPakage(mockTargetPath)).thenReturn(true);
    }

    private void mockDestinationExists() {
        when(sio.pakageIdToPath(DESTINATION_ID)).thenReturn(mockDestPath);
        when(sio.existsAndIsAPakage(mockDestPath)).thenReturn(true);
    }

    private void mockDestinationDoesNotExist() {
        when(sio.pakageIdToPath(DESTINATION_ID)).thenReturn(mockDestPath);
        when(sio.existsAndIsAPakage(mockDestPath)).thenReturn(false);
    }
}
