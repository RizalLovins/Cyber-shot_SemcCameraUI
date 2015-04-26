/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Class
 *  java.lang.String
 */
package com.sonyericsson.android.camera.smartcover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sonyericsson.android.camera.smartcover.SmartCoverActivity;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/*
 * Failed to analyse overrides
 */
public class SmartCoverReceiver
extends BroadcastReceiver {
    private static final String TAG = SmartCoverReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        if (this.isOrderedBroadcast()) {
            this.abortBroadcast();
        }
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.setClass(context, (Class)SmartCoverActivity.class);
        if (!CommonUtility.isActivityAvailable(context, intent2)) {
            CameraLogger.w(TAG, "SmartCoverActivity is disabled.");
            return;
        }
        intent2.addCategory("android.intent.category.LAUNCHER");
        intent2.setFlags(268435456);
        context.startActivity(intent2);
    }
}

