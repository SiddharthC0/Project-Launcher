package org.projectlauncher.install;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class NativeExtractor {


    private NativeExtractor() {
    }



    /**
     * Extract native binaries from Minecraft native jars.
     */
    public static void extractNatives(
            Path librariesDir,
            Path nativesDir
    ) throws IOException {


        Files.createDirectories(nativesDir);


        System.out.println(
                "Extracting natives..."
        );


        Files.walk(librariesDir)
                .filter(Files::isRegularFile)
                .filter(path ->
                        path.toString().endsWith(".jar"))
                .forEach(jar -> {

                    extractJar(
                            jar,
                            nativesDir
                    );

                });



        System.out.println(
                "Natives extracted to: "
                        + nativesDir
        );
    }




    private static void extractJar(
            Path jarPath,
            Path nativesDir
    ) {


        try (ZipFile zip =
                     new ZipFile(jarPath.toFile())) {



            Enumeration<? extends ZipEntry> entries =
                    zip.entries();



            while (entries.hasMoreElements()) {


                ZipEntry entry =
                        entries.nextElement();



                if (entry.isDirectory()) {
                    continue;
                }



                String name =
                        entry.getName();



                // Ignore signatures
                if (name.startsWith("META-INF")) {
                    continue;
                }



                if (!isNativeFile(name)) {
                    continue;
                }



                Path output =
                        nativesDir.resolve(
                                Paths.get(name)
                                        .getFileName()
                        );



                Files.createDirectories(
                        output.getParent()
                );



                try (InputStream in =
                             zip.getInputStream(entry);

                     OutputStream out =
                             Files.newOutputStream(
                                     output,
                                     StandardOpenOption.CREATE,
                                     StandardOpenOption.TRUNCATE_EXISTING
                             )) {



                    byte[] buffer =
                            new byte[8192];


                    int length;


                    while ((length = in.read(buffer)) != -1) {

                        out.write(
                                buffer,
                                0,
                                length
                        );
                    }
                }



                System.out.println(
                        "Extracted native: "
                                + output.getFileName()
                );
            }


        } catch (Exception e) {


            System.err.println(
                    "Failed extracting "
                            + jarPath
            );


            e.printStackTrace();
        }
    }






    private static boolean isNativeFile(
            String name
    ) {


        String lower =
                name.toLowerCase(Locale.ROOT);



        return lower.endsWith(".dll")
                || lower.endsWith(".so")
                || lower.endsWith(".dylib");
    }






    public static String detectOS() {


        String os =
                System.getProperty("os.name")
                        .toLowerCase(Locale.ROOT);



        if (os.contains("win")) {

            return "windows";
        }


        if (os.contains("mac")) {

            return "macos";
        }


        if (os.contains("linux")
                || os.contains("unix")) {

            return "linux";
        }



        return "unknown";
    }





    public static void main(String[] args) {


        try {


            Path libs =
                    Paths.get(
                            "launcher-data/downloads/libraries"
                    );


            Path natives =
                    Paths.get(
                            "launcher-data/natives"
                    );



            extractNatives(
                    libs,
                    natives
            );



        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void extract(
            Path nativeJar,
            Path nativesDir
    ) throws IOException {

        System.out.println("================================");
        System.out.println("Extracting: " + nativeJar.getFileName());
        System.out.println("================================");


        Files.createDirectories(nativesDir);


        try (ZipFile zip = new ZipFile(nativeJar.toFile())) {


            Enumeration<? extends ZipEntry> entries =
                    zip.entries();


            while (entries.hasMoreElements()) {


                ZipEntry entry =
                        entries.nextElement();

                System.out.println(" -> " + entry.getName());
                if (entry.isDirectory()) {
                    continue;
                }


                String name =
                        entry.getName();


                if (!isNativeFile(name)) {
                    continue;
                }


                Path output =
                        nativesDir.resolve(
                                Paths.get(name).getFileName()
                        );


                try (InputStream in =
                             zip.getInputStream(entry);

                     OutputStream out =
                             Files.newOutputStream(
                                     output,
                                     StandardOpenOption.CREATE,
                                     StandardOpenOption.TRUNCATE_EXISTING
                             )) {


                    byte[] buffer =
                            new byte[8192];

                    int read;


                    while ((read = in.read(buffer)) != -1) {

                        out.write(buffer, 0, read);
                    }
                }


                System.out.println(
                        "Extracted native: "
                                + output.getFileName()
                );
            }
        }
    }
}