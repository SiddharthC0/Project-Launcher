package org.projectlauncher.gui;

import javax.swing.JFrame;
import java.awt.Color;

import org.projectlauncher.utils.SystemPArchDetection;
import org.projectlauncher.utils.SystemPArchDetection.OperatingSystem;
import org.projectlauncher.utils.SystemPArchDetection.WindowsVersion;
import org.projectlauncher.utils.SystemPArchDetection.MacGeneration;

import org.projectlauncher.gui.platform.Windows7Glass;
import org.projectlauncher.gui.platform.Windows10Glass;
import org.projectlauncher.gui.platform.Windows11Glass;
import org.projectlauncher.gui.platform.FallbackGlass;

/**
 * Project Launcher
 *
 * Main transparency entry point.
 *
 * Usage:
 *
 * JFrame frame = new JFrame();
 * frame.setUndecorated(true);
 *
 * TransparentFrameImplementation.makeTransparent(frame);
 */
public final class TransparentFrameImplementation {

    private TransparentFrameImplementation() {
    }

    /**
     * Applies the best transparency implementation
     * available for the current platform.
     */
    public static void makeTransparent(JFrame frame) {

        if (frame == null)
            return;

        /*
         * Swing requirement.
         *
         * A transparent background allows the
         * native compositor    to show through.
         */
        frame.setBackground(new Color(1, 1, 1, 1));

        OperatingSystem os =
                SystemPArchDetection.getOperatingSystem();

        switch (os) {

            case WINDOWS -> applyWindows(frame);

            case MACOS -> applyMac(frame);

            default -> FallbackGlass.apply(frame);
        }
    }

    private static void applyWindows(JFrame frame) {

        WindowsVersion version =
                SystemPArchDetection.getWindowsVersion();

        switch (version) {

            case WINDOWS_7 ->

                    Windows7Glass.apply(frame);

            /*
             * Java reports Windows 10 and 11
             * both as version 10.0.
             *
             * Windows10Glass will internally
             * determine whether Mica is available.
             */
            case WINDOWS_10 ->

                    Windows10Glass.apply(frame);

            case WINDOWS_11 ->

                    Windows11Glass.apply(frame);

            default ->

                    FallbackGlass.apply(frame);
        }
    }

    private static void applyMac(JFrame frame) {

        MacGeneration version =
                SystemPArchDetection.getMacGeneration();

        switch (version) {

            default ->

                    FallbackGlass.apply(frame);
        }
    }
}