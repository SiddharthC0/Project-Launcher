package org.projectlauncher.install;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeExtractor {

    /**
     * Extract all OS-specific natives from LWJGL/OpenAL jars.
     * Skips META-INF and non-native files to avoid spam errors.
     */
    public static void extractNatives(Path librariesDir, Path nativesDir) throws IOException {
        if (!Files.exists(nativesDir)) Files.createDirectories(nativesDir);

        String osName = detectOS();

        Files.walk(librariesDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(jarPath -> {
                    try (ZipFile zip = new ZipFile(jarPath.toFile())) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();

                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String name = entry.getName();

                            // Skip META-INF and directories
                            if (entry.isDirectory() || name.startsWith("META-INF/")) continue;

                            // Only extract OS-specific natives or general classes
                            boolean isNative = name.toLowerCase().contains(osName.toLowerCase());
                            boolean isClass = name.endsWith(".class") || name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib");

                            if (isNative || isClass) {
                                // Handle $ in class names and safe paths
                                String relativePath = name.replace("/", File.separator);

                                Path outPath = nativesDir.resolve(relativePath);
                                if (!Files.exists(outPath.getParent())) Files.createDirectories(outPath.getParent());

                                try (InputStream in = zip.getInputStream(entry);
                                     FileOutputStream out = new FileOutputStream(outPath.toFile())) {
                                    byte[] buffer = new byte[8192];
                                    int read;
                                    while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: Failed to extract from " + jarPath + " - " + e.getMessage());
                    }
                });

        System.out.println("Natives extracted to: " + nativesDir);
    }

    /**
     * Detect the OS for native extraction.
     */
    public static String detectOS() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "macos";
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) return "linux";
        return "unknown";
    }

    public static void main(String[] args) {
        try {
            Path libs = Paths.get("launcher-data/downloads/libraries");
            Path natives = Paths.get("launcher-data/natives");
            extractNatives(libs, natives);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}