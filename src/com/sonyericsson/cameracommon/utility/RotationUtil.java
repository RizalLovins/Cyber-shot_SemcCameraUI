/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

public class RotationUtil {
    private static final String TAG = RotationUtil.class.getSimpleName();

    public static float getAngle(int n) {
        if (n == 1) {
            return -90.0f;
        }
        return 0.0f;
    }

    public static int getNormalizedRotation(int n) {
        int n2 = n % 360;
        if (45 < n2 && n2 <= 135) {
            return 90;
        }
        if (135 <= n2 && n2 <= 225) {
            return 180;
        }
        if (225 <= n2 && n2 <= 315) {
            return 270;
        }
        return 0;
    }
}

