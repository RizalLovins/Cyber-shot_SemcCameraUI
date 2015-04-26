/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.os.Message
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingCallback
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingResult
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameraextension.CameraExtension;

public class ObjectTracking {
    private static final String TAG = ObjectTracking.class.getSimpleName();
    private ObjectTrackingCallback mCallback = null;
    private final CameraFunctions mController;
    private final ObjectTrackingHandler mHandler;
    private boolean mIsAlreadyLost = true;
    private Rect mPosition;
    private boolean mShouldWaitForLost = false;

    public ObjectTracking(CameraFunctions cameraFunctions) {
        this.mHandler = new ObjectTrackingHandler((ObjectTracking)this, null);
        this.mController = cameraFunctions;
    }

    static /* synthetic */ CameraFunctions access$100(ObjectTracking objectTracking) {
        return objectTracking.mController;
    }

    private void startTracking(Rect rect) {
        this.mCallback = new ObjectTrackingCallback((ObjectTracking)this, null);
        this.mController.mCameraWindow.startObjectTrackingAnimation(rect);
        this.mController.mCameraDevice.startObjectTracking(PositionConverter.getInstance().convertFaceToDevice(rect), (CameraExtension.ObjectTrackingCallback)this.mCallback);
    }

    public void invisible() {
        this.mController.mCameraWindow.hideObjectRectangles();
    }

    public void onObjectTracked(Rect rect) {
        if (rect.isEmpty()) {
            this.mController.mCameraWindow.hideObjectRectangles();
            return;
        }
        this.mController.mCameraWindow.updateObjectRectangles(PositionConverter.getInstance().convertDeviceToFace(rect));
    }

    public void start(Rect rect) {
        if (rect != null) {
            this.mPosition = rect;
            if (this.mIsAlreadyLost) {
                super.startTracking(rect);
            }
        } else {
            return;
        }
        this.stop(false);
        this.mShouldWaitForLost = true;
    }

    public void stop(boolean bl) {
        this.mController.mCameraDevice.stopObjectTracking(bl);
        this.mController.mCameraWindow.clearObjectRectangles();
        Executor.cancelEvent(ControllerEvent.EV_OBJECT_TRACKING);
        this.mHandler.stopTimeoutCount();
        if (bl) {
            this.mShouldWaitForLost = false;
            this.mIsAlreadyLost = true;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ObjectTrackingCallback
    implements CameraExtension.ObjectTrackingCallback {
        final /* synthetic */ ObjectTracking this$0;

        private ObjectTrackingCallback(ObjectTracking objectTracking) {
            this.this$0 = objectTracking;
        }

        /* synthetic */ ObjectTrackingCallback(ObjectTracking objectTracking,  var2_2) {
            super(objectTracking);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onObjectTracked(CameraExtension.ObjectTrackingResult objectTrackingResult) {
            if (objectTrackingResult == null) return;
            if (this.this$0.mShouldWaitForLost) {
                if (!objectTrackingResult.mIsLost) return;
                {
                    ObjectTracking.access$100((ObjectTracking)this.this$0).mCameraDevice.stopObjectTrackingCallback();
                    this.this$0.startTracking(this.this$0.mPosition);
                    this.this$0.mShouldWaitForLost = false;
                }
            }
            if (this.this$0.mIsAlreadyLost && objectTrackingResult.mIsLost) {
                return;
            }
            this.this$0.mIsAlreadyLost = objectTrackingResult.mIsLost;
            if (objectTrackingResult.mIsLost) {
                this.this$0.mHandler.startTimeoutCount();
                return;
            }
            this.this$0.mHandler.stopTimeoutCount();
            Executor.postEvent(ControllerEvent.EV_OBJECT_TRACKING, 0, (Object)objectTrackingResult.mRectOfTrackedObject);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ObjectTrackingHandler
    extends Handler {
        private static final int MSG_TIMEOUT_INVISIBLE = 2;
        private static final int MSG_TIMEOUT_LOST = 1;
        private static final int TIMEOUT_INVISIBLE = 500;
        private static final int TIMEOUT_LOST = 3000;
        final /* synthetic */ ObjectTracking this$0;

        private ObjectTrackingHandler(ObjectTracking objectTracking) {
            this.this$0 = objectTracking;
        }

        /* synthetic */ ObjectTrackingHandler(ObjectTracking objectTracking,  var2_2) {
            super(objectTracking);
        }

        public void handleMessage(Message message) {
            if (!ObjectTracking.access$100((ObjectTracking)this.this$0).mCameraDevice.isObjectTrackingRunning()) {
                return;
            }
            switch (message.what) {
                default: {
                    return;
                }
                case 1: {
                    Executor.sendEmptyEvent(ControllerEvent.EV_OBJECT_TRACKING_LOST);
                    return;
                }
                case 2: 
            }
            Executor.postEmptyEvent(ControllerEvent.EV_OBJECT_TRACKING_INVISIBLE);
        }

        public void startTimeoutCount() {
            this.sendEmptyMessageDelayed(1, 3000);
            this.sendEmptyMessageDelayed(2, 500);
        }

        public void stopTimeoutCount() {
            this.removeMessages(1);
            this.removeMessages(2);
        }
    }

}

