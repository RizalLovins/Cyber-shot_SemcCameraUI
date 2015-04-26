/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.YuvImage
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$PreviewCallback
 *  android.hardware.Camera$Size
 *  android.location.Location
 *  android.os.Handler
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.graphics.YuvImage;
import android.hardware.Camera;
import android.location.Location;
import android.os.Handler;
import com.sonyericsson.android.camera.mediasaving.MediaSavingUtil;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingResult;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PreviewFrameGrabber {
    private ExecutorService mExecService = Executors.newSingleThreadExecutor();
    private int mFrameHeight = 0;
    private int mFrameWidth = 0;
    private Handler mHandler = new Handler();
    private OnPreviewFrameGrabbedListener mListener = null;
    private int mPreviewFormat = 0;
    private PhotoSavingRequest mSavingRequest;

    public PreviewFrameGrabber(PhotoSavingRequest photoSavingRequest) {
        this.mSavingRequest = photoSavingRequest;
    }

    private void callbackCaptureToUiThread(byte[] arrby) {
        UiThreadCaptureCallbackTask uiThreadCaptureCallbackTask = new UiThreadCaptureCallbackTask(arrby);
        this.mHandler.post((Runnable)uiThreadCaptureCallbackTask);
    }

    public void release() {
        this.mListener = null;
        this.mSavingRequest = null;
    }

    public void requestFrame(Camera camera) {
        if (camera == null) {
            throw new NullPointerException("Handed camera object is null");
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            throw new RuntimeException("Camera.getParameters() return NULL");
        }
        if (parameters.getPreviewSize() == null) {
            throw new RuntimeException("Camera.Parameters.getPreviewSize() return NULL");
        }
        this.mPreviewFormat = parameters.getPreviewFormat();
        this.mFrameWidth = parameters.getPreviewSize().width;
        this.mFrameHeight = parameters.getPreviewSize().height;
        camera.setOneShotPreviewCallback((Camera.PreviewCallback)new OnPreviewFrameCallback());
    }

    public void setOnPreviewFrameGrabbedListener(OnPreviewFrameGrabbedListener onPreviewFrameGrabbedListener) {
        this.mListener = onPreviewFrameGrabbedListener;
    }

    /*
     * Failed to analyse overrides
     */
    private class ConvertYuvToJpegAndCallbackTask
    implements Runnable {
        final byte[] mYuvData;

        public ConvertYuvToJpegAndCallbackTask(byte[] arrby) {
            this.mYuvData = arrby;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void run() {
            MediaSavingResult mediaSavingResult;
            ByteArrayOutputStream byteArrayOutputStream;
            MediaSavingResult mediaSavingResult2;
            PhotoSavingRequest photoSavingRequest;
            if (this.mYuvData == null) {
                PreviewFrameGrabber.this.callbackCaptureToUiThread(null);
                return;
            }
            YuvImage yuvImage = new YuvImage(this.mYuvData, PreviewFrameGrabber.this.mPreviewFormat, PreviewFrameGrabber.this.mFrameWidth, PreviewFrameGrabber.this.mFrameHeight, null);
            MediaSavingResult mediaSavingResult3 = MediaSavingUtil.convertYuvToJpegOutputStream(yuvImage, (OutputStream)(byteArrayOutputStream = new ByteArrayOutputStream()), photoSavingRequest = new PhotoSavingRequest(new TakenStatusCommon(PreviewFrameGrabber.this.mSavingRequest.getDateTaken(), PreviewFrameGrabber.access$600((PreviewFrameGrabber)PreviewFrameGrabber.this).common.orientation, PreviewFrameGrabber.access$600((PreviewFrameGrabber)PreviewFrameGrabber.this).common.location, PreviewFrameGrabber.this.mFrameWidth, PreviewFrameGrabber.this.mFrameHeight, PreviewFrameGrabber.access$600((PreviewFrameGrabber)PreviewFrameGrabber.this).common.mimeType, ".JPG", SavingTaskManager.SavedFileType.PHOTO_DURING_REC, null, null, false, false), new TakenStatusPhoto()));
            if (mediaSavingResult3 == (mediaSavingResult = MediaSavingResult.SUCCESS)) {
                try {
                    byteArrayOutputStream.flush();
                }
                catch (IOException var9_8) {
                    var9_8.printStackTrace();
                    mediaSavingResult3 = MediaSavingResult.FAIL;
                }
            }
            if (mediaSavingResult3 == (mediaSavingResult2 = MediaSavingResult.SUCCESS)) {
                PreviewFrameGrabber.this.callbackCaptureToUiThread(byteArrayOutputStream.toByteArray());
            } else {
                PreviewFrameGrabber.this.callbackCaptureToUiThread(null);
            }
            try {
                byteArrayOutputStream.close();
                return;
            }
            catch (IOException var8_7) {
                var8_7.printStackTrace();
                return;
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnPreviewFrameCallback
    implements Camera.PreviewCallback {
        OnPreviewFrameCallback() {
        }

        public void onPreviewFrame(byte[] arrby, Camera camera) {
            if (PreviewFrameGrabber.this.mListener != null) {
                PreviewFrameGrabber.this.mListener.onPreviewShutterDone();
            }
            ConvertYuvToJpegAndCallbackTask convertYuvToJpegAndCallbackTask = new ConvertYuvToJpegAndCallbackTask(arrby);
            PreviewFrameGrabber.this.mExecService.execute((Runnable)convertYuvToJpegAndCallbackTask);
        }
    }

    public static interface OnPreviewFrameGrabbedListener {
        public void onPreviewFrameGrabbed(PreviewFrameGrabber var1, byte[] var2);

        public void onPreviewShutterDone();
    }

    /*
     * Failed to analyse overrides
     */
    private class UiThreadCaptureCallbackTask
    implements Runnable {
        private final byte[] mJpegData;

        public UiThreadCaptureCallbackTask(byte[] arrby) {
            this.mJpegData = arrby;
        }

        public void run() {
            if (PreviewFrameGrabber.this.mListener != null) {
                PreviewFrameGrabber.this.mListener.onPreviewFrameGrabbed(PreviewFrameGrabber.this, this.mJpegData);
            }
        }
    }

}

