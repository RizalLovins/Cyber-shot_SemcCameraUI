/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.wifi.SupplicantState
 *  android.net.wifi.WifiConfiguration
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.BitSet
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.wificontroller.negotiation;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.wificontroller.negotiation.ConnectivityChangedBroadcastReceiver;
import com.sonyericsson.cameracommon.wificontroller.negotiation.WifiConnectionRequestCallback;
import com.sonyericsson.cameracommon.wificontroller.negotiation.WifiConnectionStatusCallback;
import com.sonyericsson.cameracommon.wificontroller.negotiation.WifiStateChangedBroadcastReceiver;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public class WifiNetworkEnvironment {
    private static final int CONNECTING_RETRY_COUNT = 60;
    private static final int CONNECTING_RETRY_INTERVAL = 1000;
    private static final int CONNECTING_TIMEOUT = 60000;
    private static final String TAG = WifiNetworkEnvironment.class.getSimpleName();
    private Handler mBackHandler = null;
    private HandlerThread mBackThread = null;
    private WifiStateChangedBroadcastReceiver mBroadcastReceiver = null;
    private WifiConnectionRequestCallback mCallback = null;
    private ConnectRetryTask mConnectRetryTask = null;
    private ConnectivityChangedBroadcastReceiver mConnectivityBroadcastReceiver = null;
    private boolean mIsConnectedInfoByConnectivityManager = false;
    private boolean mIsWifiDeviceEnabled = false;
    private int mRetryCount = 0;
    private WifiManager mWifiManager = null;

    static /* synthetic */ int access$200(WifiNetworkEnvironment wifiNetworkEnvironment) {
        return wifiNetworkEnvironment.mRetryCount;
    }

    static /* synthetic */ int access$208(WifiNetworkEnvironment wifiNetworkEnvironment) {
        int n = wifiNetworkEnvironment.mRetryCount;
        wifiNetworkEnvironment.mRetryCount = n + 1;
        return n;
    }

    static /* synthetic */ ConnectionState access$600(WifiNetworkEnvironment wifiNetworkEnvironment, String string) {
        return wifiNetworkEnvironment.checkState(string);
    }

    static /* synthetic */ boolean access$700(WifiNetworkEnvironment wifiNetworkEnvironment) {
        return wifiNetworkEnvironment.mIsConnectedInfoByConnectivityManager;
    }

    static /* synthetic */ WifiStateChangedBroadcastReceiver access$800(WifiNetworkEnvironment wifiNetworkEnvironment) {
        return wifiNetworkEnvironment.mBroadcastReceiver;
    }

    static /* synthetic */ void access$900(WifiNetworkEnvironment wifiNetworkEnvironment, String string, String string2) {
        wifiNetworkEnvironment.checkAndScanNetwork(string, string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkAndScanNetwork(String string, String string2) {
        String string3 = super.getCodedString(string);
        String string4 = super.getCodedString(string2);
        List list = this.mWifiManager.getConfiguredNetworks();
        if (list != null) {
            for (WifiConfiguration wifiConfiguration : list) {
                if (!wifiConfiguration.SSID.equals((Object)string3)) continue;
                super.connect(wifiConfiguration.networkId);
                return;
            }
        } else {
            this.mWifiManager.startScan();
            List list2 = this.mWifiManager.getScanResults();
            if (list2 == null) return;
            {
                Iterator iterator = list2.iterator();
                while (iterator.hasNext()) {
                    if (!((android.net.wifi.ScanResult)iterator.next()).SSID.equals((Object)string)) continue;
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = string3;
                    wifiConfiguration.allowedKeyManagement.set(1);
                    wifiConfiguration.preSharedKey = string4;
                    int n = this.mWifiManager.addNetwork(wifiConfiguration);
                    if (n < 0) return;
                    super.connect(n);
                    return;
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private ConnectionState checkState(String string) {
        String string2 = super.getCodedString(string);
        WifiInfo wifiInfo = this.mWifiManager.getConnectionInfo();
        if (wifiInfo == null || !wifiInfo.getSSID().equals((Object)string2)) return ConnectionState.DISCONNECTED;
        switch (.$SwitchMap$android$net$wifi$SupplicantState[wifiInfo.getSupplicantState().ordinal()]) {
            default: {
                return ConnectionState.DISCONNECTED;
            }
            case 1: {
                return ConnectionState.CONNECTED;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        return ConnectionState.CONNECTING;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void connect(int n) {
        if (!this.mWifiManager.enableNetwork(n, true)) {
            CameraLogger.w(TAG, "Failed enable network.");
            return;
        } else {
            if (!this.mWifiManager.saveConfiguration()) {
                CameraLogger.w(TAG, "Failed save configuration.");
                return;
            }
            if (this.mWifiManager.reconnect()) return;
            {
                CameraLogger.w(TAG, "Failed save configuration.");
                return;
            }
        }
    }

    private void disable() {
        this.mWifiManager.setWifiEnabled(false);
    }

    private String getCodedString(String string) {
        return "\"" + string + "\"";
    }

    public void cancelConnect() {
        this.mBroadcastReceiver.stopReceiver();
        this.mBackHandler.removeCallbacks((Runnable)this.mConnectRetryTask);
        this.mConnectRetryTask = null;
        if (!this.mWifiManager.disconnect()) {
            CameraLogger.w(TAG, "Failed cancel connection.");
        }
    }

    public boolean enable() {
        if (this.mWifiManager.isWifiApEnabled()) {
            return false;
        }
        return this.mWifiManager.setWifiEnabled(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initialize(Context context) {
        this.mBackThread = new HandlerThread("WifiRemoteBackWorker");
        this.mBackThread.start();
        this.mBackHandler = new Handler(this.mBackThread.getLooper());
        this.mWifiManager = (WifiManager)context.getSystemService("wifi");
        if (!this.mWifiManager.isWifiEnabled()) {
            this.enable();
            this.mIsWifiDeviceEnabled = false;
        } else {
            this.mIsWifiDeviceEnabled = true;
        }
        this.mBroadcastReceiver = new WifiStateChangedBroadcastReceiver(context, new WifiConnectionStatusCallbackImpl((WifiNetworkEnvironment)this, null));
        this.mBroadcastReceiver.initialize();
        this.mConnectivityBroadcastReceiver = new ConnectivityChangedBroadcastReceiver(context, new ConnectivityChangedCallbackImpl((WifiNetworkEnvironment)this, null));
        this.mConnectivityBroadcastReceiver.initialize();
    }

    public void isWifiUnavailable() {
        this.mWifiManager.isWifiApEnabled();
    }

    public void release() {
        if (this.mConnectRetryTask != null) {
            this.mBackHandler.removeCallbacks((Runnable)this.mConnectRetryTask);
            this.mConnectRetryTask = null;
        }
        if (!this.mIsWifiDeviceEnabled) {
            this.disable();
        }
        if (this.mBroadcastReceiver != null) {
            this.mBroadcastReceiver.release();
            this.mBroadcastReceiver = null;
        }
        if (this.mConnectivityBroadcastReceiver != null) {
            this.mConnectivityBroadcastReceiver.release();
            this.mConnectivityBroadcastReceiver = null;
            this.mIsConnectedInfoByConnectivityManager = false;
        }
        this.mBackThread.quitSafely();
        this.mBackThread = null;
        this.mBackHandler = null;
    }

    public void requestConnect(String string, String string2, WifiConnectionRequestCallback wifiConnectionRequestCallback) {
        this.mCallback = wifiConnectionRequestCallback;
        this.mBroadcastReceiver.stopReceiver();
        this.mBackHandler.removeCallbacks((Runnable)this.mConnectRetryTask);
        ConnectionState connectionState = super.checkState(string);
        ConnectionState connectionState2 = ConnectionState.CONNECTED;
        boolean bl = false;
        if (connectionState != connectionState2) {
            bl = true;
        }
        this.mConnectivityBroadcastReceiver.setTargetSsid(string);
        this.mConnectRetryTask = new ConnectRetryTask(string, string2, bl);
        this.mRetryCount = 0;
        this.mBackHandler.post((Runnable)this.mConnectRetryTask);
    }

    /*
     * Failed to analyse overrides
     */
    private class ConnectRetryTask
    implements Runnable {
        private final String mPasswd;
        private final boolean mShouldWaitConnectivityManagerNotify;
        private final String mSsid;

        public ConnectRetryTask(String string, String string2, boolean bl) {
            this.mSsid = string;
            this.mPasswd = string2;
            this.mShouldWaitConnectivityManagerNotify = bl;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            if (60 < WifiNetworkEnvironment.access$200(WifiNetworkEnvironment.this)) {
                WifiNetworkEnvironment.access$400(WifiNetworkEnvironment.this).removeCallbacks((Runnable)WifiNetworkEnvironment.access$300(WifiNetworkEnvironment.this));
                WifiNetworkEnvironment.access$500(WifiNetworkEnvironment.this).onConnectionFailed();
                return;
            }
            var1_1 = WifiNetworkEnvironment.access$600(WifiNetworkEnvironment.this, this.mSsid);
            switch (.$SwitchMap$com$sonyericsson$cameracommon$wificontroller$negotiation$WifiNetworkEnvironment$ConnectionState[var1_1.ordinal()]) {
                case 1: {
                    if (!this.mShouldWaitConnectivityManagerNotify || WifiNetworkEnvironment.access$700(WifiNetworkEnvironment.this)) {
                        WifiNetworkEnvironment.access$400(WifiNetworkEnvironment.this).removeCallbacks((Runnable)WifiNetworkEnvironment.access$300(WifiNetworkEnvironment.this));
                        WifiNetworkEnvironment.access$800(WifiNetworkEnvironment.this).startReceiver();
                        WifiNetworkEnvironment.access$500(WifiNetworkEnvironment.this).onConnected();
                        return;
                    }
                }
                default: {
                    ** GOTO lbl17
                }
                case 3: 
            }
            WifiNetworkEnvironment.access$900(WifiNetworkEnvironment.this, this.mSsid, this.mPasswd);
lbl17: // 2 sources:
            WifiNetworkEnvironment.access$208(WifiNetworkEnvironment.this);
            WifiNetworkEnvironment.access$400(WifiNetworkEnvironment.this).postDelayed((Runnable)WifiNetworkEnvironment.access$300(WifiNetworkEnvironment.this), 1000);
        }
    }

    private static final class ConnectionState
    extends Enum<ConnectionState> {
        private static final /* synthetic */ ConnectionState[] $VALUES;
        public static final /* enum */ ConnectionState CONNECTED = new ConnectionState();
        public static final /* enum */ ConnectionState CONNECTING = new ConnectionState();
        public static final /* enum */ ConnectionState DISCONNECTED = new ConnectionState();

        static {
            ConnectionState[] arrconnectionState = new ConnectionState[]{CONNECTED, CONNECTING, DISCONNECTED};
            $VALUES = arrconnectionState;
        }

        private ConnectionState() {
            super(string, n);
        }

        public static ConnectionState valueOf(String string) {
            return (ConnectionState)Enum.valueOf((Class)ConnectionState.class, (String)string);
        }

        public static ConnectionState[] values() {
            return (ConnectionState[])$VALUES.clone();
        }
    }

    private class ConnectivityChangedCallbackImpl
    implements ConnectivityChangedBroadcastReceiver.ConnectivityChangedCallback {
        final /* synthetic */ WifiNetworkEnvironment this$0;

        private ConnectivityChangedCallbackImpl(WifiNetworkEnvironment wifiNetworkEnvironment) {
            this.this$0 = wifiNetworkEnvironment;
        }

        /* synthetic */ ConnectivityChangedCallbackImpl(WifiNetworkEnvironment wifiNetworkEnvironment,  var2_2) {
            super(wifiNetworkEnvironment);
        }

        @Override
        public void onConnectivityChanged(boolean bl) {
            this.this$0.mIsConnectedInfoByConnectivityManager = bl;
        }
    }

    private class WifiConnectionStatusCallbackImpl
    implements WifiConnectionStatusCallback {
        final /* synthetic */ WifiNetworkEnvironment this$0;

        private WifiConnectionStatusCallbackImpl(WifiNetworkEnvironment wifiNetworkEnvironment) {
            this.this$0 = wifiNetworkEnvironment;
        }

        /* synthetic */ WifiConnectionStatusCallbackImpl(WifiNetworkEnvironment wifiNetworkEnvironment,  var2_2) {
            super(wifiNetworkEnvironment);
        }

        @Override
        public void onConnected() {
            if (this.this$0.mCallback != null) {
                this.this$0.mBackHandler.removeCallbacks((Runnable)this.this$0.mConnectRetryTask);
                this.this$0.mCallback.onConnected();
            }
        }

        @Override
        public void onConnectionFailed() {
            if (this.this$0.mCallback != null) {
                this.this$0.mBackHandler.removeCallbacks((Runnable)this.this$0.mConnectRetryTask);
                this.this$0.mCallback.onConnectionFailed();
            }
        }

        @Override
        public void onDisconnected() {
            if (this.this$0.mCallback != null) {
                this.this$0.mCallback.onDisconnected();
            }
        }
    }

}

