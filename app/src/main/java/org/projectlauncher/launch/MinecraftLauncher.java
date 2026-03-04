package org.projectlauncher.launch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinecraftLauncher {

    public static void launchMinecraft(Path downloadsDir,
                                       Path nativesDir,
                                       Path assetsDir,
                                       String assetIndexId) throws IOException {

        List<String> command = new ArrayList<>();

        // Java executable
        command.add(System.getProperty("java.home") + "/bin/java");

        // JVM options
        command.add("-Xmx2G");
        command.add("-Djava.library.path=" + nativesDir.toAbsolutePath());

        // Build full classpath (all library jars + client.jar)
        List<Path> libraryJars = Files.walk(downloadsDir.resolve("libraries"))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".jar"))
                .collect(Collectors.toList());

        libraryJars.add(downloadsDir.resolve("client.jar")); // include client.jar

        String classpath = libraryJars.stream()
                .map(Path::toString)
                .reduce((a, b) -> a + ";" + b) // Windows separator
                .orElse("");

        command.add("-cp");
        command.add(classpath);

        // Minecraft main class
        command.add("net.minecraft.client.main.Main");

        // Minecraft arguments
        command.add("--username");
        command.add("Siddy5303");

        command.add("--version");
        command.add("1.21.4");

        command.add("--gameDir");
        command.add(downloadsDir.toAbsolutePath().toString());

        // ✅ FIXED: correct assets directory
        command.add("--assetsDir");
        command.add(assetsDir.toAbsolutePath().toString());

        // ✅ FIXED: correct asset index id
        command.add("--assetIndex");
        command.add(assetIndexId);

        command.add("--accessToken");
        command.add("offline-token");

        // Start process
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        pb.start();
    }
}