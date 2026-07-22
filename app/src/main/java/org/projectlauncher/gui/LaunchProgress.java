package org.projectlauncher.gui;

import javax.swing.*;

public final class LaunchProgress {

    private static JProgressBar bar;
    private static JLabel text;

    private LaunchProgress() {
    }

    public static void bind(
            JProgressBar progressBar,
            JLabel statusLabel
    ) {
        bar = progressBar;
        text = statusLabel;
    }

    public static void update(
            String message,
            int percent
    ) {

        SwingUtilities.invokeLater(() -> {

            if (text != null)
                text.setText(message);

            if (bar != null)
                bar.setValue(percent);

        });
    }
}