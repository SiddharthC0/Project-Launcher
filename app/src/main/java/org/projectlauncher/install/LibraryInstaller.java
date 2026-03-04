package org.projectlauncher.install;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Downloads Minecraft libraries from version JSON
 */
public class LibraryInstaller {

    public static void downloadLibraries(JsonObject versionJson, Path downloadsDir) throws Exception {
        if (!Files.exists(downloadsDir)) Files.createDirectories(downloadsDir);

        JsonArray libraries = versionJson.getAsJsonArray("libraries");

        for (int i = 0; i < libraries.size(); i++) {
            JsonObject lib = libraries.get(i).getAsJsonObject();

            if (!lib.has("downloads")) continue;
            JsonObject downloads = lib.getAsJsonObject("downloads");
            if (!downloads.has("artifact")) continue;

            JsonObject artifact = downloads.getAsJsonObject("artifact");
            String url = artifact.get("url").getAsString();
            String path = artifact.get("path").getAsString();

            Path filePath = downloadsDir.resolve(path.replace("/", java.io.File.separator));
            if (!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());

            try (InputStream in = new URL(url).openStream();
                 FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
            }

            System.out.println("Downloaded: " + path);
        }
    }
}