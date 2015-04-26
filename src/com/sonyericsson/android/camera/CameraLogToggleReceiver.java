/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.String
 */
package com.sonyericsson.android.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sonyericsson.android.camera.configuration.Configurations;

/*
 * Failed to analyse overrides
 */
public class CameraLogToggleReceiver
extends BroadcastReceiver {
    private static final String TAG = "CameraLogToggleReceiver";

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        boolean bl = !Configurations.isLogForOperatorsEnabled();
        Configurations.setLogForOperators(bl);
    }
}

