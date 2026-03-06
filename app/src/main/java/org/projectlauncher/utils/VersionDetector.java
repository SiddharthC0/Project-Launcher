package org.projectlauncher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VersionDetector {

    public static List<String> getInstalledVersions() {

        List<String> versions = new ArrayList<>();

        String appdata = System.getenv("APPDATA");
        File versionsFolder = new File(appdata + "\\.minecraft\\versions");

        if (!versionsFolder.exists()) {
            System.out.println("Minecraft versions folder not found.");
            return versions;
        }

        File[] folders = versionsFolder.listFiles();

        if (folders != null) {
            for (File f : folders) {
                if (f.isDirectory()) {
                    versions.add(f.getName());
                }
            }
        }

        return versions;
    }
}