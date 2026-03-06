package org.projectlauncher.launch;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinecraftLauncher {

    /**
     * Launch Minecraft with proper classpath, natives, and assets handling.
     *
     * @param downloadsDir The launcher downloads folder (contains client.jar and libraries)
     * @param nativesDir   The folder where native libraries are extracted
     * @param assetsDir    The assets folder
     * @param assetIndexId The asset index id
     * @param versionId    The Minecraft version (vanilla or modded)
     * @param username     The username to use (offline or online)
     */
    public static void launchMinecraft(Path downloadsDir,
                                       Path nativesDir,
                                       Path assetsDir,
                                       String assetIndexId,
                                       String versionId,
                                       String username) {

        try {
            List<String> command = new ArrayList<>();

            // Java executable
            String javaExe = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            command.add(javaExe);

            // JVM options
            command.add("-Xmx2G");
            command.add("-Djava.library.path=" + nativesDir.toAbsolutePath());

            // Build classpath from all libraries + client.jar
            List<Path> libraryJars = Files.walk(downloadsDir.resolve("libraries"))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".jar"))
                    .collect(Collectors.toList());

            libraryJars.add(downloadsDir.resolve("client.jar")); // client.jar

            String classpath = libraryJars.stream()
                    .map(Path::toString)
                    .collect(Collectors.joining(System.getProperty("path.separator")));

            command.add("-cp");
            command.add(classpath);

            // Minecraft main class
            command.add("net.minecraft.client.main.Main");

            // Minecraft arguments
            command.add("--username");
            command.add(username);

            command.add("--version");
            command.add(versionId);

            Path gameDir = downloadsDir.getParent(); // launcher-data as gameDir
            command.add("--gameDir");
            command.add(gameDir.toAbsolutePath().toString());

            command.add("--assetsDir");
            command.add(assetsDir.toAbsolutePath().toString());

            command.add("--assetIndex");
            command.add(assetIndexId);

            command.add("--accessToken");
            command.add("offline-token"); // offline mode

            // Optional window size
            command.add("--width");
            command.add("854");
            command.add("--height");
            command.add("480");

            // Start process
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            pb.start();

            System.out.println("Minecraft launched with version: " + versionId);

        } catch (IOException e) {
            System.err.println("Failed to launch Minecraft!");
            e.printStackTrace();
        }
    }
}