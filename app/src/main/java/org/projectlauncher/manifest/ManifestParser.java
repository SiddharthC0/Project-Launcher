package org.projectlauncher.manifest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ManifestParser {

    /**
     * Finds the version JSON URL for a given Minecraft version (e.g., "1.21.4")
     */
    public static String getVersionUrl(JsonObject manifest, String version) {
        JsonArray versions = manifest.getAsJsonArray("versions");

        for (int i = 0; i < versions.size(); i++) {
            JsonObject v = versions.get(i).getAsJsonObject();
            String id = v.get("id").getAsString();
            if (id.equals(version)) {
                return v.get("url").getAsString();
            }
        }

        return null; // version not found
    }
}