package org.projectlauncher.gui.platform.windows;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;

/**
 * Project Launcher
 *
 * JNA representation of Windows 10
 * ACCENT_POLICY structure.
 *
 * Used with:
 *
 * SetWindowCompositionAttribute()
 */
public class AccentPolicy extends Structure {


    /*
     * Accent rendering mode.
     */
    public int AccentState;


    /*
     * Additional rendering flags.
     */
    public int AccentFlags;


    /*
     * Color + alpha value.
     *
     * Format:
     * 0xAABBGGRR
     */
    public int GradientColor;


    /*
     * Animation identifier.
     */
    public int AnimationId;



    public AccentPolicy() {
        super();
    }


    public AccentPolicy(Pointer pointer) {
        super(pointer);
        read();
    }


    @Override
    protected java.util.List<String> getFieldOrder() {

        return java.util.List.of(
                "AccentState",
                "AccentFlags",
                "GradientColor",
                "AnimationId"
        );
    }



    /**
     * Enables Acrylic style blur.
     *
     * Windows 10:
     * ACCENT_ENABLE_ACRYLICBLURBEHIND = 4
     *
     * @param alpha opacity (0-255)
     * @param rgb color value
     */
    public static AccentPolicy createAcrylic(
            int alpha,
            int rgb
    ) {

        AccentPolicy policy =
                new AccentPolicy();


        /*
         * ACCENT_ENABLE_ACRYLICBLURBEHIND
         */
        policy.AccentState = 4;


        policy.AccentFlags = 0;


        /*
         * Windows expects:
         *
         * AABBGGRR
         *
         */
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;


        policy.GradientColor =
                ((alpha & 0xFF) << 24)
                        |
                        (b << 16)
                        |
                        (g << 8)
                        |
                        r;


        policy.AnimationId = 0;


        policy.write();

        return policy;
    }
}