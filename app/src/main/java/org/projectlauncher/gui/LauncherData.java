package org.projectlauncher.gui;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class LauncherData {

    private static final String DATA_FILE = "launcher-data.json";

    private String username;
    private String status;
    private List<String> instances;
    private int instanceCount;

    private String theme;


    public LauncherData() {
        // Required by Gson
    }


    public static LauncherData load() {

        Path path = Paths.get(DATA_FILE);

        try {
            if (Files.exists(path)) {

                try (Reader reader = Files.newBufferedReader(path)) {

                    LauncherData data =
                            new Gson().fromJson(reader, LauncherData.class);

                    if (data == null) {
                        return createDefault();
                    }

                    data.fixNullValues();

                    return data;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return createDefault();
    }


    private static LauncherData createDefault() {

        LauncherData data = new LauncherData();

        data.username = "Sample";
        data.status = "OFFLINE";
        data.instances = new ArrayList<>();

        data.instances.add("Default");
        data.instances.add("Dev");
        data.instances.add("Beta");

        data.instanceCount = data.instances.size();

        data.theme = "System";

        return data;
    }


    private void fixNullValues() {

        if (username == null)
            username = "Sample";

        if (status == null)
            status = "OFFLINE";

        if (instances == null)
            instances = new ArrayList<>();

        instanceCount = instances.size();

        if (theme == null)
            theme = "System";
    }


    public void save() {

        try (Writer writer =
                     Files.newBufferedWriter(Paths.get(DATA_FILE))) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            gson.toJson(this, writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public List<String> getInstances() {
        return instances;
    }


    public void setInstances(List<String> instances) {
        this.instances = instances;
        this.instanceCount = instances.size();
    }


    public int getInstanceCount() {
        return instanceCount;
    }


    public String getTheme() {
        return theme;
    }


    public void setTheme(String theme) {
        this.theme = theme;
    }
}