/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera;

public final class OneShotType
extends Enum<OneShotType> {
    private static final /* synthetic */ OneShotType[] $VALUES;
    public static final /* enum */ OneShotType NONE = new OneShotType();
    public static final /* enum */ OneShotType PHOTO = new OneShotType();
    public static final /* enum */ OneShotType VIDEO = new OneShotType();

    static {
        OneShotType[] arroneShotType = new OneShotType[]{NONE, PHOTO, VIDEO};
        $VALUES = arroneShotType;
    }

    private OneShotType() {
        super(string, n);
    }

    public static OneShotType valueOf(String string) {
        return (OneShotType)Enum.valueOf((Class)OneShotType.class, (String)string);
    }

    public static OneShotType[] values() {
        return (OneShotType[])$VALUES.clone();
    }
}

