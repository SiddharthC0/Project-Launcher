package org.projectlauncher.gui;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class LauncherData {
    private static final String DATA_FILE = "launcher-data.json";

    private String username;
    private String status;
    private List<String> instances;
    private int instanceCount;

    public LauncherData() {
        load();
    }

    public void load() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE));
            LauncherData data = new Gson().fromJson(reader, LauncherData.class);
            reader.close();
            this.username = data.username;
            this.status = data.status;
            this.instances = data.instances;
            this.instanceCount = data.instanceCount;
        } catch (Exception e) {
            // If file not found or invalid, create default data
            this.username = "Siddy5303";
            this.status = "OFFLINE";
            this.instances = List.of("Default", "Dev", "Beta");
            this.instanceCount = this.instances.size();
            save(); // write default
        }
    }

    public void save() {
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- GETTERS AND SETTERS ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getInstances() { return instances; }
    public void setInstances(List<String> instances) { 
        this.instances = instances; 
        this.instanceCount = instances.size();
    }

    public int getInstanceCount() { return instanceCount; }
}