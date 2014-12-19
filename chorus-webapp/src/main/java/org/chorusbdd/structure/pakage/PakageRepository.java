package org.chorusbdd.structure.pakage;

public interface PakageRepository {

    Pakage findRoot();

    Pakage findById(String id);
}
