/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.wificontroller.negotiation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Failed to analyse overrides
 */
public class ConnectivityChangedBroadcastReceiver
extends BroadcastReceiver {
    private static final String TAG = ConnectivityChangedBroadcastReceiver.class.getSimpleName();
    private final ConnectivityChangedCallback mCallback;
    private final Context mContext;
    private boolean mIsReleased = false;
    private String mTargetSsid = "";

    public ConnectivityChangedBroadcastReceiver(Context context, ConnectivityChangedCallback connectivityChangedCallback) {
        this.mContext = context;
        this.mCallback = connectivityChangedCallback;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isSameSsid(String string, String string2) {
        if (string == null && string2 == null) {
            return true;
        }
        if (string == null) return false;
        if (string2 == null) {
            return false;
        }
        if (("\"" + string + "\"").equals((Object)string2)) return true;
        if (string.equals((Object)("\"" + string2 + "\""))) return true;
        return string.equals((Object)string2);
    }

    public void initialize() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver((BroadcastReceiver)this, intentFilter);
        this.mTargetSsid = "";
        this.mIsReleased = false;
    }

    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        boolean bl = false;
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            bl = false;
            if (networkInfo != null) {
                boolean bl2 = ConnectivityChangedBroadcastReceiver.isSameSsid(networkInfo.getExtraInfo(), this.mTargetSsid);
                bl = false;
                if (bl2) {
                    bl = networkInfo.isAvailable();
                }
            }
            if (!bl) {
                for (NetworkInfo networkInfo2 : connectivityManager.getAllNetworkInfo()) {
                    if (!ConnectivityChangedBroadcastReceiver.isSameSsid(networkInfo2.getExtraInfo(), this.mTargetSsid)) continue;
                    bl = networkInfo2.isAvailable();
                }
            }
        }
        if (!this.mIsReleased) {
            this.mCallback.onConnectivityChanged(bl);
        }
    }

    public void release() {
        this.mContext.unregisterReceiver((BroadcastReceiver)this);
        this.mIsReleased = true;
        this.mTargetSsid = "";
    }

    public void setTargetSsid(String string) {
        this.mTargetSsid = string;
    }

    public static interface ConnectivityChangedCallback {
        public void onConnectivityChanged(boolean var1);
    }

}

