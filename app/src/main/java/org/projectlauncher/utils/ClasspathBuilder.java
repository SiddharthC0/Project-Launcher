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


    public static List<Path> build(
            Path librariesDirectory,
            Path clientJar
    ) {

        List<Path> classpath = new ArrayList<>();


        if (!Files.exists(clientJar)) {
            throw new RuntimeException(
                    "Client jar does not exist: "
                            + clientJar.toAbsolutePath()
            );
        }


        // Client jar first
        classpath.add(clientJar);


        if (!Files.exists(librariesDirectory)) {
            throw new RuntimeException(
                    "Libraries directory does not exist: "
                            + librariesDirectory.toAbsolutePath()
            );
        }


        try (Stream<Path> files =
                     Files.walk(librariesDirectory)) {


            files
                    .filter(Files::isRegularFile)
                    .filter(path ->
                            path.toString()
                                    .endsWith(".jar"))
                    .filter(path ->
                            !path.toString()
                                    .contains("-natives"))
                    .sorted(Comparator.naturalOrder())
                    .forEach(path -> {

                        if (Files.exists(path)) {
                            classpath.add(path);
                        }

                    });


        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to build classpath.",
                    e
            );
        }


        return classpath;
    }
}