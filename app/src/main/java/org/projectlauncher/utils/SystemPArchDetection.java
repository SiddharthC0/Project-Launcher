package org.projectlauncher.utils;

import java.util.Locale;

/**
 * Project Launcher
 *
 * Detects:
 *  - Operating System
 *  - Windows Version
 *  - macOS Generation
 *  - CPU Architecture
 *
 * No native code required.
 */
public final class SystemPArchDetection {

    private SystemPArchDetection() {
    }

    /* ------------------------------------------------------------
     * ENUMS
     * ------------------------------------------------------------ */

    public enum OperatingSystem {
        WINDOWS,
        MACOS,
        LINUX,
        UNKNOWN
    }

    public enum WindowsVersion {
        WINDOWS_7,
        WINDOWS_8,
        WINDOWS_10,
        WINDOWS_11,
        UNKNOWN,
        NOT_WINDOWS
    }

    public enum MacGeneration {
        LEGACY,
        CATALINA_OR_NEWER,
        UNKNOWN,
        NOT_MAC
    }

    public enum Architecture {
        X86,
        X64,
        ARM64,
        UNKNOWN
    }

    /* ------------------------------------------------------------
     * RAW SYSTEM INFO
     * ------------------------------------------------------------ */

    private static final String OS_NAME =
            System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH);

    private static final String OS_VERSION =
            System.getProperty("os.version", "");

    private static final String OS_ARCH =
            System.getProperty("os.arch", "").toLowerCase(Locale.ENGLISH);

    /* ------------------------------------------------------------
     * OS DETECTION
     * ------------------------------------------------------------ */

    public static OperatingSystem getOperatingSystem() {

        if (OS_NAME.contains("win"))
            return OperatingSystem.WINDOWS;

        if (OS_NAME.contains("mac") || OS_NAME.contains("darwin"))
            return OperatingSystem.MACOS;

        if (OS_NAME.contains("linux"))
            return OperatingSystem.LINUX;

        return OperatingSystem.UNKNOWN;
    }

    /* ------------------------------------------------------------
     * WINDOWS DETECTION
     * ------------------------------------------------------------ */

    public static WindowsVersion getWindowsVersion() {

        if (getOperatingSystem() != OperatingSystem.WINDOWS)
            return WindowsVersion.NOT_WINDOWS;

        /*
         * Java generally reports:
         *
         * Windows 7  -> 6.1
         * Windows 8  -> 6.2
         * Windows 8.1-> 6.3
         * Windows 10 -> 10.0
         * Windows 11 -> 10.0
         *
         * Windows 10 and 11 both report 10.0.
         * We'll separate them later using native APIs.
         */

        if (OS_VERSION.startsWith("6.1"))
            return WindowsVersion.WINDOWS_7;

        if (OS_VERSION.startsWith("6.2") ||
                OS_VERSION.startsWith("6.3"))
            return WindowsVersion.WINDOWS_8;

        if (OS_VERSION.startsWith("10.")) {

            /*
             * Temporary.
             *
             * Windows 10 and 11 both expose 10.0
             * through Java.
             *
             * TransparentFrameImplementation
             * will upgrade this using Win32 APIs.
             */

            return WindowsVersion.WINDOWS_10;
        }

        return WindowsVersion.UNKNOWN;
    }

    /* ------------------------------------------------------------
     * MAC DETECTION
     * ------------------------------------------------------------ */

    public static MacGeneration getMacGeneration() {

        if (getOperatingSystem() != OperatingSystem.MACOS)
            return MacGeneration.NOT_MAC;

        /*
         * Catalina = 10.15
         * Big Sur  = 11
         * Monterey = 12
         * Ventura  = 13
         * Sonoma   = 14
         * Sequoia  = 15
         */

        try {

            String[] split = OS_VERSION.split("\\.");

            int major = Integer.parseInt(split[0]);

            if (major >= 11)
                return MacGeneration.CATALINA_OR_NEWER;

            if (major == 10) {

                int minor = Integer.parseInt(split[1]);

                if (minor >= 15)
                    return MacGeneration.CATALINA_OR_NEWER;
            }

            return MacGeneration.LEGACY;

        } catch (Exception ex) {

            return MacGeneration.UNKNOWN;
        }
    }

    /* ------------------------------------------------------------
     * CPU
     * ------------------------------------------------------------ */

    public static Architecture getArchitecture() {

        if (OS_ARCH.contains("aarch64") ||
                OS_ARCH.contains("arm64"))
            return Architecture.ARM64;

        if (OS_ARCH.contains("64"))
            return Architecture.X64;

        if (OS_ARCH.contains("86"))
            return Architecture.X86;

        return Architecture.UNKNOWN;
    }

    /* ------------------------------------------------------------
     * HELPERS
     * ------------------------------------------------------------ */

    public static boolean isWindows() {
        return getOperatingSystem() == OperatingSystem.WINDOWS;
    }

    public static boolean isMac() {
        return getOperatingSystem() == OperatingSystem.MACOS;
    }

    public static boolean isLinux() {
        return getOperatingSystem() == OperatingSystem.LINUX;
    }

    public static boolean is64Bit() {
        return getArchitecture() == Architecture.X64
                || getArchitecture() == Architecture.ARM64;
    }

    public static String getOSVersionString() {
        return OS_VERSION;
    }

    public static String getOSNameString() {
        return OS_NAME;
    }

    public static String getArchitectureString() {
        return OS_ARCH;
    }
}