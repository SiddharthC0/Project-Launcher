package org.projectlauncher.install;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeExtractor {

    /**
     * Extracts all natives from LWJGL/OpenAL jars
     */
    public static void extractNatives(Path librariesDir, Path nativesDir, String osName) throws Exception {
        if (!Files.exists(nativesDir)) Files.createDirectories(nativesDir);

        Files.walk(librariesDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(jarPath -> {
                    try (ZipFile zip = new ZipFile(jarPath.toFile())) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String name = entry.getName();

                            // Only extract OS-specific natives
                            if (name.contains(osName)) {
                                // Ensure nested parent folders are created
                                Path outPath = nativesDir.resolve(name.substring(name.indexOf('/') + 1).replace("/", File.separator));
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
                        System.err.println("Failed to extract from: " + jarPath + " - " + e.getMessage());
                    }
                });

        System.out.println("Natives extracted to: " + nativesDir);
    }
}