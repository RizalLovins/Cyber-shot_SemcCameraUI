/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.cameracommon.activity.BaseActivity;

public final class ZoomDirection
extends Enum<ZoomDirection> {
    private static final /* synthetic */ ZoomDirection[] $VALUES;
    public static final /* enum */ ZoomDirection IN = new ZoomDirection();
    public static final /* enum */ ZoomDirection IN_OUT;
    public static final /* enum */ ZoomDirection NONE;
    public static final /* enum */ ZoomDirection OUT;
    private float mScaleLength;

    static {
        OUT = new ZoomDirection();
        IN_OUT = new ZoomDirection();
        NONE = new ZoomDirection();
        ZoomDirection[] arrzoomDirection = new ZoomDirection[]{IN, OUT, IN_OUT, NONE};
        $VALUES = arrzoomDirection;
    }

    private ZoomDirection() {
        super(string, n);
    }

    public static ZoomDirection get(float f, float f2) {
        if (f < f2) {
            return IN;
        }
        if (f > f2) {
            return OUT;
        }
        return NONE;
    }

    public static ZoomDirection get(int n, ExtendedActivity extendedActivity) {
        if (extendedActivity.getLastDetectedOrientation() != BaseActivity.LayoutOrientation.Portrait) {
            // empty if block
        }
        if (n == 24) {
            return IN;
        }
        if (n == 25) {
            return OUT;
        }
        return NONE;
    }

    public static ZoomDirection valueOf(String string) {
        return (ZoomDirection)Enum.valueOf((Class)ZoomDirection.class, (String)string);
    }

    public static ZoomDirection[] values() {
        return (ZoomDirection[])$VALUES.clone();
    }

    public float getScaleLength() {
        return this.mScaleLength;
    }

    public void setScaleLength(float f) {
        this.mScaleLength = f;
    }
}

