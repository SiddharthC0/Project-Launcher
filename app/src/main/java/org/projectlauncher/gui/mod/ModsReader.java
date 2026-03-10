package org.projectlauncher.gui.mod;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;
import java.awt.Image;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Reads Minecraft mods from the .minecraft/mods folder
 * and extracts human-readable mod name + icon + version + mcVersion (for
 * grouping)
 */
public class ModsReader {

    private static final String DEFAULT_ICON_PATH = "default_icon.png"; // put in project resources

    public static List<Mod> readMods() {
        List<Mod> modList = new ArrayList<>();

        File modsFolder = new File(System.getenv("APPDATA") + "\\.minecraft\\mods");
        if (!modsFolder.exists() || !modsFolder.isDirectory())
            return modList;

        File[] modFiles = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (modFiles == null)
            return modList;

        Gson gson = new Gson();

        for (File modJar : modFiles) {
            try (JarFile jar = new JarFile(modJar)) {

                String modName = modJar.getName().replace(".jar", "");
                String version = "";
                String description = "";
                String mcVersion = "Unknown";
                ImageIcon icon = new ImageIcon(DEFAULT_ICON_PATH);

                // -------- Forge mod support (mcmod.info) --------
                JarEntry forgeEntry = jar.getJarEntry("mcmod.info");
                if (forgeEntry != null) {
                    InputStream is = jar.getInputStream(forgeEntry);
                    JsonArray modArray = gson.fromJson(new InputStreamReader(is), JsonArray.class);
                    if (modArray.size() > 0) {
                        JsonObject modInfo = modArray.get(0).getAsJsonObject();
                        modName = modInfo.has("name") ? modInfo.get("name").getAsString() : modName;
                        version = modInfo.has("version") ? modInfo.get("version").getAsString() : "";
                        description = modInfo.has("description") ? modInfo.get("description").getAsString() : "";
                        mcVersion = modInfo.has("mcversion") ? modInfo.get("mcversion").getAsString() : "Unknown";

                        String iconPath = modInfo.has("logoFile") ? modInfo.get("logoFile").getAsString() : "";
                        if (!iconPath.isEmpty()) {
                            JarEntry iconEntry = jar.getJarEntry(iconPath);
                            if (iconEntry != null) {
                                InputStream iconStream = jar.getInputStream(iconEntry);
                                icon = new ImageIcon(iconStream.readAllBytes());
                                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                                icon = new ImageIcon(img);
                            }
                        }
                    }
                }

                // -------- Fabric mod support (fabric.mod.json) --------
                JarEntry fabricEntry = jar.getJarEntry("fabric.mod.json");
                if (fabricEntry != null) {
                    InputStream is = jar.getInputStream(fabricEntry);
                    JsonObject modInfo = gson.fromJson(new InputStreamReader(is), JsonObject.class);
                    modName = modInfo.has("name") ? modInfo.get("name").getAsString() : modName;
                    version = modInfo.has("version") ? modInfo.get("version").getAsString() : version;
                    description = modInfo.has("description") ? modInfo.get("description").getAsString() : description;

                    // Fabric mcVersion
                    if (modInfo.has("depends") && modInfo.getAsJsonObject("depends").has("minecraft")) {
                        mcVersion = modInfo.getAsJsonObject("depends").get("minecraft").getAsString();
                    }

                    if (modInfo.has("icon")) {
                        String iconPath = modInfo.get("icon").getAsString();
                        JarEntry iconEntry = jar.getJarEntry(iconPath);
                        if (iconEntry != null) {
                            InputStream iconStream = jar.getInputStream(iconEntry);
                            icon = new ImageIcon(iconStream.readAllBytes());
                            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(img);
                        }
                    }
                }

                // Make name human-readable if needed
                modName = humanReadableName(modName);

                // Add to list
                Mod mod = new Mod(modName, icon);
                mod.version = version;
                mod.description = description;
                mod.mcVersion = mcVersion;
                modList.add(mod);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return modList;
    }

    // Convert raw jar/mod name into human-readable form
    private static String humanReadableName(String raw) {
        raw = raw.replace("_", " ").replace("-", " ");
        String[] words = raw.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.length() > 0)
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}