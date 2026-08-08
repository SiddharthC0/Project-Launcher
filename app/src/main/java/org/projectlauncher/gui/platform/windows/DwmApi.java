package org.projectlauncher.gui.platform.windows;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinNT.HRESULT;

/**
 * Project Launcher
 *
 * JNA mapping for Microsoft's Desktop Window Manager API.
 *
 * Native library:
 *     dwmapi.dll
 *
 * Used by:
 *     Windows 7 Glass
 *     Windows 11 Mica/Acrylic handling
 */
public interface DwmApi extends Library {

    DwmApi INSTANCE = Native.load(
            "dwmapi",
            DwmApi.class
    );


    /**
     * Checks whether DWM composition is enabled.
     *
     * Windows Vista+
     *
     * @param enabled pointer receiving BOOL result
     * @return HRESULT
     */
    HRESULT DwmIsCompositionEnabled(
            BOOL enabled
    );


    /**
     * Enables blur behind a window.
     *
     * Windows Vista / Windows 7 Aero Glass.
     *
     * @param hwnd target window
     * @param blurBehind structure pointer
     * @return HRESULT
     */
    HRESULT DwmEnableBlurBehindWindow(
            HWND hwnd,
            Pointer blurBehind
    );


    /**
     * Changes DWM window attributes.
     *
     * Windows 11 uses this for
     * system backdrop features.
     *
     * @param hwnd target window
     * @param attribute attribute id
     * @param value data pointer
     * @param size data size
     * @return HRESULT
     */
    HRESULT DwmSetWindowAttribute(
            HWND hwnd,
            int attribute,
            Pointer value,
            int size
    );

}