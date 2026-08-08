package org.projectlauncher.gui.platform.windows;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Project Launcher
 *
 * JNA mapping for:
 *
 * WINDOWCOMPOSITIONATTRIBDATA
 *
 * Used by:
 *
 * SetWindowCompositionAttribute()
 */
public class WindowCompositionAttributeData extends Structure {


    /**
     * Attribute identifier.
     *
     * For Acrylic:
     * WCA_ACCENT_POLICY = 19
     */
    public int Attribute;


    /**
     * Pointer to AccentPolicy.
     */
    public Pointer Data;


    /**
     * Size of AccentPolicy structure.
     */
    public int Size;



    public WindowCompositionAttributeData() {
        super();
    }



    public WindowCompositionAttributeData(Pointer pointer) {

        super(pointer);
        read();

    }



    @Override
    protected java.util.List<String> getFieldOrder() {

        return java.util.List.of(
                "Attribute",
                "Data",
                "Size"
        );

    }

}