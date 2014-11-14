package org.chorusbdd.structure.pakage.query;

import org.chorusbdd.structure.FileSystemDatabase;
import org.chorusbdd.structure.pakage.PakageDao;
import org.chorusbdd.structure.pakage.Pakage;
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
public class PakageRepositoryImplTest {

    @Mock PakageDao dao;
    @Mock Path mockPath;
    @Mock Pakage mockPakage;

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageDao() {
        new PakageRepositoryImpl(null);
    }

    @Test
    public void findsTheRootPackage() {
        // prepare
        when(dao.readRootPakage()).thenReturn(mockPakage);

        // run
        final Pakage result = newPakageRepositoryImpl().findRoot();

        // verify
        assertThat(result, is(mockPakage));
    }

    @Test
    public void findByIdReturnsNullWhenPackageDoesNotExist() {
        // prepare
        when(dao.pakageExists("p.id")).thenReturn(false);
        when(dao.readPakage("p.id")).thenReturn(mockPakage);

        // run
        final Pakage result = newPakageRepositoryImpl().findById("p.id");

        // verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void findByIdRetrievesPackageFromStructureIO() {
        // prepare
        when(dao.pakageExists("p.id")).thenReturn(true);
        when(dao.readPakage("p.id")).thenReturn(mockPakage);

        // run
        final Pakage result = newPakageRepositoryImpl().findById("p.id");

        // verify
        assertThat(result, is(mockPakage));
    }

    private PakageRepositoryImpl newPakageRepositoryImpl() {
        return new PakageRepositoryImpl(dao);
    }
}
