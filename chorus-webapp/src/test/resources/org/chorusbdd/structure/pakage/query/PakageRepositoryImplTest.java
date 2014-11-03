package org.chorusbdd.structure.pakage.query;

import org.chorusbdd.structure.StructureIO;
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

    @Mock StructureIO sio;
    @Mock Path mockPath;
    @Mock Pakage mockPakage;

    @Test(expected=NullPointerException.class)
    public void disallowsNullStructureIO() {
        new PakageRepositoryImpl(null);
    }

    @Test
    public void findsTheRootPackage() {
        // prepare
        when(sio.rootPath()).thenReturn(mockPath);
        when(sio.readPakage(mockPath)).thenReturn(mockPakage);

        // run
        final Pakage result = new PakageRepositoryImpl(sio).findRoot();

        // verify
        assertThat(result, is(mockPakage));
    }

    @Test
    public void findByIdReturnsNullWhenPackageDoesNotExist() {
        // prepare
        when(sio.pakageIdToPath("p.id")).thenReturn(mockPath);
        when(sio.existsAndIsAPakage(mockPath)).thenReturn(false);
        when(sio.readPakage(mockPath)).thenReturn(mockPakage);

        // run
        final Pakage result = new PakageRepositoryImpl(sio).findById("p.id");

        // verify
        assertThat(result, is(nullValue()));
    }

    @Test
    public void findByIdRetrievesPackageFromStructureIO() {
        // prepare
        when(sio.pakageIdToPath("p.id")).thenReturn(mockPath);
        when(sio.existsAndIsAPakage(mockPath)).thenReturn(true);
        when(sio.readPakage(mockPath)).thenReturn(mockPakage);

        // run
        final Pakage result = new PakageRepositoryImpl(sio).findById("p.id");

        // verify
        assertThat(result, is(mockPakage));
    }
}
