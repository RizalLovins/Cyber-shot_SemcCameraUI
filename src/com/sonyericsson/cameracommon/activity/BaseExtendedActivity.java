/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.KeyguardManager
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.telephony.PhoneStateListener
 *  android.telephony.TelephonyManager
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 */
package com.sonyericsson.cameracommon.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettingNotifyListener;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageUtil;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.OneShotUtility;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;

/*
 * Failed to analyse overrides
 */
public abstract class BaseExtendedActivity
extends BaseActivity {
    private static final String TAG = "BaseExtendedActivity";
    private boolean mIsAlertDialogOpened;
    private PhoneStateChangedListener mPhoneStateChangedListener;
    private ExtendedPhoneStateListener mPhoneStateListener;
    protected SoundPlayer mSoundPlayer;
    private StartUpGuardian mStartUpGuardian;
    private final BroadcastReceiver mUserPresentIntentReceiver;

    public BaseExtendedActivity() {
        this.mUserPresentIntentReceiver = new UserPresentIntentReceiver(this, null);
        this.mIsAlertDialogOpened = false;
    }

    protected void abort() {
        this.finish();
    }

    protected void cancelCheckAutoUploadSetting() {
        AutoUploadSettings.getInstance().cancel((Context)this);
    }

    public final void dismissDialog() {
        if (this.mNonCancelableErrorDialog != null) {
            this.mNonCancelableErrorDialog.dismiss();
            this.mNonCancelableErrorDialog = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void finishOneShot(Intent intent) {
        if (intent == null) {
            this.setResult(0);
        } else {
            this.setResult(-1, intent);
        }
        this.finish();
    }

    public Camera.Parameters forTestGetCameraParameters() {
        throw new UnsupportedOperationException("unsupported method");
    }

    protected abstract Rect getPreviewSize();

    public SoundPlayer getSoundPlayer() {
        return this.mSoundPlayer;
    }

    public boolean isAlertDialogOpened() {
        return this.mIsAlertDialogOpened;
    }

    public boolean isPhoneCallWorking() {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService("phone");
        if (telephonyManager != null && telephonyManager.getCallState() != 0) {
            return true;
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    public boolean isPreinstalledApp() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:215)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:160)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:71)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:718)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:650)
        // org.benf.cfr.reader.Main.doJar(Main.java:109)
        // com.njlabs.showjava.AppProcessActivity$4.run(AppProcessActivity.java:423)
        // java.lang.Thread.run(Thread.java:818)
        throw new IllegalStateException("Decompilation failed");
    }

    public void onBackPressed() {
        this.terminateApplication();
    }

    protected void onCreate(Bundle bundle) {
        this.mLaunchedBy = BaseActivity.LaunchedBy.INTENT;
        super.onCreate(bundle);
    }

    protected void onDestroy() {
        this.mPhoneStateListener = null;
        super.onDestroy();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mLaunchedBy = BaseActivity.LaunchedBy.INTENT;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onPause() {
        if (this.mStartUpGuardian != null) {
            this.mStartUpGuardian.pause();
            this.mStartUpGuardian = null;
        } else {
            CameraLogger.e("BaseExtendedActivity", "onPause():[Application is already paused.]");
        }
        this.unregisterReceiver(this.mUserPresentIntentReceiver);
        this.cancelCheckAutoUploadSetting();
        this.mLaunchedBy = BaseActivity.LaunchedBy.UNKNOWN;
        super.onPause();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onResume() {
        this.mStartUpGuardian = StaticConfigurationUtil.isForceSound() ? new StartUpGuardianForceSoundOn(this, null) : new StartUpGuardianUnlimited(this, null);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.USER_PRESENT");
        this.registerReceiver(this.mUserPresentIntentReceiver, intentFilter);
        this.requestCheckAutoUploadSetting();
        if (this.mLaunchedBy == BaseActivity.LaunchedBy.UNKNOWN) {
            this.mLaunchedBy = BaseActivity.LaunchedBy.HISTORY;
        }
        super.onResume();
        this.mStorageManager.resume();
        this.mStartUpGuardian.resume();
        GoogleAnalyticsUtil.setLaunchedFrom(this.mLaunchedBy, this.getIntent(), false, this.isOneShot());
    }

    public void onStart() {
        super.onStart();
        this.mPhoneStateListener = new ExtendedPhoneStateListener(this, null);
        ((TelephonyManager)this.getSystemService("phone")).listen((PhoneStateListener)this.mPhoneStateListener, 32);
    }

    protected void onStop() {
        ((TelephonyManager)this.getSystemService("phone")).listen((PhoneStateListener)this.mPhoneStateListener, 0);
        this.mPhoneStateListener = null;
        super.onStop();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onStoreComplete(boolean bl, Uri uri, String string, int n) {
        if (this.isOneShot()) {
            if (this.getExtraOutput() == null) {
                Intent intent = null;
                if (bl) {
                    intent = OneShotUtility.createResultIntent((Activity)this, uri, string, n);
                }
                this.finishOneShot(intent);
            } else {
                if (bl) {
                    this.setResult(-1);
                } else {
                    this.setResult(0);
                }
                this.finish();
            }
        }
        this.enableAutoOffTimer();
    }

    protected void requestCheckAutoUploadSetting() {
        AutoUploadSettings.getInstance().request((Context)this, new AutoUploadSettingCheckCallback(null));
    }

    public void resumeFromIncomingCall() {
        this.dismissDialog();
        this.mStartUpGuardian.tryToResumeAll();
    }

    public void setAlertDialogIsOpened(boolean bl) {
        this.mIsAlertDialogOpened = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDestinationToSave() {
        StorageManager.StorageType storageType = this.hasExtraOutputPath() ? StorageUtil.getStorageTypeFromPath(StorageUtil.getPathFromUri((Context)this, this.getExtraOutput()), (Context)this) : StorageUtil.getStorageTypeFromSaveDestination((SaveDestination)this.mCommonSettings.get(CommonSettingKey.SAVE_DESTINATION));
        this.mStorageManager.setCurrentStorage(storageType);
    }

    public void setPhoneStateChangedListener(PhoneStateChangedListener phoneStateChangedListener) {
        this.mPhoneStateChangedListener = phoneStateChangedListener;
    }

    protected void showAndConfirmDisclaimer() {
    }

    public void terminateApplication() {
        this.finish();
    }

    private static class AutoUploadSettingCheckCallback
    implements AutoUploadSettingNotifyListener {
        private AutoUploadSettingCheckCallback() {
        }

        /* synthetic */ AutoUploadSettingCheckCallback( var1) {
        }

        @Override
        public void onAutoUploadSettingNotified(Context context, String string, AutoUploadSettings.AutoUploadSetting autoUploadSetting) {
            if (autoUploadSetting == AutoUploadSettings.AutoUploadSetting.UNKNOWN) {
                // empty if block
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private static class CameraActivityFinishBroadcastReceiver
    extends BroadcastReceiver {
        BaseExtendedActivity mBaseExtendedActivity = null;

        CameraActivityFinishBroadcastReceiver(BaseExtendedActivity baseExtendedActivity) {
            this.mBaseExtendedActivity = baseExtendedActivity;
        }

        public void onReceive(Context context, Intent intent) {
            this.mBaseExtendedActivity.finish();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ExtendedPhoneStateListener
    extends PhoneStateListener {
        final /* synthetic */ BaseExtendedActivity this$0;

        private ExtendedPhoneStateListener(BaseExtendedActivity baseExtendedActivity) {
            this.this$0 = baseExtendedActivity;
        }

        /* synthetic */ ExtendedPhoneStateListener(BaseExtendedActivity baseExtendedActivity,  var2_2) {
            super(baseExtendedActivity);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onCallStateChanged(int n, String string) {
            super.onCallStateChanged(n, string);
            switch (n) {
                case 0: {
                    if (this.this$0.mStartUpGuardian == null) return;
                    {
                        this.this$0.mStartUpGuardian.resume();
                        return;
                    }
                }
                case 1: {
                    if (this.this$0.mPhoneStateChangedListener == null) return;
                    {
                        this.this$0.mPhoneStateChangedListener.onRinging();
                        return;
                    }
                }
                default: {
                    return;
                }
                case 2: 
            }
            if (this.this$0.mPhoneStateChangedListener == null) return;
            {
                this.this$0.mPhoneStateChangedListener.onOffHook();
                return;
            }
        }
    }

    public static interface PhoneStateChangedListener {
        public void onOffHook();

        public void onRinging();
    }

    private abstract class StartUpGuardian {
        private boolean mIsAlreadyResumed;
        final /* synthetic */ BaseExtendedActivity this$0;

        private StartUpGuardian(BaseExtendedActivity baseExtendedActivity) {
            this.this$0 = baseExtendedActivity;
            this.mIsAlreadyResumed = false;
        }

        /* synthetic */ StartUpGuardian(BaseExtendedActivity baseExtendedActivity,  var2_2) {
            super(baseExtendedActivity);
        }

        public void pause() {
        }

        public void resume() {
            if (((KeyguardManager)this.this$0.getSystemService("keyguard")).inKeyguardRestrictedInputMode() && Settings.Global.getInt((ContentResolver)this.this$0.getContentResolver(), (String)"device_provisioned", (int)0) != 0) {
                this.this$0.mIsKeyguardAvailable = true;
                return;
            }
            this.this$0.mIsKeyguardAvailable = false;
        }

        protected void tryToResumeAll() {
            if (!(this.this$0.mIsKeyguardAvailable || this.mIsAlreadyResumed)) {
                this.mIsAlreadyResumed = true;
                if (!this.this$0.isPreinstalledApp()) {
                    this.this$0.showAndConfirmDisclaimer();
                }
                this.this$0.resumeAll();
            }
        }
    }

    private class StartUpGuardianForceSoundOn
    extends StartUpGuardian {
        final /* synthetic */ BaseExtendedActivity this$0;

        private StartUpGuardianForceSoundOn(BaseExtendedActivity baseExtendedActivity) {
            this.this$0 = baseExtendedActivity;
            super(baseExtendedActivity, null);
        }

        /* synthetic */ StartUpGuardianForceSoundOn(BaseExtendedActivity baseExtendedActivity,  var2_2) {
            super(baseExtendedActivity);
        }

        @Override
        public void pause() {
            super.pause();
            this.this$0.dismissDialog();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void resume() {
            super.resume();
            if (!this.this$0.isPhoneCallWorking()) {
                this.this$0.pauseAudioPlayback();
                if (this.this$0.mNonCancelableErrorDialog == null) {
                    this.this$0.dismissDialog();
                    this.tryToResumeAll();
                    return;
                }
                this.this$0.resumeFromIncomingCall();
                return;
            } else {
                if (this.this$0.mNonCancelableErrorDialog != null) return;
                {
                    this.this$0.mNonCancelableErrorDialog = this.this$0.getCallingDialog();
                    return;
                }
            }
        }
    }

    private class StartUpGuardianUnlimited
    extends StartUpGuardian {
        final /* synthetic */ BaseExtendedActivity this$0;

        private StartUpGuardianUnlimited(BaseExtendedActivity baseExtendedActivity) {
            this.this$0 = baseExtendedActivity;
            super(baseExtendedActivity, null);
        }

        /* synthetic */ StartUpGuardianUnlimited(BaseExtendedActivity baseExtendedActivity,  var2_2) {
            super(baseExtendedActivity);
        }

        @Override
        public void pause() {
            super.pause();
        }

        @Override
        public void resume() {
            super.resume();
            this.tryToResumeAll();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class UserPresentIntentReceiver
    extends BroadcastReceiver {
        final /* synthetic */ BaseExtendedActivity this$0;

        private UserPresentIntentReceiver(BaseExtendedActivity baseExtendedActivity) {
            this.this$0 = baseExtendedActivity;
        }

        /* synthetic */ UserPresentIntentReceiver(BaseExtendedActivity baseExtendedActivity,  var2_2) {
            super(baseExtendedActivity);
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals((Object)"android.intent.action.USER_PRESENT") && this.this$0.mStartUpGuardian != null) {
                this.this$0.mStartUpGuardian.resume();
            }
        }
    }

}

