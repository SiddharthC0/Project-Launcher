package org.projectlauncher.install;

import com.google.gson.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Map;

public class VersionInstaller {

    // Download the version JSON file
    public static File downloadVersionJson(String versionUrl, File cacheFolder) throws IOException {
        Files.createDirectories(cacheFolder.toPath());
        File versionJson = new File(cacheFolder, "version.json");

        try (InputStream in = new URL(versionUrl).openStream()) {
            Files.copy(in, versionJson.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return versionJson;
    }

    // Parse JSON into JsonObject
    public static JsonObject parseVersionJson(File versionJson) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(versionJson)) {
            return gson.fromJson(reader, JsonObject.class);
        }
    }

    // Download asset objects (textures, sounds, panorama)
    public static void downloadAssets(Path indexJson, Path objectsFolder) throws IOException {
        Gson gson = new Gson();
        JsonObject index = gson.fromJson(Files.newBufferedReader(indexJson), JsonObject.class);
        JsonObject objects = index.getAsJsonObject("objects");

        for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
            String assetPath = entry.getKey();
            String hash = entry.getValue().getAsJsonObject().get("hash").getAsString();

            String folder = hash.substring(0, 2);
            Path target = objectsFolder.resolve(folder).resolve(hash);
            if (Files.exists(target)) continue;

            Files.createDirectories(target.getParent());
            String url = "https://resources.download.minecraft.net/" + folder + "/" + hash;

            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, target);
            }
        }
    }
}