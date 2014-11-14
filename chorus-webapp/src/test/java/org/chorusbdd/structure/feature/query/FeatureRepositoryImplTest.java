package org.chorusbdd.structure.feature.query;

import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureDao;
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
    @Mock FeatureDao dao;
    @Mock Path mockPath;
    @Mock Feature mockFeature;

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureDao() {
        new FeatureRepositoryImpl(null);
    }

    @Test
    public void findByIdReturnsNullWhenPackageDoesNotExist() {
        // prepare
        when(dao.featureExists("f.id")).thenReturn(false);
        when(dao.readFeature("f.id")).thenReturn(mockFeature);

        // run
        final Feature result = newFeatureRepositoryImpl().findById("f.id");

        // verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void findByIdRetrievesPackageFromStructureIO() {
        // prepare
        when(dao.featureExists("f.id")).thenReturn(true);
        when(dao.readFeature("f.id")).thenReturn(mockFeature);

        // run
        final Feature result = newFeatureRepositoryImpl().findById("f.id");

        // verify
        assertThat(result, is(mockFeature));
    }

    private FeatureRepositoryImpl newFeatureRepositoryImpl() {
        return new FeatureRepositoryImpl(dao);
    }
}
