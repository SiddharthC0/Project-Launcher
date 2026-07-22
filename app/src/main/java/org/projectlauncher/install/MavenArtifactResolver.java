package org.projectlauncher.install;

import java.nio.file.Path;

public final class MavenArtifactResolver {

    private static final String MAVEN_URL =
            "https://libraries.minecraft.net/";

    private MavenArtifactResolver() {
    }


    public static Artifact resolve(
            String name,
            Path librariesDirectory
    ) {

        String[] parts = name.split(":");

        if (parts.length < 3) {
            return null;
        }

        String group = parts[0];
        String artifact = parts[1];
        String version = parts[2];

        String groupPath =
                group.replace(".", "/");

        String fileName =
                artifact + "-" + version + ".jar";

        String relativePath =
                groupPath + "/"
                + artifact + "/"
                + version + "/"
                + fileName;

        return new Artifact(
                MAVEN_URL + relativePath,
                librariesDirectory.resolve(relativePath)
        );
    }


    public record Artifact(
            String url,
            Path path
    ) {
    }
}