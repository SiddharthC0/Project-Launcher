package org.projectlauncher.setup;

import java.io.File;

public class FolderSetup {

    public static void initializeFolders() {

        File baseFolder = new File("launcher-data");

        File cacheFolder = new File(baseFolder, "cache");
        File downloadsFolder = new File(baseFolder, "downloads");
        File nativesFolder = new File(baseFolder, "natives");
        File assetsFolder = new File(baseFolder, "assets");

        baseFolder.mkdirs();
        cacheFolder.mkdirs();
        downloadsFolder.mkdirs();
        nativesFolder.mkdirs();
        assetsFolder.mkdirs();

        System.out.println("Launcher folders initialized.");

    }
}