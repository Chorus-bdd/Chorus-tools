package org.chorusbdd.experimental;

@FunctionalInterface
public interface Request {
    Response execute();
}
