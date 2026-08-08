package org.projectlauncher.utils;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;


public class FontLoader {

    public static Font loadFont(URL url, float size) {
        try (InputStream is = url.openStream()) {

            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    is
            );

            return font.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Segoe UI", Font.PLAIN, (int) size);
        }
    }
}