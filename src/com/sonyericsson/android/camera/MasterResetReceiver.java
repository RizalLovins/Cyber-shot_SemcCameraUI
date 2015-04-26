/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.String
 */
package com.sonyericsson.android.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;

/*
 * Failed to analyse overrides
 */
public class MasterResetReceiver
extends BroadcastReceiver {
    private static final String TAG = "MasterResetReceiver";

    private static void resetSharedPrefs(String string, Context context) {
        context.getSharedPreferences(string, 0).edit().clear().commit();
    }

    public void onReceive(Context context, Intent intent) {
        MasterResetReceiver.resetSharedPrefs("com.sonyericsson.android.camera.shared_preferences", context);
        HardwareCapability hardwareCapability = HardwareCapability.getInstance();
        for (int i = 0; i < HardwareCapability.getNumberOfCameras(); ++i) {
            MasterResetReceiver.resetSharedPrefs(hardwareCapability.getFileName(i), context);
        }
    }
}

