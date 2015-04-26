/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Debug
 *  android.os.Debug$MemoryInfo
 *  android.util.Log
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.lang.StackTraceElement
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.Locale
 */
package com.sonyericsson.cameracommon.utility;

import android.os.Build;
import android.os.Debug;
import android.util.Log;
import java.util.Locale;

public final class CameraLogger {
    public static final boolean DEBUG = 0;
    public static final boolean DEBUG_LOG_ALL_I = 0;
    public static final boolean DEBUG_LOG_WITH_TIME = 0;
    public static final String DEBUG_PERFORM_FILE = "camera_perform.csv";
    public static final boolean DEBUG_PERFORM_MEM = 0;
    public static final String DEBUG_PERFORM_TIME_TAG = "[PERFORMANCE]";
    private static final int LOCAL_LOG_LEVEL = 2;
    public static final int LOG_ASSERT = 7;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_ERROR = 6;
    public static final int LOG_INFO = 4;
    public static final int LOG_VERBOSE = 2;
    public static final int LOG_WARN = 5;
    public static final boolean isEngBuild;
    public static final boolean isLayoutDebug;
    public static final boolean isStorageDebug;
    public static final boolean isTimeDebug;
    public static final boolean isUserdebugBuild;
    private static String sTag;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = true;
        boolean bl2 = Build.TYPE.equals((Object)"userdebug") ? bl : false;
        isUserdebugBuild = bl2;
        if (!Build.TYPE.equals((Object)"eng")) {
            bl = false;
        }
        isEngBuild = bl;
        sTag = "SemcCameraApp";
    }

    private CameraLogger() {
    }

    public static int d(String string, String string2) {
        return 0;
    }

    public static int d(String string, String string2, Throwable throwable) {
        return 0;
    }

    public static /* varargs */ int d(String string, String string2, Object ... arrobject) {
        return 0;
    }

    public static int dForOperators(String string) {
        return Log.d((String)sTag, (String)(CameraLogger.timeForOperators() + ":CAMERA_PERFORMANCE_TAG:" + string));
    }

    public static void dumpStackTrace() {
        StackTraceElement[] arrstackTraceElement = Thread.currentThread().getStackTrace();
        Log.d((String)sTag, (String)"## dump stack trace ...");
        for (int i = 1; i < arrstackTraceElement.length; ++i) {
            Log.d((String)sTag, (String)("trace:" + arrstackTraceElement[i].getClassName() + "#" + arrstackTraceElement[i].getMethodName()));
        }
    }

    public static int e(String string, String string2) {
        return Log.e((String)sTag, (String)(CameraLogger.time() + string + ":" + string2));
    }

    public static int e(String string, String string2, Throwable throwable) {
        return Log.e((String)sTag, (String)(CameraLogger.time() + string + ":" + string2), (Throwable)throwable);
    }

    public static int errorLogForNonUserVariant(String string, String string2) {
        if (Build.TYPE.equals((Object)"userdebug") || Build.TYPE.equals((Object)"eng")) {
            return Log.e((String)sTag, (String)(CameraLogger.time() + string + ":" + string2));
        }
        return 0;
    }

    public static int errorLogForNonUserVariant(String string, String string2, Throwable throwable) {
        if (Build.TYPE.equals((Object)"userdebug") || Build.TYPE.equals((Object)"eng")) {
            return Log.e((String)sTag, (String)(CameraLogger.time() + string + ":" + string2), (Throwable)throwable);
        }
        return 0;
    }

    public static String getMemoryUsage() {
        long l = Runtime.getRuntime().totalMemory();
        long l2 = Runtime.getRuntime().freeMemory();
        long l3 = Runtime.getRuntime().maxMemory();
        long l4 = Debug.getNativeHeapAllocatedSize();
        long l5 = Debug.getNativeHeapFreeSize();
        long l6 = Debug.getNativeHeapSize();
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo((Debug.MemoryInfo)memoryInfo);
        long l7 = memoryInfo.nativePss;
        long l8 = memoryInfo.getTotalPss();
        long l9 = memoryInfo.getTotalUss();
        Locale locale = Locale.US;
        Object[] arrobject = new Object[]{l, l2, l3, l4, l5, l6, l7, l8, l9};
        return String.format((Locale)locale, (String)"%d, %d, %d, %d, %d, %d, %d, %d, %d", (Object[])arrobject);
    }

    public static int i(String string, String string2) {
        return 0;
    }

    public static int i(String string, String string2, Throwable throwable) {
        return 0;
    }

    public static void p(String string, String string2) {
        Log.e((String)sTag, (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + Thread.currentThread().getName() + ":" + string + ":" + string2 + "]"));
    }

    public static void setAppName(String string) {
        sTag = string;
    }

    public static void showOrientation(String string, String string2, int n) {
        switch (n) {
            default: {
                CameraLogger.d(string, string2 + ": " + n);
                return;
            }
            case 2: {
                CameraLogger.d(string, string2 + " LANDSCAPE");
                return;
            }
            case 1: 
        }
        CameraLogger.d(string, string2 + " PORTRAIT");
    }

    private static String time() {
        return "";
    }

    private static String timeForOperators() {
        long l = System.currentTimeMillis();
        return Long.valueOf((long)l).toString() + " ";
    }

    public static int v(String string, String string2) {
        return 0;
    }

    public static int v(String string, String string2, Throwable throwable) {
        return 0;
    }

    public static int w(String string, String string2) {
        return 0;
    }

    public static int w(String string, String string2, Throwable throwable) {
        return 0;
    }
}

