package org.chorusbdd.structure.pakage.query;

import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.PakageDao;
import org.chorusbdd.structure.pakage.PakageRepository;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class PakageRepositoryImpl implements PakageRepository {
    private final PakageDao dao;

    PakageRepositoryImpl(final PakageDao dao) {
        this.dao = notNull(dao);
    }

    @Override
    public Pakage findRoot() {
        return dao.readRootPakage();
    }

    @Override
    public Pakage findById(final String id) {
        notNull(id);
        if (!dao.pakageExists(id)) {
            return null;
        }
        return dao.readPakage(id);
    }

}
