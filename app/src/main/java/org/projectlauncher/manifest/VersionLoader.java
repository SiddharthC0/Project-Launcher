package org.projectlauncher.manifest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.projectlauncher.install.VersionInstaller;

import java.io.File;

public final class VersionLoader {

    private VersionLoader() {
    }

    public static JsonObject load(String versionId, File cacheFolder) throws Exception {

        File localVersion = getLocalVersion(versionId);

        JsonObject versionJson;

        if (localVersion.exists()) {

            System.out.println("Using local version: " + versionId);

            versionJson = VersionInstaller.parseVersionJson(localVersion);

        } else {

            versionJson = loadCachedOrDownload(versionId, cacheFolder);
        }

        return resolveInheritance(versionJson, cacheFolder);
    }

    private static File getLocalVersion(String versionId) {

        return new File(
                System.getenv("APPDATA")
                        + "\\.minecraft\\versions\\"
                        + versionId
                        + "\\"
                        + versionId
                        + ".json"
        );
    }

    private static JsonObject loadCachedOrDownload(String versionId,
                                                   File cacheFolder) throws Exception {

        File cached = new File(cacheFolder, versionId + ".json");

        if (cached.exists()) {

            System.out.println("Using cached version: " + versionId);

            return VersionInstaller.parseVersionJson(cached);
        }

        JsonObject manifest = VersionManifestFetcher.fetchManifest();

        JsonArray versions = manifest.getAsJsonArray("versions");

        String versionUrl = null;

        for (int i = 0; i < versions.size(); i++) {

            JsonObject version = versions.get(i).getAsJsonObject();

            if (versionId.equals(version.get("id").getAsString())) {

                versionUrl = version.get("url").getAsString();

                break;
            }
        }

        if (versionUrl == null) {

            throw new RuntimeException("Unknown version: " + versionId);
        }

        File downloaded = VersionInstaller.downloadVersionJson(
                versionUrl,
                cacheFolder
        );

        return VersionInstaller.parseVersionJson(downloaded);
    }

    private static JsonObject resolveInheritance(JsonObject versionJson,
                                                 File cacheFolder) throws Exception {

        if (!versionJson.has("inheritsFrom")) {
            return versionJson;
        }

        String parentId = versionJson.get("inheritsFrom").getAsString();

        JsonObject parent = load(parentId, cacheFolder);

        merge(parent, versionJson);

        return versionJson;
    }

    private static void merge(JsonObject parent,
                              JsonObject child) {

        if (parent.has("libraries")) {

            JsonArray merged = child.has("libraries")
                    ? child.getAsJsonArray("libraries")
                    : new JsonArray();

            for (int i = 0; i < parent.getAsJsonArray("libraries").size(); i++) {

                merged.add(parent.getAsJsonArray("libraries").get(i));
            }

            child.add("libraries", merged);
        }

        copyIfMissing(parent, child, "downloads");
        copyIfMissing(parent, child, "assetIndex");
        copyIfMissing(parent, child, "mainClass");
    }

    private static void copyIfMissing(JsonObject parent,
                                      JsonObject child,
                                      String key) {

        if (!child.has(key) && parent.has(key)) {

            child.add(key, parent.get(key));
        }
    }
}