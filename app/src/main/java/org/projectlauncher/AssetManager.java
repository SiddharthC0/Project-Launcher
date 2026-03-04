package org.projectlauncher;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Map;

public class AssetManager {

    private static final String BASE_DIR = "app/launcher-data/downloads/assets/";
    private static final String VERSION = "1.21";
    private static final String INDEX_URL = "https://resources.download.minecraft.net/indexes/" + VERSION + ".json";

    public static void ensureAssets() throws Exception {
        Path indexFile = Paths.get(BASE_DIR, "indexes", VERSION + ".json");
        if (!Files.exists(indexFile)) {
            System.out.println("Downloading asset index...");
            Files.createDirectories(indexFile.getParent());
            try (InputStream in = new URL(INDEX_URL).openStream()) {
                Files.copy(in, indexFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        System.out.println("Parsing asset index...");
        JsonObject index;
        try (Reader reader = Files.newBufferedReader(indexFile)) {
            index = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonObject objects = index.getAsJsonObject("objects");
        int total = objects.size();
        int count = 0;

        for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
            count++;
            String assetPath = entry.getKey();
            String hash = entry.getValue().getAsJsonObject().get("hash").getAsString();
            String subfolder = hash.substring(0, 2);

            Path target = Paths.get(BASE_DIR, "objects", subfolder, hash);
            if (Files.exists(target)) continue;  // skip existing

            Files.createDirectories(target.getParent());

            String url = "https://resources.download.minecraft.net/" + subfolder + "/" + hash;
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            // Only log every 50 files
            if (count % 50 == 0) System.out.println("Downloaded " + count + "/" + total);
        }

        System.out.println("All assets ensured!");
    }
}