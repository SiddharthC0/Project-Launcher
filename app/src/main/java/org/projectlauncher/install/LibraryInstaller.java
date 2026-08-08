package org.projectlauncher.install;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.io.IOException;
public final class LibraryInstaller {

    private LibraryInstaller() {
    }


    public static void downloadLibraries(
            JsonObject versionJson,
            Path librariesDir,
            Path nativesDir
    ) {

        System.out.println("Library Installer Started");


        try {
            Files.createDirectories(librariesDir);
            Files.createDirectories(nativesDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (!versionJson.has("libraries")) {

            System.out.println("No libraries found.");
            return;
        }


        JsonArray libraries = versionJson.getAsJsonArray("libraries");
        System.out.println(
                "Total libraries: " + libraries.size()
        );

        String os = detectOS();
        String arch = detectArch();



        System.out.println("Detected OS: " + os);
        System.out.println("Libraries found: " + libraries.size());



        for (JsonElement element : libraries) {


            JsonObject library = element.getAsJsonObject();
            System.out.println(
                    "Processing: " + library.get("name").getAsString()
            );


            if (!allowed(library, os)) {

                if (library.has("name")) {

                    System.out.println(
                            "Skipping blocked library: "
                                    + library.get("name").getAsString()
                    );
                }
                System.out.println("Skipping: " + library.get("name").getAsString());
                continue;
            }



            boolean installed = false;
            boolean hasNative = false;



            if (library.has("downloads")
                    && library.get("downloads").isJsonObject()) {


                JsonObject downloads =
                        library.getAsJsonObject("downloads");

                System.out.println(
                        "DOWNLOADS DATA: " + downloads
                );



                // Normal jar
                if (downloads.has("artifact")
                        && downloads.get("artifact").isJsonObject()) {


                    JsonObject artifact =
                            downloads.getAsJsonObject("artifact");


                    System.out.println(
                            "Installing artifact: "
                                    + artifact.get("url").getAsString()
                    );


                    install(artifact, librariesDir);

                    installed = true;


// Extract native artifact if it is a native jar
                    String artifactUrl = artifact.get("url").getAsString();

                    if (artifactUrl.contains("natives-" + os)
                            || artifactUrl.contains("natives-" + os + "-")) {

                        String relativePath;

                        if (artifact.has("path")) {
                            relativePath = artifact.get("path").getAsString();
                        } else {
                            relativePath = artifactUrl.replace(
                                    "https://libraries.minecraft.net/",
                                    ""
                            );
                        }


                        Path nativeJar =
                                librariesDir.resolve(relativePath);


                        try {
                            NativeExtractor.extract(
                                    nativeJar,
                                    nativesDir
                            );

                        } catch (Exception e) {
                            System.err.println(
                                    "Failed extracting native artifact: "
                                            + nativeJar
                            );
                            e.printStackTrace();
                        }
                    }
                }



                // Native libraries
                if (downloads.has("classifiers")
                        && downloads.get("classifiers").isJsonObject()) {


                    JsonObject classifiers =
                            downloads.getAsJsonObject("classifiers");


                    for (Map.Entry<String, JsonElement> entry :
                            classifiers.entrySet()) {


                        String key = entry.getKey();


                        System.out.println(
                                "Classifier: " + key
                        );


                        if (!key.equals("natives-" + os)
                                && !key.equals("natives-" + os + "-" + arch)) {

                            continue;
                        }
                        hasNative = true;


                        JsonObject nativeArtifact =
                                entry.getValue()
                                        .getAsJsonObject();


                        System.out.println(
                                "Downloading native: " + key
                        );


                        install(
                                nativeArtifact,
                                librariesDir
                        );


                        String relativePath;


                        if (nativeArtifact.has("path")) {

                            relativePath =
                                    nativeArtifact
                                            .get("path")
                                            .getAsString();

                        } else {

                            relativePath =
                                    nativeArtifact
                                            .get("url")
                                            .getAsString()
                                            .replace(
                                                    "https://libraries.minecraft.net/",
                                                    ""
                                            );
                        }


                        Path nativeJar =
                                librariesDir.resolve(relativePath);



                        if (Files.exists(nativeJar)) {

                            try {

                                NativeExtractor.extract(
                                        nativeJar,
                                        nativesDir
                                );


                            } catch (Exception e) {

                                System.err.println(
                                        "Failed extracting native: "
                                                + nativeJar
                                );

                                e.printStackTrace();
                            }
                        }


                        installed = true;
                    }
                }
            }



            // Maven fallback

            if (!installed && library.has("name")) {


                String name =
                        library.get("name").getAsString();



                System.out.println(
                        "Maven fallback: " + name
                );


                installMaven(
                        name,
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


            if (!artifact.has("url")) {

                System.err.println(
                        "Artifact has no URL"
                );

                return;
            }



            String url =
                    artifact.get("url").getAsString();



            String relativePath;



            if (artifact.has("path")) {

                relativePath =
                        artifact.get("path")
                                .getAsString();

            } else {

                relativePath =
                        url.replace(
                                "https://libraries.minecraft.net/",
                                ""
                        );
            }



            Path destination =
                    librariesDir.resolve(relativePath);



            System.out.println(
                    "Saving to: "
                            + destination.toAbsolutePath()
            );



            download(
                    url,
                    destination
            );



        } catch (Exception e) {

            System.err.println(
                    "Library failed:"
            );

            e.printStackTrace();
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
                    "Could not resolve: " + name
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
                Files.delete(destination);
            }



            Files.createDirectories(
                    destination.getParent()
            );



            System.out.println(
                    "Downloading: "
                            + destination
            );



            URLConnection connection =
                    new URL(url)
                            .openConnection();



            connection.setConnectTimeout(
                    10000
            );

            connection.setReadTimeout(
                    30000
            );



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
                    "Failed downloading " + url,
                    e
            );
        }
    }






    private static boolean allowed(
            JsonObject library,
            String os
    ) {

        String name = library.has("name")
                ? library.get("name").getAsString()
                : "";

        String arch = detectArch();

        // Handle native libraries whose architecture is encoded in the NAME.
        if (name.contains(":natives-")) {

            if (name.contains(":natives-" + os + "-x86")) {
                return arch.equals("x86");
            }

            if (name.contains(":natives-" + os + "-arm64")) {
                return arch.equals("arm64");
            }

            // Plain "natives-windows" = 64-bit x86
            if (name.contains(":natives-" + os)
                    && !name.contains("-x86")
                    && !name.contains("-arm64")) {
                return arch.equals("x86_64");
            }
        }

        // Normal Mojang rule processing
        if (!library.has("rules")) {
            return true;
        }

        JsonArray rules = library.getAsJsonArray("rules");
        boolean allowed = true;

        for (JsonElement element : rules) {

            JsonObject rule = element.getAsJsonObject();

            String action = rule.has("action")
                    ? rule.get("action").getAsString()
                    : "allow";

            boolean matchesOS = true;

            if (rule.has("os")) {

                JsonObject osRule = rule.getAsJsonObject("os");

                if (osRule.has("name")) {
                    matchesOS = os.equals(osRule.get("name").getAsString());
                }

                if (matchesOS && osRule.has("arch")) {

                    String requiredArch = osRule.get("arch").getAsString();

                    matchesOS = requiredArch.equals(arch);
                }
            }

            if (matchesOS) {
                allowed = action.equals("allow");
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


    private static String detectArch() {

        String arch = System.getProperty("os.arch")
                .toLowerCase(Locale.ROOT);

        if (arch.contains("aarch64") || arch.contains("arm64")) {
            return "arm64";
        }

        if (arch.equals("x86")
                || arch.equals("i386")
                || arch.equals("i486")
                || arch.equals("i586")
                || arch.equals("i686")) {
            return "x86";
        }

        return "x86_64";
    }
}