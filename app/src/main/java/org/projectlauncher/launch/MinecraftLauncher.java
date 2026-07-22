package org.projectlauncher.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;

public final class MinecraftLauncher {

    private MinecraftLauncher() {
    }


    public static Process launch(
            LaunchConfiguration config
    ) throws IOException {

        List<String> command =
                new ArrayList<>();

        command.add(
                getJavaExecutable()
        );

        command.addAll(
                config.getJvmArguments()
        );
        for (Path path : config.getClasspath()) {
            System.out.println(path.toAbsolutePath());
        }
        command.add(
                "-cp"
        );

        command.add(
                buildClasspath(config)
        );

        command.add(
                config.getMainClass()
        );

        command.addAll(
                config.getGameArguments()
        );
        System.out.println("\n========== CLASSPATH ==========");
        System.out.println(buildClasspath(config));
        System.out.println("================================\n");

        System.out.println("Main class = " + config.getMainClass());

        System.out.println("\n========== COMMAND ==========");
        System.out.println(String.join(" ", command));
        System.out.println("=============================\n");

        ProcessBuilder builder =
                new ProcessBuilder(command);

        builder.directory(
                config.getGameDirectory().toFile()
        );

        builder.inheritIO();
        System.out.println("Exists: " + config.getClientJar().toAbsolutePath());
        System.out.println("Exists? " + config.getClientJar().toFile().exists());
        System.out.println("Size: " + config.getClientJar().toFile().length());
        System.out.println("Absolute: " + config.getClientJar().toAbsolutePath());

        System.out.println("\n===== ABSOLUTE CLASSPATH =====");

        for (Path p : config.getClasspath()) {
            System.out.println(p.toAbsolutePath());
        }

        System.out.println("==============================");
        return builder.start();
    }


    private static String getJavaExecutable() {

        return System.getProperty("java.home")
                + File.separator
                + "bin"
                + File.separator
                + "java";
    }


    private static String buildClasspath(
        LaunchConfiguration config
    ) {

    return String.join(
            File.pathSeparator,
            config.getClasspath()
                    .stream()
                    .map(path -> path.toAbsolutePath().toString())
                    .toList()
    );
}
}