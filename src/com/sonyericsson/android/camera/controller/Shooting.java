/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.graphics.YuvImage
 *  android.hardware.Camera
 *  android.hardware.Camera$PictureCallback
 *  android.hardware.Camera$PreviewCallback
 *  android.hardware.Camera$ShutterCallback
 *  android.view.View
 *  android.widget.RelativeLayout
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.LinkedList
 */
package com.sonyericsson.android.camera.controller;

import android.app.Activity;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.controller.VideoDevice;
import com.sonyericsson.android.camera.device.AutoFocusListener;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.mediasaving.BurstSavingTaskManager;
import com.sonyericsson.android.camera.mediasaving.MediaSavingUtil;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.LocalGoogleAnalyticsUtil;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.ThumbnailUtil;
import com.sonyericsson.cameracommon.focusview.RectangleColor;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonymobile.cameracommon.googleanalytics.parameters.Event;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class Shooting {
    private static final boolean[] AF_AEAWBLOCK;
    private static final boolean[] AF_ALLLOCK;
    private static final int MAX_CAPTURE_REQUEST = 2;
    private static final String TAG;
    private boolean[] mAfLock = AF_ALLLOCK;
    private ControllerEventSource mAfSource;
    private boolean mAfStarted;
    private final AutoFocusListener mAutoFocusCallback = new AutoFocusCallback(null);
    private LinkedList<CaptureRequest> mCaptureRequestQueue = new LinkedList();
    private final CameraFunctions mController;
    private boolean mIsSuccessSound;
    private boolean mIsTakePictureRequested;
    private boolean mIsVideoSnapshotSupported;
    private final PreviewCallbackForSnapshot mPreviewCallback = new PreviewCallbackForSnapshot(null);
    private long mTimeForLastPictureTaken = -1;

    static {
        TAG = Shooting.class.getSimpleName();
        AF_ALLLOCK = new boolean[]{true, true, true};
        AF_AEAWBLOCK = new boolean[]{true, true, false};
    }

    public Shooting(CameraFunctions cameraFunctions) {
        this.mController = cameraFunctions;
    }

    static /* synthetic */ long access$302(Shooting shooting, long l) {
        shooting.mTimeForLastPictureTaken = l;
        return l;
    }

    static /* synthetic */ LinkedList access$400(Shooting shooting) {
        return shooting.mCaptureRequestQueue;
    }

    private boolean capture(boolean bl, boolean bl2) {
        if (this.mCaptureRequestQueue.isEmpty()) {
            return false;
        }
        if (bl2) {
            CaptureRequest captureRequest = (CaptureRequest)this.mCaptureRequestQueue.peek();
            if (!bl) {
                this.mController.startPreview();
            }
            captureRequest.capture();
            super.setGoogleAnalyticsEvents(captureRequest);
            return true;
        }
        this.clearCaptureRequest();
        return false;
    }

    private void setGoogleAnalyticsEvents(CaptureRequest captureRequest) {
        Event.CaptureProcedure captureProcedure = Event.CaptureProcedure.getShootingType(this.mController.mVideoDevice.isRecording());
        int n = captureRequest.mRequest.common.orientation;
        LocalGoogleAnalyticsUtil.getInstance().setCaptureProcedureForShooting(captureProcedure, n);
    }

    public void addCaptureRequest(PhotoSavingRequest photoSavingRequest) {
        if (this.mIsTakePictureRequested) {
            this.mCaptureRequestQueue.add((Object)new TakePictureRequest(false, photoSavingRequest));
            return;
        }
        this.mCaptureRequestQueue.add((Object)new ScreenshotRequest(false, photoSavingRequest));
    }

    public boolean canAcceptNextCaptureRequest() {
        if ((CaptureRequest)this.mCaptureRequestQueue.peek() != null && this.mCaptureRequestQueue.size() < 2) {
            return true;
        }
        return false;
    }

    public void clearCaptureRequest() {
        this.mCaptureRequestQueue.clear();
    }

    public ControllerEventSource getAfTrigger() {
        return this.mAfSource;
    }

    public int getCaptureRequestQueueSize() {
        return this.mCaptureRequestQueue.size();
    }

    public long getTimeMillisForLastPictureTaken() {
        return this.mTimeForLastPictureTaken;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onAutoFocus(int n, AutoFocusListener.Result result) {
        if (n == 0) {
            if (this.mIsSuccessSound) {
                this.mController.mCameraActivity.getShutterToneGenerator().play(ShutterToneGenerator.Type.SOUND_AF_SUCCESS);
            }
            this.mController.mCameraWindow.setRectangleColor(RectangleColor.AF_SUCCESS, result);
            return;
        } else {
            if (n != 1) return;
            {
                this.mController.mCameraWindow.setRectangleColor(RectangleColor.AF_FAIL, null);
                return;
            }
        }
    }

    public void startAutoFocus(ControllerEventSource controllerEventSource) {
        this.mAfStarted = this.mController.mCameraDevice.startAutoFocus(this.mAutoFocusCallback, this.mAfLock[0], this.mAfLock[1], this.mAfLock[2]);
        if (this.mAfStarted) {
            this.mAfSource = controllerEventSource;
            return;
        }
        Executor.postEmptyEvent(ControllerEvent.EV_DEVICE_ERROR);
    }

    public void stopAutoFocus() {
        if (this.mAfStarted) {
            this.mController.mCameraDevice.stopAutoFocus();
        }
        this.mAfStarted = false;
        this.mAfSource = ControllerEventSource.UNKNOWN;
    }

    /*
     * Enabled aggressive block sorting
     */
    public SavingRequest storePicture(byte[] arrby) {
        CaptureRequest captureRequest = (CaptureRequest)this.mCaptureRequestQueue.poll();
        if (arrby == null || captureRequest == null) {
            return null;
        }
        captureRequest.store(arrby);
        return captureRequest.mRequest;
    }

    public boolean takeNextPicture(boolean bl) {
        return super.capture(false, bl);
    }

    public void takePicture(PhotoSavingRequest photoSavingRequest) {
        this.mCaptureRequestQueue.add((Object)new TakePictureRequest(true, photoSavingRequest));
        this.mIsTakePictureRequested = true;
        super.capture(true, true);
    }

    public void takeScreenShot(PhotoSavingRequest photoSavingRequest) {
        if (this.mIsVideoSnapshotSupported) {
            this.takePicture(photoSavingRequest);
            return;
        }
        this.mCaptureRequestQueue.add((Object)new ScreenshotRequest(true, photoSavingRequest));
        this.mIsTakePictureRequested = false;
        super.capture(true, true);
    }

    public void update(int n) {
        this.mIsVideoSnapshotSupported = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)n).VIDEO_SNAPSHOT.get();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void update(FocusMode focusMode) {
        this.mAfLock = focusMode == FocusMode.FIXED ? AF_AEAWBLOCK : AF_ALLLOCK;
        this.mIsSuccessSound = focusMode.isSuccessSound();
    }

    private static class AutoFocusCallback
    implements AutoFocusListener {
        public static final int AF_FAILED = 1;
        public static final int AF_SUCCESS;

        private AutoFocusCallback() {
        }

        /* synthetic */ AutoFocusCallback( var1) {
        }

        @Override
        public void onAutoFocus(AutoFocusListener.Result result) {
            int n = 1;
            if (result != null && result.isFocused()) {
                n = 0;
            }
            if (Configurations.isLogForOperatorsEnabled()) {
                CameraLogger.dForOperators("AutoFocus finished with resultCode: " + n);
            }
            Executor.postEvent(ControllerEvent.EV_AF_DONE, n, result);
        }
    }

    private abstract class CaptureRequest {
        public final boolean mIsFirstShot;
        public PhotoSavingRequest mRequest;

        public CaptureRequest(boolean bl, PhotoSavingRequest photoSavingRequest) {
            this.mIsFirstShot = bl;
            this.mRequest = photoSavingRequest;
        }

        public void capture() {
            if (!this.mIsFirstShot) {
                this.mRequest = Shooting.this.mController.updateSavingRequest(this.mRequest, false);
            }
        }

        public abstract void store(byte[] var1);
    }

    /*
     * Failed to analyse overrides
     */
    private static class PreviewCallbackForSnapshot
    implements Camera.PreviewCallback {
        private PreviewCallbackForSnapshot() {
        }

        /* synthetic */ PreviewCallbackForSnapshot( var1) {
        }

        public void onPreviewFrame(byte[] arrby, Camera camera) {
            Executor.sendEvent(ControllerEvent.EV_COMPRESSED_DATA, 0, arrby);
        }
    }

    private class ScreenshotRequest
    extends CaptureRequest {
        public ScreenshotRequest(boolean bl, PhotoSavingRequest photoSavingRequest) {
            super(bl, photoSavingRequest);
        }

        @Override
        public void capture() {
            super.capture();
            this.mRequest.setRequestId(Shooting.access$100((Shooting)Shooting.this).mCameraWindow.getRequestId(this.mIsFirstShot));
            Shooting.access$100((Shooting)Shooting.this).mCameraDevice.setOneShotPreviewCallback((Camera.PreviewCallback)Shooting.this.mPreviewCallback);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Lifted jumps to return sites
         */
        @Override
        public void store(byte[] var1) {
            if (var1 == null) return;
            var2_2 = new YuvImage(var1, 17, this.mRequest.common.width, this.mRequest.common.height, null);
            var3_3 = new ByteArrayOutputStream();
            var4_4 = MediaSavingUtil.convertYuvToJpegOutputStream(var2_2, (OutputStream)var3_3, this.mRequest);
            try {
                var3_3.flush();
                this.mRequest.setImageData(var3_3.toByteArray());
            }
            catch (IOException var7_5) {
                var4_4 = MediaSavingResult.FAIL;
            }
lbl11: // 3 sources:
            do {
                if (var4_4 != MediaSavingResult.SUCCESS) return;
                Shooting.access$100((Shooting)Shooting.this).mCameraActivity.disableAutoOffTimer();
                Shooting.access$100((Shooting)Shooting.this).mSavingTaskManager.storePicture(this.mRequest);
                return;
                break;
            } while (true);
            finally {
                var3_3.close();
                ** continue;
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ShutterCallback
    implements Camera.ShutterCallback {
        private final PhotoSavingRequest mRequest;

        public ShutterCallback(PhotoSavingRequest photoSavingRequest) {
            this.mRequest = photoSavingRequest;
        }

        public void onShutter() {
            int n = Shooting.access$100((Shooting)Shooting.this).mCameraWindow.getRequestId(false);
            this.mRequest.setRequestId(n);
            if (Executor.isRecording()) {
                Shooting.access$100((Shooting)Shooting.this).mCameraWindow.setThumbnailViewClickListener(null);
            }
            Shooting.access$100((Shooting)Shooting.this).mCameraWindow.showProgressForSnapshot(n);
            Executor.sendEvent(ControllerEvent.EV_SHUTTER_DONE, n, this.mRequest);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class TakePictureRequest
    extends CaptureRequest
    implements Camera.PictureCallback {
        public TakePictureRequest(boolean bl, PhotoSavingRequest photoSavingRequest) {
            super(bl, photoSavingRequest);
        }

        public void capture() {
            super.capture();
            Shooting.access$100((Shooting)Shooting.this).mCameraDevice.commit();
            ShutterCallback shutterCallback = new ShutterCallback(this.mRequest);
            Shooting.access$100((Shooting)Shooting.this).mCameraDevice.takePicture((Camera.ShutterCallback)shutterCallback, (Camera.PictureCallback)this);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onPictureTaken(byte[] var1, Camera var2_2) {
            Shooting.access$302(Shooting.this, System.currentTimeMillis());
            var5_3 = Shooting.access$100((Shooting)Shooting.this).mCameraActivity.isOneShot();
            var6_4 = false;
            ** if (var5_3) goto lbl-1000
lbl5: // 1 sources:
            var8_5 = this.mRequest.common.savedFileType;
            var9_6 = SavingTaskManager.SavedFileType.PHOTO_DURING_REC;
            var6_4 = false;
            ** if (var8_5 == var9_6) goto lbl-1000
lbl9: // 1 sources:
            var10_7 = Shooting.access$100(Shooting.this).getParams().getAutoReview();
            var11_8 = AutoReview.OFF;
            var6_4 = false;
            if (!(var10_7 != var11_8 && (var6_4 = true))) lbl-1000: // 3 sources:
            {
                if (var1 != null && Shooting.access$400(Shooting.this).size() == 1) {
                    var7_9 = ThumbnailUtil.createThumbnailViewFromJpeg((Activity)Shooting.access$100((Shooting)Shooting.this).mCameraActivity, var1, this.mRequest.common.orientation);
                    Shooting.access$100((Shooting)Shooting.this).mCameraWindow.setEarlyThumbnailView((View)var7_9);
                    Shooting.access$100((Shooting)Shooting.this).mCameraWindow.startEarlyThumbnailInsertAnimation(this.mRequest.getRequestId());
                }
            }
            Executor.sendEvent(ControllerEvent.EV_COMPRESSED_DATA, -1, var1);
        }

        public void store(byte[] arrby) {
            if (arrby != null) {
                this.mRequest.setImageData(arrby);
                Shooting.access$100((Shooting)Shooting.this).mCameraActivity.disableAutoOffTimer();
                Shooting.access$100((Shooting)Shooting.this).mSavingTaskManager.storePicture(this.mRequest);
            }
        }
    }

}

