/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration;

public final class ParameterSelectability
extends Enum<ParameterSelectability> {
    private static final /* synthetic */ ParameterSelectability[] $VALUES;
    public static final /* enum */ ParameterSelectability FIXED;
    public static final /* enum */ ParameterSelectability FORCE_CHANGED;
    public static final /* enum */ ParameterSelectability INVALID;
    public static final /* enum */ ParameterSelectability SELECTABLE;
    private static final String TAG = "ParameterSelectability";
    public static final /* enum */ ParameterSelectability UNAVAILABLE;
    private final boolean mIsUpdatable;

    static {
        INVALID = new ParameterSelectability(false);
        FIXED = new ParameterSelectability(false);
        UNAVAILABLE = new ParameterSelectability(false);
        SELECTABLE = new ParameterSelectability(true);
        FORCE_CHANGED = new ParameterSelectability(true);
        ParameterSelectability[] arrparameterSelectability = new ParameterSelectability[]{INVALID, FIXED, UNAVAILABLE, SELECTABLE, FORCE_CHANGED};
        $VALUES = arrparameterSelectability;
    }

    private ParameterSelectability(boolean bl) {
        super(string, n);
        this.mIsUpdatable = bl;
    }

    public static ParameterSelectability getSelectability(int n) {
        switch (n) {
            default: {
                return SELECTABLE;
            }
            case 0: {
                return INVALID;
            }
            case 1: 
        }
        return FIXED;
    }

    public static ParameterSelectability valueOf(String string) {
        return (ParameterSelectability)Enum.valueOf((Class)ParameterSelectability.class, (String)string);
    }

    public static ParameterSelectability[] values() {
        return (ParameterSelectability[])$VALUES.clone();
    }

    public boolean isUpdatable() {
        return this.mIsUpdatable;
    }
}

