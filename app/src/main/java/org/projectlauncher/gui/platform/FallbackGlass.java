package org.projectlauncher.gui.platform;

import javax.swing.JFrame;
import java.awt.Color;

/**
 * Project Launcher
 *
 * Generic fallback implementation.
 *
 * Used whenever native transparency or blur
 * is unavailable or fails to initialize.
 */
public final class FallbackGlass {

    /**
     * Default background alpha.
     *
     * Increase for a darker launcher.
     * Decrease for a more transparent launcher.
     */
    private static final int DEFAULT_ALPHA = 90;

    private FallbackGlass() {
    }

    /**
     * Applies a simple transparent window.
     *
     * No native APIs are used.
     */
    public static void apply(JFrame frame) {

        if (frame == null)
            return;

        /*
         * Make the JFrame itself transparent.
         */
        frame.setBackground(new Color(0, 0, 0, 0));

        /*
         * Give the content pane a translucent
         * dark background.
         */
        frame.getContentPane().setBackground(
                new Color(
                        10,
                        10,
                        10,
                        DEFAULT_ALPHA
                )
        );
    }

    /**
     * Allows custom opacity.
     *
     * Alpha:
     * 0   = Invisible
     * 255 = Opaque
     */
    public static void apply(JFrame frame, int alpha) {

        if (frame == null)
            return;

        alpha = Math.max(0, Math.min(255, alpha));

        frame.setBackground(new Color(0, 0, 0, 0));

        frame.getContentPane().setBackground(
                new Color(
                        10,
                        10,
                        10,
                        alpha
                )
        );
    }

}