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
 *  android.media.AudioManager
 *  android.media.AudioManager$OnAudioFocusChangeListener
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Parcelable
 *  android.view.OrientationEventListener
 *  android.view.Window
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.Set
 *  java.util.Timer
 *  java.util.TimerTask
 *  java.util.concurrent.CopyOnWriteArraySet
 */
package com.sonyericsson.cameracommon.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.OrientationEventListener;
import android.view.Window;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.activity.TerminateListener;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.BalloonTipsCounter;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StorageUtil;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.messagepopup.MessagePopupStateListener;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.settings.AutoReviewSettings;
import com.sonyericsson.cameracommon.thermal.ThermalAlertReceiver;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * Failed to analyse overrides
 */
public abstract class BaseActivity
extends Activity
implements TerminateListener,
MessagePopupStateListener,
ThermalAlertReceiver.ThermalAlertReceiverListener {
    private static final int AUTO_OFF_TIMER_TIMEOUT_COUNT = 180000;
    public static final String INTENT_SUBJECT_START_SECURE = "start-secure";
    private static final String TAG = "BaseActivity";
    boolean mAddToMediaStore;
    Timer mAutoOffTimer;
    protected AutoReviewSettings mAutoReviewSettings;
    protected CommonSettings mCommonSettings;
    private final BroadcastReceiver mExtendedBroadcastReceiver;
    private Uri mExtraOutput;
    protected GeotagManager mGeotagManager;
    Handler mHandler;
    private boolean mIsAutoOffTimerEnabled = true;
    protected boolean mIsKeyguardAvailable = false;
    private boolean mIsReceiverResistered = false;
    private LayoutOrientation mLastDetectedOrientation = LayoutOrientation.Landscape;
    private int mLastDeterminedOrientationDegree = -1;
    private int mLastOrientationDegree = -1;
    private boolean mLaunchAsOneShot = false;
    private boolean mLaunchAsOneShotPhoto = false;
    private boolean mLaunchAsOneShotPhotoSecure = false;
    private boolean mLaunchAsOneShotVideo = false;
    protected LaunchedBy mLaunchedBy = LaunchedBy.UNKNOWN;
    private Set<LayoutOrientationChangedListener> mLayoutOrientationChangedListenerSet = new CopyOnWriteArraySet();
    private LocationSettingsReader mLocationSettingsReader;
    protected MessagePopup mMessagePopup;
    protected RotatableDialog mNonCancelableErrorDialog;
    private OrientationEventListener mOrientationEventListener;
    protected SavingTaskManager mSavingTaskManager;
    private int mSensorOrientationDegree = -1;
    protected StorageController mStorageController;
    private Set<StorageEventListener> mStorageListenerSet;
    protected CameraStorageManager mStorageManager;
    protected TerminateListener mTerminateListener;
    protected ThermalAlertReceiver mThermalAlertReceiver;

    public BaseActivity() {
        this.mExtendedBroadcastReceiver = new StorageBroadcastReceiver(this, null);
        this.mStorageListenerSet = new CopyOnWriteArraySet();
        this.mHandler = new Handler();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkOneShot() {
        Bundle bundle;
        Intent intent = this.getIntent();
        String string = intent.getAction();
        if (string == null) {
            CameraLogger.e("BaseActivity", "setRequestedMode: getAction() == null ");
            intent.setAction("android.intent.action.MAIN");
            string = intent.getAction();
        }
        if (string.equals((Object)"android.media.action.IMAGE_CAPTURE")) {
            this.mLaunchAsOneShotPhoto = true;
        } else if (string.equals((Object)"android.media.action.IMAGE_CAPTURE_SECURE")) {
            this.mLaunchAsOneShotPhotoSecure = true;
        } else if (string.equals((Object)"android.media.action.VIDEO_CAPTURE")) {
            this.mLaunchAsOneShotVideo = true;
        }
        if (this.mLaunchAsOneShotPhoto || this.mLaunchAsOneShotPhotoSecure || this.mLaunchAsOneShotVideo) {
            this.mLaunchAsOneShot = true;
        }
        if ((bundle = intent.getExtras()) == null) {
            this.mExtraOutput = null;
            this.mAddToMediaStore = true;
        } else if (this.mLaunchAsOneShot) {
            Iterator iterator = bundle.keySet().iterator();
            while (iterator.hasNext()) {
                (String)iterator.next();
            }
            this.mExtraOutput = (Uri)bundle.getParcelable("output");
            this.mAddToMediaStore = bundle.getBoolean("addToMediaStore");
        } else {
            this.mExtraOutput = null;
            this.mAddToMediaStore = true;
        }
        if (this.mExtraOutput != null) {
            // empty if block
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getOrientationDegree(LayoutOrientation layoutOrientation) {
        int n;
        switch (.$SwitchMap$com$sonyericsson$cameracommon$activity$BaseActivity$LayoutOrientation[layoutOrientation.ordinal()]) {
            default: {
                return -1;
            }
            case 1: 
            case 2: {
                n = 0;
                do {
                    return (n + ProductConfig.getMountAngle((Context)this)) % 360;
                    break;
                } while (true);
            }
            case 3: {
                n = 90;
                return (n + ProductConfig.getMountAngle((Context)this)) % 360;
            }
            case 4: {
                n = 180;
                return (n + ProductConfig.getMountAngle((Context)this)) % 360;
            }
            case 5: 
        }
        n = 270;
        return (n + ProductConfig.getMountAngle((Context)this)) % 360;
    }

    private static boolean in(int n, int n2, int n3) {
        if (n >= n2 && n < n3) {
            return true;
        }
        return false;
    }

    private void muteSound(int n) {
        AudioManager audioManager;
        if (Build.VERSION.SDK_INT < 8 || (audioManager = (AudioManager)this.getSystemService("audio")) == null || audioManager.requestAudioFocus(null, n, 1) == 1) {
            // empty if block
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void notifyLayoutOrientationChanged(LayoutOrientation layoutOrientation) {
        if (layoutOrientation != this.mLastDetectedOrientation && layoutOrientation != LayoutOrientation.Unknown) {
            this.mLastDetectedOrientation = layoutOrientation;
            Iterator iterator = this.mLayoutOrientationChangedListenerSet.iterator();
            while (iterator.hasNext()) {
                ((LayoutOrientationChangedListener)iterator.next()).onLayoutOrientationChanged(this.mLastDetectedOrientation);
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void notifyStorageStatusChanged(String string, Uri uri) {
        for (StorageEventListener storageEventListener : this.mStorageListenerSet) {
            String string2 = uri.getPath();
            if (string.equals((Object)"android.intent.action.MEDIA_SCANNER_FINISHED")) {
                storageEventListener.onMediaScanFinished();
                continue;
            }
            if (string2 == null) continue;
            storageEventListener.onStorageCheckRequested(string, string2);
        }
    }

    private void registerIntentFilter(String string, String string2) {
        IntentFilter intentFilter = new IntentFilter(string);
        if (string2 != null) {
            intentFilter.addDataScheme(string2);
        }
        this.registerReceiver(this.mExtendedBroadcastReceiver, intentFilter);
        this.mIsReceiverResistered = true;
    }

    private final void stopAutoOffTimer() {
        BaseActivity baseActivity = this;
        synchronized (baseActivity) {
            if (this.mAutoOffTimer != null) {
                this.mAutoOffTimer.cancel();
                this.mAutoOffTimer.purge();
                this.mAutoOffTimer = null;
            }
            return;
        }
    }

    private void unmuteSound(int n) {
        AudioManager audioManager;
        if (Build.VERSION.SDK_INT < 8 || (audioManager = (AudioManager)this.getSystemService("audio")) == null || audioManager.abandonAudioFocus(null) == 1) {
            // empty if block
        }
    }

    private void updateBalloonTipsCounter() {
        BalloonTipsCounter balloonTipsCounter = (BalloonTipsCounter)this.mCommonSettings.get(CommonSettingKey.BALLOON_TIPS_COUNTER);
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$BalloonTipsCounter[balloonTipsCounter.ordinal()]) {
            default: {
                this.mCommonSettings.set(BalloonTipsCounter.COUNT_STOP);
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                return;
            }
            case 1: {
                this.mCommonSettings.set(BalloonTipsCounter.FIRST);
                return;
            }
            case 2: {
                this.mCommonSettings.set(BalloonTipsCounter.SECOND);
                return;
            }
            case 3: 
        }
        this.mCommonSettings.set(BalloonTipsCounter.DISPLAY_OK_FIRST);
    }

    protected abstract void abort();

    public void addOrienationListener(LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.add((Object)layoutOrientationChangedListener);
    }

    public void addStorageListener(StorageEventListener storageEventListener) {
        if (!(this.mStorageListenerSet.contains((Object)storageEventListener) || storageEventListener == null)) {
            this.mStorageListenerSet.add((Object)storageEventListener);
        }
    }

    protected void callOnCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    protected void callOnDestroy() {
        super.onDestroy();
    }

    protected void callOnPause() {
        super.onPause();
    }

    protected void callOnRestart() {
        super.onRestart();
    }

    protected void callOnResume() {
        super.onResume();
    }

    protected void callOnStart() {
        super.onStart();
    }

    protected void callOnStop() {
        super.onStop();
    }

    protected void clearKeepScreenOn() {
        this.getWindow().clearFlags(128);
    }

    protected void createCommonSettings() {
        this.mCommonSettings = new CommonSettings(this.getContentResolver(), (Context)this);
    }

    public final void disableAutoOffTimer() {
        this.stopAutoOffTimer();
        this.mIsAutoOffTimerEnabled = false;
    }

    public void disableOrientation() {
        if (this.mOrientationEventListener != null) {
            this.mOrientationEventListener.disable();
            this.mOrientationEventListener = null;
        }
    }

    public final void enableAutoOffTimer() {
        this.mIsAutoOffTimerEnabled = true;
        this.startAutoOffTimer();
    }

    public void enableOrientation() {
        if (this.mOrientationEventListener == null) {
            this.mOrientationEventListener = new ExtendedOrientationEventListener((Context)this);
            this.mOrientationEventListener.enable();
        }
    }

    public AutoReviewSettings getAutoReviewSettings() {
        return this.mAutoReviewSettings;
    }

    public abstract RotatableDialog getCallingDialog();

    public CommonSettings getCommonSettings() {
        return this.mCommonSettings;
    }

    public int getConfigurationOrientation() {
        switch (this.mLastDeterminedOrientationDegree) {
            default: {
                return 2;
            }
            case 1: 
        }
        return 1;
    }

    public Uri getExtraOutput() {
        return this.mExtraOutput;
    }

    public GeotagManager getGeoTagManager() {
        return this.mGeotagManager;
    }

    public LayoutOrientation getLastDetectedOrientation() {
        return this.mLastDetectedOrientation;
    }

    /*
     * Enabled aggressive block sorting
     */
    public LayoutOrientation getLayoutOrientation() {
        int n = this.mLastOrientationDegree;
        if (n == -1) {
            n = this.mLastDeterminedOrientationDegree;
        }
        if (n == -1) {
            return LayoutOrientation.Unknown;
        }
        int n2 = (n + (360 - ProductConfig.getMountAngle((Context)this))) % 360;
        boolean bl = this.mLastDetectedOrientation == LayoutOrientation.Portrait || this.mLastDetectedOrientation == LayoutOrientation.ReversePortrait;
        int n3 = bl ? 60 : 30;
        if (BaseActivity.in(n2, 90 - n3, n3 + 90)) {
            return LayoutOrientation.Portrait;
        }
        if (BaseActivity.in(n2, n3 + 90, 270 - n3)) {
            return LayoutOrientation.ReverseLandscape;
        }
        if (BaseActivity.in(n2, 270 - n3, n3 + 270)) {
            return LayoutOrientation.ReversePortrait;
        }
        return LayoutOrientation.Landscape;
    }

    public MessagePopup getMessagePopup() {
        return this.mMessagePopup;
    }

    public int getOrientation() {
        LayoutOrientation layoutOrientation = this.mLastDetectedOrientation;
        if (layoutOrientation == LayoutOrientation.Unknown) {
            layoutOrientation = this.getLayoutOrientation();
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$activity$BaseActivity$LayoutOrientation[layoutOrientation.ordinal()]) {
            default: {
                return 0;
            }
            case 3: 
            case 5: {
                return 1;
            }
            case 2: 
            case 4: 
        }
        return 2;
    }

    public int getOrientationDegree() {
        if (this.mLastOrientationDegree != -1) {
            return this.mLastOrientationDegree;
        }
        if (this.mLastDeterminedOrientationDegree != -1) {
            return this.mLastDeterminedOrientationDegree;
        }
        return 0;
    }

    protected abstract ParamSharedPrefWrapper getParamSharedPrefWrapper();

    public SavingTaskManager getSavingTaskManager() {
        return this.mSavingTaskManager;
    }

    public int getSensorOrientationDegree() {
        return this.mSensorOrientationDegree;
    }

    public StorageController getStorageController() {
        return this.mStorageController;
    }

    public CameraStorageManager getStorageManager() {
        return this.mStorageManager;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean hasExtraOutputPath() {
        if (this.mExtraOutput == null || StorageUtil.getPathFromUri((Context)this, this.mExtraOutput) == null) {
            return false;
        }
        return true;
    }

    public abstract boolean isAlertDialogOpened();

    public boolean isAlreadyHighTemperature() {
        return this.mThermalAlertReceiver.isAlreadyHighTemperature();
    }

    public boolean isDeviceInSecurityLock() {
        Bundle bundle = this.getIntent().getExtras();
        boolean bl = false;
        if (bundle != null) {
            String string = bundle.getString("android.intent.extra.SUBJECT");
            boolean bl2 = ((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
            boolean bl3 = "start-secure".equals((Object)string);
            bl = false;
            if (bl3) {
                bl = false;
                if (bl2) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    public abstract boolean isDualStorageAvailable();

    public boolean isGpsLocationAllowed() {
        return this.mLocationSettingsReader.getIsGpsLocationAllowed();
    }

    public abstract boolean isMenuAvailable();

    public boolean isNetworkLocationAllowed() {
        return this.mLocationSettingsReader.getIsNetworkLocationAllowed();
    }

    public boolean isOneShot() {
        return this.mLaunchAsOneShot;
    }

    public boolean isOneShotPhoto() {
        return this.mLaunchAsOneShotPhoto;
    }

    public boolean isOneShotPhotoSecure() {
        return this.mLaunchAsOneShotPhotoSecure;
    }

    public boolean isOneShotVideo() {
        return this.mLaunchAsOneShotVideo;
    }

    public boolean isThermalWarningState() {
        return this.mThermalAlertReceiver.isWarningState();
    }

    protected void keepScreenOn() {
        this.getWindow().addFlags(128);
    }

    public void logLifeCycleIn(String string, LifeCycleIds lifeCycleIds) {
        MeasurePerformance.measureTime(lifeCycleIds.mPerformanceIds, true);
        MeasurePerformance.measureResource("Start " + (Object)lifeCycleIds);
        if (lifeCycleIds == LifeCycleIds.ON_CREATE || lifeCycleIds == LifeCycleIds.ON_DESTROY) {
            // empty if block
        }
    }

    public void logLifeCycleOut(String string, LifeCycleIds lifeCycleIds) {
        MeasurePerformance.measureResource("End " + (Object)lifeCycleIds);
        MeasurePerformance.measureTime(lifeCycleIds.mPerformanceIds, false);
        if (lifeCycleIds == LifeCycleIds.ON_PAUSE) {
            MeasurePerformance.outResult();
        }
    }

    public void msgPopupCanceled() {
    }

    public void msgPopupOpened() {
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT <= 19) {
            this.setTheme(R.style.SomcCameraHoloTheme);
        }
        super.checkOneShot();
        this.mLaunchedBy = LaunchedBy.INTENT;
        this.mMessagePopup = new MessagePopup((Activity)this, (TerminateListener)this);
        this.mMessagePopup.setMessagePopupStateListener((MessagePopupStateListener)this);
        this.addOrienationListener(this.mMessagePopup);
        this.mThermalAlertReceiver = new ThermalAlertReceiver((Activity)this, this.mMessagePopup, (ThermalAlertReceiver.ThermalAlertReceiverListener)this, this.getParamSharedPrefWrapper());
        this.mAutoReviewSettings = new AutoReviewSettings();
        this.createCommonSettings();
        this.mLocationSettingsReader = new LocationSettingsReader();
        GoogleAnalyticsUtil.setContext((Context)this);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mStorageListenerSet.clear();
        this.removeOrienationListener(this.mMessagePopup);
        this.mMessagePopup.releaseContext();
        this.mMessagePopup = null;
        this.mLayoutOrientationChangedListenerSet.clear();
    }

    protected void onPause() {
        super.onPause();
        if (this.mIsReceiverResistered) {
            this.unregisterReceiver(this.mExtendedBroadcastReceiver);
            this.mIsReceiverResistered = false;
        }
        this.disableAutoOffTimer();
        this.unmuteSound(3);
        this.mCommonSettings.suspend();
        this.mMessagePopup.release();
        this.disableOrientation();
        this.removeStorageListener(this.mStorageManager);
    }

    protected void onResume() {
        this.registerIntentFilter("android.intent.action.MEDIA_MOUNTED", "file");
        this.registerIntentFilter("android.intent.action.MEDIA_UNMOUNTED", "file");
        this.registerIntentFilter("android.intent.action.MEDIA_EJECT", "file");
        this.registerIntentFilter("android.intent.action.MEDIA_SCANNER_FINISHED", "file");
        super.onResume();
        this.getWindow().clearFlags(2048);
        this.getWindow().addFlags(1024);
        this.mCommonSettings.load();
        this.updateBalloonTipsCounter();
        this.mLocationSettingsReader.readLocationSettings((Context)this);
        this.enableAutoOffTimer();
        this.mSensorOrientationDegree = this.getOrientationDegree(LayoutOrientation.Unknown);
        this.addStorageListener(this.mStorageManager);
        GoogleAnalyticsUtil.setContext((Context)this);
    }

    public boolean onSearchRequested() {
        return false;
    }

    protected void onStop() {
        super.onStop();
    }

    public void pauseAudioPlayback() {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.sendBroadcast(intent);
        this.muteSound(3);
    }

    public void readLocationSettings() {
        this.mLocationSettingsReader.readLocationSettings((Context)this);
    }

    public void removeOrienationListener(LayoutOrientationChangedListener layoutOrientationChangedListener) {
        this.mLayoutOrientationChangedListenerSet.remove((Object)layoutOrientationChangedListener);
    }

    public void removeStorageListener(StorageEventListener storageEventListener) {
        if (this.mStorageListenerSet.contains((Object)storageEventListener)) {
            this.mStorageListenerSet.remove((Object)storageEventListener);
        }
    }

    public final void restartAutoOffTimer() {
        BaseActivity baseActivity = this;
        synchronized (baseActivity) {
            this.stopAutoOffTimer();
            this.startAutoOffTimer();
            return;
        }
    }

    protected abstract void resumeAll();

    public abstract void setAlertDialogIsOpened(boolean var1);

    public boolean shouldAddOnlyNewContent() {
        return false;
    }

    public boolean shouldAddToMediaStore() {
        return this.mAddToMediaStore;
    }

    protected boolean startAutoOffTimer() {
        return this.startAutoOffTimer(180000);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    protected final boolean startAutoOffTimer(int n) {
        void var6_2 = this;
        synchronized (var6_2) {
            boolean bl = this.mIsAutoOffTimerEnabled;
            boolean bl2 = false;
            if (!bl) return bl2;
            Timer timer = this.mAutoOffTimer;
            bl2 = false;
            if (timer != null) return bl2;
            this.mAutoOffTimer = new Timer(true);
            this.mAutoOffTimer.schedule((TimerTask)new AutoOffTimerTask((BaseActivity)this, null), (long)n);
            return true;
        }
    }

    public boolean updateRemain() {
        MeasurePerformance.measureTimeOverwrite(MeasurePerformance.PerformanceIds.UPDATE_REMAIN, true);
        long l = this.mSavingTaskManager.getExpectedTotalSavedPicturesSize();
        this.mStorageManager.updateRemain(l, false);
        boolean bl = this.mStorageManager.isReady();
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.UPDATE_REMAIN, false);
        return bl;
    }

    /*
     * Failed to analyse overrides
     */
    private class AutoOffTimerTask
    extends TimerTask {
        final /* synthetic */ BaseActivity this$0;

        private AutoOffTimerTask(BaseActivity baseActivity) {
            this.this$0 = baseActivity;
        }

        /* synthetic */ AutoOffTimerTask(BaseActivity baseActivity, com.sonyericsson.cameracommon.activity.BaseActivity$1 var2_2) {
            super(baseActivity);
        }

        public void run() {
            this.this$0.stopAutoOffTimer();
            this.this$0.mHandler.post((Runnable)new Runnable(){

                public void run() {
                    AutoOffTimerTask.this.this$0.abort();
                }
            });
        }

    }

    /*
     * Failed to analyse overrides
     */
    private class ExtendedOrientationEventListener
    extends OrientationEventListener {
        public ExtendedOrientationEventListener(Context context) {
            super(context);
        }

        public void onOrientationChanged(int n) {
            if (n != -1) {
                BaseActivity.this.mSensorOrientationDegree = n;
            }
            if (n == BaseActivity.this.mLastOrientationDegree) {
                return;
            }
            BaseActivity.this.mLastOrientationDegree = n;
            if (BaseActivity.this.mLastOrientationDegree != -1) {
                BaseActivity.this.mLastDeterminedOrientationDegree = BaseActivity.this.mLastOrientationDegree;
            }
            BaseActivity.this.notifyLayoutOrientationChanged(BaseActivity.this.getLayoutOrientation());
        }
    }

    public static final class LaunchedBy
    extends Enum<LaunchedBy> {
        private static final /* synthetic */ LaunchedBy[] $VALUES;
        public static final /* enum */ LaunchedBy HISTORY;
        public static final /* enum */ LaunchedBy INTENT;
        public static final /* enum */ LaunchedBy UNKNOWN;

        static {
            UNKNOWN = new LaunchedBy();
            INTENT = new LaunchedBy();
            HISTORY = new LaunchedBy();
            LaunchedBy[] arrlaunchedBy = new LaunchedBy[]{UNKNOWN, INTENT, HISTORY};
            $VALUES = arrlaunchedBy;
        }

        private LaunchedBy() {
            super(string, n);
        }

        public static LaunchedBy valueOf(String string) {
            return (LaunchedBy)Enum.valueOf((Class)LaunchedBy.class, (String)string);
        }

        public static LaunchedBy[] values() {
            return (LaunchedBy[])$VALUES.clone();
        }
    }

    public static final class LayoutOrientation
    extends Enum<LayoutOrientation> {
        private static final /* synthetic */ LayoutOrientation[] $VALUES;
        public static final /* enum */ LayoutOrientation Landscape;
        public static final /* enum */ LayoutOrientation Portrait;
        public static final /* enum */ LayoutOrientation ReverseLandscape;
        public static final /* enum */ LayoutOrientation ReversePortrait;
        public static final /* enum */ LayoutOrientation Unknown;

        static {
            Unknown = new LayoutOrientation();
            Portrait = new LayoutOrientation();
            Landscape = new LayoutOrientation();
            ReversePortrait = new LayoutOrientation();
            ReverseLandscape = new LayoutOrientation();
            LayoutOrientation[] arrlayoutOrientation = new LayoutOrientation[]{Unknown, Portrait, Landscape, ReversePortrait, ReverseLandscape};
            $VALUES = arrlayoutOrientation;
        }

        private LayoutOrientation() {
            super(string, n);
        }

        public static LayoutOrientation valueOf(String string) {
            return (LayoutOrientation)Enum.valueOf((Class)LayoutOrientation.class, (String)string);
        }

        public static LayoutOrientation[] values() {
            return (LayoutOrientation[])$VALUES.clone();
        }
    }

    public static interface LayoutOrientationChangedListener {
        public void onLayoutOrientationChanged(LayoutOrientation var1);
    }

    public static final class LifeCycleIds
    extends Enum<LifeCycleIds> {
        private static final /* synthetic */ LifeCycleIds[] $VALUES;
        public static final /* enum */ LifeCycleIds ON_CREATE = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_CREATE, "onCreate()");
        public static final /* enum */ LifeCycleIds ON_DESTROY;
        public static final /* enum */ LifeCycleIds ON_NEW_INTENT;
        public static final /* enum */ LifeCycleIds ON_PAUSE;
        public static final /* enum */ LifeCycleIds ON_RESTART;
        public static final /* enum */ LifeCycleIds ON_RESUME;
        public static final /* enum */ LifeCycleIds ON_START;
        public static final /* enum */ LifeCycleIds ON_STOP;
        private final String mLog;
        private final MeasurePerformance.PerformanceIds mPerformanceIds;

        static {
            ON_NEW_INTENT = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_NEW_INTENT, "onNewIntent()");
            ON_START = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_START, "onStart()");
            ON_RESTART = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_RESTART, "onRestart()");
            ON_RESUME = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_RESUME, "onResume()");
            ON_PAUSE = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_PAUSE, "onPause()");
            ON_STOP = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_STOP, "onStop()");
            ON_DESTROY = new LifeCycleIds(MeasurePerformance.PerformanceIds.ON_DESTROY, "onDestroy()");
            LifeCycleIds[] arrlifeCycleIds = new LifeCycleIds[]{ON_CREATE, ON_NEW_INTENT, ON_START, ON_RESTART, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY};
            $VALUES = arrlifeCycleIds;
        }

        private LifeCycleIds(MeasurePerformance.PerformanceIds performanceIds, String string2) {
            super(string, n);
            this.mPerformanceIds = performanceIds;
            this.mLog = string2;
        }

        public static LifeCycleIds valueOf(String string) {
            return (LifeCycleIds)Enum.valueOf((Class)LifeCycleIds.class, (String)string);
        }

        public static LifeCycleIds[] values() {
            return (LifeCycleIds[])$VALUES.clone();
        }

        public String toString() {
            return this.mLog;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class StorageBroadcastReceiver
    extends BroadcastReceiver {
        final /* synthetic */ BaseActivity this$0;

        private StorageBroadcastReceiver(BaseActivity baseActivity) {
            this.this$0 = baseActivity;
        }

        /* synthetic */ StorageBroadcastReceiver(BaseActivity baseActivity,  var2_2) {
            super(baseActivity);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onReceive(Context context, Intent intent) {
            String string = intent.getAction();
            if (string.equals((Object)"android.intent.action.MEDIA_MOUNTED")) {
                this.this$0.notifyStorageStatusChanged(string, intent.getData());
                return;
            } else {
                if (string.equals((Object)"android.intent.action.MEDIA_UNMOUNTED")) {
                    this.this$0.notifyStorageStatusChanged(string, intent.getData());
                    return;
                }
                if (string.equals((Object)"android.intent.action.MEDIA_EJECT")) {
                    this.this$0.notifyStorageStatusChanged(string, intent.getData());
                    return;
                }
                if (!string.equals((Object)"android.intent.action.MEDIA_SCANNER_FINISHED")) return;
                {
                    this.this$0.notifyStorageStatusChanged(string, intent.getData());
                    return;
                }
            }
        }
    }

    public static interface StorageEventListener {
        public void onMediaScanFinished();

        public void onStorageCheckRequested(String var1, String var2);
    }

}

