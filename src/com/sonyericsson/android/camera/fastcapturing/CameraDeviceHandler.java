/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.admin.DevicePolicyManager
 *  android.content.ComponentName
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Area
 *  android.hardware.Camera$CameraInfo
 *  android.hardware.Camera$ErrorCallback
 *  android.hardware.Camera$OnZoomChangeListener
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$PictureCallback
 *  android.hardware.Camera$PreviewCallback
 *  android.hardware.Camera$ShutterCallback
 *  android.hardware.Camera$Size
 *  android.location.Location
 *  android.media.AudioManager
 *  android.media.CamcorderProfile
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.util.Log
 *  android.view.OrientationEventListener
 *  android.view.SurfaceHolder
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$AutoFocusCallback
 *  com.sonyericsson.cameraextension.CameraExtension$AutoFocusResult
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotCallback
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotResult
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$FilePathGenerator
 *  com.sonyericsson.cameraextension.CameraExtension$MediaProviderUpdator
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingCallback
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingResult
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  com.sonyericsson.cameraextension.CameraExtension$StorageFullDetector
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.InterruptedException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.concurrent.CancellationException
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import com.sonyericsson.android.camera.CameraSize;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.configuration.parameters.CaptureFrameShape;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Microphone;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.Stabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.device.CameraDeviceUtil;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingCameraUtils;
import com.sonyericsson.android.camera.fastcapturing.PlatformDependencyResolver;
import com.sonyericsson.android.camera.fastcapturing.PreviewFrameGrabber;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.android.camera.smartcover.SmartCoverActivity;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.ShutterSound;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import com.sonyericsson.cameracommon.device.CommonPlatformDependencyResolver;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.VideoSavingRequest;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
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
import com.sonyericsson.cameracommon.status.eachcamera.VideoRecordingFps;
import com.sonyericsson.cameracommon.status.eachcamera.VideoResolution;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.PresetConfigurationResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.utility.StaticConfigurationUtil;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.cameraparametervalidator.CameraParameterValidator;
import com.sonymobile.cameracommon.media.recorder.RecorderException;
import com.sonymobile.cameracommon.media.recorder.RecorderFactory;
import com.sonymobile.cameracommon.media.recorder.RecorderInterface;
import com.sonymobile.cameracommon.media.recorder.RecorderParameters;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentificationFactory;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentificationUtil;
import com.sonymobile.cameracommon.testevent.TestEventSender;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CameraDeviceHandler {
    public static final int STATUS_OPENED = 2;
    public static final int STATUS_OPENING = 1;
    public static final int STATUS_RELEASED = 0;
    private static final String TAG = "CameraDeviceHandler";
    private BurstShot mBurstShotSetting = null;
    private Rect mCamcordRect = null;
    private Camera mCamera = null;
    private int mCameraDeviceId = 0;
    private CameraExtension mCameraExtension = null;
    private Camera.CameraInfo mCameraInfo = null;
    private ExecutorService mCloseDeviceExecutorService;
    private Future<?> mCloseDeviceFuture;
    private CommonSettings mCommonSettings = null;
    private Context mContext = null;
    private int mCurrentDeviceState = 0;
    private Camera.ErrorCallback mErrorCallback = null;
    private com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification mFaceIdService = null;
    private FaceIdentify mFaceIdentifySetting = null;
    private FastCaptureOrientation mFastCaptureOrientation = null;
    private FastCapture mFastCaptureSetting = null;
    private Flash mFlashSetting = null;
    private GeotagManager mGeotagManager = null;
    private boolean mIsCameraDisabled = false;
    private boolean mIsFaceDetectionAlreadyStarted = false;
    private boolean mIsImmediateReleaseRequested = false;
    private boolean mIsLaunchAndCaptureShutterSoundEnabled = false;
    private boolean mIsObjectTrackingAlreadyStarted = false;
    private boolean mIsPreScanSucceeded = false;
    private Boolean mIsRecording = false;
    private boolean mIsSceneRecognitionAlreadyStarted = false;
    private boolean mIsSecureOneShotPhoto = false;
    private boolean mIsSmartCoverCamera = false;
    private PhotoSavingRequest mLastSavingRequest;
    private VideoSavingRequest mLastVideoSavingRequest;
    private int mLatestBurstId = -1;
    private Camera.Parameters mLatestCachedParameters = null;
    private LoadSettingsThread mLoadSettingsThread = null;
    private final OnAutoFocusCallback mOnAutoFocusCallback;
    private final OnFaceDetectionCallback mOnFaceDetectionCallback;
    private final OnObjectTrackedCallback mOnObjectTrackedCallback;
    private final OnPictureTakenCallback mOnPictureTakenCallback;
    private OnPreAutoFocusCallback mOnPreAutoFocusCallback;
    private final OnSceneModeChangedCallback mOnSceneModeChangedCallback;
    private final OnShutterCallback mOnShutterCallback;
    private OnZoomChangedCallback mOnZoomChangedCallback;
    private OpenDeviceThread mOpenDeviceThread = null;
    private CaptureFrameShape mPhotoCaptureFrameShapeSetting = null;
    private FocusMode mPhotoFocusModeSetting = null;
    private Hdr mPhotoHdrSetting = null;
    private PhotoLight mPhotoLightSetting = null;
    private Resolution mPhotoResolutionSetting = null;
    private Stabilizer mPhotoStabilizerSetting = null;
    private Rect mPictureRect = null;
    private PreProcessState mPreProcessState = PreProcessState.NOT_STARTED;
    private SharedPreferences mPreferences = null;
    private PreviewFrameGrabber mPreviewFrameGrabber = null;
    private Rect mPreviewRect = null;
    private final RequestContinuousPreviewFrameCallback mRequestContinuousPreviewFrameCallback;
    private final RequestOnePreviewFrameCallback mRequestOnePreviewFrameCallback;
    private com.sonyericsson.cameracommon.commonsetting.values.ShutterSound mShutterSoundSetting = null;
    private SoftSkin mSoftSkinSetting = null;
    private StateMachine mStateMachine;
    private Handler mUiThreadHandler = new Handler();
    private FocusMode mVideoFocusModeSetting = null;
    private VideoHdr mVideoHdrSetting = null;
    private Microphone mVideoMicrophoneSetting = null;
    private RecorderInterface mVideoRecorder;
    private Scene mVideoSceneSetting = null;
    private VideoSize mVideoSizeSetting = null;
    private VideoStabilizer mVideoStabilizerSetting = null;
    private WhiteBalance mVideoWhiteBalanceSetting = null;

    public CameraDeviceHandler() {
        this.mRequestOnePreviewFrameCallback = new RequestOnePreviewFrameCallback(this, null);
        this.mRequestContinuousPreviewFrameCallback = new RequestContinuousPreviewFrameCallback(this, null);
        this.mOnAutoFocusCallback = new OnAutoFocusCallback();
        this.mOnShutterCallback = new OnShutterCallback();
        this.mOnPictureTakenCallback = new OnPictureTakenCallback();
        this.mOnSceneModeChangedCallback = new OnSceneModeChangedCallback();
        this.mOnFaceDetectionCallback = new OnFaceDetectionCallback();
        this.mOnObjectTrackedCallback = new OnObjectTrackedCallback();
    }

    static /* synthetic */ Camera.CameraInfo access$3300(CameraDeviceHandler cameraDeviceHandler) {
        return cameraDeviceHandler.mCameraInfo;
    }

    private void awaitFinishingRecording() {
        if (this.mVideoRecorder == null) {
            return;
        }
        this.mVideoRecorder.awaitFinish();
    }

    private void changePreProcessStateTo(PreProcessState preProcessState) {
        this.mPreProcessState = preProcessState;
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

    private PhotoSavingRequest createPreCaptureSavingRequest() {
        long l = System.currentTimeMillis();
        int n = this.mFastCaptureOrientation.getOrientation();
        GeotagManager geotagManager = this.mGeotagManager;
        Location location = null;
        if (geotagManager != null) {
            location = this.mGeotagManager.getCurrentLocation();
        }
        Rect rect = this.getPictureRect();
        PhotoSavingRequest photoSavingRequest = new PhotoSavingRequest(new TakenStatusCommon(l, n, location, rect.width(), rect.height(), "image/jpeg", ".JPG", SavingTaskManager.SavedFileType.PHOTO, null, "", false, false), new TakenStatusPhoto());
        return photoSavingRequest;
    }

    private void doSetParametersToDevice(Camera camera, Camera.Parameters parameters) {
        camera.setParameters(parameters);
        this.mLatestCachedParameters.remove("key-sony-ext-shuttersound");
        this.mLatestCachedParameters.remove("key-sony-ext-recordingsound");
    }

    private void finishVideoNrSetting() {
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8)) {
            this.setVideoNr(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private Camera getCameraInstance() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            boolean bl = this.mIsImmediateReleaseRequested;
            Camera camera = null;
            if (bl) return camera;
            boolean bl2 = this.isOpenDeviceTaskFinishedSuccessfully();
            camera = null;
            if (!bl2) return camera;
            return this.mCamera;
        }
    }

    private static int getCameraTypeFrom(FastCapture fastCapture) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[fastCapture.ordinal()]) {
            default: {
                throw new IllegalStateException("getCameraTypeFrom():[FastCapture=" + fastCapture + "]");
            }
            case 1: 
            case 2: 
            case 3: {
                return 1;
            }
            case 4: 
        }
        return 2;
    }

    private String getDependHdrIsValue(Hdr hdr, Stabilizer stabilizer) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$Hdr[hdr.ordinal()]) {
            default: {
                return null;
            }
            case 1: {
                return hdr.getValue();
            }
            case 2: {
                return hdr.getValue();
            }
            case 3: 
        }
        if (stabilizer == Stabilizer.ON) {
            return stabilizer.getValue();
        }
        return hdr.getValue();
    }

    private FaceIdentify getFaceIdentify() {
        return this.mFaceIdentifySetting;
    }

    private boolean isMicrophoneEnabled() {
        if (this.mVideoMicrophoneSetting.equals((Object)Microphone.ON)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private boolean isOpenDeviceTaskFinishedSuccessfully() {
        if (this.mOpenDeviceThread == null) return false;
        try {
            this.mOpenDeviceThread.join(4000);
            if (this.mOpenDeviceThread == null) {
                return false;
            }
            if (!this.mOpenDeviceThread.isAlive()) return this.mOpenDeviceThread.isSuccess();
            {
                CameraLogger.e("CameraDeviceHandler", "mOpenDeviceThread.join() timeout.");
                return false;
            }
        }
        catch (CancellationException var3_1) {
            CameraLogger.e("CameraDeviceHandler", "isOpenDeviceTaskFinishedSuccessfully():[task is canceled]", (Throwable)var3_1);
            return false;
        }
        catch (InterruptedException var1_2) {
            CameraLogger.e("CameraDeviceHandler", "isOpenDeviceTaskFinishedSuccessfully():[task is Interrupted]", (Throwable)var1_2);
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean isPictureSizeSupported(Rect rect, Camera.Parameters parameters) {
        if (parameters.getSupportedPictureSizes() == null) {
            return false;
        }
        Iterator iterator = parameters.getSupportedPictureSizes().iterator();
        do {
            if (!iterator.hasNext()) return false;
            Camera.Size size = (Camera.Size)iterator.next();
        } while (size.width != rect.width() || size.height != rect.height());
        return true;
    }

    private static boolean isPreProcessRequiredFrom(FastCapture fastCapture) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[fastCapture.ordinal()]) {
            default: {
                throw new IllegalStateException("isPreAfRequiredFrom():[FastCapture=" + fastCapture + "]");
            }
            case 1: 
            case 2: 
            case 4: {
                return false;
            }
            case 3: 
        }
        return true;
    }

    private boolean isSceneRecognitionSupported() {
        return PlatformDependencyResolver.isSceneRecognitionSupported(this.mLatestCachedParameters);
    }

    private void joinCloseDeviceTask() {
        if (this.mCloseDeviceFuture != null) {
            try {
                this.mCloseDeviceFuture.get();
                return;
            }
            catch (InterruptedException var4_1) {
                CameraLogger.e("CameraDeviceHandler", "Camera closing has been interrupted.", (Throwable)var4_1);
                return;
            }
            catch (ExecutionException var2_2) {
                CameraLogger.e("CameraDeviceHandler", "Camera closing failed.", (Throwable)var2_2);
                return;
            }
        }
        CameraLogger.e("CameraDeviceHandler", "joinCloseDeviceTask: Close camera device task is not submitted.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void loadSettings() {
        if (this.mContext != null) {
            Context context;
            Context context2 = context = this.mContext;
            synchronized (context2) {
                this.mPreferences = this.mContext.getSharedPreferences("com.sonyericsson.android.camera.shared_preferences", 0);
            }
        }
        if (this.mContext != null) {
            this.mCommonSettings = new CommonSettings(this.mContext.getContentResolver(), this.mContext);
            this.mCommonSettings.load();
        }
        if (this.mFastCaptureSetting == null) {
            this.mFastCaptureSetting = (FastCapture)this.mCommonSettings.get(CommonSettingKey.FAST_CAPTURE);
        }
        this.mShutterSoundSetting = (com.sonyericsson.cameracommon.commonsetting.values.ShutterSound)this.mCommonSettings.get(CommonSettingKey.SHUTTER_SOUND);
        if (!FastCapturingCameraUtils.isSettingValueAvailableInSharedPreferences(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.BURST_SHOT)) {
            return;
        }
        this.mFlashSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.FLASH, Flash.AUTO);
        this.mPhotoLightSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.PHOTO_LIGHT, PhotoLight.OFF);
        this.mPhotoResolutionSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.RESOLUTION, Resolution.VGA);
        this.mPhotoCaptureFrameShapeSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.CAPTURE_FRAME_SHAPE, CaptureFrameShape.WIDE);
        this.mPhotoFocusModeSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.FOCUS_MODE, FocusMode.SINGLE);
        this.mPhotoStabilizerSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, this.mCameraDeviceId, 1, ParameterKey.STABILIZER, Stabilizer.OFF);
        this.mPhotoHdrSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, this.mCameraDeviceId, 1, ParameterKey.HDR, Hdr.HDR_OFF);
        this.mFaceIdentifySetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.FACE_IDENTIFY, FaceIdentify.OFF);
        this.mSoftSkinSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.SOFT_SKIN, SoftSkin.ON);
        this.mBurstShotSetting = this.mIsSecureOneShotPhoto ? BurstShot.OFF : FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.BURST_SHOT, BurstShot.BEST_EFFORT);
        if (!(this.mFaceIdentifySetting != FaceIdentify.ON || this.mContext == null || FaceIdentificationUtil.exist(this.mContext))) {
            this.mFaceIdentifySetting = FaceIdentify.OFF;
        }
        this.mVideoSizeSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.VIDEO_SIZE, VideoSize.VGA);
        this.mVideoSceneSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.SCENE, Scene.OFF);
        this.mVideoFocusModeSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.FOCUS_MODE, FocusMode.FACE_DETECTION);
        this.mVideoWhiteBalanceSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.WHITE_BALANCE, WhiteBalance.AUTO);
        this.mVideoStabilizerSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.VIDEO_STABILIZER, VideoStabilizer.ON);
        this.mVideoMicrophoneSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.MICROPHONE, Microphone.ON);
        this.mVideoHdrSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 2, this.mCameraDeviceId, ParameterKey.VIDEO_HDR, VideoHdr.OFF);
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + "CameraDeviceHandler" + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    /*
     * Exception decompiling
     */
    private boolean openCamera() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
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

    private void overlayBurstCaptureParameters(int n, Camera.Parameters parameters) {
        if (!PlatformDependencyResolver.isBurstCaptureSupported(parameters)) {
            throw new IllegalStateException("setBurstCaptureParameters():[Burst not supported]");
        }
        if (this.mShutterSoundSetting == com.sonyericsson.cameracommon.commonsetting.values.ShutterSound.ON && this.mCameraDeviceId == 0) {
            String string = ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_BURST_SHUTTER, ShutterSound.SOUND1);
            this.mCameraExtension.setBurstShutterSoundFilePath(string);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void prepareMediaRecorder(VideoSavingRequest videoSavingRequest, RecorderInterface.RecorderListener recorderListener, RecorderInterface.RecordingSoundPlayer recordingSoundPlayer, boolean bl) {
        if (this.mVideoRecorder != null) {
            throw new IllegalStateException("prepareMediaRecorder():[not NULL]");
        }
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        camera.unlock();
        Context context = this.mContext;
        Handler handler = this.mUiThreadHandler;
        boolean bl2 = VideoStabilizer.isIntelligentActive(this.mVideoStabilizerSetting) && bl;
        this.mVideoRecorder = RecorderFactory.create(context, recorderListener, recordingSoundPlayer, handler, 1000, bl2);
        CamcorderProfile camcorderProfile = this.mVideoSizeSetting.getVideoProfile().getCamcorderProfile();
        if (camcorderProfile == null) {
            throw new RuntimeException("CamcorderProfile is null.");
        }
        RecorderParameters recorderParameters = RecorderParameters.newRecordParameters().setLocation(videoSavingRequest.common.location).setMaxDuration((int)videoSavingRequest.video.maxDurationMills).setMaxFileSize(videoSavingRequest.video.maxFileSizeBytes).setMicrophoneEnabled(super.isMicrophoneEnabled()).setOrientationHint(videoSavingRequest.common.orientation).setProfile(camcorderProfile).setUri(Uri.fromFile((File)new File(videoSavingRequest.getFilePath()))).build();
        if (this.mVideoRecorder.prepare(this.mCamera, recorderParameters)) return;
        CameraLogger.e("CameraDeviceHandler", "prepareMediaRecorder() : failed");
        super.awaitFinishingRecording();
        throw new RuntimeException("prepareMediaRecorder():[Failed to prepare MediaRecorder.]");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void prepareVideoNrSetting() {
        boolean bl = HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8);
        boolean bl2 = HardwareCapability.getInstance().isVideoNrSupported(this.mCameraDeviceId);
        boolean bl3 = this.mVideoSizeSetting != VideoSize.FULL_HD_60FPS;
        if (!bl) return;
        if (bl2 && bl3) {
            this.setVideoNr(true);
        }
    }

    private void prepareZoom(Camera camera) {
        this.mOnZoomChangedCallback = new OnZoomChangedCallback();
        camera.setZoomChangeListener((Camera.OnZoomChangeListener)this.mOnZoomChangedCallback);
    }

    private void reconnectCamera() {
        Camera camera = this.getCameraInstance();
        if (camera == null) {
            return;
        }
        try {
            camera.reconnect();
            return;
        }
        catch (IOException var2_2) {
            throw new RuntimeException("reconnectCamera():[Failed to reconnect Camera.]");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void releaseCamera() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            switch (this.mCurrentDeviceState) {
                default: {
                    this.isOpenDeviceTaskFinishedSuccessfully();
                    this.releaseCameraImmediately();
                    this.mIsImmediateReleaseRequested = false;
                }
                case 0: {
                    break;
                }
                case 1: {
                    this.mIsImmediateReleaseRequested = true;
                }
            }
            return;
        }
    }

    private void releaseCameraImmediately() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.releaseCameraImmediatelyNotSynchronized();
            return;
        }
    }

    private void releaseCameraImmediatelyNotSynchronized() {
        this.stopSceneRecognitionNotSynchronized();
        this.stopFaceDetectionNotSynchronized();
        this.deselectObject();
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).putDefaultAll().publish();
        new GlobalCameraStatusPublisher(this.mContext).putDefaultAll().publish();
        this.mPreviewRect = null;
        this.mPictureRect = null;
        this.mCamcordRect = null;
        this.mFastCaptureSetting = null;
        this.mShutterSoundSetting = null;
        this.mFlashSetting = null;
        this.mPhotoResolutionSetting = null;
        this.mPhotoCaptureFrameShapeSetting = null;
        this.mPhotoFocusModeSetting = null;
        this.mPhotoStabilizerSetting = null;
        this.mPhotoHdrSetting = null;
        this.mBurstShotSetting = null;
        this.mFaceIdentifySetting = null;
        this.mVideoSizeSetting = null;
        this.mVideoSceneSetting = null;
        this.mVideoFocusModeSetting = null;
        this.mVideoWhiteBalanceSetting = null;
        this.mVideoStabilizerSetting = null;
        this.mVideoMicrophoneSetting = null;
        this.mVideoHdrSetting = null;
        if (this.mGeotagManager != null) {
            this.mGeotagManager.releaseResource();
            this.mGeotagManager.release();
            this.mGeotagManager = null;
        }
        if (this.mFastCaptureOrientation != null) {
            this.mFastCaptureOrientation.disable();
            this.mFastCaptureOrientation = null;
        }
        this.mOpenDeviceThread = null;
        this.mLoadSettingsThread = null;
        this.mContext = null;
        if (this.mCameraExtension != null && this.mCameraDeviceId == 0) {
            this.mCameraExtension.waitForCurrentSavingTaskStackCleared(4000);
            this.mCameraExtension.setBurstShotCallback(null);
            this.mCameraExtension.setBurstStorageFullDetector(null);
        }
        this.mCameraInfo = null;
        if (this.mCamera != null) {
            this.mCamera.setZoomChangeListener(null);
            this.mCamera.setErrorCallback(null);
            this.mCamera.stopPreview();
            Camera camera = this.mCamera;
            this.mCamera = null;
            camera.release();
        }
        if (this.mCameraExtension != null) {
            this.mCameraExtension.release();
            this.mCameraExtension = null;
        }
        this.changePreProcessStateTo(PreProcessState.NOT_STARTED);
        this.mLatestCachedParameters = null;
        this.mIsPreScanSucceeded = false;
        this.mIsLaunchAndCaptureShutterSoundEnabled = false;
        this.mCurrentDeviceState = 0;
    }

    private void resetShutterSound() {
        if (this.mIsLaunchAndCaptureShutterSoundEnabled) {
            this.mLatestCachedParameters.set("key-sony-ext-shuttersound", "/system/media/audio/ui/camera_click.ogg");
            this.trySetParametersToDevice(this.mLatestCachedParameters);
            this.mIsLaunchAndCaptureShutterSoundEnabled = false;
        }
    }

    private void selectDefaultCameraLightSettingsIfSharedPreferencesIsEmpty(Camera.Parameters parameters) {
        if (this.mFlashSetting == null && PlatformDependencyResolver.isFlashLightSupported(parameters)) {
            this.mFlashSetting = Flash.getFlashFromParameterString(PlatformDependencyResolver.getDefaultFlash(parameters));
        }
        if (this.mPhotoLightSetting == null && PlatformDependencyResolver.isPhotoLightSupported(parameters)) {
            this.mPhotoLightSetting = PhotoLight.getPhotoLightFromParameterString(PlatformDependencyResolver.getDefaultPhotolight(parameters));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean setCameraLight(String string) {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            return false;
        }
        parameters.setFlashMode(string);
        EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId);
        PhotoLight.Value value = "torch".equals((Object)string) ? PhotoLight.Value.ON : PhotoLight.Value.OFF;
        eachCameraStatusPublisher.put(new com.sonyericsson.cameracommon.status.eachcamera.PhotoLight(value)).publish();
        return this.trySetParametersToDevice(parameters);
    }

    private void setCommonCaptureParameters(int n, Camera.Parameters parameters) {
        super.setSceneToParameters(n, parameters);
        super.setFocusModeToParameters(n, parameters);
        super.setWhiteBalanceToParameters(n, parameters);
        super.setMeteringToParameters(n, parameters);
        this.setMicrophone();
        super.setVideoHdrToParameters(n, parameters);
        super.setIso(parameters);
        super.setFpsRange(n, parameters);
        super.setRecordingHintToParameters(n, parameters);
        super.setJpegEncodingQuality(2);
        super.setShutterSound(parameters);
        super.setDcModeParameters(parameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setCurrentSettingsToParameters(int n, Camera.Parameters parameters) {
        super.setPreviewPictureAndCamcordSizeToParameters(n, parameters);
        if (this.mPhotoHdrSetting == null) {
            switch (this.mCameraDeviceId) {
                default: {
                    CameraLogger.w("CameraDeviceHandler", "CameraId[" + this.mCameraDeviceId + "] is not supported.");
                    this.mPhotoHdrSetting = Hdr.getDefault(CapturingMode.SCENE_RECOGNITION);
                    break;
                }
                case 0: {
                    this.mPhotoHdrSetting = Hdr.getDefault(CapturingMode.SCENE_RECOGNITION);
                    break;
                }
                case 1: {
                    this.mPhotoHdrSetting = super.isSceneRecognitionSupported() ? Hdr.getDefault(CapturingMode.SUPERIOR_FRONT) : Hdr.getDefault(CapturingMode.FRONT_PHOTO);
                }
            }
        }
        if (this.mPhotoStabilizerSetting == null) {
            switch (this.mCameraDeviceId) {
                default: {
                    CameraLogger.w("CameraDeviceHandler", "CameraId[" + this.mCameraDeviceId + "] is not supported.");
                    this.mPhotoStabilizerSetting = Stabilizer.getDefault(CapturingMode.SCENE_RECOGNITION);
                    break;
                }
                case 0: {
                    this.mPhotoStabilizerSetting = Stabilizer.getDefault(CapturingMode.SCENE_RECOGNITION);
                    break;
                }
                case 1: {
                    this.mPhotoStabilizerSetting = super.isSceneRecognitionSupported() ? Stabilizer.getDefault(CapturingMode.SUPERIOR_FRONT) : Stabilizer.getDefault(CapturingMode.FRONT_PHOTO);
                }
            }
        }
        super.setStabilizerToParameters(n, parameters);
        if (this.mBurstShotSetting == null) {
            this.mBurstShotSetting = BurstShot.getDefault();
        }
        if (this.mBurstShotSetting == BurstShot.HIGH) {
            super.overlayBurstCaptureParameters(n, parameters);
        }
        super.setCommonCaptureParameters(n, parameters);
    }

    private void setDcModeParameters(Camera.Parameters parameters) {
        CommonPlatformDependencyResolver.setDcMode(parameters, CommonPlatformDependencyResolver.ApplicationType.NORMAL);
    }

    private void setFocusModeToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setFocusModeToParameters():[UnExpected State]");
            }
            case 1: {
                if (this.mPhotoFocusModeSetting != null) {
                    parameters.setFocusMode(this.mPhotoFocusModeSetting.getValue());
                    return;
                }
                parameters.setFocusMode(PlatformDependencyResolver.getDefaultFocusModeForFastCapturePhoto(parameters));
                return;
            }
            case 2: 
        }
        if (this.mVideoFocusModeSetting != null) {
            parameters.setFocusMode(this.mVideoFocusModeSetting.getValueForVideo());
            return;
        }
        parameters.setFocusMode(PlatformDependencyResolver.getDefaultFocusModeForFastCaptureVideo(parameters));
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void setFpsRange(int n, Camera.Parameters parameters) {
        List list;
        if (parameters == null) {
            return;
        }
        int n2 = -1;
        switch (n) {
            case 1: {
                n2 = 60;
            }
            default: {
                break;
            }
            case 2: {
                if (this.mVideoSizeSetting == null) break;
                n2 = this.mVideoSizeSetting.getVideoFrameRate();
            }
        }
        if ((list = parameters.getSupportedPreviewFpsRange()) == null) return;
        int[] arrn = CameraDeviceUtil.computePreviewFpsRange(n2, list);
        if (arrn.length <= 0) return;
        parameters.setPreviewFpsRange(arrn[0], arrn[1]);
        parameters.setPreviewFrameRate(arrn[1] / 1000);
    }

    private void setIso(Camera.Parameters parameters) {
        if (PlatformDependencyResolver.isIsoSupported(parameters, "auto")) {
            parameters.set("sony-ae-mode", "auto");
        }
    }

    private void setJpegEncodingQuality(int n) {
        this.mLatestCachedParameters.setJpegQuality(MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(n));
    }

    private void setMeteringToParameters(int n, Camera.Parameters parameters) {
        String string = PlatformDependencyResolver.getDefaultMeteringForFastCapture(parameters);
        if (string != null) {
            parameters.set("sony-metering-mode", string);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setPictureSize(int n, Camera.Parameters parameters) {
        if (!(this.mPhotoResolutionSetting != null && super.isPictureSizeSupported(this.mPhotoResolutionSetting.getPictureRect(), parameters))) {
            switch (this.mCameraDeviceId) {
                case 0: {
                    if (this.mPhotoCaptureFrameShapeSetting == null) {
                        this.mPhotoCaptureFrameShapeSetting = CaptureFrameShape.getDefaultValue(CapturingMode.SCENE_RECOGNITION);
                    }
                    this.mPhotoResolutionSetting = PlatformDependencyResolver.getDefaultResolutionFrom(this.mContext, this.mPhotoCaptureFrameShapeSetting, parameters, this.mCameraDeviceId);
                }
                default: {
                    break;
                }
                case 1: {
                    if (super.isSceneRecognitionSupported()) {
                        this.mPhotoCaptureFrameShapeSetting = CaptureFrameShape.getDefaultValue(CapturingMode.SUPERIOR_FRONT);
                        this.mPhotoResolutionSetting = PlatformDependencyResolver.getDefaultResolutionFrom(this.mContext, this.mPhotoCaptureFrameShapeSetting, parameters, this.mCameraDeviceId);
                        break;
                    }
                    this.mPhotoResolutionSetting = PlatformDependencyResolver.getDefaultResolution(parameters, this.mContext);
                }
            }
        }
        if (this.mPhotoResolutionSetting != null) {
            this.mPictureRect = this.mPhotoResolutionSetting.getPictureRect();
            parameters.setPictureSize(this.mPictureRect.width(), this.mPictureRect.height());
        }
    }

    private void setPreviewPictureAndCamcordSizeToParameters(int n, Camera.Parameters parameters) {
        super.setPictureSize(n, parameters);
        super.setVideoSize(n, parameters);
        super.setPreviewSize(n, parameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setPreviewSize(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setPreviewSize():[UnExpected FastCapture]");
            }
            case 1: {
                this.mPreviewRect = PlatformDependencyResolver.getOptimalPreviewSize(parameters, n, this.mPictureRect);
                break;
            }
            case 2: {
                this.mPreviewRect = PlatformDependencyResolver.getOptimalPreviewSize(parameters, n, this.mCamcordRect);
            }
        }
        parameters.setPreviewSize(this.mPreviewRect.width(), this.mPreviewRect.height());
    }

    private void setRecordingHintToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setVideoModeToParameters():[UnExpected State]");
            }
            case 1: {
                super.finishVideoNrSetting();
                parameters.setRecordingHint(false);
                return;
            }
            case 2: 
        }
        super.prepareVideoNrSetting();
        parameters.setRecordingHint(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setSceneToParameters(int n, Camera.Parameters parameters) {
        if (this.mCameraDeviceId == 1) {
            super.setSoftSkin(n, parameters);
            return;
        } else {
            switch (n) {
                default: {
                    throw new IllegalStateException("setSceneToParameters():[UnExpected State]");
                }
                case 1: {
                    String string = PlatformDependencyResolver.getDefaultSceneModeForFastCapture(parameters);
                    if (string == null) return;
                    parameters.setSceneMode(string);
                    return;
                }
                case 2: {
                    if (this.mVideoSceneSetting == null) return;
                    parameters.setSceneMode(this.mVideoSceneSetting.getValue());
                    return;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setShutterSound(Camera.Parameters parameters) {
        boolean bl = !this.isShutterSoundOff();
        this.setPhotoShutterSound(bl);
        boolean bl2 = this.isShutterSoundOff();
        boolean bl3 = false;
        if (!bl2) {
            bl3 = true;
        }
        this.setVideoRecordSound(bl3);
        if (this.mShutterSoundSetting == com.sonyericsson.cameracommon.commonsetting.values.ShutterSound.ON && this.mFastCaptureSetting == FastCapture.LAUNCH_AND_CAPTURE) {
            this.mLatestCachedParameters.set("key-sony-ext-shuttersound", ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_FAST_CAPTURE_SHUTTER_DONE, ShutterSound.SOUND1));
            this.mIsLaunchAndCaptureShutterSoundEnabled = true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void setSoftSkin(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setSoftSkin():[UnExpected State]");
            }
            case 1: {
                String string;
                List list = parameters.getSupportedSceneModes();
                if (list == null) {
                    return;
                }
                if (PlatformDependencyResolver.isSoftSkinSupported(parameters) && list.contains((Object)"portrait")) {
                    SoftSkin softSkin = SoftSkin.ON;
                    if (this.mSoftSkinSetting != null) {
                        softSkin = this.mSoftSkinSetting;
                    }
                    parameters.setSceneMode("auto");
                    parameters.set("sony-soft-skin-level-for-picture", softSkin.getLevel(PlatformDependencyResolver.getSoftSkinMaxLevel(parameters)));
                    return;
                }
                if (this.mSoftSkinSetting != null) {
                    string = this.mSoftSkinSetting.getScene().getValue();
                } else {
                    boolean bl = list.contains((Object)"soft-skin");
                    string = null;
                    if (!bl) return;
                    string = SoftSkin.ON.getScene().getValue();
                }
                if (string == null) return;
                parameters.setSceneMode(string);
                return;
            }
            case 2: 
        }
        String string = PlatformDependencyResolver.getDefaultSceneModeForFastCapture(parameters);
        if (string == null) return;
        parameters.setSceneMode(string);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setStabilizerToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setStabilizerToParameters():[UnExpected State]");
            }
            case 1: {
                String string;
                if (PlatformDependencyResolver.isHdrSupported(parameters)) {
                    string = super.getDependHdrIsValue(this.mPhotoHdrSetting, this.mPhotoStabilizerSetting);
                } else {
                    boolean bl = PlatformDependencyResolver.isImageStabilizerSupported(parameters);
                    string = null;
                    if (!bl) return;
                    string = this.mPhotoStabilizerSetting.getValue();
                }
                if (string != null) {
                    this.setStabilizer(parameters, n, string);
                }
                return;
            }
            case 2: 
        }
        super.setVideoStabilizerToParameters(n, parameters);
    }

    private void setStreamNotificationMute(boolean bl) {
        AudioManager audioManager = (AudioManager)this.mContext.getSystemService("audio");
        if (audioManager != null) {
            audioManager.setStreamMute(5, bl);
        }
    }

    private void setVideoHdrToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setStabilizerToParameters():[UnExpected State]");
            }
            case 1: {
                parameters.set("sony-video-hdr", "off");
                return;
            }
            case 2: 
        }
        if (PlatformDependencyResolver.isVideoHdrSupported(parameters)) {
            if (this.mVideoHdrSetting != null) {
                parameters.set("sony-video-hdr", this.mVideoHdrSetting.getValue());
                return;
            }
            parameters.set("sony-video-hdr", "on");
            return;
        }
        parameters.set("sony-video-hdr", "off");
    }

    private void setVideoNr(boolean bl) {
        if (this.mLatestCachedParameters == null) {
            return;
        }
        if (bl) {
            this.mLatestCachedParameters.set("sony-video-nr", "on");
            return;
        }
        this.mLatestCachedParameters.set("sony-video-nr", "off");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setVideoSize(int n, Camera.Parameters parameters) {
        if (this.mVideoSizeSetting == null) {
            this.mVideoSizeSetting = PlatformDependencyResolver.getDefaultVideoSize(parameters);
            this.mCamcordRect = this.mVideoSizeSetting != null ? this.mVideoSizeSetting.getVideoRect() : new Rect(0, 0, parameters.getPreviewSize().width, parameters.getPreviewSize().height);
        } else {
            this.mCamcordRect = this.mVideoSizeSetting.getVideoRect();
        }
        parameters.set("video-size", "" + this.mCamcordRect.width() + "x" + this.mCamcordRect.height());
        if (n == 2) {
            parameters.setPictureSize(this.mCamcordRect.width(), this.mCamcordRect.height());
        }
    }

    private void setVideoStabilizerToParameters(int n, Camera.Parameters parameters) {
        if (!PlatformDependencyResolver.isVideoStabilizerSupported(parameters)) {
            return;
        }
        if (this.mVideoStabilizerSetting != null) {
            this.setStabilizer(parameters, n, this.mVideoStabilizerSetting.getValue());
            return;
        }
        VideoStabilizer videoStabilizer = VideoStabilizer.getRecommendedVideoStabilizerValue(this.mCameraDeviceId, this.mVideoSizeSetting);
        this.setStabilizer(parameters, n, videoStabilizer.getValue());
        this.mVideoStabilizerSetting = videoStabilizer;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setWhiteBalanceToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setWhiteBalanceToParameters():[UnExpected State]");
            }
            case 1: {
                String string = PlatformDependencyResolver.getDefaultWhiteBalanceForFastCapturePhoto(parameters);
                if (string == null) return;
                parameters.setWhiteBalance(string);
                return;
            }
            case 2: {
                if (this.mVideoWhiteBalanceSetting == null) return;
                parameters.setWhiteBalance(this.mVideoWhiteBalanceSetting.getValue());
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void startObjectTracking() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            boolean bl;
            if (this.mCameraExtension != null && this.mLatestCachedParameters != null && PlatformDependencyResolver.isObjectTrackingSuppoted(this.mLatestCachedParameters) && !(bl = this.mIsObjectTrackingAlreadyStarted)) {
                this.mCameraExtension.setObjectTrackingCallback((CameraExtension.ObjectTrackingCallback)this.mOnObjectTrackedCallback, 75, 100);
                this.mCameraExtension.startObjectTracking();
                this.mIsObjectTrackingAlreadyStarted = true;
                new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new ObjectTracking(ObjectTracking.Value.ON)).publish();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void stopFaceDetectionNotSynchronized() {
        if (!(this.mIsFaceDetectionAlreadyStarted && this.mLatestCachedParameters != null && PlatformDependencyResolver.isFaceDetectionSupported(this.mLatestCachedParameters) && this.mCamera != null && this.mCameraExtension != null)) {
            return;
        }
        this.mCameraExtension.setFaceDetectionCallback(null);
        try {
            this.mCamera.stopFaceDetection();
        }
        catch (RuntimeException var1_1) {
            CameraLogger.w("CameraDeviceHandler", ".stopFaceDetection():[stopFaceDetection failed]", (Throwable)var1_1);
        }
        this.mIsFaceDetectionAlreadyStarted = false;
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new FaceIdentification(FaceIdentification.Value.OFF)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.OFF)).publish();
    }

    private void stopObjectTracking() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.stopObjectTrackingNotSynchronized();
            return;
        }
    }

    private void stopObjectTrackingNotSynchronized() {
        if (!(this.mCameraExtension != null && this.mLatestCachedParameters != null && PlatformDependencyResolver.isObjectTrackingSuppoted(this.mLatestCachedParameters) && this.mIsObjectTrackingAlreadyStarted)) {
            return;
        }
        this.mCameraExtension.setObjectTrackingCallback(null, 0, 0);
        this.mCameraExtension.stopObjectTracking();
        this.mIsObjectTrackingAlreadyStarted = false;
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new ObjectTracking(ObjectTracking.Value.OFF)).publish();
    }

    private void stopPreview() {
        if (this.isRecorderWorking() || this.mCamera == null) {
            return;
        }
        this.mCamera.stopPreview();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void stopSceneRecognitionNotSynchronized() {
        if (!(this.mIsSceneRecognitionAlreadyStarted && this.mCameraExtension != null && this.mLatestCachedParameters != null && PlatformDependencyResolver.isSceneRecognitionSupported(this.mLatestCachedParameters))) {
            return;
        }
        this.mCameraExtension.stopSceneRecognition();
        this.mIsSceneRecognitionAlreadyStarted = false;
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new SceneRecognition(SceneRecognition.Value.OFF)).publish();
    }

    private void stopVideoMetadata() {
        if (HardwareCapability.getInstance().isVideoMetaDataSupported(this.mCameraDeviceId) && this.mCameraExtension != null) {
            this.mCameraExtension.stopVideoMetadata();
            new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new Metadata(Metadata.Value.OFF)).publish();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void applyCameraLightSetting(int n) {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            return;
        }
        if (PlatformDependencyResolver.isFlashLightSupported(parameters)) {
            super.setCameraLight(this.mFlashSetting.getValue());
        } else {
            if (!PlatformDependencyResolver.isPhotoLightSupported(parameters)) return;
            super.setCameraLight(this.mPhotoLightSetting.getValue());
        }
        this.trySetParametersToDevice(parameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void applySavingRequest(SavingRequest savingRequest, Camera.Parameters parameters) {
        if (parameters != null) {
            parameters.setRotation(savingRequest.common.orientation);
            parameters.removeGpsData();
            if (savingRequest.common.location != null) {
                double d = savingRequest.common.location.getLatitude();
                double d2 = savingRequest.common.location.getLongitude();
                boolean bl = d != 0.0 || d2 != 0.0;
                if (bl) {
                    parameters.setGpsLatitude(d);
                    parameters.setGpsLongitude(d2);
                    if (savingRequest.common.location.hasAltitude()) {
                        parameters.setGpsAltitude(savingRequest.common.location.getAltitude());
                    }
                    if (savingRequest.common.location.getTime() != 0) {
                        parameters.setGpsTimestamp(savingRequest.common.location.getTime() / 1000);
                    }
                }
            }
            this.trySetParametersToDevice(parameters);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void autoFocus() {
        if (this.mOnPreAutoFocusCallback != null || this.mCameraExtension == null) {
            return;
        }
        try {
            this.mCameraExtension.startAutoFocus((CameraExtension.AutoFocusCallback)this.mOnAutoFocusCallback, true, true, true);
            return;
        }
        catch (RuntimeException var1_1) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void cancelAutoFocus() {
        if (this.mOnPreAutoFocusCallback != null || this.mCameraExtension == null) {
            return;
        }
        this.mCameraExtension.stopAutoFocus();
    }

    public void captureWhileRecording(PhotoSavingRequest photoSavingRequest) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        if (this.mLatestCachedParameters.isVideoSnapshotSupported()) {
            this.takePicture(photoSavingRequest);
            return;
        }
        this.mPreviewFrameGrabber = new PreviewFrameGrabber(photoSavingRequest);
        this.mPreviewFrameGrabber.setOnPreviewFrameGrabbedListener(new OnPreviewFrameGrabbedCallback(photoSavingRequest));
        this.mPreviewFrameGrabber.requestFrame(camera);
    }

    public void changeBurstShotSettingTo(BurstShot burstShot) {
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean changeCameraModeTo(int n, boolean bl) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return false;
        }
        this.requestCacheParameters();
        Camera.Parameters parameters = this.getLatestCachedParameters();
        switch (n) {
            default: {
                throw new IllegalStateException("changeCameraModeTo():[preview] [UnExpected camera TYPE]");
            }
            case 1: {
                parameters.setPictureSize(this.mPictureRect.width(), this.mPictureRect.height());
                this.mPreviewRect = PlatformDependencyResolver.getOptimalPreviewSize(parameters, n, this.mPictureRect);
                break;
            }
            case 2: {
                List list = parameters.getSupportedPictureSizes();
                Rect rect = null;
                if (list != null && (rect = CameraSize.getOptimalVideoSnapshotSizeFromCamerSizeList(this.mCamcordRect, list)) != null) {
                    parameters.setPictureSize(rect.width(), rect.height());
                }
                this.mPreviewRect = PlatformDependencyResolver.getOptimalPreviewSize(parameters, n, this.mCamcordRect);
            }
        }
        parameters.setPreviewSize(this.mPreviewRect.width(), this.mPreviewRect.height());
        if (bl) {
            if (PlatformDependencyResolver.isVideoStabilizerSupported(parameters)) {
                this.setStabilizer(parameters, 2, this.mVideoStabilizerSetting.getValue());
            }
        } else {
            super.setSceneToParameters(n, parameters);
            super.setFocusModeToParameters(n, parameters);
            super.setWhiteBalanceToParameters(n, parameters);
            super.setMeteringToParameters(n, parameters);
            super.setStabilizerToParameters(n, parameters);
        }
        super.setRecordingHintToParameters(n, parameters);
        boolean bl2 = this.willPreviewBeRestarted();
        if (bl2) {
            super.stopPreview();
        }
        this.trySetParametersToDevice(parameters);
        if (bl2) {
            camera.startPreview();
        }
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).putFromParameter(camera.getParameters()).publish();
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean deselectObject() {
        if (!(this.mIsObjectTrackingAlreadyStarted && this.getCameraInstance() != null && PlatformDependencyResolver.isObjectTrackingSuppoted(this.mLatestCachedParameters))) {
            return false;
        }
        this.mCameraExtension.deselectObject();
        this.stopObjectTracking();
        return true;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void finalizeRecording() {
        CameraDeviceHandler cameraDeviceHandler = this;
        // MONITORENTER : cameraDeviceHandler
        RecorderInterface recorderInterface = this.mVideoRecorder;
        if (recorderInterface != null) {
            this.mVideoRecorder.stop();
            this.mLastVideoSavingRequest.setDuration(this.mVideoRecorder.getRecordingTimeMillis());
        }
        this.mIsRecording = false;
        if (this.mStateMachine != null) {
            this.mStateMachine.onVideoRecordingDone(this.mLastVideoSavingRequest);
        }
        // MONITOREXIT : cameraDeviceHandler
        boolean bl = !this.isShutterSoundOff();
        this.setPhotoShutterSound(bl);
        this.trySetParametersToDevice(this.mLatestCachedParameters);
        this.setStreamNotificationMute(false);
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new DeviceStatus(DeviceStatus.Value.STILL_PREVIEW)).publish();
        return;
        catch (RecorderException recorderException) {
            throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
        }
    }

    public BurstShot getBurstShotSetting() {
        return this.mBurstShotSetting;
    }

    public Rect getCamcordRect() {
        return this.mCamcordRect;
    }

    public int getCameraDeviceStatus() {
        return this.mCurrentDeviceState;
    }

    public int getCameraId() {
        return this.mCameraDeviceId;
    }

    public com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification getFaceIdService() {
        return this.mFaceIdService;
    }

    public Flash getFlashSetting() {
        return this.mFlashSetting;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public Camera.Parameters getLatestCachedParameters() {
        if (this.mCurrentDeviceState != 2) {
            return null;
        }
        if (this.mLatestCachedParameters != null) return this.mLatestCachedParameters;
        Camera camera = this.getCameraInstance();
        if (camera == null) return null;
        this.mLatestCachedParameters = camera.getParameters();
        if (this.mLatestCachedParameters != null) return this.mLatestCachedParameters;
        CameraLogger.e("CameraDeviceHandler", "getLatestCachedParameters: mLatestCachedParameters is null");
        return null;
    }

    public int getMaxSuperResolutionZoom() {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            return 0;
        }
        return PlatformDependencyResolver.getMaxSuperResolutionZoom(parameters);
    }

    public int getMaxZoom() {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            return 0;
        }
        return parameters.getMaxZoom();
    }

    public Rect getPictureRect() {
        return this.mPictureRect;
    }

    public PreProcessState getPreProcessState() {
        return this.mPreProcessState;
    }

    public Rect getPreviewRect() {
        return this.mPreviewRect;
    }

    public com.sonyericsson.cameracommon.commonsetting.values.ShutterSound getShutterSound() {
        return this.mShutterSoundSetting;
    }

    public VideoHdr getVideoHdr() {
        return this.mVideoHdrSetting;
    }

    public VideoSize getVideoSize() {
        return this.mVideoSizeSetting;
    }

    public VideoStabilizer getVideoStabilizer() {
        return this.mVideoStabilizerSetting;
    }

    public void initialize() {
        this.mIsImmediateReleaseRequested = false;
    }

    public boolean isCameraDeviceIsOpenedRightNow() {
        if (this.mCurrentDeviceState == 2) {
            return true;
        }
        return false;
    }

    public boolean isCameraDisabled() {
        return this.mIsCameraDisabled;
    }

    public boolean isCameraFront() {
        if (this.mCameraDeviceId == 1) {
            return true;
        }
        return false;
    }

    public boolean isCloseDeviceTaskWorking() {
        if (this.mCloseDeviceFuture != null) {
            return true;
        }
        return false;
    }

    public boolean isImmediateReleaseRequested() {
        return this.mIsImmediateReleaseRequested;
    }

    public boolean isOpenDeviceThreadAlive() {
        if (this.mOpenDeviceThread != null && this.mOpenDeviceThread.isAlive()) {
            return true;
        }
        return false;
    }

    public boolean isPreCaptureAlreadyDone() {
        if (this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
            return true;
        }
        return false;
    }

    public boolean isPreCaptureOnGoing() {
        if (this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED) {
            return true;
        }
        return false;
    }

    public boolean isPreScanAlreadyDone() {
        if (this.mPreProcessState == PreProcessState.PRE_SCAN_DONE || this.mPreProcessState == PreProcessState.PRE_CAPTURE_STARTED || this.mPreProcessState == PreProcessState.PRE_CAPTURE_DONE) {
            return true;
        }
        return false;
    }

    public boolean isPreScanOnGoing() {
        if (this.mPreProcessState == PreProcessState.PRE_SCAN_STARTED) {
            return true;
        }
        return false;
    }

    public boolean isPreScanSucceeded() {
        return this.mIsPreScanSucceeded;
    }

    public boolean isRecorderWorking() {
        if (this.mVideoRecorder == null) {
            CameraLogger.d("CameraDeviceHandler", "Video Recorder: null");
            return false;
        }
        if (!(this.mVideoRecorder.isRecordingOrPaused() || this.mVideoRecorder.isStopping())) {
            CameraLogger.d("CameraDeviceHandler", "Video Recorder: not working");
            return false;
        }
        CameraLogger.d("CameraDeviceHandler", "Video Recorder: working");
        return true;
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isShutterSoundOff() {
        if (this.mShutterSoundSetting == null) return false;
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$ShutterSound[this.mShutterSoundSetting.ordinal()]) {
            default: {
                return false;
            }
            case 1: 
        }
        return true;
    }

    public void overlaySmartCoverCameraSetting() {
        this.mFlashSetting = Flash.OFF;
        this.mPhotoResolutionSetting = Resolution.EIGHT_MP;
        this.mPhotoFocusModeSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, 1, this.mCameraDeviceId, ParameterKey.FOCUS_MODE, FocusMode.SINGLE);
        this.mPhotoStabilizerSetting = FastCapturingCameraUtils.loadParameter(this.mPreferences, this.mCameraDeviceId, 1, ParameterKey.STABILIZER, Stabilizer.OFF);
        this.mBurstShotSetting = BurstShot.OFF;
        this.mFaceIdentifySetting = FaceIdentify.OFF;
        this.mSoftSkinSetting = SoftSkin.OFF;
    }

    /*
     * Exception decompiling
     */
    public void pauseRecording() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.util.ConcurrentModificationException
        // java.util.LinkedList$ReverseLinkIterator.next(LinkedList.java:217)
        // org.benf.cfr.reader.bytecode.analysis.structured.statement.Block.extractLabelledBlocks(Block.java:212)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement$LabelledBlockExtractor.transform(Op04StructuredStatement.java:483)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.transform(Op04StructuredStatement.java:600)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.insertLabelledBlocks(Op04StructuredStatement.java:610)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:774)
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

    public void preCapture() {
        Camera camera = this.getCameraInstance();
        if (camera == null) {
            return;
        }
        this.mLastSavingRequest = this.createPreCaptureSavingRequest();
        this.applySavingRequest(this.mLastSavingRequest, this.mLatestCachedParameters);
        this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_STARTED);
        camera.takePicture((Camera.ShutterCallback)new OnPreCaptureShutterCallback(), null, (Camera.PictureCallback)new OnPreCapturePictureTakenCallback());
    }

    public void preScan() {
        if (this.mCameraExtension != null) {
            this.mOnPreAutoFocusCallback = new OnPreAutoFocusCallback();
            this.mCameraExtension.startAutoFocus((CameraExtension.AutoFocusCallback)this.mOnPreAutoFocusCallback, true, true, true);
            this.changePreProcessStateTo(PreProcessState.PRE_SCAN_STARTED);
        }
    }

    public void prepareAdditionalFeatures(int n, ExtendedActivity extendedActivity) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        super.prepareZoom(camera);
        switch (n) {
            default: {
                throw new IllegalStateException("prepareAdditionalSettings():[UnExpected State]");
            }
            case 1: {
                this.startFaceDetection();
                this.startFaceIdService((Activity)extendedActivity);
                this.startSceneRecognition();
                return;
            }
            case 2: 
        }
        this.startFaceDetection();
        this.startFaceIdService((Activity)extendedActivity);
        this.startSceneRecognition();
    }

    public void releaseCameraInstance() {
        CameraLogger.d("CameraDeviceHandler", "releaseCameraInstance():[IN]");
        if (!this.isRecorderWorking()) {
            this.releaseCamera();
            return;
        }
        this.mCloseDeviceExecutorService = Executors.newSingleThreadExecutor();
        this.mCloseDeviceFuture = this.mCloseDeviceExecutorService.submit((Runnable)new CloseDeviceTask(this, null));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void releaseCameraOnError() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.mCamera.release();
            this.mCamera = null;
            return;
        }
    }

    public void releaseVideo() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.awaitFinishingRecording();
            this.setStreamNotificationMute(false);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void requestCacheParameters() {
        Camera camera = this.getCameraInstance();
        if (camera == null) {
            return;
        }
        this.mLatestCachedParameters = camera.getParameters();
        if (this.mLatestCachedParameters != null) return;
        CameraLogger.e("CameraDeviceHandler", "requestCacheParameters: mLatestCachedParameters is null.");
    }

    public void requestContinuousPreviewFrame(boolean bl) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        if (bl) {
            camera.setOneShotPreviewCallback((Camera.PreviewCallback)this.mRequestContinuousPreviewFrameCallback);
            return;
        }
        camera.setOneShotPreviewCallback(null);
    }

    public void requestOnePreviewFrame() {
        Camera camera = this.getCameraInstance();
        if (camera == null) {
            return;
        }
        camera.setOneShotPreviewCallback((Camera.PreviewCallback)this.mRequestOnePreviewFrameCallback);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void resetFocusMode() {
        if (this.getCameraInstance() == null) {
            return;
        }
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) return;
        if (parameters.getMaxNumFocusAreas() < 1) return;
        parameters.setFocusMode(PlatformDependencyResolver.getDefaultFocusModeForFastCapturePhoto(parameters));
        parameters.set("sony-focus-area", "center");
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)new Camera.Area(new Rect(), 0));
        parameters.setFocusAreas((List)arrayList);
        if (!this.trySetParametersToDevice(parameters)) return;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void restoreSettingAfterRecording() {
        this.mVideoRecorder = null;
        this.reconnectCamera();
        this.stopVideoMetadata();
        boolean bl = !this.isShutterSoundOff();
        this.setVideoRecordSound(bl);
        this.trySetParametersToDevice(this.getLatestCachedParameters());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void resumePhotoLight() {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            return;
        }
        if (PlatformDependencyResolver.isFlashLightSupported(parameters)) {
            this.setCameraLight(this.mFlashSetting.getValue());
            return;
        }
        if (!PlatformDependencyResolver.isPhotoLightSupported(parameters)) return;
        this.setCameraLight(this.mPhotoLightSetting.getValue());
    }

    public void resumeRecording() {
        this.setStreamNotificationMute(true);
        try {
            if (this.mVideoRecorder != null) {
                this.mVideoRecorder.resume();
            }
            return;
        }
        catch (RecorderException var1_1) {
            throw new RuntimeException("resumeRecording():[Failed to resume MediaRecorder.]");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean selectObject(PointF pointF) {
        if (!(super.getCameraInstance() != null && PlatformDependencyResolver.isObjectTrackingSuppoted(this.mLatestCachedParameters))) {
            return false;
        }
        if (this.mIsObjectTrackingAlreadyStarted) {
            super.stopObjectTracking();
        }
        super.startObjectTracking();
        int n = (int)(pointF.x * (float)this.mPreviewRect.width());
        int n2 = (int)(pointF.y * (float)this.mPreviewRect.height());
        this.mCameraExtension.selectObject(n, n2);
        return true;
    }

    public boolean setAdditionalSettings(ParameterKey parameterKey, ParameterValue parameterValue) {
        int n = 1;
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                n = 0;
            }
            case 4: {
                return n;
            }
            case 1: {
                this.mPhotoHdrSetting = (Hdr)parameterValue;
                Camera.Parameters parameters = this.getLatestCachedParameters();
                this.setStabilizer(parameters, n, this.mPhotoHdrSetting.getValue());
                return this.trySetParametersToDevice(parameters);
            }
            case 2: {
                this.mPhotoStabilizerSetting = (Stabilizer)parameterValue;
                Camera.Parameters parameters = this.getLatestCachedParameters();
                this.setStabilizer(parameters, n, this.mPhotoStabilizerSetting.getValue());
                return this.trySetParametersToDevice(parameters);
            }
            case 3: {
                this.mFlashSetting = (Flash)parameterValue;
                return super.setCameraLight(this.mFlashSetting.getValue());
            }
            case 5: 
        }
        this.mPhotoLightSetting = (PhotoLight)parameterValue;
        return super.setCameraLight(this.mPhotoLightSetting.getValue());
    }

    public void setErrorCallback(Camera.ErrorCallback errorCallback) {
        this.mErrorCallback = errorCallback;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void setFocusPosition(PointF pointF) {
        if (super.getCameraInstance() == null) {
            return;
        }
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) return;
        if (parameters.getMaxNumFocusAreas() < 1) return;
        int n = (int)(2000.0f * pointF.x - 1000.0f);
        int n2 = (int)(2000.0f * pointF.y - 1000.0f);
        Camera.Area area = new Camera.Area(new Rect(n - 50, n2 - 50, n + 50, n2 + 50), 1000);
        if (!new Rect(-1000, -1000, 1000, 1000).contains(area.rect)) return;
        parameters.set("sony-focus-area", "user");
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)area);
        parameters.setFocusAreas((List)arrayList);
        if (!this.trySetParametersToDevice(parameters)) return;
    }

    public void setMicrophone() {
        if (this.mVideoMicrophoneSetting == null) {
            this.mVideoMicrophoneSetting = Microphone.ON;
        }
    }

    void setModeLessBurstPreviewPictureAndCamcordSizeToParameters(int n, Camera.Parameters parameters) {
        this.mPictureRect = PlatformDependencyResolver.getBurstPictureSizeAccordingTo(parameters).getPictureRect();
        parameters.setPictureSize(this.mPictureRect.width(), this.mPictureRect.height());
        super.setVideoSize(n, parameters);
        super.setPreviewSize(n, parameters);
    }

    void setModeLessBurstStabilizerToParameters(int n, Camera.Parameters parameters) {
        switch (n) {
            default: {
                throw new IllegalStateException("setBurstCaptureStabilizerToParameters():[UnExpected State]");
            }
            case 1: {
                this.setStabilizer(parameters, n, "off");
                return;
            }
            case 2: 
        }
        super.setVideoStabilizerToParameters(n, parameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPhotoHdrSettings(ParameterValue parameterValue) {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (!(parameters != null && PlatformDependencyResolver.isHdrSupported(parameters))) {
            return;
        }
        this.mPhotoHdrSetting = (Hdr)parameterValue;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setPhotoShutterSound(boolean bl) {
        if (this.mCamera == null || 17 > Build.VERSION.SDK_INT) {
            return;
        }
        if (bl) {
            this.mLatestCachedParameters.set("key-sony-ext-shuttersound", "/system/media/audio/ui/camera_click.ogg");
            return;
        }
        this.mLatestCachedParameters.set("key-sony-ext-shuttersound", ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_OFF, ShutterSound.OFF));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void setSelectedFacePosition(int n, int n2) {
        void var5_3 = this;
        synchronized (var5_3) {
            CameraExtension cameraExtension = this.mCameraExtension;
            if (cameraExtension != null) {
                this.mCameraExtension.setSelectFacePos(n, n2);
            }
            return;
        }
    }

    public void setStabilizer(Camera.Parameters parameters, int n, String string) {
        if (n == 2) {
            parameters.set("sony-is", "off");
            parameters.set("sony-vs", string);
            return;
        }
        parameters.set("sony-is", string);
        parameters.set("sony-vs", "off");
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.mStateMachine = stateMachine;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setTorch(boolean bl) {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) {
            CameraLogger.e("CameraDeviceHandler", "setTorch: params is null.");
            return;
        }
        String string = bl ? "torch" : "off";
        parameters.setFlashMode(string);
        this.trySetParametersToDevice(parameters);
    }

    public void setVideoRecordSound(boolean bl) {
        if (this.mCamera == null) {
            return;
        }
        if (bl) {
            this.mLatestCachedParameters.set("key-sony-ext-recordingsound", "/system/media/audio/ui/VideoRecord.ogg");
            return;
        }
        this.mLatestCachedParameters.set("key-sony-ext-recordingsound", ShutterToneGenerator.getSoundFilePath(ShutterToneGenerator.Type.SOUND_OFF, ShutterSound.OFF));
    }

    public int startBurstCapture(CameraExtension.FilePathGenerator filePathGenerator, CameraExtension.MediaProviderUpdator mediaProviderUpdator) {
        this.mLatestCachedParameters.set("sony-burst-shot", "on");
        int n = PlatformDependencyResolver.getBurstFrameRate(this.mLatestCachedParameters, this.mContext);
        this.mLatestCachedParameters.set("sony-burst-shot-frame-rate", n);
        super.setJpegEncodingQuality(1);
        this.trySetParametersToDevice(this.mLatestCachedParameters);
        this.mCameraExtension.setBurstShotCallback((CameraExtension.BurstShotCallback)new OnBurstShotCallback());
        this.mCameraExtension.setBurstStorageFullDetector((CameraExtension.StorageFullDetector)new BurstStorageFullDetector());
        this.mLatestBurstId = this.mCameraExtension.startBurstShot(filePathGenerator, mediaProviderUpdator);
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new BurstShooting(BurstShooting.Value.ON)).publish();
        return this.mLatestBurstId;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startCameraOpen(Context context, int n, FastCapture fastCapture, boolean bl) {
        void var7_5 = this;
        synchronized (var7_5) {
            this.mIsSmartCoverCamera = context instanceof SmartCoverActivity;
            this.mIsSecureOneShotPhoto = bl;
            if (((DevicePolicyManager)context.getSystemService("device_policy")).getCameraDisabled(null)) {
                CameraLogger.errorLogForNonUserVariant("CameraDeviceHandler", "[CameraNotAvailable] startCameraOpen: dpm.getCameraDisabled(null)");
                this.mIsCameraDisabled = true;
            } else {
                this.mIsCameraDisabled = false;
                switch (this.mCurrentDeviceState) {
                    default: {
                        this.mContext = context;
                        this.mCameraDeviceId = n;
                        this.mFastCaptureSetting = fastCapture;
                        super.clearCloseDeviceTask();
                        this.mCameraInfo = new Camera.CameraInfo();
                        Camera.getCameraInfo((int)this.mCameraDeviceId, (Camera.CameraInfo)this.mCameraInfo);
                        StaticConfigurationUtil.setCameraInfo(this.mCameraInfo);
                        if (!(this.mOpenDeviceThread != null && this.mOpenDeviceThread.isAlive())) {
                            this.mOpenDeviceThread = new OpenDeviceThread((CameraDeviceHandler)this, null);
                            this.mOpenDeviceThread.setName(OpenDeviceThread.class.getSimpleName());
                            this.mOpenDeviceThread.setPriority(10);
                            this.mOpenDeviceThread.start();
                        }
                        if (this.mLoadSettingsThread != null && this.mLoadSettingsThread.isAlive()) break;
                        this.mLoadSettingsThread = new LoadSettingsThread((CameraDeviceHandler)this, null);
                        this.mLoadSettingsThread.setName(LoadSettingsThread.class.getSimpleName());
                        this.mLoadSettingsThread.setPriority(5);
                        this.mLoadSettingsThread.start();
                        break;
                    }
                    case 1: 
                    case 2: {
                        this.mIsImmediateReleaseRequested = false;
                    }
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startFaceDetection() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            boolean bl = this.mIsFaceDetectionAlreadyStarted;
            if (!bl && this.mLatestCachedParameters != null && PlatformDependencyResolver.isFaceDetectionSupported(this.mLatestCachedParameters) && this.mCamera != null && this.mCameraExtension != null) {
                this.mCameraExtension.setFaceDetectionCallback((CameraExtension.FaceDetectionCallback)this.mOnFaceDetectionCallback);
                this.mCamera.startFaceDetection();
                this.mIsFaceDetectionAlreadyStarted = true;
                if (this.mFaceIdentifySetting == null) {
                    new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new FaceDetection(FaceDetection.Value.ON)).publish();
                } else {
                    EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId);
                    FaceIdentification.Value value = FaceIdentify.ON.getValue().equals((Object)this.mFaceIdentifySetting.getValue()) ? FaceIdentification.Value.ON : FaceIdentification.Value.OFF;
                    eachCameraStatusPublisher.put(new FaceIdentification(value)).put((FaceIdentification)new FaceDetection(FaceDetection.Value.ON)).publish();
                }
            }
            return;
        }
    }

    public void startFaceIdService(Activity activity) {
        if (super.getFaceIdentify() == FaceIdentify.ON && this.mFaceIdService == null) {
            this.mFaceIdService = FaceIdentificationFactory.createNewInstance((Context)activity);
        }
    }

    public void startLiveViewFinder(SurfaceHolder surfaceHolder) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        if (surfaceHolder == null) {
            CameraLogger.e("CameraDeviceHandler", "Camera.setPreviewDisplay() failed. Because surfaceHolder is null.");
            return;
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            return;
        }
        catch (IOException var3_3) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startPreview() {
        Camera camera = this.getCameraInstance();
        if (camera == null) return;
        try {
            camera.startPreview();
            return;
        }
        catch (RuntimeException var2_2) {
            if (this.mStateMachine == null) return;
            this.mStateMachine.onDeviceError(StateMachine.ErrorCode.ERROR_ON_START_PREVIEW, (Exception)var2_2);
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void startRecording(VideoSavingRequest videoSavingRequest, RecorderInterface.RecorderListener recorderListener, SoundPlayer soundPlayer) {
        Camera camera;
        boolean bl = true;
        this.setPhotoShutterSound(false);
        super.setFpsRange(2, this.getLatestCachedParameters());
        this.trySetParametersToDevice(this.getLatestCachedParameters());
        this.mIsLaunchAndCaptureShutterSoundEnabled = false;
        super.setStreamNotificationMute(bl);
        void var19_5 = this;
        // MONITORENTER : var19_5
        this.mLastVideoSavingRequest = videoSavingRequest;
        boolean bl2 = !this.isShutterSoundOff() ? bl : false;
        super.prepareMediaRecorder(videoSavingRequest, recorderListener, soundPlayer, bl2);
        if (this.mVideoRecorder != null) {
            if (this.mCameraExtension != null && HardwareCapability.getInstance().isVideoMetaDataSupported(this.mCameraDeviceId)) {
                this.mCameraExtension.startVideoMetadata();
                new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new Metadata(Metadata.Value.ON)).publish();
            }
            this.mVideoRecorder.start();
            this.mIsRecording = true;
        }
        if ((camera = super.getCameraInstance()) != null) {
            new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).putFromParameter(camera.getParameters()).put(new DeviceStatus(DeviceStatus.Value.VIDEO_RECORDING)).publish();
        } else {
            new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new DeviceStatus(DeviceStatus.Value.VIDEO_RECORDING)).publish();
        }
        if (VideoStabilizer.isIntelligentActive(this.mVideoStabilizerSetting)) {
            this.setVideoRecordSound(false);
            this.trySetParametersToDevice(this.getLatestCachedParameters());
        }
        // MONITOREXIT : var19_5
        return;
        catch (RecorderException recorderException) {
            CameraLogger.w("CameraDeviceHandler", "mMediaRecorder.start() fail.");
            Camera camera2 = super.getCameraInstance();
            if (camera2 != null) {
                CameraLogger.w("CameraDeviceHandler", "Camera.lock() after mMediaRecorder.start() fail.");
                camera2.lock();
            }
            this.releaseVideo();
            if (this.isShutterSoundOff()) {
                bl = false;
            }
            this.setPhotoShutterSound(bl);
            String string = this.mLastVideoSavingRequest.getFilePath();
            if (string == null) throw new RuntimeException((Throwable)recorderException);
            try {
                File file = new File(this.mLastVideoSavingRequest.getFilePath());
                if (!file.exists()) throw new RuntimeException((Throwable)recorderException);
                if (!file.isFile()) throw new RuntimeException((Throwable)recorderException);
                if (file.delete()) throw new RuntimeException((Throwable)recorderException);
                CameraLogger.e("CameraDeviceHandler", "videoFile.delete(): [Unable to delete empty video file.]");
            }
            catch (Exception var16_12) {
                CameraLogger.e("CameraDeviceHandler", "startRecording: [Unable to delete empty media file.]");
                throw new RuntimeException((Throwable)recorderException);
            }
            throw new RuntimeException((Throwable)recorderException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startSceneRecognition() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            boolean bl = this.mIsSceneRecognitionAlreadyStarted;
            if (!bl && this.mCameraExtension != null && this.mLatestCachedParameters != null && PlatformDependencyResolver.isSceneRecognitionSupported(this.mLatestCachedParameters)) {
                this.mCameraExtension.startSceneRecognition((CameraExtension.SceneRecognitionCallback)this.mOnSceneModeChangedCallback);
                this.mIsSceneRecognitionAlreadyStarted = true;
                new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new SceneRecognition(SceneRecognition.Value.ON)).publish();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startSmoothZoom(int n) {
        Camera camera;
        if (this.mOnZoomChangedCallback == null || (camera = super.getCameraInstance()) == null) {
            return;
        }
        camera.startSmoothZoom(n);
    }

    public int stopBurstCapture() {
        this.mCameraExtension.stopBurstShot();
        this.mLatestCachedParameters.set("sony-burst-shot", "off");
        this.setJpegEncodingQuality(2);
        this.trySetParametersToDevice(this.mLatestCachedParameters);
        new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId).put(new BurstShooting(BurstShooting.Value.OFF)).publish();
        return this.mLatestBurstId;
    }

    public void stopFaceDetection() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.stopFaceDetectionNotSynchronized();
            return;
        }
    }

    public void stopFaceIdService() {
        if (this.mFaceIdService != null) {
            this.mFaceIdService.release();
            this.mFaceIdService = null;
        }
    }

    public void stopLiveViewFinder() {
        this.stopFaceDetection();
        this.stopSceneRecognition();
        this.stopPreview();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void stopRecording() {
        var4_1 = this;
        // MONITORENTER : var4_1
        var2_2 = this.mVideoRecorder;
        ** if (var2_2 == null) goto lbl7
lbl5: // 1 sources:
        try {
            this.mVideoRecorder.stop();
lbl7: // 2 sources:
            // MONITOREXIT : var4_1
            return;
        }
        catch (RecorderException var3_3) {
            throw new RuntimeException("stopRecording():[Failed to stop MediaRecorder.]");
        }
    }

    public void stopSceneRecognition() {
        CameraDeviceHandler cameraDeviceHandler = this;
        synchronized (cameraDeviceHandler) {
            this.stopSceneRecognitionNotSynchronized();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void stopSmoothZoom() {
        Camera camera;
        if (this.mOnZoomChangedCallback == null || (camera = this.getCameraInstance()) == null) {
            return;
        }
        camera.stopSmoothZoom();
        this.requestCacheParameters();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void suspendPhotoLight() {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (parameters == null) return;
        if (PlatformDependencyResolver.isFlashLightSupported(parameters)) {
            if (this.mFlashSetting != Flash.LED_ON) return;
            {
                this.setCameraLight(Flash.LED_OFF.getValue());
                return;
            }
        }
        if (!(PlatformDependencyResolver.isPhotoLightSupported(parameters) && this.mPhotoLightSetting == PhotoLight.ON)) {
            return;
        }
        this.setCameraLight(PhotoLight.OFF.getValue());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void takePicture(PhotoSavingRequest photoSavingRequest) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return;
        }
        if (this.mIsLaunchAndCaptureShutterSoundEnabled) {
            super.resetShutterSound();
        }
        this.mLastSavingRequest = photoSavingRequest;
        camera.takePicture((Camera.ShutterCallback)this.mOnShutterCallback, null, (Camera.PictureCallback)this.mOnPictureTakenCallback);
        EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(this.mContext, this.mCameraDeviceId);
        DeviceStatus.Value value = photoSavingRequest.common.savedFileType == SavingTaskManager.SavedFileType.PHOTO_DURING_REC ? DeviceStatus.Value.PICTURE_TAKING_DURING_VIDEO_RECORDING : DeviceStatus.Value.PICTURE_TAKING;
        eachCameraStatusPublisher.put(new DeviceStatus(value)).publish();
    }

    public boolean trySetParametersToDevice(Camera.Parameters parameters) {
        Camera camera = super.getCameraInstance();
        if (camera == null) {
            return false;
        }
        try {
            CameraParameterValidator.validate(this.mContext, 2131099648, 2131099649, parameters);
            super.doSetParametersToDevice(camera, this.mLatestCachedParameters);
            this.mLatestCachedParameters = camera.getParameters();
            return true;
        }
        catch (RuntimeException var3_3) {
            CameraLogger.e("CameraDeviceHandler", ".trySetParametersToDevice():[RuntimeException:Failed to setParameters()]");
            CameraLogger.e("CameraDeviceHandler", ".trySetParametersToDevice():[mIsRecording = " + (Object)this.mIsRecording + "]");
            this.mLatestCachedParameters = null;
            return false;
        }
    }

    public boolean waitForCameraInitialization() {
        if (this.getCameraInstance() != null) {
            return true;
        }
        return false;
    }

    public boolean willPreviewBeRestarted() {
        Camera.Parameters parameters = this.getLatestCachedParameters();
        if (!PlatformDependencyResolver.getOptimalPreviewSize(parameters, 1, this.mPictureRect).equals((Object)PlatformDependencyResolver.getOptimalPreviewSize(parameters, 2, this.mCamcordRect))) {
            return true;
        }
        return false;
    }

    /*
     * Failed to analyse overrides
     */
    class BurstStorageFullDetector
    implements CameraExtension.StorageFullDetector {
        BurstStorageFullDetector() {
        }

        public boolean isCurrentStorageFull() {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                return CameraDeviceHandler.this.mStateMachine.isStorageFull();
            }
            return false;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class CloseDeviceTask
    implements Runnable {
        final /* synthetic */ CameraDeviceHandler this$0;

        private CloseDeviceTask(CameraDeviceHandler cameraDeviceHandler) {
            this.this$0 = cameraDeviceHandler;
        }

        /* synthetic */ CloseDeviceTask(CameraDeviceHandler cameraDeviceHandler,  var2_2) {
            super(cameraDeviceHandler);
        }

        public void run() {
            this.this$0.awaitFinishingRecording();
            this.this$0.restoreSettingAfterRecording();
            this.this$0.releaseCamera();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class FastCaptureOrientation
    extends OrientationEventListener {
        private int mOrientation;

        FastCaptureOrientation(Context context) {
            super(context);
            this.mOrientation = -1;
        }

        int getOrientation() {
            int n = RotationUtil.getNormalizedRotation(this.mOrientation);
            switch (CameraDeviceHandler.access$3300((CameraDeviceHandler)CameraDeviceHandler.this).facing) {
                default: {
                    return (n + CameraDeviceHandler.access$3300((CameraDeviceHandler)CameraDeviceHandler.this).orientation) % 360;
                }
                case 0: {
                    return (n + CameraDeviceHandler.access$3300((CameraDeviceHandler)CameraDeviceHandler.this).orientation) % 360;
                }
                case 1: 
            }
            return (360 + CameraDeviceHandler.access$3300((CameraDeviceHandler)CameraDeviceHandler.this).orientation - n) % 360;
        }

        public void onOrientationChanged(int n) {
            this.mOrientation = n;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class LoadSettingsThread
    extends Thread {
        final /* synthetic */ CameraDeviceHandler this$0;

        private LoadSettingsThread(CameraDeviceHandler cameraDeviceHandler) {
            this.this$0 = cameraDeviceHandler;
        }

        /* synthetic */ LoadSettingsThread(CameraDeviceHandler cameraDeviceHandler, com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler$1 var2_2) {
            super(cameraDeviceHandler);
        }

        public void run() {
            this.this$0.loadSettings();
            if (this.this$0.mFastCaptureSetting == FastCapture.LAUNCH_AND_CAPTURE) {
                if (PresetConfigurationResolver.isGeoTagEnabled(this.this$0.mCommonSettings.get(CommonSettingKey.GEO_TAG), this.this$0.mContext)) {
                    this.this$0.mGeotagManager = new GeotagManager(this.this$0.mContext);
                    this.this$0.mGeotagManager.assignResource();
                    this.this$0.mUiThreadHandler.post((Runnable)new Runnable(){

                        public void run() {
                            if (LoadSettingsThread.this.this$0.mContext != null && LoadSettingsThread.this.this$0.mGeotagManager != null) {
                                LoadSettingsThread.this.this$0.mGeotagManager.startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(LoadSettingsThread.this.this$0.mContext, "gps"), LocationSettingsReader.isLocationProviderAllowed(LoadSettingsThread.this.this$0.mContext, "network"));
                            }
                        }
                    });
                }
                this.this$0.mFastCaptureOrientation = new FastCaptureOrientation(this.this$0.mContext);
                this.this$0.mFastCaptureOrientation.enable();
            }
            if (this.this$0.mIsSmartCoverCamera) {
                this.this$0.overlaySmartCoverCameraSetting();
            }
        }

    }

    /*
     * Failed to analyse overrides
     */
    class OnAutoFocusCallback
    implements CameraExtension.AutoFocusCallback {
        OnAutoFocusCallback() {
        }

        public void onAutoFocus(CameraExtension.AutoFocusResult autoFocusResult) {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onAutoFocusDone(autoFocusResult.isFocused());
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnBurstShotCallback
    implements CameraExtension.BurstShotCallback {
        OnBurstShotCallback() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void onBurstFrameDropped(CameraExtension.BurstShotResult burstShotResult) {
            if (CameraDeviceHandler.this.mStateMachine == null) return;
            switch (burstShotResult.mErrorCode) {
                default: {
                    CameraLogger.e("CameraDeviceHandler", "OnBurstFrameDropped():[UnExpected Error] [ErrorCode=" + burstShotResult.mErrorCode + "]");
                    return;
                }
                case 1: 
            }
            CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
        }

        public void onBurstGroupStoreCompleted(CameraExtension.BurstShotResult burstShotResult) {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BURST_GROUP_STORE_COMPLETED, new Object[]{burstShotResult});
            }
        }

        public void onBurstPictureStoreCompleted(CameraExtension.BurstShotResult burstShotResult) {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BURST_STORE_COMPLETED, new Object[0]);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void onBurstShutterDone(CameraExtension.BurstShotResult burstShotResult) {
            if (CameraDeviceHandler.this.mStateMachine == null) return;
            switch (burstShotResult.mErrorCode) {
                default: {
                    CameraLogger.e("CameraDeviceHandler", "OnBurstShutterDone():[UnExpected Error] [ErrorCode=" + burstShotResult.mErrorCode + "]");
                    return;
                }
                case 0: {
                    CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_BURST_SHUTTER_DONE, new Object[]{burstShotResult});
                    return;
                }
                case 2: 
            }
            CameraDeviceHandler.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnFaceDetectionCallback
    implements CameraExtension.FaceDetectionCallback {
        OnFaceDetectionCallback() {
        }

        public void onFaceDetection(CameraExtension.FaceDetectionResult faceDetectionResult) {
            if (CameraDeviceHandler.this.mStateMachine == null || faceDetectionResult == null) {
                return;
            }
            CameraDeviceHandler.this.mStateMachine.onFaceDetected(faceDetectionResult);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnObjectTrackedCallback
    implements CameraExtension.ObjectTrackingCallback {
        OnObjectTrackedCallback() {
        }

        public void onObjectTracked(CameraExtension.ObjectTrackingResult objectTrackingResult) {
            if (CameraDeviceHandler.this.mStateMachine == null || objectTrackingResult == null) {
                return;
            }
            CameraDeviceHandler.this.mStateMachine.onObjectTracked(objectTrackingResult);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnPictureTakenCallback
    implements Camera.PictureCallback {
        OnPictureTakenCallback() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onPictureTaken(byte[] arrby, Camera camera) {
            TestEventSender.onPictureTaken();
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onTakePictureDone(arrby, CameraDeviceHandler.this.mLastSavingRequest);
            }
            EachCameraStatusPublisher eachCameraStatusPublisher = new EachCameraStatusPublisher(CameraDeviceHandler.this.mContext, CameraDeviceHandler.this.mCameraDeviceId);
            DeviceStatus.Value value = CameraDeviceHandler.this.mVideoRecorder == null ? DeviceStatus.Value.STILL_PREVIEW : DeviceStatus.Value.VIDEO_RECORDING;
            eachCameraStatusPublisher.put(new DeviceStatus(value)).publish();
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnPreAutoFocusCallback
    implements CameraExtension.AutoFocusCallback {
        OnPreAutoFocusCallback() {
        }

        public void onAutoFocus(CameraExtension.AutoFocusResult autoFocusResult) {
            CameraDeviceHandler.this.mOnPreAutoFocusCallback = null;
            CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_SCAN_DONE);
            CameraDeviceHandler.this.mIsPreScanSucceeded = autoFocusResult.isFocused();
            if (CameraDeviceHandler.this.mStateMachine != null) {
                if (!CameraDeviceHandler.this.mIsPreScanSucceeded) {
                    CameraDeviceHandler.this.resetShutterSound();
                }
                CameraDeviceHandler.this.mStateMachine.onInitialAutoFocusDone(autoFocusResult.isFocused());
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnPreCapturePictureTakenCallback
    implements Camera.PictureCallback {
        OnPreCapturePictureTakenCallback() {
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Lifted jumps to return sites
         */
        public void onPictureTaken(byte[] arrby, Camera camera) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, false);
            MeasurePerformance.outResult();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, true);
            CameraDeviceHandler.this.changePreProcessStateTo(PreProcessState.PRE_CAPTURE_DONE);
            Camera camera2 = CameraDeviceHandler.this.getCameraInstance();
            if (camera2 != null) {
                camera2.startPreview();
            }
            if (CameraDeviceHandler.this.mStateMachine == null) return;
            CameraDeviceHandler.this.mStateMachine.onPreTakePictureDone(arrby, CameraDeviceHandler.this.mLastSavingRequest);
            return;
            catch (RuntimeException runtimeException) {
                if (CameraDeviceHandler.this.mStateMachine == null) return;
                CameraDeviceHandler.this.mStateMachine.onDeviceError(StateMachine.ErrorCode.ERROR_ON_START_PREVIEW, (Exception)runtimeException);
                return;
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnPreCaptureShutterCallback
    implements Camera.ShutterCallback {
        OnPreCaptureShutterCallback() {
        }

        public void onShutter() {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onPreShutterDone(CameraDeviceHandler.this.mLastSavingRequest);
            }
        }
    }

    private class OnPreviewFrameGrabbedCallback
    implements PreviewFrameGrabber.OnPreviewFrameGrabbedListener {
        private final PhotoSavingRequest mPreviewFrameRequest;

        public OnPreviewFrameGrabbedCallback(PhotoSavingRequest photoSavingRequest) {
            this.mPreviewFrameRequest = photoSavingRequest;
        }

        @Override
        public void onPreviewFrameGrabbed(PreviewFrameGrabber previewFrameGrabber, byte[] arrby) {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onTakePictureDone(arrby, this.mPreviewFrameRequest);
            }
        }

        @Override
        public void onPreviewShutterDone() {
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onShutterDone(this.mPreviewFrameRequest);
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnSceneModeChangedCallback
    implements CameraExtension.SceneRecognitionCallback {
        OnSceneModeChangedCallback() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onSceneModeChanged(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
            if (sceneRecognitionResult == null || CameraDeviceHandler.this.mStateMachine == null) {
                return;
            }
            CameraDeviceHandler.this.mStateMachine.onSceneModeChanged(sceneRecognitionResult);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class OnShutterCallback
    implements Camera.ShutterCallback {
        OnShutterCallback() {
        }

        public void onShutter() {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, false);
            MeasurePerformance.outResult();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STOT_TO_SHOT, true);
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onShutterDone(CameraDeviceHandler.this.mLastSavingRequest);
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    public class OnZoomChangedCallback
    implements Camera.OnZoomChangeListener {
        public void onZoomChange(int n, boolean bl, Camera camera) {
            CameraDeviceHandler.this.getLatestCachedParameters().setZoom(n);
            if (CameraDeviceHandler.this.mStateMachine != null) {
                CameraDeviceHandler.this.mStateMachine.onZoomChange(n, bl, camera);
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class OpenDeviceThread
    extends Thread {
        private static final String TAG = "OpenDeviceThread";
        private volatile boolean mIsSuccess;
        final /* synthetic */ CameraDeviceHandler this$0;

        private OpenDeviceThread(CameraDeviceHandler cameraDeviceHandler) {
            this.this$0 = cameraDeviceHandler;
            this.mIsSuccess = false;
        }

        /* synthetic */ OpenDeviceThread(CameraDeviceHandler cameraDeviceHandler,  var2_2) {
            super(cameraDeviceHandler);
        }

        public boolean isSuccess() {
            return this.mIsSuccess;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Lifted jumps to return sites
         */
        public void run() {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.OPEN_CAMERA_DEVICE_TASK, true);
            this.this$0.mCurrentDeviceState = 1;
            if (!this.this$0.openCamera()) {
                return;
            }
            this.this$0.mLatestCachedParameters = this.this$0.mCamera.getParameters();
            if (this.this$0.mLatestCachedParameters == null) {
                CameraLogger.e("OpenDeviceThread", "run: mLatestCachedParameters is null.");
                return;
            }
            try {
                this.this$0.mLoadSettingsThread.join(4000);
                if (this.this$0.mLoadSettingsThread.isAlive()) {
                    CameraLogger.e("OpenDeviceThread", "LoadSettingsThread Timeouted.");
                    return;
                }
            }
            catch (CancellationException var5_1) {
                CameraLogger.e("OpenDeviceThread", "LoadSettingsThread is Cancelled.", (Throwable)var5_1);
                return;
            }
            catch (InterruptedException var3_2) {
                CameraLogger.e("OpenDeviceThread", "LoadSettingsThread is Interrupted.", (Throwable)var3_2);
                return;
            }
            if (this.this$0.mCamera == null) return;
            this.this$0.setCurrentSettingsToParameters(CameraDeviceHandler.getCameraTypeFrom(this.this$0.mFastCaptureSetting), this.this$0.mLatestCachedParameters);
            CameraParameterValidator.validate(this.this$0.mContext, 2131099648, 2131099649, this.this$0.mLatestCachedParameters);
            this.this$0.doSetParametersToDevice(this.this$0.mCamera, this.this$0.mLatestCachedParameters);
            if (this.this$0.mCamera != null) {
                this.this$0.mCamera.startPreview();
            }
            if (this.this$0.mIsImmediateReleaseRequested) {
                this.this$0.releaseCameraImmediatelyNotSynchronized();
                this.this$0.mIsImmediateReleaseRequested = false;
                return;
            }
            if (CameraDeviceHandler.isPreProcessRequiredFrom(this.this$0.mFastCaptureSetting)) {
                this.this$0.preScan();
            }
            this.this$0.selectDefaultCameraLightSettingsIfSharedPreferencesIsEmpty(this.this$0.mLatestCachedParameters);
            this.mIsSuccess = true;
            this.this$0.mCurrentDeviceState = 2;
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.OPEN_CAMERA_DEVICE_TASK, false);
            Camera.Parameters parameters = this.this$0.mLatestCachedParameters;
            String string = parameters.get("sony-vs");
            String string2 = parameters.get("sony-video-nr");
            new EachCameraStatusPublisher(this.this$0.mContext, this.this$0.mCameraDeviceId).putFromParameter(this.this$0.mLatestCachedParameters).put(new DeviceStatus(DeviceStatus.Value.STILL_PREVIEW)).put((DeviceStatus)new VideoResolution(this.this$0.mCamcordRect)).put((VideoResolution)new VideoRecordingFps(this.this$0.mVideoSizeSetting.getVideoFrameRate())).put((VideoRecordingFps)VideoStabilizerStatus.fromCameraParameter(string)).put((VideoStabilizerStatus)VideoNoiseReduction.fromCameraParameter(string2)).publish();
        }
    }

    public static final class PreProcessState
    extends Enum<PreProcessState> {
        private static final /* synthetic */ PreProcessState[] $VALUES;
        public static final /* enum */ PreProcessState NOT_STARTED = new PreProcessState();
        public static final /* enum */ PreProcessState PRE_CAPTURE_DONE;
        public static final /* enum */ PreProcessState PRE_CAPTURE_STARTED;
        public static final /* enum */ PreProcessState PRE_SCAN_DONE;
        public static final /* enum */ PreProcessState PRE_SCAN_STARTED;

        static {
            PRE_SCAN_STARTED = new PreProcessState();
            PRE_SCAN_DONE = new PreProcessState();
            PRE_CAPTURE_STARTED = new PreProcessState();
            PRE_CAPTURE_DONE = new PreProcessState();
            PreProcessState[] arrpreProcessState = new PreProcessState[]{NOT_STARTED, PRE_SCAN_STARTED, PRE_SCAN_DONE, PRE_CAPTURE_STARTED, PRE_CAPTURE_DONE};
            $VALUES = arrpreProcessState;
        }

        private PreProcessState() {
            super(string, n);
        }

        public static PreProcessState valueOf(String string) {
            return (PreProcessState)Enum.valueOf((Class)PreProcessState.class, (String)string);
        }

        public static PreProcessState[] values() {
            return (PreProcessState[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class RequestContinuousPreviewFrameCallback
    implements Camera.PreviewCallback {
        final /* synthetic */ CameraDeviceHandler this$0;

        private RequestContinuousPreviewFrameCallback(CameraDeviceHandler cameraDeviceHandler) {
            this.this$0 = cameraDeviceHandler;
        }

        /* synthetic */ RequestContinuousPreviewFrameCallback(CameraDeviceHandler cameraDeviceHandler,  var2_2) {
            super(cameraDeviceHandler);
        }

        public void onPreviewFrame(byte[] arrby, Camera camera) {
            Camera camera2;
            if (this.this$0.mStateMachine != null) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED, arrby);
            }
            if ((camera2 = this.this$0.getCameraInstance()) != null) {
                camera2.setOneShotPreviewCallback((Camera.PreviewCallback)this.this$0.mRequestContinuousPreviewFrameCallback);
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class RequestOnePreviewFrameCallback
    implements Camera.PreviewCallback {
        final /* synthetic */ CameraDeviceHandler this$0;

        private RequestOnePreviewFrameCallback(CameraDeviceHandler cameraDeviceHandler) {
            this.this$0 = cameraDeviceHandler;
        }

        /* synthetic */ RequestOnePreviewFrameCallback(CameraDeviceHandler cameraDeviceHandler,  var2_2) {
            super(cameraDeviceHandler);
        }

        public void onPreviewFrame(byte[] arrby, Camera camera) {
            if (this.this$0.mStateMachine != null && this.this$0.mLatestCachedParameters != null) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo((int)this.this$0.mCameraDeviceId, (Camera.CameraInfo)cameraInfo);
                StateMachine stateMachine = this.this$0.mStateMachine;
                StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_ON_ONE_PREVIEW_FRAME_UPDATED;
                Object[] arrobject = new Object[]{arrby, this.this$0.mLatestCachedParameters.getPreviewFormat(), this.this$0.getPreviewRect()};
                stateMachine.sendEvent(transitterEvent, arrobject);
            }
        }
    }

}

