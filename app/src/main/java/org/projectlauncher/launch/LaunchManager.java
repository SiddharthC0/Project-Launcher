package org.projectlauncher.launch;

import com.google.gson.JsonObject;
import org.projectlauncher.install.InstallationManager;
import org.projectlauncher.manifest.VersionLoader;
import org.projectlauncher.instances.Instance;

import java.nio.file.Path;

public final class LaunchManager {

    private LaunchManager() {
    }

    public static Process launch(
            String versionId,
            Instance instance,
            Path cacheDirectory,
            Path downloadsDirectory,
            Path assetsDirectory,
            Path nativesDirectory
    ) throws Exception {

        JsonObject versionJson =
                VersionLoader.load(
                        versionId,
                        cacheDirectory.toFile()
                );

        InstallationManager.install(
                versionJson,
                versionId,
                downloadsDirectory,
                assetsDirectory,
                nativesDirectory
        );

        Path clientJar =
                downloadsDirectory.resolve(versionId + ".jar");

        LaunchConfiguration configuration =
                new LaunchConfigurationBuilder()
                        .build(
                                versionJson,
                                instance,
                                downloadsDirectory.getParent(),
                                downloadsDirectory.resolve("libraries"),
                                assetsDirectory,
                                nativesDirectory,
                                clientJar
                        );

        return MinecraftLauncher.launch(configuration);
    }
}