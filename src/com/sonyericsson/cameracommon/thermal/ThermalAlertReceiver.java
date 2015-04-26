/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.ServiceConnection
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.CheckBox
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.TextView
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.sonyericsson.cameracommon.thermal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;
import com.sonyericsson.cameracommon.utility.BrandConfig;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.psm.sysmonservice.ISysmonService;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Failed to analyse overrides
 */
public class ThermalAlertReceiver
extends BroadcastReceiver {
    private static final String ACTION_CAMERA_COOLED_DOWN_NORMAL = "com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL";
    private static final String ACTION_CAMERA_HEATED_CLOSE_TO_SHUTDOWN = "com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN";
    private static final String ACTION_CAMERA_HEATED_OVER_CRITICAL = "com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL";
    private static final String ACTION_CAMERA_HEATED_OVER_LOW_TEMP_BURN = "com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN";
    private static final String ACTION_CAMERA_LOW_TEMP_BURN_TIMER_RESET = "com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET";
    private static final String ACTION_CAMERA_LOW_TEMP_BURN_TIMER_SET = "com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET";
    private static final int CAMERA_CRITICAL = 604;
    private static final int CAMERA_HEATED_CLOSE_TO_SHUTDOWN = 620;
    private static final int CAMERA_LOW_TEMP_BURN = 610;
    private static final int CAMERA_NORMAL = 600;
    private static final int CAMERA_WARNING = 603;
    private static final int INVALID_LOW_TEMP_BURN_TIMEOUT_DURATION = -1;
    private static final String KEY_LOW_TEMP_BURN_TIMER_DURATION_SEC = "com.sonyericsson.psm.extra.TIMEOUT_SEC";
    private static final int LOW_TEMP_BURN_TIMER_LIMIT_MILLIS = 1800000;
    private static final String SHARED_PREFS_KEY_THERMAL_WARNING_DISABLED = "THERMAL_WARNING_DISABLED";
    private static final String SYSMON_SERVICE = "com.sonyericsson.psm.sysmonservice";
    private static final String SYSMON_SERVICE_ACTION = "com.sonyericsson.psm.sysmonservice.ISysmonService";
    private static final String TAG = ThermalAlertReceiver.class.getSimpleName();
    private static final int VARIABLE_LOW_TEMP_BURN_TIMEOUT_DURATION_NOT_SUPPORTED;
    private final Activity mActivity;
    private boolean mIsAlreadyHighTemperature = false;
    private boolean mIsBindSysmonService;
    private boolean mIsWarningState = false;
    private final ThermalAlertReceiverListener mListener;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerFixedDuration;
    private final LowTempBurnTimeoutTimerWrapper mLowTempBurnTimerVariableDuration;
    private final MessagePopup mMessagePopup;
    private final ParamSharedPrefWrapper mParamSharedPrefWrapper;
    private final ServiceConnection mServiceConnectionSysmon;
    private ISysmonService mSysmonService;
    private RotatableDialog mThermalWarningDialog = null;

    public ThermalAlertReceiver(Activity activity, MessagePopup messagePopup, ThermalAlertReceiverListener thermalAlertReceiverListener, ParamSharedPrefWrapper paramSharedPrefWrapper) {
        this.mActivity = activity;
        this.mMessagePopup = messagePopup;
        this.mListener = thermalAlertReceiverListener;
        this.mParamSharedPrefWrapper = paramSharedPrefWrapper;
        this.mThermalWarningDialog = null;
        this.mServiceConnectionSysmon = new ServiceConnectionSysmon();
        this.mLowTempBurnTimerFixedDuration = new LowTempBurnTimeoutTimerWrapper((ThermalAlertReceiver)this, null);
        this.mLowTempBurnTimerVariableDuration = new LowTempBurnTimeoutTimerWrapper((ThermalAlertReceiver)this, null);
    }

    private void changeToNormalState() {
        this.mIsWarningState = false;
        this.mListener.onNotifyThermalNormal();
    }

    private void changeToWarningState() {
        this.mIsWarningState = true;
        this.mListener.onNotifyThermalWarning();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkLowTempBurnTimeoutTimerDuration(int n, int n2) {
        if (n2 == 0) {
            if (n != 610) return;
            {
                this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000);
                return;
            }
        } else {
            if (n2 == -1) return;
            {
                this.mLowTempBurnTimerVariableDuration.requestTimeMillis(n2 * 1000);
                return;
            }
        }
    }

    private void checkStartupStatus(int n, String string) {
        this.mIsAlreadyHighTemperature = false;
        switch (n) {
            default: {
                return;
            }
            case 604: {
                this.mIsAlreadyHighTemperature = true;
                super.finishOnStartup();
                return;
            }
            case 603: {
                this.mIsAlreadyHighTemperature = true;
                super.finishOnStartup();
                return;
            }
            case 600: {
                super.changeToNormalState();
                return;
            }
            case 620: 
        }
        super.changeToWarningState();
    }

    private void finishOnStartup() {
        this.mListener.onReachHighTemperature(true);
        this.mMessagePopup.showRotatableToastMessageAndAbort(R.string.cam_strings_error_high_temp_already_high_txt, 1, RotatableToast.ToastPosition.CENTER);
    }

    private String getSharedPrefsKeyForThermalWarningDialog() {
        String string = this.mActivity.getClass().getSimpleName();
        if (string.equals((Object)"FastCapturingActivity") || string.equals((Object)"CameraActivityForCaptureOnlyPhoto") || string.equals((Object)"CameraActivityForCaptureOnlyVideo")) {
            string = "CameraActivity";
        }
        return "THERMAL_WARNING_DISABLED_" + string;
    }

    private int getThermalWarningString() {
        if (BrandConfig.isVerizonBrand()) {
            return R.string.cam_strings_error_high_temp_info_vzw_txt;
        }
        return R.string.cam_strings_error_high_temp_info_txt;
    }

    private void showDialogCritical() {
        this.mListener.onReachHighTemperature(false);
        this.mMessagePopup.showThermalCritical();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void showDialogWarning() {
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        if (layoutInflater == null) {
            return;
        }
        View view = layoutInflater.inflate(R.layout.thermal_warning_popup_content, null);
        ((TextView)view.findViewById(R.id.header_text)).setText(this.getThermalWarningString());
        ((CheckBox)view.findViewById(R.id.check_box)).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)new CheckBoxListener(this.mParamSharedPrefWrapper, this.getSharedPrefsKeyForThermalWarningDialog()));
        ThermalWarningDialogCloseListener thermalWarningDialogCloseListener = new ThermalWarningDialogCloseListener(this, null);
        this.mThermalWarningDialog = this.mMessagePopup.showOkAndCustomView(view, R.string.cam_strings_dialog_high_temp_title_txt, false, R.string.cam_strings_ok_txt, (DialogInterface.OnClickListener)thermalWarningDialogCloseListener, (DialogInterface.OnCancelListener)thermalWarningDialogCloseListener);
        if (this.mThermalWarningDialog == null) return;
        this.mThermalWarningDialog.setOnDismissListener((DialogInterface.OnDismissListener)thermalWarningDialogCloseListener);
    }

    public boolean isAlreadyHighTemperature() {
        return this.mIsAlreadyHighTemperature;
    }

    public boolean isWarningState() {
        return this.mIsWarningState;
    }

    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL");
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN");
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL");
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN");
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET");
        intentFilter.addAction("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET");
        this.mActivity.registerReceiver((BroadcastReceiver)this, intentFilter);
    }

    public void onDestroy() {
        this.mActivity.unregisterReceiver((BroadcastReceiver)this);
    }

    public void onPause() {
        this.mIsAlreadyHighTemperature = false;
        if (this.mIsBindSysmonService) {
            this.mIsBindSysmonService = false;
            this.mActivity.unbindService(this.mServiceConnectionSysmon);
        }
        this.mLowTempBurnTimerFixedDuration.cancel();
        this.mLowTempBurnTimerVariableDuration.cancel();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (!this.mIsBindSysmonService || this.mIsAlreadyHighTemperature) return;
        String string = intent.getAction();
        if ("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_CRITICAL".equals((Object)string)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            this.mLowTempBurnTimerVariableDuration.cancel();
            this.mIsAlreadyHighTemperature = true;
            if (this.mThermalWarningDialog != null) {
                this.mThermalWarningDialog.dismiss();
            }
            super.showDialogCritical();
            return;
        }
        if ("com.sonyericsson.psm.action.CAMERA_COOLED_DOWN_NORMAL".equals((Object)string)) {
            this.mLowTempBurnTimerFixedDuration.cancel();
            super.changeToNormalState();
            return;
        }
        if ("com.sonyericsson.psm.action.CAMERA_HEATED_OVER_LOW_TEMP_BURN".equals((Object)string)) {
            this.mLowTempBurnTimerFixedDuration.requestTimeMillis(1800000);
            return;
        }
        if ("com.sonyericsson.psm.action.CAMERA_HEATED_CLOSE_TO_SHUTDOWN".equals((Object)string)) {
            if (!this.mParamSharedPrefWrapper.getParamFromSP(super.getSharedPrefsKeyForThermalWarningDialog(), false)) {
                super.showDialogWarning();
            }
            super.changeToWarningState();
            return;
        }
        if ("com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_SET".equals((Object)string)) {
            int n;
            Bundle bundle = intent.getExtras();
            if (bundle == null || (n = bundle.getInt("com.sonyericsson.psm.extra.TIMEOUT_SEC", -1)) == -1) return;
            {
                this.mLowTempBurnTimerVariableDuration.requestTimeMillis(n * 1000);
                return;
            }
        }
        if (!"com.sonyericsson.psm.action.CAMERA_LOW_TEMP_BURN_TIMER_RESET".equals((Object)string)) {
            return;
        }
        this.mLowTempBurnTimerVariableDuration.cancel();
    }

    public void onResume() {
        this.mIsAlreadyHighTemperature = false;
        Intent intent = new Intent("com.sonyericsson.psm.sysmonservice");
        intent.setAction("com.sonyericsson.psm.sysmonservice.ISysmonService");
        this.mIsBindSysmonService = this.mActivity.bindService(intent, this.mServiceConnectionSysmon, 0);
        if (this.mIsBindSysmonService) {
            return;
        }
        this.mActivity.unbindService(this.mServiceConnectionSysmon);
    }

    /*
     * Failed to analyse overrides
     */
    protected class CheckBoxListener
    implements CompoundButton.OnCheckedChangeListener {
        private final ParamSharedPrefWrapper mSharedPrefs;
        private final String mSharedPrefsKey;

        public CheckBoxListener(ParamSharedPrefWrapper paramSharedPrefWrapper, String string) {
            this.mSharedPrefs = paramSharedPrefWrapper;
            this.mSharedPrefsKey = string;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
            boolean bl2 = false;
            if (bl) {
                bl2 = true;
            }
            this.mSharedPrefs.setParamToSP(this.mSharedPrefsKey, bl2);
        }
    }

    private class LowTempBurnTimeoutTimerWrapper {
        static final long INVALID_TIMER_TIME = -1;
        private Timer mTimer;
        private long mTimerToBeExpiredTimeMillis;
        final /* synthetic */ ThermalAlertReceiver this$0;

        private LowTempBurnTimeoutTimerWrapper(ThermalAlertReceiver thermalAlertReceiver) {
            this.this$0 = thermalAlertReceiver;
            this.mTimer = null;
            this.mTimerToBeExpiredTimeMillis = -1;
        }

        /* synthetic */ LowTempBurnTimeoutTimerWrapper(ThermalAlertReceiver thermalAlertReceiver,  var2_2) {
            super(thermalAlertReceiver);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private long getRemainedTimeMillis() {
            if (this.mTimerToBeExpiredTimeMillis == -1) {
                return -1;
            }
            long l = this.mTimerToBeExpiredTimeMillis - System.currentTimeMillis();
            if (l > 0) return l;
            return -1;
        }

        public final void cancel() {
            LowTempBurnTimeoutTimerWrapper lowTempBurnTimeoutTimerWrapper = this;
            synchronized (lowTempBurnTimeoutTimerWrapper) {
                if (this.mTimer != null) {
                    this.mTimer.cancel();
                    this.mTimer.purge();
                    this.mTimer = null;
                    this.mTimerToBeExpiredTimeMillis = -1;
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public final void requestTimeMillis(long l) {
            void var6_2 = this;
            synchronized (var6_2) {
                long l2 = super.getRemainedTimeMillis();
                if (l2 == -1 || l2 >= l) {
                    this.cancel();
                    this.mTimer = new Timer(true);
                    this.mTimer.schedule((TimerTask)new LowTempBurnTimerTask((LowTempBurnTimeoutTimerWrapper)this, null), l);
                    this.mTimerToBeExpiredTimeMillis = l + System.currentTimeMillis();
                }
                return;
            }
        }

        /*
         * Failed to analyse overrides
         */
        private class LowTempBurnTimerTask
        extends TimerTask {
            private final Handler mHandler;
            final /* synthetic */ LowTempBurnTimeoutTimerWrapper this$1;

            private LowTempBurnTimerTask(LowTempBurnTimeoutTimerWrapper lowTempBurnTimeoutTimerWrapper) {
                this.this$1 = lowTempBurnTimeoutTimerWrapper;
                this.mHandler = new Handler();
            }

            /* synthetic */ LowTempBurnTimerTask(LowTempBurnTimeoutTimerWrapper lowTempBurnTimeoutTimerWrapper, com.sonyericsson.cameracommon.thermal.ThermalAlertReceiver$1 var2_2) {
                super(lowTempBurnTimeoutTimerWrapper);
            }

            public void run() {
                this.cancel();
                this.mHandler.post((Runnable)new Runnable(){

                    public void run() {
                        LowTempBurnTimerTask.this.this$1.this$0.mIsAlreadyHighTemperature = true;
                        LowTempBurnTimerTask.this.this$1.this$0.showDialogCritical();
                    }
                });
            }

        }

    }

    /*
     * Failed to analyse overrides
     */
    class ServiceConnectionSysmon
    implements ServiceConnection {
        ServiceConnectionSysmon() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ThermalAlertReceiver.this.mSysmonService = ISysmonService.Stub.asInterface(iBinder);
            if (ThermalAlertReceiver.this.mSysmonService == null) return;
            try {
                int n = ThermalAlertReceiver.this.mSysmonService.getThermalLevelForCamera();
                ThermalAlertReceiver.this.checkStartupStatus(n, "sysmon");
                int n2 = ThermalAlertReceiver.this.mSysmonService.getCameraLowTempBurnTimeoutSec();
                ThermalAlertReceiver.this.checkLowTempBurnTimeoutTimerDuration(n, n2);
                return;
            }
            catch (Exception var4_5) {
                CameraLogger.e(TAG, "sysmon ServiceConnection failed.", (Throwable)var4_5);
                return;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ThermalAlertReceiver.this.mSysmonService = null;
        }
    }

    public static interface ThermalAlertReceiverListener {
        public void onNotifyThermalNormal();

        public void onNotifyThermalWarning();

        public void onReachHighTemperature(boolean var1);
    }

    /*
     * Failed to analyse overrides
     */
    private class ThermalWarningDialogCloseListener
    implements DialogInterface.OnCancelListener,
    DialogInterface.OnClickListener,
    DialogInterface.OnDismissListener {
        final /* synthetic */ ThermalAlertReceiver this$0;

        private ThermalWarningDialogCloseListener(ThermalAlertReceiver thermalAlertReceiver) {
            this.this$0 = thermalAlertReceiver;
        }

        /* synthetic */ ThermalWarningDialogCloseListener(ThermalAlertReceiver thermalAlertReceiver,  var2_2) {
            super(thermalAlertReceiver);
        }

        public void onCancel(DialogInterface dialogInterface) {
            this.this$0.mThermalWarningDialog = null;
        }

        public void onClick(DialogInterface dialogInterface, int n) {
            this.this$0.mThermalWarningDialog = null;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            this.this$0.mThermalWarningDialog = null;
        }
    }

}

