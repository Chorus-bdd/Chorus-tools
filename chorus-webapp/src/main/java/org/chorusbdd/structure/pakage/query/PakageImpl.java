package org.chorusbdd.structure.pakage.query;


import org.chorusbdd.structure.feature.Feature;
import org.chorusbdd.structure.pakage.Pakage;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.isDirectory;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.structure.StructureIO.ROOT_ID;

public class PakageImpl implements Pakage {

    private final String id;
    private final String humanName;
    private final List<Pakage> subpackages;
    private final List<Feature> features;

    public PakageImpl(final String id, final String humanName, final Stream<Pakage> subpackages, final Stream<Feature> features) {
        this.id = notBlank(id);
        this.humanName = notBlank(humanName);
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
        //if (ROOT_ID.equals(id)) {
        //    return "*root-package*";
        //}
        //return fileName().replaceAll("\\.feature", "")
        //        .replaceAll("\\W+", " ")
        //        .replaceAll("([a-z\\d])([A-Z])", "$1 $2");
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

