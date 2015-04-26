/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Parcelable
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import com.sonyericsson.cameracommon.status.CameraStatusValue;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public abstract class CameraStatusPublisher<T extends CameraStatusValue> {
    private static final String ACTION_CAMERA_STATUS_UPDATE = "com.sonymobile.cameracommon.action.CAMERA_STATUS_UPDATE";
    private static final String EXTRA_CAMERA_STATUS = "CAMERA_STATUS";
    private static final String PACKAGE = "com.sonymobile.cameracommon";
    private static final String TAG = CameraStatusPublisher.class.getSimpleName();
    private static volatile int sCameraCommonVersion = -1;
    private final ContentValues mContentValues;
    private final Context mContext;

    CameraStatusPublisher(Context context) {
        this.mContext = context;
        this.mContentValues = new ContentValues();
        if (sCameraCommonVersion < 0) {
            sCameraCommonVersion = CameraStatusPublisher.getCameraCommonVersion(context.getPackageManager());
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int getCameraCommonVersion(PackageManager packageManager) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.sonymobile.cameracommon", 0);
            int n = 0;
            if (packageInfo == null) return n;
        }
        catch (PackageManager.NameNotFoundException var1_3) {
            CameraLogger.e(TAG, "com.sonymobile.cameracommon package doesn't exist.");
            return 0;
        }
        return packageInfo.versionCode;
    }

    private static void publish(Context context, ContentValues contentValues) {
        Intent intent = new Intent("com.sonymobile.cameracommon.action.CAMERA_STATUS_UPDATE");
        intent.setPackage("com.sonymobile.cameracommon");
        intent.putExtra("CAMERA_STATUS", (Parcelable)contentValues);
        try {
            context.startService(intent);
            return;
        }
        catch (SecurityException var5_3) {
            return;
        }
    }

    protected int getCameraCommonVersion() {
        return sCameraCommonVersion;
    }

    protected String keyPrefix() {
        return "";
    }

    public void publish() {
        CameraStatusPublisher.publish(this.mContext, this.mContentValues);
    }

    public CameraStatusPublisher<T> put(T t) {
        if (t != null && this.getCameraCommonVersion() >= t.minRequiredVersion()) {
            t.putInto(this.mContentValues, this.keyPrefix());
        }
        return this;
    }

    public abstract CameraStatusPublisher<T> putDefaultAll();

    public static class Integrator {
        private final ContentValues mContentValues;
        private final Context mContext;

        public Integrator(Context context) {
            this.mContext = context;
            this.mContentValues = new ContentValues();
        }

        public <U extends CameraStatusValue> Integrator integrate(CameraStatusPublisher<U> cameraStatusPublisher) {
            this.mContentValues.putAll(cameraStatusPublisher.mContentValues);
            return this;
        }

        public void publish() {
            CameraStatusPublisher.publish(this.mContext, this.mContentValues);
        }
    }

}

