package org.projectlauncher.gui.mod;

import javax.swing.ImageIcon;

public class Mod {
    public String name;
    public ImageIcon icon;
    public String version;
    public String description;
    public String mcVersion; // Minecraft version for grouping

    public Mod(String name, ImageIcon icon) {
        this.name = name;
        this.icon = icon;
        this.version = "";
        this.description = "";
        this.mcVersion = "Unknown"; // default
    }
}