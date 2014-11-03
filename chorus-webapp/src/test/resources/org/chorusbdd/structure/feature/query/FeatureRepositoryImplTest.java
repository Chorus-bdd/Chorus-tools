package org.chorusbdd.structure.feature.query;

import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.feature.Feature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureRepositoryImplTest {
    @Mock StructureIO sio;
    @Mock Path mockPath;
    @Mock Feature mockFeature;

    @Test(expected=NullPointerException.class)
    public void disallowsNullStructureIO() {
        new FeatureRepositoryImpl(null);
    }

    @Test
    public void findByIdReturnsNullWhenPackageDoesNotExist() {
        // prepare
        when(sio.featureIdToPath("f.id")).thenReturn(mockPath);
        when(sio.existsAndIsAFeature(mockPath)).thenReturn(false);
        when(sio.readFeature(mockPath)).thenReturn(mockFeature);

        // run
        final Feature result = new FeatureRepositoryImpl(sio).findById("f.id");

        // verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void findByIdRetrievesPackageFromStructureIO() {
        // prepare
        when(sio.featureIdToPath("f.id")).thenReturn(mockPath);
        when(sio.existsAndIsAFeature(mockPath)).thenReturn(true);
        when(sio.readFeature(mockPath)).thenReturn(mockFeature);

        // run
        final Feature result = new FeatureRepositoryImpl(sio).findById("f.id");

        // verify
        assertThat(result, is(mockFeature));
    }
}
