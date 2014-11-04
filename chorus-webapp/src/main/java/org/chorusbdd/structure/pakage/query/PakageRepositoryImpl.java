package org.chorusbdd.structure.pakage.query;

import org.chorusbdd.structure.StructureIO;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.PakageRepository;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class PakageRepositoryImpl implements PakageRepository {
    private final StructureIO sio;

    PakageRepositoryImpl(final StructureIO sio) {
        this.sio = notNull(sio);
    }

    @Override
    public Pakage findRoot() {
        return sio.readPakage(sio.rootPath());
    }

    @Override
    public Pakage findById(final String id) {
        notNull(id);
        final Path path = sio.pakageIdToPath(id);
        if (!sio.existsAndIsAPakage(path)) {
            return null;
        }
        return sio.readPakage(path);
    }

}
