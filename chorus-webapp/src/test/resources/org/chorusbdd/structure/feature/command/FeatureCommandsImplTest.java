package org.chorusbdd.structure.feature.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureEvents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureCommandsImplTest {

    @Mock StructureIO sio;
    @Mock FeatureEvents.Modify mockModifyEvent;
    @Mock FeatureEvents.Move mockMoveEvent;
    @Mock FeatureEvents.Delete mockDeleteEvent;

    @Mock Path mockFeaturePath;
    @Mock Path mockTargetPath;
    @Mock Path mockDestPath;
    @Mock Path mockFeatureParentPath;
    @Mock Path mockTargetParentPath;
    @Mock Path mockDestParentPath;

    private final String FEATURE_ID  = "foo.bar.far";
    private final String TARGET_ID = "tar.get.id";
    private final String DESTINATION_ID = "de.st.id";

    @Before
    public void mockEventIds() {
        when(mockModifyEvent.featureId()).thenReturn(FEATURE_ID);
        when(mockDeleteEvent.featureId()).thenReturn(FEATURE_ID);
        when(mockMoveEvent.targetId()).thenReturn(TARGET_ID);
        when(mockMoveEvent.destinationId()).thenReturn(DESTINATION_ID);
    }

    @Before
    public void mockPath() {
        when(mockFeaturePath.getParent()).thenReturn(mockFeatureParentPath);
        when(mockDestPath.getParent()).thenReturn(mockDestParentPath);
        when(mockTargetPath.getParent()).thenReturn(mockTargetParentPath);
    }

    // ------------------------------------------------------------ Composition

    //@Test(expected=NullPointerException.class)
    //public void disallowsNullStructureIO() {
    //    new FeatureCommandsImpl(null);
    //}

    // ----------------------------------------------------------------- Modify

    @Test
    public void modifyCreatesParentPackageOfDestination() throws OptimisticLockFailedException {
        when(sio.featureIdToPath(FEATURE_ID)).thenReturn(mockFeaturePath);

        newFeatureCommandsImpl().apply(mockModifyEvent);

        verify(sio).writePakage(mockFeatureParentPath);
    }

    @Test(expected = OptimisticLockFailedException.class)
    public void modifyThrowsWhenDestinationExistsAndMd5OptimisticLockDoesNotMatchChecksum() throws OptimisticLockFailedException {
        // prepare
        mockFeatureExists();
        mockModifyEventHasTextAndMd5("feature text", "98734711");
        mockExistingFeatureWithMd5(mockFeaturePath, "12345");

        // run
        newFeatureCommandsImpl().apply(mockModifyEvent);
    }

    @Test(expected = OptimisticLockFailedException.class)
    public void modifyThrowsWhenDestinationExistsAndMd5OptimisticLockNotSet() throws OptimisticLockFailedException {
        // prepare
        mockFeatureExists();
        mockModifyEventHasTextAndMd5("feature text", "98734711");
        mockExistingFeatureWithMd5(mockFeaturePath, "");

        // run
        newFeatureCommandsImpl().apply(mockModifyEvent);
    }

    @Test
    public void modifiesFeatureWhenWhenDestinationExistsAndMd5OptimisticLockSucceeds() throws OptimisticLockFailedException {
        // prepare
        mockFeatureDoesNotExist();
        mockModifyEventHasTextAndOptimisticMd5("feature text", "12345");
        mockExistingFeatureWithMd5(mockFeaturePath, "12345");

        // run
        newFeatureCommandsImpl().apply(mockModifyEvent);

        // verify
        verify(sio).writeFeature(mockFeaturePath, "feature text");
    }

    @Test
    public void writesFeatureWithoutCheckingLockWhenWhenDestinationDoesNotExists() throws OptimisticLockFailedException {
        mockFeatureDoesNotExist();
        mockModifyEventHasTextAndOptimisticMd5("feature text", "");

        newFeatureCommandsImpl().apply(mockModifyEvent);

        verify(sio).writeFeature(mockFeaturePath, "feature text");
    }

    // ------------------------------------------------------------------- Move

    @Test(expected=ResourceNotFoundException.class)
    public void moveThrowsWhenTargetDoesNotExist() {
        // prepare
        mockTargetDoesNotExist();
        mockDestinationExists();

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent);
    }


    @Test(expected=ResourceExistsException.class)
    public void moveThrowsWhenDestinationExists() {
        // prepare
        mockTargetExists();
        mockDestinationExists();

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent);
    }

    @Test
    public void movesDestinationPackageAndFeatureWhenTargetExistsAndDestinationDoesNotExist() {
        // prepare
        mockTargetExists();
        mockDestinationDoesNotExist();

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent);

        // verify
        InOrder inorder = inOrder(sio);
        inorder.verify(sio).writePakage(mockDestParentPath);
        inorder.verify(sio).moveFeature(mockTargetPath, mockDestPath);
    }

    // ----------------------------------------------------------------- Delete

    @Test(expected=ResourceNotFoundException.class)
    public void deleteThrowsWhenFeatureDoesNotExist() {
        mockFeatureDoesNotExist();

        newFeatureCommandsImpl().apply(mockDeleteEvent);
    }

    @Test
    public void deletesFeatureWhenFeatureExists() {
        mockFeatureExists();

        newFeatureCommandsImpl().apply(mockDeleteEvent);
        verify(sio).deleteFeature(mockFeaturePath);
    }

    // ---------------------------------------------------------------- Helpers

    private FeatureCommandsImpl newFeatureCommandsImpl() {
       return null;// return new FeatureCommandsImpl(sio);
    }

    private void mockModifyEventHasTextAndMd5(final String text, final String md5) {
        when(mockModifyEvent.text()).thenReturn(text);
        when(mockModifyEvent.optimisticMd5()).thenReturn(md5);
    }

    private void mockModifyEventHasTextAndOptimisticMd5(final String text, final String md5) {
        when(mockModifyEvent.text()).thenReturn(text);
        when(mockModifyEvent.optimisticMd5()).thenReturn(md5);
    }

    private void mockFeatureDoesNotExist() {
        when(sio.featureIdToPath(FEATURE_ID)).thenReturn(mockFeaturePath);
        when(sio.existsAndIsAFeature(mockFeaturePath)).thenReturn(false);
    }

    private void mockFeatureExists() {
        when(sio.featureIdToPath(FEATURE_ID)).thenReturn(mockFeaturePath);
        when(sio.existsAndIsAFeature(mockFeaturePath)).thenReturn(true);
    }

    private void mockTargetDoesNotExist() {
        when(sio.featureIdToPath(TARGET_ID)).thenReturn(mockTargetPath);
        when(sio.existsAndIsAFeature(mockTargetPath)).thenReturn(false);
    }

    private void mockTargetExists() {
        when(sio.featureIdToPath(TARGET_ID)).thenReturn(mockTargetPath);
        when(sio.existsAndIsAFeature(mockTargetPath)).thenReturn(true);
    }

    private void mockDestinationExists() {
        when(sio.featureIdToPath(DESTINATION_ID)).thenReturn(mockDestPath);
        when(sio.existsAndIsAFeature(mockDestPath)).thenReturn(true);
    }

    private void mockDestinationDoesNotExist() {
        when(sio.featureIdToPath(DESTINATION_ID)).thenReturn(mockDestPath);
        when(sio.existsAndIsAFeature(mockDestPath)).thenReturn(false);
    }

    private Feature mockExistingFeatureWithMd5(Path path, String md5) {
        Feature mockFeature = mock(Feature.class);
        when(sio.readFeature(path)).thenReturn(mockFeature);
        when(mockFeature.md5()).thenReturn(md5);
        return mockFeature;
    }
}
