/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$CameraInfo
 *  android.hardware.Camera$PictureCallback
 *  android.hardware.Camera$ShutterCallback
 *  android.location.Location
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Message
 *  android.view.View
 *  android.widget.RelativeLayout
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotCallback
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotResult
 *  com.sonyericsson.cameraextension.CameraExtension$FilePathGenerator
 *  com.sonyericsson.cameraextension.CameraExtension$MediaProviderUpdator
 *  com.sonyericsson.cameraextension.CameraExtension$StorageFullDetector
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.sonyericsson.android.camera.controller;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.burst.BurstInfoUpdator;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.mediasaving.BurstSavingTaskManager;
import com.sonyericsson.android.camera.mediasaving.MediaSavingUtil;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.contentsview.ThumbnailUtil;
import com.sonyericsson.cameracommon.mediasaving.EachDirPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.googleanalytics.parameters.Event;
import java.io.File;
import java.io.IOException;

public class BurstShooting {
    private static final String TAG = BurstShooting.class.getSimpleName();
    private int mBurstId;
    private BurstShotMethod mBurstShooting;
    private SavingRequest.StoreDataCallback mCallbackBestEffort;
    private SavingRequest.StoreDataCallback mCallbackHigh;
    private final CameraFunctions mController;
    private StoreDataResult mCoverResult;
    private PhotoSavingRequest mCurrentSavingRequest;
    private final MessageHandler mHandler;
    private boolean mIsCapturing;
    private boolean mIsStopRequested;
    private int mMinimumPictureCount;
    private EachDirPathBuilder mPathBuilder;
    private int mPictureCount;
    private ControllerEventSource mStartedBy;

    public BurstShooting(CameraFunctions cameraFunctions) {
        this.mHandler = new MessageHandler((BurstShooting)this, null);
        this.mCallbackBestEffort = new StoreDataCallbackBestEffort();
        this.mCallbackHigh = new StoreDataCallbackHigh();
        this.mMinimumPictureCount = 1;
        this.mController = cameraFunctions;
    }

