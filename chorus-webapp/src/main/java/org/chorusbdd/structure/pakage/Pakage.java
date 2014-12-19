package org.chorusbdd.structure.pakage;

import org.chorusbdd.structure.feature.Feature;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface Pakage {
    String id();
    String humanName();
    List<Pakage> subpackages();
    List<Feature> features();
}
