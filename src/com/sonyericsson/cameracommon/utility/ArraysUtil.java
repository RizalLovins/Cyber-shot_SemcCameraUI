/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

public class ArraysUtil {
    private static final String TAG = ArraysUtil.class.getSimpleName();

    public static void swap(float[] arrf, int n, int n2) {
        float f = arrf[n2];
        arrf[n2] = arrf[n];
        arrf[n] = f;
    }
}

