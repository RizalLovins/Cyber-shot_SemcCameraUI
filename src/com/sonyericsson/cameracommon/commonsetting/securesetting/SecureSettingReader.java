/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.provider.Settings
 *  android.provider.Settings$Secure
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.securesetting;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class SecureSettingReader {
    private static final String TAG = SecureSettingReader.class.getSimpleName();

    public static String load(Context context) {
        try {
            String string = Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"com.sonymobile.camera.quick_launch");
            if (string != null) {
                return string;
            }
        }
        catch (SecurityException var1_2) {
            CameraLogger.w(TAG, "[SecurityException] Can not access to Settings.Secure provider");
        }
        return null;
    }
}

