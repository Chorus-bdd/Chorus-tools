package org.chorusbdd.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static String readFile(final Path path, final Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(path);
            return new String(encoded, encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Avoids locking files and requiring a close on the stream */
    public static Stream<Path> listFiles(final Path path) throws IOException {
        try (final Stream<Path> stream = Files.list(path)) {
            final List<Path> collect = stream.collect(Collectors.toList());
            return collect.stream();
        }
    }

    public static void deleteDirectoryTree(final Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                throw e;
            }
        });
    }
}
