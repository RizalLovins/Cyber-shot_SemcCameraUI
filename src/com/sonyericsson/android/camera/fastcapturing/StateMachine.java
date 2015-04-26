/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.graphics.Point
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$CameraInfo
 *  android.hardware.Camera$Parameters
 *  android.location.Location
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.PowerManager
 *  android.os.storage.StorageManager
 *  android.os.storage.StorageManager$StorageType
 *  android.util.Log
 *  android.view.SurfaceHolder
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$BurstShotResult
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$FilePathGenerator
 *  com.sonyericsson.cameraextension.CameraExtension$MediaProviderUpdator
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingResult
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
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
 *  java.util.Set
 *  java.util.concurrent.CopyOnWriteArraySet
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.SurfaceHolder;
import com.sonyericsson.android.camera.CameraButtonIntentReceiver;
import com.sonyericsson.android.camera.burst.BurstInfoUpdator;
import com.sonyericsson.android.camera.burst.BurstShotPathBuilder;
import com.sonyericsson.android.camera.configuration.ConversationReader;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.ChapterThumbnail;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity;
import com.sonyericsson.android.camera.fastcapturing.PlatformDependencyResolver;
import com.sonyericsson.android.camera.fastcapturing.view.BaseFastViewFinder;
import com.sonyericsson.android.camera.mediasaving.BurstSavingTaskManager;
import com.sonyericsson.android.camera.mediasaving.MediaSavingUtil;
import com.sonyericsson.android.camera.util.LocalGoogleAnalyticsUtil;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.ThumbnailUtil;
import com.sonyericsson.cameracommon.controller.ZoomController;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.VideoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.updator.MediaProviderUpdator;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.AutoReviewWindow;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.selftimerfeedback.LedLight;
import com.sonyericsson.cameracommon.selftimerfeedback.SelfTimerFeedback;
import com.sonyericsson.cameracommon.sound.SoundPlayer;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.ClassDefinitionChecker;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RecordingUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.googleanalytics.parameters.CustomDimension;
import com.sonymobile.cameracommon.media.recorder.RecorderInterface;
import com.sonymobile.cameracommon.media.utility.AudioResourceChecker;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import com.sonymobile.cameracommon.vanilla.wearablebridge.common.AbstractCapturableState;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StateMachine
implements StorageController.StorageListener,
SavingRequest.StoreDataCallback {
    private static final int OBJECT_TRACKING_ENGINE_RESET_TIMEOUT_COUNT = 3000;
    private static final int PREPARE_PINCH_ZOOM_TIMEOUT_COUNT = 100;
    private static final int RESUME_TIMEOUT = 5000;
    private static final String TAG = StateMachine.class.getSimpleName();
    FastCapturingActivity mActivity;
    private StoreDataResult mBurstCoverResult;
    private int mBurstPictureCount;
    CameraDeviceHandler mCameraDeviceHandler;
    private ChangeCameraModeTask mChangeCameraModeTask;
    private ChapterThumbnail mChapterThumbnail;
    private ContentsViewController mContentsViewController = null;
    ConversationReader mConversationReader = null;
    private CapturingMode mCurrentCapturingMode;
    private State mCurrentState;
    private float mCurrentZoomLength;
    private ExecutorService mExecService = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler();
    private boolean mIsInModeLessRecording = false;
    private boolean mIsStopBurstRequested;
    private PhotoSavingRequest mLastPhotoSavingRequest = null;
    private VideoSavingRequest mLastVideoSavingRequest = null;
    private final Runnable mNotifyResumeTimeoutTask;
    private Set<OnStateChangedListener> mOnStateChangedListenerSet;
    private List<PhotoSavingRequest> mPhotoSavingRequestList = new ArrayList();
    private final RecorderInterface.RecorderListener mRecorderListener;
    private SoundPlayer mSoundPlayer;
    private StartRecordingTask mStartRecordingTask;
    private CapturingMode mTargetCapturingMode;
    private TrackedObjectLostTimeoutTask mTrackedObjectLostTimeoutTask;
    private int mUpdateProgressCount;
    BaseFastViewFinder mViewFinder;

    public StateMachine(FastCapturingActivity fastCapturingActivity) {
        this.mCurrentState = new StateNone((StateMachine)this);
        this.mUpdateProgressCount = 0;
        this.mChangeCameraModeTask = null;
        this.mStartRecordingTask = null;
        this.mSoundPlayer = null;
        this.mOnStateChangedListenerSet = new CopyOnWriteArraySet();
        this.mNotifyResumeTimeoutTask = new Runnable(){

            public void run() {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_RESUME_TIMEOUT, new Object[0]);
            }
        };
        this.mChapterThumbnail = null;
        this.mRecorderListener = new RecorderInterface.RecorderListener(){

            @Override
            public void onRecordError(int n, int n2) {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_RECORDING_ERROR, new Object[0]);
                StateMachine.this.mCameraDeviceHandler.releaseVideo();
            }

            @Override
            public void onRecordFinished(RecorderInterface.Result result) {
                switch (.$SwitchMap$com$sonymobile$cameracommon$media$recorder$RecorderInterface$Result[result.ordinal()]) {
                    default: {
                        return;
                    }
                    case 1: 
                    case 2: 
                    case 3: {
                        if (StateMachine.this.mContentsViewController != null) {
                            StateMachine.this.mContentsViewController.enableClick();
                        }
                        StateMachine.this.mCameraDeviceHandler.finalizeRecording();
                        StateMachine.this.mCameraDeviceHandler.restoreSettingAfterRecording();
                        return;
                    }
                    case 4: 
                }
                Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, 1, 0);
            }

            @Override
            public void onRecordPaused(RecorderInterface.Result result) {
            }

            @Override
            public void onRecordProgress(long l) {
                StateMachine.this.updateRecordingProgress((int)l);
            }
        };
        this.mTrackedObjectLostTimeoutTask = new TrackedObjectLostTimeoutTask((StateMachine)this, null);
        this.mCurrentCapturingMode = CapturingMode.SCENE_RECOGNITION;
        this.mTargetCapturingMode = CapturingMode.SCENE_RECOGNITION;
        this.mActivity = fastCapturingActivity;
    }

    static /* synthetic */ void access$300(StateMachine stateMachine, StoreDataResult storeDataResult) {
        stateMachine.launchEditor(storeDataResult);
    }

    static /* synthetic */ void access$400(StateMachine stateMachine, StoreDataResult storeDataResult) {
        stateMachine.doStoreComplete(storeDataResult);
    }

    static /* synthetic */ boolean access$500(StateMachine stateMachine, StoreDataResult storeDataResult) {
        return stateMachine.notifyStoreDone(storeDataResult);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void calculateRemainStorage(boolean bl, boolean bl2) {
        if (this.mCameraDeviceHandler == null) return;
        if (this.mViewFinder == null) return;
        if (this.mCameraDeviceHandler.getPictureRect() == null) return;
        if (this.mCameraDeviceHandler.getCamcordRect() == null) return;
        if (this.mActivity == null) {
            return;
        }
        long l = this.mActivity.getStorageManager().updateRemain(0, bl2);
        if (bl) {
            if (l > 153600) {
                this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_HIDE_REMAIN_INDICATOR, new Object[0]);
            } else {
                this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_REMAIN_INDICATOR, new Object[0]);
            }
        }
        if (l > 61440) return;
        this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
        this.mActivity.disableAutoOffTimer();
    }

    private void cancelAutoFocus(boolean bl) {
        if (bl) {
            this.mCameraDeviceHandler.resetFocusMode();
        }
        this.mCameraDeviceHandler.cancelAutoFocus();
    }

    private void changeCameraModeTo(int n, boolean bl, boolean bl2) {
        if (this.mCameraDeviceHandler.willPreviewBeRestarted()) {
            this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_HIDE_SURFACE, new Object[0]);
        }
        this.mChangeCameraModeTask = new ChangeCameraModeTask(n, bl, bl2);
        this.mHandler.post((Runnable)this.mChangeCameraModeTask);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private /* varargs */ void changeTo(State state, Object ... arrobject) {
        void var5_3 = this;
        synchronized (var5_3) {
            this.mCurrentState.exit();
            this.mCurrentState = state;
            Iterator iterator = this.mOnStateChangedListenerSet.iterator();
            do {
                if (!iterator.hasNext()) {
                    this.mCurrentState.entry();
                    return;
                }
                ((OnStateChangedListener)iterator.next()).onStateChanged(this.mCurrentState.getCaptureState(), arrobject);
            } while (true);
        }
    }

    private void changeToBackCameraMode() {
        this.mTargetCapturingMode = CapturingMode.SCENE_RECOGNITION;
        this.changeTo(new StateCameraSwitching(this), new Object[0]);
        this.mActivity.prepareCameraDeviceHandler((Context)this.mActivity, FastCapture.LAUNCH_ONLY, 0);
        this.mViewFinder.checkSurfaceIsPreparedOrNot();
        this.mCurrentCapturingMode = this.mTargetCapturingMode;
    }

    private void changeToFrontCameraMode() {
        this.mTargetCapturingMode = CapturingMode.FRONT_PHOTO;
        this.changeTo(new StateCameraSwitching(this), new Object[0]);
        this.mActivity.prepareCameraDeviceHandler((Context)this.mActivity, FastCapture.LAUNCH_ONLY, 1);
        this.mViewFinder.checkSurfaceIsPreparedOrNot();
        this.mCurrentCapturingMode = this.mTargetCapturingMode;
    }

    private void checkCallback(SavingRequest savingRequest) {
        savingRequest.addCallback((SavingRequest.StoreDataCallback)this);
    }

    private void clearBurst() {
        this.mIsStopBurstRequested = false;
        this.mBurstPictureCount = 0;
        this.mLastPhotoSavingRequest = null;
    }

    private PhotoSavingRequest createBurstSavingRequest(int n) {
        PhotoSavingRequest photoSavingRequest = super.createPhotoSavingRequest(SavingTaskManager.SavedFileType.BURST);
        if (photoSavingRequest != null) {
            photoSavingRequest.setSomcType(2);
            this.mCameraDeviceHandler.applySavingRequest(photoSavingRequest, this.mCameraDeviceHandler.getLatestCachedParameters());
            photoSavingRequest.setRequestId(n);
        }
        return photoSavingRequest;
    }

    private PhotoSavingRequest createPhotoSavingRequest(SavingTaskManager.SavedFileType savedFileType) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$SavingTaskManager$SavedFileType[savedFileType.ordinal()]) {
            default: {
                throw new IllegalArgumentException("Unexpected type : " + (Object)savedFileType);
            }
            case 2: 
            case 3: 
            case 4: 
        }
        PhotoSavingRequest photoSavingRequest = new PhotoSavingRequest(super.createTakenStatusCommon(savedFileType, this.mCameraDeviceHandler.getPictureRect(), "image/jpeg", ".JPG", null), new TakenStatusPhoto());
        photoSavingRequest.addCallback((SavingRequest.StoreDataCallback)this);
        photoSavingRequest.setExtraOutput(this.mActivity.getExtraOutput());
        return photoSavingRequest;
    }

    /*
     * Enabled aggressive block sorting
     */
    private TakenStatusCommon createTakenStatusCommon(SavingTaskManager.SavedFileType savedFileType, Rect rect, String string, String string2, String string3) {
        long l = System.currentTimeMillis();
        int n = this.getOrientation();
        Location location = this.mActivity.getGeoTagManager().getCurrentLocation();
        boolean bl = this.isSuperResolutionZoom();
        int n2 = .$SwitchMap$com$sonyericsson$cameracommon$mediasaving$SavingTaskManager$SavedFileType[savedFileType.ordinal()];
        boolean bl2 = false;
        switch (n2) {
            case 4: {
                bl2 = true;
            }
            default: {
                return new TakenStatusCommon(l, n, location, rect.width(), rect.height(), string, string2, savedFileType, string3, "", bl2, bl);
            }
            case 1: 
        }
        bl2 = false;
        bl = false;
        return new TakenStatusCommon(l, n, location, rect.width(), rect.height(), string, string2, savedFileType, string3, "", bl2, bl);
    }

    /*
     * Exception decompiling
     */
    private VideoSavingRequest createVideoSavingRequest(SavingTaskManager.SavedFileType var1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:436)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:62)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:400)
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

    private void doCapture() {
        this.mLastPhotoSavingRequest = this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO);
        if (this.mLastPhotoSavingRequest != null) {
            this.mCameraDeviceHandler.applySavingRequest(this.mLastPhotoSavingRequest, this.mCameraDeviceHandler.getLatestCachedParameters());
            this.mCameraDeviceHandler.takePicture(this.mLastPhotoSavingRequest);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void doCaptureBestEffortBurst(boolean bl) {
        int n = -1;
        if (bl) {
            n = this.mViewFinder.getRequestId(false);
        } else if (this.mLastPhotoSavingRequest != null) {
            n = this.mLastPhotoSavingRequest.getRequestId();
        }
        this.mLastPhotoSavingRequest = super.createBurstSavingRequest(n);
        if (this.mLastPhotoSavingRequest != null) {
            if (bl) {
                this.mLastPhotoSavingRequest.setSomcType(2);
                BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVEVT_REQUEST_ADD_COUNT_UP_VIEW;
                Object[] arrobject = new Object[]{this.mLastPhotoSavingRequest.getRequestId()};
                baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            } else {
                this.mLastPhotoSavingRequest.setSomcType(129);
            }
            this.mCameraDeviceHandler.takePicture(this.mLastPhotoSavingRequest);
            this.mBurstPictureCount = 1 + this.mBurstPictureCount;
        }
    }

    private void doCaptureWhileRecording() {
        PhotoSavingRequest photoSavingRequest = this.createPhotoSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
        if (photoSavingRequest != null) {
            photoSavingRequest.setRequestId(this.mViewFinder.getRequestId(true));
            if (this.mContentsViewController != null) {
                this.mContentsViewController.hideThumbnail();
            }
            this.mViewFinder.onShutterDone(true);
            this.mCameraDeviceHandler.captureWhileRecording(photoSavingRequest);
        }
    }

    private void doChangeSelectedFace(Point point) {
        this.mCameraDeviceHandler.deselectObject();
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTION_STARTED, new Object[0]);
        this.mCameraDeviceHandler.setSelectedFacePosition(point.x, point.y);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void doDeselectObject() {
        if (!(this.mCameraDeviceHandler != null && this.mCameraDeviceHandler.deselectObject())) {
            return;
        }
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
    }

    private void doFastestCamcord() {
        if (!this.mActivity.getStorageManager().isReady()) {
            return;
        }
        this.mActivity.pauseAudioPlayback();
        this.changeTo(new StateVideoRecordingStartingFromFastestCamcord(true), new Object[0]);
        this.calculateRemainStorage(false, false);
        this.mLastVideoSavingRequest = this.createVideoSavingRequest(SavingTaskManager.SavedFileType.VIDEO);
        this.mViewFinder.setRecordingOrientation(this.mActivity.getOrientation());
        if (this.mContentsViewController != null) {
            this.mContentsViewController.disableClick();
        }
        this.mExecService.execute((Runnable)new DoFastestCamcordTask(this, null));
    }

    private void doFastestCapture() {
        if (!this.mActivity.getStorageManager().isReady()) {
            this.changeTo(new StateWarning(), new Object[0]);
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$CameraDeviceHandler$PreProcessState[this.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
            default: {
                throw new IllegalStateException("Un-Expected state : " + (Object)this.mCameraDeviceHandler.getPreProcessState());
            }
            case 2: {
                this.changeTo(new StatePhotoCaptureWaitForAfDone(true, false), new Object[0]);
                return;
            }
            case 1: {
                this.doCapture();
                this.changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 3: {
                this.changeTo(new StatePhotoCapture(), new Object[0]);
                return;
            }
            case 4: 
        }
        this.changeTo(new StatePhotoCapture(), new Object[0]);
    }

    private void doHandleRecordingError() {
        this.mActivity.getMessagePopup().showUnknownErrorMessage();
        this.changeTo(new StateWarning(), new Object[0]);
    }

    private void doPauseRecording() {
        this.mCameraDeviceHandler.pauseRecording();
        this.changeTo(new StateVideoRecordingPausing(), new Object[0]);
    }

    private void doResumeRecording() {
        this.mCameraDeviceHandler.resumeRecording();
        this.changeTo(new StateVideoRecording(this), new Object[0]);
    }

    private void doSelectObject(PointF pointF) {
        if (!this.mCameraDeviceHandler.selectObject(pointF)) {
            return;
        }
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_OBJECT_TRACKING_STARTED, new Object[0]);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void doStartHighSpeedBurst(State state, boolean bl) {
        PhotoSavingRequest photoSavingRequest;
        BurstShotPathBuilder burstShotPathBuilder;
        this.mLastPhotoSavingRequest = photoSavingRequest = super.createBurstSavingRequest(this.mViewFinder.getRequestId(false));
        BurstInfoUpdator burstInfoUpdator = new BurstInfoUpdator((BaseActivity)this.mActivity, photoSavingRequest);
        ((BurstSavingTaskManager)this.mActivity.getSavingTaskManager()).setUpdator(burstInfoUpdator);
        burstInfoUpdator.prepareBulkInsert();
        try {
            burstShotPathBuilder = new BurstShotPathBuilder();
        }
        catch (IOException var12_12) {
            super.cancelAutoFocus(false);
            burstShotPathBuilder = null;
        }
        boolean bl2 = false;
        if (burstShotPathBuilder != null) {
            int n = this.mCameraDeviceHandler.startBurstCapture((CameraExtension.FilePathGenerator)burstShotPathBuilder, (CameraExtension.MediaProviderUpdator)burstInfoUpdator);
            bl2 = false;
            if (n != -1 && (bl2 = true)) {
                Object[] arrobject = new Object[]{bl};
                super.changeTo(state, arrobject);
                if (photoSavingRequest != null) {
                    BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
                    BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVEVT_REQUEST_ADD_COUNT_UP_VIEW;
                    Object[] arrobject2 = new Object[]{photoSavingRequest.getRequestId()};
                    baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
                }
                return;
            }
        }
        super.changeTo(new StateWarning(), new Object[0]);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private /* varargs */ void doStartRecording(Object ... arrobject) {
        CameraButtonIntentReceiver.killStartUpNotifierImmediately();
        if (this.mContentsViewController != null) {
            this.mContentsViewController.disableClick();
        }
        this.mLastVideoSavingRequest = super.createVideoSavingRequest(SavingTaskManager.SavedFileType.VIDEO);
        this.mViewFinder.setRecordingOrientation(this.mActivity.getOrientation());
        VideoSavingRequest videoSavingRequest = this.mLastVideoSavingRequest;
        boolean bl = false;
        if (videoSavingRequest != null) {
            VideoSize videoSize = VideoSize.getValueFromFrameSize(this.mLastVideoSavingRequest.common.width, this.mLastVideoSavingRequest.common.height);
            VideoSize videoSize2 = VideoSize.MMS;
            boolean bl2 = false;
            if (videoSize == videoSize2) {
                bl2 = true;
            }
            VideoHdr videoHdr = this.mCameraDeviceHandler.getVideoHdr();
            VideoHdr videoHdr2 = VideoHdr.ON;
            boolean bl3 = false;
            if (videoHdr == videoHdr2) {
                bl3 = true;
            }
            BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_PREPARE_RECORDING_INDICATOR;
            Object[] arrobject2 = new Object[]{(int)this.mLastVideoSavingRequest.video.maxDurationMills, bl2, bl3};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            try {
                this.mCameraDeviceHandler.startRecording(this.mLastVideoSavingRequest, this.mRecorderListener, this.mSoundPlayer);
                super.updateDateTaken(this.mLastVideoSavingRequest);
                bl = true;
            }
            catch (RuntimeException var13_13) {
                CameraLogger.w(TAG, "Start recording failed.", (Throwable)var13_13);
                if (this.mContentsViewController != null) {
                    this.mContentsViewController.enableClick();
                }
                if (!AudioResourceChecker.isAudioResourceAvailable((BaseActivity)this.mActivity)) {
                    this.mActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
                    super.changeTo(new StateStandby((StateMachine)this), new Object[0]);
                    return;
                }
                this.mActivity.getMessagePopup().showErrorUncancelable(2131361870, 2131361905, false);
                GoogleAnalyticsUtil.sendCameraNotAvailableEvent();
                bl = false;
            }
            if (bl) {
                super.changeTo(new StateVideoRecordingStarting(false), arrobject);
                return;
            }
        }
        super.changeTo(new StateWarning(), new Object[0]);
    }

    private void doStartRecordingInPhotoMode() {
        this.mViewFinder.hideHudIcons();
        this.changeTo(new StateNone(this), new Object[0]);
        if (!PlatformDependencyResolver.isAutoSceneRecogntionDuringRecSupported(this.mCameraDeviceHandler.getLatestCachedParameters())) {
            this.mCameraDeviceHandler.stopSceneRecognition();
        }
        this.changeCameraModeTo(2, false, this.mIsInModeLessRecording);
        this.mStartRecordingTask = new StartRecordingTask(this, null);
        this.mHandler.post((Runnable)this.mStartRecordingTask);
    }

    private void doStopBestEffortBurst() {
        this.cancelAutoFocus(true);
        this.mViewFinder.fadeoutCounter();
        this.onBurstShootingDone();
    }

    private boolean doStopHighSpeedBurst() {
        int n = this.mCameraDeviceHandler.stopBurstCapture();
        this.cancelAutoFocus(false);
        this.onBurstShootingDone();
        if (n != -1) {
            return true;
        }
        CameraLogger.e(TAG, "doStopHighSpeedBurst Failed.");
        return false;
    }

    private void doStopRecording() {
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_STOP, true);
        if (this.mContentsViewController != null) {
            this.mContentsViewController.enableClick();
        }
        this.mCameraDeviceHandler.stopRecording();
        this.mViewFinder.onCaptureDone();
        if (VideoStabilizer.isIntelligentActive(this.mCameraDeviceHandler.getVideoStabilizer())) {
            this.mLastVideoSavingRequest.setRequestId(this.mViewFinder.getRequestId(true));
        }
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_STOP, false);
        MeasurePerformance.outResult();
    }

    private void doStopZoom() {
        this.mCameraDeviceHandler.stopSmoothZoom();
    }

    private void doStoreComplete(StoreDataResult storeDataResult) {
        final int n = storeDataResult.savingRequest.getRequestId();
        final boolean bl = storeDataResult.isSuccess();
        final Uri uri = storeDataResult.uri;
        this.mActivity.runOnUiThread((Runnable)new Runnable(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             */
            public void run() {
                StateMachine stateMachine;
                StateMachine stateMachine2 = stateMachine = StateMachine.this;
                synchronized (stateMachine2) {
                    if (StateMachine.this.mContentsViewController != null) {
                        if (n != -1) {
                            if (bl) {
                                StateMachine.this.mContentsViewController.addContent(n, uri);
                            } else {
                                StateMachine.this.mContentsViewController.pause();
                                StateMachine.this.mContentsViewController.reload();
                            }
                        } else {
                            StateMachine.this.mContentsViewController.pause();
                            StateMachine.this.mContentsViewController.reload();
                        }
                    }
                    return;
                }
            }
        });
    }

    private void doSuperResolutionZoomIn() {
        this.mCameraDeviceHandler.startSmoothZoom(this.mCameraDeviceHandler.getMaxSuperResolutionZoom());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void doZoom(int n, float f) {
        int n2 = this.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
        int n3 = this.mCameraDeviceHandler.getMaxSuperResolutionZoom();
        int n4 = this.mCameraDeviceHandler.getMaxZoom();
        if (f > 0.0f && n2 == n3 && n < n3) {
            return;
        }
        this.mCurrentZoomLength = ZoomController.getZoomValue(n2, f);
        if (this.mCurrentZoomLength < 0.0f) {
            this.mCurrentZoomLength = 0.0f;
        } else if (this.mCurrentZoomLength < (float)n3) {
            if (this.mCurrentZoomLength > (float)n3) {
                this.mCurrentZoomLength = n3;
            }
        } else if (this.mCurrentZoomLength > (float)n4) {
            this.mCurrentZoomLength = n4;
        }
        this.mCameraDeviceHandler.startSmoothZoom(Math.round((float)this.mCurrentZoomLength));
    }

    private void doZoomIn() {
        this.mCameraDeviceHandler.startSmoothZoom(this.mCameraDeviceHandler.getMaxZoom());
    }

    private void doZoomOut() {
        this.mCameraDeviceHandler.startSmoothZoom(0);
    }

    private int getCameraId(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION) {
            return 0;
        }
        return 1;
    }

    private long getMaxDurationMillisecondForCamcord(VideoSize videoSize, long l) {
        long l2;
        long l3;
        long l4;
        long l5;
        long l6 = l4 = RecordingUtil.getDurationMillsFromAverage(this.mActivity.getStorageController().getAvailableStorageSize(), videoSize.getAverageFileSize());
        if (videoSize.isConstraint() && (l2 = 1000 * (long)this.mConversationReader.getMmsOptions().mMaxDuration) < l4) {
            l6 = l2;
        }
        if (l < (l3 = l6 / 1000) * (l5 = 1024 * videoSize.getAverageFileSize() / 60)) {
            l6 = 1000 * l / l5;
        }
        if (Integer.MAX_VALUE < l6) {
            l6 = Integer.MAX_VALUE;
        }
        return l6;
    }

    private long getMaxFileSizeBytesForCamcord(VideoSize videoSize) {
        long l;
        long l2 = l = 1024 * RecordingUtil.getRecordableSizeKBytes(this.mActivity.getStorageController());
        if (videoSize.isConstraint()) {
            long l3;
            if (this.mConversationReader == null) {
                this.mConversationReader = new ConversationReader();
                this.mConversationReader.readConversationProvider((Context)this.mActivity);
            }
            if ((l3 = this.mConversationReader.getMmsOptions().mMaxFileSizeBytes) < l) {
                l2 = l3;
            }
        }
        return l2;
    }

    private int getOrientation() {
        int n = RotationUtil.getNormalizedRotation(this.mActivity.getSensorOrientationDegree());
        Camera.CameraInfo cameraInfo = CameraDevice.getCameraInfo(this.getCurrentCameraId());
        switch (cameraInfo.facing) {
            default: {
                return (n + cameraInfo.orientation) % 360;
            }
            case 0: {
                return (n + cameraInfo.orientation) % 360;
            }
            case 1: 
        }
        return (360 + cameraInfo.orientation - n) % 360;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isBurstShotEnabled() {
        if (this.isPhotoSelfTimerEnabled() || this.mCameraDeviceHandler.getBurstShotSetting() == BurstShot.OFF) {
            return false;
        }
        return true;
    }

    private boolean isPhotoSelfTimerEnabled() {
        if (this.mViewFinder.getPhotoSelfTimerSetting() != SelfTimer.OFF) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isSmoothZoomEnabled() {
        Camera.Parameters parameters;
        if (this.mCameraDeviceHandler.isCameraFront() || (parameters = this.mCameraDeviceHandler.getLatestCachedParameters()) == null || !parameters.isSmoothZoomSupported()) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean isSuperResolutionZoom() {
        if (this.isInModeLessRecording()) {
            return false;
        }
        Camera.Parameters parameters = this.mCameraDeviceHandler.getLatestCachedParameters();
        if (parameters == null) return false;
        int n = parameters.getZoom();
        int n2 = this.mCameraDeviceHandler.getMaxSuperResolutionZoom();
        if (!PlatformDependencyResolver.isSuperResolutionZoomSupported(parameters)) return false;
        if (n <= 0) return false;
        if (n > n2) return false;
        return true;
    }

    private void launchEditor(StoreDataResult storeDataResult) {
        if (AutoReviewWindow.isEditorAvailable((Context)this.mActivity, storeDataResult.uri, storeDataResult.savingRequest.common.mimeType)) {
            this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_AUTO_REVIEW, storeDataResult);
            return;
        }
        super.changeTo(new StateStandby((StateMachine)this, true), new Object[0]);
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    private void moveToCameraNotAvailable() {
        CameraLogger.e(TAG, ".startFastCapture():[Camera not available]");
        this.mActivity.getMessagePopup().showErrorUncancelable(2131361870, 2131361905, false);
        this.changeTo(new StateWarning(), new Object[0]);
        GoogleAnalyticsUtil.sendCameraNotAvailableEvent();
    }

    private void notifyStateBlocked() {
        if (this.mActivity.getWearableBridge() != null) {
            this.mActivity.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.BLOCKED);
            this.mActivity.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.BLOCKED);
        }
    }

    private void notifyStateIdle() {
        if (this.mActivity.getWearableBridge() != null) {
            this.mActivity.getWearableBridge().getPhotoStateNotifier().onStateChanged(AbstractCapturableState.AbstractPhotoState.IDLE);
            this.mActivity.getWearableBridge().getVideoStateNotifier().onStateChanged(AbstractCapturableState.AbstractVideoState.IDLE);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean notifyStoreDone(StoreDataResult storeDataResult) {
        if (storeDataResult == null || storeDataResult.savingRequest == null || storeDataResult.savingRequest.common == null || this.mLastPhotoSavingRequest == null && this.mLastVideoSavingRequest == null) {
            return false;
        }
        int n = this.mLastPhotoSavingRequest == null ? this.mLastVideoSavingRequest.getRequestId() : (this.mLastVideoSavingRequest == null ? this.mLastPhotoSavingRequest.getRequestId() : (this.mLastPhotoSavingRequest.getRequestId() > this.mLastVideoSavingRequest.getRequestId() ? this.mLastPhotoSavingRequest.getRequestId() : this.mLastVideoSavingRequest.getRequestId()));
        if (storeDataResult.savingRequest.getRequestId() == n) {
            BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_ICONS_ON_REVIEW_WINDOW;
            Object[] arrobject = new Object[]{storeDataResult.uri, storeDataResult.savingRequest.common.mimeType};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            return true;
        }
        return false;
    }

    private void requestPlayAutoFocusSuccessSound(boolean bl) {
        if (bl && !this.mCameraDeviceHandler.isShutterSoundOff()) {
            this.mActivity.playSound(0);
        }
    }

    private void requestStorePicture(PhotoSavingRequest photoSavingRequest) {
        void var4_2 = this;
        synchronized (var4_2) {
            RequestStoreTask requestStoreTask = new RequestStoreTask(photoSavingRequest);
            this.mExecService.execute((Runnable)requestStoreTask);
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void requestStoreVideo(VideoSavingRequest videoSavingRequest) {
        void var3_2 = this;
        synchronized (var3_2) {
            if (videoSavingRequest == null) {
                do {
                    return;
                    break;
                } while (true);
            }
            if (!VideoStabilizer.isIntelligentActive(this.mCameraDeviceHandler.getVideoStabilizer())) {
                this.mLastVideoSavingRequest.setRequestId(this.mViewFinder.getRequestId(true));
            }
            videoSavingRequest.setDateTaken(System.currentTimeMillis());
            this.mActivity.getSavingTaskManager().storeVideo(videoSavingRequest);
            this.sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
            return;
        }
    }

    private void sendGoogleAnalyticsEvent() {
        GoogleAnalyticsUtil.setCustomDimensionLaunchedFrom(CustomDimension.LaunchedFrom.SAME_PACKAGE);
        LocalGoogleAnalyticsUtil.getInstance().sendView(this.mCurrentCapturingMode, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean startAutoFocus() {
        if (!(this.mActivity == null || this.mActivity.updateRemain() && this.mActivity.getSavingTaskManager().canPushStoreTask())) {
            return false;
        }
        this.mCameraDeviceHandler.autoFocus();
        return true;
    }

    private void startFastCapture(SurfaceHolder surfaceHolder, FastCapture fastCapture) {
        if (this.mCameraDeviceHandler.isImmediateReleaseRequested()) {
            CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] request to release camera device.");
            super.moveToCameraNotAvailable();
            return;
        }
        if (!this.mCameraDeviceHandler.waitForCameraInitialization()) {
            if (this.mActivity.getCameraDevice().getCameraDeviceStatus() == 0) {
                this.mActivity.prepareCameraDeviceHandler((Context)this.mActivity, fastCapture, this.getTargetCameraId());
                if (!this.mCameraDeviceHandler.waitForCameraInitialization()) {
                    CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] Device open failed.");
                    super.moveToCameraNotAvailable();
                    return;
                }
            } else {
                CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] Device open has started but failed.");
                super.moveToCameraNotAvailable();
                return;
            }
        }
        this.mCameraDeviceHandler.startLiveViewFinder(surfaceHolder);
        Rect rect = this.mCameraDeviceHandler.getPreviewRect();
        if (rect == null) {
            this.mActivity.showCameraNotAvailableError();
            super.changeTo(new StateWarning(), new Object[0]);
            return;
        }
        Rect rect2 = LayoutDependencyResolver.getSurfaceRect((Activity)this.mActivity, (float)rect.width() / (float)rect.height());
        PositionConverter.getInstance().init(false, 0, rect2, rect);
        PositionConverter.getInstance().setSurfaceSize(rect2.width(), rect2.height());
        PositionConverter.getInstance().setPreviewSize(rect.width(), rect.height());
        this.mActivity.requestPostLazyInitializationTaskExecute();
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$values$FastCapture[fastCapture.ordinal()]) {
            default: {
                return;
            }
            case 1: 
            case 2: {
                super.changeTo(new StateStandby((StateMachine)this, true), new Object[0]);
                return;
            }
            case 3: {
                super.changeTo(new StateStandby((StateMachine)this), new Object[0]);
                super.doFastestCapture();
                return;
            }
            case 4: {
                super.changeTo(new StateStandby((StateMachine)this, true), new Object[0]);
                super.doFastestCamcord();
                return;
            }
            case 5: 
        }
        throw new IllegalArgumentException("StateMachine.Resume:[FastCapture OFF]");
    }

    private void stopPlaySound() {
        this.mActivity.stopPlayingSound();
    }

    private void storePicture(PhotoSavingRequest photoSavingRequest) {
        this.mActivity.disableAutoOffTimer();
        this.mActivity.getSavingTaskManager().storePicture(photoSavingRequest);
    }

    private void switchCamera() {
        this.mCameraDeviceHandler.releaseCameraInstance();
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[this.mCurrentCapturingMode.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                this.sendGoogleAnalyticsEvent();
                this.changeToFrontCameraMode();
                return;
            }
            case 2: 
            case 3: 
        }
        this.sendGoogleAnalyticsEvent();
        this.changeToBackCameraMode();
    }

    private void updateDateTaken(SavingRequest savingRequest) {
        savingRequest.setDateTaken(System.currentTimeMillis());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateRecordingProgress(int n) {
        if (!(this.mViewFinder != null && this.mViewFinder.isSetupHeadupDisplayInvoked())) {
            return;
        }
        if (this.mActivity != null) {
            this.mActivity.disableAutoOffTimer();
            if (this.mUpdateProgressCount == 0) {
                super.calculateRemainStorage(true, false);
            }
        }
        this.mUpdateProgressCount = this.mUpdateProgressCount >= 10 ? 0 : 1 + this.mUpdateProgressCount;
        BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
        BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_RECORDING_PROGRESS;
        Object[] arrobject = new Object[]{n};
        baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
    }

    public void addOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.add((Object)onStateChangedListener);
    }

    public boolean canApplicationBeFinished() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            boolean bl = this.mCurrentState.getCaptureState().canApplicationBeFinished();
            return bl;
        }
    }

    public boolean canCurrentStateHandleAsynchronizedTask() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            boolean bl = this.mCurrentState.getCaptureState().canHandleAsynchronizedTask();
            return bl;
        }
    }

    public boolean canHandleWearableCaptureRequest() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            boolean bl = this.mCurrentState.getCaptureState().canHandleWearableCaptureRequest();
            return bl;
        }
    }

    public boolean canSwitchPhotoVideoMode() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            boolean bl = this.mCurrentState.getCaptureState().canSwitchPhotoVideoMode();
            return bl;
        }
    }

    public int getCurrentCameraId() {
        return this.getCameraId(this.mCurrentCapturingMode);
    }

    public Camera.Parameters getCurrentCameraParameters(boolean bl) {
        if (bl) {
            this.mCameraDeviceHandler.requestCacheParameters();
        }
        return this.mCameraDeviceHandler.getLatestCachedParameters();
    }

    public CapturingMode getCurrentCapturingMode() {
        return this.mCurrentCapturingMode;
    }

    public final int getSensorOrientation() {
        if (this.mActivity.getLastDetectedOrientation() == BaseActivity.LayoutOrientation.Portrait) {
            return 1;
        }
        return 2;
    }

    public int getTargetCameraId() {
        return this.getCameraId(this.mTargetCapturingMode);
    }

    public CapturingMode getTargetCapturingMode() {
        return this.mTargetCapturingMode;
    }

    public boolean isInModeLessRecording() {
        return this.mIsInModeLessRecording;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isInRecordingStartingState() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            CaptureState captureState = this.mCurrentState.getCaptureState();
            CaptureState captureState2 = CaptureState.STATE_VIDEO_RECORDING_STARTING;
            if (captureState != captureState2) return false;
            return true;
        }
    }

    public boolean isMenuAvailable() {
        return this.mCurrentState.isMenuAvailable();
    }

    public boolean isRecording() {
        if (this.mCurrentState != null) {
            return this.mCurrentState.isRecording();
        }
        return false;
    }

    public boolean isStorageFull() {
        if (Math.max((long)0, (long)this.mActivity.getStorageController().getAvailableStorageSize()) <= 61440) {
            return true;
        }
        return false;
    }

    public void onAutoFocusDone(boolean bl) {
        TransitterEvent transitterEvent = TransitterEvent.EVENT_ON_AUTO_FOCUS_DONE;
        Object[] arrobject = new Object[]{bl};
        this.sendEvent(transitterEvent, arrobject);
    }

    @Override
    public void onAvailableSizeUpdated(long l) {
    }

    public void onBurstShootingDone() {
        this.mViewFinder.onCaptureDone();
        if (this.mBurstPictureCount > 1) {
            this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, new Object[0]);
        }
        if (this.mBurstCoverResult != null) {
            BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
            Object[] arrobject = new Object[]{this.mBurstCoverResult.savingRequest.getRequestId()};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
        }
    }

    public void onBurstStoreComplete(boolean bl) {
        if (this.mContentsViewController != null) {
            this.mContentsViewController.addContent(this.mBurstCoverResult.savingRequest.getRequestId(), this.mBurstCoverResult.uri);
            if (bl && !this.mActivity.isOneShotPhoto()) {
                MediaProviderUpdator.sendBroadcastCameraShot((Context)this.mActivity, this.mBurstCoverResult.uri, true);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    @Override
    public void onDestinationToSaveChanged() {
        StateMachine stateMachine = this;
        synchronized (stateMachine) {
            if (this.mActivity.getStorageManager().isReady()) {
                this.sendEvent(TransitterEvent.EVENT_STORAGE_MOUNTED, new Object[0]);
            } else {
                this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
            }
            return;
        }
    }

    public void onDeviceError(ErrorCode errorCode, Exception exception) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$StateMachine$ErrorCode[errorCode.ordinal()]) {
            default: {
                return;
            }
            case 1: 
        }
        if (((PowerManager)this.mActivity.getSystemService("power")).isScreenOn()) {
            CameraLogger.e(TAG, "onDeviceError(): [Screen backlight is ON.");
            this.mActivity.showCameraNotAvailableError();
            return;
        }
        this.mActivity.finish();
    }

    public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
        this.sendStaticEvent(StaticEvent.EVENT_ON_FACE_DETECTED, new Object[]{faceDetectionResult});
    }

    public void onInitialAutoFocusDone(boolean bl) {
        TransitterEvent transitterEvent = TransitterEvent.EVENT_ON_INITIAL_AUTO_FOCUS_DONE;
        Object[] arrobject = new Object[]{bl};
        this.sendEvent(transitterEvent, arrobject);
    }

    public void onNotifyThermalStatus(boolean bl) {
        if (bl) {
            this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_WARNING, new Object[0]);
            return;
        }
        this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_NOTIFY_THERMAL_NORMAL, new Object[0]);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onObjectTracked(CameraExtension.ObjectTrackingResult objectTrackingResult) {
        if (objectTrackingResult.mIsLost) {
            this.mHandler.removeCallbacks((Runnable)this.mTrackedObjectLostTimeoutTask);
            this.mHandler.postDelayed((Runnable)this.mTrackedObjectLostTimeoutTask, 3000);
        } else {
            this.mHandler.removeCallbacks((Runnable)this.mTrackedObjectLostTimeoutTask);
        }
        this.sendStaticEvent(StaticEvent.EVENT_ON_OBJECT_TRACKED, new Object[]{objectTrackingResult});
    }

    public void onPreShutterDone(PhotoSavingRequest photoSavingRequest) {
        super.checkCallback(photoSavingRequest);
        this.sendEvent(TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, photoSavingRequest);
    }

    public void onPreTakePictureDone(byte[] arrby, PhotoSavingRequest photoSavingRequest) {
        super.checkCallback(photoSavingRequest);
        photoSavingRequest.setImageData(arrby);
        this.sendEvent(TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, photoSavingRequest);
    }

    public void onSceneModeChanged(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
        this.sendStaticEvent(StaticEvent.EVENT_ON_SCENE_MODE_CHANGED, new Object[]{sceneRecognitionResult});
    }

    public void onShutterDone(PhotoSavingRequest photoSavingRequest) {
        this.sendEvent(TransitterEvent.EVENT_ON_SHUTTER_DONE, photoSavingRequest);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    @Override
    public void onStorageStateChanged(String string) {
        void var3_2 = this;
        synchronized (var3_2) {
            if (this.mActivity != null) {
                if (!this.mActivity.getStorageManager().isReady()) {
                    this.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
                    this.mActivity.disableAutoOffTimer();
                } else {
                    this.sendEvent(TransitterEvent.EVENT_STORAGE_MOUNTED, new Object[0]);
                }
            }
            return;
        }
    }

    @Override
    public void onStoreComplete(final StoreDataResult storeDataResult) {
        if (MediaSavingUtil.isCoverPicture(storeDataResult.savingRequest.getSomcType())) {
            this.storeCoverResult(storeDataResult);
            this.mHandler.post((Runnable)new Runnable(){

                public void run() {
                    StateMachine stateMachine = StateMachine.this;
                    TransitterEvent transitterEvent = TransitterEvent.EVENT_ON_STORE_COMPLETED;
                    Object[] arrobject = new Object[]{storeDataResult};
                    stateMachine.sendEvent(transitterEvent, arrobject);
                }
            });
            return;
        }
        if (this.mActivity.isOneShotPhotoSecure()) {
            this.mActivity.finishSecureOneShot(storeDataResult);
            return;
        }
        this.sendEvent(TransitterEvent.EVENT_ON_STORE_COMPLETED, storeDataResult);
    }

    public void onTakePictureDone(byte[] arrby, PhotoSavingRequest photoSavingRequest) {
        photoSavingRequest.setImageData(arrby);
        this.sendEvent(TransitterEvent.EVENT_ON_TAKE_PICTURE_DONE, photoSavingRequest);
    }

    public void onVideoRecordingDone(VideoSavingRequest videoSavingRequest) {
        this.mIsInModeLessRecording = false;
        this.sendEvent(TransitterEvent.EVENT_ON_VIDEO_RECORDING_DONE, videoSavingRequest);
    }

    public void onZoomChange(int n, boolean bl, Camera camera) {
        BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
        BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_ZOOM_CHANGED;
        Object[] arrobject = new Object[]{n};
        baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
    }

    public void releaseContentsViewController() {
        this.mContentsViewController = null;
    }

    public void removeChangeCameraModeTask() {
        this.mHandler.removeCallbacks((Runnable)this.mChangeCameraModeTask);
    }

    public void removeOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListenerSet.remove((Object)onStateChangedListener);
    }

    public void removeStartRecordingTask() {
        this.mHandler.removeCallbacks((Runnable)this.mStartRecordingTask);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public /* varargs */ void sendEvent(TransitterEvent var1, Object ... var2_2) {
        var5_3 = this;
        // MONITORENTER : var5_3
        var4_4 = .$SwitchMap$com$sonyericsson$android$camera$fastcapturing$StateMachine$TransitterEvent[var1.ordinal()];
        switch (var4_4) {
            case 1: {
                this.mCurrentState.handleInitialize(var2_2);
                ** break;
            }
            case 2: {
                this.mCurrentState.handleResume(var2_2);
                ** break;
            }
            case 3: {
                this.mCurrentState.handleResumeTimeout(var2_2);
                ** break;
            }
            case 4: {
                this.mCurrentState.handlePause(var2_2);
                ** break;
            }
            case 5: {
                this.mCurrentState.handleFinalize(var2_2);
                ** break;
            }
            case 6: {
                this.mCurrentState.handleOnEvfPrepared(var2_2);
                ** break;
            }
            case 7: {
                this.mCurrentState.handleOnEvfPreparationFailed(var2_2);
                ** break;
            }
            case 8: {
                this.mCurrentState.handleOnInitialAutoFocusDone(var2_2);
                ** break;
            }
            case 9: {
                this.mCurrentState.handleOnAutoFocusDone(var2_2);
                ** break;
            }
            case 10: {
                this.mCurrentState.handleOnPreShutterDone(var2_2);
                ** break;
            }
            case 11: {
                this.mCurrentState.handleOnShutterDone(var2_2);
                ** break;
            }
            case 12: {
                this.mCurrentState.handleOnPreTakePictureDone(var2_2);
                ** break;
            }
            case 13: {
                this.mCurrentState.handleOnTakePictureDone(var2_2);
                ** break;
            }
            case 14: {
                this.mCurrentState.handleOnVideoRecordingDone(var2_2);
                ** break;
            }
            case 15: {
                this.mCurrentState.handleTouchContentProgress();
                ** break;
            }
            case 16: {
                this.mCurrentState.handleOnStoreRequested(var2_2);
                ** break;
            }
            case 17: {
                this.mCurrentState.handleOnStoreCompleted(var2_2);
                ** break;
            }
            case 18: {
                this.mCurrentState.handleKeyFocusDown(var2_2);
                ** break;
            }
            case 19: {
                this.mCurrentState.handleKeyFocusUp(var2_2);
                ** break;
            }
            case 20: {
                this.mCurrentState.handleKeyCaptureDown(var2_2);
                ** break;
            }
            case 21: {
                this.mCurrentState.handleKeyCaptureUp(var2_2);
                ** break;
            }
            case 22: {
                this.mCurrentState.handleKeyZoomInDown(var2_2);
                ** break;
            }
            case 23: {
                this.mCurrentState.handleKeyZoomOutDown(var2_2);
                ** break;
            }
            case 24: {
                this.mCurrentState.handleKeyZoomUp(var2_2);
                ** break;
            }
            case 25: {
                this.mCurrentState.handleKeyMenu(var2_2);
                ** break;
            }
            case 26: {
                this.mCurrentState.handleKeyBack(var2_2);
                ** break;
            }
            case 27: {
                this.mCurrentState.handlePrepareTouchZoom(var2_2);
                ** break;
            }
            case 28: {
                this.mCurrentState.handleOnPrepareTouchZoomTimedOut(var2_2);
                ** break;
            }
            case 29: {
                this.mCurrentState.handleStartTouchZoom(var2_2);
                ** break;
            }
            case 30: {
                this.mCurrentState.handleStopTouchZoom(var2_2);
                ** break;
            }
            case 31: {
                this.mCurrentState.handleCancelTouchZoom(var2_2);
                ** break;
            }
            case 32: {
                this.mCurrentState.handleCaptureButtonTouch(var2_2);
                ** break;
            }
            case 33: {
                this.mCurrentState.handleCaptureButtonRelease(var2_2);
                ** break;
            }
            case 34: {
                this.mCurrentState.handleCaptureButtonCancel(var2_2);
                ** break;
            }
            case 35: {
                this.mCurrentState.handleCaptureButtonLongPress(var2_2);
                ** break;
            }
            case 36: {
                this.mCurrentState.handleScreenClear(var2_2);
                ** break;
            }
            case 37: {
                this.mCurrentState.handleStartAfSearchInTouch(var2_2);
                ** break;
            }
            case 38: {
                this.mCurrentState.handleStartAfSearchInTouchStop(var2_2);
                ** break;
            }
            case 39: {
                this.mCurrentState.handleDialogOpened(var2_2);
                ** break;
            }
            case 40: {
                this.mCurrentState.handleDialogClosed(var2_2);
                ** break;
            }
            case 41: {
                this.mCurrentState.handleStorageError(var2_2);
                ** break;
            }
            case 42: {
                this.mCurrentState.handleStorageMounted(var2_2);
                ** break;
            }
            case 43: {
                this.mCurrentState.handleStorageShouldChange(var2_2);
                ** break;
            }
            case 44: {
                this.mCurrentState.handleSetFocusPosition(var2_2);
                ** break;
            }
            case 45: {
                this.mCurrentState.handleRequestSetupHeadUpDisplay(var2_2);
                ** break;
            }
            case 46: {
                this.mCurrentState.handleOnRecordingStartWaitDone(var2_2);
                ** break;
            }
            case 47: {
                this.mCurrentState.handleOnRecordingError(var2_2);
                ** break;
            }
            case 48: {
                this.mCurrentState.handleCamcordButtonRelease(var2_2);
                ** break;
            }
            case 49: {
                this.mCurrentState.handleSubCamcordButtonTouch(var2_2);
                ** break;
            }
            case 50: {
                this.mCurrentState.handleSubCamcordButtonRelease(var2_2);
                ** break;
            }
            case 51: {
                this.mCurrentState.handleSubCamcordButtonCancel(var2_2);
                ** break;
            }
            case 52: {
                this.mCurrentState.handleChangeSelectedFace(var2_2);
                ** break;
            }
            case 53: {
                this.mCurrentState.handleSetSelectedObjectPosition(var2_2);
                ** break;
            }
            case 54: {
                this.mCurrentState.handleDeselectObjectPosition(var2_2);
                ** break;
            }
            case 55: {
                this.mCurrentState.handleStartAfAfterObjectTracked(var2_2);
                ** break;
            }
            case 56: {
                this.mCurrentState.handleOnBurstShutterDone(var2_2);
                ** break;
            }
            case 57: {
                this.mCurrentState.handleOnBurstStoreCompleted(var2_2);
                ** break;
            }
            case 58: {
                this.mCurrentState.handleOnBurstGroupStoreCompleted(var2_2);
                ** break;
            }
            case 59: {
                this.mCurrentState.handleOnOnePreviewFrameUpdated(var2_2);
                ** break;
            }
            case 60: {
                this.mCurrentState.handleOnContinuousPreviewFrameUpdated(var2_2);
            }
lbl184: // 61 sources:
            default: {
                // MONITOREXIT : var5_3
                return;
            }
            case 61: 
        }
        this.mCurrentState.handleSwitchCamera(var2_2);
    }

    /*
     * Exception decompiling
     */
    public /* varargs */ void sendStaticEvent(StaticEvent var1, Object ... var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:436)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:62)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:400)
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

    public void sendVideoChapterThumbnailToViewFinder() {
        if (this.mChapterThumbnail != null && this.mViewFinder.isHeadUpDesplayReady()) {
            if (this.mLastVideoSavingRequest != null) {
                this.mChapterThumbnail.setOrientation(this.mLastVideoSavingRequest.common.orientation);
            }
            BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_ADD_VIDEO_CHAPTER;
            Object[] arrobject = new Object[]{this.mChapterThumbnail};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            this.mChapterThumbnail = null;
        }
    }

    public void setCameraDeviceHandler(CameraDeviceHandler cameraDeviceHandler) {
        this.mCameraDeviceHandler = cameraDeviceHandler;
    }

    public void setCurrentCapturingMode(CapturingMode capturingMode) {
        this.mCurrentCapturingMode = capturingMode;
    }

    public void setTargetCapturingMode(CapturingMode capturingMode) {
        this.mTargetCapturingMode = capturingMode;
    }

    public void setViewFinder(BaseFastViewFinder baseFastViewFinder) {
        this.mViewFinder = baseFastViewFinder;
    }

    public void showStorageErrorDialogForce() {
        if (this.mActivity != null) {
            if (this.mActivity.getStorageManager() != null) {
                return;
            }
            CameraLogger.w(TAG, "mActivity.getStorageManager() is null.");
            return;
        }
        CameraLogger.w(TAG, "mActivity is null.");
    }

    public boolean storeCoverResult(StoreDataResult storeDataResult) {
        if (MediaSavingUtil.isCoverPicture(storeDataResult.savingRequest.getSomcType())) {
            this.mBurstCoverResult = storeDataResult;
            BaseFastViewFinder baseFastViewFinder = this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SET_EARLY_THUMBNAIL;
            Object[] arrobject = new Object[]{ThumbnailUtil.createThumbnailViewFromUri((Activity)this.mActivity, storeDataResult.uri, storeDataResult.savingRequest.common.orientation)};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            return true;
        }
        return false;
    }

    protected void switchStorage() {
        if (this.mActivity != null) {
            if (this.mActivity.getCommonSettings().get(CommonSettingKey.SAVE_DESTINATION) == SaveDestination.SDCARD) {
                this.mActivity.getCommonSettings().set(SaveDestination.EMMC);
                this.mActivity.getStorageManager().setCurrentStorage(StorageManager.StorageType.INTERNAL);
            }
        } else {
            return;
        }
        this.mActivity.getCommonSettings().set(SaveDestination.SDCARD);
        this.mActivity.getStorageManager().setCurrentStorage(StorageManager.StorageType.EXTERNAL_CARD);
    }

    public boolean tryToSetCameraParameters(Camera.Parameters parameters) {
        return this.mCameraDeviceHandler.trySetParametersToDevice(parameters);
    }

    public static final class CaptureState
    extends Enum<CaptureState> {
        private static final /* synthetic */ CaptureState[] $VALUES;
        public static final /* enum */ CaptureState STATE_CAMERA_SWITCHING;
        public static final /* enum */ CaptureState STATE_FINALIZE;
        public static final /* enum */ CaptureState STATE_INITIALIZE;
        public static final /* enum */ CaptureState STATE_NONE;
        public static final /* enum */ CaptureState STATE_PAUSE;
        public static final /* enum */ CaptureState STATE_PHOTO_AF_DONE;
        public static final /* enum */ CaptureState STATE_PHOTO_AF_DONE_IN_TOUCH;
        public static final /* enum */ CaptureState STATE_PHOTO_AF_SEARCH;
        public static final /* enum */ CaptureState STATE_PHOTO_AF_SEARCH_IN_TOUCH;
        public static final /* enum */ CaptureState STATE_PHOTO_AF_SEARCH_IN_TOUCH_DRAGGING_FOCUS_POSITION;
        public static final /* enum */ CaptureState STATE_PHOTO_BASE;
        public static final /* enum */ CaptureState STATE_PHOTO_CAPTURE;
        public static final /* enum */ CaptureState STATE_PHOTO_CAPTURE_BEST_EFFORT_BURST;
        public static final /* enum */ CaptureState STATE_PHOTO_CAPTURE_HI_SPEED_BURST;
        public static final /* enum */ CaptureState STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE;
        public static final /* enum */ CaptureState STATE_PHOTO_CAPTURE_WAIT_FOR_NEXT_CAPTURE;
        public static final /* enum */ CaptureState STATE_PHOTO_READY_FOR_RECORDING;
        public static final /* enum */ CaptureState STATE_PHOTO_SELFTIMER_COUNTDOWN;
        public static final /* enum */ CaptureState STATE_PHOTO_STORE;
        public static final /* enum */ CaptureState STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START;
        public static final /* enum */ CaptureState STATE_PHOTO_ZOOMING;
        public static final /* enum */ CaptureState STATE_PHOTO_ZOOMING_BASE;
        public static final /* enum */ CaptureState STATE_PHOTO_ZOOMING_IN_TOUCH;
        public static final /* enum */ CaptureState STATE_RESUME;
        public static final /* enum */ CaptureState STATE_STANDBY;
        public static final /* enum */ CaptureState STATE_STANDBY_DIALOG;
        public static final /* enum */ CaptureState STATE_VIDEO_BASE;
        public static final /* enum */ CaptureState STATE_VIDEO_CAPTURE_WHILE_RECORDING;
        public static final /* enum */ CaptureState STATE_VIDEO_RECORDING;
        public static final /* enum */ CaptureState STATE_VIDEO_RECORDING_PAUSING;
        public static final /* enum */ CaptureState STATE_VIDEO_RECORDING_STARTING;
        public static final /* enum */ CaptureState STATE_VIDEO_STORE;
        public static final /* enum */ CaptureState STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING;
        public static final /* enum */ CaptureState STATE_VIDEO_ZOOMING_BASE;
        public static final /* enum */ CaptureState STATE_VIDEO_ZOOMING_IN_TOUCH_WHILE_RECORDING;
        public static final /* enum */ CaptureState STATE_VIDEO_ZOOMING_WHILE_RECORDING;
        public static final /* enum */ CaptureState STATE_WARNING;
        final boolean mCanApplicationBeFinished;
        final boolean mCanHandleAsynchronizedTask;
        final boolean mCanHandleWearableCaptureRequest;
        final boolean mCanSwitchPhotoVideoMode;

        static {
            STATE_NONE = new CaptureState(false, false, false, false);
            STATE_INITIALIZE = new CaptureState(false, false, false, false);
            STATE_RESUME = new CaptureState(false, false, false, false);
            STATE_CAMERA_SWITCHING = new CaptureState(false, false, false, false);
            STATE_PHOTO_BASE = new CaptureState(false, false, false, false);
            STATE_PHOTO_ZOOMING_BASE = new CaptureState(false, false, false, false);
            STATE_STANDBY = new CaptureState(true, true, true, true);
            STATE_PHOTO_READY_FOR_RECORDING = new CaptureState(false, false, false, false);
            STATE_PHOTO_SELFTIMER_COUNTDOWN = new CaptureState(true, false, false, true);
            STATE_PHOTO_ZOOMING = new CaptureState(false, true, false, false);
            STATE_PHOTO_ZOOMING_IN_TOUCH = new CaptureState(false, true, false, false);
            STATE_STANDBY_DIALOG = new CaptureState(true, true, false, false);
            STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START = new CaptureState(false, false, false, false);
            STATE_PHOTO_AF_SEARCH = new CaptureState(false, false, false, false);
            STATE_PHOTO_AF_SEARCH_IN_TOUCH = new CaptureState(false, false, false, false);
            STATE_PHOTO_AF_SEARCH_IN_TOUCH_DRAGGING_FOCUS_POSITION = new CaptureState(false, false, false, false);
            STATE_PHOTO_AF_DONE = new CaptureState(false, false, false, false);
            STATE_PHOTO_AF_DONE_IN_TOUCH = new CaptureState(false, false, false, false);
            STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE = new CaptureState(false, false, false, false);
            STATE_PHOTO_CAPTURE = new CaptureState(false, false, false, false);
            STATE_PHOTO_CAPTURE_WAIT_FOR_NEXT_CAPTURE = new CaptureState(false, false, false, false);
            STATE_PHOTO_CAPTURE_BEST_EFFORT_BURST = new CaptureState(false, false, false, false);
            STATE_PHOTO_CAPTURE_HI_SPEED_BURST = new CaptureState(false, false, false, false);
            STATE_PHOTO_STORE = new CaptureState(false, false, false, false);
            STATE_VIDEO_BASE = new CaptureState(false, false, false, false);
            STATE_VIDEO_ZOOMING_BASE = new CaptureState(false, false, false, false);
            STATE_VIDEO_RECORDING_STARTING = new CaptureState(false, false, false, false);
            STATE_VIDEO_RECORDING = new CaptureState(true, false, false, false);
            STATE_VIDEO_CAPTURE_WHILE_RECORDING = new CaptureState(false, false, false, false);
            STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING = new CaptureState(false, false, false, false);
            STATE_VIDEO_ZOOMING_WHILE_RECORDING = new CaptureState(false, false, false, false);
            STATE_VIDEO_ZOOMING_IN_TOUCH_WHILE_RECORDING = new CaptureState(false, false, false, false);
            STATE_VIDEO_STORE = new CaptureState(false, false, false, false);
            STATE_PAUSE = new CaptureState(false, true, false, false);
            STATE_WARNING = new CaptureState(true, true, true, false);
            STATE_FINALIZE = new CaptureState(false, false, false, false);
            STATE_VIDEO_RECORDING_PAUSING = new CaptureState(true, false, false, false);
            CaptureState[] arrcaptureState = new CaptureState[]{STATE_NONE, STATE_INITIALIZE, STATE_RESUME, STATE_CAMERA_SWITCHING, STATE_PHOTO_BASE, STATE_PHOTO_ZOOMING_BASE, STATE_STANDBY, STATE_PHOTO_READY_FOR_RECORDING, STATE_PHOTO_SELFTIMER_COUNTDOWN, STATE_PHOTO_ZOOMING, STATE_PHOTO_ZOOMING_IN_TOUCH, STATE_STANDBY_DIALOG, STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START, STATE_PHOTO_AF_SEARCH, STATE_PHOTO_AF_SEARCH_IN_TOUCH, STATE_PHOTO_AF_SEARCH_IN_TOUCH_DRAGGING_FOCUS_POSITION, STATE_PHOTO_AF_DONE, STATE_PHOTO_AF_DONE_IN_TOUCH, STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE, STATE_PHOTO_CAPTURE, STATE_PHOTO_CAPTURE_WAIT_FOR_NEXT_CAPTURE, STATE_PHOTO_CAPTURE_BEST_EFFORT_BURST, STATE_PHOTO_CAPTURE_HI_SPEED_BURST, STATE_PHOTO_STORE, STATE_VIDEO_BASE, STATE_VIDEO_ZOOMING_BASE, STATE_VIDEO_RECORDING_STARTING, STATE_VIDEO_RECORDING, STATE_VIDEO_CAPTURE_WHILE_RECORDING, STATE_VIDEO_STORE_PHOTO_WHILE_RECORDING, STATE_VIDEO_ZOOMING_WHILE_RECORDING, STATE_VIDEO_ZOOMING_IN_TOUCH_WHILE_RECORDING, STATE_VIDEO_STORE, STATE_PAUSE, STATE_WARNING, STATE_FINALIZE, STATE_VIDEO_RECORDING_PAUSING};
            $VALUES = arrcaptureState;
        }

        private CaptureState(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
            super(string, n);
            this.mCanHandleAsynchronizedTask = bl;
            this.mCanApplicationBeFinished = bl2;
            this.mCanSwitchPhotoVideoMode = bl3;
            this.mCanHandleWearableCaptureRequest = bl4;
        }

        public static CaptureState valueOf(String string) {
            return (CaptureState)Enum.valueOf((Class)CaptureState.class, (String)string);
        }

        public static CaptureState[] values() {
            return (CaptureState[])$VALUES.clone();
        }

        public boolean canApplicationBeFinished() {
            return this.mCanApplicationBeFinished;
        }

        public boolean canHandleAsynchronizedTask() {
            return this.mCanHandleAsynchronizedTask;
        }

        public boolean canHandleWearableCaptureRequest() {
            return this.mCanHandleWearableCaptureRequest;
        }

        public boolean canSwitchPhotoVideoMode() {
            return this.mCanSwitchPhotoVideoMode;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class ChangeCameraModeTask
    implements Runnable {
        private final boolean mIsChangeToStandbyRequired;
        private final boolean mIsModelessRecording;
        private final int mRequestCameraType;

        public ChangeCameraModeTask(int n, boolean bl, boolean bl2) {
            this.mRequestCameraType = n;
            this.mIsChangeToStandbyRequired = bl;
            this.mIsModelessRecording = bl2;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            if (StateMachine.this.mCameraDeviceHandler == null) return;
            if (StateMachine.this.mViewFinder == null) {
                return;
            }
            if (StateMachine.this.mCameraDeviceHandler.changeCameraModeTo(this.mRequestCameraType, this.mIsModelessRecording)) {
                StateMachine.this.doDeselectObject();
            }
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_RESIZE_EVF_SCOPE, new Object[0]);
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_CAMERA_MODE_CHANGED_TO;
            Object[] arrobject = new Object[]{this.mRequestCameraType};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            if (this.mIsChangeToStandbyRequired) {
                switch (this.mRequestCameraType) {
                    case 1: {
                        StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
                        StateMachine.this.mActivity.updateLaunchMode(FastCapture.LAUNCH_ONLY);
                        break;
                    }
                    case 2: {
                        StateMachine.this.changeTo(new StateStandby(StateMachine.this), new Object[0]);
                        StateMachine.this.mActivity.updateLaunchMode(FastCapture.VIDEO_LAUNCH_ONLY);
                    }
                }
            }
            if (!StateMachine.this.mCameraDeviceHandler.willPreviewBeRestarted()) return;
             var4_4 = new Runnable(){

                public void run() {
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_SURFACE, new Object[0]);
                }
            };
            if (StateMachine.this.mCameraDeviceHandler.getVideoSize().isConstraint()) {
                StateMachine.this.mHandler.postDelayed((Runnable)var4_4, 1500);
                return;
            }
            StateMachine.this.mHandler.postDelayed((Runnable)var4_4, 30);
        }

    }

    /*
     * Failed to analyse overrides
     */
    private class DoFastestCamcordTask
    implements Runnable {
        final /* synthetic */ StateMachine this$0;

        private DoFastestCamcordTask(StateMachine stateMachine) {
            this.this$0 = stateMachine;
        }

        /* synthetic */ DoFastestCamcordTask(StateMachine stateMachine, com.sonyericsson.android.camera.fastcapturing.StateMachine$1 var2_2) {
            super(stateMachine);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void run() {
            if (!this.this$0.mActivity.getStorageManager().isReady()) {
                this.this$0.mHandler.post((Runnable)new Runnable(){

                    public void run() {
                        DoFastestCamcordTask.this.this$0.changeTo(new StateWarning(), new Object[0]);
                    }
                });
                return;
            }
            if (this.this$0.mLastVideoSavingRequest == null) return;
            try {
                if (!this.this$0.isInRecordingStartingState()) return;
                this.this$0.mCameraDeviceHandler.startRecording(this.this$0.mLastVideoSavingRequest, this.this$0.mRecorderListener, this.this$0.mSoundPlayer);
                this.this$0.updateDateTaken(this.this$0.mLastVideoSavingRequest);
                return;
            }
            catch (RuntimeException var1_1) {
                this.this$0.mHandler.post((Runnable)new Runnable(){

                    public void run() {
                        if (AudioResourceChecker.isAudioResourceAvailable((BaseActivity)DoFastestCamcordTask.this.this$0.mActivity)) {
                            DoFastestCamcordTask.this.this$0.mActivity.getMessagePopup().showErrorUncancelable(2131361870, 2131361905, false);
                            DoFastestCamcordTask.this.this$0.changeTo(new StateWarning(), new Object[0]);
                            GoogleAnalyticsUtil.sendCameraNotAvailableEvent();
                            return;
                        }
                        DoFastestCamcordTask.this.this$0.mActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
                        DoFastestCamcordTask.this.this$0.changeTo(new StateStandby(DoFastestCamcordTask.this.this$0), new Object[0]);
                    }
                });
                return;
            }
        }

    }

    public static final class ErrorCode
    extends Enum<ErrorCode> {
        private static final /* synthetic */ ErrorCode[] $VALUES;
        public static final /* enum */ ErrorCode ERROR_ON_START_PREVIEW = new ErrorCode();

        static {
            ErrorCode[] arrerrorCode = new ErrorCode[]{ERROR_ON_START_PREVIEW};
            $VALUES = arrerrorCode;
        }

        private ErrorCode() {
            super(string, n);
        }

        public static ErrorCode valueOf(String string) {
            return (ErrorCode)Enum.valueOf((Class)ErrorCode.class, (String)string);
        }

        public static ErrorCode[] values() {
            return (ErrorCode[])$VALUES.clone();
        }
    }

    private class FaceIdCallback
    implements FaceIdentification.FaceIdentificationCallback {
        final /* synthetic */ StateMachine this$0;

        private FaceIdCallback(StateMachine stateMachine) {
            this.this$0 = stateMachine;
        }

        /* synthetic */ FaceIdCallback(StateMachine stateMachine,  var2_2) {
            super(stateMachine);
        }

        @Override
        public void onFaceIdentified(List<FaceIdentification.FaceIdentificationResult> list) {
            this.this$0.sendStaticEvent(StaticEvent.EVENT_ON_FACE_IDENTEFIED, new Object[]{list});
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class NotifyDelayedEventTask
    implements Runnable {
        private final Object[] mArgs;
        private final TransitterEvent mEvent;
        final /* synthetic */ StateMachine this$0;

        private NotifyDelayedEventTask(StateMachine stateMachine, TransitterEvent transitterEvent, Object[] arrobject) {
            this.this$0 = stateMachine;
            this.mEvent = transitterEvent;
            this.mArgs = arrobject;
        }

        /* synthetic */ NotifyDelayedEventTask(StateMachine stateMachine, TransitterEvent transitterEvent, Object[] arrobject,  var4) {
            super(stateMachine, transitterEvent, arrobject);
        }

        public void run() {
            this.this$0.sendEvent(this.mEvent, this.mArgs);
        }
    }

    public static interface OnStateChangedListener {
        public /* varargs */ void onStateChanged(CaptureState var1, Object ... var2);
    }

    /*
     * Failed to analyse overrides
     */
    private class RequestStoreTask
    implements Runnable {
        private final PhotoSavingRequest mRequest;

        public RequestStoreTask(PhotoSavingRequest photoSavingRequest) {
            this.mRequest = photoSavingRequest;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void run() {
            StateMachine stateMachine;
            StateMachine stateMachine2 = stateMachine = StateMachine.this;
            synchronized (stateMachine2) {
                if (StateMachine.this.mContentsViewController != null) {
                    StateMachine.this.storePicture(this.mRequest);
                } else if (StateMachine.this.mActivity.isDeviceInSecurityLock()) {
                    StateMachine.this.mPhotoSavingRequestList.add((Object)this.mRequest);
                } else {
                    this.mRequest.setRequestId(-1);
                    StateMachine.this.storePicture(this.mRequest);
                }
            }
            StateMachine.this.mHandler.post((Runnable)new Runnable(){

                public void run() {
                    StateMachine.this.calculateRemainStorage(false, false);
                    StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_STORE_REQUESTED, new Object[0]);
                }
            });
        }

    }

    /*
     * Failed to analyse overrides
     */
    private class StartRecordingTask
    implements Runnable {
        final /* synthetic */ StateMachine this$0;

        private StartRecordingTask(StateMachine stateMachine) {
            this.this$0 = stateMachine;
        }

        /* synthetic */ StartRecordingTask(StateMachine stateMachine,  var2_2) {
            super(stateMachine);
        }

        public void run() {
            this.this$0.doStartRecording(new Object[0]);
        }
    }

    class State {
        protected CaptureState mCaptureState;

        public State() {
            this.mCaptureState = CaptureState.STATE_NONE;
        }

        public void entry() {
        }

        public void exit() {
        }

        public CaptureState getCaptureState() {
            return this.mCaptureState;
        }

        public /* varargs */ void handleCamcordButtonRelease(Object ... arrobject) {
        }

        public /* varargs */ void handleCancelTouchZoom(Object ... arrobject) {
        }

        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
        }

        public /* varargs */ void handleCaptureButtonLongPress(Object ... arrobject) {
        }

        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
        }

        public /* varargs */ void handleCaptureButtonTouch(Object ... arrobject) {
        }

        public /* varargs */ void handleChangeSelectedFace(Object ... arrobject) {
        }

        public /* varargs */ void handleDeselectObjectPosition(Object ... arrobject) {
        }

        public /* varargs */ void handleDialogClosed(Object ... arrobject) {
        }

        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
        }

        public /* varargs */ void handleFinalize(Object ... arrobject) {
        }

        public /* varargs */ void handleInitialize(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyBack(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyCaptureUp(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyFocusDown(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyFocusUp(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyMenu(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyZoomInDown(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyZoomOutDown(Object ... arrobject) {
        }

        public /* varargs */ void handleKeyZoomUp(Object ... arrobject) {
        }

        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnBurstGroupStoreCompleted(Object ... arrobject) {
            StateMachine.this.onBurstStoreComplete(true);
        }

        public /* varargs */ void handleOnBurstShutterDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnBurstStoreCompleted(Object ... arrobject) {
        }

        public /* varargs */ void handleOnContinuousPreviewFrameUpdated(Object ... arrobject) {
        }

        public /* varargs */ void handleOnEvfPreparationFailed(Object ... arrobject) {
        }

        public /* varargs */ void handleOnEvfPrepared(Object ... arrobject) {
        }

        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
        }

        public /* varargs */ void handleOnFaceIdentified(Object ... arrobject) {
        }

        public /* varargs */ void handleOnInitialAutoFocusDone(Object ... arrobject) {
            CameraLogger.e("FastCapture", "ERROR:PRE-SCAN Event is not handled correctly. Check sequence.");
            StateMachine.this.cancelAutoFocus(false);
        }

        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
        }

        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
        }

        public /* varargs */ void handleOnOrientationChanged(Object ... arrobject) {
        }

        public /* varargs */ void handleOnPhotoStackInitialized(Object ... arrobject) {
        }

        public /* varargs */ void handleOnPreShutterDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnPreTakePictureDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnPrepareTouchZoomTimedOut(Object ... arrobject) {
        }

        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
        }

        public /* varargs */ void handleOnRecordingStartWaitDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnSceneModeChanged(Object ... arrobject) {
        }

        public /* varargs */ void handleOnShutterDone(Object ... arrobject) {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public /* varargs */ void handleOnStoreCompleted(Object ... var1) {
            var12_2 = this;
            // MONITORENTER : var12_2
            var3_3 = StateMachine.this.mViewFinder;
            ** if (var3_3 == null) goto lbl-1000
lbl5: // 1 sources:
            var4_4 = (StoreDataResult)var1[0];
            if (var4_4.savingRequest.common.savedFileType == SavingTaskManager.SavedFileType.BURST) {
                if (StateMachine.this.mCameraDeviceHandler.getBurstShotSetting() == BurstShot.BEST_EFFORT && StateMachine.access$100(StateMachine.this)) {
                    StateMachine.this.onBurstStoreComplete(false);
                }
                if (!MediaSavingUtil.isCoverPicture(var4_4.savingRequest.getSomcType()) || !StateMachine.access$100(StateMachine.this)) return;
                {
                    if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.OFF) {
                        var9_5 = StateMachine.this.mViewFinder;
                        var10_6 = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
                        var11_7 = new Object[]{var4_4.savingRequest.getRequestId()};
                        var9_5.sendViewUpdateEvent(var10_6, var11_7);
                        return;
                    }
                    if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.EDIT) {
                        if (StateMachine.access$200(StateMachine.this) != 1) return;
                        {
                            StateMachine.access$300(StateMachine.this, var4_4);
                            return;
                        }
                    } else {
                        if (StateMachine.access$200(StateMachine.this) != 1) return;
                        {
                            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_AUTO_REVIEW, new Object[]{var4_4});
                            return;
                        }
                    }
                }
            }
            StateMachine.access$400(StateMachine.this, var4_4);
            StateMachine.this.mViewFinder.ensureVideoAutoReviewSettingHasLoaded();
            switch (.$SwitchMap$com$sonyericsson$cameracommon$mediasaving$SavingTaskManager$SavedFileType[var4_4.savingRequest.common.savedFileType.ordinal()]) {
                case 1: {
                    if (StateMachine.this.mViewFinder.getVideoAutoReviewSetting() == VideoAutoReview.EDIT && this.mCaptureState != CaptureState.STATE_PAUSE && this.mCaptureState != CaptureState.STATE_RESUME) {
                        StateMachine.access$300(StateMachine.this, var4_4);
                        ** break;
                    }
                    if (StateMachine.this.mViewFinder.getVideoAutoReviewSetting() == VideoAutoReview.OFF) {
                        StateMachine.access$500(StateMachine.this, var4_4);
                        ** break;
                    }
                    try {
                        if (this.mCaptureState != CaptureState.STATE_PAUSE && this.mCaptureState != CaptureState.STATE_RESUME) {
                            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_AUTO_REVIEW, new Object[]{var4_4});
                        } else {
                            ** GOTO lbl48
                        }
                    }
                    catch (ActivityNotFoundException var6_8) {
                        CameraLogger.e(StateMachine.access$600(), "openReviewWindow: failed.", (Throwable)var6_8);
                    }
                    ** break;
                }
                case 2: {
                    if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.EDIT) {
                        StateMachine.access$300(StateMachine.this, var4_4);
                        ** break;
                    }
                    if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.OFF) {
                        StateMachine.access$500(StateMachine.this, var4_4);
                        ** break;
                    }
                    StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_AUTO_REVIEW, new Object[]{var4_4});
                }
