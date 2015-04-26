/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.KeyguardManager
 *  android.content.Context
 *  android.content.Intent
 *  android.media.AudioManager
 *  android.view.KeyEvent
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 */
package com.sonyericsson.android.camera.smartcover;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.android.camera.fastcapturing.view.BaseFastViewFinder;
import com.sonyericsson.android.camera.smartcover.view.SmartCoverViewFinder;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import com.sonymobile.coversupport.CoverModeHandler;
import com.sonymobile.coversupport.OnCoverModeChangedListener;

/*
 * Failed to analyse overrides
 */
public class SmartCoverActivity
extends FastCapturingActivity
implements OnCoverModeChangedListener {
    private static String TAG = SmartCoverActivity.class.getSimpleName();
    private static final CameraDeviceHandler sCameraDeviceHandler = new CameraDeviceHandler();
    private AudioManager mAudioManager;
    private CoverModeHandler mCoverModeHandler = null;
    private CoverModeHandler.WindowInfo mCoverModeSizeInPixel = null;
    public boolean mShouldStartFastCapture = true;
    private SmartCoverViewFinder mSmartCoverViewFinder = null;

    public static CameraDeviceHandler staticCameraDevice() {
        throw new UnsupportedOperationException("This method is unsupported.");
    }

    protected void confirmLocationService() {
    }

    protected BaseFastViewFinder createViewFinder(FastCapturingActivity fastCapturingActivity) {
        this.mSmartCoverViewFinder = new SmartCoverViewFinder((Context)fastCapturingActivity);
        return this.mSmartCoverViewFinder;
    }

    public CameraDeviceHandler getCameraDevice() {
        return sCameraDeviceHandler;
    }

    public WearableBridgeClient getWearableBridge() {
        return null;
    }

    protected boolean isVideoSupported() {
        return false;
    }

    protected void loadFastCapturingLaunchSetting() {
        SmartCoverActivity.setFastCaptureSetting((FastCapture)FastCapture.LAUNCH_ONLY);
        if (SmartCoverActivity.getCurrentCapturingMode() != null) {
            this.mStateMachine.setCurrentCapturingMode(CapturingMode.SCENE_RECOGNITION);
        }
        if (SmartCoverActivity.getTargetCapturingMode() != null) {
            this.mStateMachine.setTargetCapturingMode(CapturingMode.SCENE_RECOGNITION);
        }
    }

    public void onCoverModeChanged(boolean bl) {
        if (bl) {
            this.mCoverModeSizeInPixel = this.mCoverModeHandler.getWindowInfo();
            if (this.mCoverModeSizeInPixel != null) {
                this.mSmartCoverViewFinder.setCoverModeSizeInPixel(this.mCoverModeSizeInPixel);
            }
            return;
        }
        if (this.mShouldStartFastCapture) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass((Context)this, (Class)FastCapturingActivity.class);
            if (((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                intent.putExtra("android.intent.extra.SUBJECT", "start-secure");
            }
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setFlags(268435456);
            this.startActivity(intent);
        }
        this.abort();
    }

    public void onDestroy() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_DESTROY);
        this.mSmartCoverViewFinder = null;
        super.onDestroy();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_DESTROY);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        KeyEventTranslator.TranslatedKeyCode translatedKeyCode = this.mKeyEventTranslator.translateKeyCodeOnDown(n);
        if (keyEvent.getRepeatCount() > 0 && translatedKeyCode != KeyEventTranslator.TranslatedKeyCode.VOLUME) {
            return true;
        }
        if (this.isFinishing()) return true;
        this.restartAutoOffTimer();
        int n2 = 1;
        if (this.mAudioManager.isMusicActive()) {
            n2 = 3;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            default: {
                return super.onKeyDown(n, keyEvent);
            }
            case 1: {
                if (n == 24) {
                    this.mAudioManager.adjustStreamVolume(n2, 1, 8);
                    return true;
                }
                if (n != 25) return true;
                this.mAudioManager.adjustStreamVolume(n2, -1, 8);
                return true;
            }
            case 2: {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
                return true;
            }
            case 3: {
                this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
                return true;
            }
            case 4: 
            case 5: 
        }
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_FOCUS_DOWN, new Object[0]);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_KEY_CAPTURE_DOWN, new Object[0]);
        return true;
    }

    public void onPause() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_PAUSE);
        super.onPause();
        this.mCoverModeSizeInPixel = null;
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_PAUSE);
    }

    public void onStart() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_START);
        super.onStart();
        this.mCoverModeHandler = new CoverModeHandler((Activity)this, (OnCoverModeChangedListener)this, true);
        this.mCoverModeHandler.start();
        this.onCoverModeChanged(this.mCoverModeHandler.isCoverModeActive());
        this.mAudioManager = (AudioManager)this.getSystemService("audio");
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_START);
    }

    public void onStop() {
        this.logLifeCycleIn(TAG, BaseActivity.LifeCycleIds.ON_STOP);
        this.mCoverModeHandler.stop();
        this.mCoverModeHandler = null;
        super.onStop();
        this.logLifeCycleOut(TAG, BaseActivity.LifeCycleIds.ON_STOP);
    }

    public void setShouldStartFastCapture(boolean bl) {
        this.mShouldStartFastCapture = bl;
    }

    public boolean shouldAddOnlyNewContent() {
        return true;
    }

    public void showCameraNotAvailableError() {
        this.showCameraNotAvailableError(true);
    }

}

