package org.projectlauncher.install;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.security.MessageDigest;

public class ClientDownloader {

    public static void downloadClient(
            String versionId,
            String clientUrl,
            String expectedSha1,
            Path downloadsDir
    ) throws Exception {

        Files.createDirectories(downloadsDir);

        Path clientFile = downloadsDir.resolve(versionId + ".jar");
        Path tempFile = downloadsDir.resolve(versionId + ".jar.tmp");

        int attempts = 3;

        for (int attempt = 1; attempt <= attempts; attempt++) {

            try {

                System.out.println(
                        "Downloading client attempt "
                                + attempt
                                + "/"
                                + attempts
                );

                URLConnection connection =
                        new URL(clientUrl).openConnection();

                connection.setConnectTimeout(10000);
                connection.setReadTimeout(60000);

                try (
                        InputStream in = connection.getInputStream();
                        FileOutputStream out = new FileOutputStream(tempFile.toFile())
                ) {

                    byte[] buffer = new byte[8192];
                    int read;

                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }

                if (expectedSha1 != null && !expectedSha1.isBlank()) {

                    String actual = sha1(tempFile);

                    System.out.println("SHA1: " + actual);

                    if (!actual.equalsIgnoreCase(expectedSha1)) {
                        throw new Exception("SHA1 mismatch!");
                    }
                }

                Files.move(
                        tempFile,
                        clientFile,
                        StandardCopyOption.REPLACE_EXISTING
                );

                System.out.println(
                        "Client verified and installed: "
                                + clientFile
                );

                return;

            } catch (Exception e) {

                System.err.println(
                        "Client download failed: "
                                + e.getMessage()
                );

                Files.deleteIfExists(tempFile);

                if (attempt == attempts) {
                    throw e;
                }

                Thread.sleep(2000);
            }
        }
    }

    private static String sha1(Path file) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-1");

        try (InputStream in = Files.newInputStream(file)) {

            byte[] buffer = new byte[8192];
            int read;

            while ((read = in.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }

        StringBuilder result = new StringBuilder();

        for (byte b : digest.digest()) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}