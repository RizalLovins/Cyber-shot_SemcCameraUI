/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.util.Log
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 */
package com.sonyericsson.android.camera;

import android.app.Application;
import android.util.Log;

/*
 * Failed to analyse overrides
 */
public class CameraApplication
extends Application {
    private static final String TAG = CameraApplication.class.getSimpleName();

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    public void onCreate() {
        super.onCreate();
    }
}

