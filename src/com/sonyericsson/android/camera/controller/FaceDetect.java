/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.List
 */
package com.sonyericsson.android.camera.controller;

import android.content.Context;
import android.graphics.Rect;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.EventDispatcher;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.device.PreviewFrameRetriever;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.focusview.FaceInformationList;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentificationFactory;
import java.util.List;

public class FaceDetect {
    private static final int FACE_DETECTION_RESULT_REFRESH_TIMEOUT = 1000;
    private static final String TAG = FaceDetect.class.getSimpleName();
    private final CameraFunctions mController;
    private final FaceDetectionCallback mFaceDetectionCallback;
    private CameraExtension.FaceDetectionResult mFaceDetectionResultCache;
    private FaceIdentification mFaceIdentification;
    private Boolean mIsFaceDetectionIdSupported;
    private final PreviewFrameRetriever.OnPreviewFrameAvarableListener mOnPreviewFrameAvarableListener;
    private int mSmileLevel;
    private FaceDetectionState mState;
    private String mUserTouchFaceUuid;

    public FaceDetect(CameraFunctions cameraFunctions) {
        this.mFaceDetectionCallback = new FaceDetectionCallback((FaceDetect)this, null);
        this.mOnPreviewFrameAvarableListener = new MyOnPreviewFrameAvarableListener((FaceDetect)this, null);
        this.mState = new Unknown((FaceDetect)this, null);
        this.mFaceIdentification = null;
        this.mFaceDetectionResultCache = null;
        this.mUserTouchFaceUuid = null;
        this.mController = cameraFunctions;
    }

    private CameraExtension.FaceDetectionResult getFaceDetectionResultCache() {
        FaceDetect faceDetect = this;
        synchronized (faceDetect) {
            CameraExtension.FaceDetectionResult faceDetectionResult = this.mFaceDetectionResultCache;
            return faceDetectionResult;
        }
    }

