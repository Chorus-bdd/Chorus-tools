package org.chorusbdd.structure.pakage.query;


import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.pakage.Pakage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.Validate.notNull;

public class PakageImpl implements Pakage {

    private final String id;
    private final String humanName;
    private final List<Pakage> subpackages;
    private final List<Feature> features;

    public PakageImpl(final String id, final String humanName, final Stream<Pakage> subpackages, final Stream<Feature> features) {
        this.id = notNull(id);
        this.humanName = notNull(humanName);
        this.subpackages =  notNull(subpackages).collect(Collectors.<Pakage>toList());
        this.features = notNull(features).collect(Collectors.<Feature>toList());
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String humanName() {
        return humanName;
    }

    @Override
    public List<Pakage> subpackages() {
        return Collections.unmodifiableList(subpackages);
    }

    @Override
    public List<Feature> features() {
        return Collections.unmodifiableList(features);
    }
}

