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

        put("auth_player_name", username);
        put("auth_uuid", uuid);
        put("auth_access_token", accessToken);

        put("auth_session", accessToken);
        put("auth_xuid", "");

        put("user_type", userType);
        put("version_name", versionId);

        put(
                "game_directory",
                gameDirectory
        );

        put(
                "assets_root",
                assetsDirectory
        );

        put(
                "assets_index_name",
                assetIndexId
        );

        put(
                "natives_directory",
                nativesDirectory
        );
    }



    private void put(
            String key,
            String value
    ) {

        values.put(
                key,
                value == null ? "" : value
        );
    }


    private void put(
            String key,
            Path path
    ) {

        values.put(
                key,
                path == null
                        ? ""
                        : path.toAbsolutePath().toString()
        );
    }



    public String resolve(
            String argument
    ) {

        String result = argument;


        for (Map.Entry<String, String> entry :
                values.entrySet()) {


            result = result.replace(
                    "${" + entry.getKey() + "}",
                    entry.getValue()
            );
        }


        return result;
    }
}