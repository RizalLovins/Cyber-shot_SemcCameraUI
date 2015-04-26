/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Message
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.EventDispatcher;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.utility.CameraTimer;

public class Selftimer {
    private static final int AF_DELAY_DURATION = 0;
    private static final int SELF_TIMER_CAPTURE_DELAY_DURATION = 200;
    private static final int SELF_TIMER_CONTROL_INTERVAL_MILLIS = 250;
    private static final int[] SELF_TIMER_INTERVALS;
    private static final int[] SELF_TIMER_LEVELS;
    private static final int SELF_TIMER_LIGHT_FEEDBACK_DELAY = 300;
    private static final String TAG;
    private final int mCameraType;
    private final CameraFunctions mController;
    private final SelfTimerHandler mHandler = new SelfTimerHandler(null);
    private int mMaxDuration;
    private int mOffsetDuration;
    private ShutterToneGenerator.Type mSoundType;
    private ControllerEventSource mSource;
    private CameraTimer mTimer;

    static {
        TAG = Selftimer.class.getSimpleName();
        SELF_TIMER_LEVELS = new int[]{2000, 4000, 10000};
        SELF_TIMER_INTERVALS = new int[]{250, 500, 1000};
        AF_DELAY_DURATION = 3 * SELF_TIMER_INTERVALS[0];
    }

    public Selftimer(CameraFunctions cameraFunctions, int n) {
        this.mController = cameraFunctions;
        this.mCameraType = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void countdown(int n) {
        if (this.mCameraType == 1) {
            this.mController.mCameraWindow.startSelfTimerCountDownAnimation();
        }
        int n2 = 0;
        do {
            if (n2 >= SELF_TIMER_LEVELS.length || n <= SELF_TIMER_LEVELS[n2]) {
                if (n2 >= SELF_TIMER_LEVELS.length) {
                    n2 = -1 + SELF_TIMER_LEVELS.length;
                }
                if (n2 == 0) {
                    this.mController.mCameraActivity.getShutterToneGenerator().blink();
                    return;
                } else {
                    if ((n - SELF_TIMER_LEVELS[n2 - 1]) % SELF_TIMER_INTERVALS[n2] != 0) return;
                    {
                        this.mController.mCameraActivity.getShutterToneGenerator().blink();
                        return;
                    }
                }
            }
            ++n2;
        } while (true);
    }

    public void finish() {
        this.mController.mCameraWindow.cancelSelftimer(false);
        Message message = Message.obtain();
        message.what = 3;
        message.obj = this.mSource;
        this.mHandler.sendMessageDelayed(message, 200);
        this.mTimer = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void start(ControllerEventSource controllerEventSource) {
        switch (this.mCameraType) {
            case 1: {
                this.mController.mCameraWindow.startPhotoSelftimer();
                break;
            }
            case 2: {
                this.mController.mCameraWindow.startVideoSelftimer();
            }
        }
        this.mOffsetDuration = this.mController.shouldAfStartAfterSelfTimer(controllerEventSource.mType) ? 200 + AF_DELAY_DURATION : 200;
        this.mSource = controllerEventSource;
        if (this.mSoundType != null) {
            this.mController.mCameraActivity.getShutterToneGenerator().play(this.mSoundType);
        }
        this.mTimer = new CameraTimer(this.mMaxDuration, 250, (Handler)this.mHandler, "SelfTimer", 300);
        this.mTimer.start();
        this.mController.mEventDispatcher.updateSelfTimerStatus(true);
    }

    public void stop(boolean bl) {
        this.mController.mEventDispatcher.updateSelfTimerStatus(false);
        if (this.mTimer != null) {
            this.mController.mCameraActivity.getShutterToneGenerator().cancelPlayAndBlink();
            this.mController.mCameraWindow.cancelSelftimer(bl);
            this.mTimer.cancel();
            this.mTimer = null;
        }
        this.mHandler.removeMessages(3);
    }

    public void update(int n, ShutterToneGenerator.Type type) {
        this.mMaxDuration = n;
        this.mSoundType = type;
    }

    /*
     * Failed to analyse overrides
     */
    private static class SelfTimerHandler
    extends Handler {
        private SelfTimerHandler() {
        }

        /* synthetic */ SelfTimerHandler( var1) {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 0: {
                    Executor.sendEvent(ControllerEvent.EV_SELFTIMER_COUNTDOWN, message.arg1, null);
                    return;
                }
                case 1: {
                    Executor.sendEvent(ControllerEvent.EV_SELFTIMER_FINISH, message.arg1, null);
                    return;
                }
                case 3: 
            }
            ControllerEventSource controllerEventSource = (ControllerEventSource)message.obj;
            Executor.sendEvent(ControllerEvent.EV_SELFTIMER_CAPTURE, controllerEventSource);
        }
    }

}

