/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$MemoryInfo
 *  android.content.Context
 *  android.os.Debug
 *  android.os.Debug$MemoryInfo
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.android.camera.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import java.util.List;

public class MemoryAnalyzer {
    private static final String TAG = "MemoryAnalyzer";

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static void logMemoryInfo(Context var0_1, String var1, String var2_2) {
        var8_3 = MemoryAnalyzer.class;
        // MONITORENTER : com.sonyericsson.android.camera.util.MemoryAnalyzer.class
        var4_4 = (ActivityManager)var0_1.getSystemService("activity");
        var4_4.getMemoryInfo(new ActivityManager.MemoryInfo());
        var5_5 = var4_4.getRunningAppProcesses();
        ** if (var5_5 == null) goto lbl14
lbl7: // 1 sources:
        var6_6 = 0;
        do {
            if (var6_6 >= var5_5.size() || ((android.app.ActivityManager$RunningAppProcessInfo)var5_5.get((int)var6_6)).processName.equalsIgnoreCase("com.sonyericsson.android.camera")) {
                var7_7 = var5_5.size();
                if (var6_6 != var7_7) {
                    Debug.getMemoryInfo((Debug.MemoryInfo)new Debug.MemoryInfo());
                    return;
                }
            } else {
                ** GOTO lbl16
            }
lbl14: // 3 sources:
            // MONITOREXIT : var8_3
            return;
lbl16: // 2 sources:
            ++var6_6;
        } while (true);
    }
}

