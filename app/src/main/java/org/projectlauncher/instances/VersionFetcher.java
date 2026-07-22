package org.projectlauncher.instances;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class VersionFetcher {

    public static List<String> getVanillaVersions() {
        List<String> versions = new ArrayList<>();

        try {
            URL url = new URL("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray arr = json.getAsJsonArray("versions");

            for (JsonElement e : arr) {
                JsonObject obj = e.getAsJsonObject();
                versions.add(obj.get("id").getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return versions;
    }

    public static List<String> getFabricVersions() {
        List<String> versions = new ArrayList<>();

        try {
            URL url = new URL("https://meta.fabricmc.net/v2/versions/game");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            JsonArray arr = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement e : arr) {
                JsonObject obj = e.getAsJsonObject();
                versions.add(obj.get("version").getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return versions;
    }

    public static void downloadVanilla(String version) {

        try {

            // get version manifest
            URL url = new URL("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray versions = json.getAsJsonArray("versions");

            String versionJsonUrl = null;

            for (JsonElement e : versions) {
                JsonObject obj = e.getAsJsonObject();

                if (obj.get("id").getAsString().equals(version)) {
                    versionJsonUrl = obj.get("url").getAsString();
                    break;
                }
            }

            if (versionJsonUrl == null) {
                System.out.println("Version not found.");
                return;
            }

            // get version details
            BufferedReader reader2 = new BufferedReader(
                    new InputStreamReader(new URL(versionJsonUrl).openStream()));

            JsonObject versionJson = JsonParser.parseReader(reader2).getAsJsonObject();

            String jarUrl = versionJson
                    .getAsJsonObject("downloads")
                    .getAsJsonObject("client")
                    .get("url")
                    .getAsString();

            // download jar
            File folder = new File("versions/" + version);
            folder.mkdirs();

            File out = new File(folder, version + ".jar");

            try (InputStream in = new URL(jarUrl).openStream();
                    FileOutputStream fos = new FileOutputStream(out)) {

                byte[] buffer = new byte[8192];
                int len;

                while ((len = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }

            System.out.println("Downloaded Vanilla " + version);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFabric(String version) {

        try {

            String loader = "0.15.11"; // you can later fetch this dynamically

            String urlStr = "https://meta.fabricmc.net/v2/versions/loader/"
                    + version + "/" + loader + "/profile/json";

            URL url = new URL(urlStr);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            // Parse JSON using Gson
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            // Create folder
            File folder = new File("versions/fabric-" + version);
            folder.mkdirs();

            File out = new File(folder, "profile.json");

            // Write formatted JSON using Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(out)) {
                gson.toJson(json, writer);
            }

            System.out.println("Fabric profile downloaded for " + version);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}