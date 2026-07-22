package org.projectlauncher.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class RuleEvaluator {

    private RuleEvaluator() {
    }


    public static boolean isAllowed(JsonObject object) {

        if (!object.has("rules")) {
            return true;
        }

        JsonArray rules =
                object.getAsJsonArray("rules");

        boolean allowed = false;

        for (int i = 0; i < rules.size(); i++) {

            JsonObject rule =
                    rules.get(i).getAsJsonObject();

            String action =
                    rule.get("action").getAsString();

            if (!matches(rule)) {
                continue;
            }

            allowed = action.equals("allow");
        }

        return allowed;
    }


    private static boolean matches(JsonObject rule) {

        if (!rule.has("os")) {
            return true;
        }

        JsonObject os =
                rule.getAsJsonObject("os");

        if (!os.has("name")) {
            return true;
        }

        String required =
                os.get("name")
                        .getAsString();

        return getCurrentOS()
                .equals(required);
    }


    private static String getCurrentOS() {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        if (os.contains("win")) {
            return "windows";
        }

        if (os.contains("mac")) {
            return "osx";
        }

        if (os.contains("linux")) {
            return "linux";
        }

        return "unknown";
    }
}