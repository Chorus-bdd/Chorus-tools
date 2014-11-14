package org.chorusbdd.structure.pakage;


public interface PakageDao {

    // ------------------------------------------------------------- Path Types

    boolean pakageExists(String id);

    Pakage readRootPakage();
    Pakage readPakage(String id);

    void writePakage(String id);
    void movePakage(String target, String destination);
    void deletePakage(String id);

    String parent(String id);
}
