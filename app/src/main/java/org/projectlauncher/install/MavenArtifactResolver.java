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

        System.out.println("MAVEN RESOLVE: " + name);


        if (name == null || name.isBlank()) {
            return null;
        }


        String[] parts =
                name.split(":");


        if (parts.length < 3) {

            System.out.println(
                    "Invalid Maven coordinate: " + name
            );

            return null;
        }



        String group =
                parts[0];


        String artifact =
                parts[1];


        String version =
                parts[2];



        String groupPath =
                group.replace(".", "/");



        /*
         * Minecraft libraries use:
         *
         * group/artifact/version/artifact-version.jar
         *
         * Example:
         *
         * net.sf.jopt-simple
         * ->
         * net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar
         */
        String fileName =
                artifact
                        + "-"
                        + version
                        + ".jar";



        String relativePath =
                groupPath
                        + "/"
                        + artifact
                        + "/"
                        + version
                        + "/"
                        + fileName;



        Path destination =
                librariesDirectory.resolve(relativePath);



        String url =
                MAVEN_URL
                        + relativePath;



        System.out.println(
                "Resolved URL: " + url
        );


        System.out.println(
                "Resolved Path: " + destination
        );



        return new Artifact(
                url,
                destination
        );
    }



    public record Artifact(
            String url,
            Path path
    ) {
    }
}