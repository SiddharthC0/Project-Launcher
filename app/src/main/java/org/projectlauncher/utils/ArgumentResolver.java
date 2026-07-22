package org.projectlauncher.utils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ArgumentResolver {

    private final Map<String, String> values;

    public ArgumentResolver(
            String username,
            String uuid,
            String accessToken,
            String userType,
            String versionId,
            Path gameDirectory,
            Path assetsDirectory,
            String assetIndexId,
            Path nativesDirectory
    ) {

        values = new HashMap<>();

        values.put("auth_player_name", username);
        values.put("auth_uuid", uuid);
        values.put("auth_access_token", accessToken);

        values.put("user_type", userType);
        values.put("version_name", versionId);

        values.put(
                "game_directory",
                gameDirectory.toAbsolutePath().toString()
        );

        values.put(
                "assets_root",
                assetsDirectory.toAbsolutePath().toString()
        );

        values.put(
                "assets_index_name",
                assetIndexId
        );

        values.put(
                "natives_directory",
                nativesDirectory.toAbsolutePath().toString()
        );
    }


    public String resolve(String argument) {

        String result = argument;

        for (Map.Entry<String, String> entry : values.entrySet()) {

            result = result.replace(
                    "${" + entry.getKey() + "}",
                    entry.getValue()
            );
        }

        return result;
    }
}