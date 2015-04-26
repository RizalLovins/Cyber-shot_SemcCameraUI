/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.focusview;

public final class RectangleColor
extends Enum<RectangleColor> {
    private static final /* synthetic */ RectangleColor[] $VALUES;
    public static final /* enum */ RectangleColor AF_FAIL;
    public static final /* enum */ RectangleColor AF_SUCCESS;
    public static final /* enum */ RectangleColor NORMAL;
    public static final /* enum */ RectangleColor RECORDING;

    static {
        NORMAL = new RectangleColor();
        AF_SUCCESS = new RectangleColor();
        AF_FAIL = new RectangleColor();
        RECORDING = new RectangleColor();
        RectangleColor[] arrrectangleColor = new RectangleColor[]{NORMAL, AF_SUCCESS, AF_FAIL, RECORDING};
        $VALUES = arrrectangleColor;
    }

    private RectangleColor() {
        super(string, n);
    }

    public static RectangleColor valueOf(String string) {
        return (RectangleColor)Enum.valueOf((Class)RectangleColor.class, (String)string);
    }

    public static RectangleColor[] values() {
        return (RectangleColor[])$VALUES.clone();
    }
}

