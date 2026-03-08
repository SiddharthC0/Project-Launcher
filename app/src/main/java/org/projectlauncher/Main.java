package org.projectlauncher;
/*
 * Project Launcher
 * Copyright (c) 2026 Siddharth Chauhan
 * All Rights Reserved.
 *
 * This source code is the proprietary property of Siddharth Chauhan.
 * Unauthorized copying, modification, distribution, or use of this
 * file, via any medium, is strictly prohibited without explicit
 * written permission from the author.
 *
 * This file is part of the Project Launcher software.
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.projectlauncher.gui.LauncherInterfaceMain;
import org.projectlauncher.install.*;
import org.projectlauncher.launch.MinecraftLauncher;
import org.projectlauncher.manifest.VersionManifestFetcher;
import org.projectlauncher.setup.FolderSetup;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir")); // debug

        // Create required folders
        FolderSetup.initializeFolders();

        // Launch Swing GUI
        LauncherInterfaceMain launcherInterfaceMain = new LauncherInterfaceMain();
        launcherInterfaceMain.launchInterface();
    }

    public static void launchVersion(String versionId) {
        try {
            File baseFolder = new File("launcher-data");
            File cacheFolder = new File(baseFolder, "cache");
            File downloadsFolder = new File(baseFolder, "downloads");
            File nativesFolder = new File(baseFolder, "natives");
            File assetsFolder = new File(baseFolder, "assets");

            ensureFolders(baseFolder, cacheFolder, downloadsFolder, nativesFolder, assetsFolder);

            System.out.println("Launching version: " + versionId);

            // Load version JSON recursively for modded versions
            JsonObject versionJson = loadVersionJson(versionId, cacheFolder);
            System.out.println(versionJson.keySet());

            // Download libraries
            LibraryInstaller.downloadLibraries(versionJson, downloadsFolder.toPath().resolve("libraries"));

            // Download client.jar
            String clientUrl = versionJson.getAsJsonObject("downloads")
                                          .getAsJsonObject("client")
                                          .get("url")
                                          .getAsString();
            ClientDownloader.downloadClient(clientUrl, downloadsFolder.toPath());

            // Handle asset index
            JsonObject assetIndexObj = versionJson.getAsJsonObject("assetIndex");
            String assetIndexUrl = assetIndexObj.get("url").getAsString();
            String assetIndexId = assetIndexObj.get("id").getAsString();

            Path indexPath = assetsFolder.toPath().resolve("indexes").resolve(assetIndexId + ".json");
            Files.createDirectories(indexPath.getParent());
            try (InputStream in = new URL(assetIndexUrl).openStream()) {
                Files.copy(in, indexPath, StandardCopyOption.REPLACE_EXISTING);
            }

            VersionInstaller.downloadAssets(indexPath, assetsFolder.toPath().resolve("objects"));

            // Extract natives
            NativeExtractor.extractNatives(downloadsFolder.toPath().resolve("libraries"), nativesFolder.toPath());

            // Launch Minecraft with offline username
            MinecraftLauncher.launchMinecraft(
                    downloadsFolder.toPath(),
                    nativesFolder.toPath(),
                    assetsFolder.toPath(),
                    assetIndexId,
                    versionId,
                    "Siddy5303"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ensureFolders(File... folders) {
        for (File f : folders) if (!f.exists()) f.mkdirs();
    }

    private static JsonObject loadVersionJson(String versionId, File cacheFolder) throws Exception {
        File localVersion = new File(System.getenv("APPDATA") + "\\.minecraft\\versions\\" + versionId + "\\" + versionId + ".json");

        JsonObject versionJson;
        if (localVersion.exists()) {
            System.out.println("Using LOCAL version JSON: " + localVersion.getAbsolutePath());
            versionJson = VersionInstaller.parseVersionJson(localVersion);
        } else {
            JsonObject manifest = VersionManifestFetcher.fetchManifest();
            JsonArray versions = manifest.getAsJsonArray("versions");
            String versionUrl = null;

            for (int i = 0; i < versions.size(); i++) {
                JsonObject v = versions.get(i).getAsJsonObject();
                if (v.get("id").getAsString().equals(versionId)) {
                    versionUrl = v.get("url").getAsString();
                    break;
                }
            }
            if (versionUrl == null) throw new RuntimeException("Version not found: " + versionId);

            File versionJsonFile = VersionInstaller.downloadVersionJson(versionUrl, cacheFolder);
            versionJson = VersionInstaller.parseVersionJson(versionJsonFile);
        }

        // Handle inheritsFrom recursively
        if (versionJson.has("inheritsFrom")) {
            String parentId = versionJson.get("inheritsFrom").getAsString();
            System.out.println("Version inherits from: " + parentId);
            JsonObject parentJson = loadVersionJson(parentId, cacheFolder);

            JsonObject downloads = versionJson.has("downloads") ? versionJson.getAsJsonObject("downloads") : null;
            JsonObject parentDownloads = parentJson.getAsJsonObject("downloads");
            if (downloads == null || !downloads.has("client")) versionJson.add("downloads", parentDownloads);

            if (!versionJson.has("assetIndex")) versionJson.add("assetIndex", parentJson.getAsJsonObject("assetIndex"));
        }

        return versionJson;
    }
}
