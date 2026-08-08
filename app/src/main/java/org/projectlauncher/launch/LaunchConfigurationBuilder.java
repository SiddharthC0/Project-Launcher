package org.projectlauncher.launch;

import com.google.gson.JsonObject;
import org.projectlauncher.instances.Instance;
import org.projectlauncher.utils.ArgumentResolver;
import org.projectlauncher.utils.ClasspathBuilder;
import org.projectlauncher.utils.GameArgumentsBuilder;
import org.projectlauncher.utils.JvmArgumentsBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class LaunchConfigurationBuilder {

    public LaunchConfiguration build(
            JsonObject versionJson,
            Instance instance,
            Path gameDirectory,
            Path librariesDirectory,
            Path assetsDirectory,
            Path nativesDirectory,
            Path clientJar
    ) {

        Objects.requireNonNull(versionJson, "Version JSON cannot be null");
        Objects.requireNonNull(instance, "Instance cannot be null");
        Objects.requireNonNull(gameDirectory, "Game directory cannot be null");
        Objects.requireNonNull(librariesDirectory, "Libraries directory cannot be null");
        Objects.requireNonNull(assetsDirectory, "Assets directory cannot be null");
        Objects.requireNonNull(nativesDirectory, "Natives directory cannot be null");
        Objects.requireNonNull(clientJar, "Client jar cannot be null");


        String versionId =
                requireString(versionJson, "id");


        String mainClass =
                requireString(versionJson, "mainClass");


        String assetIndexId = "legacy";

        if (versionJson.has("assetIndex")) {

            JsonObject assetIndex =
                    versionJson.getAsJsonObject("assetIndex");

            if (assetIndex.has("id")) {
                assetIndexId =
                        assetIndex.get("id").getAsString();
            }
        }


        String username =
                resolveUsername(instance);


        String uuid =
                generateOfflineUUID(username);


        String accessToken = "0";
        String userType = "legacy";


        String versionType =
                versionJson.has("type")
                        ? versionJson.get("type").getAsString()
                        : "release";


        ArgumentResolver resolver =
                new ArgumentResolver(
                        username,
                        uuid,
                        accessToken,
                        userType,
                        versionId,
                        gameDirectory,
                        assetsDirectory,
                        assetIndexId,
                        nativesDirectory
                );


        List<Path> classpath =
                ClasspathBuilder.build(
                        librariesDirectory,
                        clientJar
                );


        List<String> jvmArguments =
                JvmArgumentsBuilder.build(
                        versionJson,
                        resolver
                );

        jvmArguments.add(
                "-Djava.library.path=" +
                        nativesDirectory.toAbsolutePath()
        );

        jvmArguments.add(
                "-Dorg.lwjgl.librarypath=" +
                        nativesDirectory.toAbsolutePath()
        );

        jvmArguments.add(
                "-Dorg.lwjgl.system.SharedLibraryExtractPath=" +
                        nativesDirectory.toAbsolutePath()
        );


        List<String> gameArguments =
                GameArgumentsBuilder.build(
                        versionJson,
                        resolver
                );



        return new LaunchConfiguration(
                gameDirectory,
                assetsDirectory,
                librariesDirectory,
                nativesDirectory,
                clientJar,
                versionId,
                assetIndexId,
                mainClass,
                username,
                uuid,
                accessToken,
                userType,
                versionType,
                jvmArguments,
                gameArguments,
                classpath
        );
    }


    private String requireString(
            JsonObject json,
            String key
    ) {

        if (!json.has(key)) {
            throw new IllegalArgumentException(
                    "Missing version JSON property: " + key
            );
        }

        return json.get(key).getAsString();
    }


    private String resolveUsername(
            Instance instance
    ) {

        return "Player";
    }


    private String generateOfflineUUID(
            String username
    ) {

        return UUID.nameUUIDFromBytes(
                ("OfflinePlayer:" + username)
                        .getBytes(StandardCharsets.UTF_8)
        ).toString();
    }
}