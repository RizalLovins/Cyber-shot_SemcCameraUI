/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.NetworkInfo
 *  android.os.Bundle
 *  android.os.Parcelable
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.wificontroller.negotiation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import com.sonyericsson.cameracommon.wificontroller.negotiation.WifiConnectionStatusCallback;

/*
 * Failed to analyse overrides
 */
public class WifiStateChangedBroadcastReceiver
extends BroadcastReceiver {
    private static final String TAG = WifiStateChangedBroadcastReceiver.class.getSimpleName();
    private WifiConnectionStatusCallback mCallback = null;
    private Context mContext = null;
    private boolean mIsReceiveStarted = false;

    public WifiStateChangedBroadcastReceiver(Context context, WifiConnectionStatusCallback wifiConnectionStatusCallback) {
        this.mContext = context;
        this.mCallback = wifiConnectionStatusCallback;
    }

    public void initialize() {
        this.mIsReceiveStarted = false;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mContext.registerReceiver((BroadcastReceiver)this, intentFilter);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        Bundle bundle;
        NetworkInfo networkInfo;
        if (!this.mIsReceiveStarted) return;
        String string = intent.getAction();
        if (string.equals((Object)"android.net.wifi.supplicant.STATE_CHANGE")) {
            if (!intent.hasExtra("supplicantError")) return;
            {
                this.mCallback.onConnectionFailed();
                return;
            }
        }
        if (!string.equals((Object)"android.net.wifi.STATE_CHANGE") || (bundle = intent.getExtras()) == null || (networkInfo = (NetworkInfo)bundle.getParcelable("networkInfo")) == null) return;
        if (networkInfo.isConnected()) {
            this.mCallback.onConnected();
            return;
        }
        if (networkInfo.isConnectedOrConnecting()) {
            return;
        }
        this.mCallback.onDisconnected();
    }

    public void release() {
        this.mContext.unregisterReceiver((BroadcastReceiver)this);
        this.mContext = null;
        this.mCallback = null;
    }

    public void startReceiver() {
        this.mIsReceiveStarted = true;
    }

    public void stopReceiver() {
        this.mIsReceiveStarted = false;
    }
}

