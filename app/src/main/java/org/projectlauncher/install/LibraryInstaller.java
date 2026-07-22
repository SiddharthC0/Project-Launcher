package org.projectlauncher.install;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public final class LibraryInstaller {

    private LibraryInstaller() {
    }


    public static void downloadLibraries(
            JsonObject versionJson,
            Path librariesDir
    ) {

        System.out.println("Library Installer Started");

        try {
            Files.createDirectories(librariesDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (!versionJson.has("libraries")) {
            System.out.println("No libraries found.");
            return;
        }


        JsonArray libraries =
                versionJson.getAsJsonArray("libraries");


        String os = detectOS();

        System.out.println("Detected OS: " + os);
        System.out.println("Libraries found: " + libraries.size());


        for (int i = 0; i < libraries.size(); i++) {

            JsonObject library =
                    libraries.get(i).getAsJsonObject();


            if (!allowed(library, os)) {

                if (library.has("name")) {
                    System.out.println(
                            "Skipping blocked library: "
                                    + library.get("name").getAsString()
                    );
                }

                continue;
            }


            if (library.has("downloads")) {

                JsonObject downloads =
                        library.getAsJsonObject("downloads");


                if (downloads.has("artifact")) {

                    install(
                            downloads.getAsJsonObject("artifact"),
                            librariesDir
                    );
                }


                if (downloads.has("classifiers")) {

                    JsonObject classifiers =
                            downloads.getAsJsonObject("classifiers");


                    String nativeKey =
                            "natives-" + os;


                    if (classifiers.has(nativeKey)) {

                        System.out.println(
                                "Native library detected: "
                                        + nativeKey
                        );


                        install(
                                classifiers.getAsJsonObject(nativeKey),
                                librariesDir
                        );
                    }
                }

            } else if (library.has("name")) {

                System.out.println(
                        "Resolving Maven library: "
                                + library.get("name").getAsString()
                );


                installMaven(
                        library.get("name").getAsString(),
                        librariesDir
                );
            }
        }


        System.out.println(
                "Library Installation Finished"
        );
    }



    private static void install(
            JsonObject artifact,
            Path librariesDir
    ) {

        try {

            if (!artifact.has("path")) {
                System.out.println(
                        "Skipping artifact without path"
                );
                return;
            }


            String pathString =
                    artifact.get("path")
                            .getAsString();


            String url =
                    artifact.has("url")
                            ? artifact.get("url")
                            .getAsString()
                            : "https://libraries.minecraft.net/"
                            + pathString;


            Path path =
                    librariesDir.resolve(pathString);


            download(
                    url,
                    path
            );


        } catch (Exception e) {

            System.err.println(
                    "Library failed: "
                            + e.getMessage()
            );
        }
    }



    private static void installMaven(
            String name,
            Path librariesDir
    ) {

        MavenArtifactResolver.Artifact artifact =
                MavenArtifactResolver.resolve(
                        name,
                        librariesDir
                );


        if (artifact == null) {

            System.err.println(
                    "Could not resolve: "
                            + name
            );

            return;
        }


        download(
                artifact.url(),
                artifact.path()
        );
    }



    private static void download(
            String url,
            Path destination
    ) {

        try {

            if (Files.exists(destination)) {

                System.out.println(
                        "Already exists: "
                                + destination
                );

                return;
            }


            Path parent =
                    destination.getParent();


            if (parent != null) {
                Files.createDirectories(parent);
            }


            System.out.println(
                    "Downloading: "
                            + destination
            );


            URLConnection connection =
                    new URL(url).openConnection();


            connection.setConnectTimeout(10000);
            connection.setReadTimeout(30000);


            try (InputStream in =
                         connection.getInputStream()) {


                Files.copy(
                        in,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }


            System.out.println(
                    "Downloaded: "
                            + destination
            );


        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed downloading "
                            + url,
                    e
            );
        }
    }



    private static boolean allowed(
            JsonObject library,
            String os
    ) {

        if (!library.has("rules")) {
            return true;
        }


        JsonArray rules =
                library.getAsJsonArray("rules");


        boolean allowed = false;


        for (int i = 0; i < rules.size(); i++) {

            JsonObject rule =
                    rules.get(i)
                            .getAsJsonObject();


            String action =
                    rule.has("action")
                            ? rule.get("action")
                            .getAsString()
                            : "allow";


            boolean matchesOS = true;


            if (rule.has("os")) {

                JsonObject osRule =
                        rule.getAsJsonObject("os");


                if (osRule.has("name")) {

                    matchesOS =
                            osRule.get("name")
                                    .getAsString()
                                    .equals(os);
                }
            }


            if (matchesOS) {

                allowed =
                        action.equals("allow");
            }
        }


        return allowed;
    }



    private static String detectOS() {

        String os =
                System.getProperty("os.name")
                        .toLowerCase(Locale.ROOT);


        if (os.contains("win")) {
            return "windows";
        }


        if (os.contains("mac")) {
            return "osx";
        }


        return "linux";
    }
}