lbl48: // 10 sources:
                default: {
                    ** GOTO lbl52
                }
                case 3: 
            }
            StateMachine.this.mViewFinder.startHideThumbnail();
lbl52: // 2 sources:
            if (StateMachine.this.mActivity.getWearableBridge() == null) lbl-1000: // 2 sources:
            {
                // MONITOREXIT : var12_2
                return;
            }
            StateMachine.this.mActivity.getWearableBridge().getPhotoStateNotifier().onCaptureSucceeded();
        }

        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
        }

        public /* varargs */ void handleOnTakePictureDone(Object ... arrobject) {
        }

        public /* varargs */ void handleOnVideoRecordingDone(Object ... arrobject) {
        }

        public /* varargs */ void handlePause(Object ... arrobject) {
        }

        public /* varargs */ void handlePrepareTouchZoom(Object ... arrobject) {
        }

        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
            StateMachine.this.mHandler.postDelayed((Runnable)new ReTrySetupHeadUpDisplayTask(), 100);
        }

        public /* varargs */ void handleResume(Object ... arrobject) {
        }

        public /* varargs */ void handleResumeTimeout(Object ... arrobject) {
        }

        public /* varargs */ void handleScreenClear(Object ... arrobject) {
        }

        public /* varargs */ void handleSetFocusPosition(Object ... arrobject) {
        }

        public /* varargs */ void handleSetSelectedObjectPosition(Object ... arrobject) {
        }

        public /* varargs */ void handleStartAfAfterObjectTracked(Object ... arrobject) {
        }

        public /* varargs */ void handleStartAfSearchInTouch(Object ... arrobject) {
        }

        public /* varargs */ void handleStartAfSearchInTouchStop(Object ... arrobject) {
        }

        public /* varargs */ void handleStartTouchZoom(Object ... arrobject) {
        }

        public /* varargs */ void handleStopTouchZoom(Object ... arrobject) {
        }

        public /* varargs */ void handleStorageError(Object ... arrobject) {
        }

        public /* varargs */ void handleStorageMounted(Object ... arrobject) {
        }

        public /* varargs */ void handleStorageShouldChange(Object ... arrobject) {
        }

        public /* varargs */ void handleSubCamcordButtonCancel(Object ... arrobject) {
        }

        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
        }

        public /* varargs */ void handleSubCamcordButtonTouch(Object ... arrobject) {
        }

        public /* varargs */ void handleSwitchCamera(Object ... arrobject) {
        }

        public void handleTouchContentProgress() {
        }

        public boolean isMenuAvailable() {
            return false;
        }

        public boolean isRecording() {
            return false;
        }

        public String toString() {
            if (this.mCaptureState == null) {
                return CaptureState.STATE_NONE.toString();
            }
            return this.mCaptureState.toString();
        }

        /*
         * Failed to analyse overrides
         */
        class ReTrySetupHeadUpDisplayTask
        implements Runnable {
            ReTrySetupHeadUpDisplayTask() {
            }

            public void run() {
                StateMachine.this.sendEvent(TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, new Object[0]);
            }
        }

    }

    class StateCameraSwitching
    extends State {
        private static final String TAG = "StateMachine.StateCameraSwitching";
        private final FastCapture mFastCapture;
        final /* synthetic */ StateMachine this$0;

        public StateCameraSwitching(StateMachine stateMachine) {
            this.this$0 = stateMachine;
            this.mCaptureState = CaptureState.STATE_CAMERA_SWITCHING;
            this.mFastCapture = FastCapture.LAUNCH_ONLY;
            stateMachine.mHandler.removeCallbacks(stateMachine.mNotifyResumeTimeoutTask);
            stateMachine.mHandler.postDelayed(stateMachine.mNotifyResumeTimeoutTask, 5000);
        }

        public StateCameraSwitching(StateMachine stateMachine, FastCapture fastCapture) {
            this.this$0 = stateMachine;
            this.mFastCapture = fastCapture;
        }

        @Override
        public /* varargs */ void handleOnEvfPreparationFailed(Object ... arrobject) {
            if (!this.this$0.mCameraDeviceHandler.isOpenDeviceThreadAlive()) {
                this.this$0.mActivity.prepareCameraDeviceHandler((Context)this.this$0.mActivity, this.mFastCapture, this.this$0.getTargetCameraId());
            }
        }

        @Override
        public /* varargs */ void handleOnEvfPrepared(Object ... arrobject) {
            SurfaceHolder surfaceHolder = (SurfaceHolder)arrobject[0];
            this.this$0.startFastCapture(surfaceHolder, this.mFastCapture);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            this.this$0.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleResumeTimeout(Object ... arrobject) {
            GoogleAnalyticsUtil.setCameraNotAvailableFailedToOpen();
            CameraLogger.errorLogForNonUserVariant("StateMachine.StateCameraSwitching", "[CameraNotAvailable] resume timeout.");
            this.this$0.mActivity.showCameraNotAvailableError();
            this.this$0.changeTo(new StateWarning(), new Object[0]);
        }
    }

    class StateFinalize
    extends State {
        public StateFinalize() {
            this.mCaptureState = CaptureState.STATE_FINALIZE;
            if (StateMachine.this.mSoundPlayer != null) {
                StateMachine.this.mSoundPlayer.release();
            }
        }

        @Override
        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
        }
    }

    class StateInitialize
    extends State {
        public StateInitialize() {
            this.mCaptureState = CaptureState.STATE_INITIALIZE;
            switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$CameraDeviceHandler$PreProcessState[StateMachine.this.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
                default: {
                    return;
                }
                case 1: 
            }
            StateMachine.this.mCameraDeviceHandler.preCapture();
        }

        @Override
        public /* varargs */ void handleOnInitialAutoFocusDone(Object ... arrobject) {
            StateMachine.this.mCameraDeviceHandler.preCapture();
        }

        @Override
        public /* varargs */ void handleOnPreShutterDone(Object ... arrobject) {
            NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(StateMachine.this, TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, arrobject, null);
            StateMachine.this.mHandler.postDelayed((Runnable)notifyDelayedEventTask, 100);
        }

        @Override
        public /* varargs */ void handleOnPreTakePictureDone(Object ... arrobject) {
            NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(StateMachine.this, TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, arrobject, null);
            StateMachine.this.mHandler.postDelayed((Runnable)notifyDelayedEventTask, 100);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleResume(Object ... arrobject) {
            StateMachine.this.mSoundPlayer = new SoundPlayer();
            StateMachine.this.changeTo(new StateResume((FastCapture)arrobject[0]), arrobject);
        }
    }

    class StateNone
    extends State {
        final /* synthetic */ StateMachine this$0;

        public StateNone(StateMachine stateMachine) {
            this.this$0 = stateMachine;
            this.mCaptureState = CaptureState.STATE_NONE;
        }

        public StateNone(StateMachine stateMachine, FastCapture fastCapture) {
            this.this$0 = stateMachine;
            this.mCaptureState = CaptureState.STATE_NONE;
        }

        @Override
        public /* varargs */ void handleInitialize(Object ... arrobject) {
            this.this$0.changeTo(new StateInitialize(), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            this.this$0.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePause
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePause";

        public StatePause() {
            super();
            this.mCaptureState = CaptureState.STATE_PAUSE;
            StateMachine.this.mCameraDeviceHandler.stopFaceDetection();
            StateMachine.this.mCameraDeviceHandler.stopSceneRecognition();
            StateMachine.this.mCameraDeviceHandler.deselectObject();
            StateMachine.this.mCameraDeviceHandler.stopLiveViewFinder();
            StateMachine.this.mCurrentCapturingMode = CapturingMode.SCENE_RECOGNITION;
            StateMachine.this.mTargetCapturingMode = CapturingMode.SCENE_RECOGNITION;
            StateMachine.this.removeStartRecordingTask();
        }

        @Override
        public /* varargs */ void handleFinalize(Object ... arrobject) {
            StateMachine.this.changeTo(new StateFinalize(), arrobject);
        }

        @Override
        public /* varargs */ void handleOnVideoRecordingDone(Object ... arrobject) {
            StateMachine.this.requestStoreVideo((VideoSavingRequest)arrobject[0]);
        }

        @Override
        public /* varargs */ void handleResume(Object ... arrobject) {
            if (StateMachine.this.mSoundPlayer == null) {
                StateMachine.this.mSoundPlayer = new SoundPlayer();
            }
            FastCapture fastCapture = (FastCapture)arrobject[0];
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_CAMERA_MODE_CHANGED_TO;
            Object[] arrobject2 = new Object[]{1};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            StateMachine.this.changeTo(new StateResume(fastCapture), new Object[0]);
        }
    }

    class StatePhotoAfDone
    extends StatePhotoBase {
        public StatePhotoAfDone() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_DONE;
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (StateMachine.this.mCameraDeviceHandler == null) {
                return;
            }
            if (StateMachine.this.isBurstShotEnabled()) {
                switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$BurstShot[StateMachine.this.mCameraDeviceHandler.getBurstShotSetting().ordinal()]) {
                    default: {
                        StateMachine.this.doCapture();
                        StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
                        return;
                    }
                    case 1: {
                        StateMachine stateMachine = StateMachine.this;
                        StatePhotoCaptureBestEffortBurst statePhotoCaptureBestEffortBurst = new StatePhotoCaptureBestEffortBurst(false, true);
                        Object[] arrobject2 = new Object[]{true};
                        stateMachine.changeTo(statePhotoCaptureBestEffortBurst, arrobject2);
                        StateMachine.this.doCaptureBestEffortBurst(true);
                        return;
                    }
                    case 2: 
                }
                StateMachine.this.doStartHighSpeedBurst(new StatePhotoCaptureHighSpeedBurst(false, true), true);
                return;
            }
            StateMachine.this.doCapture();
            StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
        }

        @Override
        public /* varargs */ void handleKeyFocusUp(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoAfDoneInTouch
    extends StatePhotoBase {
        public StatePhotoAfDoneInTouch() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_DONE_IN_TOUCH;
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleCaptureButtonLongPress(Object ... arrobject) {
            if (!StateMachine.this.isBurstShotEnabled()) {
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$BurstShot[StateMachine.this.mCameraDeviceHandler.getBurstShotSetting().ordinal()]) {
                default: {
                    return;
                }
                case 1: {
                    StateMachine stateMachine = StateMachine.this;
                    StatePhotoCaptureBestEffortBurst statePhotoCaptureBestEffortBurst = new StatePhotoCaptureBestEffortBurst(false, false);
                    Object[] arrobject2 = new Object[]{true};
                    stateMachine.changeTo(statePhotoCaptureBestEffortBurst, arrobject2);
                    StateMachine.this.doCaptureBestEffortBurst(true);
                    return;
                }
                case 2: 
            }
            StateMachine.this.doStartHighSpeedBurst(new StatePhotoCaptureHighSpeedBurst(false, false), true);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            StateMachine.this.doCapture();
            StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStartAfSearchInTouch(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StatePhotoAfSearchInTouch(true, true), arrobject);
        }
    }

    class StatePhotoAfSearch
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoAfSearch";

        public StatePhotoAfSearch() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_SEARCH;
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, true), arrobject);
        }

        @Override
        public /* varargs */ void handleKeyFocusUp(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            StateMachine.this.requestPlayAutoFocusSuccessSound((Boolean)arrobject[0]);
            StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoAfSearchInTouch
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoAfSearchInTouch";
        private boolean mIsAutoFocusAlreadyCanceled;
        private boolean mIsPreparePinchZoomAlreadyTimedout;
        private boolean mIsUseBurst;

        public StatePhotoAfSearchInTouch(boolean bl, boolean bl2) {
            super();
            this.mIsPreparePinchZoomAlreadyTimedout = false;
            this.mIsUseBurst = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_SEARCH_IN_TOUCH;
            this.mIsAutoFocusAlreadyCanceled = bl;
            this.mIsUseBurst = bl2;
            StateMachine.this.mHandler.postDelayed((Runnable)new Runnable(StateMachine.this){
                final /* synthetic */ StateMachine val$this$0;

                public void run() {
                    StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_PREPARE_TOUCH_ZOOM_TIMED_OUT, new Object[0]);
                }
            }, 100);
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            if (!this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.cancelAutoFocus(false);
                this.mIsAutoFocusAlreadyCanceled = true;
            }
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleCaptureButtonLongPress(Object ... arrobject) {
            if (StateMachine.this.mCameraDeviceHandler.getBurstShotSetting().isBurstShotOn()) {
                this.mIsUseBurst = true;
            }
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, false), arrobject);
            if (this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.startAutoFocus();
                this.mIsAutoFocusAlreadyCanceled = false;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            StateMachine.this.requestPlayAutoFocusSuccessSound((Boolean)arrobject[0]);
            StateMachine.this.changeTo(new StatePhotoAfDoneInTouch(), arrobject);
            if (!this.mIsUseBurst) return;
            switch (com.sonyericsson.android.camera.fastcapturing.StateMachine$5.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$BurstShot[StateMachine.this.mCameraDeviceHandler.getBurstShotSetting().ordinal()]) {
                default: {
                    return;
                }
                case 1: {
                    StateMachine stateMachine = StateMachine.this;
                    StatePhotoCaptureBestEffortBurst statePhotoCaptureBestEffortBurst = new StatePhotoCaptureBestEffortBurst(false, false);
                    Object[] arrobject2 = new Object[]{true};
                    stateMachine.changeTo(statePhotoCaptureBestEffortBurst, arrobject2);
                    StateMachine.this.doCaptureBestEffortBurst(true);
                    return;
                }
                case 2: 
            }
            StateMachine.this.doStartHighSpeedBurst(new StatePhotoCaptureHighSpeedBurst(false, false), true);
        }

        @Override
        public /* varargs */ void handleOnPrepareTouchZoomTimedOut(Object ... arrobject) {
            this.mIsPreparePinchZoomAlreadyTimedout = true;
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            if (!this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.cancelAutoFocus(true);
                this.mIsAutoFocusAlreadyCanceled = true;
            }
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handlePrepareTouchZoom(Object ... arrobject) {
            if (this.mIsPreparePinchZoomAlreadyTimedout || !StateMachine.this.isSmoothZoomEnabled()) {
                return;
            }
            if (!this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.cancelAutoFocus(true);
                this.mIsAutoFocusAlreadyCanceled = true;
            }
            StateMachine.this.changeTo(new StatePhotoZoomingInTouch(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleSetFocusPosition(Object ... arrobject) {
            if (this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.mCameraDeviceHandler.setFocusPosition((PointF)arrobject[1]);
            }
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED;
            Object[] arrobject2 = new Object[]{arrobject[0], arrobject[2]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleStartAfSearchInTouch(Object ... arrobject) {
            if (!this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.cancelAutoFocus(false);
                this.mIsAutoFocusAlreadyCanceled = true;
            }
            StateMachine.this.changeTo(new StatePhotoAfSearchInTouchDraggingFocusPosition(), arrobject);
        }

        @Override
        public /* varargs */ void handleStartAfSearchInTouchStop(Object ... arrobject) {
            if (this.mIsAutoFocusAlreadyCanceled) {
                StateMachine.this.startAutoFocus();
                this.mIsAutoFocusAlreadyCanceled = false;
            }
        }

    }

    class StatePhotoAfSearchInTouchDraggingFocusPosition
    extends StatePhotoBase {
        public StatePhotoAfSearchInTouchDraggingFocusPosition() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_AF_SEARCH_IN_TOUCH_DRAGGING_FOCUS_POSITION;
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            StateMachine.this.mCameraDeviceHandler.resetFocusMode();
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, false), arrobject);
            StateMachine.this.startAutoFocus();
        }

        @Override
        public /* varargs */ void handleCaptureButtonTouch(Object ... arrobject) {
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoAfSearchInTouch(false, (Boolean)arrobject[0]), arrobject);
            }
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.mCameraDeviceHandler.resetFocusMode();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleSetFocusPosition(Object ... arrobject) {
            StateMachine.this.mCameraDeviceHandler.setFocusPosition((PointF)arrobject[1]);
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED;
            Object[] arrobject2 = new Object[]{arrobject[0], arrobject[2]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleStartAfSearchInTouchStop(Object ... arrobject) {
            StateMachine.this.startAutoFocus();
            StateMachine.this.changeTo(new StatePhotoAfSearchInTouch(false, true), arrobject);
        }
    }

    class StatePhotoBase
    extends State {
        public StatePhotoBase() {
            this.mCaptureState = CaptureState.STATE_PHOTO_BASE;
        }

        @Override
        public /* varargs */ void handleOnOrientationChanged(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }
    }

    class StatePhotoCapture
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCapture";
        private boolean mIsNextCaptureRequired;

        public StatePhotoCapture() {
            this.mIsNextCaptureRequired = false;
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE;
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            if (!(StateMachine.this.mViewFinder.getAutoReviewSetting() != AutoReview.OFF || StateMachine.this.mActivity.isOneShotPhotoSecure())) {
                this.mIsNextCaptureRequired = true;
            }
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (!(StateMachine.this.mViewFinder.getAutoReviewSetting() != AutoReview.OFF || StateMachine.this.mActivity.isOneShotPhotoSecure())) {
                this.mIsNextCaptureRequired = true;
            }
        }

        @Override
        public /* varargs */ void handleOnPreShutterDone(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleOnPreTakePictureDone(Object ... arrobject) {
            PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
            StateMachine.this.changeTo(new StatePhotoStore(), new Object[0]);
            StateMachine.this.requestStorePicture(photoSavingRequest);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        @Override
        public /* varargs */ void handleOnShutterDone(Object ... arrobject) {
            StateMachine.this.mViewFinder.onCaptureDone();
            PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
            void var4_3 = this;
            synchronized (var4_3) {
                photoSavingRequest.setRequestId(StateMachine.this.mViewFinder.getRequestId(false));
            }
            StateMachine.this.mViewFinder.startCaptureFeedbackAnimation();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleOnTakePictureDone(Object ... arrobject) {
            PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
            if (this.mIsNextCaptureRequired) {
                StateMachine.this.requestStorePicture(photoSavingRequest);
                StateMachine.this.mViewFinder.startCaptureFeedbackAnimation();
                StateMachine.this.doCapture();
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForNextCapture(), new Object[0]);
            } else {
                StateMachine.this.changeTo(new StatePhotoStore(), new Object[0]);
                StateMachine.this.requestStorePicture(photoSavingRequest);
            }
            if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.OFF) {
                BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SET_EARLY_THUMBNAIL;
                Object[] arrobject2 = new Object[]{ThumbnailUtil.createThumbnailViewFromJpeg((Activity)StateMachine.this.mActivity, photoSavingRequest.getImageData(), StateMachine.access$2700((StateMachine)StateMachine.this).common.orientation)};
                baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
                BaseFastViewFinder baseFastViewFinder2 = StateMachine.this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent2 = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
                Object[] arrobject3 = new Object[]{photoSavingRequest.getRequestId()};
                baseFastViewFinder2.sendViewUpdateEvent(viewUpdateEvent2, arrobject3);
            }
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoCaptureBestEffortBurst
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCapture";
        private final boolean mIsCalledByKeyEvent;
        private boolean mIsCaptureDone;
        private boolean mIsMemoryErrorOccured;
        private boolean mIsRequestStoreDone;
        private final BurstShotPathBuilder mPathBuilder;
        private boolean mWaitingForAfDone;

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public StatePhotoCaptureBestEffortBurst(boolean bl, boolean bl2) {
            BurstShotPathBuilder burstShotPathBuilder;
            this.mIsMemoryErrorOccured = false;
            this.mIsCaptureDone = false;
            this.mIsRequestStoreDone = true;
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_BEST_EFFORT_BURST;
            this.mWaitingForAfDone = bl;
            this.mIsCalledByKeyEvent = bl2;
            StateMachine.this.clearBurst();
            try {
                burstShotPathBuilder = new BurstShotPathBuilder();
            }
            catch (IOException var5_5) {
                burstShotPathBuilder = null;
            }
            this.mPathBuilder = burstShotPathBuilder;
            StateMachine.this.mActivity.disableAutoOffTimer();
        }

        private void setStopBurstRequest(boolean bl) {
            if (this.mIsCalledByKeyEvent == bl) {
                StateMachine.this.mIsStopBurstRequested = true;
            }
        }

        private void takeNextPicture() {
            if (this.mIsCaptureDone && this.mIsRequestStoreDone) {
                if (this.mIsMemoryErrorOccured) {
                    StateMachine.this.doStopBestEffortBurst();
                    StateMachine.this.onBurstStoreComplete(false);
                    StateMachine.this.changeTo(new StateStandby(StateMachine.this, false), new Object[0]);
                    StateMachine.this.changeTo(new StateWarning(), new Object[0]);
                }
            } else {
                return;
            }
            if (StateMachine.this.mIsStopBurstRequested || !StateMachine.this.mActivity.getSavingTaskManager().canPushStoreTask()) {
                StateMachine.this.doStopBestEffortBurst();
                StateMachine.this.changeTo(new StatePhotoStore(), new Object[0]);
                return;
            }
            StateMachine.this.doCaptureBestEffortBurst(false);
            this.mIsCaptureDone = false;
            this.mIsRequestStoreDone = false;
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            super.setStopBurstRequest(false);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            super.setStopBurstRequest(false);
        }

        @Override
        public /* varargs */ void handleKeyCaptureUp(Object ... arrobject) {
            super.setStopBurstRequest(true);
        }

        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            if (!this.mWaitingForAfDone) {
                return;
            }
            StateMachine.this.requestPlayAutoFocusSuccessSound((Boolean)arrobject[0]);
            StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
            StateMachine stateMachine = StateMachine.this;
            Object[] arrobject2 = new Object[]{false};
            stateMachine.changeTo((State)this, arrobject2);
            StateMachine.this.doCaptureBestEffortBurst(true);
            this.mWaitingForAfDone = false;
        }

        @Override
        public /* varargs */ void handleOnShutterDone(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_COUNT_UP_VIEW;
            Object[] arrobject2 = new Object[]{StateMachine.this.mBurstPictureCount};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
            this.mIsRequestStoreDone = true;
            super.takeNextPicture();
        }

        @Override
        public /* varargs */ void handleOnTakePictureDone(Object ... arrobject) {
            if (this.mPathBuilder != null) {
                PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
                String string = this.mPathBuilder.getNextFilePathToStorePicture();
                photoSavingRequest.setFilePath(string);
                photoSavingRequest.setExtraOutput(Uri.fromFile((File)new File(string)));
                StateMachine.this.requestStorePicture(photoSavingRequest);
            }
            this.mIsCaptureDone = true;
            super.takeNextPicture();
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopBestEffortBurst();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            this.mIsMemoryErrorOccured = true;
        }
    }

    class StatePhotoCaptureHighSpeedBurst
    extends StatePhotoBase {
        private final boolean mIsCalledByKeyEvent;
        private boolean mWaitingForAfDone;

        public StatePhotoCaptureHighSpeedBurst(boolean bl, boolean bl2) {
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_HI_SPEED_BURST;
            this.mWaitingForAfDone = bl;
            this.mIsCalledByKeyEvent = bl2;
            StateMachine.this.clearBurst();
            StateMachine.this.mActivity.disableAutoOffTimer();
        }

        private void finishBurst() {
            StateMachine.this.mViewFinder.fadeoutCounter();
            if (StateMachine.this.mIsStopBurstRequested && StateMachine.this.mBurstPictureCount >= 2) {
                if (StateMachine.this.doStopHighSpeedBurst()) {
                    StateMachine.this.mIsStopBurstRequested = false;
                }
            } else {
                return;
            }
            StateMachine.this.changeTo(new StateWarning(), new Object[0]);
        }

        private void setStopBurstRequest() {
            StateMachine.this.mIsStopBurstRequested = true;
        }

        private void setStopBurstRequest(boolean bl) {
            if (this.mIsCalledByKeyEvent == bl) {
                StateMachine.this.mIsStopBurstRequested = true;
                super.finishBurst();
            }
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            super.setStopBurstRequest(false);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            super.setStopBurstRequest(false);
        }

        @Override
        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
            if (arrobject.length > 0 && arrobject[0] != null && (BaseFastViewFinder.UiComponentKind)arrobject[0] == BaseFastViewFinder.UiComponentKind.REVIEW_WINDOW) {
                StateMachine.this.changeTo(new StatePhotoStandbyDialog(), arrobject);
            }
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
        }

        @Override
        public /* varargs */ void handleKeyCaptureUp(Object ... arrobject) {
            super.setStopBurstRequest(true);
        }

        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            if (!this.mWaitingForAfDone) {
                return;
            }
            StateMachine.this.requestPlayAutoFocusSuccessSound((Boolean)arrobject[0]);
            StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
            StateMachine.this.doStartHighSpeedBurst((State)this, false);
            this.mWaitingForAfDone = false;
        }

        @Override
        public /* varargs */ void handleOnBurstGroupStoreCompleted(Object ... arrobject) {
            ((BurstSavingTaskManager)StateMachine.this.mActivity.getSavingTaskManager()).getUpdator().commitBulkInsert();
            StateMachine.this.onBurstStoreComplete(true);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public /* varargs */ void handleOnBurstShutterDone(Object ... arrobject) {
            StateMachine.this.updateDateTaken(StateMachine.this.mLastPhotoSavingRequest);
            CameraExtension.BurstShotResult burstShotResult = (CameraExtension.BurstShotResult)arrobject[0];
            if (burstShotResult == null) {
                return;
            }
            StateMachine.this.mBurstPictureCount = burstShotResult.mPictureCount;
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_COUNT_UP_VIEW;
            Object[] arrobject2 = new Object[]{StateMachine.this.mBurstPictureCount};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            if (!StateMachine.this.mIsStopBurstRequested) return;
            StateMachine.this.mActivity.postDelayedEvent((Runnable)new FinishHighSpeedBurstTask((StatePhotoCaptureHighSpeedBurst)this, null), 100);
        }

        @Override
        public /* varargs */ void handleOnBurstStoreCompleted(Object ... arrobject) {
            if (StateMachine.this.mIsStopBurstRequested) {
                super.finishBurst();
                return;
            }
            StateMachine.this.calculateRemainStorage(true, true);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopHighSpeedBurst();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            super.setStopBurstRequest();
        }

        /*
         * Failed to analyse overrides
         */
        private class FinishHighSpeedBurstTask
        implements Runnable {
            final /* synthetic */ StatePhotoCaptureHighSpeedBurst this$1;

            private FinishHighSpeedBurstTask(StatePhotoCaptureHighSpeedBurst statePhotoCaptureHighSpeedBurst) {
                this.this$1 = statePhotoCaptureHighSpeedBurst;
            }

            /* synthetic */ FinishHighSpeedBurstTask(StatePhotoCaptureHighSpeedBurst statePhotoCaptureHighSpeedBurst,  var2_2) {
                super(statePhotoCaptureHighSpeedBurst);
            }

            public void run() {
                this.this$1.finishBurst();
            }
        }

    }

    class StatePhotoCaptureWaitForAfDone
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCaptureWaitForAfDone";
        private final boolean mIsCalledByKeyEvent;
        private final boolean mIsDirectCaptureRequired;
        private boolean mIsReleaseShutterKey;

        public StatePhotoCaptureWaitForAfDone(boolean bl, boolean bl2) {
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_AF_DONE;
            this.mIsCalledByKeyEvent = bl2;
            this.mIsDirectCaptureRequired = bl;
        }

        @Override
        public /* varargs */ void handleKeyCaptureUp(Object ... arrobject) {
            this.mIsReleaseShutterKey = true;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            StateMachine.this.requestPlayAutoFocusSuccessSound((Boolean)arrobject[0]);
            if (this.mIsReleaseShutterKey || !this.mIsCalledByKeyEvent) {
                StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
                StateMachine.this.doCapture();
                StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
                return;
            }
            if (StateMachine.this.mCameraDeviceHandler == null) return;
            int n = StateMachine.this.mCameraDeviceHandler.getCameraDeviceStatus();
            if (n == 0) return;
            if (!StateMachine.this.isBurstShotEnabled()) {
                StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
                StateMachine.this.doCapture();
                StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$BurstShot[StateMachine.this.mCameraDeviceHandler.getBurstShotSetting().ordinal()]) {
                default: {
                    StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
                    StateMachine.this.doCapture();
                    StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
                    return;
                }
                case 1: {
                    StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
                    StateMachine stateMachine = StateMachine.this;
                    StatePhotoCaptureBestEffortBurst statePhotoCaptureBestEffortBurst = new StatePhotoCaptureBestEffortBurst(false, this.mIsCalledByKeyEvent);
                    Object[] arrobject2 = new Object[]{false};
                    stateMachine.changeTo(statePhotoCaptureBestEffortBurst, arrobject2);
                    StateMachine.this.doCaptureBestEffortBurst(true);
                    return;
                }
                case 2: 
            }
            StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
            StateMachine.this.doStartHighSpeedBurst(new StatePhotoCaptureHighSpeedBurst(false, this.mIsCalledByKeyEvent), true);
        }

        @Override
        public /* varargs */ void handleOnInitialAutoFocusDone(Object ... arrobject) {
            if (this.mIsDirectCaptureRequired) {
                StateMachine.this.changeTo(new StatePhotoAfDone(), arrobject);
                StateMachine.this.mCameraDeviceHandler.preCapture();
                StateMachine.this.changeTo(new StatePhotoCapture(), arrobject);
                return;
            }
            StateMachine.this.cancelAutoFocus(false);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoCaptureWaitForNextCapture
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StatePhotoCapture";
        private PhotoSavingRequest mCachedRequest;
        private boolean mIsAlreadyPreviousCaptureDone;
        private boolean mIsAlreadyRequestStoreDone;
        private boolean mIsMemoryErrorOccured;
        private boolean mIsNextCaptureRequired;

        public StatePhotoCaptureWaitForNextCapture() {
            this.mIsMemoryErrorOccured = false;
            this.mIsNextCaptureRequired = false;
            this.mIsAlreadyPreviousCaptureDone = false;
            this.mIsAlreadyRequestStoreDone = false;
            this.mCachedRequest = null;
            this.mCaptureState = CaptureState.STATE_PHOTO_CAPTURE_WAIT_FOR_NEXT_CAPTURE;
        }

        private /* varargs */ void checkAndGoToNextCapture(Object ... arrobject) {
            if (this.mIsAlreadyPreviousCaptureDone && this.mIsAlreadyRequestStoreDone) {
                StateMachine.this.requestStorePicture(this.mCachedRequest);
                if (this.mIsMemoryErrorOccured) {
                    StateMachine.this.mViewFinder.onCaptureDone();
                    StateMachine.this.cancelAutoFocus(true);
                    StateMachine.this.changeTo(new StateStandby(StateMachine.this, false), new Object[0]);
                    StateMachine.this.changeTo(new StateWarning(), new Object[0]);
                }
            } else {
                return;
            }
            if (!this.mIsNextCaptureRequired) {
                StateMachine.this.mViewFinder.onCaptureDone();
                StateMachine.this.changeTo(new StatePhotoStore(), new Object[0]);
                return;
            }
            StateMachine.this.mViewFinder.startCaptureFeedbackAnimation();
            StateMachine.this.doCapture();
            this.mIsAlreadyPreviousCaptureDone = false;
            this.mIsAlreadyRequestStoreDone = false;
            this.mIsNextCaptureRequired = false;
            if (StateMachine.this.mViewFinder.getAutoReviewSetting() == AutoReview.OFF) {
                PhotoSavingRequest photoSavingRequest = this.mCachedRequest;
                BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SET_EARLY_THUMBNAIL;
                Object[] arrobject2 = new Object[]{ThumbnailUtil.createThumbnailViewFromJpeg((Activity)StateMachine.this.mActivity, photoSavingRequest.getImageData(), StateMachine.access$2700((StateMachine)StateMachine.this).common.orientation)};
                baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
                BaseFastViewFinder baseFastViewFinder2 = StateMachine.this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent2 = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
                Object[] arrobject3 = new Object[]{photoSavingRequest.getRequestId()};
                baseFastViewFinder2.sendViewUpdateEvent(viewUpdateEvent2, arrobject3);
            }
            this.mCachedRequest = null;
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            if (!StateMachine.this.mActivity.getSavingTaskManager().canPushStoreTask()) {
                return;
            }
            this.mIsNextCaptureRequired = true;
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (!StateMachine.this.mActivity.getSavingTaskManager().canPushStoreTask()) {
                return;
            }
            this.mIsNextCaptureRequired = true;
        }

        @Override
        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
            this.mIsAlreadyRequestStoreDone = true;
            super.checkAndGoToNextCapture(arrobject);
        }

        @Override
        public /* varargs */ void handleOnTakePictureDone(Object ... arrobject) {
            this.mCachedRequest = (PhotoSavingRequest)arrobject[0];
            if (StateMachine.this.mContentsViewController != null) {
                this.mCachedRequest.setRequestId(StateMachine.this.mContentsViewController.createClearContentFrame());
            }
            this.mIsAlreadyPreviousCaptureDone = true;
            Object[] arrobject2 = new Object[]{this.mCachedRequest};
            super.checkAndGoToNextCapture(arrobject2);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            this.mIsMemoryErrorOccured = true;
        }
    }

    class StatePhotoReadyForRecording
    extends StatePhotoBase {
        public StatePhotoReadyForRecording() {
            this.mCaptureState = CaptureState.STATE_PHOTO_READY_FOR_RECORDING;
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            StateMachine.this.changeTo(new StateWarning(), arrobject);
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonCancel(Object ... arrobject) {
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_START, true);
            if (!AudioResourceChecker.isAudioResourceAvailable((BaseActivity)StateMachine.this.mActivity)) {
                StateMachine.this.mActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
                StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
                return;
            }
            if (!StateMachine.this.mActivity.updateRemain()) {
                StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
                return;
            }
            StateMachine.this.mActivity.pauseAudioPlayback();
            StateMachine.this.cancelAutoFocus(true);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
            StateMachine.this.doStartRecordingInPhotoMode();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_START, false);
            MeasurePerformance.outResult();
        }

        @Override
        public boolean isMenuAvailable() {
            return false;
        }
    }

    class StatePhotoStandbyDialog
    extends StatePhotoBase {
        public StatePhotoStandbyDialog() {
            this.mCaptureState = CaptureState.STATE_STANDBY_DIALOG;
            StateMachine.this.mCameraDeviceHandler.suspendPhotoLight();
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.enableAutoOffTimer();
            }
        }

        @Override
        public /* varargs */ void handleDialogClosed(Object ... arrobject) {
            StateMachine.this.mActivity.notifyStateIdleToWearable();
            StateMachine.this.mCameraDeviceHandler.resumePhotoLight();
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_UPDATE_DIALOGS;
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
        }

        @Override
        public /* varargs */ void handleKeyBack(Object ... arrobject) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_CLOSE_DIALOGS, new Object[0]);
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleKeyMenu(Object ... arrobject) {
            StateMachine.this.mActivity.requestLaunchAdvancedCameraApplication(1);
        }

        @Override
        public /* varargs */ void handleOnContinuousPreviewFrameUpdated(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageShouldChange(Object ... arrobject) {
            StateMachine.this.switchStorage();
        }

        @Override
        public /* varargs */ void handleSwitchCamera(Object ... arrobject) {
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_CLOSE_DIALOGS, new Object[0]);
            StateMachine.this.switchCamera();
        }

        @Override
        public boolean isMenuAvailable() {
            return true;
        }
    }

    class StatePhotoStore
    extends StatePhotoBase {
        public StatePhotoStore() {
            this.mCaptureState = CaptureState.STATE_PHOTO_STORE;
            StateMachine.this.cancelAutoFocus(true);
        }

        @Override
        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
            if (arrobject.length > 0 && arrobject[0] != null && (BaseFastViewFinder.UiComponentKind)arrobject[0] == BaseFastViewFinder.UiComponentKind.REVIEW_WINDOW) {
                StateMachine.this.changeTo(new StatePhotoStandbyDialog(), arrobject);
            }
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
            if (StateMachine.this.mViewFinder.getAutoReviewSetting() == null) {
                BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
                BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
                Object[] arrobject2 = new Object[]{BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_CAPTURE};
                baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            }
            if (StateMachine.this.mLastPhotoSavingRequest != null && StateMachine.access$2700((StateMachine)StateMachine.this).common.savedFileType == SavingTaskManager.SavedFileType.BURST && StateMachine.this.mIsStopBurstRequested && StateMachine.this.mBurstPictureCount > 1) {
                StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
                return;
            } else {
                if (StateMachine.this.mViewFinder.getAutoReviewSetting() != AutoReview.OFF) return;
                {
                    StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
                    return;
                }
            }
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoWaitingTrackedObjectForAfStart
    extends State {
        boolean mIsAutoFocusStarted;
        boolean mIsFirstCallback;
        boolean mIsImmediateCaptureRequired;

        public StatePhotoWaitingTrackedObjectForAfStart() {
            this.mIsAutoFocusStarted = false;
            this.mIsImmediateCaptureRequired = false;
            this.mIsFirstCallback = true;
            this.mCaptureState = CaptureState.STATE_PHOTO_WAITING_TRACKED_OBJECT_FOR_AF_START;
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            if (this.mIsAutoFocusStarted) {
                StateMachine.this.cancelAutoFocus(true);
            }
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), new Object[0]);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            this.mIsImmediateCaptureRequired = true;
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            if (((CameraExtension.ObjectTrackingResult)arrobject[0]).mIsLost || this.mIsFirstCallback) {
                this.mIsFirstCallback = false;
                return;
            }
            StateMachine.this.startAutoFocus();
            this.mIsAutoFocusStarted = true;
            if (this.mIsImmediateCaptureRequired) {
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, false), new Object[0]);
                return;
            }
            StateMachine.this.changeTo(new StatePhotoAfSearchInTouch(false, false), new Object[0]);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            if (this.mIsAutoFocusStarted) {
                StateMachine.this.cancelAutoFocus(true);
            }
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStartAfAfterObjectTracked(Object ... arrobject) {
            if (this.mIsAutoFocusStarted) {
                return;
            }
            PointF pointF = (PointF)arrobject[1];
            StateMachine.this.doDeselectObject();
            StateMachine.this.doSelectObject(pointF);
            this.mIsAutoFocusStarted = false;
            this.mIsImmediateCaptureRequired = false;
        }
    }

    class StatePhotoZooming
    extends StatePhotoZoomingBase {
        public StatePhotoZooming() {
            super();
            this.mCaptureState = CaptureState.STATE_PHOTO_ZOOMING;
        }

        @Override
        public /* varargs */ void handleKeyZoomUp(Object ... arrobject) {
            StateMachine.this.doStopZoom();
            StateMachine stateMachine = StateMachine.this;
            StateStandby stateStandby = new StateStandby(StateMachine.this, true);
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.UiComponentKind.ZOOM_BAR};
            stateMachine.changeTo(stateStandby, arrobject2);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StatePhotoZoomingBase
    extends StatePhotoBase {
        public StatePhotoZoomingBase() {
            this.mCaptureState = CaptureState.STATE_PHOTO_ZOOMING_BASE;
        }

        @Override
        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }
    }

    class StatePhotoZoomingInTouch
    extends StatePhotoZoomingBase {
        private final int mStartZoomStep;

        public StatePhotoZoomingInTouch() {
            this.mCaptureState = CaptureState.STATE_PHOTO_ZOOMING_IN_TOUCH;
            this.mStartZoomStep = StateMachine.this.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
            StateMachine.this.mCurrentZoomLength = StateMachine.this.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
        }

        @Override
        public /* varargs */ void handleCancelTouchZoom(Object ... arrobject) {
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStartTouchZoom(Object ... arrobject) {
            float f = ((Float)arrobject[0]).floatValue();
            StateMachine.this.doZoom(this.mStartZoomStep, f);
        }

        @Override
        public /* varargs */ void handleStopTouchZoom(Object ... arrobject) {
            StateMachine.this.doStopZoom();
        }
    }

    class StateResume
    extends StateCameraSwitching {
        /*
         * Enabled aggressive block sorting
         */
        public StateResume(FastCapture fastCapture) {
            super(StateMachine.this, fastCapture);
            this.mCaptureState = CaptureState.STATE_RESUME;
            switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$CameraDeviceHandler$PreProcessState[StateMachine.this.mCameraDeviceHandler.getPreProcessState().ordinal()]) {
                default: {
                    break;
                }
                case 1: {
                    StateMachine.this.mCameraDeviceHandler.preCapture();
                }
            }
            StateMachine.this.mHandler.removeCallbacks(StateMachine.this.mNotifyResumeTimeoutTask);
            StateMachine.this.mHandler.postDelayed(StateMachine.this.mNotifyResumeTimeoutTask, 5000);
        }

        @Override
        public /* varargs */ void handleOnInitialAutoFocusDone(Object ... arrobject) {
            StateMachine.this.mCameraDeviceHandler.preCapture();
        }

        @Override
        public /* varargs */ void handleOnPreShutterDone(Object ... arrobject) {
            NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(StateMachine.this, TransitterEvent.EVENT_ON_PRE_SHUTTER_DONE, arrobject, null);
            StateMachine.this.mHandler.postDelayed((Runnable)notifyDelayedEventTask, 100);
        }

        @Override
        public /* varargs */ void handleOnPreTakePictureDone(Object ... arrobject) {
            NotifyDelayedEventTask notifyDelayedEventTask = new NotifyDelayedEventTask(StateMachine.this, TransitterEvent.EVENT_ON_PRE_TAKE_PICTURE_DONE, arrobject, null);
            StateMachine.this.mHandler.postDelayed((Runnable)notifyDelayedEventTask, 100);
        }
    }

    class StateSelfTimerCountdown
    extends StateStandby {
        private final Runnable mCountdownFinishEvent;
        private SelfTimerFeedback mFeedback;
        private final LedLight mLedLight;

        public StateSelfTimerCountdown() {
            super(StateMachine.this);
            this.mCaptureState = CaptureState.STATE_PHOTO_SELFTIMER_COUNTDOWN;
            this.mLedLight = new LedLightImpl((StateSelfTimerCountdown)this, null);
            this.mCountdownFinishEvent = new CountdownFinishEvent((StateSelfTimerCountdown)this, null);
        }

        private void recoverFlash() {
            StateMachine.this.mCameraDeviceHandler.resumePhotoLight();
        }

        private void startSelfTimer(SelfTimer selfTimer) {
            StateMachine.this.mHandler.postDelayed(this.mCountdownFinishEvent, (long)selfTimer.getDurationInMillisecond());
            this.mFeedback = new SelfTimerFeedback(selfTimer.getDurationInMillisecond(), this.mLedLight);
            this.mFeedback.start(0);
            if (!StateMachine.this.mCameraDeviceHandler.isShutterSoundOff()) {
                StateMachine.this.mActivity.playSound(selfTimer);
            }
        }

        @Override
        public void entry() {
            this.startSelfTimer(StateMachine.this.mViewFinder.getPhotoSelfTimerSetting());
        }

        @Override
        public void exit() {
            StateMachine.this.stopPlaySound();
            this.mFeedback.stop();
            StateMachine.this.mHandler.removeCallbacks(this.mCountdownFinishEvent);
            StateMachine.this.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_SELFTIMER_FINISHED, new Object[0]);
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleCaptureButtonTouch(Object ... arrobject) {
            super.recoverFlash();
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoAfSearchInTouch(false, (Boolean)arrobject[0]), arrobject);
            }
        }

        @Override
        public /* varargs */ void handleKeyBack(Object ... arrobject) {
            super.recoverFlash();
            StateMachine.this.changeTo(new StateStandby(StateMachine.this, true), arrobject);
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            super.recoverFlash();
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, true), new Object[0]);
            }
        }

        @Override
        public /* varargs */ void handleKeyFocusDown(Object ... arrobject) {
            super.recoverFlash();
            if (StateMachine.this.startAutoFocus()) {
                StateMachine.this.changeTo(new StatePhotoAfSearch(), arrobject);
            }
        }

        @Override
        public /* varargs */ void handleKeyZoomInDown(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleKeyZoomOutDown(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            super.recoverFlash();
            super.handlePause(arrobject);
        }

        @Override
        public /* varargs */ void handleSetFocusPosition(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            super.recoverFlash();
            super.handleStorageError(arrobject);
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
        }

        /*
         * Failed to analyse overrides
         */
        private class CountdownFinishEvent
        implements Runnable {
            final /* synthetic */ StateSelfTimerCountdown this$1;

            private CountdownFinishEvent(StateSelfTimerCountdown stateSelfTimerCountdown) {
                this.this$1 = stateSelfTimerCountdown;
            }

            /* synthetic */ CountdownFinishEvent(StateSelfTimerCountdown stateSelfTimerCountdown,  var2_2) {
                super(stateSelfTimerCountdown);
            }

            public void run() {
                this.this$1.recoverFlash();
                if (this.this$1.StateMachine.this.startAutoFocus()) {
                    this.this$1.StateMachine.this.changeTo(new StatePhotoCaptureWaitForAfDone(false, true), new Object[0]);
                }
            }
        }

        private class LedLightImpl
        implements LedLight {
            final /* synthetic */ StateSelfTimerCountdown this$1;

            private LedLightImpl(StateSelfTimerCountdown stateSelfTimerCountdown) {
                this.this$1 = stateSelfTimerCountdown;
            }

            /* synthetic */ LedLightImpl(StateSelfTimerCountdown stateSelfTimerCountdown,  var2_2) {
                super(stateSelfTimerCountdown);
            }

            @Override
            public void turnOff() {
                this.this$1.StateMachine.this.mCameraDeviceHandler.setTorch(false);
            }

            @Override
            public void turnOn() {
                this.this$1.StateMachine.this.mCameraDeviceHandler.setTorch(true);
            }
        }

    }

    class StateStandby
    extends StatePhotoBase {
        private static final String TAG = "StateMachine.StateStandby";
        private CameraExtension.FaceDetectionResult mLatestFaceDetectionResult;
        private final boolean mWithExtensionFeatures;
        final /* synthetic */ StateMachine this$0;

        public StateStandby(StateMachine stateMachine) {
            super(stateMachine, false);
        }

        public StateStandby(StateMachine stateMachine, boolean bl) {
            this.this$0 = stateMachine;
            this.mLatestFaceDetectionResult = null;
            this.mCaptureState = CaptureState.STATE_STANDBY;
            this.mWithExtensionFeatures = bl;
        }

        @Override
        public void entry() {
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.notifyStateIdleToWearable();
            }
            this.this$0.mCameraDeviceHandler.startPreview();
            if (this.mWithExtensionFeatures) {
                if (!this.this$0.mViewFinder.isTouchFocus()) {
                    this.this$0.mCameraDeviceHandler.startFaceDetection();
                    this.this$0.mCameraDeviceHandler.startFaceIdService((Activity)this.this$0.mActivity);
                }
                this.this$0.mCameraDeviceHandler.startSceneRecognition();
            }
            if (this.this$0.mActivity != null) {
                this.this$0.mActivity.enableAutoOffTimer();
            }
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED;
            Object[] arrobject = new Object[]{this.this$0.getSensorOrientation()};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
            if (this.this$0.mViewFinder.isHeadUpDesplayReady()) {
                this.this$0.mActivity.getStorageManager().updateRemain(0, false);
                if (!this.this$0.mActivity.getStorageManager().isReady()) {
                    this.this$0.sendEvent(TransitterEvent.EVENT_STORAGE_ERROR, new Object[0]);
                }
            }
        }

        @Override
        public void exit() {
            this.this$0.mActivity.notifyStateBlockedToWearable();
        }

        @Override
        public /* varargs */ void handleCaptureButtonCancel(Object ... arrobject) {
            if (this.this$0.isPhotoSelfTimerEnabled()) {
                return;
            }
            this.this$0.mCameraDeviceHandler.resetFocusMode();
            this.this$0.mCameraDeviceHandler.startFaceDetection();
            this.this$0.mCameraDeviceHandler.startFaceIdService((Activity)this.this$0.mActivity);
            this.this$0.mCameraDeviceHandler.startSceneRecognition();
            this.this$0.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_AF_CANCELED, new Object[0]);
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            if (this.this$0.isPhotoSelfTimerEnabled() && this.this$0.mActivity.updateRemain()) {
                this.this$0.changeTo(new StateSelfTimerCountdown(), new Object[0]);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleCaptureButtonTouch(Object ... arrobject) {
            if (this.this$0.isPhotoSelfTimerEnabled() || !this.this$0.startAutoFocus()) {
                return;
            }
            this.this$0.changeTo(new StatePhotoAfSearchInTouch(false, (Boolean)arrobject[0]), arrobject);
        }

        @Override
        public /* varargs */ void handleChangeSelectedFace(Object ... arrobject) {
            Point point = (Point)arrobject[0];
            this.this$0.doChangeSelectedFace(point);
        }

        @Override
        public /* varargs */ void handleDeselectObjectPosition(Object ... arrobject) {
            this.this$0.doDeselectObject();
        }

        @Override
        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
            this.this$0.changeTo(new StatePhotoStandbyDialog(), arrobject);
            this.this$0.mActivity.notifyStateBlockedToWearable();
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (this.this$0.isPhotoSelfTimerEnabled()) {
                this.this$0.changeTo(new StateSelfTimerCountdown(), new Object[0]);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleKeyFocusDown(Object ... arrobject) {
            if (this.this$0.isPhotoSelfTimerEnabled() || !this.this$0.startAutoFocus()) {
                return;
            }
            this.this$0.changeTo(new StatePhotoAfSearch(), arrobject);
        }

        @Override
        public /* varargs */ void handleKeyMenu(Object ... arrobject) {
            this.this$0.mActivity.requestLaunchAdvancedCameraApplication(1);
        }

        @Override
        public /* varargs */ void handleKeyZoomInDown(Object ... arrobject) {
            if (this.this$0.mCameraDeviceHandler.getLatestCachedParameters() == null) {
                return;
            }
            this.handleScreenClear(arrobject);
            if (this.this$0.isSmoothZoomEnabled()) {
                this.this$0.changeTo(new StatePhotoZooming(), arrobject);
                if (this.this$0.mCameraDeviceHandler.getLatestCachedParameters().getZoom() < this.this$0.mCameraDeviceHandler.getMaxSuperResolutionZoom()) {
                    this.this$0.doSuperResolutionZoomIn();
                    return;
                }
                this.this$0.doZoomIn();
                return;
            }
            this.this$0.mActivity.getMessagePopup().showZoomHelpMessage(false);
        }

        @Override
        public /* varargs */ void handleKeyZoomOutDown(Object ... arrobject) {
            if (this.this$0.mCameraDeviceHandler.getLatestCachedParameters() == null) {
                return;
            }
            this.handleScreenClear(arrobject);
            if (this.this$0.isSmoothZoomEnabled()) {
                this.this$0.changeTo(new StatePhotoZooming(), arrobject);
                this.this$0.doZoomOut();
                return;
            }
            this.this$0.mActivity.getMessagePopup().showZoomHelpMessage(false);
        }

        @Override
        public /* varargs */ void handleOnAutoFocusDone(Object ... arrobject) {
            this.this$0.cancelAutoFocus(false);
        }

        @Override
        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
            this.mLatestFaceDetectionResult = (CameraExtension.FaceDetectionResult)arrobject[0];
            FaceIdentification faceIdentification = this.this$0.mCameraDeviceHandler.getFaceIdService();
            if (!(faceIdentification == null || faceIdentification.isBusy())) {
                this.this$0.mCameraDeviceHandler.requestOnePreviewFrame();
            }
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED;
            Object[] arrobject2 = new Object[]{this.mLatestFaceDetectionResult};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnFaceIdentified(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_NAME_DETECTED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
            FaceIdentification faceIdentification;
            if (this.this$0.mCameraDeviceHandler == null || this.mLatestFaceDetectionResult == null || (faceIdentification = this.this$0.mCameraDeviceHandler.getFaceIdService()) == null || this.mLatestFaceDetectionResult.extFaceList.size() <= 0) {
                return;
            }
            List<FaceIdentification.FaceIdentificationRequest> list = FaceDetectUtil.createFaceIdentificationRequest(this.mLatestFaceDetectionResult);
            faceIdentification.request((byte[])arrobject[0], (Integer)arrobject[1], ((Rect)arrobject[2]).width(), ((Rect)arrobject[2]).height(), this.this$0.getOrientation(), list, new FaceIdCallback(this.this$0, null));
        }

        @Override
        public /* varargs */ void handleOnSceneModeChanged(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            this.this$0.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handlePrepareTouchZoom(Object ... arrobject) {
            if (this.this$0.isSmoothZoomEnabled()) {
                this.this$0.mCurrentZoomLength = this.this$0.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
                this.this$0.changeTo(new StatePhotoZoomingInTouch(), arrobject);
            }
        }

        @Override
        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
            this.this$0.mViewFinder.sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_RELEASED, new Object[0]);
        }

        @Override
        public /* varargs */ void handleSetFocusPosition(Object ... arrobject) {
            this.this$0.mCameraDeviceHandler.setFocusPosition((PointF)arrobject[1]);
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FOCUS_POSITION_SELECTED;
            Object[] arrobject2 = new Object[]{arrobject[0], arrobject[2]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleSetSelectedObjectPosition(Object ... arrobject) {
            PointF pointF = (PointF)arrobject[1];
            this.this$0.doSelectObject(pointF);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleStartAfAfterObjectTracked(Object ... arrobject) {
            if (!(PlatformDependencyResolver.isObjectTrackingSuppoted(this.this$0.mCameraDeviceHandler.getLatestCachedParameters()) && (this.this$0.mActivity == null || this.this$0.mActivity.updateRemain()))) {
                return;
            }
            PointF pointF = (PointF)arrobject[1];
            this.this$0.doDeselectObject();
            this.this$0.doSelectObject(pointF);
            this.this$0.changeTo(new StatePhotoWaitingTrackedObjectForAfStart(), new Object[0]);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            this.this$0.changeTo(new StateWarning(), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageShouldChange(Object ... arrobject) {
            this.this$0.switchStorage();
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonTouch(Object ... arrobject) {
            this.this$0.changeTo(new StatePhotoReadyForRecording(), new Object[0]);
        }

        @Override
        public /* varargs */ void handleSwitchCamera(Object ... arrobject) {
            this.this$0.switchCamera();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleTouchContentProgress() {
            if (this.this$0.mViewFinder == null || this.this$0.mLastVideoSavingRequest != null && (this.this$0.mLastPhotoSavingRequest == null || this.this$0.mLastVideoSavingRequest.getDateTaken() > this.this$0.mLastPhotoSavingRequest.getDateTaken()) || this.this$0.mLastPhotoSavingRequest == null) {
                return;
            }
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_INSTANT_VIEWER;
            Object[] arrobject = new Object[]{this.this$0.mLastPhotoSavingRequest.getImageData(), this.this$0.mLastPhotoSavingRequest};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject);
        }

        @Override
        public boolean isMenuAvailable() {
            return true;
        }
    }

    class StateVideoBase
    extends State {
        StateVideoBase() {
        }

        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
            CameraLogger.e(TAG, "## ## ## ## StateVideoBase.handleOnOnePreviewFrameUpdated()");
            StateMachine.this.mChapterThumbnail = new ChapterThumbnail((byte[])arrobject[0], (Integer)arrobject[1], (Rect)arrobject[2]);
        }

        @Override
        public /* varargs */ void handleOnOrientationChanged(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_ORIENTATION_CHANGED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    class StateVideoCaptureWhileRecording
    extends State {
        private final boolean mIsPaused;
        private boolean mIsReturnToVideoRecordingRequired;

        public StateVideoCaptureWhileRecording(boolean bl) {
            this.mIsReturnToVideoRecordingRequired = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_CAPTURE_WHILE_RECORDING;
            this.mIsPaused = bl;
        }

        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
            StateMachine.this.mChapterThumbnail = new ChapterThumbnail((byte[])arrobject[0], (Integer)arrobject[1], (Rect)arrobject[2]);
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            StateMachine.this.doHandleRecordingError();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        @Override
        public /* varargs */ void handleOnShutterDone(Object ... arrobject) {
            PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
            void var4_3 = this;
            synchronized (var4_3) {
                if (StateMachine.this.mContentsViewController != null) {
                    StateMachine.this.mContentsViewController.setClickThumbnailProgressListener(null);
                    photoSavingRequest.setRequestId(StateMachine.this.mContentsViewController.createClearContentFrame());
                } else {
                    photoSavingRequest.setRequestId(-1);
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
            if (this.mIsPaused) {
                StateMachine.this.changeTo(new StateVideoRecordingPausing(), arrobject);
            } else {
                StateMachine.this.changeTo(new StateVideoRecording(StateMachine.this, this.mIsReturnToVideoRecordingRequired), arrobject);
            }
            if (this.mIsReturnToVideoRecordingRequired) {
                StateMachine.this.doStopRecording();
            }
        }

        @Override
        public /* varargs */ void handleOnTakePictureDone(Object ... arrobject) {
            PhotoSavingRequest photoSavingRequest = (PhotoSavingRequest)arrobject[0];
            StateMachine.this.requestStorePicture(photoSavingRequest);
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SET_EARLY_THUMBNAIL;
            Object[] arrobject2 = new Object[]{ThumbnailUtil.createThumbnailViewFromJpeg((Activity)StateMachine.this.mActivity, photoSavingRequest.getImageData(), photoSavingRequest.common.orientation)};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            BaseFastViewFinder baseFastViewFinder2 = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent2 = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
            Object[] arrobject3 = new Object[]{photoSavingRequest.getRequestId()};
            baseFastViewFinder2.sendViewUpdateEvent(viewUpdateEvent2, arrobject3);
        }

        @Override
        public /* varargs */ void handleOnVideoRecordingDone(Object ... arrobject) {
            StateMachine.this.changeTo(new StateVideoStore(), arrobject);
            VideoSavingRequest videoSavingRequest = (VideoSavingRequest)arrobject[0];
            StateMachine.this.requestStoreVideo(videoSavingRequest);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopRecording();
            StateMachine.this.mCameraDeviceHandler.finalizeRecording();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            StateMachine.this.doStopRecording();
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
            this.mIsReturnToVideoRecordingRequired = true;
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    class StateVideoRecording
    extends StateVideoBase {
        private static final String TAG = "StateMachine.StateVideoRecording";
        private boolean mAlreadyRequestStop;
        final /* synthetic */ StateMachine this$0;

        public StateVideoRecording(StateMachine stateMachine) {
            this.this$0 = stateMachine;
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            stateMachine.sendVideoChapterThumbnailToViewFinder();
        }

        public StateVideoRecording(StateMachine stateMachine, boolean bl) {
            this.this$0 = stateMachine;
            this.mAlreadyRequestStop = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING;
            stateMachine.sendVideoChapterThumbnailToViewFinder();
            this.mAlreadyRequestStop = bl;
        }

        @Override
        public /* varargs */ void handleCamcordButtonRelease(Object ... arrobject) {
            this.mAlreadyRequestStop = true;
            this.this$0.doPauseRecording();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            if (this.mAlreadyRequestStop || arrobject[0] == ControllerEventSource.TOUCH_FACE || this.this$0.mCameraDeviceHandler.getVideoSize() == VideoSize.MMS) {
                return;
            }
            this.this$0.changeTo(new StateVideoCaptureWhileRecording(this.isPaused()), new Object[0]);
            this.mAlreadyRequestStop = true;
            this.this$0.doCaptureWhileRecording();
        }

        @Override
        public /* varargs */ void handleChangeSelectedFace(Object ... arrobject) {
            Point point = (Point)arrobject[0];
            this.this$0.doChangeSelectedFace(point);
        }

        @Override
        public /* varargs */ void handleDeselectObjectPosition(Object ... arrobject) {
            this.this$0.doDeselectObject();
        }

        @Override
        public /* varargs */ void handleKeyBack(Object ... arrobject) {
            this.mAlreadyRequestStop = true;
            this.this$0.doStopRecording();
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (this.mAlreadyRequestStop) {
                return;
            }
            if (this.this$0.isInModeLessRecording()) {
                this.this$0.changeTo(new StateVideoCaptureWhileRecording(this.isPaused()), new Object[0]);
                this.this$0.doCaptureWhileRecording();
                return;
            }
            this.this$0.doStopRecording();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleKeyZoomInDown(Object ... arrobject) {
            if (this.mAlreadyRequestStop || this.this$0.mCameraDeviceHandler.getLatestCachedParameters() == null || !this.this$0.isSmoothZoomEnabled()) {
                return;
            }
            this.this$0.changeTo(new StateVideoZoomingWhileRecording(this.isPaused()), arrobject);
            this.this$0.doZoomIn();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handleKeyZoomOutDown(Object ... arrobject) {
            if (this.mAlreadyRequestStop || this.this$0.mCameraDeviceHandler.getLatestCachedParameters() == null || !this.this$0.isSmoothZoomEnabled()) {
                return;
            }
            this.this$0.changeTo(new StateVideoZoomingWhileRecording(this.isPaused()), arrobject);
            this.this$0.doZoomOut();
        }

        @Override
        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
            super.handleOnOnePreviewFrameUpdated(arrobject);
            this.this$0.sendVideoChapterThumbnailToViewFinder();
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            this.this$0.doHandleRecordingError();
        }

        @Override
        public /* varargs */ void handleOnSceneModeChanged(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnVideoRecordingDone(Object ... arrobject) {
            this.this$0.changeTo(new StateVideoStore(), arrobject);
            VideoSavingRequest videoSavingRequest = (VideoSavingRequest)arrobject[0];
            this.this$0.requestStoreVideo(videoSavingRequest);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            this.mAlreadyRequestStop = true;
            this.this$0.doStopRecording();
            this.this$0.mCameraDeviceHandler.finalizeRecording();
            this.this$0.changeTo(new StatePause(), arrobject);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public /* varargs */ void handlePrepareTouchZoom(Object ... arrobject) {
            if (this.mAlreadyRequestStop || !this.this$0.isSmoothZoomEnabled()) {
                return;
            }
            this.this$0.changeTo(new StateVideoZoomingInTouchWhileRecording(this.isPaused()), arrobject);
        }

        @Override
        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = this.this$0.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            this.this$0.sendVideoChapterThumbnailToViewFinder();
        }

        @Override
        public /* varargs */ void handleSetSelectedObjectPosition(Object ... arrobject) {
            PointF pointF = (PointF)arrobject[1];
            this.this$0.doSelectObject(pointF);
        }

        @Override
        public /* varargs */ void handleStorageError(Object ... arrobject) {
            this.mAlreadyRequestStop = true;
            this.this$0.doStopRecording();
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
            this.mAlreadyRequestStop = true;
            this.this$0.doStopRecording();
        }

        protected boolean isPaused() {
            return false;
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    class StateVideoRecordingPausing
    extends StateVideoRecording {
        public StateVideoRecordingPausing() {
            super(StateMachine.this);
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING_PAUSING;
            StateMachine.this.mCameraDeviceHandler.startPreview();
        }

        @Override
        public /* varargs */ void handleCamcordButtonRelease(Object ... arrobject) {
            StateMachine.this.doResumeRecording();
            StateMachine.this.mCameraDeviceHandler.requestOnePreviewFrame();
        }

        @Override
        protected boolean isPaused() {
            return true;
        }
    }

    class StateVideoRecordingStarting
    extends StateVideoBase {
        private boolean mIsImmediatelyRecordingPauseRequired;
        private boolean mIsImmediatelyRecordingStopRequired;
        private boolean mShouldRequestChapterThumbnail;

        public StateVideoRecordingStarting(boolean bl) {
            this.mIsImmediatelyRecordingStopRequired = false;
            this.mIsImmediatelyRecordingPauseRequired = false;
            this.mCaptureState = CaptureState.STATE_VIDEO_RECORDING_STARTING;
            StateMachine.this.mIsInModeLessRecording = true;
            this.mShouldRequestChapterThumbnail = ClassDefinitionChecker.isMediaRecorderPauseAndResumeSupported();
            if (StateMachine.this.mActivity != null) {
                StateMachine.this.mActivity.disableAutoOffTimer();
            }
            StateMachine.this.mChapterThumbnail = null;
            if (!bl && this.mShouldRequestChapterThumbnail) {
                StateMachine.this.mCameraDeviceHandler.requestOnePreviewFrame();
            }
            StateMachine.this.mHandler.postDelayed((Runnable)new Runnable(StateMachine.this){
                final /* synthetic */ StateMachine val$this$0;

                public void run() {
                    StateMachine.this.sendEvent(TransitterEvent.EVENT_ON_RECORDING_START_WAIT_DONE, new Object[0]);
                }
            }, 1600);
        }

        private boolean isPaused() {
            return false;
        }

        @Override
        public /* varargs */ void handleCamcordButtonRelease(Object ... arrobject) {
            this.mIsImmediatelyRecordingPauseRequired = true;
            this.mIsImmediatelyRecordingStopRequired = false;
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            if (!(StateMachine.this.mCameraDeviceHandler.getVideoSize() == VideoSize.MMS || this.mIsImmediatelyRecordingStopRequired)) {
                StateMachine.this.changeTo(new StateVideoCaptureWhileRecording(super.isPaused()), new Object[0]);
                StateMachine.this.doCaptureWhileRecording();
            }
        }

        @Override
        public /* varargs */ void handleKeyBack(Object ... arrobject) {
            this.mIsImmediatelyRecordingStopRequired = true;
        }

        @Override
        public /* varargs */ void handleKeyCaptureDown(Object ... arrobject) {
            if (StateMachine.this.isInModeLessRecording()) {
                StateMachine.this.changeTo(new StateVideoCaptureWhileRecording(super.isPaused()), new Object[0]);
                StateMachine.this.doCaptureWhileRecording();
                return;
            }
            this.mIsImmediatelyRecordingStopRequired = true;
        }

        @Override
        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
            super.handleOnOnePreviewFrameUpdated(arrobject);
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            StateMachine.this.doHandleRecordingError();
        }

        @Override
        public /* varargs */ void handleOnRecordingStartWaitDone(Object ... arrobject) {
            if (this.mIsImmediatelyRecordingStopRequired) {
                StateMachine.this.changeTo(new StateVideoRecording(StateMachine.this, true), arrobject);
                StateMachine.this.doStopRecording();
                return;
            }
            if (this.mIsImmediatelyRecordingPauseRequired) {
                StateMachine.this.doPauseRecording();
                StateMachine.this.changeTo(new StateVideoRecordingPausing(), arrobject);
                return;
            }
            StateMachine.this.changeTo(new StateVideoRecording(StateMachine.this), arrobject);
        }

        @Override
        public /* varargs */ void handleOnSceneModeChanged(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_DETECTED_SCENE_CHANGED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnVideoRecordingDone(Object ... arrobject) {
            StateMachine.this.changeTo(new StateVideoStore(), arrobject);
            VideoSavingRequest videoSavingRequest = (VideoSavingRequest)arrobject[0];
            StateMachine.this.requestStoreVideo(videoSavingRequest);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopRecording();
            StateMachine.this.mCameraDeviceHandler.finalizeRecording();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
            StateMachine.this.sendVideoChapterThumbnailToViewFinder();
        }

        @Override
        public /* varargs */ void handleSubCamcordButtonRelease(Object ... arrobject) {
            this.mIsImmediatelyRecordingStopRequired = true;
            this.mIsImmediatelyRecordingPauseRequired = false;
        }

        @Override
        public boolean isRecording() {
            return true;
        }

    }

    class StateVideoRecordingStartingFromFastestCamcord
    extends StateVideoRecordingStarting {
        public StateVideoRecordingStartingFromFastestCamcord(boolean bl) {
            super(bl);
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            StateMachine.this.changeTo(new StateWarning(), new Object[0]);
        }
    }

    class StateVideoStore
    extends StateVideoBase {
        public StateVideoStore() {
            this.mCaptureState = CaptureState.STATE_VIDEO_STORE;
        }

        @Override
        public /* varargs */ void handleOnOnePreviewFrameUpdated(Object ... arrobject) {
        }

        @Override
        public /* varargs */ void handleOnStoreRequested(Object ... arrobject) {
            StateMachine.this.changeCameraModeTo(1, true, false);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }
    }

    class StateVideoZoomingBase
    extends StateVideoBase {
        public StateVideoZoomingBase() {
            this.mCaptureState = CaptureState.STATE_VIDEO_ZOOMING_BASE;
        }

        @Override
        public /* varargs */ void handleOnFaceDetected(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_FACE_DETECTED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleOnObjectTracked(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
            Object[] arrobject2 = new Object[]{arrobject[0]};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }
    }

    class StateVideoZoomingInTouchWhileRecording
    extends StateVideoZoomingBase {
        final boolean mIsPaused;
        int mStartZoomStep;

        public StateVideoZoomingInTouchWhileRecording(boolean bl) {
            this.mCaptureState = CaptureState.STATE_VIDEO_ZOOMING_IN_TOUCH_WHILE_RECORDING;
            this.mIsPaused = bl;
            this.mStartZoomStep = StateMachine.this.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
            StateMachine.this.mCurrentZoomLength = StateMachine.this.mCameraDeviceHandler.getLatestCachedParameters().getZoom();
        }

        @Override
        public /* varargs */ void handleCancelTouchZoom(Object ... arrobject) {
            if (this.mIsPaused) {
                StateMachine.this.changeTo(new StateVideoRecordingPausing(), arrobject);
                return;
            }
            StateMachine.this.changeTo(new StateVideoRecording(StateMachine.this), arrobject);
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            StateMachine.this.doHandleRecordingError();
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopRecording();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleScreenClear(Object ... arrobject) {
            this.handleCaptureButtonCancel(arrobject);
        }

        @Override
        public /* varargs */ void handleStartTouchZoom(Object ... arrobject) {
            float f = ((Float)arrobject[0]).floatValue();
            StateMachine.this.doZoom(this.mStartZoomStep, f);
        }

        @Override
        public /* varargs */ void handleStopTouchZoom(Object ... arrobject) {
            StateMachine.this.doStopZoom();
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    class StateVideoZoomingWhileRecording
    extends StateVideoZoomingBase {
        private final boolean mIsPaused;

        public StateVideoZoomingWhileRecording(boolean bl) {
            this.mCaptureState = CaptureState.STATE_VIDEO_ZOOMING_WHILE_RECORDING;
            this.mIsPaused = bl;
        }

        @Override
        public /* varargs */ void handleKeyZoomUp(Object ... arrobject) {
            StateMachine.this.doStopZoom();
            if (this.mIsPaused) {
                StateMachine stateMachine = StateMachine.this;
                StateVideoRecordingPausing stateVideoRecordingPausing = new StateVideoRecordingPausing();
                Object[] arrobject2 = new Object[]{BaseFastViewFinder.UiComponentKind.ZOOM_BAR};
                stateMachine.changeTo(stateVideoRecordingPausing, arrobject2);
                return;
            }
            StateMachine stateMachine = StateMachine.this;
            StateVideoRecording stateVideoRecording = new StateVideoRecording(StateMachine.this);
            Object[] arrobject3 = new Object[]{BaseFastViewFinder.UiComponentKind.ZOOM_BAR};
            stateMachine.changeTo(stateVideoRecording, arrobject3);
        }

        @Override
        public /* varargs */ void handleOnRecordingError(Object ... arrobject) {
            StateMachine.this.doHandleRecordingError();
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.doStopRecording();
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    class StateWarning
    extends StatePhotoBase {
        public StateWarning() {
            StateMachine.this.mIsInModeLessRecording = false;
            this.mCaptureState = CaptureState.STATE_WARNING;
        }

        @Override
        public /* varargs */ void handleCaptureButtonRelease(Object ... arrobject) {
            StateMachine.this.mViewFinder.onCaptureDone();
        }

        @Override
        public /* varargs */ void handleDialogOpened(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePhotoStandbyDialog(), arrobject);
            StateMachine.this.mActivity.notifyStateBlockedToWearable();
        }

        @Override
        public /* varargs */ void handleKeyMenu(Object ... arrobject) {
            StateMachine.this.mActivity.requestLaunchAdvancedCameraApplication(1);
        }

        @Override
        public /* varargs */ void handlePause(Object ... arrobject) {
            StateMachine.this.changeTo(new StatePause(), arrobject);
        }

        @Override
        public /* varargs */ void handleRequestSetupHeadUpDisplay(Object ... arrobject) {
            BaseFastViewFinder baseFastViewFinder = StateMachine.this.mViewFinder;
            BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent = BaseFastViewFinder.ViewUpdateEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
            Object[] arrobject2 = new Object[]{BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY};
            baseFastViewFinder.sendViewUpdateEvent(viewUpdateEvent, arrobject2);
        }

        @Override
        public /* varargs */ void handleStorageMounted(Object ... arrobject) {
            StateMachine.this.changeTo(new StateStandby(StateMachine.this), arrobject);
        }

        @Override
        public /* varargs */ void handleStorageShouldChange(Object ... arrobject) {
            StateMachine.this.switchStorage();
        }

        @Override
        public /* varargs */ void handleSwitchCamera(Object ... arrobject) {
            StateMachine.this.switchCamera();
        }

        @Override
        public boolean isMenuAvailable() {
            return true;
        }
    }

    public static final class StaticEvent
    extends Enum<StaticEvent> {
        private static final /* synthetic */ StaticEvent[] $VALUES;
        public static final /* enum */ StaticEvent EVENT_ON_FACE_DETECTED;
        public static final /* enum */ StaticEvent EVENT_ON_FACE_IDENTEFIED;
        public static final /* enum */ StaticEvent EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED;
        public static final /* enum */ StaticEvent EVENT_ON_LAZY_INITIALIZATION_TASK_RUN;
        public static final /* enum */ StaticEvent EVENT_ON_OBJECT_TRACKED;
        public static final /* enum */ StaticEvent EVENT_ON_ORIENTATION_CHANGED;
        public static final /* enum */ StaticEvent EVENT_ON_PHOTO_STACK_INITIALIZED;
        public static final /* enum */ StaticEvent EVENT_ON_SCENE_MODE_CHANGED;

        static {
            EVENT_ON_PHOTO_STACK_INITIALIZED = new StaticEvent();
            EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED = new StaticEvent();
            EVENT_ON_SCENE_MODE_CHANGED = new StaticEvent();
            EVENT_ON_FACE_DETECTED = new StaticEvent();
            EVENT_ON_FACE_IDENTEFIED = new StaticEvent();
            EVENT_ON_OBJECT_TRACKED = new StaticEvent();
            EVENT_ON_ORIENTATION_CHANGED = new StaticEvent();
            EVENT_ON_LAZY_INITIALIZATION_TASK_RUN = new StaticEvent();
            StaticEvent[] arrstaticEvent = new StaticEvent[]{EVENT_ON_PHOTO_STACK_INITIALIZED, EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, EVENT_ON_SCENE_MODE_CHANGED, EVENT_ON_FACE_DETECTED, EVENT_ON_FACE_IDENTEFIED, EVENT_ON_OBJECT_TRACKED, EVENT_ON_ORIENTATION_CHANGED, EVENT_ON_LAZY_INITIALIZATION_TASK_RUN};
            $VALUES = arrstaticEvent;
        }

        private StaticEvent() {
            super(string, n);
        }

        public static StaticEvent valueOf(String string) {
            return (StaticEvent)Enum.valueOf((Class)StaticEvent.class, (String)string);
        }

        public static StaticEvent[] values() {
            return (StaticEvent[])$VALUES.clone();
        }
    }

    public static final class TouchEventSource
    extends Enum<TouchEventSource> {
        private static final /* synthetic */ TouchEventSource[] $VALUES;
        public static final /* enum */ TouchEventSource CAPTURE_AREA;
        public static final /* enum */ TouchEventSource FACE;
        public static final /* enum */ TouchEventSource PHOTO_BUTTON;
        public static final /* enum */ TouchEventSource UNKNOWN;
        public static final /* enum */ TouchEventSource VIDEO_BUTTON;

        static {
            UNKNOWN = new TouchEventSource();
            CAPTURE_AREA = new TouchEventSource();
            FACE = new TouchEventSource();
            PHOTO_BUTTON = new TouchEventSource();
            VIDEO_BUTTON = new TouchEventSource();
            TouchEventSource[] arrtouchEventSource = new TouchEventSource[]{UNKNOWN, CAPTURE_AREA, FACE, PHOTO_BUTTON, VIDEO_BUTTON};
            $VALUES = arrtouchEventSource;
        }

        private TouchEventSource() {
            super(string, n);
        }

        public static TouchEventSource valueOf(String string) {
            return (TouchEventSource)Enum.valueOf((Class)TouchEventSource.class, (String)string);
        }

        public static TouchEventSource[] values() {
            return (TouchEventSource[])$VALUES.clone();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class TrackedObjectLostTimeoutTask
    implements Runnable {
        final /* synthetic */ StateMachine this$0;

        private TrackedObjectLostTimeoutTask(StateMachine stateMachine) {
            this.this$0 = stateMachine;
        }

        /* synthetic */ TrackedObjectLostTimeoutTask(StateMachine stateMachine,  var2_2) {
            super(stateMachine);
        }

        public void run() {
            this.this$0.doDeselectObject();
        }
    }

    public static final class TransitterEvent
    extends Enum<TransitterEvent> {
        private static final /* synthetic */ TransitterEvent[] $VALUES;
        public static final /* enum */ TransitterEvent EVENT_CAMCORD_BUTTON_RELEASE;
        public static final /* enum */ TransitterEvent EVENT_CANCEL_TOUCH_ZOOM;
        public static final /* enum */ TransitterEvent EVENT_CAPTURE_BUTTON_CANCEL;
        public static final /* enum */ TransitterEvent EVENT_CAPTURE_BUTTON_LONG_PRESS;
        public static final /* enum */ TransitterEvent EVENT_CAPTURE_BUTTON_RELEASE;
        public static final /* enum */ TransitterEvent EVENT_CAPTURE_BUTTON_TOUCH;
        public static final /* enum */ TransitterEvent EVENT_CHANGE_SELECTED_FACE;
        public static final /* enum */ TransitterEvent EVENT_DESELECT_OBJECT_POSITION;
        public static final /* enum */ TransitterEvent EVENT_DIALOG_CLOSED;
        public static final /* enum */ TransitterEvent EVENT_DIALOG_OPENED;
        public static final /* enum */ TransitterEvent EVENT_FINALIZE;
        public static final /* enum */ TransitterEvent EVENT_INITIALIZE;
        public static final /* enum */ TransitterEvent EVENT_KEY_BACK;
        public static final /* enum */ TransitterEvent EVENT_KEY_CAPTURE_DOWN;
        public static final /* enum */ TransitterEvent EVENT_KEY_CAPTURE_UP;
        public static final /* enum */ TransitterEvent EVENT_KEY_FOCUS_DOWN;
        public static final /* enum */ TransitterEvent EVENT_KEY_FOCUS_UP;
        public static final /* enum */ TransitterEvent EVENT_KEY_MENU;
        public static final /* enum */ TransitterEvent EVENT_KEY_ZOOM_IN_DOWN;
        public static final /* enum */ TransitterEvent EVENT_KEY_ZOOM_OUT_DOWN;
        public static final /* enum */ TransitterEvent EVENT_KEY_ZOOM_UP;
        public static final /* enum */ TransitterEvent EVENT_ON_AUTO_FOCUS_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_BURST_GROUP_STORE_COMPLETED;
        public static final /* enum */ TransitterEvent EVENT_ON_BURST_SHUTTER_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_BURST_STORE_COMPLETED;
        public static final /* enum */ TransitterEvent EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED;
        public static final /* enum */ TransitterEvent EVENT_ON_DEVICE_ERROR;
        public static final /* enum */ TransitterEvent EVENT_ON_EVF_PREPARATION_FAILED;
        public static final /* enum */ TransitterEvent EVENT_ON_EVF_PREPARED;
        public static final /* enum */ TransitterEvent EVENT_ON_INITIAL_AUTO_FOCUS_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_ONE_PREVIEW_FRAME_UPDATED;
        public static final /* enum */ TransitterEvent EVENT_ON_PREPARE_TOUCH_ZOOM_TIMED_OUT;
        public static final /* enum */ TransitterEvent EVENT_ON_PRE_SHUTTER_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_PRE_TAKE_PICTURE_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_RECORDING_ERROR;
        public static final /* enum */ TransitterEvent EVENT_ON_RECORDING_START_WAIT_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_SHUTTER_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_STORE_COMPLETED;
        public static final /* enum */ TransitterEvent EVENT_ON_STORE_REQUESTED;
        public static final /* enum */ TransitterEvent EVENT_ON_SWITCH_CAMERA;
        public static final /* enum */ TransitterEvent EVENT_ON_TAKE_PICTURE_DONE;
        public static final /* enum */ TransitterEvent EVENT_ON_VIDEO_RECORDING_DONE;
        public static final /* enum */ TransitterEvent EVENT_PAUSE;
        public static final /* enum */ TransitterEvent EVENT_PREPARE_TOUCH_ZOOM;
        public static final /* enum */ TransitterEvent EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
        public static final /* enum */ TransitterEvent EVENT_RESUME;
        public static final /* enum */ TransitterEvent EVENT_RESUME_TIMEOUT;
        public static final /* enum */ TransitterEvent EVENT_SCREEN_CLEAR;
        public static final /* enum */ TransitterEvent EVENT_SET_FOCUS_POSITION;
        public static final /* enum */ TransitterEvent EVENT_SET_SELECTED_OBJECT_POSITION;
        public static final /* enum */ TransitterEvent EVENT_START_AF_AFTER_OBJECT_TRACKED;
        public static final /* enum */ TransitterEvent EVENT_START_AF_SEARCH_IN_TOUCH;
        public static final /* enum */ TransitterEvent EVENT_START_AF_SEARCH_IN_TOUCH_STOP;
        public static final /* enum */ TransitterEvent EVENT_START_TOUCH_ZOOM;
        public static final /* enum */ TransitterEvent EVENT_STOP_TOUCH_ZOOM;
        public static final /* enum */ TransitterEvent EVENT_STORAGE_ERROR;
        public static final /* enum */ TransitterEvent EVENT_STORAGE_MOUNTED;
        public static final /* enum */ TransitterEvent EVENT_STORAGE_SHOULD_CHANGE;
        public static final /* enum */ TransitterEvent EVENT_SUB_CAMCORD_BUTTON_CANCEL;
        public static final /* enum */ TransitterEvent EVENT_SUB_CAMCORD_BUTTON_RELEASE;
        public static final /* enum */ TransitterEvent EVENT_SUB_CAMCORD_BUTTON_TOUCH;
        public static final /* enum */ TransitterEvent EVENT_TOUCH_CONTENT_PROGRESS;

        static {
            EVENT_INITIALIZE = new TransitterEvent();
            EVENT_RESUME = new TransitterEvent();
            EVENT_RESUME_TIMEOUT = new TransitterEvent();
            EVENT_PAUSE = new TransitterEvent();
            EVENT_FINALIZE = new TransitterEvent();
            EVENT_ON_EVF_PREPARED = new TransitterEvent();
            EVENT_ON_EVF_PREPARATION_FAILED = new TransitterEvent();
            EVENT_ON_INITIAL_AUTO_FOCUS_DONE = new TransitterEvent();
            EVENT_ON_AUTO_FOCUS_DONE = new TransitterEvent();
            EVENT_ON_PRE_SHUTTER_DONE = new TransitterEvent();
            EVENT_ON_SHUTTER_DONE = new TransitterEvent();
            EVENT_ON_PRE_TAKE_PICTURE_DONE = new TransitterEvent();
            EVENT_ON_TAKE_PICTURE_DONE = new TransitterEvent();
            EVENT_ON_VIDEO_RECORDING_DONE = new TransitterEvent();
            EVENT_ON_DEVICE_ERROR = new TransitterEvent();
            EVENT_ON_ONE_PREVIEW_FRAME_UPDATED = new TransitterEvent();
            EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED = new TransitterEvent();
            EVENT_ON_SWITCH_CAMERA = new TransitterEvent();
            EVENT_ON_BURST_SHUTTER_DONE = new TransitterEvent();
            EVENT_ON_BURST_STORE_COMPLETED = new TransitterEvent();
            EVENT_ON_BURST_GROUP_STORE_COMPLETED = new TransitterEvent();
            EVENT_ON_STORE_REQUESTED = new TransitterEvent();
            EVENT_ON_STORE_COMPLETED = new TransitterEvent();
            EVENT_STORAGE_ERROR = new TransitterEvent();
            EVENT_STORAGE_MOUNTED = new TransitterEvent();
            EVENT_STORAGE_SHOULD_CHANGE = new TransitterEvent();
            EVENT_KEY_FOCUS_DOWN = new TransitterEvent();
            EVENT_KEY_FOCUS_UP = new TransitterEvent();
            EVENT_KEY_CAPTURE_DOWN = new TransitterEvent();
            EVENT_KEY_CAPTURE_UP = new TransitterEvent();
            EVENT_KEY_ZOOM_IN_DOWN = new TransitterEvent();
            EVENT_KEY_ZOOM_OUT_DOWN = new TransitterEvent();
            EVENT_KEY_ZOOM_UP = new TransitterEvent();
            EVENT_KEY_MENU = new TransitterEvent();
            EVENT_KEY_BACK = new TransitterEvent();
            EVENT_PREPARE_TOUCH_ZOOM = new TransitterEvent();
            EVENT_ON_PREPARE_TOUCH_ZOOM_TIMED_OUT = new TransitterEvent();
            EVENT_START_TOUCH_ZOOM = new TransitterEvent();
            EVENT_STOP_TOUCH_ZOOM = new TransitterEvent();
            EVENT_CANCEL_TOUCH_ZOOM = new TransitterEvent();
            EVENT_CAPTURE_BUTTON_TOUCH = new TransitterEvent();
            EVENT_CAPTURE_BUTTON_RELEASE = new TransitterEvent();
            EVENT_CAPTURE_BUTTON_CANCEL = new TransitterEvent();
            EVENT_CAPTURE_BUTTON_LONG_PRESS = new TransitterEvent();
            EVENT_START_AF_SEARCH_IN_TOUCH = new TransitterEvent();
            EVENT_START_AF_SEARCH_IN_TOUCH_STOP = new TransitterEvent();
            EVENT_SET_FOCUS_POSITION = new TransitterEvent();
            EVENT_CAMCORD_BUTTON_RELEASE = new TransitterEvent();
            EVENT_SUB_CAMCORD_BUTTON_TOUCH = new TransitterEvent();
            EVENT_SUB_CAMCORD_BUTTON_RELEASE = new TransitterEvent();
            EVENT_SUB_CAMCORD_BUTTON_CANCEL = new TransitterEvent();
            EVENT_CHANGE_SELECTED_FACE = new TransitterEvent();
            EVENT_SET_SELECTED_OBJECT_POSITION = new TransitterEvent();
            EVENT_DESELECT_OBJECT_POSITION = new TransitterEvent();
            EVENT_START_AF_AFTER_OBJECT_TRACKED = new TransitterEvent();
            EVENT_SCREEN_CLEAR = new TransitterEvent();
            EVENT_TOUCH_CONTENT_PROGRESS = new TransitterEvent();
            EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY = new TransitterEvent();
            EVENT_ON_RECORDING_START_WAIT_DONE = new TransitterEvent();
            EVENT_ON_RECORDING_ERROR = new TransitterEvent();
            EVENT_DIALOG_OPENED = new TransitterEvent();
            EVENT_DIALOG_CLOSED = new TransitterEvent();
            TransitterEvent[] arrtransitterEvent = new TransitterEvent[]{EVENT_INITIALIZE, EVENT_RESUME, EVENT_RESUME_TIMEOUT, EVENT_PAUSE, EVENT_FINALIZE, EVENT_ON_EVF_PREPARED, EVENT_ON_EVF_PREPARATION_FAILED, EVENT_ON_INITIAL_AUTO_FOCUS_DONE, EVENT_ON_AUTO_FOCUS_DONE, EVENT_ON_PRE_SHUTTER_DONE, EVENT_ON_SHUTTER_DONE, EVENT_ON_PRE_TAKE_PICTURE_DONE, EVENT_ON_TAKE_PICTURE_DONE, EVENT_ON_VIDEO_RECORDING_DONE, EVENT_ON_DEVICE_ERROR, EVENT_ON_ONE_PREVIEW_FRAME_UPDATED, EVENT_ON_CONTINUOUS_PREVIEW_FRAME_UPDATED, EVENT_ON_SWITCH_CAMERA, EVENT_ON_BURST_SHUTTER_DONE, EVENT_ON_BURST_STORE_COMPLETED, EVENT_ON_BURST_GROUP_STORE_COMPLETED, EVENT_ON_STORE_REQUESTED, EVENT_ON_STORE_COMPLETED, EVENT_STORAGE_ERROR, EVENT_STORAGE_MOUNTED, EVENT_STORAGE_SHOULD_CHANGE, EVENT_KEY_FOCUS_DOWN, EVENT_KEY_FOCUS_UP, EVENT_KEY_CAPTURE_DOWN, EVENT_KEY_CAPTURE_UP, EVENT_KEY_ZOOM_IN_DOWN, EVENT_KEY_ZOOM_OUT_DOWN, EVENT_KEY_ZOOM_UP, EVENT_KEY_MENU, EVENT_KEY_BACK, EVENT_PREPARE_TOUCH_ZOOM, EVENT_ON_PREPARE_TOUCH_ZOOM_TIMED_OUT, EVENT_START_TOUCH_ZOOM, EVENT_STOP_TOUCH_ZOOM, EVENT_CANCEL_TOUCH_ZOOM, EVENT_CAPTURE_BUTTON_TOUCH, EVENT_CAPTURE_BUTTON_RELEASE, EVENT_CAPTURE_BUTTON_CANCEL, EVENT_CAPTURE_BUTTON_LONG_PRESS, EVENT_START_AF_SEARCH_IN_TOUCH, EVENT_START_AF_SEARCH_IN_TOUCH_STOP, EVENT_SET_FOCUS_POSITION, EVENT_CAMCORD_BUTTON_RELEASE, EVENT_SUB_CAMCORD_BUTTON_TOUCH, EVENT_SUB_CAMCORD_BUTTON_RELEASE, EVENT_SUB_CAMCORD_BUTTON_CANCEL, EVENT_CHANGE_SELECTED_FACE, EVENT_SET_SELECTED_OBJECT_POSITION, EVENT_DESELECT_OBJECT_POSITION, EVENT_START_AF_AFTER_OBJECT_TRACKED, EVENT_SCREEN_CLEAR, EVENT_TOUCH_CONTENT_PROGRESS, EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, EVENT_ON_RECORDING_START_WAIT_DONE, EVENT_ON_RECORDING_ERROR, EVENT_DIALOG_OPENED, EVENT_DIALOG_CLOSED};
            $VALUES = arrtransitterEvent;
        }

        private TransitterEvent() {
            super(string, n);
        }

        public static TransitterEvent valueOf(String string) {
            return (TransitterEvent)Enum.valueOf((Class)TransitterEvent.class, (String)string);
        }

        public static TransitterEvent[] values() {
            return (TransitterEvent[])$VALUES.clone();
        }
    }

}

