package org.projectlauncher.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class ClasspathBuilder {

    private ClasspathBuilder() {
    }

    public static List<Path> build(Path librariesDirectory,
                                   Path clientJar) {

        List<Path> classpath = new ArrayList<>();

        try (Stream<Path> files = Files.walk(librariesDirectory)) {

            files.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".jar"))
                    .sorted(Comparator.naturalOrder())
                    .forEach(classpath::add);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to build classpath.",
                    e
            );
        }

        classpath.add(clientJar);

        return classpath;
    }
}