    private void setFaceDetectionResultCache(CameraExtension.FaceDetectionResult faceDetectionResult) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mFaceDetectionResultCache = faceDetectionResult;
            return;
        }
    }

    private void setState(FaceDetectionState faceDetectionState) {
        this.mState = faceDetectionState;
    }

    private void startFaceIdentification() {
        if (this.mFaceIdentification == null) {
            this.mFaceIdentification = FaceIdentificationFactory.createNewInstance((Context)this.mController.mCameraActivity);
        }
    }

    private void stopFaceIdentification() {
        if (this.mFaceIdentification != null) {
            this.setFaceDetectionResultCache(null);
            this.faceLost();
            this.mFaceIdentification.release();
            this.mFaceIdentification = null;
        }
    }

    private void updateUuidBeforeUpdateView(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (this.mIsFaceDetectionIdSupported != null && !this.mIsFaceDetectionIdSupported.booleanValue() && FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult)) {
            this.mUserTouchFaceUuid = String.valueOf((int)((com.sonyericsson.cameraextension.CameraExtension$ExtFace)faceDetectionResult.extFaceList.get((int)faceDetectionResult.indexOfSelectedFace)).face.id);
        }
    }

    private void updateView(List<FaceIdentification.FaceIdentificationResult> list, CameraExtension.FaceDetectionResult faceDetectionResult, boolean bl) {
        Rect rect = this.mController.mCameraDevice.getPreviewRect();
        FaceInformationList faceInformationList = FaceDetectUtil.getFaceInformationList(list, super.getFaceDetectionResultCache(), rect, this.mUserTouchFaceUuid);
        if (faceInformationList == null) {
            return;
        }
        faceInformationList.setUseSmileGuage(bl);
        this.mController.mCameraWindow.updateFaceRectangles(faceInformationList);
    }

    public void changeFocusedFace(String string, Rect rect) {
        this.mUserTouchFaceUuid = string;
        super.updateView(null, super.getFaceDetectionResultCache(), false);
        this.mState.changeFocusedFace(rect);
    }

    public void clear() {
        this.stopFaceIdentification();
        this.stop();
    }

    public void enableFaceIdentification(boolean bl) {
        if (bl) {
            super.startFaceIdentification();
            return;
        }
        super.stopFaceIdentification();
    }

    public void faceLost() {
        this.mUserTouchFaceUuid = null;
        this.mController.mCameraWindow.disableTouchDetectedFace();
        this.mState.onFaceLost();
    }

    public CameraExtension.FaceDetectionCallback getFaceDetectionCallback() {
        return this.mFaceDetectionCallback;
    }

    public boolean isFaceDetect() {
        return this.mState.isFaceDetectionAvailable();
    }

    public void requestFaceId(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (!(this.mFaceIdentification == null || this.mFaceIdentification.isBusy())) {
            this.mController.mCameraDevice.getPreviewFrameRetriever().request(this.mOnPreviewFrameAvarableListener);
        }
    }

    public void start() {
        this.mState.start();
    }

    public void stop() {
        if (this.mState != null) {
            this.mState.stop();
        }
    }

    public void update(FocusMode focusMode, SmileCapture smileCapture) {
        CapturingMode capturingMode = this.mController.mControllerManager.getCapturingMode();
        if (smileCapture.isSmileCaptureOn()) {
            this.mSmileLevel = smileCapture.getIntValue();
            super.setState(new NoSmileFaceDetected((FaceDetect)this, null));
            return;
        }
        if (focusMode == FocusMode.FACE_DETECTION || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO) {
            super.setState(new NoFaceDetected((FaceDetect)this, null));
            this.mController.mCameraDevice.setFaceDetectionCallback((CameraExtension.FaceDetectionCallback)this.mFaceDetectionCallback);
            return;
        }
        super.setState(new NotAvailable());
    }

    public void updateFaceIdentify(List<FaceIdentification.FaceIdentificationResult> list) {
        this.mState.onFaceIdentified(list);
    }

    public void updateFaceRectangle(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult == null) {
            this.faceLost();
            return;
        }
        if (faceDetectionResult.extFaceList.size() > 0) {
            this.mState.onFaceDetected(faceDetectionResult);
            return;
        }
        this.faceLost();
    }

    private class FaceDetected
    extends FaceDetectionAvailable {
        public FaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            super(FaceDetect.this, null);
            this.onFaceDetected(faceDetectionResult);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            FaceDetect.this.updateUuidBeforeUpdateView(faceDetectionResult);
            FaceDetect.this.updateView(null, faceDetectionResult, false);
            this.checkAndChangeFocusedFace(faceDetectionResult);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> list) {
            if (FaceDetect.this.getFaceDetectionResultCache() == null || FaceDetect.access$100((FaceDetect)FaceDetect.this).extFaceList.size() == 0 || FaceDetect.this.mFaceIdentification == null) {
                return;
            }
            FaceDetect.this.updateView(list, FaceDetect.this.getFaceDetectionResultCache(), false);
            this.checkAndChangeFocusedFace(FaceDetect.this.getFaceDetectionResultCache());
        }

        @Override
        public void onFaceLost() {
            FaceDetect.access$000((FaceDetect)FaceDetect.this).mCameraWindow.clearFaceRectangles();
            FaceDetect.this.setState(new NoFaceDetected(FaceDetect.this, null));
        }
    }

    private abstract class FaceDetectionAvailable
    implements FaceDetectionState {
        final /* synthetic */ FaceDetect this$0;

        private FaceDetectionAvailable(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
        }

        /* synthetic */ FaceDetectionAvailable(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        @Override
        public void changeFocusedFace(Rect rect) {
            Rect rect2 = PositionConverter.getInstance().convertFaceToDevice(rect);
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraDevice.setSelectedFacePosition(rect2.centerX(), rect2.centerY());
        }

        /*
         * Enabled aggressive block sorting
         */
        protected void checkAndChangeFocusedFace(CameraExtension.FaceDetectionResult faceDetectionResult) {
            NamedFace namedFace;
            if (!((this.this$0.mIsFaceDetectionIdSupported == null || this.this$0.mIsFaceDetectionIdSupported.booleanValue()) && FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult) && (namedFace = FaceDetect.access$000((FaceDetect)this.this$0).mCameraWindow.getTopPriorityFace()) != null && !String.valueOf((int)((com.sonyericsson.cameraextension.CameraExtension$ExtFace)faceDetectionResult.extFaceList.get((int)faceDetectionResult.indexOfSelectedFace)).face.id).equals((Object)namedFace.mUuid))) {
                return;
            }
            this.changeFocusedFace(PositionConverter.getInstance().convertToView(namedFace.mFacePosition));
        }

        @Override
        public final boolean isFaceDetectionAvailable() {
            return true;
        }

        @Override
        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> list) {
        }

        @Override
        public final void start() {
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraDevice.setFaceDetectionCallback((CameraExtension.FaceDetectionCallback)this.this$0.mFaceDetectionCallback);
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraDevice.startFaceDetection();
        }

        @Override
        public final void stop() {
            this.this$0.stopFaceIdentification();
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraDevice.setFaceDetectionCallback(null);
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraDevice.stopFaceDetection();
            FaceDetect.access$000((FaceDetect)this.this$0).mCameraWindow.hideFaceRectangles();
            Executor.cancelEvent(ControllerEvent.EV_FACE_DETECT);
            Executor.cancelEvent(ControllerEvent.EV_FACE_IDENTIFY);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class FaceDetectionCallback
    implements CameraExtension.FaceDetectionCallback {
        final /* synthetic */ FaceDetect this$0;

        private FaceDetectionCallback(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
        }

        /* synthetic */ FaceDetectionCallback(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onFaceDetection(CameraExtension.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult == null) {
                return;
            }
            if (System.currentTimeMillis() - this.this$0.mController.getTimeMillisForLastPictureTaken() < 1000) {
                if (this.this$0.getFaceDetectionResultCache() == null) return;
                this.this$0.setFaceDetectionResultCache(null);
                Executor.postEvent(ControllerEvent.EV_FACE_DETECT, 0, null);
                return;
            }
            if (this.this$0.mIsFaceDetectionIdSupported == null) {
                if (!faceDetectionResult.extFaceList.isEmpty()) {
                    this.this$0.mIsFaceDetectionIdSupported = FaceDetectUtil.hasValidFaceId(faceDetectionResult);
                }
            } else if (!this.this$0.mIsFaceDetectionIdSupported.booleanValue()) {
                FaceDetectUtil.setUuidFaceDetectionResult(faceDetectionResult);
            }
            this.this$0.setFaceDetectionResultCache(faceDetectionResult);
            Executor.postEvent(ControllerEvent.EV_FACE_DETECT, 0, (Object)faceDetectionResult);
        }
    }

    private static interface FaceDetectionState {
        public void changeFocusedFace(Rect var1);

        public boolean isFaceDetectionAvailable();

        public void onFaceDetected(CameraExtension.FaceDetectionResult var1);

        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> var1);

        public void onFaceLost();

        public void start();

        public void stop();
    }

    private static class FaceIdentificationCallback
    implements FaceIdentification.FaceIdentificationCallback {
        private FaceIdentificationCallback() {
        }

        /* synthetic */ FaceIdentificationCallback( var1) {
        }

        @Override
        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> list) {
            if (list == null) {
                return;
            }
            Executor.postEvent(ControllerEvent.EV_FACE_IDENTIFY, 0, list);
        }
    }

    private class MyOnPreviewFrameAvarableListener
    implements PreviewFrameRetriever.OnPreviewFrameAvarableListener {
        final /* synthetic */ FaceDetect this$0;

        private MyOnPreviewFrameAvarableListener(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
        }

        /* synthetic */ MyOnPreviewFrameAvarableListener(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void onPreviewFrameAvarable(PreviewFrameRetriever previewFrameRetriever, byte[] arrby) {
            boolean bl = true;
            if (this.this$0.mFaceIdentification == null) {
                return;
            }
            PreviewFrameRetriever.PreviewInfo previewInfo = previewFrameRetriever.getPreviewInfo();
            if (arrby != null && previewInfo != null) {
                if (previewInfo.width == 0) return;
                if (previewInfo.height == 0) return;
                CameraExtension.FaceDetectionResult faceDetectionResult = this.this$0.getFaceDetectionResultCache();
                if (faceDetectionResult == null) return;
                List<FaceIdentification.FaceIdentificationRequest> list = FaceDetectUtil.createFaceIdentificationRequest(faceDetectionResult);
                if (list == null) return;
                if (CameraDevice.getCameraInfo(FaceDetect.access$000((FaceDetect)this.this$0).mControllerManager.getCameraId()) == null) return;
                this.this$0.mFaceIdentification.request(arrby, previewInfo.format, previewInfo.width, previewInfo.height, this.this$0.mController.getOrientation(), list, new FaceIdentificationCallback(null));
                return;
            }
            CameraLogger.e(TAG, "onPreviewFrame: data or info is null.");
            String string = TAG;
            StringBuilder stringBuilder = new StringBuilder().append("data is null = ");
            boolean bl2 = arrby == null ? bl : false;
            StringBuilder stringBuilder2 = stringBuilder.append(bl2).append("info is null = ");
            if (previewInfo != null) {
                bl = false;
            }
            CameraLogger.e(string, stringBuilder2.append(bl).toString());
        }
    }

    private class NoFaceDetected
    extends FaceDetectionAvailable {
        final /* synthetic */ FaceDetect this$0;

        private NoFaceDetected(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
            super(faceDetect, null);
        }

        /* synthetic */ NoFaceDetected(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            this.this$0.setState(new FaceDetected(faceDetectionResult));
        }

        @Override
        public void onFaceLost() {
        }
    }

    private class NoSmileFaceDetected
    extends NoFaceDetected {
        final /* synthetic */ FaceDetect this$0;

        private NoSmileFaceDetected(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
            super(faceDetect, null);
        }

        /* synthetic */ NoSmileFaceDetected(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            this.this$0.setState(new SmileFaceDetected(faceDetectionResult));
        }
    }

    private final class NotAvailable
    extends Unknown {
        public NotAvailable() {
            super(FaceDetect.this, null);
            FaceDetect.access$000((FaceDetect)FaceDetect.this).mCameraDevice.setFaceDetectionCallback(null);
        }

        @Override
        public boolean isFaceDetectionAvailable() {
            Parameters parameters = FaceDetect.access$000((FaceDetect)FaceDetect.this).mParameterManager.getParameters();
            if (parameters.capturingMode == CapturingMode.FRONT_PHOTO && parameters.getFocusMode() == FocusMode.FIXED || parameters.capturingMode == CapturingMode.SUPERIOR_FRONT && parameters.getFocusMode() == FocusMode.FIXED) {
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            if (this.isFaceDetectionAvailable()) {
                FaceDetect.access$000((FaceDetect)FaceDetect.this).mCameraDevice.startFaceDetection();
            }
        }

        @Override
        public void stop() {
            if (this.isFaceDetectionAvailable()) {
                FaceDetect.access$000((FaceDetect)FaceDetect.this).mCameraDevice.stopFaceDetection();
            }
        }
    }

    private final class SmileFaceDetected
    extends FaceDetected {
        public SmileFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            super(faceDetectionResult);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            if (!FaceDetect.access$000((FaceDetect)FaceDetect.this).mEventDispatcher.isSelfTimerRunning()) {
                FaceDetect.this.updateUuidBeforeUpdateView(faceDetectionResult);
                FaceDetect.this.updateView(null, faceDetectionResult, true);
                NamedFace namedFace = FaceDetect.access$000((FaceDetect)FaceDetect.this).mCameraWindow.getTopPriorityFace();
                if (namedFace != null) {
                    this.checkAndChangeFocusedFace(faceDetectionResult);
                    if (namedFace.mSmileScore >= FaceDetect.this.mSmileLevel) {
                        Executor.sendEvent(ControllerEvent.EV_SMILE_CAPTURE, ControllerEventSource.OTHER);
                    }
                }
            }
        }

        @Override
        public void onFaceLost() {
            super.onFaceLost();
            FaceDetect.this.setState(new NoSmileFaceDetected(FaceDetect.this, null));
        }
    }

    private class Unknown
    implements FaceDetectionState {
        final /* synthetic */ FaceDetect this$0;

        private Unknown(FaceDetect faceDetect) {
            this.this$0 = faceDetect;
        }

        /* synthetic */ Unknown(FaceDetect faceDetect,  var2_2) {
            super(faceDetect);
        }

        @Override
        public void changeFocusedFace(Rect rect) {
        }

        @Override
        public boolean isFaceDetectionAvailable() {
            return false;
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
        }

        @Override
        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> list) {
        }

        @Override
        public void onFaceLost() {
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }
    }

}

