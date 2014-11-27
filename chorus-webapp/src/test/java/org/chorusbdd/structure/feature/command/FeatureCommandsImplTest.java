package org.chorusbdd.structure.feature.command;

import org.chorusbdd.exception.ResourceExistsException;
import org.chorusbdd.exception.ResourceNotFoundException;
import org.chorusbdd.history.Svc;
import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureDao;
import org.chorusbdd.structure.feature.FeatureEvents;
import org.chorusbdd.structure.pakage.PakageDao;
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
public class FeatureCommandsImplTest {

    @Mock FeatureDao featureDao;
    @Mock PakageDao pakageDao;
    @Mock Svc svc;


    // ------------------------------------------------------------ Composition

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageDao() {
        new FeatureCommandsImpl(null, featureDao, svc);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureDao() {
        new FeatureCommandsImpl(pakageDao, null, svc);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullSvc() {
        new FeatureCommandsImpl(pakageDao, featureDao, null);
    }

    // ----------------------------------------------------------------- Modify

    @Test
    public void modifyCreatesParentPackageOfDestination() throws OptimisticLockFailedException {
        when(featureDao.pakage("foo.bar.far")).thenReturn("foo.bar");

        newFeatureCommandsImpl().apply(mockModifyEvent("foo.bar.far"));

        verify(pakageDao).writePakage("foo.bar");
    }


    @Test(expected = OptimisticLockFailedException.class)
    public void modifyThrowsWhenDestinationExistsAndMd5OptimisticLockDoesNotMatchChecksum() throws OptimisticLockFailedException {
        // prepare
        mockExistingFeatureWithMd5("foo.bar.far", "12345");

        // run
        newFeatureCommandsImpl().apply(mockModifyEventWithTextAndOptimisticMd5("foo.bar.far", "feature text", "98734711"));
    }

    @Test(expected = OptimisticLockFailedException.class)
    public void modifyThrowsWhenDestinationExistsAndMd5OptimisticLockNotSet() throws OptimisticLockFailedException {
        // prepare
        mockExistingFeatureWithMd5("foo.bar.far", "");

        // run
        newFeatureCommandsImpl().apply(mockModifyEventWithTextAndOptimisticMd5("foo.bar.far", "feature text", "98734711"));
    }

    @Test(expected = OptimisticLockFailedException.class)
    public void modifyThrowsWhenDestinationDoesNotExistAndMd5OptimisticLockIsSet() throws OptimisticLockFailedException {
        // run
        newFeatureCommandsImpl().apply(mockModifyEventWithTextAndOptimisticMd5("foo.bar.far", "feature text", "98734711"));
    }

    @Test
    public void modifiesFeatureWhenWhenDestinationExistsAndMd5OptimisticLockSucceeds() throws OptimisticLockFailedException {
        // prepare
        mockExistingFeatureWithMd5("foo.bar.far", "12345");

        // run
        newFeatureCommandsImpl().apply(mockModifyEventWithTextAndOptimisticMd5("foo.bar.far", "feature text", "12345"));

        // verify
        verify(featureDao).writeFeature("foo.bar.far", "feature text");
    }

    @Test
    public void writesFeatureWithoutCheckingLockWhenWhenDestinationDoesNotExists() throws OptimisticLockFailedException {
        when(featureDao.featureExists("foo.bar.far")).thenReturn(false);

        newFeatureCommandsImpl().apply(mockModifyEventWithTextAndOptimisticMd5("foo.bar.far", "feature text", ""));

        verify(featureDao).writeFeature("foo.bar.far", "feature text");
    }

    // ------------------------------------------------------------------- Move

    @Test(expected=ResourceNotFoundException.class)
    public void moveThrowsWhenTargetDoesNotExist() {
        // prepare
        when(featureDao.featureExists("tar.get.id")).thenReturn(false);
        when(featureDao.featureExists("de.st.id")).thenReturn(true);

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));
    }

    @Test(expected=ResourceExistsException.class)
    public void moveThrowsWhenDestinationExists() {
        // prepare
        when(featureDao.featureExists("tar.get.id")).thenReturn(true);
        when(featureDao.featureExists("de.st.id")).thenReturn(true);

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));
    }

    @Test
    public void movesDestinationPackageAndFeatureWhenTargetExistsAndDestinationDoesNotExist() {
        // prepare
        when(featureDao.featureExists("tar.get.id")).thenReturn(true);
        when(featureDao.featureExists("de.st.id")).thenReturn(false);
        when(featureDao.pakage("de.st.id")).thenReturn("de.st");

        // run
        newFeatureCommandsImpl().apply(mockMoveEvent("tar.get.id", "de.st.id"));

        // verify
        InOrder inorder = inOrder(pakageDao, featureDao);
        inorder.verify(pakageDao).writePakage("de.st");
        inorder.verify(featureDao).moveFeature("tar.get.id", "de.st.id");
    }

    // ----------------------------------------------------------------- Delete

    @Test(expected=ResourceNotFoundException.class)
    public void deleteThrowsWhenFeatureDoesNotExist() {
        when(featureDao.featureExists("foo.bar.far")).thenReturn(false);

        newFeatureCommandsImpl().apply(mockDeleteEvent("foo.bar.far"));
    }

    @Test
    public void deletesFeatureWhenFeatureExists() {
        when(featureDao.featureExists("foo.bar.far")).thenReturn(true);

        newFeatureCommandsImpl().apply(mockDeleteEvent("foo.bar.far"));
        verify(featureDao).deleteFeature("foo.bar.far");
    }

    // ---------------------------------------------------------------- Helpers

    private FeatureCommandsImpl newFeatureCommandsImpl() {
       return new FeatureCommandsImpl(pakageDao, featureDao, svc);
    }

    private FeatureEvents.Move mockMoveEvent(final String targetId, final String destinationId) {
        final FeatureEvents.Move mock = mock(FeatureEvents.Move.class);
        when(mock.targetId()).thenReturn(targetId);
        when(mock.destinationId()).thenReturn(destinationId);
        return mock;
    }

    private FeatureEvents.Delete mockDeleteEvent(final String id) {
        final FeatureEvents.Delete mock = mock(FeatureEvents.Delete.class);
        when(mock.featureId()).thenReturn(id);
        return mock;
    }

    private FeatureEvents.Modify mockModifyEvent(final String id) {
        final FeatureEvents.Modify mock = mock(FeatureEvents.Modify.class);
        when(mock.featureId()).thenReturn(id);
        return mock;
    }

    private FeatureEvents.Modify mockModifyEventWithTextAndOptimisticMd5(final String id, final String text, final String md5) {
        final FeatureEvents.Modify mock = mockModifyEvent(id);
        when(mock.text()).thenReturn(text);
        when(mock.optimisticMd5()).thenReturn(md5);
        return mock;
    }

    private Feature mockExistingFeatureWithMd5(String id, String md5) {
        Feature mockFeature = mock(Feature.class);
        when(featureDao.readFeature(id)).thenReturn(mockFeature);
        when(featureDao.featureExists(id)).thenReturn(true);
        when(mockFeature.md5()).thenReturn(md5);
        return mockFeature;
    }
}
