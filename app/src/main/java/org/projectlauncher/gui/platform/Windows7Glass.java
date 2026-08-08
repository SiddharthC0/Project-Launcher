package org.projectlauncher.gui.platform;

import javax.swing.JFrame;

import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HRESULT;

import org.projectlauncher.gui.platform.windows.DwmApi;
import org.projectlauncher.gui.platform.windows.DwmBlurBehind;

/**
 * Project Launcher
 *
 * Windows 7 Aero Glass implementation.
 *
 * Uses:
 *     DwmEnableBlurBehindWindow()
 *
 * Available:
 *     Windows Vista / Windows 7
 */
public final class Windows7Glass {

    private Windows7Glass() {
    }


    /**
     * Applies Windows 7 Aero transparency.
     */
    public static void apply(JFrame frame) {

        if (frame == null)
            return;


        try {

            HWND hwnd =
                    WindowHandleUtil.getHWND(frame);


            if (hwnd == null) {
                FallbackGlass.apply(frame);
                return;
            }


            /*
             * Check if Desktop Window Manager
             * composition is active.
             */
            BOOL enabled =
                    new BOOL(false);


            HRESULT composition =
                    DwmApi.INSTANCE
                            .DwmIsCompositionEnabled(enabled);


            /*
             * HRESULT:
             *
             * >= 0 = success
             * < 0  = failure
             */
            if (composition.intValue() < 0
                    || !enabled.booleanValue()) {

                FallbackGlass.apply(frame);
                return;
            }


            /*
             * Create DWM blur configuration.
             */
            DwmBlurBehind blur =
                    DwmBlurBehind.createEnabled();


            HRESULT result =
                    DwmApi.INSTANCE
                            .DwmEnableBlurBehindWindow(
                                    hwnd,
                                    blur.getPointer()
                            );


            if (result.intValue() < 0) {

                FallbackGlass.apply(frame);
                return;
            }


            /*
             * Keep Swing background transparent.
             */
            frame.setBackground(
                    new java.awt.Color(
                            0,
                            0,
                            0,
                            0
                    )
            );


        } catch (Throwable error) {

            /*
             * Native code should never
             * kill the launcher.
             */
            FallbackGlass.apply(frame);
        }
    }
}