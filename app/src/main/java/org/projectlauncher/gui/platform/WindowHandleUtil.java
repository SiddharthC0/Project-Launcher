package org.projectlauncher.gui.platform;

import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Project Launcher
 *
 * Utility for retrieving the native HWND
 * belonging to a Swing JFrame.
 */
public final class WindowHandleUtil {

    private WindowHandleUtil() {
    }

    /**
     * Returns the native HWND for this frame.
     *
     * Returns null if unavailable.
     */
    public static HWND getHWND(JFrame frame) {

        if (frame == null)
            return null;

        /*
         * Ensure the peer exists.
         *
         * Native.getComponentPointer(...)
         * requires the heavyweight peer.
         */
        if (!frame.isDisplayable()) {
            frame.addNotify();
        }

        Pointer pointer = Native.getComponentPointer(frame);

        if (pointer == null)
            return null;

        HWND hwnd = new HWND();
        hwnd.setPointer(pointer);

        return hwnd;
    }

    /**
     * Returns true if a valid HWND
     * could be obtained.
     */
    public static boolean hasHWND(JFrame frame) {
        return getHWND(frame) != null;
    }

    /**
     * Returns the raw pointer value.
     *
     * Mainly useful for debugging.
     */
    public static long getPointerValue(JFrame frame) {

        HWND hwnd = getHWND(frame);

        if (hwnd == null)
            return 0L;

        return Pointer.nativeValue(hwnd.getPointer());
    }

}