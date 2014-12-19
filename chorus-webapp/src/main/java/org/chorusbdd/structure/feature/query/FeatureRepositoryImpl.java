package org.chorusbdd.structure.feature.query;

import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.feature.FeatureDao;
import org.chorusbdd.structure.feature.FeatureRepository;

import javax.annotation.concurrent.Immutable;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@Immutable
class FeatureRepositoryImpl implements FeatureRepository {
    private final FeatureDao dao;

    FeatureRepositoryImpl(final FeatureDao dao) {
        this.dao = notNull(dao);
    }

    @Override
    public Feature findById(final String id) {
        notBlank(id);
        if (!dao.featureExists(id)) {
            return null;
        }
        return dao.readFeature(id);
    }

    @Override
    public Feature findAtRevision(final String id, final String revisionId) {
        notBlank(id);
        return dao.readFeatureAtRevision(id, revisionId);
    }
}
