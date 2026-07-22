package org.projectlauncher.install;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Map;

public final class VersionInstaller {

    private VersionInstaller() {
    }


    public static File downloadVersionJson(
            String versionUrl,
            File cacheFolder
    ) throws IOException {

        Files.createDirectories(cacheFolder.toPath());

        File versionJson =
                new File(cacheFolder, "version.json");


        Path temp =
                versionJson.toPath()
                        .resolveSibling("version.json.tmp");


        try(InputStream in =
                new URL(versionUrl).openStream()) {

            Files.copy(
                    in,
                    temp,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }


        Files.move(
                temp,
                versionJson.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );


        return versionJson;
    }


    public static JsonObject parseVersionJson(
            File versionJson
    ) throws IOException {

        try(Reader reader =
                new FileReader(versionJson)) {

            return JsonParser
                    .parseReader(reader)
                    .getAsJsonObject();
        }
    }


    public static void downloadAssets(
            Path indexJson,
            Path objectsFolder
    ) throws IOException {


        if(!isValidJson(indexJson)) {

            System.out.println(
                    "Corrupted asset index. Deleting..."
            );

            Files.deleteIfExists(indexJson);

            throw new IOException(
                    "Invalid asset index"
            );
        }


        JsonObject index;

        try(Reader reader =
                Files.newBufferedReader(indexJson)) {

            index =
                    JsonParser
                    .parseReader(reader)
                    .getAsJsonObject();
        }


        JsonObject objects =
                index.getAsJsonObject("objects");


        if(objects == null) {

            throw new IOException(
                    "Asset index missing objects"
            );
        }


        Files.createDirectories(objectsFolder);


        for(Map.Entry<String, JsonElement> entry :
                objects.entrySet()) {


            JsonObject object =
                    entry.getValue()
                            .getAsJsonObject();


            if(!object.has("hash")) {
                continue;
            }


            String hash =
                    object.get("hash")
                            .getAsString();


            String folder =
                    hash.substring(0,2);


            Path target =
                    objectsFolder
                            .resolve(folder)
                            .resolve(hash);


            if(Files.exists(target)) {
                continue;
            }


            Files.createDirectories(
                    target.getParent()
            );


            String url =
                    "https://resources.download.minecraft.net/"
                    + folder
                    + "/"
                    + hash;


            Path temp =
                    target.resolveSibling(
                            target.getFileName()
                            + ".tmp"
                    );


            try(InputStream in =
                    new URL(url).openStream()) {


                Files.copy(
                        in,
                        temp,
                        StandardCopyOption.REPLACE_EXISTING
                );


                Files.move(
                        temp,
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );


            } catch(Exception e) {

                Files.deleteIfExists(temp);

                throw e;
            }
        }
    }


    private static boolean isValidJson(
            Path file
    ) {

        try(Reader reader =
                Files.newBufferedReader(file)) {

            JsonParser.parseReader(reader);
            return true;

        } catch(Exception e) {

            return false;
        }
    }
}