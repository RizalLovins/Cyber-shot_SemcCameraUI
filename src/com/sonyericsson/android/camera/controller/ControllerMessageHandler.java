/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.Queue
 *  java.util.concurrent.ConcurrentLinkedQueue
 */
package com.sonyericsson.android.camera.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.ControllerMessage;
import com.sonyericsson.android.camera.controller.State;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Failed to analyse overrides
 */
public class ControllerMessageHandler
implements Runnable {
    private static final String TAG = ControllerMessageHandler.class.getSimpleName();
    private State mCurrentState;
    private Handler mHandler;
    private Queue<ControllerMessage> mQueue;

    public ControllerMessageHandler(Context context, State state) {
        this.mHandler = new Handler(context.getMainLooper());
        this.mQueue = new ConcurrentLinkedQueue();
        this.setState(state);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void dispatch(ControllerMessage var1) {
        var4_2 = this;
        // MONITORENTER : var4_2
        var3_3 = .$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEvent[var1.mEventId.ordinal()];
        switch (var3_3) {
            case 1: {
                this.mCurrentState.handleAbort(var1);
                ** break;
            }
            case 2: {
                this.mCurrentState.handleAfCancel(var1);
                ** break;
            }
            case 3: {
                this.mCurrentState.handleAfDone(var1);
                ** break;
            }
            case 4: {
                this.mCurrentState.handleAfStart(var1);
                ** break;
            }
            case 5: {
                this.mCurrentState.handleBurstStart(var1);
                ** break;
            }
            case 6: {
                this.mCurrentState.handleBurstStop(var1);
                ** break;
            }
            case 7: {
                this.mCurrentState.handleCameraSetupFinished(var1);
                ** break;
            }
            case 8: {
                this.mCurrentState.handleCapture(var1);
                ** break;
            }
            case 9: {
                this.mCurrentState.handleChangeCapturingMode(var1);
                ** break;
            }
            case 10: {
                this.mCurrentState.handleCompressedData(var1);
                ** break;
            }
            case 11: {
                this.mCurrentState.handleBurstCompressedData(var1);
                ** break;
            }
            case 12: {
                this.mCurrentState.handleControllerPause(var1);
                ** break;
            }
            case 13: {
                this.mCurrentState.handleControllerResume(var1);
                ** break;
            }
            case 14: {
                this.mCurrentState.handleDeviceError(var1);
                ** break;
            }
            case 15: {
                this.mCurrentState.handleAudioResourceError(var1);
                ** break;
            }
            case 16: {
                this.mCurrentState.handleStorageError(var1);
                ** break;
            }
            case 17: {
                this.mCurrentState.handleStorageMounted(var1);
                ** break;
            }
            case 18: {
                this.mCurrentState.handleStorageShouldChange(var1);
                ** break;
            }
            case 19: {
                this.mCurrentState.handleFaceDetect(var1);
                ** break;
            }
            case 20: {
                this.mCurrentState.handleFaceIdentify(var1);
                ** break;
            }
            case 21: {
                this.mCurrentState.handleFaceDetectChange(var1);
                ** break;
            }
            case 22: {
                this.mCurrentState.handleFocusPositionCancel(var1);
                ** break;
            }
            case 23: {
                this.mCurrentState.handleFocusPositionChange(var1);
                ** break;
            }
            case 24: {
                this.mCurrentState.handleFocusPositionContinue(var1);
                ** break;
            }
            case 25: {
                this.mCurrentState.handleFocusPositionFinish(var1);
                ** break;
            }
            case 26: {
                this.mCurrentState.handleFocusPositionStart(var1);
                ** break;
            }
            case 27: {
                this.mCurrentState.handleKeyBack(var1);
                ** break;
            }
            case 28: {
                this.mCurrentState.handleLaunch(var1);
                ** break;
            }
            case 29: {
                this.mCurrentState.handleObjectTracking(var1);
                ** break;
            }
            case 30: {
                this.mCurrentState.handleObjectTrackingInvisible(var1);
                ** break;
            }
            case 31: {
                this.mCurrentState.handleObjectTrackingLost(var1);
                ** break;
            }
            case 32: {
                this.mCurrentState.handleObjectTrackingStart(var1);
                ** break;
            }
            case 33: {
                this.mCurrentState.handleOpenSettingsDialog(var1);
                ** break;
            }
            case 34: {
                this.mCurrentState.handleReachHighTemperature(var1);
                ** break;
            }
            case 35: {
                this.mCurrentState.handleSceneChanged(var1);
                ** break;
            }
            case 36: {
                this.mCurrentState.handleSelfTimerCancel(var1);
                ** break;
            }
            case 37: {
                this.mCurrentState.handleSelfTimerCapture(var1);
                ** break;
            }
            case 38: {
                this.mCurrentState.handleSelfTimerCountdown(var1);
                ** break;
            }
            case 39: {
                this.mCurrentState.handleSelfTimerFinish(var1);
                ** break;
            }
            case 40: {
                this.mCurrentState.handleSelfTimerStart(var1);
                ** break;
            }
            case 41: {
                this.mCurrentState.handleShutterDone(var1);
                ** break;
            }
            case 42: {
                this.mCurrentState.handleSmileCapture(var1);
                ** break;
            }
            case 43: {
                this.mCurrentState.handleStoreDone(var1);
                ** break;
            }
            case 44: {
                this.mCurrentState.handleSurfaceChanged(var1);
                ** break;
            }
            case 45: {
                this.mCurrentState.handleSurfaceCreated(var1);
                ** break;
            }
            case 46: {
                this.mCurrentState.handleSurfaceDestroyed(var1);
                ** break;
            }
            case 47: {
                this.mCurrentState.handleVideoInfo(var1);
                ** break;
            }
            case 48: {
                this.mCurrentState.handleVideoProgress(var1);
                ** break;
            }
            case 49: {
                this.mCurrentState.handleVideoStartWaitDone(var1);
                ** break;
            }
            case 50: {
                this.mCurrentState.handleVideoFinished(var1);
                ** break;
            }
            case 51: {
                this.mCurrentState.handleZoomFinish(var1);
                ** break;
            }
            case 52: {
                this.mCurrentState.handleZoomPrepare(var1);
                ** break;
            }
            case 53: {
                this.mCurrentState.handleZoomProgress(var1);
                ** break;
            }
            case 54: {
                this.mCurrentState.handleZoomStart(var1);
                ** break;
            }
            case 55: {
                this.mCurrentState.handleZoomStop(var1);
                ** break;
            }
            case 56: {
                this.mCurrentState.handleClickContentProgress(var1);
                ** break;
            }
            case 57: {
                this.mCurrentState.handleVideoPauseResume(var1);
            }
lbl175: // 58 sources:
            default: {
                // MONITOREXIT : var4_2
                return;
            }
            case 58: 
        }
        this.mCurrentState.handleVideoPaused(var1);
    }

    private boolean isVideoRecordingEvent(ControllerEvent controllerEvent, ControllerEventSource controllerEventSource) {
        if (controllerEvent == ControllerEvent.EV_CAPTURE && controllerEventSource == ControllerEventSource.VIDEO_BUTTON) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean queueHasThisEvent(ControllerEvent controllerEvent, ControllerEventSource controllerEventSource) {
        for (ControllerMessage controllerMessage : this.mQueue) {
            if (controllerEvent != controllerMessage.mEventId || controllerEventSource != controllerMessage.mSource) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void cancel(ControllerEvent controllerEvent) {
        void var4_2 = this;
        synchronized (var4_2) {
            Iterator iterator = this.mQueue.iterator();
            while (iterator.hasNext()) {
                if (((ControllerMessage)iterator.next()).mEventId != controllerEvent) continue;
                iterator.remove();
            }
            return;
        }
    }

    public void clear() {
        ControllerMessageHandler controllerMessageHandler = this;
        synchronized (controllerMessageHandler) {
            this.mQueue.clear();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void clearAutoDispatchEvent() {
        ControllerMessageHandler controllerMessageHandler = this;
        synchronized (controllerMessageHandler) {
            Iterator iterator = this.mQueue.iterator();
            while (iterator.hasNext()) {
                if (((ControllerMessage)iterator.next()).mExpectedState == null) continue;
                iterator.remove();
            }
            return;
        }
    }

    public void dispatchEvent(ControllerEvent controllerEvent, ControllerEventSource controllerEventSource, int n, Object object) {
        void var6_5 = this;
        synchronized (var6_5) {
            super.dispatch(new ControllerMessage(controllerEvent, controllerEventSource, n, object, null));
            return;
        }
    }

    public State getState() {
        ControllerMessageHandler controllerMessageHandler = this;
        synchronized (controllerMessageHandler) {
            State state = this.mCurrentState;
            return state;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void run() {
        ControllerMessageHandler controllerMessageHandler = this;
        synchronized (controllerMessageHandler) {
            int n = 0;
            int n2 = 0;
            Iterator iterator = this.mQueue.iterator();
            while (iterator.hasNext()) {
                ControllerMessage controllerMessage = (ControllerMessage)iterator.next();
                if (!this.mQueue.contains((Object)controllerMessage)) continue;
                if (controllerMessage.isRunnable(this.mCurrentState.getClass())) {
                    ++n2;
                    MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.HANDLE_EVENT, true, controllerMessage.toString());
                    this.dispatch(controllerMessage);
                    MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.HANDLE_EVENT, false);
                    iterator.remove();
                    continue;
                }
                ++n;
            }
            if (n > 0 && n2 > 0) {
                this.mHandler.post((Runnable)this);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void sendEvent(ControllerEvent controllerEvent, ControllerEventSource controllerEventSource, int n, Object object, Class<? extends State> class_) {
        ControllerMessageHandler controllerMessageHandler = this;
        synchronized (controllerMessageHandler) {
            boolean bl;
            if (!(this.isVideoRecordingEvent(controllerEvent, controllerEventSource) && (bl = this.queueHasThisEvent(controllerEvent, controllerEventSource)))) {
                ControllerMessage controllerMessage = new ControllerMessage(controllerEvent, controllerEventSource, n, object, class_);
                this.mQueue.offer((Object)controllerMessage);
                this.mHandler.post((Runnable)this);
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setState(State state) {
        void var3_2 = this;
        synchronized (var3_2) {
            if (state == null) {
                do {
                    return;
                    break;
                } while (true);
            }
            if (this.mCurrentState != null) {
                this.mCurrentState.exit();
            }
            this.mCurrentState = state;
            this.mCurrentState.entry();
            return;
        }
    }

}

