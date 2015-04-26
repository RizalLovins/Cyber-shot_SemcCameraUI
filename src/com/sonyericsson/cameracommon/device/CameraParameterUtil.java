/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.device;

import android.hardware.Camera;
import java.util.Iterator;
import java.util.List;

public class CameraParameterUtil {
    private static final String TAG = CameraParameterUtil.class.getSimpleName();

    public static int getPreviewMasFps(Camera.Parameters parameters) {
        int n = 0;
        if (parameters != null) {
            List list = parameters.getSupportedPreviewFpsRange();
            n = 0;
            if (list != null) {
                Iterator iterator = list.iterator();
                while (iterator.hasNext()) {
                    int n2 = ((int[])iterator.next())[1] / 1000;
                    if (n >= n2) continue;
                    n = n2;
                }
            }
        }
        return n;
    }
}

