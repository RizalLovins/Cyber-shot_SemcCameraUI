/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$OnZoomChangeListener
 *  android.hardware.Camera$Parameters
 *  android.os.Handler
 *  android.os.Message
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.SuperResolution;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.EventDispatcher;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.controller.ZoomDirection;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.controller.ZoomController;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;

public class AutoZoom {
    private static final int DELAY_ZOOMBAR_HIDE = 1000;
    private static final int MIN_ZOOM_STEP = 0;
    private static final String TAG = AutoZoom.class.getSimpleName();
    private static final int TEXT_ZOOM_NOT_SUPPORTED = 2131361935;
    private final AutoZoomCallback mCallback = new AutoZoomCallback(null);
    private final CameraFunctions mController;
    private float mCurrentZoomLength;
    private boolean mIsDeviceZooming;
    private boolean mIsFocusModeChangedInZoom;
    private boolean mIsUiZooming;
    private boolean mKeepZoomStep;
    private ZoomState mState;
    private final ZoombarHandler mZoombarHandler;

    public AutoZoom(CameraFunctions cameraFunctions) {
        this.mZoombarHandler = new ZoombarHandler();
        this.mController = cameraFunctions;
        this.mCurrentZoomLength = this.mController.mCameraDevice.getZoom();
    }

    static /* synthetic */ CameraFunctions access$300(AutoZoom autoZoom) {
        return autoZoom.mController;
    }

