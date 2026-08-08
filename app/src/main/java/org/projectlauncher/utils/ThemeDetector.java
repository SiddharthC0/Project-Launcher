package org.projectlauncher.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.projectlauncher.gui.LauncherData;

public final class ThemeDetector {

    private ThemeDetector() {
    }

    public static final class Theme {

        public final Color backgroundColor1;
        public final Color backgroundColor2;
        public final Color textColor1;
        public final Color textColor2;
        public final Color buttonColor;
        public final Color actualColor;

        public Theme(
                Color backgroundColor1,
                Color backgroundColor2,
                Color textColor1,
                Color textColor2,
                Color buttonColor,
                Color actualColor) {

            this.backgroundColor1 = backgroundColor1;
            this.backgroundColor2 = backgroundColor2;
            this.textColor1 = textColor1;
            this.textColor2 = textColor2;
            this.buttonColor = buttonColor;
            this.actualColor = actualColor;
        }
    }

    public static Theme getTheme() {

        LauncherData data = LauncherData.load();

        String theme = data.getTheme();

        if (theme == null)
            theme = "System";

        switch (theme) {

            case "Electric Yellow":
                return new Theme(
                        new Color(0xAA,0xFF,0x42,0),
                        new Color(0xC8,0xFF,0x70,0),
                        new Color(255,255,255,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0xFF,0xFF,0x00,5),
                        new Color(0xAA,0xFF,0x42,255));

            case "Inferno Orange":
                return new Theme(
                        new Color(0xFC,0x7D,0x32,0),
                        new Color(0xFF,0x9B,0x55,0),
                        new Color(255,255,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0xFF,0x45,0x00,5),
                        new Color(0xFC, 0x7D, 0x32, 255));

            case "Nebula":
                return new Theme(
                        new Color(0x32,0xAC,0xFC,0),
                        new Color(0x65,0xC5,0xFF,0),
                        new Color(0xFF,0xFF,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0x00,0x8C,0xFF,5),
                        new Color(0x32, 0xAC, 0xFC, 255));

            case "Neon Green":
                return new Theme(
                        new Color(0x32,0xFC,0x79,0),
                        new Color(0x66,0xFF,0x9B,0),
                        new Color(0xFF,0xFF,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0x00,0xFF,0x55,5),
                        new Color(0x32, 0xFC, 0x79, 255));

            case "Hyper Pink":
                return new Theme(
                        new Color(0xF4,0x78,0xFF,0),
                        new Color(0xFF,0x9C,0xFF,0),
                        new Color(0xFF,0xFF,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0xFF,0x00,0xAA,5),
                        new Color(0xF4, 0x78, 0xFF, 255));

            case "Crimson Red":
                return new Theme(
                        new Color(0xF7,0x36,0x36,0),
                        new Color(0xFF,0x66,0x66,0),
                        new Color(0xFF,0xFF,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0xFF,0x00,0x00,5),
                        new Color(0xF7, 0x36, 0x36, 255));

            case "UV":
                return new Theme(
                        new Color(0xA8,0x55,0xFF,0),
                        new Color(0xC0,0x84,0xFF,0),
                        new Color(0xFF,0xFF,0xFF,255),
                        new Color(0x00,0x00,0x00,255),
                        new Color(0x8A,0x00,0xFF,5),
                        new Color(0xA8, 0x55, 0xFF, 255));

            case "Dark":
                return darkTheme();

            case "Light":
                return lightTheme();

            case "System":
            default:
                return isDarkTheme()
                        ? darkTheme()
                        : lightTheme();
        }
    }

    private static Theme darkTheme() {

        return new Theme(
                new Color(0x09,0x09,0x09,0),
                new Color(0x15,0x15,0x15,60),
                new Color(0xEF,0xEF,0xEF,255),
                new Color(0xFF,0xFF,0xFF,255),
                new Color(0x0F,0x8F,0xD4,160),
                new Color(0x09, 0x09, 0x09, 255));
    }

    private static Theme lightTheme() {

        return new Theme(
                new Color(0xFF,0xFF,0xFF,0),
                new Color(0xF5,0xF5,0xF5,60),
                new Color(0x20,0x20,0x20,255),
                new Color(0x00,0x00,0x00,255),
                new Color(0x00,0x78,0xD4,160),
                new Color(0xFF, 0xFF, 0xFF, 255));
    }

    public static boolean isDarkTheme() {

        if (!System.getProperty("os.name").toLowerCase().contains("windows"))
            return false;

        try {

            Process process = Runtime.getRuntime().exec(
                    "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme");

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    if (line.startsWith("AppsUseLightTheme")) {

                        String[] parts = line.split("\\s+");

                        if (parts.length >= 3)
                            return parts[2].equalsIgnoreCase("0x0");
                    }
                }
            }

            process.waitFor();

        } catch (IOException | InterruptedException e) {

            if (e instanceof InterruptedException)
                Thread.currentThread().interrupt();
        }

        return false;
    }

    public static boolean isLightTheme() {
        return !isDarkTheme();
    }
}