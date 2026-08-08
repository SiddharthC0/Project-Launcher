package org.projectlauncher.gui.platform.windows;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Project Launcher
 *
 * JNA mapping for user32.dll
 *
 * Used for Windows 10 Acrylic effects.
 *
 * Note:
 * SetWindowCompositionAttribute is an undocumented
 * Windows API used for advanced window materials.
 */
public interface User32Ex extends Library {

    User32Ex INSTANCE = Native.load(
            "user32",
            User32Ex.class
    );


    /**
     * Applies composition attributes to a window.
     *
     * @param hwnd target window
     * @param data ACCENT_POLICY wrapper
     *
     * @return true on success
     */
    boolean SetWindowCompositionAttribute(
            HWND hwnd,
            Pointer data
    );

}