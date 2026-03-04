package org.projectlauncher.install;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Downloads the Minecraft client jar
 */
public class ClientDownloader {

    public static void downloadClient(String clientUrl, Path downloadsDir) throws Exception {
        if (!Files.exists(downloadsDir)) Files.createDirectories(downloadsDir);

        Path clientFile = downloadsDir.resolve("client.jar");

        try (InputStream in = new URL(clientUrl).openStream();
             FileOutputStream out = new FileOutputStream(clientFile.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        }

        System.out.println("Client downloaded to: " + clientFile);
    }
}