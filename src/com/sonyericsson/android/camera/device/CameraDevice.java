/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Area
 *  android.hardware.Camera$AutoFocusCallback
 *  android.hardware.Camera$CameraInfo
 *  android.hardware.Camera$ErrorCallback
 *  android.hardware.Camera$OnZoomChangeListener
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$PictureCallback
 *  android.hardware.Camera$PreviewCallback
 *  android.hardware.Camera$ShutterCallback
 *  android.hardware.Camera$Size
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.util.SparseArray
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$AutoFocusCallback
 *  com.sonyericsson.cameraextension.CameraExtension$AutoFocusResult
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotCallback
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$FilePathGenerator
 *  com.sonyericsson.cameraextension.CameraExtension$MediaProviderUpdator
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingCallback
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  com.sonyericsson.cameraextension.CameraExtension$StorageFullDetector
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.sonyericsson.android.camera.device;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.CameraSize;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.Stabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.controller.VideoDevice;
import com.sonyericsson.android.camera.device.AutoFocusListener;
import com.sonyericsson.android.camera.device.CameraDeviceException;
import com.sonyericsson.android.camera.device.CameraDeviceUtil;
import com.sonyericsson.android.camera.device.CameraSurfaceHolder;
import com.sonyericsson.android.camera.device.PreviewFrameRetriever;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.status.CameraStatusPublisher;
import com.sonyericsson.cameracommon.status.EachCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusPublisher;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;
import com.sonyericsson.cameracommon.status.eachcamera.BurstShooting;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.eachcamera.FaceDetection;
import com.sonyericsson.cameracommon.status.eachcamera.FaceIdentification;
import com.sonyericsson.cameracommon.status.eachcamera.Metadata;
import com.sonyericsson.cameracommon.status.eachcamera.ObjectTracking;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.cameracommon.status.eachcamera.SceneRecognition;
import com.sonyericsson.cameracommon.status.eachcamera.VideoNoiseReduction;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;
import com.sonyericsson.cameracommon.status.global.BuiltInCameraIds;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.ProductConfig;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.testevent.TestEventSender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * Failed to analyse overrides
 */
