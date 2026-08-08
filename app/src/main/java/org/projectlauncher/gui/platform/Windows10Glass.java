package org.projectlauncher.gui.platform;

import javax.swing.JFrame;
import java.awt.Color;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;

import org.projectlauncher.gui.platform.windows.AccentPolicy;
import org.projectlauncher.gui.platform.windows.User32Ex;
import org.projectlauncher.gui.platform.windows.WindowCompositionAttributeData;

/**
 * Project Launcher
 *
 * Windows 10 Acrylic transparency implementation.
 *
 * Uses:
 *     SetWindowCompositionAttribute()
 *
 * Note:
 * This API is not officially documented by Microsoft,
 * but is the standard method used for Acrylic-style
 * effects on Windows 10.
 */
public final class Windows10Glass {


    private Windows10Glass() {
    }


    /**
     * Applies Acrylic transparency.
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
             * Acrylic material.
             *
             * Alpha controls transparency.
             *
             * Lower alpha:
             *     more transparent
             *
             * Higher alpha:
             *     darker
             */
            AccentPolicy accent =
                    AccentPolicy.createAcrylic(
                            150,
                            0x101010
                    );


            Pointer accentPointer =
                    accent.getPointer();



            WindowCompositionAttributeData data =
                    new WindowCompositionAttributeData();


            /*
             * WCA_ACCENT_POLICY
             */
            data.Attribute = 19;


            data.Data =
                    accentPointer;


            data.Size =
                    accent.size();



            data.write();



            boolean result =
                    User32Ex.INSTANCE
                            .SetWindowCompositionAttribute(
                                    hwnd,
                                    data.getPointer()
                            );



            if (!result) {

                FallbackGlass.apply(frame);
                return;

            }



            /*
             * Make Swing background transparent.
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


            /*
             * Native graphics should never
             * prevent launcher startup.
             */
            FallbackGlass.apply(frame);

        }

    }

}