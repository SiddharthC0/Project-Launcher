package org.projectlauncher.launch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class MinecraftLauncher {

    private MinecraftLauncher() {
    }


    public static Process launch(LaunchConfiguration config) throws IOException {

        if (config == null) {
            throw new IllegalArgumentException("LaunchConfiguration cannot be null");
        }

        List<String> command = new ArrayList<>();


        // Java executable
        String java = getJavaExecutable();

        if (!new File(java).exists()) {
            throw new IOException(
                    "Java executable not found:\n" + java
            );
        }

        command.add(java);


        // JVM arguments
        if (config.getJvmArguments() != null) {
            command.addAll(config.getJvmArguments());
        }


        // Classpath
        String classpath = buildClasspath(config);

        if (classpath.isEmpty()) {
            throw new IOException(
                    "Classpath is empty. Cannot launch Minecraft."
            );
        }

        command.add("-cp");
        command.add(classpath);


        // Main class
        if (config.getMainClass() == null ||
                config.getMainClass().isBlank()) {

            throw new IOException(
                    "Minecraft main class is missing."
            );
        }

        command.add(config.getMainClass());


        // Game arguments
        if (config.getGameArguments() != null) {
            command.addAll(config.getGameArguments());
        }


        /*
         * DEBUG
         */
        System.out.println("\n========== JAVA ==========");
        System.out.println(java);

        System.out.println("\n========== MAIN CLASS ==========");
        System.out.println(config.getMainClass());

        System.out.println("\n========== CLASSPATH ==========");
        for (Path path : config.getClasspath()) {
            System.out.println(path.toAbsolutePath());
        }

        System.out.println("\n========== FULL CLASSPATH ==========");
        System.out.println(classpath);

        System.out.println("\n========== COMMAND ==========");
        System.out.println(String.join(" ", command));
        System.out.println("=============================\n");
        System.out.println("\n========== NATIVE DEBUG ==========");

        String nativePath = System.getProperty("projectlauncher.natives");

        if (nativePath != null) {

            File natives = new File(nativePath);

            System.out.println("Native directory: " + natives);

            File lwjgl =
                    new File(natives, "lwjgl.dll");

            System.out.println(
                    "lwjgl.dll exists: " + lwjgl.exists()
            );

        } else {

            System.out.println(
                    "No native debug path provided"
            );
        }

        System.out.println("===============================\n");

        /*
         * Game directory
         */
        if (config.getGameDirectory() == null) {
            throw new IOException(
                    "Game directory is missing."
            );
        }

        File gameDir =
                config.getGameDirectory()
                        .toFile();

        if (!gameDir.exists()) {
            gameDir.mkdirs();
        }


        /*
         * Client jar check
         */
        if (config.getClientJar() != null) {

            File client =
                    config.getClientJar()
                            .toFile();

            System.out.println("Client jar:");
            System.out.println(client.getAbsolutePath());
            System.out.println("Exists: " + client.exists());
            System.out.println("Size: " + client.length());
        }


        ProcessBuilder builder =
                new ProcessBuilder(command);


        builder.directory(gameDir);

        // Send Minecraft output to launcher console
        builder.inheritIO();
        Path natives = Path.of(
                "C:\\Users\\Siddharth C\\Documents\\Project Launcher\\launcher-data\\natives"
        );

        System.out.println("Native folder exists: " + natives.toFile().exists());

        File lwjgl = natives.resolve("lwjgl.dll").toFile();

        System.out.println("lwjgl.dll exists: " + lwjgl.exists());
        System.out.println("lwjgl.dll path: " + lwjgl.getAbsolutePath());

        System.out.println("===== ARGUMENT LIST =====");

        for (int i = 0; i < command.size(); i++) {
            System.out.println(i + " : [" + command.get(i) + "]");
        }

        System.out.println("=========================");

        return builder.start();


    }



    private static String getJavaExecutable() {

        String executable =
                System.getProperty("os.name")
                        .toLowerCase()
                        .contains("win")
                        ? "java.exe"
                        : "java";


        return System.getProperty("java.home")
                + File.separator
                + "bin"
                + File.separator
                + executable;
    }



    private static String buildClasspath(
            LaunchConfiguration config
    ) {

        if (config.getClasspath() == null ||
                config.getClasspath().isEmpty()) {

            return "";
        }


        return String.join(
                File.pathSeparator,

                config.getClasspath()
                        .stream()
                        .map(path ->
                                path.toAbsolutePath().toString()
                        )
                        .toList()
        );
    }
}