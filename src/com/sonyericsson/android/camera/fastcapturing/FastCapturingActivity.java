/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.KeyguardManager
 *  android.content.ActivityNotFoundException
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.res.Configuration
 *  android.hardware.Camera
 *  android.hardware.Camera$ErrorCallback
 *  android.hardware.Camera$Parameters
 *  android.media.MediaPlayer
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Parcelable
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.CameraButtonIntentReceiver;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.LaunchConditions;
import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.PlatformDependencyResolver;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.android.camera.fastcapturing.view.BaseFastViewFinder;
import com.sonyericsson.android.camera.fastcapturing.view.FastViewFinder;
import com.sonyericsson.android.camera.mediasaving.BurstSavingTaskManager;
import com.sonyericsson.android.camera.util.LocalGoogleAnalyticsUtil;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettingNotifyListener;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StorageAutoSwitchController;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StorageUtil;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagSettingListener;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.thermal.ThermalAlertReceiver;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.OneShotUtility;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.cameracommon.viewfinder.ViewFinder;
import com.sonyericsson.cameracommon.viewfinder.ViewFinderInterface;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.media.utility.AudioResourceChecker;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentificationUtil;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.ObserveWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import java.io.IOException;

/*
 * Failed to analyse overrides
 */
public class FastCapturingActivity
extends ExtendedActivity
implements DialogInterface.OnCancelListener {
    public static final int AUTO_OFF_TIME_OUT_DURATION = 30000;
    public static final String EXTRA_IS_INTERRUPT_PROCESSING_OCCURRED = "com.sonymobile.android.camera.extra.IS_INTERRUPT_PROCESSING_OCCURRED";
    public static final String INTENT_ACTION_MAIN_FAST_CAPTURING_PHOTO_CAMERA = "com.sonyericsson.android.camera.action.MAIN_FAST_CAPTURING_PHOTO_CAMERA";
    public static final String INTENT_ACTION_MAIN_FAST_CAPTURING_VIDEO_CAMERA = "com.sonyericsson.android.camera.action.FRONT_FAST_CAPTURING_VIDEO_CAMERA";
    public static final String INTENT_SUBJECT_CANCEL = "cancel";
    public static final String INTENT_SUBJECT_PAUSED = "activity-paused";
    public static final String INTENT_SUBJECT_PREPARE = "prepare";
    public static final String INTENT_SUBJECT_RESUMED = "activity-resumed";
    public static final String INTENT_SUBJECT_START = "start";
    public static final String INTENT_SUBJECT_START_SECURE = "start-secure";
    public static final String INTENT_SUBJECT_START_WITH_SECURITY_SETTING_DIALOG = "security-setting";
    public static final String KEY_FAST_CAPTURING_CONFIRMATION_COUNT = "KEY_FAST_CAPTURING_CONFIRMATION_COUNT";
    public static final String KEY_SECURITY_CONFIRMATION_IS_DONE = "KEY_SECURITY_CONFIRMATION_IS_DONE";
    public static final int REQUEST_FINISH_OWN_SELF_DELAY_TIME = 300;
    public static final int SETUP_DEVICE_SETUP_WAIT_TIME = 100;
    public static final int SETUP_LAZY_EXECUTION_WAIT_TIME = 200;
    public static final int SOUND_AF_SUCCESS = 0;
    public static final int SOUND_SELF_TIMER_INSTANT = 4;
    public static final int SOUND_SELF_TIMER_LONG = 2;
    public static final int SOUND_SELF_TIMER_SHORT = 3;
    public static final int SOUND_SHUTTER = 1;
    private static String TAG = FastCapturingActivity.class.getSimpleName();
    private static final CameraDeviceHandler sCameraDeviceHandler;
    private static CapturingMode sCurrentCapturingMode;
    private static FastCapture sFastCaptureSetting;
    private static CapturingMode sTargetCapturingMode;
    private CameraDeviceHandler mCameraDeviceHandler;
    boolean mIsInterruptProcessingOccurred = false;
    private boolean mIsSecurePhotoLaunchedByIntent = false;
    private boolean mIsSetContentView = false;
    protected KeyEventTranslator mKeyEventTranslator;
    private MediaPlayer mMediaPlayer;
    private PostDeviceInitializationTask mPostDeviceInitializationTask = null;
    private Handler mPostEventHandler;
    private ScreenOffReceiver mScreenOffReceiver;
    protected StateMachine mStateMachine;
    private BaseFastViewFinder mViewFinder;
    private WearableBridgeClient mWearableBridgeClient = null;
    private ObserveWearableInterface.LifeCycleObserver mWearableBridgeLifeCycleObserver = null;
    private ObserveWearableInterface.PhotoEventObserver mWearableBridgePhotoEventObserver = null;
    private ObserveWearableInterface.VideoEventObserver mWearableBridgeVideoEventObserver = null;

    static {
        sFastCaptureSetting = null;
        sCurrentCapturingMode = null;
        sTargetCapturingMode = null;
        sCameraDeviceHandler = new CameraDeviceHandler();
    }

    public FastCapturingActivity() {
        CameraLogger.setAppName("SemcCameraUI");
    }

    static /* synthetic */ GeotagManager access$1000(FastCapturingActivity fastCapturingActivity) {
        return fastCapturingActivity.mGeotagManager;
    }

    static /* synthetic */ GeotagManager access$1100(FastCapturingActivity fastCapturingActivity) {
        return fastCapturingActivity.mGeotagManager;
    }

    static /* synthetic */ GeotagManager access$900(FastCapturingActivity fastCapturingActivity) {
        return fastCapturingActivity.mGeotagManager;
    }

    private void checkAudioAvailable() {
        if (!(sFastCaptureSetting == null || sFastCaptureSetting != FastCapture.LAUNCH_AND_RECORDING || AudioResourceChecker.isAudioResourceAvailable((BaseActivity)this))) {
            this.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
            sFastCaptureSetting = FastCapture.LAUNCH_ONLY;
        }
    }

    private void checkSecureIntent(Intent intent) {
        this.mIsSecurePhotoLaunchedByIntent = false;
        if (intent != null && "android.media.action.STILL_IMAGE_CAMERA_SECURE".equals((Object)intent.getAction())) {
            this.mIsSecurePhotoLaunchedByIntent = true;
            FastCapturingActivity.requestReloadFastCaptureSetting();
            CameraButtonIntentReceiver.enableStartUpNotificationFlag();
        }
    }

    protected static CapturingMode getCurrentCapturingMode() {
        return sCurrentCapturingMode;
    }

    private void getDownAll() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_FINALIZE, new Object[0]);
        this.mStateMachine.setCameraDeviceHandler(null);
        this.mStateMachine.setViewFinder(null);
        this.mStorageManager.removeStorageListener(this.mStateMachine);
        this.mViewFinder.setStateMachine(null);
        this.mViewFinder.setCameraDevice(null);
        this.mStateMachine = null;
        this.mViewFinder = null;
        this.mPostEventHandler = null;
        this.releaseMediaPlayer();
        this.mStorageManager.release();
        this.mGeotagManager.release();
        this.unregisterReceiver((BroadcastReceiver)this.mScreenOffReceiver);
        this.mScreenOffReceiver = null;
        this.mIsSetContentView = false;
        this.mKeyEventTranslator = null;
    }

    protected static FastCapture getFastCaptureSetting() {
        return sFastCaptureSetting;
    }

    protected static CapturingMode getTargetCapturingMode() {
        return sTargetCapturingMode;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void initGoogleAnalytics() {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$activity$BaseActivity$LaunchedBy[this.mLaunchConditions.getLaunchedBy().ordinal()]) {
            case 1: {
                GoogleAnalyticsUtil.setLaunchedFrom(BaseActivity.LaunchedBy.HISTORY, null, false, false);
            }
        }
        CapturingMode capturingMode = sCurrentCapturingMode == null ? CapturingMode.SCENE_RECOGNITION : sCurrentCapturingMode;
        LocalGoogleAnalyticsUtil.getInstance().sendView(capturingMode, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void loadDestinationToSave() {
        SaveDestination saveDestination = (SaveDestination)this.getCommonSettings().get(CommonSettingKey.SAVE_DESTINATION);
        int n = .$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$SaveDestination[saveDestination.ordinal()];
        DestinationToSave destinationToSave = null;
        switch (n) {
            case 1: {
                destinationToSave = DestinationToSave.EMMC;
            }
            default: {
                break;
            }
            case 2: {
                destinationToSave = DestinationToSave.SDCARD;
            }
        }
        if (destinationToSave != null) {
            this.mStorageManager.setCurrentStorage(destinationToSave.getType());
        }
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    private void notifyActivityState(String string) {
        Intent intent = new Intent("android.intent.action.CAMERA_BUTTON", null);
        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)new KeyEvent(0, 27));
        intent.putExtra("android.intent.extra.SUBJECT", string);
        intent.putExtra("com.sonymobile.android.camera.extra.IS_INTERRUPT_PROCESSING_OCCURRED", this.mIsInterruptProcessingOccurred);
        intent.addFlags(268435456);
        intent.setPackage(this.getPackageName());
        this.sendOrderedBroadcast(intent, null);
    }

    private void playStartUpNotificationSoundIfRequired() {
        FastCapture fastCapture = (FastCapture)this.getCommonSettings().get(CommonSettingKey.FAST_CAPTURE);
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[fastCapture.ordinal()]) {
            default: {
                CameraButtonIntentReceiver.releaseStartUpNotificationFlag();
                return;
            }
            case 2: 
            case 5: 
        }
        CameraButtonIntentReceiver.notifyStartUpIfRequired((Context)this);
    }

    private void postViewRelatedLazyInitializationTaskExecute() {
        if (sFastCaptureSetting == null) {
            this.loadFastCapturingLaunchSetting();
        }
        if (sFastCaptureSetting == null) {
            sFastCaptureSetting = FastCapture.LAUNCH_ONLY;
        }
        this.mPostDeviceInitializationTask = new PostDeviceInitializationTask(this);
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[sFastCaptureSetting.ordinal()]) {
            default: {
                this.postDelayedEvent((Runnable)this.mPostDeviceInitializationTask, 100);
                return;
            }
            case 2: 
        }
        this.postEvent((Runnable)this.mPostDeviceInitializationTask);
    }

    private void releaseMediaPlayer() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void releaseWearableFramework() {
        if (this.mWearableBridgeClient != null) {
            this.mWearableBridgeClient.release();
            this.mWearableBridgeClient = null;
        }
        this.mWearableBridgeLifeCycleObserver = null;
        this.mWearableBridgePhotoEventObserver = null;
        this.mWearableBridgeVideoEventObserver = null;
    }

    public static void requestReloadFastCaptureSetting() {
        sFastCaptureSetting = null;
        sCurrentCapturingMode = null;
        sTargetCapturingMode = null;
    }

    protected static void setCurrentCapturingMode(CapturingMode capturingMode) {
        sCurrentCapturingMode = capturingMode;
    }

    protected static void setFastCaptureSetting(FastCapture fastCapture) {
        sFastCaptureSetting = fastCapture;
    }

    protected static void setTargetCapturingMode(CapturingMode capturingMode) {
        sTargetCapturingMode = capturingMode;
    }

    private void setUpAll() {
        this.setupCoreInstance();
        this.mStorageController = new StorageAutoSwitchController((StorageAutoSwitchController.StorageAutoSwitchListener)this.mViewFinder, (ViewFinderInterface)this.mViewFinder);
        this.mMessagePopup.setStorageDialogStateListener((StorageController.StorageDialogStateListener)((ViewFinder)this.mViewFinder));
        this.mStorageController.setMessegePopup(this.mMessagePopup);
        this.mStorageManager = new CameraStorageManager((Activity)this, this.mStorageController);
        DestinationToSave.setMountPoint((Context)this, StorageUtil.getMountedPaths((Context)this));
        this.mStorageController.setStorage(StorageUtil.getPathFromType(StorageManager.StorageType.EXTERNAL_CARD, (Context)this), 0);
        this.mStorageController.setStorage(StorageUtil.getPathFromType(StorageManager.StorageType.INTERNAL, (Context)this), 1);
        if (!(this.getResources().getConfiguration().orientation != 2 || this.mIsSetContentView)) {
            this.mViewFinder.setContentView();
            this.mIsSetContentView = true;
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_INITIALIZE, new Object[0]);
        this.mSavingTaskManager = new BurstSavingTaskManager((BaseActivity)this);
        this.mGeotagManager = new GeotagManager((Context)this);
        this.mPostEventHandler = new Handler();
        this.mMediaPlayer = new MediaPlayer();
        this.mStorageManager.addStorageListener(this.mStateMachine);
        this.mScreenOffReceiver = new ScreenOffReceiver(this);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        this.registerReceiver((BroadcastReceiver)this.mScreenOffReceiver, intentFilter);
        this.mKeyEventTranslator = new KeyEventTranslator(this.getCommonSettings());
    }

    private void setupCoreInstance() {
        this.mStateMachine = new StateMachine(this);
        this.mViewFinder = this.createViewFinder(this);
        this.mStateMachine.setCameraDeviceHandler(this.getCameraDevice());
        this.mStateMachine.setViewFinder(this.mViewFinder);
        this.mViewFinder.setStateMachine(this.mStateMachine);
        this.mViewFinder.setCameraDevice(this.getCameraDevice());
    }

    private void setupWearableFramework() {
        this.mWearableBridgeLifeCycleObserver = new WearableBridgeLifeCycleObserver(this, null);
        this.mWearableBridgePhotoEventObserver = new WearableBridgePhotoEventObserver(this, null);
        this.mWearableBridgeVideoEventObserver = new WearableBridgeVideoEventObserver(null);
        this.mWearableBridgeClient = new WearableBridgeClient((Activity)this, new Handler(), this.mWearableBridgeLifeCycleObserver, this.mWearableBridgePhotoEventObserver, this.mWearableBridgeVideoEventObserver);
    }

    public static CameraDeviceHandler staticCameraDevice() {
        return sCameraDeviceHandler;
    }

    public void abort() {
        this.abort(true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void abort(boolean bl) {
        if (this.mStateMachine == null) return;
        if (this.mStateMachine.canApplicationBeFinished()) {
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, new Object[0]);
            if (!bl) return;
            if (this.isDeviceInSecurityLock()) {
                this.finish();
                return;
            }
            if (this.isOneShotPhotoSecure()) {
                this.setResult(0);
                this.finish();
                return;
            }
            this.requestSuspend();
            return;
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_BACK, new Object[0]);
    }

    protected void cancelCheckAutoUploadSetting() {
        AutoUploadSettings.getInstance().cancel((Context)this);
    }

    public void cancelDelayedEvent(Runnable runnable) {
        if (this.mPostEventHandler != null) {
            this.mPostEventHandler.removeCallbacks(runnable);
        }
    }

    protected void confirmLocationService() {
        if (FastCapturingActivity.getFastCaptureSetting() == FastCapture.LAUNCH_ONLY) {
            this.mGeotagManager.confirmLocationService((BaseActivity)this, this.mCommonSettings, new GeotagSettingListener(){

                @Override
                public void onSet(boolean bl) {
                }
            });
        }
    }

    protected BaseFastViewFinder createViewFinder(FastCapturingActivity fastCapturingActivity) {
        return new FastViewFinder((Context)fastCapturingActivity);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void finishSecureOneShot(StoreDataResult storeDataResult) {
        if (storeDataResult.isSuccess()) {
            this.setResult(-1, OneShotUtility.createResultIntent((Activity)this, storeDataResult.uri, storeDataResult.savingRequest.common.mimeType, storeDataResult.savingRequest.common.orientation));
        } else {
            this.setResult(0);
        }
        this.finish();
    }

    public CameraDeviceHandler getCameraDevice() {
        return sCameraDeviceHandler;
    }

    public FastCapture getCurrentLaunchMode() {
        return sFastCaptureSetting;
    }

    protected ParamSharedPrefWrapper getParamSharedPrefWrapper() {
        return new ParamSharedPrefWrapper((Context)this, "com.sonyericsson.android.camera.shared_preferences", "0.0.0");
    }

    public WearableBridgeClient getWearableBridge() {
        if (this.isOneShotPhotoSecure()) {
            return null;
        }
        return this.mWearableBridgeClient;
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

    public boolean isDirectCaptureWithStartUpEnabled() {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[sFastCaptureSetting.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
        }
        return true;
    }

    public boolean isDualStorageAvailable() {
        return true;
    }

    public boolean isMenuAvailable() {
        return this.mStateMachine.isMenuAvailable();
    }

    public boolean isRecording() {
        if (this.mStateMachine != null) {
            return this.mStateMachine.isRecording();
        }
        return false;
    }

    public boolean isSecurePhotoLaunchedByIntent() {
        return this.mIsSecurePhotoLaunchedByIntent;
    }

    protected boolean isVideoSupported() {
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void loadFastCapturingLaunchSetting() {
        if (this.isOneShotPhotoSecure()) {
            FastCapturingActivity.setFastCaptureSetting(FastCapture.LAUNCH_ONLY);
        } else if (FastCapturingActivity.getFastCaptureSetting() == null) {
            FastCapturingActivity.setFastCaptureSetting((FastCapture)this.getCommonSettings().get(CommonSettingKey.FAST_CAPTURE));
        }
        if (FastCapturingActivity.getCurrentCapturingMode() != null) {
            this.mStateMachine.setCurrentCapturingMode(FastCapturingActivity.getCurrentCapturingMode());
        }
        if (FastCapturingActivity.getTargetCapturingMode() != null) {
            this.mStateMachine.setTargetCapturingMode(FastCapturingActivity.getTargetCapturingMode());
        }
    }

    public void notifyStateBlockedToWearable() {
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.BLOCKED);
            this.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.BLOCKED);
        }
    }

    public void notifyStateIdleToWearable() {
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.IDLE);
            this.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.IDLE);
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.finish();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!(configuration.orientation != 2 || this.mViewFinder == null || this.mIsSetContentView)) {
            this.mViewFinder.setContentView();
            this.mIsSetContentView = true;
        }
    }

    public void onCreate(Bundle bundle) {
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STARTUP_TIME, true);
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_CREATE);
        super.onCreate(null);
        super.checkSecureIntent(this.getIntent());
        this.mThermalAlertReceiver.onCreate();
        super.setupWearableFramework();
        super.setUpAll();
        this.mLaunchConditions.setLaunchedBy(BaseActivity.LaunchedBy.INTENT);
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_CREATE);
    }

    public void onDestroy() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_DESTROY);
        super.onDestroy();
        this.getDownAll();
        this.mThermalAlertReceiver.onDestroy();
        this.releaseWearableFramework();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_DESTROY);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        KeyEventTranslator.TranslatedKeyCode translatedKeyCode = this.mKeyEventTranslator.translateKeyCodeOnDown(n);
        if (keyEvent.getRepeatCount() > 0 && translatedKeyCode != KeyEventTranslator.TranslatedKeyCode.VOLUME) {
            return true;
        }
        if (this.isFinishing()) return true;
        this.restartAutoOffTimer();
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            case 7: 
            case 8: 
            case 9: {
                return true;
            }
            default: {
                return super.onKeyDown(n, keyEvent);
            }
            case 1: {
                StateMachine.TransitterEvent transitterEvent = n == 24 ? StateMachine.TransitterEvent.EVENT_KEY_ZOOM_IN_DOWN : StateMachine.TransitterEvent.EVENT_KEY_ZOOM_OUT_DOWN;
                this.mStateMachine.sendEvent(transitterEvent, new Object[0]);
                return true;
            }
            case 2: {
                if (this.mStateMachine.isInModeLessRecording()) return true;
                return false;
            }
            case 3: {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
                return true;
            }
            case 4: {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
                return true;
            }
            case 5: 
            case 6: {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
                return true;
            }
            case 10: 
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
        return true;
    }

    /*
     * Exception decompiling
     */
    public boolean onKeyUp(int var1, KeyEvent var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[CASE]], but top level block is 3[SWITCH]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:392)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:444)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2783)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:764)
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

    protected void onNewIntent(Intent intent) {
        this.mLaunchConditions.setLaunchedBy(BaseActivity.LaunchedBy.INTENT);
        super.checkSecureIntent(intent);
    }

    public void onNotifyThermalNormal() {
        if (this.mStateMachine != null) {
            this.mStateMachine.onNotifyThermalStatus(false);
        }
    }

    public void onNotifyThermalWarning() {
        if (this.mStateMachine != null) {
            this.mStateMachine.onNotifyThermalStatus(true);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onPause() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_PAUSE);
        FastCapturingActivity.setCurrentCapturingMode(this.mStateMachine.getCurrentCapturingMode());
        FastCapturingActivity.setTargetCapturingMode(this.mStateMachine.getTargetCapturingMode());
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, new Object[0]);
        this.mStateMachine.removeChangeCameraModeTask();
        this.notifyActivityState("activity-paused");
        if (this.mCameraDeviceHandler != null) {
            this.mCameraDeviceHandler.stopFaceIdService();
            this.mCameraDeviceHandler.releaseCameraInstance();
            this.mCameraDeviceHandler.setStateMachine(null);
            this.mCameraDeviceHandler = null;
        } else {
            this.getCameraDevice().releaseCameraInstance();
        }
        this.mStorageManager.pause();
        this.mThermalAlertReceiver.onPause();
        this.mGeotagManager.releaseResource();
        this.cancelCheckAutoUploadSetting();
        this.mLaunchConditions.setLaunchedBy(BaseActivity.LaunchedBy.UNKNOWN);
        if (this.mPostDeviceInitializationTask != null) {
            this.cancelDelayedEvent((Runnable)this.mPostDeviceInitializationTask);
        }
        this.mPostEventHandler = null;
        this.mStateMachine.releaseContentsViewController();
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getLifeCycleNotifier().onPause();
        }
        super.onPause();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_PAUSE);
    }

    public void onReachHighTemperature(boolean bl) {
        if (this.mStateMachine != null) {
            GoogleAnalyticsUtil.sendThermalEvent(bl, this.isRecording());
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, new Object[0]);
            this.mStateMachine.removeChangeCameraModeTask();
            this.mStateMachine.removeStartRecordingTask();
        }
        if (this.mCameraDeviceHandler != null) {
            this.mCameraDeviceHandler.releaseCameraInstance();
            this.mCameraDeviceHandler.setStateMachine(null);
            this.mCameraDeviceHandler = null;
            return;
        }
        this.getCameraDevice().releaseCameraInstance();
    }

    public void onRestart() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_RESTART);
        super.onRestart();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_RESTART);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onResume() {
        boolean bl = true;
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STARTUP_TIME, bl);
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_RESUME);
        super.onResume();
        if (this.mPostEventHandler == null) {
            this.mPostEventHandler = new Handler();
        }
        if (this.getWearableBridge() != null) {
            this.getWearableBridge().getLifeCycleNotifier().onResume();
        }
        this.mThermalAlertReceiver.onResume();
        this.loadFastCapturingLaunchSetting();
        this.checkAudioAvailable();
        this.loadDestinationToSave();
        this.confirmLocationService();
        this.mGeotagManager.updateLocation(this.mCommonSettings.get(CommonSettingKey.GEO_TAG));
        this.notifyActivityState("activity-resumed");
        this.mForceSound.resume();
        this.mViewFinder.requestInflate(this.getLayoutInflater());
        StorageController storageController = this.mStorageController;
        if (!(this.mLaunchConditions.isOneShot() && this.getExtraOutput() != null)) {
            bl = false;
        }
        storageController.setOneShotMode(bl);
        this.initGoogleAnalytics();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_RESUME);
    }

    public void onStart() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_START);
        super.onStart();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_START);
    }

    public void onStop() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_STOP);
        super.onStop();
        this.mIsInterruptProcessingOccurred = false;
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_STOP);
    }

    protected void onUserLeaveHint() {
        this.mIsInterruptProcessingOccurred = true;
    }

    public void pauseCameraView() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, new Object[0]);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void playSound(int n) {
        MediaPlayer mediaPlayer;
        if (this.mMediaPlayer == null) {
            return;
        }
        MediaPlayer mediaPlayer2 = mediaPlayer = this.mMediaPlayer;
        // MONITORENTER : mediaPlayer2
        String string = null;
        switch (n) {
            case 0: {
                string = ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_AF_SUCCESS, ShutterSound.SOUND1);
                break;
            }
            case 2: {
                string = ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_SELFTIMER_10SEC, ShutterSound.SOUND1);
                break;
            }
            case 3: {
                String string2;
                string = string2 = ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_SELFTIMER_2SEC, ShutterSound.SOUND1);
                break;
            }
            case 4: {
                string = null;
            }
        }
        if (string == null) {
            // MONITOREXIT : mediaPlayer2
            return;
        }
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(string);
            this.mMediaPlayer.setAudioStreamType(7);
            this.mMediaPlayer.setVolume(7.0f, 7.0f);
            this.mMediaPlayer.prepare();
        }
        catch (IOException var4_6) {
            this.mMediaPlayer.reset();
        }
        this.mMediaPlayer.start();
        // MONITOREXIT : mediaPlayer2
    }

    public void playSound(SelfTimer selfTimer) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[selfTimer.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                this.playSound(2);
                return;
            }
            case 2: {
                this.playSound(3);
                return;
            }
            case 3: 
        }
        this.playSound(4);
    }

    public void postDelayedEvent(Runnable runnable, long l) {
        if (this.mPostEventHandler != null) {
            this.mPostEventHandler.postDelayed(runnable, l);
        }
    }

    public void postEvent(Runnable runnable) {
        if (this.mPostEventHandler != null) {
            this.mPostEventHandler.post(runnable);
        }
    }

    public void prepareCameraDeviceHandler(Context context, FastCapture fastCapture, int n) {
        switch (this.getCameraDevice().getCameraDeviceStatus()) {
            default: {
                return;
            }
            case 0: 
        }
        this.getCameraDevice().startCameraOpen(context, n, fastCapture, this.isOneShotPhotoSecure());
    }

    protected void requestCheckAutoUploadSetting() {
        AutoUploadSettings.getInstance().request((Context)this, new AutoUploadSettingCheckCallback(null));
    }

    public void requestFinishCameraActivity() {
        this.sendBroadcast(new Intent("com.sonyericsson.android.camera.intent.action.FINISH_CAMERAACTIVITY"));
    }

    public void requestLaunchAdvancedCameraApplication(int n) {
        if (this.mStateMachine.canApplicationBeFinished()) {
            CapturingMode capturingMode = this.mStateMachine.getCurrentCapturingMode();
            if (capturingMode.isFront() && PlatformDependencyResolver.isSceneRecognitionSupported(this.mCameraDeviceHandler.getLatestCachedParameters())) {
                capturingMode = CapturingMode.SUPERIOR_FRONT;
            }
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PAUSE, new Object[0]);
            this.mPostEventHandler.post((Runnable)new RequestLaunchAdvancedCameraTask(n, capturingMode));
        }
    }

    public void requestPostLazyInitializationTaskExecute() {
        this.postViewRelatedLazyInitializationTaskExecute();
    }

    public void requestSuspend() {
        if (!this.moveTaskToBack(true)) {
            this.finish();
        }
    }

    public void resumeAll() {
        if (sFastCaptureSetting == FastCapture.OFF) {
            if (this.isSecurePhotoLaunchedByIntent()) {
                this.updateLaunchMode(FastCapture.LAUNCH_ONLY);
            }
        } else {
            StateMachine stateMachine = this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_RESUME;
            Object[] arrobject = new Object[]{sFastCaptureSetting};
            stateMachine.sendEvent(transitterEvent, arrobject);
            this.prepareCameraDeviceHandler((Context)this, sFastCaptureSetting, this.mStateMachine.getTargetCameraId());
            this.mCameraDeviceHandler = this.getCameraDevice();
            this.mCameraDeviceHandler.initialize();
            this.mCameraDeviceHandler.setStateMachine(this.mStateMachine);
            this.mCameraDeviceHandler.setErrorCallback((Camera.ErrorCallback)new CameraErrorCallbackImpl(this, null));
            if (this.mCameraDeviceHandler.isCameraDisabled()) {
                this.showCameraNotAvailableError();
            }
            this.enableOrientation();
            this.requestCheckAutoUploadSetting();
            this.mSavingTaskManager.onResume();
            return;
        }
        CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] sFastCaptureSetting : FastCapture.OFF ");
        this.showCameraNotAvailableError();
    }

    public void resumeAll(boolean bl) {
        this.resumeAll();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDestinationToSave(DestinationToSave destinationToSave) {
        this.mStorageManager.setCurrentStorage(destinationToSave.getType());
        this.mStorageManager.requestCheckAll();
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$DestinationToSave[destinationToSave.ordinal()]) {
            case 1: {
                this.getCommonSettings().set(SaveDestination.EMMC);
            }
            default: {
                break;
            }
            case 2: {
                this.getCommonSettings().set(SaveDestination.SDCARD);
            }
        }
        this.getCommonSettings().setSelectability(CommonSettingKey.SAVE_DESTINATION, this.getStorageManager().isToggledStorageReady());
    }

    public boolean shouldAddOnlyNewContent() {
        return this.mIsSecurePhotoLaunchedByIntent;
    }

    public void showCameraNotAvailableError() {
        this.showCameraNotAvailableError(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void showCameraNotAvailableError(boolean bl) {
        boolean bl2 = this.mCameraDeviceHandler != null && this.mCameraDeviceHandler.isCameraDisabled();
        this.getMessagePopup().showCameraNotAvailableError(bl2, bl);
    }

    public boolean startAutoOffTimer() {
        return this.startAutoOffTimer(30000);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void stopPlayingSound() {
        MediaPlayer mediaPlayer;
        if (this.mMediaPlayer == null) {
            return;
        }
        MediaPlayer mediaPlayer2 = mediaPlayer = this.mMediaPlayer;
        synchronized (mediaPlayer2) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.reset();
            }
            return;
        }
    }

    public void terminateApplication() {
        this.finish();
    }

    public void updateLaunchMode(FastCapture fastCapture) {
        sFastCaptureSetting = fastCapture;
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
    private class CameraErrorCallbackImpl
    implements Camera.ErrorCallback {
        final /* synthetic */ FastCapturingActivity this$0;

        private CameraErrorCallbackImpl(FastCapturingActivity fastCapturingActivity) {
            this.this$0 = fastCapturingActivity;
        }

        /* synthetic */ CameraErrorCallbackImpl(FastCapturingActivity fastCapturingActivity,  var2_2) {
            super(fastCapturingActivity);
        }

        public void onError(int n, Camera camera) {
            if (this.this$0.mCameraDeviceHandler != null) {
                this.this$0.mCameraDeviceHandler.releaseCameraOnError();
            }
            CameraLogger.e(TAG, "Camera error occurred. Error code = " + n);
            this.this$0.getMessagePopup().showErrorUncancelable(2131361871, 2131361905, false);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class LazyInitializationTask
    implements Runnable {
        private final FastCapturingActivity mActivity;

        LazyInitializationTask(FastCapturingActivity fastCapturingActivity2) {
            this.mActivity = fastCapturingActivity2;
        }

        private void retry() {
            if (FastCapturingActivity.this.mPostEventHandler != null) {
                FastCapturingActivity.this.postDelayedEvent((Runnable)new LazyInitializationTask(this.mActivity), 200);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void run() {
            if (!(FastCapturingActivity.this.mStateMachine != null && FastCapturingActivity.this.mCameraDeviceHandler != null && FastCapturingActivity.this.mViewFinder != null && FastCapturingActivity.this.mViewFinder.isHeadUpDesplayReady())) {
                this.retry();
                return;
            }
            if (!FastCapturingActivity.this.mStateMachine.canCurrentStateHandleAsynchronizedTask()) {
                this.retry();
                return;
            }
            FastCapturingActivity.this.requestFinishCameraActivity();
            FaceIdentificationUtil.notifyCameraLaunch((Context)FastCapturingActivity.this);
            FastCapturingActivity.this.mCameraDeviceHandler.startFaceIdService((Activity)this.mActivity);
            FastCapturingActivity.this.playStartUpNotificationSoundIfRequired();
            FastCapturingActivity.this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, new Object[0]);
            if (sFastCaptureSetting == null) {
                FastCapturingActivity.this.loadFastCapturingLaunchSetting();
            }
            if (sFastCaptureSetting == null) {
                sFastCaptureSetting = FastCapture.LAUNCH_ONLY;
            }
            switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[sFastCaptureSetting.ordinal()]) {
                default: {
                    return;
                }
                case 1: 
                case 2: 
                case 3: {
                    FastCapturingActivity.this.updateLaunchMode(FastCapture.LAUNCH_ONLY);
                    if (FastCapturingActivity.this.mCameraDeviceHandler.getLatestCachedParameters() == null || FastCapturingActivity.this.mCameraDeviceHandler.isCameraFront() || !FastCapturingActivity.this.isVideoSupported()) return;
                    {
                        AudioResourceChecker.checkAudioResourceAndShowErrorDialogIfNecessary((BaseActivity)FastCapturingActivity.this);
                        return;
                    }
                }
                case 4: 
            }
            FastCapturingActivity.this.mCameraDeviceHandler.setPhotoHdrSettings(Hdr.HDR_AUTO);
            FastCapturingActivity.this.updateLaunchMode(FastCapture.LAUNCH_ONLY);
        }
    }

    public static final class ModeName
    extends Enum<ModeName> {
        private static final /* synthetic */ ModeName[] $VALUES;
        public static final /* enum */ ModeName FAST_CAPTURING_CAMERA = new ModeName();
        public static final /* enum */ ModeName FAST_CAPTURING_VIDEO = new ModeName();

        static {
            ModeName[] arrmodeName = new ModeName[]{FAST_CAPTURING_CAMERA, FAST_CAPTURING_VIDEO};
            $VALUES = arrmodeName;
        }

        private ModeName() {
            super(string, n);
        }

        public static ModeName valueOf(String string) {
            return (ModeName)Enum.valueOf((Class)ModeName.class, (String)string);
        }

        public static ModeName[] values() {
            return (ModeName[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    class PostDeviceInitializationTask
    implements Runnable {
        private final FastCapturingActivity mActivity;

        PostDeviceInitializationTask(FastCapturingActivity fastCapturingActivity2) {
            this.mActivity = fastCapturingActivity2;
        }

        private void applySettingsForFastVideo() {
            FastCapturingActivity.this.mCameraDeviceHandler.prepareAdditionalFeatures(2, (ExtendedActivity)FastCapturingActivity.this);
        }

        private void retry() {
            if (FastCapturingActivity.this.mPostEventHandler != null) {
                FastCapturingActivity.this.postDelayedEvent((Runnable)new PostDeviceInitializationTask(this.mActivity), 100);
                return;
            }
            FastCapturingActivity.this.updateLaunchMode(FastCapture.LAUNCH_ONLY);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            if (FastCapturingActivity.this.mStateMachine == null || FastCapturingActivity.access$400(FastCapturingActivity.this) == null || FastCapturingActivity.access$500(FastCapturingActivity.this) == null || FastCapturingActivity.access$700() == null) {
                this.retry();
                return;
            }
            if (FastCapturingActivity.access$400(FastCapturingActivity.this).isPreScanOnGoing() || FastCapturingActivity.access$400(FastCapturingActivity.this).isPreCaptureOnGoing()) {
                this.retry();
                return;
            }
            var1_1 = FastCapturingActivity.access$400(FastCapturingActivity.this).getLatestCachedParameters();
            if (!FastCapturingActivity.this.mStateMachine.canCurrentStateHandleAsynchronizedTask()) {
                this.retry();
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[FastCapturingActivity.access$700().ordinal()]) {
                case 1: 
                case 2: 
                case 3: {
                    FastCapturingActivity.access$400(FastCapturingActivity.this).prepareAdditionalFeatures(1, (ExtendedActivity)FastCapturingActivity.this);
                    if (var1_1 != null) {
                        if (PlatformDependencyResolver.isFlashLightSupported(var1_1)) {
                            FastCapturingActivity.access$400(FastCapturingActivity.this).applyCameraLightSetting(1);
                        } else if (PlatformDependencyResolver.isPhotoLightSupported(var1_1)) {
                            // empty if block
                        }
                        FastCapturingActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, new Object[0]);
                    }
                }
                default: {
                    ** GOTO lbl29
                }
                case 4: 
            }
            if (var1_1 != null && PlatformDependencyResolver.isFlashLightSupported(var1_1)) {
                FastCapturingActivity.access$400(FastCapturingActivity.this).applyCameraLightSetting(1);
            }
            FastCapturingActivity.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, new Object[0]);
            if (var1_1 != null) {
                this.applySettingsForFastVideo();
            }
lbl29: // 4 sources:
            if (FastCapturingActivity.access$900(FastCapturingActivity.this) != null) {
                FastCapturingActivity.access$1000(FastCapturingActivity.this).setLocationAcquiredListener((LocationAcquiredListener)FastCapturingActivity.access$500(FastCapturingActivity.this));
                FastCapturingActivity.access$1100(FastCapturingActivity.this).notifyStatus();
            }
            FastCapturingActivity.this.postEvent((Runnable)new LazyInitializationTask(this.mActivity));
        }
    }

    /*
     * Failed to analyse overrides
     */
    class RequestLaunchAdvancedCameraTask
    implements Runnable {
        private static final String TAG = "RequestLaunchAdvancedCameraTask";
        private final int mCameraType;
        private final CapturingMode mCurrentMode;

        public RequestLaunchAdvancedCameraTask(int n, CapturingMode capturingMode) {
            this.mCameraType = n;
            this.mCurrentMode = capturingMode;
        }

        /*
         * Unable to fully structure code
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void run() {
            FastCapturingActivity.this.abort(false);
            var1_1 = new Intent();
            switch (this.mCameraType) lbl-1000: // 2 sources:
            {
                do {
                    default: lbl-1000: // 2 sources:
                    {
                        do {
                            var1_1.setClass(FastCapturingActivity.this.getApplicationContext(), (Class)CameraActivity.class);
                            var1_1.addFlags(268435456);
                            var1_1.putExtra("com.sonyericsson.android.camera3d.extra.requstadvancedsettingsdialogopen", true);
                            var1_1.putExtra("com.sonyericsson.android.camera.extra.launchedByFastCapturing", true);
                            var1_1.setAction(this.mCurrentMode.getValue());
                            if (CommonUtility.isActivityAvailable(FastCapturingActivity.this.getApplicationContext(), var1_1)) {
                                FastCapturingActivity.this.startActivity(var1_1);
                                FastCapturingActivity.this.overridePendingTransition(0, 0);
                            }
lbl16: // 4 sources:
                            do {
                                FastCapturingActivity.this.requestSuspend();
                                return;
                                break;
                            } while (true);
                            break;
                        } while (true);
                    }
                    break;
                } while (true);
                case 1: {
                    var1_1.setAction("android.media.action.STILL_IMAGE_CAMERA");
                    ** continue;
                }
                case 2: 
            }
            var1_1.setAction("android.media.action.VIDEO_CAMERA");
            ** while (true)
            catch (ActivityNotFoundException var8_2) {
                ** continue;
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    static class ScreenOffReceiver
    extends BroadcastReceiver {
        private static final String TAG = "ScreenOffReceiver";
        private FastCapturingActivity mActivity;

        public ScreenOffReceiver(FastCapturingActivity fastCapturingActivity) {
            this.mActivity = fastCapturingActivity;
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals((Object)intent.getAction())) {
                this.mActivity.finish();
            }
        }
    }

    private class WearableBridgeLifeCycleObserver
    implements ObserveWearableInterface.LifeCycleObserver {
        final /* synthetic */ FastCapturingActivity this$0;

        private WearableBridgeLifeCycleObserver(FastCapturingActivity fastCapturingActivity) {
            this.this$0 = fastCapturingActivity;
        }

        /* synthetic */ WearableBridgeLifeCycleObserver(FastCapturingActivity fastCapturingActivity,  var2_2) {
            super(fastCapturingActivity);
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onResume() {
            if (this.this$0.mStateMachine.canHandleWearableCaptureRequest()) {
                this.this$0.notifyStateIdleToWearable();
                return;
            }
            this.this$0.notifyStateBlockedToWearable();
        }
    }

    private class WearableBridgePhotoEventObserver
    implements ObserveWearableInterface.PhotoEventObserver {
        final /* synthetic */ FastCapturingActivity this$0;

        private WearableBridgePhotoEventObserver(FastCapturingActivity fastCapturingActivity) {
            this.this$0 = fastCapturingActivity;
        }

        /* synthetic */ WearableBridgePhotoEventObserver(FastCapturingActivity fastCapturingActivity,  var2_2) {
            super(fastCapturingActivity);
        }

        @Override
        public void onPhotoCaptureRequested() {
            if (this.this$0.mStateMachine.canHandleWearableCaptureRequest()) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_UP, new Object[0]);
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_UP, new Object[0]);
                return;
            }
            this.this$0.getWearableBridge().getPhotoStateNotifier().onCaptureFailed();
        }
    }

    private static class WearableBridgeVideoEventObserver
    implements ObserveWearableInterface.VideoEventObserver {
        private WearableBridgeVideoEventObserver() {
        }

        /* synthetic */ WearableBridgeVideoEventObserver( var1) {
        }

        @Override
        public void onStartVideoRecRequested() {
        }

        @Override
        public void onStopVideoRecRequested() {
        }
    }

}

