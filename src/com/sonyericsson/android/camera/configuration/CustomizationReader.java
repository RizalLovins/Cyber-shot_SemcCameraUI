/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.sonyericsson.android.camera.configuration;

import android.content.Context;
import android.content.res.Resources;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class CustomizationReader {
    private static final String TAG = CustomizationReader.class.getSimpleName();
    private static boolean sIsMmsSupported = false;

    private CustomizationReader() {
    }

    static boolean isMmsSupported() {
        return sIsMmsSupported;
    }

    public static boolean isMmsSupported(Context context) {
        CustomizationReader.readCustomization(context);
        return sIsMmsSupported;
    }

    static void readCustomization(Context context) {
        try {
            if (context.getResources().getBoolean(2131427333)) {
                sIsMmsSupported = false;
                return;
            }
            sIsMmsSupported = true;
            return;
        }
        catch (Resources.NotFoundException var1_1) {
            CameraLogger.e(TAG, "readCustomization failed.", (Throwable)var1_1);
            return;
        }
    }
}

