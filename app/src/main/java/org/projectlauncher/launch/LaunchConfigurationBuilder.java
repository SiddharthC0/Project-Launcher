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

        Objects.requireNonNull(versionJson, "versionJson");
        Objects.requireNonNull(gameDirectory, "gameDirectory");
        Objects.requireNonNull(librariesDirectory, "librariesDirectory");
        Objects.requireNonNull(assetsDirectory, "assetsDirectory");
        Objects.requireNonNull(nativesDirectory, "nativesDirectory");
        Objects.requireNonNull(clientJar, "clientJar");

        String versionId =
                versionJson.get("id")
                        .getAsString();

        String mainClass =
                versionJson.get("mainClass")
                        .getAsString();

        String assetIndexId =
                versionJson
                        .getAsJsonObject("assetIndex")
                        .get("id")
                        .getAsString();


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