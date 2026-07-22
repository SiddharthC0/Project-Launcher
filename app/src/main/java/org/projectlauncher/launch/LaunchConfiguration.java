package org.projectlauncher.launch;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class LaunchConfiguration {

    private final Path gameDirectory;
    private final Path assetsDirectory;
    private final Path librariesDirectory;
    private final Path nativesDirectory;
    private final Path clientJar;

    private final String versionId;
    private final String assetIndexId;
    private final String mainClass;

    private final String username;
    private final String uuid;
    private final String accessToken;
    private final String userType;
    private final String versionType;

    private final List<String> jvmArguments;
    private final List<String> gameArguments;
    private final List<Path> classpath;

    public LaunchConfiguration(
            Path gameDirectory,
            Path assetsDirectory,
            Path librariesDirectory,
            Path nativesDirectory,
            Path clientJar,
            String versionId,
            String assetIndexId,
            String mainClass,
            String username,
            String uuid,
            String accessToken,
            String userType,
            String versionType,
            List<String> jvmArguments,
            List<String> gameArguments,
            List<Path> classpath) {

        this.gameDirectory = Objects.requireNonNull(gameDirectory);
        this.assetsDirectory = Objects.requireNonNull(assetsDirectory);
        this.librariesDirectory = Objects.requireNonNull(librariesDirectory);
        this.nativesDirectory = Objects.requireNonNull(nativesDirectory);
        this.clientJar = Objects.requireNonNull(clientJar);

        this.versionId = Objects.requireNonNull(versionId);
        this.assetIndexId = Objects.requireNonNull(assetIndexId);
        this.mainClass = Objects.requireNonNull(mainClass);

        this.username = Objects.requireNonNull(username);
        this.uuid = Objects.requireNonNull(uuid);
        this.accessToken = Objects.requireNonNull(accessToken);
        this.userType = Objects.requireNonNull(userType);
        this.versionType = Objects.requireNonNull(versionType);

        this.jvmArguments = List.copyOf(jvmArguments);
        this.gameArguments = List.copyOf(gameArguments);
        this.classpath = List.copyOf(classpath);
    }

    public Path getGameDirectory() {
        return gameDirectory;
    }

    public Path getAssetsDirectory() {
        return assetsDirectory;
    }

    public Path getLibrariesDirectory() {
        return librariesDirectory;
    }

    public Path getNativesDirectory() {
        return nativesDirectory;
    }

    public Path getClientJar() {
        return clientJar;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getAssetIndexId() {
        return assetIndexId;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserType() {
        return userType;
    }

    public String getVersionType() {
        return versionType;
    }

    public List<String> getJvmArguments() {
        return jvmArguments;
    }

    public List<String> getGameArguments() {
        return gameArguments;
    }

    public List<Path> getClasspath() {
        return classpath;
    }
}