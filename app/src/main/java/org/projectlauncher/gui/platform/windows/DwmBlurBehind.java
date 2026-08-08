package org.projectlauncher.gui.platform.windows;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;

import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.HRGN;

/**
 * Project Launcher
 *
 * JNA representation of Microsoft's
 * DWM_BLURBEHIND structure.
 *
 * Used with:
 *     DwmEnableBlurBehindWindow()
 */
public class DwmBlurBehind extends Structure {

    /*
     * Flags telling DWM which fields are valid.
     */
    public int dwFlags;


    /*
     * Enables or disables blur.
     */
    public BOOL fEnable;


    /*
     * Optional region handle.
     *
     * NULL means the whole window.
     */
    public HRGN hRgnBlur;


    /*
     * Enables transition animation
     * when maximizing/restoring.
     */
    public BOOL fTransitionOnMaximized;


    public DwmBlurBehind() {
        super();
    }


    public DwmBlurBehind(Pointer pointer) {
        super(pointer);
        read();
    }


    @Override
    protected java.util.List<String> getFieldOrder() {

        return java.util.List.of(
                "dwFlags",
                "fEnable",
                "hRgnBlur",
                "fTransitionOnMaximized"
        );
    }


    /**
     * Convenience creator.
     *
     * Enables blur for the entire window.
     */
    public static DwmBlurBehind createEnabled() {

        DwmBlurBehind blur =
                new DwmBlurBehind();


        /*
         * DWM_BB_ENABLE
         */
        blur.dwFlags = 0x00000001;


        blur.fEnable =
                new BOOL(true);


        /*
         * NULL region:
         * apply blur to whole window
         */
        blur.hRgnBlur = null;


        blur.fTransitionOnMaximized =
                new BOOL(false);


        blur.write();

        return blur;
    }
}