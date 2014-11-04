package org.chorusbdd.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

public class StreamUtils {

    public static <T> Stream<T> stream(final Iterator<T> iterator) {
        return StreamSupport.stream(spliteratorUnknownSize(iterator, 0), false);
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> parallelStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }
}
