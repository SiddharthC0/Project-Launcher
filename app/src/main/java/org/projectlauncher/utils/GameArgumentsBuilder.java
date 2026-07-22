package org.projectlauncher.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class GameArgumentsBuilder {

    private GameArgumentsBuilder() {
    }


    public static List<String> build(
            JsonObject versionJson,
            ArgumentResolver resolver
    ) {

        List<String> arguments = new ArrayList<>();

        if (versionJson.has("arguments")) {

            JsonObject argumentObject =
                    versionJson.getAsJsonObject("arguments");

            if (argumentObject.has("game")) {

                addModernArguments(
                        argumentObject.getAsJsonArray("game"),
                        arguments,
                        resolver
                );
            }

        } else if (versionJson.has("minecraftArguments")) {

            addLegacyArguments(
                    versionJson
                            .get("minecraftArguments")
                            .getAsString(),
                    arguments,
                    resolver
            );
        }

        return arguments;
    }


    private static void addModernArguments(
            JsonArray jsonArguments,
            List<String> output,
            ArgumentResolver resolver
    ) {

        for (JsonElement element : jsonArguments) {

            if (!element.isJsonPrimitive()) {
                continue;
            }

            String value =
                    element.getAsString();

            output.add(
                    resolver.resolve(value)
            );
        }
    }


    private static void addLegacyArguments(
            String raw,
            List<String> output,
            ArgumentResolver resolver
    ) {

        String[] split =
                raw.split(" ");

        for (String argument : split) {

            output.add(
                    resolver.resolve(argument)
            );
        }
    }
}