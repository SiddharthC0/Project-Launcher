package org.projectlauncher.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


        removeDuplicateQuickPlay(arguments);


        System.out.println("========== GAME ARGUMENTS ==========");

        for (String arg : arguments) {
            System.out.println(arg);
        }

        System.out.println("====================================");


        return arguments;
    }





    private static void addModernArguments(
            JsonArray jsonArguments,
            List<String> output,
            ArgumentResolver resolver
    ) {


        for (JsonElement element : jsonArguments) {


            // Normal argument
            if (element.isJsonPrimitive()) {


                output.add(
                        resolver.resolve(
                                element.getAsString()
                        )
                );


                continue;
            }




            // Conditional argument
            if (element.isJsonObject()) {


                JsonObject object =
                        element.getAsJsonObject();



                if (!isAllowed(object)) {
                    continue;
                }



                if (!object.has("value")) {
                    continue;
                }



                JsonElement value =
                        object.get("value");



                if (value.isJsonArray()) {


                    for (JsonElement item :
                            value.getAsJsonArray()) {


                        output.add(
                                resolver.resolve(
                                        item.getAsString()
                                )
                        );
                    }


                } else {


                    output.add(
                            resolver.resolve(
                                    value.getAsString()
                            )
                    );
                }
            }
        }
    }






    private static boolean isAllowed(
            JsonObject argument
    ) {


        if (!argument.has("rules")) {
            return true;
        }



        JsonArray rules =
                argument.getAsJsonArray("rules");



        boolean allowed = false;



        for (JsonElement element : rules) {


            JsonObject rule =
                    element.getAsJsonObject();



            String action =
                    rule.has("action")
                            ? rule.get("action")
                            .getAsString()
                            : "allow";



            boolean matches = true;



            // OS check
            if (rule.has("os")) {


                JsonObject os =
                        rule.getAsJsonObject("os");



                if (os.has("name")) {


                    String current =
                            System.getProperty("os.name")
                                    .toLowerCase(Locale.ROOT);



                    String required =
                            os.get("name")
                                    .getAsString();



                    if (required.equals("windows")) {

                        matches =
                                current.contains("win");

                    } else if (required.equals("linux")) {

                        matches =
                                current.contains("linux");

                    } else if (required.equals("osx")) {

                        matches =
                                current.contains("mac");
                    }
                }




                if (matches && os.has("arch")) {


                    String required =
                            os.get("arch")
                                    .getAsString();



                    String current =
                            System.getProperty("os.arch")
                                    .toLowerCase(Locale.ROOT);



                    if (required.equals("x86_64")) {

                        matches =
                                current.contains("amd64")
                                        ||
                                        current.contains("x86_64");


                    } else if (required.equals("arm64")) {


                        matches =
                                current.contains("arm64")
                                        ||
                                        current.contains("aarch64");
                    }
                }
            }




            if (matches) {

                allowed =
                        action.equals("allow");
            }
        }



        return allowed;
    }








    private static void removeDuplicateQuickPlay(
            List<String> arguments
    ) {


        int count = 0;


        for (String arg : arguments) {


            if (arg.startsWith("--quickPlay")) {
                count++;
            }
        }



        if (count <= 1) {
            return;
        }



        System.out.println(
                "Removing duplicate quick play arguments..."
        );



        List<String> cleaned =
                new ArrayList<>();


        boolean found = false;



        for (String arg : arguments) {


            if (arg.startsWith("--quickPlay")) {


                if (found) {
                    continue;
                }


                found = true;
            }


            cleaned.add(arg);
        }



        arguments.clear();

        arguments.addAll(cleaned);
    }








    private static void addLegacyArguments(
            String raw,
            List<String> output,
            ArgumentResolver resolver
    ) {


        boolean quote = false;

        StringBuilder current =
                new StringBuilder();



        for (char c : raw.toCharArray()) {


            if (c == '"') {

                quote = !quote;

                continue;
            }



            if (c == ' ' && !quote) {


                if (!current.isEmpty()) {


                    output.add(
                            resolver.resolve(
                                    current.toString()
                            )
                    );


                    current.setLength(0);
                }


            } else {


                current.append(c);
            }
        }




        if (!current.isEmpty()) {


            output.add(
                    resolver.resolve(
                            current.toString()
                    )
            );
        }
    }
}