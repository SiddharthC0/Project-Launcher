package org.projectlauncher.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class JvmArgumentsBuilder {

    private JvmArgumentsBuilder() {
    }

    public static List<String> build(
            JsonObject versionJson,
            ArgumentResolver resolver
    ) {

        List<String> arguments = new ArrayList<>();

        if (versionJson.has("arguments")) {

            JsonObject argumentObject =
                    versionJson.getAsJsonObject("arguments");

            if (argumentObject.has("jvm")) {

                addArguments(
                        argumentObject.getAsJsonArray("jvm"),
                        arguments,
                        resolver
                );
            }
        }

        return arguments;
    }

    private static void addArguments(
            JsonArray array,
            List<String> output,
            ArgumentResolver resolver
    ) {

        boolean skipNext = false;

        for (JsonElement element : array) {

            if (skipNext) {
                skipNext = false;
                continue;
            }

            if (!element.isJsonPrimitive()) {
                continue;
            }

            String arg = resolver.resolve(
                    element.getAsString()
            );

            // Skip Mojang's classpath placeholder.
            // MinecraftLauncher adds the real classpath itself.
            if (arg.equals("-cp") || arg.equals("-classpath")) {
                skipNext = true;
                continue;
            }

            if (arg.equals("${classpath}")) {
                continue;
            }

            output.add(arg);
        }
    }
}