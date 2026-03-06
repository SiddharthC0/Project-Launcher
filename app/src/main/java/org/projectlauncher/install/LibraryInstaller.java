package org.projectlauncher.install;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibraryInstaller {

    /**
     * Downloads all libraries required by a Minecraft version JSON.
     * Ensures libraries exist in the classpath (including joptsimple).
     */
    public static void downloadLibraries(JsonObject versionJson, Path librariesDir) {
        librariesDir.toFile().mkdirs();

        JsonArray libraries = versionJson.getAsJsonArray("libraries");
        String os = System.getProperty("os.name").toLowerCase();

        for (int i = 0; i < libraries.size(); i++) {
            JsonObject lib = libraries.get(i).getAsJsonObject();

            // --- Check rules (OS-specific) ---
            if (lib.has("rules")) {
                JsonArray rules = lib.getAsJsonArray("rules");
                boolean allowed = false;
                for (int r = 0; r < rules.size(); r++) {
                    JsonObject rule = rules.get(r).getAsJsonObject();
                    String action = rule.get("action").getAsString();
                    boolean osMatch = true;
                    if (rule.has("os")) {
                        JsonObject osRule = rule.getAsJsonObject("os");
                        if (osRule.has("name")) {
                            String osName = osRule.get("name").getAsString().toLowerCase();
                            osMatch = os.contains(osName);
                        }
                    }
                    if (osMatch && action.equals("allow")) allowed = true;
                    if (osMatch && action.equals("disallow")) allowed = false;
                }
                if (!allowed) continue; // skip library not allowed on this OS
            }

            // --- Get artifact URL/path ---
            JsonObject downloads = lib.has("downloads") ? lib.getAsJsonObject("downloads") : null;
            JsonObject artifact = (downloads != null && downloads.has("artifact")) ? downloads.getAsJsonObject("artifact") : null;

            String url;
            String path;

            if (artifact == null || !artifact.has("url") || !artifact.has("path")) {
                // Compute Maven-style URL/path for missing artifact info
                String name = lib.get("name").getAsString(); // e.g., "org.lwjgl:lwjgl:3.3.3:natives-windows"
                String[] parts = name.split(":");
                if (parts.length < 3) continue;

                String groupId = parts[0];
                String artifactId = parts[1];
                String version = parts[2];
                String classifier = parts.length == 4 ? parts[3] : null;

                path = groupId.replace('.', '/') + "/" + artifactId + "/" + version + "/" +
                       artifactId + "-" + version + (classifier != null ? "-" + classifier : "") + ".jar";

                url = "https://libraries.minecraft.net/" + path;
                System.out.println("Computed missing URL/path for library: " + name);
            } else {
                url = artifact.get("url").getAsString();
                path = artifact.get("path").getAsString();
            }

            Path dest = librariesDir.resolve(path);
            try {
                if (!Files.exists(dest.getParent())) Files.createDirectories(dest.getParent());
                if (!Files.exists(dest)) {
                    try (InputStream in = new URL(url).openStream()) {
                        Files.copy(in, dest);
                        System.out.println("Downloaded library: " + path);
                    }
                } else {
                    System.out.println("Library already exists: " + path);
                }
            } catch (Exception e) {
                System.err.println("Failed to download library: " + path + " - " + e.getMessage());
            }
        }

        // --- Extra safety: force joptsimple if missing ---
        Path joptsimpleJar = librariesDir.resolve("joptsimple/joptsimple/5.0.1/joptsimple-5.0.1.jar");
        if (!Files.exists(joptsimpleJar)) {
            try {
                Files.createDirectories(joptsimpleJar.getParent());
                try (InputStream in = new URL("https://libraries.minecraft.net/joptsimple/joptsimple/5.0.1/joptsimple-5.0.1.jar").openStream()) {
                    Files.copy(in, joptsimpleJar);
                    System.out.println("Forced download: joptsimple-5.0.1.jar");
                }
            } catch (Exception e) {
                System.err.println("Failed to download joptsimple manually: " + e.getMessage());
            }
        }
    }
}