    private int getMaxZoom() {
        SuperResolution superResolution = this.mController.mParameterManager.getParameters().getSuperResolution();
        int n = this.mController.mCameraDevice.getZoom();
        if (superResolution == SuperResolution.ON && n < this.mController.mCameraDevice.getMaxSrZoom()) {
            return this.mController.mCameraDevice.getMaxSrZoom();
        }
        return this.mController.mCameraDevice.getMaxZoom();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setFocusMode(FocusMode focusMode) {
        if (this.mController.getParams().getFocusMode() == FocusMode.MULTI) {
            this.mIsFocusModeChangedInZoom = !this.mIsFocusModeChangedInZoom;
            this.mController.mCameraDevice.setFocusMode(focusMode);
            this.mController.mCameraDevice.commitParameters();
            this.mController.mCameraWindow.setFocusMode(focusMode);
            this.mController.mCameraWindow.showCurrentAfRectangle();
        }
    }

    private void setState(ZoomState zoomState) {
        this.mState = zoomState;
    }

    private void startAutoZoom(int n) {
        if (this.mIsDeviceZooming) {
            return;
        }
        if (n != 0) {
            super.setFocusMode(FocusMode.SINGLE);
        }
        try {
            this.mController.mCameraDevice.startSmoothZoom(n, (Camera.OnZoomChangeListener)this.mCallback);
            this.mIsDeviceZooming = true;
            return;
        }
        catch (Exception var2_2) {
            this.finish(ControllerEventSource.OTHER);
            return;
        }
    }

    private void stopAutoZoom() {
        if (this.mIsDeviceZooming) {
            this.mController.mCameraDevice.stopSmoothZoom();
            this.mIsDeviceZooming = false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void clear() {
        this.stop();
        this.mZoombarHandler.removeMessages(0);
        if (!this.mKeepZoomStep) {
            this.setZoom(0);
            this.mController.mCameraWindow.initZoom();
        }
        this.mIsFocusModeChangedInZoom = false;
    }

    public void finish(ControllerEventSource controllerEventSource) {
        if (this.mState != null) {
            this.mState.finishZoom();
        }
        this.mController.mEventDispatcher.updateZoomStatus(false, controllerEventSource);
        if (this.mIsUiZooming) {
            this.mIsUiZooming = false;
        }
    }

    public boolean isZoomAvailable() {
        return this.mState.isZoomAvailable();
    }

    public boolean isZooming() {
        return this.mIsUiZooming;
    }

    public void keepZoomStep(boolean bl) {
        this.mKeepZoomStep = bl;
    }

    public void onZoomProgress(int n, boolean bl) {
        this.mController.mCameraWindow.updateZoombar(n, this.mController.mCameraDevice.getMaxZoom(), this.mController.mCameraDevice.getMaxSrZoom());
        this.mController.mCameraDevice.setZoom(n);
        if (bl && n == 0) {
            super.setFocusMode(FocusMode.MULTI);
        }
    }

    public void prepare(ControllerEventSource controllerEventSource) {
        if (this.mState != null && this.mState.prepare()) {
            this.mController.mEventDispatcher.updateZoomStatus(true, controllerEventSource);
            this.mIsUiZooming = true;
        }
    }

    public void setZoom(int n) {
        if (this.mState != null) {
            this.mState.setZoomStep(n);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void start(ZoomDirection zoomDirection) {
        if (zoomDirection == ZoomDirection.IN) {
            this.mState.startZoomIn();
            return;
        } else {
            if (zoomDirection == ZoomDirection.OUT) {
                this.mState.startZoomOut();
                return;
            }
            if (zoomDirection != ZoomDirection.IN_OUT) return;
            {
                this.mState.startZoom(zoomDirection.getScaleLength());
                return;
            }
        }
    }

    public void stop() {
        if (this.mState != null) {
            this.mState.stopZoom();
        }
    }

    public void update(int n) {
        if (n == 1) {
            super.setState(new ZoomFront((AutoZoom)this, null));
            return;
        }
        if (com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)n).SMOOTH_ZOOM.get().booleanValue() && this.mController.mCameraDevice.getParameters() != null) {
            if (this.mController.mCameraDevice.getMaxZoom() > 0) {
                super.setState(new ZoomSupported((AutoZoom)this, null));
                return;
            }
            super.setState(new ZoomNotSupported());
            return;
        }
        super.setState(new ZoomNotSupported());
    }

    /*
     * Failed to analyse overrides
     */
    private static class AutoZoomCallback
    implements Camera.OnZoomChangeListener {
        private AutoZoomCallback() {
        }

        /* synthetic */ AutoZoomCallback( var1) {
        }

        public void onZoomChange(int n, boolean bl, Camera camera) {
            Executor.postEvent(ControllerEvent.EV_ZOOM_PROGRESS, n, bl);
        }
    }

    private class ZoomFront
    extends ZoomNotSupported {
        final /* synthetic */ AutoZoom this$0;

        private ZoomFront(AutoZoom autoZoom) {
            this.this$0 = autoZoom;
            super();
        }

        /* synthetic */ ZoomFront(AutoZoom autoZoom,  var2_2) {
            super(autoZoom);
        }

        @Override
        public boolean prepare() {
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraActivity.getMessagePopup().showRotatableToastMessage(2131361935, 1, RotatableToast.ToastPosition.TOP);
            return true;
        }
    }

    private class ZoomNotSupported
    implements ZoomState {
        public ZoomNotSupported() {
            AutoZoom.access$300((AutoZoom)AutoZoom.this).mCameraDevice.clearZoomChangeListener();
        }

        @Override
        public void finishZoom() {
        }

        @Override
        public boolean isZoomAvailable() {
            return false;
        }

        @Override
        public boolean prepare() {
            return false;
        }

        @Override
        public void setZoomStep(int n) {
        }

        @Override
        public void startZoom(float f) {
        }

        @Override
        public void startZoomIn() {
        }

        @Override
        public void startZoomOut() {
        }

        @Override
        public void stopZoom() {
        }
    }

    private static interface ZoomState {
        public void finishZoom();

        public boolean isZoomAvailable();

        public boolean prepare();

        public void setZoomStep(int var1);

        public void startZoom(float var1);

        public void startZoomIn();

        public void startZoomOut();

        public void stopZoom();
    }

    private class ZoomSupported
    implements ZoomState {
        private int mStartZoomValue;
        final /* synthetic */ AutoZoom this$0;

        private ZoomSupported(AutoZoom autoZoom) {
            this.this$0 = autoZoom;
        }

        /* synthetic */ ZoomSupported(AutoZoom autoZoom,  var2_2) {
            super(autoZoom);
        }

        @Override
        public void finishZoom() {
            this.this$0.mZoombarHandler.sendEmptyMessageDelayed(0, 1000);
            Executor.cancelEvent(ControllerEvent.EV_ZOOM_PROGRESS);
        }

        @Override
        public boolean isZoomAvailable() {
            return true;
        }

        @Override
        public boolean prepare() {
            this.mStartZoomValue = AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getZoom();
            this.this$0.mCurrentZoomLength = AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getZoom();
            this.this$0.mZoombarHandler.removeMessages(0);
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraWindow.hideFaceRectangles();
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraWindow.showZoombar(Executor.isRecording());
            return true;
        }

        @Override
        public void setZoomStep(int n) {
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.setZoom(n);
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.commitParameters();
            AutoZoom.access$300((AutoZoom)this.this$0).mCameraWindow.updateZoombar(n, AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxZoom(), AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxSrZoom());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void startZoom(float f) {
            SuperResolution superResolution = AutoZoom.access$300((AutoZoom)this.this$0).mParameterManager.getParameters().getSuperResolution();
            int n = AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getZoom();
            if (f > 0.0f && superResolution == SuperResolution.ON && n == AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxSrZoom() && this.mStartZoomValue < AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxSrZoom()) {
                return;
            }
            this.this$0.mCurrentZoomLength = ZoomController.getZoomValue(this.this$0.mCurrentZoomLength, f);
            if (this.this$0.mCurrentZoomLength < 0.0f) {
                this.this$0.mCurrentZoomLength = 0.0f;
            } else {
                int n2 = this.this$0.getMaxZoom();
                if (this.this$0.mCurrentZoomLength > (float)n2) {
                    this.this$0.mCurrentZoomLength = n2;
                }
            }
            this.this$0.startAutoZoom(Math.round((float)this.this$0.mCurrentZoomLength));
        }

        @Override
        public void startZoomIn() {
            SuperResolution superResolution = AutoZoom.access$300((AutoZoom)this.this$0).mParameterManager.getParameters().getSuperResolution();
            int n = AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getZoom();
            if (superResolution == SuperResolution.ON && n == AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxSrZoom() && this.mStartZoomValue != AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getMaxSrZoom()) {
                return;
            }
            this.this$0.startAutoZoom(this.this$0.getMaxZoom());
        }

        @Override
        public void startZoomOut() {
            if (AutoZoom.access$300((AutoZoom)this.this$0).mCameraDevice.getZoom() != 0) {
                this.this$0.startAutoZoom(0);
            }
        }

        @Override
        public void stopZoom() {
            this.this$0.stopAutoZoom();
        }
    }

    /*
     * Failed to analyse overrides
     */
    class ZoombarHandler
    extends Handler {
        private static final int MSG_HIDE;

        ZoombarHandler() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 0: 
            }
            AutoZoom.access$300((AutoZoom)AutoZoom.this).mCameraWindow.hideZoombar();
        }
    }

}

