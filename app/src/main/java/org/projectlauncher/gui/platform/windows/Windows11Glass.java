package org.projectlauncher.gui.platform;

import javax.swing.JFrame;
import java.awt.Color;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.WinDef.HWND;

import org.projectlauncher.gui.platform.windows.DwmApi;

/**
 * Project Launcher
 *
 * Windows 11 Mica / Acrylic implementation.
 *
 * Uses:
 *
 *     DwmSetWindowAttribute()
 *
 * Windows 11 introduced system backdrops
 * through DWM attributes.
 */
public final class Windows11Glass {


    private Windows11Glass() {
    }


    /**
     * Applies Windows 11 system backdrop.
     */
    public static void apply(JFrame frame) {


        if (frame == null)
            return;


        try {


            HWND hwnd =
                    WindowHandleUtil.getHWND(frame);

            System.out.println("HWND = " + hwnd);


            if (hwnd == null) {

                FallbackGlass.apply(frame);
                return;

            }



            /*
             * DWMWA_SYSTEMBACKDROP_TYPE
             *
             * Windows 11 22H2+
             */
            int DWMWA_SYSTEMBACKDROP_TYPE = 38;



            /*
             * Backdrop values:
             *
             * 0 = Auto
             * 1 = None
             * 2 = Mica
             * 3 = Acrylic
             * 4 = Tabbed
             */
            int acrylic = 3;



            Memory value =
                    new Memory(4);


            value.setInt(
                    0,
                    acrylic
            );



            int result =
                    DwmApi.INSTANCE
                            .DwmSetWindowAttribute(
                                    hwnd,
                                    DWMWA_SYSTEMBACKDROP_TYPE,
                                    value,
                                    4
                            )
                            .intValue();
            System.out.println("DWM result: " + result);


            if (result < 0) {

                FallbackGlass.apply(frame);
                return;

            }



            /*
             * Required for proper
             * transparent composition.
             */
            frame.setBackground(
                    new Color(
                            0,
                            0,
                            0,
                            0
                    )
            );


        }
        catch (Throwable error) {


            FallbackGlass.apply(frame);

        }

    }

}