    static /* synthetic */ int access$1208(BurstShooting burstShooting) {
        int n = burstShooting.mPictureCount;
        burstShooting.mPictureCount = n + 1;
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private PhotoSavingRequest createSavingRequest(String string, SavingRequest.StoreDataCallback storeDataCallback) {
        int n = RotationUtil.getNormalizedRotation(this.mController.mCameraActivity.getSensorOrientationDegree());
        Camera.CameraInfo cameraInfo = CameraDevice.getCameraInfo(this.mController.mControllerManager.getCameraId());
        int n2 = cameraInfo.facing == 1 ? (360 + cameraInfo.orientation - n) % 360 : (n + cameraInfo.orientation) % 360;
        PhotoSavingRequest photoSavingRequest = new PhotoSavingRequest(new TakenStatusCommon(System.currentTimeMillis(), n2, this.mController.mCameraActivity.getGeoTagManager().getCurrentLocation(), this.mController.mCameraDevice.getPictureRect().width(), this.mController.mCameraDevice.getPictureRect().height(), "image/jpeg", ".JPG", SavingTaskManager.SavedFileType.BURST, string, "", true, false), new TakenStatusPhoto());
        photoSavingRequest.addCallback(storeDataCallback);
        photoSavingRequest.setSomcType(2);
        return photoSavingRequest;
    }

    private PhotoSavingRequest getCopiedSavingRequest() {
        return new PhotoSavingRequest(this.mCurrentSavingRequest);
    }

    private String getPath() {
        String string = "/dev/null";
        if (this.mPathBuilder != null) {
            string = this.mPathBuilder.assignImageFilePath();
        }
        return string;
    }

    private EachDirPathBuilder getPathBuilder() {
        try {
            EachDirPathBuilder eachDirPathBuilder = new EachDirPathBuilder(MediaSavingConstants.BURST_DIR_NAME);
            return eachDirPathBuilder;
        }
        catch (IOException var2_2) {
            return null;
        }
    }

    private void onShootingDone() {
        this.mController.mCameraWindow.fadeoutCounter();
        if (this.mCoverResult != null) {
            this.mController.mCameraWindow.startEarlyThumbnailInsertAnimation(this.mCoverResult.savingRequest.getRequestId());
        }
    }

    private void prepareNextShot(boolean bl) {
        if (MediaSavingUtil.isCoverPicture(this.mCurrentSavingRequest.getSomcType())) {
            this.mCurrentSavingRequest.setSomcType(129);
        }
        this.mController.updateSavingRequest(this.mCurrentSavingRequest, bl);
    }

    private void sendGoogleAnalyticsEvents() {
        Event.CaptureProcedure captureProcedure = Event.CaptureProcedure.getBurstType(this.mBurstShooting.getClass().getSimpleName().equals((Object)"High"));
        boolean bl = this.mController.mCameraActivity.isAlreadyHighTemperature();
        int n = this.mCurrentSavingRequest.common.orientation;
        GoogleAnalyticsUtil.sendBurstEvent(captureProcedure, bl, this.mPictureCount, n);
    }

    private void setIsCapturing(boolean bl) {
        this.mIsCapturing = bl;
    }

    private void setIsStopRequested(boolean bl) {
        this.mIsStopRequested = bl;
    }

    private void storeCoverResult(StoreDataResult storeDataResult) {
        if (this.mCoverResult == null) {
            this.mCoverResult = storeDataResult;
            RelativeLayout relativeLayout = ThumbnailUtil.createThumbnailViewFromUri((Activity)this.mController.mCameraActivity, storeDataResult.uri, storeDataResult.savingRequest.common.orientation);
            this.mController.mCameraWindow.setEarlyThumbnailView((View)relativeLayout);
            if (this.isStopRequested()) {
                this.mHandler.sendEmptyMessageDelayed(0, 0);
            }
        }
    }

    public void clear() {
        this.setIsCapturing(false);
        this.setIsStopRequested(false);
        this.mBurstId = 0;
        this.mHandler.clear();
        this.mCoverResult = null;
        this.mPictureCount = 0;
        this.mMinimumPictureCount = 1;
        this.mStartedBy = ControllerEventSource.OTHER;
    }

    public void finish() {
        this.stop();
    }

    public int getPictureCount() {
        return this.mPictureCount;
    }

    public ControllerEventSource getStartedBy() {
        return this.mStartedBy;
    }

    public boolean isCapturing() {
        return this.mIsCapturing;
    }

    public boolean isStopRequested() {
        return this.mIsStopRequested;
    }

    public void logBurstShotResult(String string, CameraExtension.BurstShotResult burstShotResult) {
    }

    public void requestContinue() {
        if (this.isStopRequested()) {
            return;
        }
        this.mBurstShooting.requestContinue();
    }

    public boolean start(ControllerEventSource controllerEventSource) {
        this.clear();
        boolean bl = this.mBurstShooting.isAvailable();
        boolean bl2 = false;
        if (bl) {
            this.mStartedBy = controllerEventSource;
            this.mPathBuilder = super.getPathBuilder();
            this.mCurrentSavingRequest = this.mBurstShooting.getSavingRequest();
            this.mCurrentSavingRequest.setRequestId(this.mController.mCameraWindow.getRequestId(false));
            boolean bl3 = this.mBurstShooting.start();
            bl2 = false;
            if (bl3) {
                this.mController.mCameraWindow.startCaptureFeedbackAnimation();
                this.mController.mCameraWindow.addCountUpView(this.mCurrentSavingRequest.getRequestId());
                bl2 = true;
            }
        }
        return bl2;
    }

    public void stop() {
        if (this.isStopRequested()) {
            return;
        }
        this.setIsStopRequested(true);
        this.onShootingDone();
        if (this.mPictureCount >= this.mMinimumPictureCount) {
            this.mBurstShooting.finish();
        }
        if (this.mPictureCount > 1) {
            this.mController.mCameraWindow.startCaptureFeedbackAnimation();
        }
        this.sendGoogleAnalyticsEvents();
    }

    public void stop(int n) {
        this.mMinimumPictureCount = n;
        this.stop();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void update(BurstShot burstShot) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$BurstShot[burstShot.ordinal()]) {
            default: {
                this.mBurstShooting = new Off(null);
                break;
            }
            case 1: {
                this.mBurstShooting = new BestEffort((BurstShooting)this, null);
                break;
            }
            case 2: {
                this.mBurstShooting = new High((BurstShooting)this, null);
                break;
            }
            case 3: {
                this.mBurstShooting = new Off(null);
            }
        }
        this.clear();
    }

