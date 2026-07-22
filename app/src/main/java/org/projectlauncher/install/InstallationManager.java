package org.projectlauncher.install;

import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class InstallationManager {

    private InstallationManager() {
    }

    public static void install(
            JsonObject versionJson,
            String versionId,
            Path downloadsDirectory,
            Path assetsDirectory,
            Path nativesDirectory
    ) throws Exception {

        installLibraries(versionJson, downloadsDirectory);

        installClient(versionJson, versionId, downloadsDirectory);

        installAssets(versionJson, assetsDirectory);

        installNatives(downloadsDirectory, nativesDirectory);
    }

    private static void installLibraries(
            JsonObject versionJson,
            Path downloadsDirectory
    ) {

        LibraryInstaller.downloadLibraries(
                versionJson,
                downloadsDirectory.resolve("libraries")
        );
    }

    private static void installClient(
            JsonObject versionJson,
            String versionId,
            Path downloadsDirectory
    ) throws Exception {

        JsonObject client = versionJson
                .getAsJsonObject("downloads")
                .getAsJsonObject("client");

        Path clientJar = downloadsDirectory.resolve(versionId + ".jar");

        if (Files.exists(clientJar))
            return;

        ClientDownloader.downloadClient(
        versionId,
        client.get("url").getAsString(),
        client.get("sha1").getAsString(),
        downloadsDirectory
);
    }

    private static void installAssets(
            JsonObject versionJson,
            Path assetsDirectory
    ) throws Exception {

        JsonObject assetIndex =
                versionJson.getAsJsonObject("assetIndex");

        String id = assetIndex.get("id").getAsString();

        Path index = assetsDirectory
                .resolve("indexes")
                .resolve(id + ".json");

        Files.createDirectories(index.getParent());

        if (!Files.exists(index)) {

            try (InputStream in =
                         new URL(assetIndex.get("url").getAsString()).openStream()) {

                Files.copy(
                        in,
                        index,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        }

        VersionInstaller.downloadAssets(
                index,
                assetsDirectory.resolve("objects")
        );
    }

    private static void installNatives(
            Path downloadsDirectory,
            Path nativesDirectory
    ) throws Exception {

        Path marker = nativesDirectory.resolve(".extracted");

        if (Files.exists(marker))
            return;

        NativeExtractor.extractNatives(
                downloadsDirectory.resolve("libraries"),
                nativesDirectory
        );

        Files.createFile(marker);
    }
}