public class CameraDevice
implements Camera.ErrorCallback,
SurfaceHolder.Callback,
CameraExtension.SceneRecognitionCallback {
    private static final int CAMERA_IS_NOT_OPENED = -1;
    private static final int CAMERA_OPEN_FAILED = -2;
    private static final String TAG = CameraDevice.class.getSimpleName();
    private static Camera.Parameters sCachedParameters = null;
    private static volatile SparseArray<Camera.CameraInfo> sCameraInfoArray;
    private AutoFocus mAutoFocus;
    private AutoFocusListener mAutoFocusListener;
    private ShutterSound mBurstShutterSound;
    private CapabilityList mCachedCapabilityList;
    private Camera mCamera;
    private final CameraActivity mCameraActivity;
    private CameraExtension mCameraExtension;
    private int mCameraId;
    private ExecutorService mCloseDeviceExecutorService;
    private Future<?> mCloseDeviceFuture;
    private boolean mCommitParameters;
    private int mDisplayOrientation = 0;
    private boolean mIsAlreadySetBurstShutterSound;
    private boolean mIsBurstShooting;
    private volatile boolean mIsCancelOpenCameraRequested = false;
    private boolean mIsCapturing;
    private boolean mIsFaceDetectionRunning;
    private boolean mIsObjectTrackingRunning;
    private boolean mIsPreviewing;
    private boolean mIsSceneRecognitionRunning;
    private boolean mIsVideo;
    private LedLight mLedLight;
    public boolean mNeedStartPreview = false;
    private ExecutorService mOpenDeviceExecutorService;
    private Future<Camera> mOpenDeviceFuture;
    private int mOpenTaskTargetCameraId = -1;
    private Rect mPictureSize = new Rect();
    private PreviewFrameRetriever mPreviewFrameRetriever = new PreviewFrameRetriever();
    private Rect mPreviewSize = new Rect();
    private CameraExtension.SceneRecognitionCallback mSceneRecognitionCallback;
    private Future<?> mSetupDeviceFuture;
    private SurfaceHolder mSurfaceHolder;
    private VideoDevice mVideoDevice = null;
    private int mVideoFps = 0;

    static {
        if (HardwareCapability.isCameraSupported()) {
            sCameraInfoArray = new SparseArray();
            CameraDevice.setCameraInfo(sCameraInfoArray, 0);
            if (HardwareCapability.isFrontCameraSupported()) {
                CameraDevice.setCameraInfo(sCameraInfoArray, 1);
            }
        }
    }

    public CameraDevice(CameraActivity cameraActivity) {
        this.mCameraActivity = cameraActivity;
    }

    static /* synthetic */ SurfaceHolder access$1700(CameraDevice cameraDevice) {
        return cameraDevice.mSurfaceHolder;
    }

    private void clearCloseDeviceTask() {
        if (this.isCloseDeviceTaskWorking()) {
            this.joinCloseDeviceTask();
        }
        if (this.mCloseDeviceFuture != null) {
            this.mCloseDeviceFuture.cancel(true);
            this.mCloseDeviceFuture = null;
        }
        if (this.mCloseDeviceExecutorService != null) {
            this.mCloseDeviceExecutorService.shutdown();
            this.mCloseDeviceExecutorService = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void close(boolean bl) {
        if (bl) {
            this.clearResumeDeviceTask();
        }
        if (this.mCamera != null) {
            this.mPreviewFrameRetriever.dettachCamera();
        }
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).putDefaultAll().publish();
        new GlobalCameraStatusPublisher((Context)this.mCameraActivity).putDefaultAll().publish();
        void var4_2 = this;
        synchronized (var4_2) {
            if (this.mCamera == null) {
                return;
            }
            this.finishPreviewing();
            this.mPreviewFrameRetriever.dettachCamera();
            this.clearZoomChangeListener();
            this.mCamera.setErrorCallback(null);
            if (this.mCameraExtension != null) {
                if (this.mCameraId == 0) {
                    this.mCameraExtension.waitForCurrentSavingTaskStackCleared(4000);
                    this.mCameraExtension.setBurstShotCallback(null);
                    this.mCameraExtension.setBurstStorageFullDetector(null);
                }
                this.mCameraExtension.setFaceDetectionCallback(null);
                this.mCameraExtension.release();
                this.mCameraExtension = null;
                this.mBurstShutterSound = null;
                this.mIsAlreadySetBurstShutterSound = false;
            }
            this.mCamera.release();
            this.mCamera = null;
            this.setSurfaceHolder(null);
            this.mIsCapturing = false;
            return;
        }
    }

    private void finishVideoNrSetting() {
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8)) {
            this.setVideoNr(false);
        }
    }

    public static Camera.CameraInfo getCameraInfo(int n) {
        if (sCameraInfoArray == null) {
            return null;
        }
        return (Camera.CameraInfo)sCameraInfoArray.get(n);
    }

    private int getOpenTaskTargetCameraId() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            int n = this.mOpenTaskTargetCameraId;
            return n;
        }
    }

    private void initParametersCache() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "Camera is not initialized.");
            return;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        if (parameters == null) {
            CameraLogger.w(TAG, "initParametersCache: params is null.");
        }
        this.setCachedParameters(parameters);
    }

    private boolean isCapturing() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            boolean bl = this.mIsCapturing;
            return bl;
        }
    }

    private boolean isCloseDeviceTaskWorking() {
        if (this.mCloseDeviceFuture != null) {
            return true;
        }
        return false;
    }

    private boolean isFailedOpenCameraDeviceTask() {
        if (this.getOpenTaskTargetCameraId() == -2) {
            return true;
        }
        return false;
    }

    private boolean isFront() {
        if (this.mCameraId == 1) {
            return true;
        }
        return false;
    }

    private boolean isRequestCameraOpened(int n) {
        int n2 = super.getOpenTaskTargetCameraId();
        if (n2 == -1) {
            return false;
        }
        if (n2 == n) {
            return true;
        }
        super.close(false);
        return false;
    }

    public static boolean isSceneRecognitionRequired(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isSupported(String string, List<String> list) {
        if (list == null || list.indexOf((Object)string) < 0) {
            return false;
        }
        return true;
    }

    private void joinCloseDeviceTask() {
        if (this.mCloseDeviceFuture != null) {
            try {
                this.mCloseDeviceFuture.get();
                return;
            }
            catch (InterruptedException var4_1) {
                CameraLogger.e(TAG, "Camera closing has been interrupted.", (Throwable)var4_1);
                return;
            }
            catch (ExecutionException var2_2) {
                CameraLogger.e(TAG, "Camera closing failed.", (Throwable)var2_2);
                return;
            }
        }
        CameraLogger.e(TAG, "joinCloseDeviceTask: Close camera device task is not submitted.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private boolean openCameraDeviceWithRetry(int n) {
        void var9_2 = this;
        synchronized (var9_2) {
            block6 : for (int i = 0; i < 5; ++i) {
                if (this.mIsCancelOpenCameraRequested) return this.isAvailable();
                try {
                    this.open(n);
                    return this.isAvailable();
                }
                catch (CameraDeviceException var5_5) {
                    CameraLogger.e(TAG, "Open camera device failed.", (Throwable)var5_5);
                    for (int j = 0; j < 5; ++j) {
                        try {
                            if (this.mIsCancelOpenCameraRequested) continue block6;
                            Thread.sleep((long)100);
                            continue;
                        }
                        catch (InterruptedException var8_7) {
                            return this.isAvailable();
                        }
                    }
                    continue;
                }
            }
            return this.isAvailable();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void prepareVideoNrSetting() {
        boolean bl = HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8);
        boolean bl2 = HardwareCapability.getInstance().isVideoNrSupported(this.getCameraId());
        boolean bl3 = this.mVideoFps != VideoSize.FULL_HD_60FPS.getVideoFrameRate();
        if (!bl) return;
        if (bl2) {
            if (!bl3) {
                this.setVideoNr(false);
                return;
            }
            this.setVideoNr(true);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setCachedParameters(Camera.Parameters parameters) {
        int n = -1;
        if (sCachedParameters != null) {
            n = sCachedParameters.getZoom();
        }
        sCachedParameters = parameters;
        if (parameters == null) return;
        {
            Camera.Size size;
            if (n >= 0 && (parameters.getZoom() < 0 || parameters.getZoom() > parameters.getMaxZoom())) {
                sCachedParameters.setZoom(n);
            }
            if ((size = parameters.getPreviewSize()) == null) {
                sCachedParameters.setPreviewSize(this.mPreviewSize.width(), this.mPreviewSize.height());
                return;
            } else {
                if (size.width >= 0 && size.height >= 0) return;
                {
                    sCachedParameters.setPreviewSize(this.mPreviewSize.width(), this.mPreviewSize.height());
                    return;
                }
            }
        }
    }

    private static void setCameraInfo(SparseArray<Camera.CameraInfo> sparseArray, int n) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo((int)n, (Camera.CameraInfo)cameraInfo);
        StaticConfigurationUtil.setCameraInfo(cameraInfo);
        sparseArray.put(n, (Object)cameraInfo);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void setDcModeParameters(Camera.Parameters parameters) {
        void var3_2 = this;
        synchronized (var3_2) {
            CommonPlatformDependencyResolver.setDcMode(parameters, CommonPlatformDependencyResolver.ApplicationType.NORMAL);
            return;
        }
    }

    private void setOpenTaskTargetCameraId(int n) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mOpenTaskTargetCameraId = n;
            return;
        }
    }

    private void setVideoSize(Rect rect) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setVideoSize: params is null.");
            return;
        }
        parameters.set(this.getStringKey(ParameterKey.VIDEO_SIZE, true), "" + rect.width() + "x" + rect.height());
    }

    public void clearResumeDeviceTask() {
        this.mIsCancelOpenCameraRequested = true;
        if (this.isStartedOpenCameraDeviceTask()) {
            this.joinOpenCameraDeviceTask(false);
        }
        if (this.isStartedSetupCameraDeviceTask()) {
            this.joinSetupCameraDeviceTask(false);
        }
        this.mIsCancelOpenCameraRequested = false;
        if (this.mOpenDeviceFuture != null) {
            this.mOpenDeviceFuture.cancel(true);
            this.mOpenDeviceFuture = null;
        }
        if (this.mSetupDeviceFuture != null) {
            this.mSetupDeviceFuture.cancel(true);
            this.mSetupDeviceFuture = null;
        }
        if (this.mOpenDeviceExecutorService != null) {
            this.mOpenDeviceExecutorService.shutdown();
            this.mOpenDeviceExecutorService = null;
        }
        this.setOpenTaskTargetCameraId(-1);
    }

    public void clearZoomChangeListener() {
        if (this.mCamera == null) {
            return;
        }
        this.mCamera.setZoomChangeListener(null);
    }

    public void close() {
        if (!this.isRecorderWorking()) {
            this.close(true);
            return;
        }
        this.mCloseDeviceExecutorService = Executors.newSingleThreadExecutor();
        this.mCloseDeviceFuture = this.mCloseDeviceExecutorService.submit((Runnable)new CloseDeviceTask(this, null));
    }

    public void commit() {
        if (this.mCommitParameters) {
            this.commitParameters();
        }
    }

    /*
     * Exception decompiling
     */
    public void commitParameters() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.lang.ClassCastException: org.benf.cfr.reader.bytecode.analysis.parse.statement.Nop cannot be cast to org.benf.cfr.reader.bytecode.analysis.parse.statement.TryStatement
        // org.benf.cfr.reader.bytecode.analysis.parse.utils.finalhelp.FinalAnalyzer.identifyFinally(FinalAnalyzer.java:157)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.FinallyRewriter.identifyFinally(FinallyRewriter.java:40)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:446)
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

    public void dumpParameters() {
        if (sCachedParameters != null) {
            // empty if block
        }
    }

    public void finishBurstShot(Parameters parameters) {
        this.setResolution(parameters.getUpdatedResolution(parameters.getHdr()));
        this.setBurstShot(BurstShot.OFF);
        this.setJpegEncodingQuality(2);
        this.commit();
        this.startPreview(this.mSurfaceHolder);
    }

    public void finishPreviewing() {
        this.setOneShotPreviewCallback(null);
        this.stopSceneRecognition();
        this.stopFaceDetection();
        this.stopObjectTracking(true);
        this.stopBurstShot(-1);
        if (!this.isRecorderWorking()) {
            this.stopPreview();
        }
    }

    public void finishRec(Resolution resolution) {
        this.setVideoMode(this.mIsVideo);
        if (CameraSize.isAspectRatioDifferent(this.getPreviewRect(), this.getPhotoPreviewSize(resolution))) {
            this.setResolution(resolution);
        }
        this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(60, this.mCachedCapabilityList.FPS_RANGE.get()));
        this.commit();
        this.startPreview(this.mSurfaceHolder);
    }

    public AutoFocus getAutoFocus(CapabilityList capabilityList) {
        if (capabilityList.EXTENSION_VERSION.get().length() > 0) {
            return new AutoFocusExtension((CameraDevice)this, null);
        }
        return new AutoFocusNoExtension((CameraDevice)this, null);
    }

    public final Camera getCamera() {
        return this.mCamera;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    public int getDisplayOrientation() {
        return this.mDisplayOrientation;
    }

    public String getImageStabilizer(Camera.Parameters parameters) {
        String string = parameters.get(this.getStringKey(ParameterKey.STABILIZER, false));
        if (string == null) {
            string = "";
        }
        return string;
    }

    public LedLight getLedLight(CapabilityList capabilityList) {
        List<String> list = capabilityList.FLASH.get();
        if (CameraDevice.isSupported("torch", list) && CameraDevice.isSupported("off", list)) {
            return new LedLightSupported((CameraDevice)this, null);
        }
        return new LedLightNotSupported((CameraDevice)this, null);
    }

    public final int getMaxShutterSpeed() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getMaxShutterSpeed: params is null.");
            return -1;
        }
        return Integer.valueOf((String)parameters.get("sony-max-shutter-speed"));
    }

    /*
     * Enabled aggressive block sorting
     */
    public final int getMaxSrZoom() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getMaxSrZoom: params is null.");
            return 0;
        } else {
            String string = parameters.get("sony-max-sr-zoom");
            if (string == null) return 0;
            return Integer.parseInt((String)string);
        }
    }

    public final int getMaxZoom() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getMaxZoom: params is null.");
            return 0;
        }
        return parameters.getMaxZoom();
    }

    public final int getMinShutterSpeed() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getMinShutterSpeed: params is null.");
            return -1;
        }
        return Integer.valueOf((String)parameters.get("sony-min-shutter-speed"));
    }

    public Camera.Parameters getParameters() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "Camera is not initialized.");
            return null;
        }
        if (sCachedParameters == null) {
            CameraLogger.w(TAG, "sCachedParameters is null. re-get parameters.");
            this.setCachedParameters(this.mCamera.getParameters());
        }
        if (sCachedParameters == null) {
            CameraLogger.w(TAG, "camera parameters is null. getNumberOfCameras() = " + HardwareCapability.getNumberOfCameras());
        }
        return sCachedParameters;
    }

    public Rect getPhotoPreviewSize(Resolution resolution) {
        return CameraSize.getPhotoPreviewRect(resolution.getPictureRect(), this.mCachedCapabilityList.PREVIEW_SIZE_FOR_VIDEO.get(), this.mCachedCapabilityList.PREVIEW_SIZE.get());
    }

    public Rect getPictureRect() {
        return new Rect(this.mPictureSize);
    }

    public final int getPreviewFormat() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "Camera is not opened. parameters is null.");
            return -1;
        }
        return parameters.getPreviewFormat();
    }

    public PreviewFrameRetriever getPreviewFrameRetriever() {
        return this.mPreviewFrameRetriever;
    }

    public Rect getPreviewRect() {
        return new Rect(this.mPreviewSize);
    }

    public final int getShutterSpeed() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getShutterSpeed: params is null.");
            return -1;
        }
        return Integer.valueOf((String)parameters.get("sony-shutter-speed"));
    }

    public String getStringKey(ParameterKey parameterKey, boolean bl) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                return "";
            }
            case 1: {
                return "sony-is";
            }
            case 2: {
                if (bl) {
                    return "sony-vs";
                }
                return "sony-is";
            }
            case 3: 
        }
        return "video-size";
    }

    public final List<Integer> getSupportedPreviewFormats() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getSupportedPreviewFormats: params is null.");
            return new ArrayList();
        }
        return parameters.getSupportedPreviewFormats();
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceHolder;
    }

    public Rect getVideoPreviewSize(VideoSize videoSize) {
        return CameraSize.getVideoPreviewRect(videoSize.getVideoRect(), this.mCachedCapabilityList.PREVIEW_SIZE_FOR_VIDEO.get(), this.mCachedCapabilityList.PREVIEW_SIZE.get());
    }

    public final int getZoom() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getZoom: params is null.");
            return 0;
        }
        return parameters.getZoom();
    }

    /*
     * Enabled aggressive block sorting
     */
    public final boolean isAvailable() {
        if (this.mCamera == null || this.mCameraExtension == null) {
            return false;
        }
        return true;
    }

    public boolean isObjectTrackingRunning() {
        return this.mIsObjectTrackingRunning;
    }

    public boolean isOpenCameraDeviceTaskRunning() {
        if (this.mOpenDeviceFuture == null || this.mOpenDeviceFuture.isDone()) {
            return false;
        }
        return true;
    }

    public final boolean isPreviewing() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            boolean bl = this.mIsPreviewing;
            return bl;
        }
    }

    public boolean isRecorderWorking() {
        if (this.mVideoDevice == null) {
            return false;
        }
        return this.mVideoDevice.isRecorderWorking();
    }

    public boolean isSceneRecognitionRunning() {
        return this.mIsSceneRecognitionRunning;
    }

    public boolean isSetupCameraDeviceTaskRunning() {
        if (this.mSetupDeviceFuture == null || this.mSetupDeviceFuture.isDone()) {
            return false;
        }
        return true;
    }

    public boolean isStartedOpenCameraDeviceTask() {
        if (this.mOpenDeviceFuture != null) {
            return true;
        }
        return false;
    }

    public boolean isStartedSetupCameraDeviceTask() {
        if (this.mSetupDeviceFuture != null) {
            return true;
        }
        return false;
    }

    public boolean isVideoHdrOn() {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setVideoHdr: params is null.");
            return false;
        }
        String string = parameters.get("sony-video-hdr");
        boolean bl = false;
        if (string != null) {
            bl = string.equals((Object)VideoHdr.ON.getValue());
        }
        return bl;
    }

    /*
     * Loose catch block
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void joinOpenCameraDeviceTask(boolean bl) {
        if (this.mOpenDeviceFuture != null) {
            boolean bl2 = false;
            if (bl) {
                this.mOpenDeviceFuture.get();
                do {
                    return;
                    break;
                } while (true);
            }
            try {
                this.mOpenDeviceFuture.get(4000, TimeUnit.MILLISECONDS);
                return;
            }
            catch (InterruptedException var12_3) {
                CameraLogger.e(TAG, "Open camera has been interrupted.", (Throwable)var12_3);
                return;
            }
            catch (ExecutionException var9_4) {
                CameraLogger.e(TAG, "Open camera failed.", (Throwable)var9_4);
                return;
            }
            catch (TimeoutException var6_5) {
                CameraLogger.e(TAG, "Open camera is time out.", (Throwable)var6_5);
                bl2 = true;
                {
                    catch (Throwable throwable) {
                        throw throwable;
                    }
                }
                super.setOpenTaskTargetCameraId(-2);
                return;
            }
            finally {
                if (!bl2) {
                    this.mOpenDeviceFuture.cancel(true);
                    this.mOpenDeviceFuture = null;
                }
            }
        }
        CameraLogger.e(TAG, "joinOpenCameraDeviceTask: open camera device task is not submitted.");
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void joinSetupCameraDeviceTask(boolean var1) {
        ** if (this.mSetupDeviceFuture == null) goto lbl31
lbl2: // 1 sources:
        if (var1) {
            this.mSetupDeviceFuture.get();
lbl5: // 3 sources:
            do {
                return;
                break;
            } while (true);
        }
        try {
            this.mSetupDeviceFuture.get(4000, TimeUnit.MILLISECONDS);
            return;
        }
        catch (InterruptedException var11_2) {
            CameraLogger.e(CameraDevice.TAG, "SetupCameraDeviceTask has been interrupted.", (Throwable)var11_2);
            return;
        }
        catch (ExecutionException var8_3) {
            CameraLogger.e(CameraDevice.TAG, "SetupCameraDeviceTask failed.", (Throwable)var8_3);
            return;
        }
        catch (TimeoutException var5_4) {
            CameraLogger.e(CameraDevice.TAG, "SetupCameraDeviceTask is time out.", (Throwable)var5_4);
            return;
            {
                catch (Throwable var3_5) {
                    throw var3_5;
                }
            }
        }
        finally {
            ** if (true) goto lbl5
lbl25: // 1 sources:
            this.mSetupDeviceFuture.cancel(true);
            this.mSetupDeviceFuture = null;
            if (this.mOpenDeviceExecutorService == null) ** continue;
            this.mOpenDeviceExecutorService.shutdown();
            this.mOpenDeviceExecutorService = null;
        }
lbl31: // 1 sources:
        CameraLogger.e(CameraDevice.TAG, "joinSetupCameraDeviceTask: setup camera device task is not submitted.");
    }

    public void lockCamera() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "lockCamera: mCamera is null.");
            return;
        }
        this.mCamera.lock();
    }

    public boolean needStartPreview() {
        return this.mNeedStartPreview;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void onError(int n, Camera camera) {
        void var4_3 = this;
        synchronized (var4_3) {
            this.mCamera.release();
            this.mCamera = null;
        }
        Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, n, (Object)camera);
    }

    public void onSceneModeChanged(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
        if (this.mSceneRecognitionCallback != null) {
            this.mSceneRecognitionCallback.onSceneModeChanged(sceneRecognitionResult);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final void open(int var1) throws CameraDeviceException {
        var8_2 = this;
        // MONITORENTER : var8_2
        this.mCachedCapabilityList = HardwareCapability.getCapability(var1);
        super.setCachedParameters(null);
        var3_3 = this.mCamera;
        if (var3_3 == null) {
            this.mCamera = Camera.open((int)var1);
            this.mCameraId = var1;
        }
        this.mCameraExtension = CameraExtension.open((Camera)this.mCamera, (int)var1);
        if (!this.isAvailable()) {
            throw new CameraDeviceException("Open camera failed: mCamera: " + (Object)this.mCamera + ", mCameraExtension: " + (Object)this.mCameraExtension);
        } else {
            ** GOTO lbl15
        }
        catch (RuntimeException var7_4) {
            throw new CameraDeviceException("Open camera failed.", (Exception)var7_4);
        }
lbl15: // 2 sources:
        super.setCachedParameters(this.mCamera.getParameters());
        this.mCamera.setErrorCallback((Camera.ErrorCallback)this);
        this.mAutoFocus = this.getAutoFocus(this.mCachedCapabilityList);
        this.mLedLight = this.getLedLight(this.mCachedCapabilityList);
        this.mIsFaceDetectionRunning = false;
        this.mIsSceneRecognitionRunning = false;
        this.mIsObjectTrackingRunning = false;
        var8_2 = this;
        // MONITORENTER : var8_2
        this.mIsCapturing = false;
        // MONITOREXIT : var8_2
        this.mPreviewFrameRetriever.attachCamera(this.mCamera);
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new DeviceStatus(DeviceStatus.Value.POWER_ON)).publish();
        var5_5 = new GlobalCameraStatusPublisher((Context)this.mCameraActivity);
        var6_6 = new int[]{this.mCameraId};
        var5_5.put(new BuiltInCameraIds(var6_6)).publish();
        // MONITOREXIT : var8_2
    }

    public void prepareBurstShot(Parameters parameters) {
        this.setBurstFrameRate();
        this.setResolution(BurstShot.getBurstResolution(parameters));
        this.setBurstShot(parameters.getBurstShot());
        this.setJpegEncodingQuality(1);
        this.commit();
        this.startPreview(this.mSurfaceHolder);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void prepareRec(VideoSize videoSize) {
        this.setVideoMode(true);
        if (CameraSize.isAspectRatioDifferent(this.getPreviewRect(), this.getVideoPreviewSize(videoSize))) {
            this.setPreviewSizeAndFpsRangeForVideo(videoSize);
        } else {
            this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(videoSize.getVideoFrameRate(), this.mCachedCapabilityList.FPS_RANGE.get()));
        }
        this.commit();
        this.startPreview(this.mSurfaceHolder);
    }

    public void reconnectCamera() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "reconnectCamera: mCamera is null.");
            return;
        }
        this.setCachedParameters(null);
        try {
            this.mCamera.reconnect();
            this.initParametersCache();
            return;
        }
        catch (IOException var1_1) {
            CameraLogger.e(TAG, "reconnect() failed.", (Throwable)var1_1);
            return;
        }
    }

    public final void release() {
        this.setSceneRecognitionCallback(null);
        this.setAutoFocusListener(null);
    }

    public void resetFocusAreaAndRect(FocusMode focusMode) {
        if (focusMode != null) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "resetFocusAreaAndRect: getParameters() fail. params is null");
                return;
            }
            parameters.set("sony-focus-area", focusMode.getFocusArea());
        }
        this.setFocusRect(new Rect());
    }

    public void resetZoom() {
        this.setZoom(0);
    }

    public void restoreSettingAfterRecording() {
        this.mVideoDevice = null;
        this.reconnectCamera();
        this.stopVideoMetadata();
        this.setVideoRecordingSound(this.mCameraActivity.getParameterManager().getParameters().getShutterSound());
        this.commit();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void resume(Parameters parameters) {
        this.mCachedCapabilityList = HardwareCapability.getCapability(parameters.capturingMode.getCameraId());
        Rect rect = parameters.capturingMode.getType() == 2 || this.mCameraActivity.isOneShotVideo() ? this.getVideoPreviewSize(parameters.getVideoSize()) : this.getPhotoPreviewSize(parameters.getResolution());
        this.updatePreviewSize(rect);
        this.startSetupCameraDeviceTask(parameters);
    }

    public void setAutoFocusListener(AutoFocusListener autoFocusListener) {
        this.mAutoFocusListener = autoFocusListener;
    }

    public void setBurstFrameRate() {
        Camera.Parameters parameters;
        int n = this.mCameraActivity.getResources().getInteger(2131230722);
        int n2 = this.mCachedCapabilityList.MAX_BURST_SHOT_FRAME_RATE.get();
        if (n2 < n) {
            n = n2;
        }
        if ((parameters = this.getParameters()) == null) {
            CameraLogger.e(TAG, "setBurstFrameRate: params is null.");
            return;
        }
        parameters.set("sony-burst-shot-frame-rate", n);
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setBurstShot(BurstShot burstShot) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setBurstShot: params is null.");
                return;
            }
            parameters.set("sony-burst-shot", burstShot.getValue());
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setBurstShutterSound(ShutterSound shutterSound) {
        if (this.mCameraExtension == null || this.mCameraId != 0 || this.mBurstShutterSound == shutterSound && this.mIsAlreadySetBurstShutterSound) {
            return;
        }
        this.mBurstShutterSound = shutterSound;
        this.mIsAlreadySetBurstShutterSound = true;
        String string = ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_BURST_SHUTTER, shutterSound);
        this.mCameraExtension.setBurstShutterSoundFilePath(string);
    }

    public void setCapturingMode(CapturingMode capturingMode) {
        this.mIsVideo = false;
        if (capturingMode.getType() == 2 || this.mCameraActivity.isOneShotVideo()) {
            this.mIsVideo = true;
        }
        this.setVideoMode(this.mIsVideo);
        if (!this.mIsVideo) {
            this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(60, this.mCachedCapabilityList.FPS_RANGE.get()));
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setDisplayOrientation(int n) {
        if (this.mCamera == null) {
            return;
        }
        CameraActivity cameraActivity = this.mCameraActivity;
        Camera.CameraInfo cameraInfo = (Camera.CameraInfo)sCameraInfoArray.get(n);
        int n2 = 360 - ProductConfig.getMountAngle((Context)cameraActivity);
        int n3 = cameraInfo.facing == 1 ? (360 - (n2 + cameraInfo.orientation) % 360) % 360 : (360 + (cameraInfo.orientation - n2)) % 360;
        try {
            this.mCamera.setDisplayOrientation(n3);
            this.mDisplayOrientation = n3;
            return;
        }
        catch (RuntimeException var6_6) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setEv(Ev ev) {
        void var4_2 = this;
        synchronized (var4_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                return;
            }
            parameters.setExposureCompensation(ev.getIntValue());
        }
        this.mCommitParameters = true;
    }

    public void setExifInfo(Integer n, Double d, Double d2, Double d3, Long l) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            this.mCommitParameters = false;
            return;
        }
        boolean bl = false;
        if (n != null) {
            parameters.setRotation(n.intValue());
            bl = true;
        }
        parameters.removeGpsData();
        if (d != null && d2 != null) {
            parameters.setGpsLatitude(d.doubleValue());
            parameters.setGpsLongitude(d2.doubleValue());
            bl|=true;
        }
        if (d3 != null) {
            parameters.setGpsAltitude(d3.doubleValue());
            bl|=true;
        }
        if (l != null) {
            parameters.setGpsTimestamp(l.longValue());
            bl|=true;
        }
        this.mCommitParameters = bl;
    }

    public final void setFaceDetectionCallback(CameraExtension.FaceDetectionCallback faceDetectionCallback) {
        if (this.mCameraExtension == null) {
            return;
        }
        this.mCameraExtension.setFaceDetectionCallback(faceDetectionCallback);
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void setFlashMode(Flash flash) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setFlashMode: params is null.");
            return;
        }
        parameters.setFlashMode(flash.getValue());
        this.mCommitParameters = true;
        EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId);
        PhotoLight.Value value = parameters.getFlashMode() != null && "torch".equals((Object)parameters.getFlashMode()) ? PhotoLight.Value.ON : PhotoLight.Value.OFF;
        eachCameraStatusPublisher.put(new PhotoLight(value)).publish();
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void setFocusMode(FocusMode focusMode) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setFocusMode: params is null.");
            return;
        }
        if (this.mIsVideo) {
            parameters.setFocusMode(focusMode.getValueForVideo());
        } else {
            parameters.setFocusMode(focusMode.getValue());
        }
        if (!super.isFront()) {
            this.resetFocusAreaAndRect(focusMode);
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void setFocusRect(Rect rect) {
        int n;
        List list;
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setFocusRect: params is null.");
            return;
        }
        if (parameters.getMaxNumFocusAreas() < 1) return;
        if (rect.width() == 0 || rect.height() == 0) {
            if (parameters.get("sony-focus-area").equals((Object)"user")) {
                rect.set(-100, -100, 100, 100);
            } else {
                rect = new Rect();
            }
        }
        if (rect.isEmpty()) {
            n = 0;
        } else {
            n = 1000;
            parameters.set("sony-focus-area", "user");
        }
        if ((list = parameters.getFocusAreas()) == null) {
            Camera.Area area = new Camera.Area(new Rect(), n);
            list = new ArrayList();
            list.add((Object)area);
        }
        ((Camera.Area)list.get((int)0)).rect = rect;
        ((Camera.Area)list.get((int)0)).weight = n;
        parameters.setFocusAreas(list);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setFpsRange(int[] arrn) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setFpsRange: params is null.");
            return;
        } else {
            if (arrn.length <= 0) return;
            {
                parameters.setPreviewFpsRange(arrn[0], arrn[1]);
                parameters.setPreviewFrameRate(arrn[1] / 1000);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setHdr(Hdr hdr) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setHdr: params is null.");
                return;
            }
            if (hdr == Hdr.HDR_OFF && this.getImageStabilizer(parameters).equals((Object)Stabilizer.ON.getValue())) {
                return;
            }
            parameters.set(this.getStringKey(hdr.getParameterKey(), this.mIsVideo), hdr.getValue());
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setIso(Iso iso) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                return;
            }
            parameters.set("sony-ae-mode", iso.getValue());
            int n = iso.getIsoValue();
            if (n > 0) {
                parameters.set("sony-iso", Integer.toString((int)n));
            }
        }
        this.mCommitParameters = true;
    }

    public final void setJpegEncodingQuality(int n) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setJpegEncodingQuality: params is null.");
            return;
        }
        parameters.setJpegQuality(MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(n));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setMetering(Metering metering) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setMetering: params is null.");
                return;
            }
            parameters.set("sony-metering-mode", metering.getValue());
        }
        this.mCommitParameters = true;
    }

    public final void setOneShotPreviewCallback(Camera.PreviewCallback previewCallback) {
        if (this.mCamera != null) {
            this.mCamera.setOneShotPreviewCallback(previewCallback);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPhotoShutterSound(boolean bl) {
        if (this.mCamera == null || 17 > Build.VERSION.SDK_INT) {
            return;
        }
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getParameters() return null.");
            return;
        }
        if (bl) {
            parameters.set("key-sony-ext-shuttersound", "/system/media/audio/ui/camera_click.ogg");
        } else {
            parameters.set("key-sony-ext-shuttersound", ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_OFF, ShutterSound.OFF));
        }
        this.mCommitParameters = true;
    }

    public void setPictureSize(Rect rect) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setPictureSize: params is null.");
            return;
        }
        parameters.setPictureSize(rect.width(), rect.height());
        this.mPictureSize = new Rect(rect);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setPreviewDisplay(SurfaceHolder surfaceHolder) throws IOException {
        void var3_2 = this;
        synchronized (var3_2) {
            if (surfaceHolder != null) {
                this.setSurfaceHolder(surfaceHolder);
                if (this.mCamera != null) {
                    this.mCamera.setPreviewDisplay(surfaceHolder);
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPreviewSize(Rect rect) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setPreviewSize: params is null.");
            return;
        }
        if (rect.width() != this.mPreviewSize.width() || rect.height() != this.mPreviewSize.height()) {
            this.stopPreview();
            this.mNeedStartPreview = true;
        } else {
            this.mNeedStartPreview = false;
        }
        parameters.setPreviewSize(rect.width(), rect.height());
        this.updatePreviewSize(rect);
        PreviewFrameRetriever.PreviewInfo previewInfo = new PreviewFrameRetriever.PreviewInfo(parameters.getPreviewFormat(), parameters.getPreviewSize().width, parameters.getPreviewSize().height);
        this.mPreviewFrameRetriever.setPreviewInfo(previewInfo);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setPreviewSizeAndFpsRangeForVideo(VideoSize videoSize) {
        Rect rect = this.getVideoPreviewSize(videoSize);
        void var4_3 = this;
        synchronized (var4_3) {
            super.setVideoSize(videoSize.getVideoRect());
            this.setPreviewSize(rect);
            this.setFpsRange(CameraDeviceUtil.computePreviewFpsRange(videoSize.getVideoFrameRate(), this.mCachedCapabilityList.FPS_RANGE.get()));
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setResolution(Resolution resolution) {
        Rect rect = this.getPhotoPreviewSize(resolution);
        void var4_3 = this;
        synchronized (var4_3) {
            this.setPictureSize(resolution.getPictureRect());
            this.setPreviewSize(rect);
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setScene(Scene scene) {
        void var6_2 = this;
        synchronized (var6_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setScene: params is null.");
                return;
            }
            if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8)) {
                int n = HardwareCapability.getInstance().getMaxSoftSkinLevel(this.mCameraId);
                if (Scene.SOFT_SKIN == scene) {
                    parameters.set("sony-soft-skin-level-for-picture", SoftSkin.ON.getLevel(n));
                    scene = Scene.PORTRAIT;
                } else {
                    parameters.set("sony-soft-skin-level-for-picture", SoftSkin.OFF.getLevel(n));
                }
            }
            parameters.setSceneMode(scene.getValue());
        }
        this.mCommitParameters = true;
    }

    public final void setSceneRecognitionCallback(CameraExtension.SceneRecognitionCallback sceneRecognitionCallback) {
        this.mSceneRecognitionCallback = sceneRecognitionCallback;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void setSelectedFacePosition(int n, int n2) {
        if (this.mCameraExtension == null) {
            CameraLogger.w(TAG, "setSelectFacePos: mCameraExtension is null.");
            return;
        } else {
            if (!this.mIsPreviewing || !this.mIsFaceDetectionRunning) return;
            {
                this.mCameraExtension.setSelectFacePos(n, n2);
                return;
            }
        }
    }

    public void setShutterSound(ShutterSound shutterSound) {
        this.setPhotoShutterSound(shutterSound.getBooleanValue());
        this.setVideoRecordingSound(shutterSound);
        this.setBurstShutterSound(shutterSound);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setSmileCapture(SmileCapture smileCapture) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setSmileCapture: params is null.");
                return;
            }
            parameters.set("sony-smile-detect", smileCapture.getValue());
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setSoftSkin(SoftSkin softSkin) {
        if (this.mIsVideo || this.mCameraId == 0) {
            return;
        }
        void var7_2 = this;
        synchronized (var7_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setSoftSkin: params is null.");
                return;
            }
            if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8)) {
                CapturingMode capturingMode = this.mCameraActivity.getControllerManager().getCapturingMode();
                if (softSkin.isEnable() && !capturingMode.isSuperiorAuto()) {
                    List<String> list = HardwareCapability.getCapability((int)this.mCameraId).SCENE.get();
                    if (list.contains((Object)Scene.PORTRAIT.getValue()) && HardwareCapability.getInstance().getMaxSoftSkinLevel(this.mCameraId) > 0) {
                        parameters.setSceneMode(Scene.PORTRAIT.getValue());
                    } else if (list.contains((Object)Scene.SOFT_SKIN.getValue())) {
                        parameters.setSceneMode(Scene.SOFT_SKIN.getValue());
                    }
                } else {
                    parameters.setSceneMode(Scene.OFF.getValue());
                }
                parameters.set("sony-soft-skin-level-for-picture", softSkin.getLevel(HardwareCapability.getInstance().getMaxSoftSkinLevel(this.mCameraId)));
            } else {
                parameters.setSceneMode(softSkin.getScene().getValue());
            }
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setStabilizer(Stabilizer stabilizer) {
        void var6_2 = this;
        synchronized (var6_2) {
            String string;
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setStabilizer: params is null.");
                return;
            }
            parameters.set(this.getStringKey(stabilizer.getParameterKey(), true), Stabilizer.OFF.getValue());
            if (stabilizer == Stabilizer.OFF && ((string = this.getImageStabilizer(parameters)).equals((Object)Hdr.HDR_AUTO.getValue()) || string.equals((Object)Hdr.HDR_ON.getValue()))) {
                return;
            }
            parameters.set(this.getStringKey(stabilizer.getParameterKey(), false), stabilizer.getValue());
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setSteadyShot(VideoStabilizer videoStabilizer) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setSteadyShot: params is null.");
                return;
            }
            parameters.set(this.getStringKey(videoStabilizer.getParameterKey(), false), Stabilizer.OFF.getValue());
            parameters.set("sony-vs", videoStabilizer.getValue());
        }
        this.mCommitParameters = true;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder = surfaceHolder;
    }

    public void setVideoDevice(VideoDevice videoDevice) {
        this.mVideoDevice = videoDevice;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setVideoHdr(VideoHdr videoHdr) {
        void var5_2 = this;
        synchronized (var5_2) {
            if (videoHdr.getValue() == null) {
                return;
            }
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setVideoHdr: params is null.");
                return;
            }
            parameters.set("sony-video-hdr", videoHdr.getValue());
        }
        this.mCommitParameters = true;
    }

    public void setVideoMode(boolean bl) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setVideoMode: params is null.");
            return;
        }
        parameters.setRecordingHint(bl);
        if (bl) {
            super.prepareVideoNrSetting();
            return;
        }
        super.finishVideoNrSetting();
    }

    public void setVideoNr(boolean bl) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "prepareVideoNr: params is null.");
            return;
        }
        if (bl) {
            parameters.set("sony-video-nr", "on");
            return;
        }
        parameters.set("sony-video-nr", "off");
    }

    public void setVideoPictureSize(Rect rect) {
        Rect rect2;
        if (this.mCachedCapabilityList.VIDEO_SNAPSHOT.get().booleanValue() && (rect2 = CameraSize.getOptimalVideoSnapshotSize(rect, this.mCachedCapabilityList.PICTURE_SIZE.get())) != null) {
            this.setPictureSize(rect2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setVideoRecordingSound(ShutterSound shutterSound) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "getParameters() return null.");
            return;
        }
        String string = shutterSound.getBooleanValue() != false ? "/system/media/audio/ui/VideoRecord.ogg" : ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_OFF, shutterSound);
        parameters.set("key-sony-ext-recordingsound", string);
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setVideoSize(VideoSize videoSize) {
        if (videoSize == null) {
            return;
        }
        Rect rect = videoSize.getVideoRect();
        String string = "" + rect.width() + "x" + rect.height();
        void var6_4 = this;
        synchronized (var6_4) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                return;
            }
            parameters.set("video-size", string);
        }
        this.mVideoFps = videoSize.getVideoFrameRate();
        if (this.mIsVideo) {
            super.prepareVideoNrSetting();
        }
        this.mCommitParameters = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setWhiteBalance(WhiteBalance whiteBalance) {
        void var5_2 = this;
        synchronized (var5_2) {
            Camera.Parameters parameters = this.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "setWhiteBalance: params is null.");
                return;
            }
            parameters.setWhiteBalance(whiteBalance.getValue());
        }
        this.mCommitParameters = true;
    }

    public final void setZoom(int n) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters == null) {
            CameraLogger.e(TAG, "setZoom: params is null.");
            return;
        }
        parameters.setZoom(n);
    }

    public boolean startAutoFocus(AutoFocusListener autoFocusListener, boolean bl, boolean bl2, boolean bl3) {
        if (!this.mIsPreviewing) {
            return false;
        }
        try {
            this.mAutoFocus.startAutoFocus(autoFocusListener, bl, bl2, bl3);
            if (Configurations.isLogForOperatorsEnabled()) {
                CameraLogger.dForOperators("AutoFocus started");
            }
            return true;
        }
        catch (Exception var5_5) {
            CameraLogger.e(TAG, "startAutoFocus failed.", (Throwable)var5_5);
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int startBurstShot(CameraExtension.BurstShotCallback burstShotCallback, CameraExtension.StorageFullDetector storageFullDetector, CameraExtension.FilePathGenerator filePathGenerator, CameraExtension.MediaProviderUpdator mediaProviderUpdator) {
        if (!this.mIsPreviewing || this.mIsBurstShooting) {
            return -1;
        }
        this.mCameraExtension.setBurstShotCallback(burstShotCallback);
        this.mCameraExtension.setBurstStorageFullDetector(storageFullDetector);
        int n = this.mCameraExtension.startBurstShot(filePathGenerator, mediaProviderUpdator);
        this.mIsBurstShooting = true;
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new BurstShooting(BurstShooting.Value.ON)).publish();
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startFaceDetection() {
        EachCameraStatusPublisher eachCameraStatusPublisher;
        FaceIdentification.Value value;
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startFaceDetection: mCamera is null.");
            return;
        } else {
            if (!this.mIsPreviewing || this.mIsFaceDetectionRunning) return;
            {
                this.mCamera.startFaceDetection();
                this.mIsFaceDetectionRunning = true;
                eachCameraStatusPublisher = new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId);
                value = FaceIdentify.ON.getValue().equals((Object)this.mCameraActivity.getParameterManager().getParameters().getFaceIdentify().getValue()) ? FaceIdentification.Value.ON : FaceIdentification.Value.OFF;
            }
        }
        eachCameraStatusPublisher.put(new FaceIdentification(value)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.ON)).publish();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startObjectTracking(Rect rect, CameraExtension.ObjectTrackingCallback objectTrackingCallback) {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startObjectTracking: mCamera is null.");
            return;
        } else {
            if (this.mCachedCapabilityList.MAX_NUM_FOCUS_AREA.get() < 1 || this.mCameraExtension == null || !this.mIsPreviewing) return;
            {
                this.mCameraExtension.setObjectTrackingCallback(objectTrackingCallback, 75, 100);
                this.mCameraExtension.startObjectTracking();
                this.mCameraExtension.selectObject(rect.centerX(), rect.centerY());
                this.mIsObjectTrackingRunning = true;
                new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new ObjectTracking(ObjectTracking.Value.ON)).publish();
                return;
            }
        }
    }

    public void startOpenCameraDeviceTask(int n) {
        super.clearCloseDeviceTask();
        super.setOpenTaskTargetCameraId(-1);
        this.mOpenDeviceExecutorService = Executors.newSingleThreadExecutor();
        this.mIsCancelOpenCameraRequested = false;
        this.mOpenDeviceFuture = this.mOpenDeviceExecutorService.submit((Callable)new OpenCameraDeviceTask(n));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startPreview() throws CameraDeviceException {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startPreview: mCamera is null.");
            return;
        }
        Camera.Parameters parameters = this.getParameters();
        if (parameters != null) {
            DeviceStatus.Value value = this.mIsVideo ? DeviceStatus.Value.VIDEO_PREVIEW : DeviceStatus.Value.STILL_PREVIEW;
            DeviceStatus deviceStatus = new DeviceStatus(value);
            String string = parameters.get("sony-vs");
            String string2 = parameters.get("sony-video-nr");
            new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).putFromParameter(parameters).put(deviceStatus).put((DeviceStatus)VideoStabilizerStatus.fromCameraParameter(string)).put((VideoStabilizerStatus)VideoNoiseReduction.fromCameraParameter(string2)).publish();
        }
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            if (this.mIsPreviewing) {
                return;
            }
            try {
                this.mCamera.startPreview();
                this.mIsPreviewing = true;
                return;
            }
            catch (RuntimeException var3_7) {
                this.mIsPreviewing = false;
                throw new CameraDeviceException("Start preview failed.", (Exception)var3_7);
            }
        }
    }

    public void startPreview(SurfaceHolder surfaceHolder) {
        try {
            this.setPreviewDisplay(surfaceHolder);
            this.startPreview();
            return;
        }
        catch (IOException var3_2) {
            Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, -256, (Object)this.mCamera);
            return;
        }
        catch (CameraDeviceException var2_3) {
            Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, -255, (Object)this.mCamera);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startSceneRecognition() {
        if (this.mCameraExtension == null || !this.mIsPreviewing || this.mIsSceneRecognitionRunning) {
            return;
        }
        this.startFaceDetection();
        this.mCameraExtension.startSceneRecognition(this.mSceneRecognitionCallback);
        this.mIsSceneRecognitionRunning = true;
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new SceneRecognition(SceneRecognition.Value.ON)).publish();
    }

    public void startSetupCameraDeviceTask(Parameters parameters) {
        super.clearCloseDeviceTask();
        if (this.mOpenDeviceExecutorService == null) {
            this.mOpenDeviceExecutorService = Executors.newSingleThreadExecutor();
        }
        SetupCameraDeviceTask setupCameraDeviceTask = new SetupCameraDeviceTask(parameters);
        this.mSetupDeviceFuture = this.mOpenDeviceExecutorService.submit((Runnable)setupCameraDeviceTask);
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void startSmoothZoom(int n) {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startSmoothZoom: mCamera is null.");
            return;
        } else {
            if (!this.isPreviewing()) return;
            {
                this.mCamera.startSmoothZoom(n);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startSmoothZoom(int n, Camera.OnZoomChangeListener onZoomChangeListener) {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startSmoothZoom: mCamera is null.");
            return;
        } else {
            if (!this.isPreviewing()) return;
            {
                this.mCamera.setZoomChangeListener(onZoomChangeListener);
                this.mCamera.startSmoothZoom(n);
                return;
            }
        }
    }

    public void startVideoMetadata() {
        if (HardwareCapability.getInstance().isVideoMetaDataSupported(this.mCameraId) && this.mCameraExtension != null) {
            this.mCameraExtension.startVideoMetadata();
            new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new Metadata(Metadata.Value.ON)).publish();
        }
    }

    public final void stopAutoFocus() {
        if (this.mAutoFocus != null) {
            this.mAutoFocus.stopAutoFocus();
        }
    }

    public int stopBurstShot(int n) {
        if (!this.mIsBurstShooting) {
            return -1;
        }
        this.mIsBurstShooting = false;
        this.mCameraExtension.stopBurstShot();
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new BurstShooting(BurstShooting.Value.OFF)).publish();
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void stopFaceDetection() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "startFaceDetection: mCamera is null.");
            return;
        } else {
            if (!this.mIsFaceDetectionRunning) return;
            {
                this.mIsFaceDetectionRunning = false;
                this.mCamera.stopFaceDetection();
                new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new FaceIdentification(FaceIdentification.Value.OFF)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.OFF)).publish();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void stopObjectTracking(boolean bl) {
        if (!(this.mCameraExtension != null && this.mIsObjectTrackingRunning)) {
            return;
        }
        this.mIsObjectTrackingRunning = false;
        try {
            this.mCameraExtension.deselectObject();
            if (bl) {
                this.mCameraExtension.setObjectTrackingCallback(null, 0, 0);
            }
        }
        catch (RuntimeException var2_2) {
            CameraLogger.e(TAG, "ObjectTrackingDevice stop failed.", (Throwable)var2_2);
        }
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new ObjectTracking(ObjectTracking.Value.OFF)).publish();
    }

    public void stopObjectTrackingCallback() {
        if (this.mCameraExtension != null) {
            this.mCameraExtension.setObjectTrackingCallback(null, 0, 0);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void stopPreview() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            if (this.mCamera == null) {
                CameraLogger.w(TAG, "stopPreview: mCamera is null.");
                this.mIsPreviewing = false;
            } else if (this.mIsPreviewing) {
                this.mIsPreviewing = false;
                this.mCamera.stopPreview();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void stopSceneRecognition() {
        if (!(this.mCameraExtension != null && this.mIsSceneRecognitionRunning)) {
            return;
        }
        this.mIsSceneRecognitionRunning = false;
        this.mCameraExtension.stopSceneRecognition();
        new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new SceneRecognition(SceneRecognition.Value.OFF)).publish();
    }

    public final void stopSmoothZoom() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "stopSmoothZoom: mCamera is null.");
            return;
        }
        this.mCamera.stopSmoothZoom();
    }

    public void stopVideoMetadata() {
        if (HardwareCapability.getInstance().isVideoMetaDataSupported(this.mCameraId) && this.mCameraExtension != null) {
            this.mCameraExtension.stopVideoMetadata();
            new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId).put(new Metadata(Metadata.Value.OFF)).publish();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int n, int n2, int n3) {
        this.setSurfaceHolder(surfaceHolder);
        CameraSurfaceHolder cameraSurfaceHolder = new CameraSurfaceHolder(surfaceHolder, n2, n3);
        Executor.sendEvent(ControllerEvent.EV_SURFACE_CHANGED, 0, cameraSurfaceHolder);
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Executor.sendEvent(ControllerEvent.EV_SURFACE_CREATED, 0, (Object)surfaceHolder);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.setSurfaceHolder(null);
        Executor.sendEmptyEvent(ControllerEvent.EV_SURFACE_DESTROYED);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void suspend(boolean bl, boolean bl2) {
        void var4_3 = this;
        synchronized (var4_3) {
            if (bl2) {
                this.stopSceneRecognition();
            }
            if (bl) {
                this.stopFaceDetection();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public final void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback pictureCallback) {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "takePicture: mCamera is null.");
            return;
        }
        EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher((Context)this.mCameraActivity, this.mCameraId);
        DeviceStatus.Value value = Executor.isRecording() ? DeviceStatus.Value.PICTURE_TAKING_DURING_VIDEO_RECORDING : DeviceStatus.Value.PICTURE_TAKING;
        eachCameraStatusPublisher.put(new DeviceStatus(value)).publish();
        JpegCallback jpegCallback = new JpegCallback((CameraDevice)this, pictureCallback, null);
        void var8_6 = this;
        synchronized (var8_6) {
            this.mIsCapturing = true;
            if (!Executor.isRecording()) {
                this.mIsPreviewing = false;
            }
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_ON_PICT_TAKEN, true);
            this.mCamera.takePicture(shutterCallback, null, (Camera.PictureCallback)jpegCallback);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public final void turnOffLight() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            if (this.mLedLight != null) {
                this.mLedLight.turnOff();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public final void turnOnLight() {
        CameraDevice cameraDevice = this;
        synchronized (cameraDevice) {
            if (this.mLedLight != null) {
                this.mLedLight.turnOn();
            }
            return;
        }
    }

    public void unlockCamera() {
        if (this.mCamera == null) {
            CameraLogger.w(TAG, "unlockCamera: mCamera is null.");
            return;
        }
        this.mCamera.unlock();
    }

    public void updatePreviewSize(Rect rect) {
        this.mPreviewSize = new Rect(rect);
        PositionConverter.getInstance().setPreviewSize(rect.width(), rect.height());
    }

    private abstract class AutoFocus {
        final /* synthetic */ CameraDevice this$0;

        private AutoFocus(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
        }

        /* synthetic */ AutoFocus(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        public abstract void startAutoFocus(AutoFocusListener var1, boolean var2, boolean var3, boolean var4) throws Exception;

        public abstract void stopAutoFocus();
    }

    /*
     * Failed to analyse overrides
     */
    private class AutoFocusExtension
    extends AutoFocus
    implements CameraExtension.AutoFocusCallback {
        final /* synthetic */ CameraDevice this$0;

        private AutoFocusExtension(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
            super(cameraDevice, null);
        }

        /* synthetic */ AutoFocusExtension(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        public void onAutoFocus(CameraExtension.AutoFocusResult autoFocusResult) {
            if (this.this$0.mAutoFocusListener != null) {
                AutoFocusResultWrapper autoFocusResultWrapper = new AutoFocusResultWrapper(autoFocusResult);
                this.this$0.mAutoFocusListener.onAutoFocus(autoFocusResultWrapper);
            }
        }

        public void startAutoFocus(AutoFocusListener autoFocusListener, boolean bl, boolean bl2, boolean bl3) throws Exception {
            if (this.this$0.mCameraExtension == null) {
                CameraLogger.w(TAG, "startAutoFocus: mCameraExtension is null.");
                return;
            }
            this.this$0.setAutoFocusListener(autoFocusListener);
            this.this$0.mCameraExtension.setFaceDetectionCallback(null);
            this.this$0.mCameraExtension.startAutoFocus((CameraExtension.AutoFocusCallback)this, bl, bl2, bl3);
        }

        public void stopAutoFocus() {
            if (this.this$0.mCameraExtension == null) {
                CameraLogger.w(TAG, "stopAutoFocus: mCameraExtension is null.");
                return;
            }
            this.this$0.mCameraExtension.setFaceDetectionCallback(null);
            this.this$0.mCameraExtension.stopAutoFocus();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class AutoFocusNoExtension
    extends AutoFocus
    implements Camera.AutoFocusCallback {
        final /* synthetic */ CameraDevice this$0;

        private AutoFocusNoExtension(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
            super(cameraDevice, null);
        }

        /* synthetic */ AutoFocusNoExtension(CameraDevice cameraDevice, com.sonyericsson.android.camera.device.CameraDevice$1 var2_2) {
            super(cameraDevice);
        }

        private void autoFocus() {
            new Handler().post((Runnable)new Runnable(){

                public void run() {
                    AutoFocusNoExtension.this.onAutoFocus(true, AutoFocusNoExtension.this.this$0.mCamera);
                }
            });
        }

        public void onAutoFocus(boolean bl, Camera camera) {
            if (this.this$0.mAutoFocusListener != null) {
                AutoFocusResultWrapper autoFocusResultWrapper = new AutoFocusResultWrapper(bl);
                this.this$0.mAutoFocusListener.onAutoFocus(autoFocusResultWrapper);
            }
        }

        public void startAutoFocus(AutoFocusListener autoFocusListener, boolean bl, boolean bl2, boolean bl3) throws Exception {
            if (this.this$0.mCamera == null) {
                CameraLogger.w(TAG, "startAutoFocus: mCamera is null.");
                return;
            }
            this.this$0.setAutoFocusListener(autoFocusListener);
            Camera.Parameters parameters = this.this$0.getParameters();
            if (parameters == null) {
                CameraLogger.e(TAG, "startAutoFocus: params is null.");
                return;
            }
            String string = parameters.getFocusMode();
            if (string.equals((Object)"continuous-video")) {
                super.autoFocus();
                return;
            }
            if (string.equals((Object)"edof")) {
                super.autoFocus();
                return;
            }
            if (string.equals((Object)"fixed")) {
                super.autoFocus();
                return;
            }
            if (string.equals((Object)"infinity")) {
                super.autoFocus();
                return;
            }
            this.this$0.mCamera.autoFocus((Camera.AutoFocusCallback)this);
        }

        public void stopAutoFocus() {
            if (this.this$0.mCamera == null) {
                CameraLogger.w(TAG, "stopAutoFocus: mCamera is null.");
                return;
            }
            this.this$0.mCamera.cancelAutoFocus();
        }

    }

    private static class AutoFocusResultWrapper
    implements AutoFocusListener.Result {
        private final CameraExtension.AutoFocusResult mResult;
        private final boolean mSuccess;

        public AutoFocusResultWrapper(CameraExtension.AutoFocusResult autoFocusResult) {
            this.mSuccess = false;
            this.mResult = autoFocusResult;
        }

        public AutoFocusResultWrapper(boolean bl) {
            this.mSuccess = bl;
            this.mResult = null;
        }

        @Override
        public boolean isFocused() {
            if (this.mResult == null) {
                return this.mSuccess;
            }
            return this.mResult.isFocused();
        }

        @Override
        public boolean isFocused(int n) {
            if (this.mResult == null) {
                return false;
            }
            return this.mResult.isFocused(n);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class CloseDeviceTask
    implements Runnable {
        final /* synthetic */ CameraDevice this$0;

        private CloseDeviceTask(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
        }

        /* synthetic */ CloseDeviceTask(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        public void run() {
            if (this.this$0.mVideoDevice != null) {
                this.this$0.mVideoDevice.awaitFinishingRecording();
            }
            this.this$0.restoreSettingAfterRecording();
            this.this$0.mCameraActivity.getShutterToneGenerator().releaseResources();
            this.this$0.close(true);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private final class JpegCallback
    implements Camera.PictureCallback {
        private final Camera.PictureCallback mClientJpegCallback;
        final /* synthetic */ CameraDevice this$0;

        private JpegCallback(CameraDevice cameraDevice, Camera.PictureCallback pictureCallback) {
            this.this$0 = cameraDevice;
            this.mClientJpegCallback = pictureCallback;
        }

        /* synthetic */ JpegCallback(CameraDevice cameraDevice, Camera.PictureCallback pictureCallback,  var3_2) {
            super(cameraDevice, pictureCallback);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void onPictureTaken(byte[] arrby, Camera camera) {
            CameraDevice cameraDevice;
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_ON_PICT_TAKEN, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, false);
            MeasurePerformance.outResult();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, true);
            TestEventSender.onPictureTaken();
            CameraDevice cameraDevice2 = cameraDevice = this.this$0;
            synchronized (cameraDevice2) {
                this.this$0.mIsCapturing = false;
            }
            this.mClientJpegCallback.onPictureTaken(arrby, camera);
            EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher((Context)this.this$0.mCameraActivity, this.this$0.mCameraId);
            DeviceStatus.Value value = Executor.isRecording() ? DeviceStatus.Value.VIDEO_RECORDING : DeviceStatus.Value.STILL_PREVIEW;
            eachCameraStatusPublisher.put(new DeviceStatus(value)).publish();
            if (CameraLogger.isUserdebugBuild) {
                CameraLogger.p(TAG, "JpegCallback.onPictureTaken out");
            }
        }
    }

    private abstract class LedLight {
        final /* synthetic */ CameraDevice this$0;

        private LedLight(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
        }

        /* synthetic */ LedLight(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        public abstract void turnOff();

        public abstract void turnOn();
    }

    private class LedLightNotSupported
    extends LedLight {
        final /* synthetic */ CameraDevice this$0;

        private LedLightNotSupported(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
            super(cameraDevice, null);
        }

        /* synthetic */ LedLightNotSupported(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        @Override
        public void turnOff() {
        }

        @Override
        public void turnOn() {
        }
    }

    private class LedLightSupported
    extends LedLight {
        final /* synthetic */ CameraDevice this$0;

        private LedLightSupported(CameraDevice cameraDevice) {
            this.this$0 = cameraDevice;
            super(cameraDevice, null);
        }

        /* synthetic */ LedLightSupported(CameraDevice cameraDevice,  var2_2) {
            super(cameraDevice);
        }

        @Override
        public void turnOff() {
            this.this$0.setFlashMode(Flash.LED_OFF);
            this.this$0.commitParameters();
        }

        @Override
        public void turnOn() {
            this.this$0.setFlashMode(Flash.LED_ON);
            this.this$0.commitParameters();
        }
    }

    private class OpenCameraDeviceTask
    implements Callable<Camera> {
        private final int mCameraId;

        public OpenCameraDeviceTask(int n) {
            this.mCameraId = n;
        }

        public Camera call() {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.OPEN_CAMERA_DEVICE_TASK, true);
            boolean bl = CameraDevice.this.openCameraDeviceWithRetry(this.mCameraId);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.OPEN_CAMERA_DEVICE_TASK, false);
            if (bl) {
                CameraDevice.this.setOpenTaskTargetCameraId(this.mCameraId);
                return CameraDevice.this.mCamera;
            }
            CameraDevice.this.setOpenTaskTargetCameraId(-2);
            return null;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SetupCameraDeviceTask
    implements Runnable {
        private final Handler mHandler;
        private final Parameters mParams;

        public SetupCameraDeviceTask(Parameters parameters) {
            this.mHandler = new Handler();
            this.mParams = parameters;
        }

        /*
         * Enabled aggressive block sorting
         */
        private void applyAllParameters() {
            CameraDevice.this.setDisplayOrientation(CameraDevice.this.mCameraId);
            CameraDevice.this.setJpegEncodingQuality(2);
            CameraDevice.this.setCapturingMode(this.mParams.capturingMode);
            CameraDevice.this.setEv(this.mParams.getEv());
            CameraDevice.this.setFocusMode(this.mParams.getFocusMode());
            CameraDevice.this.setVideoSize(this.mParams.getVideoSize());
            if (CameraDevice.this.mIsVideo) {
                CameraDevice.this.setPreviewSizeAndFpsRangeForVideo(this.mParams.getVideoSize());
                CameraDevice.this.setScene(this.mParams.getScene());
                CameraDevice.this.setSmileCapture(SmileCapture.OFF);
                CameraDevice.this.setSteadyShot(this.mParams.getVideoStabilizer());
            } else {
                Hdr hdr = this.mParams.getHdr();
                CameraDevice.this.setHdr(hdr);
                CameraDevice.this.setResolution(this.mParams.getUpdatedResolution(hdr));
                CameraDevice.this.setIso(this.mParams.getIso());
                if (CameraDevice.this.mCameraId == 1) {
                    CameraDevice.this.setSoftSkin(this.mParams.getSoftSkin());
                } else {
                    CameraDevice.this.setScene(this.mParams.getScene());
                }
                CameraDevice.this.setSmileCapture(this.mParams.getSmileCapture());
                CameraDevice.this.setStabilizer(this.mParams.getStabilizer());
            }
            CameraDevice.this.setMetering(this.mParams.getMetering());
            CameraDevice.this.setShutterSound(this.mParams.getShutterSound());
            CameraDevice.this.setVideoHdr(this.mParams.getVideoHdr());
            CameraDevice.this.setWhiteBalance(this.mParams.getWhiteBalance());
            CameraDevice.this.setDcModeParameters(sCachedParameters);
            CameraDevice.this.commit();
        }

        private boolean isCameraAvailable() {
            if (CameraDevice.this.isFailedOpenCameraDeviceTask()) {
                CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] Camera open was failed.");
                GoogleAnalyticsUtil.setCameraNotAvailableFailedToOpen();
                return false;
            }
            if (CameraDevice.this.isRequestCameraOpened(this.mParams.capturingMode.getCameraId())) {
                return true;
            }
            if (CameraDevice.this.openCameraDeviceWithRetry(this.mParams.capturingMode.getCameraId())) {
                return true;
            }
            CameraLogger.e(TAG, "SetupCameraDeviceTask: Camera is not available.");
            return false;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Lifted jumps to return sites
         */
        public void run() {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SETUP_CAMERA_DEVICE_TASK, true);
            var1_1 = this.isCameraAvailable();
            var2_2 = false;
            ** if (!var1_1) goto lbl-1000
lbl5: // 1 sources:
            this.applyAllParameters();
            try {
                CameraDevice.this.setPreviewDisplay(CameraDevice.access$1700(CameraDevice.this));
                CameraDevice.this.startPreview();
                var2_2 = true;
            }
            catch (IOException var8_3) {
                CameraLogger.e(CameraDevice.access$700(), "setPreviewDisplay failed.", (Throwable)var8_3);
                var2_2 = false;
            }
            catch (CameraDeviceException var6_4) {
                CameraLogger.e(CameraDevice.access$700(), "startPreview failed.", (Throwable)var6_4);
                var2_2 = false;
            }
            if (var2_2) {
                this.mHandler.post((Runnable)new Runnable(){

                    public void run() {
                        Executor.postEmptyEvent(ControllerEvent.EV_CAMERA_SETUP_FINISHED);
                    }
                });
            } else lbl-1000: // 2 sources:
            {
                this.mHandler.post((Runnable)new Runnable(){

                    public void run() {
                        Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, 100, (Object)CameraDevice.this.mCamera);
                    }
                });
            }
            if (Configurations.isLogForOperatorsEnabled()) {
                CameraLogger.dForOperators("Camera is launched completely");
            }
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SETUP_CAMERA_DEVICE_TASK, false);
        }

    }

}