    /*
     * Failed to analyse overrides
     */
    private class BestEffort
    implements Camera.PictureCallback,
    Camera.ShutterCallback,
    BurstShotMethod {
        final /* synthetic */ BurstShooting this$0;

        private BestEffort(BurstShooting burstShooting) {
            this.this$0 = burstShooting;
        }

        /* synthetic */ BestEffort(BurstShooting burstShooting,  var2_2) {
            super(burstShooting);
        }

        private void takePicture() {
            this.this$0.setIsCapturing(true);
            BurstShooting.access$500((BurstShooting)this.this$0).mCameraDevice.commit();
            BurstShooting.access$500((BurstShooting)this.this$0).mCameraDevice.takePicture((Camera.ShutterCallback)this, (Camera.PictureCallback)this);
            BurstShooting.access$1208(this.this$0);
        }

        public void finish() {
        }

        public PhotoSavingRequest getSavingRequest() {
            String string = this.this$0.getPath();
            PhotoSavingRequest photoSavingRequest = this.this$0.createSavingRequest(string, this.this$0.mCallbackBestEffort);
            this.this$0.mController.setExifInfo(photoSavingRequest.common.orientation, photoSavingRequest.common.location);
            photoSavingRequest.setExtraOutput(Uri.fromFile((File)new File(string)));
            return photoSavingRequest;
        }

        public boolean isAvailable() {
            return true;
        }

        public void onPictureTaken(byte[] arrby, Camera camera) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_ON_PICT_TAKEN, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, false);
            MeasurePerformance.outResult();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, true);
            PhotoSavingRequest photoSavingRequest = this.this$0.getCopiedSavingRequest();
            photoSavingRequest.setImageData(arrby);
            BurstShooting.access$500((BurstShooting)this.this$0).mSavingTaskManager.storePicture(photoSavingRequest);
            this.this$0.setIsCapturing(false);
            Executor.sendEvent(ControllerEvent.EV_BURST_COMPRESSED_DATA, this.this$0.mPictureCount, null);
        }

        public void onShutter() {
            Executor.sendEvent(ControllerEvent.EV_SHUTTER_DONE, this.this$0.mPictureCount, null);
        }

        public void requestContinue() {
            this.this$0.prepareNextShot(false);
            this.this$0.mCurrentSavingRequest.setExtraOutput(Uri.fromFile((File)new File(this.this$0.getPath())));
            this.this$0.mController.startPreview();
            this.takePicture();
        }

        public boolean start() {
            this.takePicture();
            return true;
        }
    }

    private static interface BurstShotMethod {
        public void finish();

        public PhotoSavingRequest getSavingRequest();

        public boolean isAvailable();

        public void requestContinue();

        public boolean start();
    }

    /*
     * Failed to analyse overrides
     */
    private class High
    implements BurstShotMethod,
    CameraExtension.BurstShotCallback,
    CameraExtension.FilePathGenerator,
    CameraExtension.StorageFullDetector {
        final /* synthetic */ BurstShooting this$0;

        private High(BurstShooting burstShooting) {
            this.this$0 = burstShooting;
        }

        /* synthetic */ High(BurstShooting burstShooting,  var2_2) {
            super(burstShooting);
        }

        public void finish() {
            if (BurstShooting.access$500((BurstShooting)this.this$0).mCameraDevice.stopBurstShot(this.this$0.mBurstId) == this.this$0.mBurstId) {
                this.this$0.mController.finishBurstShot();
            }
        }

        public String getNextFilePathToStorePicture() {
            return this.this$0.getPath();
        }

        public PhotoSavingRequest getSavingRequest() {
            PhotoSavingRequest photoSavingRequest = this.this$0.createSavingRequest(null, this.this$0.mCallbackHigh);
            this.this$0.mController.setExifInfo(photoSavingRequest.common.orientation, photoSavingRequest.common.location);
            return photoSavingRequest;
        }

        public boolean isAvailable() {
            return true;
        }

        public boolean isCurrentStorageFull() {
            if (Math.max((long)0, (long)BurstShooting.access$500((BurstShooting)this.this$0).mCameraActivity.getStorageController().getAvailableStorageSize()) <= 61440) {
                return true;
            }
            return false;
        }

        public void onBurstFrameDropped(CameraExtension.BurstShotResult burstShotResult) {
            if (burstShotResult.mErrorCode == 1) {
                Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
            }
        }

        public void onBurstGroupStoreCompleted(CameraExtension.BurstShotResult burstShotResult) {
            void var5_2 = this;
            synchronized (var5_2) {
                BurstShooting.access$500((BurstShooting)this.this$0).mSavingTaskManager.getUpdator().commitBulkInsert();
                if (this.this$0.mCoverResult != null) {
                    BurstShooting.access$500((BurstShooting)this.this$0).mSavingTaskManager.notifyBurstShotGroupStoreComplete(this.this$0.mCoverResult, true);
                    this.this$0.mController.onStoreComplete(this.this$0.mCoverResult);
                }
                this.this$0.setIsCapturing(false);
                return;
            }
        }

        public void onBurstPictureStoreCompleted(CameraExtension.BurstShotResult burstShotResult) {
            if (this.this$0.isStopRequested()) {
                this.finish();
            }
            Executor.sendEvent(ControllerEvent.EV_BURST_COMPRESSED_DATA, burstShotResult.mPictureCount, null);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onBurstShutterDone(CameraExtension.BurstShotResult burstShotResult) {
            this.this$0.mController.updateSavingRequest(this.this$0.mCurrentSavingRequest, true);
            switch (burstShotResult.mErrorCode) {
                case 0: {
                    this.this$0.mPictureCount = burstShotResult.mPictureCount;
                    Executor.sendEvent(ControllerEvent.EV_SHUTTER_DONE, burstShotResult.mPictureCount, null);
                }
                default: {
                    break;
                }
                case 2: {
                    Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
                }
            }
            if (this.this$0.isStopRequested() && burstShotResult.mPictureCount >= this.this$0.mMinimumPictureCount) {
                this.finish();
            }
        }

        public void requestContinue() {
        }

        public boolean start() {
            BurstInfoUpdator burstInfoUpdator = new BurstInfoUpdator((BaseActivity)BurstShooting.access$500((BurstShooting)this.this$0).mCameraActivity, this.this$0.mCurrentSavingRequest);
            ((BurstSavingTaskManager)BurstShooting.access$500((BurstShooting)this.this$0).mCameraActivity.getSavingTaskManager()).setUpdator(burstInfoUpdator);
            this.this$0.mController.prepareBurstShot();
            this.this$0.setIsCapturing(true);
            this.this$0.mBurstId = BurstShooting.access$500((BurstShooting)this.this$0).mCameraDevice.startBurstShot((CameraExtension.BurstShotCallback)this, (CameraExtension.StorageFullDetector)this, (CameraExtension.FilePathGenerator)this, (CameraExtension.MediaProviderUpdator)burstInfoUpdator);
            if (this.this$0.mBurstId < 0) {
                this.this$0.setIsCapturing(false);
                return false;
            }
            return true;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class MessageHandler
    extends Handler {
        public static final int MSG_SHOW_EARLY_THUMBNAIL;
        final /* synthetic */ BurstShooting this$0;

        private MessageHandler(BurstShooting burstShooting) {
            this.this$0 = burstShooting;
        }

        /* synthetic */ MessageHandler(BurstShooting burstShooting,  var2_2) {
            super(burstShooting);
        }

        public void clear() {
            this.removeMessages(0);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 0: 
            }
            BurstShooting.access$500((BurstShooting)this.this$0).mCameraWindow.startEarlyThumbnailInsertAnimation(BurstShooting.access$400((BurstShooting)this.this$0).savingRequest.getRequestId());
        }
    }

    private static class Off
    implements BurstShotMethod {
        private Off() {
        }

        /* synthetic */ Off( var1) {
        }

        @Override
        public void finish() {
        }

        @Override
        public PhotoSavingRequest getSavingRequest() {
            return null;
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public void requestContinue() {
        }

        @Override
        public boolean start() {
            return false;
        }
    }

    class StoreDataCallbackBestEffort
    implements SavingRequest.StoreDataCallback {
        StoreDataCallbackBestEffort() {
        }

        @Override
        public void onStoreComplete(StoreDataResult storeDataResult) {
            if (MediaSavingUtil.isCoverPicture(storeDataResult.savingRequest.getSomcType())) {
                BurstShooting.this.storeCoverResult(storeDataResult);
            }
            if (BurstShooting.this.mCoverResult != null && BurstShooting.this.isStopRequested()) {
                BurstShooting.this.mController.onStoreComplete(BurstShooting.this.mCoverResult);
            }
        }
    }

    class StoreDataCallbackHigh
    implements SavingRequest.StoreDataCallback {
        StoreDataCallbackHigh() {
        }

        @Override
        public void onStoreComplete(StoreDataResult storeDataResult) {
            if (MediaSavingUtil.isCoverPicture(storeDataResult.savingRequest.getSomcType())) {
                BurstShooting.this.storeCoverResult(storeDataResult);
            }
        }
    }

}

