/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.res.Resources;
import com.sonyericsson.android.camera.CameraActivity;

public class ResolutionDependence {
    public static boolean isDependOnAspect(Context context) {
        if (context instanceof CameraActivity) {
            return context.getResources().getBoolean(2131427334);
        }
        return false;
    }
}

