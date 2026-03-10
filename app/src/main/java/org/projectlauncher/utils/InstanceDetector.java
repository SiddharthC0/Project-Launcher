package org.projectlauncher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstanceDetector {

    public static class InstanceInfo {
        public String name;
        public String path;
        public String type;
        public String state;

        public InstanceInfo(String name, String path, String type, String state) {
            this.name = name;
            this.path = path;
            this.type = type;
            this.state = state;
        }
    }

    public static List<InstanceInfo> getInstances() {

        List<InstanceInfo> instances = new ArrayList<>();

        String mcPath = System.getProperty("user.home") +
                File.separator + "AppData" +
                File.separator + "Roaming" +
                File.separator + ".minecraft" +
                File.separator + "versions";

        File versionsDir = new File(mcPath);

        if (!versionsDir.exists()) return instances;

        for (File folder : versionsDir.listFiles()) {

            if (!folder.isDirectory()) continue;

            String name = folder.getName();
            String type = detectType(name);

            instances.add(new InstanceInfo(
                    name,
                    folder.getAbsolutePath(),
                    type,
                    "Installed"
            ));
        }

        return instances;
    }

    private static String detectType(String versionName) {

        String lower = versionName.toLowerCase();

        if (lower.contains("fabric"))
            return "Fabric";

        if (lower.contains("forge"))
            return "Forge";

        if (lower.contains("quilt"))
            return "Quilt";

        return "Vanilla";
    }
}

// Example code that i wrote