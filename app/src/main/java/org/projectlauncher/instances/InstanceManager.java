package org.projectlauncher.instances;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InstanceManager {

    private static final String FILE = "instance-info.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Instance> detectInstances() {

        List<Instance> saved = loadInstances();
        Map<String, Instance> savedMap = new HashMap<>();

        for (Instance i : saved) {
            savedMap.put(i.version, i);
        }

        List<Instance> result = new ArrayList<>();

        File versionsDir = new File(System.getenv("APPDATA") + "/.minecraft/versions");

        if (!versionsDir.exists())
            return result;

        File[] folders = versionsDir.listFiles(File::isDirectory);

        if (folders == null)
            return result;

        for (File folder : folders) {

            String versionId = folder.getName();

            Instance inst;

            if (savedMap.containsKey(versionId)) {

                inst = savedMap.get(versionId);

            } else {

                inst = new Instance();
                inst.name = versionId; // default nickname
                inst.version = versionId;
                inst.type = detectType(versionId);
                inst.gameDir = folder.getAbsolutePath();
                inst.jvmArgs = Arrays.asList("-Xmx2G", "-Xms1G");

                saved.add(inst);
            }

            result.add(inst);
        }

        saveInstances(saved);

        return result;
    }

    private static String detectType(String version) {

        String v = version.toLowerCase();

        if (v.contains("fabric"))
            return "Fabric";

        if (v.contains("forge"))
            return "Forge";

        if (v.contains("quilt"))
            return "Quilt";

        return "Vanilla";
    }

    private static List<Instance> loadInstances() {

        try {

            File f = new File(FILE);

            if (!f.exists())
                return new ArrayList<>();

            InstanceDatabase db =
                    gson.fromJson(new FileReader(f), InstanceDatabase.class);

            if (db.instances == null)
                db.instances = new ArrayList<>();

            return db.instances;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private static void saveInstances(List<Instance> instances) {

        try {

            InstanceDatabase db = new InstanceDatabase();
            db.instances = instances;

            FileWriter writer = new FileWriter(FILE);
            gson.toJson(db, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}