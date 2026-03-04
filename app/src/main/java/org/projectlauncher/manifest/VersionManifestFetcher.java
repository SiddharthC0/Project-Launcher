package org.projectlauncher.manifest;

import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VersionManifestFetcher {
    private static final String MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public static JsonObject fetchManifest() throws Exception {
        try (InputStreamReader reader = new InputStreamReader(new URL(MANIFEST_URL).openStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}