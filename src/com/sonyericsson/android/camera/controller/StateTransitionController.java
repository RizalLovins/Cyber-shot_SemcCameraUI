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
 *  android.graphics.Rect
 *  android.net.Uri
 *  android.os.Handler
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionCallback
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 */
package com.sonyericsson.android.camera.controller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.view.animation.Animation;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.LaunchConditions;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.controller.AutoZoom;
import com.sonyericsson.android.camera.controller.BurstShooting;
import com.sonyericsson.android.camera.controller.CameraFunctions;
import com.sonyericsson.android.camera.controller.ControllerEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.ControllerMessage;
import com.sonyericsson.android.camera.controller.EventAction;
import com.sonyericsson.android.camera.controller.EventDispatcher;
import com.sonyericsson.android.camera.controller.Executor;
import com.sonyericsson.android.camera.controller.FaceDetect;
import com.sonyericsson.android.camera.controller.Inactive;
import com.sonyericsson.android.camera.controller.ObjectTracking;
import com.sonyericsson.android.camera.controller.SceneRecognition;
import com.sonyericsson.android.camera.controller.Selftimer;
import com.sonyericsson.android.camera.controller.Shooting;
import com.sonyericsson.android.camera.controller.State;
import com.sonyericsson.android.camera.controller.TouchFocus;
import com.sonyericsson.android.camera.controller.VideoDevice;
import com.sonyericsson.android.camera.controller.ZoomDirection;
import com.sonyericsson.android.camera.device.AutoFocusListener;
import com.sonyericsson.android.camera.device.CameraDevice;
import com.sonyericsson.android.camera.device.CameraSurfaceHolder;
import com.sonyericsson.android.camera.mediasaving.BurstSavingTaskManager;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.view.CameraWindow;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.focusview.RectangleColor;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.DcfPathBuilder;
import com.sonyericsson.cameracommon.mediasaving.SavingTaskManager;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.PhotoSavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.VideoSavingRequest;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.AutoReviewWindow;
import com.sonyericsson.cameracommon.rotatableview.RotatableDialog;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.RecordingUtil;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.media.utility.AudioResourceChecker;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.NotifyWearableInterface;
import com.sonymobile.cameracommon.vanilla.wearablebridge.handheld.client.WearableBridgeClient;
import java.util.List;

public class StateTransitionController
extends CameraFunctions {
    private static final String TAG = StateTransitionController.class.getSimpleName();
    private SavingRequest mLastSavingRequest = null;

    public StateTransitionController(CameraActivity cameraActivity, EventDispatcher eventDispatcher) {
        super(cameraActivity, eventDispatcher);
        this.registerStates();
    }

    private void launchEditor(StoreDataResult storeDataResult) {
        if (!AutoReviewWindow.isEditorAvailable((Context)this.mCameraActivity, storeDataResult.uri, storeDataResult.savingRequest.common.mimeType)) {
            Executor.setState(this.getViewFinderState());
            return;
        }
        this.mCameraWindow.openReviewWindow(storeDataResult.uri, storeDataResult.savingRequest);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean notifyStoreDone(StoreDataResult storeDataResult) {
        if (storeDataResult == null || storeDataResult.savingRequest == null || this.mLastSavingRequest == null || storeDataResult.savingRequest.getRequestId() != this.mLastSavingRequest.getRequestId()) {
            return false;
        }
        this.mCameraWindow.requestShowIconsOnReviewWindow(storeDataResult);
        return true;
    }

    private boolean shouldRestartPreview(ControllerMessage controllerMessage) {
        if (controllerMessage != null && controllerMessage.isAbortEvent()) {
            return false;
        }
        return this.shouldRestartPreview(this.mCameraDevice.getPhotoPreviewSize(this.getParams().getResolution()));
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean storeImage(byte[] arrby) {
        SavingRequest savingRequest = this.mShooting.storePicture(arrby);
        if (savingRequest != null && savingRequest.common.savedFileType != SavingTaskManager.SavedFileType.PHOTO_DURING_REC) {
            this.mLastSavingRequest = savingRequest;
        }
        if (this.mCameraActivity.isOneShot()) {
            return false;
        }
        if (this.mCameraActivity.getWearableBridge() != null) {
            this.mCameraActivity.getWearableBridge().getPhotoStateNotifier().onCaptureSucceeded();
        }
        return true;
    }

    protected void cancelAf(Class<? extends State> class_) {
        this.mShooting.stopAutoFocus();
        this.mCameraWindow.hideOnScreenButton();
        Executor.setState(class_);
    }

    @Override
    public void captureAutoTransition() {
        Executor.postEvent(ControllerEvent.EV_CAPTURE, ControllerEventSource.AUTO_STATE_TRANSITION, 0, null, AfDone.class);
    }

    protected void captureInPhotoPreview(ControllerEventSource controllerEventSource) {
        if (controllerEventSource == null) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerEventSource.ordinal()]) {
            default: {
                this.startAf(controllerEventSource, true);
                return;
            }
            case 1: 
        }
        this.startRec(RecordingModeless.class);
    }

    protected void finalizeRec(ControllerMessage controllerMessage) {
        this.mCameraWindow.hideRecordingView();
        this.updateSelftimers();
        VideoSavingRequest videoSavingRequest = this.mVideoDevice.getSavingRequest();
        long l = this.mVideoDevice.getRecordingTime();
        videoSavingRequest.setDuration(l);
        videoSavingRequest.setDateTaken(l + videoSavingRequest.getDateTaken());
        this.releaseVideoDevice();
        this.mCameraActivity.disableAutoOffTimer();
        if (this.mCameraActivity.getStorageManager().isReadable()) {
            if (!VideoStabilizer.isIntelligentActive(this.getParams().getVideoStabilizer())) {
                videoSavingRequest.setRequestId(this.mCameraWindow.getRequestId(true));
            }
            this.mSavingTaskManager.storeVideo(videoSavingRequest);
        }
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_STOP, false);
        MeasurePerformance.outResult();
        if (!this.mCameraActivity.isOneShot()) {
            if (this.mControllerManager.getType() == 1 && super.shouldRestartPreview(controllerMessage)) {
                Executor.setState(RecordingModelessFinishing.class);
                this.changeSurfaceSize(1);
            }
        } else {
            return;
        }
        this.finishRecording(controllerMessage);
    }

    protected EventAction getEventAction(ControllerMessage controllerMessage) {
        return (EventAction)controllerMessage.mArg2;
    }

    protected Class<? extends State> getViewFinderState() {
        if (this.mCameraActivity.isOneShotVideo()) {
            return ViewFinderVideo.class;
        }
        CapturingMode capturingMode = this.mControllerManager.getCapturingMode();
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return ViewFinderSuperiorAuto.class;
        }
        if (capturingMode.getType() == 2) {
            return ViewFinderVideo.class;
        }
        return ViewFinderPhoto.class;
    }

    protected Rect getViewRect(ControllerMessage controllerMessage) {
        return (Rect)controllerMessage.mArg2;
    }

    protected void launch() {
        Executor.setState(this.getViewFinderState());
        Executor.sendEmptyEvent(ControllerEvent.EV_LAUNCH);
    }

    protected void onVideoInfo(ControllerMessage controllerMessage) {
        int n = controllerMessage.mArg1;
        if (n == 800 || n == 801) {
            // empty if block
        }
        this.stopRec(controllerMessage);
    }

    protected void pauseRec(ControllerMessage controllerMessage) {
        if (this.mVideoDevice.isRecording()) {
            this.mVideoDevice.pause();
        }
    }

    protected void registerStates() {
        Executor.registerState(new Error(this, null));
        Executor.registerState(new Inactive());
        Executor.registerState(new Idle(this, null));
        Executor.registerState(new IdleForResume(this, null));
        Executor.registerState(new IdleForFinish(this, null));
        Executor.registerState(new AfSearching(this, null));
        Executor.registerState(new AfDone(this, null));
        Executor.registerState(new AfSearchingModelessRecording(this, null));
        Executor.registerState(new AfDoneModelessRecording(this, null));
        Executor.registerState(new ObjectSearching(this, null));
        Executor.registerState(new Capturing(this, null));
        Executor.registerState(new CapturingInManualRecording(this, null));
        Executor.registerState(new CapturingInModelessRecording(this, null));
        Executor.registerState(new RecordingManual(this, null));
        Executor.registerState(new RecordingModeless());
        Executor.registerState(new RecordingModelessFinishing(this, null));
        Executor.registerState(new RecordingModelessPreparing(this, null));
        Executor.registerState(new BurstShooting(this, null));
        Executor.registerState(new StorageWarning(this, null));
        Executor.registerState(new Pause(this, null));
        Executor.registerState(new ViewFinderSuperiorAuto(this, null));
        Executor.registerState(new ViewFinderPhoto(this, null));
        Executor.registerState(new ViewFinderVideo(this, null));
        Executor.registerState(new PauseInManualRecording(this, null));
        Executor.registerState(new PauseInModelessRecording(this, null));
        Executor.registerState(new CapturingInManualRecordingPausing(this, null));
        Executor.registerState(new CapturingInModelessRecordingPausing(this, null));
    }

    protected void resumeRec(ControllerMessage controllerMessage) {
        if (this.mVideoDevice.isPaused()) {
            this.mVideoDevice.resume();
            this.mCameraWindow.showRecordingView();
            this.mCameraWindow.requestToAddVideoChapter(this.mVideoDevice.getSavingRequest().common.orientation);
        }
    }

    @Override
    public void setInitState(boolean bl) {
        if (bl) {
            Executor.setState(IdleForResume.class);
            return;
        }
        Executor.setState(Idle.class);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void startAf(ControllerEventSource controllerEventSource, boolean bl) {
        if (!this.mCameraActivity.updateRemain()) {
            return;
        }
        if (!this.mSavingTaskManager.canPushStoreTask()) return;
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerEventSource.ordinal()]) {
            case 1: {
                return;
            }
        }
        Executor.setState(AfSearching.class);
        this.mCameraWindow.showAutoFocusView();
        this.mShooting.startAutoFocus(controllerEventSource);
        if (!bl) return;
        this.captureAutoTransition();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void startRec(Class<? extends State> class_) {
        if (!AudioResourceChecker.isAudioResourceAvailable((BaseActivity)this.mCameraActivity)) {
            this.mCameraActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
            Executor.setState(this.getViewFinderState());
            return;
        }
        if (this.mVideoDevice.isRecording() || this.mCameraActivity.isAlertDialogOpened()) return;
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_START, true);
        this.mCameraActivity.pauseAudioPlayback();
        if (this.mControllerManager.getType() == 1) {
            if (this.shouldRestartPreview(this.mCameraDevice.getVideoPreviewSize(this.getParams().getVideoSize()))) {
                Executor.setState(RecordingModelessPreparing.class);
                this.prepareRecording();
                this.changeSurfaceSize(2);
            } else {
                Executor.setState(class_);
                this.prepareRecording();
                this.startModelessRecording();
            }
        } else {
            Executor.setState(class_);
            this.prepareRecording();
            this.startRecording();
        }
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_START, false);
        MeasurePerformance.outResult();
    }

    protected boolean stopBurst(ControllerEventSource controllerEventSource) {
        if (this.mBurstShooting.getStartedBy() != controllerEventSource) {
            return false;
        }
        this.mBurstShooting.stop();
        return true;
    }

    protected void stopBurstAndGoToViewFinder(ControllerEventSource controllerEventSource) {
        if (this.stopBurst(controllerEventSource) && !this.mBurstShooting.isCapturing()) {
            Executor.setState(this.getViewFinderState());
        }
    }

    protected void stopBurstAutoTransition(ControllerEventSource controllerEventSource) {
        if (controllerEventSource == ControllerEventSource.KEY) {
            Executor.clearAutoDispatchEvent();
            this.stopBurst(controllerEventSource);
            Executor.postEvent(ControllerEvent.EV_CAPTURE, ControllerEventSource.AUTO_STATE_TRANSITION, 0, null, AfDone.class);
            return;
        }
        Executor.postEvent(ControllerEvent.EV_BURST_STOP, controllerEventSource, 0, null, BurstShooting.class);
    }

    protected void stopRec(ControllerMessage controllerMessage) {
        MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RECORDING_STOP, true);
        if (this.mVideoDevice.isRecording()) {
            this.mVideoDevice.stop();
            VideoSavingRequest videoSavingRequest = this.mVideoDevice.getSavingRequest();
            if (this.mCameraActivity.getStorageManager().isReadable() && VideoStabilizer.isIntelligentActive(this.getParams().getVideoStabilizer())) {
                videoSavingRequest.setRequestId(this.mCameraWindow.getRequestId(true));
            }
            this.mLastSavingRequest = videoSavingRequest;
        }
    }

    public void stopRecordingUrgently(ControllerMessage controllerMessage) {
        if (this.mVideoDevice.isRecording()) {
            this.stopRec(controllerMessage);
        }
        if (this.mVideoDevice.isRecording() || this.mVideoDevice.isRecordStopping()) {
            this.finalizeRec(controllerMessage);
            if (RecordingUtil.deleteFile(this.mVideoDevice.getOutputFile(), true)) {
                DcfPathBuilder.getInstance().returnUnusedFile();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void switchStorage() {
        if (this.mCameraActivity.getParameterManager().get(ParameterKey.DESTINATION_TO_SAVE) == DestinationToSave.SDCARD) {
            this.mCameraActivity.getParameterManager().set(DestinationToSave.EMMC);
        } else {
            this.mCameraActivity.getParameterManager().set(DestinationToSave.SDCARD);
        }
        this.mCameraActivity.updateSettingDialog();
        ParameterKey.DESTINATION_TO_SAVE.setSelectability(ParameterSelectability.UNAVAILABLE);
    }

    private class AbstractIdle
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private AbstractIdle(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AbstractIdle(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleSurfaceDestroyed(ControllerMessage controllerMessage) {
        }
    }

    private abstract class AbstractPreviewing
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private AbstractPreviewing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AbstractPreviewing(StateTransitionController stateTransitionController, com.sonyericsson.android.camera.controller.StateTransitionController$1 var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mAutoZoom.stop();
            this.this$0.mAutoZoom.finish(ControllerEventSource.OTHER);
        }

        @Override
        public void entry() {
            super.entry();
            this.this$0.mEventDispatcher.resetStatus();
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void handleClickContentProgress(ControllerMessage controllerMessage) {
            if (this.this$0.mLastSavingRequest == null) return;
            if (StateTransitionController.access$2900((StateTransitionController)this.this$0).common == null) {
                return;
            }
            Uri uri = (Uri)controllerMessage.mArg2;
            if (StateTransitionController.access$2900((StateTransitionController)this.this$0).common.mimeType != "image/jpeg") {
                if (uri == null) return;
                AutoReviewWindow.launchAlbum((Activity)this.this$0.mCameraActivity, uri, StateTransitionController.access$2900((StateTransitionController)this.this$0).common.mimeType);
                return;
            }
            if (uri == null) {
                this.this$0.mCameraWindow.openInstantViewer(((PhotoSavingRequest)this.this$0.mLastSavingRequest).getImageData(), null, this.this$0.mLastSavingRequest);
                return;
            }
            AutoReviewWindow.launchAlbum((Activity)this.this$0.mCameraActivity, uri, StateTransitionController.access$2900((StateTransitionController)this.this$0).common.mimeType);
        }

        @Override
        public void handleFaceDetect(ControllerMessage controllerMessage) {
            this.this$0.mFaceDetect.updateFaceRectangle((CameraExtension.FaceDetectionResult)controllerMessage.mArg2);
            this.this$0.mCameraDevice.setFaceDetectionCallback(this.this$0.mFaceDetect.getFaceDetectionCallback());
        }

        @Override
        public void handleFaceDetectChange(ControllerMessage controllerMessage) {
            this.this$0.mFaceDetect.changeFocusedFace(((com.sonyericsson.cameracommon.focusview.NamedFace)controllerMessage.mArg2).mUuid, ((com.sonyericsson.cameracommon.focusview.NamedFace)controllerMessage.mArg2).mFacePosition);
        }

        @Override
        public void handleObjectTracking(ControllerMessage controllerMessage) {
            this.this$0.mObjectTracking.onObjectTracked((Rect)controllerMessage.mArg2);
        }

        @Override
        public void handleObjectTrackingInvisible(ControllerMessage controllerMessage) {
            this.this$0.mObjectTracking.invisible();
        }

        @Override
        public void handleObjectTrackingLost(ControllerMessage controllerMessage) {
            this.this$0.mObjectTracking.stop(true);
        }

        @Override
        public void handleObjectTrackingStart(ControllerMessage controllerMessage) {
            this.this$0.mObjectTracking.start((Rect)controllerMessage.mArg2);
        }

        protected void handleOpenReviewWindow(ControllerMessage controllerMessage) {
            StoreDataResult storeDataResult = (StoreDataResult)controllerMessage.mArg2;
            if (!(this.this$0.getParams().getVideoAutoReview() == VideoAutoReview.OFF || this.this$0.mCameraActivity.isOneShot())) {
                try {
                    if (this.this$0.getParams().getVideoAutoReview() == VideoAutoReview.EDIT) {
                        this.this$0.launchEditor(storeDataResult);
                        return;
                    }
                    this.this$0.mCameraWindow.openReviewWindow(storeDataResult.uri, storeDataResult.savingRequest);
                    return;
                }
                catch (ActivityNotFoundException var3_3) {
                    CameraLogger.e(TAG, "openReviewWindow: failed.", (Throwable)var3_3);
                    Executor.setState(this.this$0.getViewFinderState());
                }
            }
        }

        protected void handleOpenReviewWindowOrNot(ControllerMessage controllerMessage) {
            String string = ((StoreDataResult)controllerMessage.mArg2).savingRequest.common.mimeType;
            if (string.equals((Object)"video/mp4") || string.equals((Object)"video/3gpp")) {
                this.handleOpenReviewWindow(controllerMessage);
                return;
            }
            this.this$0.mCameraWindow.startHideThumbnail((Animation.AnimationListener)new Animation.AnimationListener(){

                public void onAnimationEnd(Animation animation) {
                    if (AbstractPreviewing.this.this$0.mVideoDevice.isRecording()) {
                        AbstractPreviewing.this.this$0.mCameraWindow.hideThumbnail();
                    }
                    AbstractPreviewing.this.this$0.mIsVideoSmileCapturing = false;
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
        }

        @Override
        public void handleSceneChanged(ControllerMessage controllerMessage) {
            this.this$0.mSceneRecognition.onRecognizedSceneChanged((CameraExtension.SceneRecognitionResult)controllerMessage.mArg2);
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.finish(controllerMessage.mSource);
        }

        @Override
        public void handleZoomPrepare(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.prepare(controllerMessage.mSource);
        }

        @Override
        public void handleZoomProgress(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.onZoomProgress(controllerMessage.mArg1, (Boolean)controllerMessage.mArg2);
        }

        @Override
        public void handleZoomStart(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.start((ZoomDirection)controllerMessage.mArg2);
        }

        @Override
        public void handleZoomStop(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.stop();
        }

    }

    private abstract class AbstractSearching
    extends ActiveState {
        public boolean mBurstCapture;
        public boolean mCapture;
        final /* synthetic */ StateTransitionController this$0;

        private AbstractSearching(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AbstractSearching(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.mCapture = false;
            this.mBurstCapture = false;
        }
    }

    private abstract class AbstractViewFinder
    extends AbstractPreviewing {
        final /* synthetic */ StateTransitionController this$0;

        private AbstractViewFinder(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AbstractViewFinder(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mCurrentSelfTimer.stop(false);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void entry() {
            super.entry();
            Executor.clearAutoDispatchEvent();
            if (this.this$0.mEventDispatcher.isDragging() && this.this$0.getParams().getTouchCapture().getBooleanValue().booleanValue()) {
                this.this$0.mCameraWindow.setRectangleColor(RectangleColor.NORMAL, null);
                if (this.this$0.getParams().getFocusMode() == FocusMode.TOUCH_FOCUS) {
                    this.this$0.mEventDispatcher.updateTouchFocusStatus(true);
                } else {
                    this.this$0.mEventDispatcher.updateTouchCaptureStatus(true);
                }
            } else {
                this.this$0.startViewFinder();
                if (this.this$0.mCameraWindow.prepared() && !this.this$0.mCameraActivity.updateRemain()) {
                    Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
                }
            }
            this.this$0.mCameraWindow.setOrientationFollowingSensor();
        }

        @Override
        public void handleChangeCapturingMode(ControllerMessage controllerMessage) {
            this.this$0.changeCapturingMode((CapturingMode)controllerMessage.mArg2);
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
            this.this$0.suspendViewFinder();
            Executor.setState(Pause.class);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleLaunch(ControllerMessage controllerMessage) {
            this.this$0.mAutoZoom.update(this.this$0.getParams().capturingMode.getCameraId());
            if (!this.this$0.mCameraWindow.prepared()) {
                this.this$0.prepareView();
                this.this$0.mCameraActivity.getStorageManager().updateRemain(0, true);
            } else {
                MeasurePerformance.outResultDelay(1000);
            }
            this.this$0.doExtraOperation(this.this$0.mCameraActivity.getExtraOperation());
        }

        @Override
        public void handleSelfTimerCancel(ControllerMessage controllerMessage) {
            if (this.this$0.mCameraActivity.getWearableBridge() != null) {
                this.this$0.mCameraActivity.getWearableBridge().getPhotoStateNotifier().onCaptureFailed();
            }
            this.this$0.cancelSelfTimer(false);
            this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
        }

        @Override
        public void handleSelfTimerCountdown(ControllerMessage controllerMessage) {
            this.this$0.mCurrentSelfTimer.countdown(controllerMessage.mArg1);
        }

        @Override
        public void handleSelfTimerFinish(ControllerMessage controllerMessage) {
            this.this$0.finishSelfTimer();
        }

        @Override
        public void handleSelfTimerStart(ControllerMessage controllerMessage) {
            this.this$0.startSelfTimer(controllerMessage.mArg1, controllerMessage.mSource);
        }

        @Override
        public void handleSmileCapture(ControllerMessage controllerMessage) {
            Executor.sendEvent(ControllerEvent.EV_AF_START, 1, (Object)ControllerEventSource.OTHER);
            this.this$0.captureAutoTransition();
        }

        @Override
        public void handleStorageShouldChange(ControllerMessage controllerMessage) {
            this.this$0.switchStorage();
        }

        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
            if (controllerMessage == null) {
                return;
            }
            this.handleOpenReviewWindow(controllerMessage);
            StoreDataResult storeDataResult = (StoreDataResult)controllerMessage.mArg2;
            int n = storeDataResult.getTextId();
            if (n > 0) {
                this.this$0.mCameraActivity.getMessagePopup().showRotatableToastMessage(n, 0, RotatableToast.ToastPosition.BOTTOM);
            }
            this.this$0.notifyStoreDone(storeDataResult);
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            this.this$0.startPreview((CameraSurfaceHolder)controllerMessage.mArg2);
            this.this$0.mSceneRecognition.start();
            this.this$0.mFaceDetect.enableFaceIdentification(this.this$0.getParams().getFaceIdentify().getBooleanValue());
            this.this$0.mFaceDetect.start();
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            super.handleZoomFinish(controllerMessage);
            this.this$0.mCameraWindow.showDefaultView();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean isMenuAvailable() {
            if (this.this$0.mEventDispatcher.isSelfTimerRunning() || !this.this$0.mEventDispatcher.isCaptureValid()) {
                return false;
            }
            return true;
        }
    }

    private class ActiveState
    extends State {
        final /* synthetic */ StateTransitionController this$0;

        private ActiveState(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
        }

        /* synthetic */ ActiveState(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        public void emergencyExit(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleAbort(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            Executor.setState(IdleForFinish.class);
            Executor.setState(Inactive.class);
        }

        @Override
        public void handleDeviceError(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            Executor.setState(Error.class);
            if (!this.this$0.mCameraActivity.isHighTemperature()) {
                this.this$0.showErrorMessage(controllerMessage.mArg1);
            }
        }

        @Override
        public void handleReachHighTemperature(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            this.this$0.mCameraDevice.close();
            Executor.setState(IdleForFinish.class);
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            Executor.setState(StorageWarning.class);
        }

        @Override
        public void handleSurfaceCreated(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.hideBlankScreen();
        }

        @Override
        public void handleSurfaceDestroyed(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            this.this$0.mCameraDevice.finishPreviewing();
        }
    }

    private class AfDone
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private AfDone(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AfDone(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mShooting.stopAutoFocus();
        }

        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
            if (controllerMessage.mSource == ControllerEventSource.TOUCH && this.this$0.mCameraDevice.isObjectTrackingRunning()) {
                return;
            }
            this.this$0.cancelAf(this.this$0.getViewFinderState());
        }

        @Override
        public void handleBurstStart(ControllerMessage controllerMessage) {
            Executor.setState(BurstShooting.class);
            if (!this.this$0.mBurstShooting.start(controllerMessage.mSource)) {
                this.this$0.cancelAf(this.this$0.getViewFinderState());
            }
        }

        @Override
        public void handleBurstStop(ControllerMessage controllerMessage) {
            this.this$0.stopBurstAutoTransition(controllerMessage.mSource);
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            Executor.setState(Capturing.class);
            this.this$0.mLastSavingRequest = this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO);
            this.this$0.mShooting.takePicture((PhotoSavingRequest)this.this$0.mLastSavingRequest);
        }

        @Override
        public void handleObjectTrackingLost(ControllerMessage controllerMessage) {
            this.this$0.cancelAf(this.this$0.getViewFinderState());
            this.this$0.mObjectTracking.stop(true);
        }
    }

    private class AfDoneModelessRecording
    extends AfDone {
        final /* synthetic */ StateTransitionController this$0;

        private AfDoneModelessRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AfDoneModelessRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.this$0.mShooting.stopAutoFocus();
        }

        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleBurstStart(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleBurstStop(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (controllerMessage.mSource == ControllerEventSource.AUTO_STATE_TRANSITION) {
                this.this$0.startRec(RecordingModeless.class);
            }
        }
    }

    private class AfSearching
    extends AbstractSearching {
        final /* synthetic */ StateTransitionController this$0;

        private AfSearching(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AfSearching(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mShooting.stopAutoFocus();
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
            if (controllerMessage.mSource == ControllerEventSource.TOUCH && this.this$0.mCameraDevice.isObjectTrackingRunning()) {
                return;
            }
            this.this$0.cancelAf(this.this$0.getViewFinderState());
            if (!this.mBurstCapture) return;
            Executor.cancelEvent(ControllerEvent.EV_BURST_START);
        }

        @Override
        public void handleAfDone(ControllerMessage controllerMessage) {
            this.this$0.mShooting.onAutoFocus(controllerMessage.mArg1, (AutoFocusListener.Result)controllerMessage.mArg2);
            Executor.setState(AfDone.class);
        }

        @Override
        public void handleBurstStart(ControllerMessage controllerMessage) {
            this.mBurstCapture = true;
            Executor.postEvent(ControllerEvent.EV_BURST_START, controllerMessage.mSource, 0, null, AfDone.class);
        }

        @Override
        public void handleBurstStop(ControllerMessage controllerMessage) {
            this.this$0.stopBurstAutoTransition(controllerMessage.mSource);
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mBurstCapture) {
                Executor.cancelEvent(ControllerEvent.EV_BURST_START);
            }
            this.mCapture = true;
            this.this$0.captureAutoTransition();
        }

        @Override
        public void handleFaceDetectChange(ControllerMessage controllerMessage) {
            this.this$0.restoreFocusMode(true);
            this.this$0.mFaceDetect.enableFaceIdentification(this.this$0.getParams().getFaceIdentify().getBooleanValue());
            super.handleFaceDetectChange(controllerMessage);
        }

        @Override
        public void handleObjectTrackingLost(ControllerMessage controllerMessage) {
            if (!this.mCapture) {
                this.this$0.cancelAf(this.this$0.getViewFinderState());
                this.this$0.mObjectTracking.stop(true);
            }
        }

        @Override
        public void handleZoomPrepare(ControllerMessage controllerMessage) {
            if (this.this$0.mShooting.getAfTrigger() == ControllerEventSource.TOUCH && controllerMessage.mSource == ControllerEventSource.TOUCH) {
                this.this$0.cancelAf(this.this$0.getViewFinderState());
                Executor.sendEvent(controllerMessage);
            }
        }
    }

    private class AfSearchingModelessRecording
    extends AfSearching {
        final /* synthetic */ StateTransitionController this$0;

        private AfSearchingModelessRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ AfSearchingModelessRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleAfDone(ControllerMessage controllerMessage) {
            Executor.setState(AfDoneModelessRecording.class);
        }

        @Override
        public void handleBurstStart(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleBurstStop(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
        }
    }

    private class BurstShooting
    extends ActiveState {
        private boolean mShouldOpenReviewWindow;
        final /* synthetic */ StateTransitionController this$0;

        private BurstShooting(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ BurstShooting(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mBurstShooting.finish();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void entry() {
            super.entry();
            this.mShouldOpenReviewWindow = !this.this$0.mCameraActivity.isOneShot() && this.this$0.getParams().getAutoReview() != AutoReview.OFF;
            this.this$0.mCameraWindow.showBurstShootingView();
            this.this$0.mCameraWindow.disableClickOnThumbnail();
        }

        @Override
        public void exit() {
            super.exit();
            this.this$0.exitFromShooting();
            if (this.mShouldOpenReviewWindow && this.this$0.mBurstShooting.getPictureCount() == 1) {
                return;
            }
            this.this$0.startPreview();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                case 2: {
                    this.this$0.stopBurstAndGoToViewFinder(controllerMessage.mSource);
                    return;
                }
                case 4: {
                    if (this.this$0.getEventAction(controllerMessage) != EventAction.CANCEL) return;
                    {
                        this.this$0.stopBurstAndGoToViewFinder(controllerMessage.mSource);
                        return;
                    }
                }
                default: {
                    return;
                }
                case 5: 
            }
            if (this.this$0.getEventAction(controllerMessage) != EventAction.CANCEL) return;
            {
                this.this$0.stopBurstAndGoToViewFinder(controllerMessage.mSource);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleBurstCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.mCameraActivity.updateRemain()) {
                Executor.sendEmptyEvent(ControllerEvent.EV_STORAGE_ERROR);
                return;
            } else {
                if (this.this$0.mBurstShooting.isStopRequested()) return;
                {
                    this.this$0.mBurstShooting.requestContinue();
                    return;
                }
            }
        }

        @Override
        public void handleBurstStop(ControllerMessage controllerMessage) {
            if (this.this$0.mBurstShooting.getStartedBy() == controllerMessage.mSource) {
                this.this$0.mBurstShooting.stop(2);
            }
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            this.this$0.stopBurst(controllerMessage.mSource);
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
            if (controllerMessage.mArg2 != null && ((Boolean)controllerMessage.mArg2).booleanValue()) {
                Executor.setState(Pause.class);
            }
        }

        @Override
        public void handleShutterDone(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.countUp(controllerMessage.mArg1);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.onCaptureDone();
            if (this.mShouldOpenReviewWindow && this.this$0.mBurstShooting.getPictureCount() == 1) {
                StoreDataResult storeDataResult = (StoreDataResult)controllerMessage.mArg2;
                if (this.this$0.getParams().getAutoReview() != AutoReview.EDIT) {
                    this.this$0.mCameraWindow.openReviewWindow(storeDataResult.uri, storeDataResult.savingRequest);
                    return;
                }
                this.this$0.launchEditor(storeDataResult);
                return;
            } else {
                if (this.this$0.getParams().getBurstShot() == BurstShot.BEST_EFFORT && this.this$0.mBurstShooting.isCapturing()) return;
                {
                    Executor.setState(this.this$0.getViewFinderState());
                    return;
                }
            }
        }

        @Override
        public boolean isBackKeyValid() {
            return false;
        }
    }

    private class Capturing
    extends ActiveState {
        private boolean mOpenAutoReviewWindow;
        final /* synthetic */ StateTransitionController this$0;

        private Capturing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ Capturing(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void entry() {
            super.entry();
            this.mOpenAutoReviewWindow = !this.this$0.mCameraActivity.isOneShot() && this.this$0.getParams().getAutoReview() != AutoReview.OFF;
            this.this$0.mCameraWindow.disableClickOnThumbnail();
        }

        @Override
        public void exit() {
            super.exit();
            this.this$0.exitFromShooting();
            if (this.this$0.getParams().getAutoReview() != AutoReview.OFF) {
                return;
            }
            this.this$0.startPreview();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (controllerMessage.mSource == ControllerEventSource.VIDEO_BUTTON || this.mOpenAutoReviewWindow) {
                return;
            }
            this.this$0.addCaptureRequest(SavingTaskManager.SavedFileType.PHOTO);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.storeImage((byte[])controllerMessage.mArg2) || this.mOpenAutoReviewWindow || this.this$0.mShooting.takeNextPicture(this.this$0.mCameraActivity.updateRemain())) {
                return;
            }
            Executor.setState(this.this$0.getViewFinderState());
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
            if (controllerMessage.mArg2 != null && ((Boolean)controllerMessage.mArg2).booleanValue()) {
                Executor.setState(Pause.class);
            }
        }

        @Override
        public void handleShutterDone(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.startCaptureFeedbackAnimation();
        }

        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
            if (this.this$0.mCameraActivity.isOneShotPhoto()) {
                return;
            }
            StoreDataResult storeDataResult = (StoreDataResult)controllerMessage.mArg2;
            if (this.this$0.getParams().getAutoReview() == AutoReview.EDIT) {
                this.this$0.launchEditor(storeDataResult);
                return;
            }
            this.this$0.mCameraWindow.openReviewWindow(storeDataResult.uri, storeDataResult.savingRequest);
        }

        @Override
        public boolean isBackKeyValid() {
            return false;
        }
    }

    private class CapturingInManualRecording
    extends Capturing {
        private boolean mStopRecRequested;
        final /* synthetic */ StateTransitionController this$0;

        private CapturingInManualRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ CapturingInManualRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.stopRecordingUrgently(controllerMessage);
        }

        @Override
        public void entry() {
            super.entry();
            this.mStopRecRequested = false;
        }

        @Override
        public void exit() {
            this.this$0.mShooting.clearCaptureRequest();
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecRequested) {
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                default: {
                    return;
                }
                case 2: 
            }
            this.this$0.addCaptureRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.storeImage((byte[])controllerMessage.mArg2)) return;
            {
                if (this.mStopRecRequested) {
                    Executor.setState(RecordingManual.class);
                    this.this$0.onVideoInfo(controllerMessage);
                    return;
                } else {
                    if (this.this$0.mShooting.takeNextPicture(this.this$0.mCameraActivity.updateRemain())) return;
                    {
                        Executor.setState(RecordingManual.class);
                        return;
                    }
                }
            }
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleShutterDone(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleVideoFinished(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleVideoInfo(ControllerMessage controllerMessage) {
            this.mStopRecRequested = true;
        }

        @Override
        public void handleVideoProgress(ControllerMessage controllerMessage) {
            this.this$0.mVideoDevice.update(controllerMessage.mArg1);
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    private class CapturingInManualRecordingPausing
    extends CapturingInManualRecording {
        private boolean mStopRecRequested;
        final /* synthetic */ StateTransitionController this$0;

        private CapturingInManualRecordingPausing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ CapturingInManualRecordingPausing(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.storeImage((byte[])controllerMessage.mArg2)) return;
            {
                if (this.mStopRecRequested) {
                    this.this$0.onVideoInfo(controllerMessage);
                    return;
                } else {
                    if (this.this$0.mShooting.takeNextPicture(this.this$0.mCameraActivity.updateRemain())) return;
                    {
                        Executor.setState(PauseInManualRecording.class);
                        return;
                    }
                }
            }
        }
    }

    private class CapturingInModelessRecording
    extends CapturingInManualRecording {
        private boolean mStopRecRequested;
        final /* synthetic */ StateTransitionController this$0;

        private CapturingInModelessRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ CapturingInModelessRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecRequested) {
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                default: {
                    return;
                }
                case 2: {
                    this.this$0.addCaptureRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
                    return;
                }
                case 3: 
                case 4: 
            }
            this.this$0.addCaptureRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.storeImage((byte[])controllerMessage.mArg2)) return;
            {
                if (this.mStopRecRequested) {
                    Executor.setState(RecordingModeless.class);
                    this.this$0.onVideoInfo(controllerMessage);
                    return;
                } else {
                    if (this.this$0.mShooting.takeNextPicture(this.this$0.mCameraActivity.updateRemain())) return;
                    {
                        Executor.setState(RecordingModeless.class);
                        return;
                    }
                }
            }
        }
    }

    private class CapturingInModelessRecordingPausing
    extends CapturingInManualRecording {
        private boolean mStopRecRequested;
        final /* synthetic */ StateTransitionController this$0;

        private CapturingInModelessRecordingPausing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ CapturingInModelessRecordingPausing(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleCompressedData(ControllerMessage controllerMessage) {
            if (!this.this$0.storeImage((byte[])controllerMessage.mArg2)) return;
            {
                if (this.mStopRecRequested) {
                    this.this$0.onVideoInfo(controllerMessage);
                    return;
                } else {
                    if (this.this$0.mShooting.takeNextPicture(this.this$0.mCameraActivity.updateRemain())) return;
                    {
                        Executor.setState(PauseInModelessRecording.class);
                        return;
                    }
                }
            }
        }
    }

    private class Error
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private Error(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ Error(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.this$0.prepareForFinish();
            if (this.this$0.mCameraActivity.isOneShotPhoto()) {
                this.this$0.finishDelayed();
            }
        }

        @Override
        public void handleDeviceError(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleReachHighTemperature(ControllerMessage controllerMessage) {
            Executor.setState(IdleForFinish.class);
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleSurfaceDestroyed(ControllerMessage controllerMessage) {
        }
    }

    private class Idle
    extends AbstractIdle {
        final /* synthetic */ StateTransitionController this$0;

        private Idle(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ Idle(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            this.this$0.startPreview((CameraSurfaceHolder)controllerMessage.mArg2);
            this.this$0.launch();
        }
    }

    private class IdleForFinish
    extends AbstractIdle {
        final /* synthetic */ StateTransitionController this$0;

        private IdleForFinish(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ IdleForFinish(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.this$0.finish();
        }

        @Override
        public void handleAbort(ControllerMessage controllerMessage) {
            Executor.setState(Inactive.class);
        }
    }

    private class IdleForResume
    extends AbstractIdle {
        private boolean mSetupFinished;
        private CameraSurfaceHolder mSurfaceHolder;
        private boolean mSurfacePrepared;
        final /* synthetic */ StateTransitionController this$0;

        private IdleForResume(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ IdleForResume(StateTransitionController stateTransitionController, com.sonyericsson.android.camera.controller.StateTransitionController$1 var2_2) {
            super(stateTransitionController);
        }

        private void checkCondition() {
            if (this.mSurfacePrepared && this.mSetupFinished) {
                if (this.mSurfaceHolder != null) {
                    this.this$0.setPreviewDisplay(this.mSurfaceHolder);
                    this.mSurfaceHolder = null;
                }
                new Handler().post((Runnable)new Runnable(){

                    public void run() {
                        Executor.sendEmptyEvent(ControllerEvent.EV_LAUNCH);
                    }
                });
            }
        }

        @Override
        public void entry() {
            super.entry();
            this.mSurfacePrepared = false;
            this.mSurfaceHolder = null;
            this.mSetupFinished = false;
        }

        @Override
        public void handleCameraSetupFinished(ControllerMessage controllerMessage) {
            this.this$0.mCameraDevice.clearResumeDeviceTask();
            this.mSetupFinished = true;
            super.checkCondition();
        }

        @Override
        public void handleLaunch(ControllerMessage controllerMessage) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SURFACE_CHANGED_TO_LAUNCH, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.RESUME_TO_LAUNCH, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.LAUNCH, true);
            this.this$0.launch();
            this.this$0.mCameraWindow.startSetupCapturingModeSelectorTask(this.this$0.getParams());
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.LAUNCH, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.LAUNCH_TO_DISPATCH_DRAW, true);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STARTUP_TIME, false);
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.ON_RESUME_TO_SURFACE_CHANGED, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SURFACE_CHANGED, true);
            this.mSurfacePrepared = true;
            this.mSurfaceHolder = (CameraSurfaceHolder)controllerMessage.mArg2;
            if (!(this.this$0.mCameraDevice.isOpenCameraDeviceTaskRunning() || this.this$0.mCameraDevice.isSetupCameraDeviceTaskRunning())) {
                this.this$0.setPreviewDisplay(this.mSurfaceHolder);
                this.mSurfaceHolder = null;
            }
            super.checkCondition();
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SURFACE_CHANGED, false);
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.SURFACE_CHANGED_TO_LAUNCH, true);
        }

    }

    private class ObjectSearching
    extends AbstractSearching {
        final /* synthetic */ StateTransitionController this$0;

        private ObjectSearching(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ ObjectSearching(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.mObjectTracking.stop(true);
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            this.mCapture = true;
        }

        @Override
        public void handleObjectTracking(ControllerMessage controllerMessage) {
            this.this$0.mObjectTracking.onObjectTracked((Rect)controllerMessage.mArg2);
            this.this$0.mCurrentSelfTimer.stop(true);
            this.this$0.startAf(controllerMessage.mSource, this.mCapture);
        }

        @Override
        public void handleObjectTrackingLost(ControllerMessage controllerMessage) {
            if (!this.mCapture) {
                this.this$0.mCurrentSelfTimer.stop(false);
                this.this$0.mObjectTracking.stop(true);
                Executor.setState(this.this$0.getViewFinderState());
            }
        }
    }

    private class Pause
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private Pause(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ Pause(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleChangeCapturingMode(ControllerMessage controllerMessage) {
            this.this$0.changeCapturingMode((CapturingMode)controllerMessage.mArg2);
        }

        @Override
        public void handleControllerResume(ControllerMessage controllerMessage) {
            this.this$0.startPreview();
            Executor.setState(this.this$0.getViewFinderState());
        }

        @Override
        public void handleKeyBack(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.closeCapturingModeSelector();
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
            Executor.sendEmptyEvent(ControllerEvent.EV_CONTROLLER_RESUME);
        }

        @Override
        public void handleStorageMounted(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.updateSettingDialog();
        }

        @Override
        public void handleStorageShouldChange(ControllerMessage controllerMessage) {
            this.this$0.switchStorage();
        }

        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
            if (controllerMessage != null) {
                this.this$0.notifyStoreDone((StoreDataResult)controllerMessage.mArg2);
            }
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            this.this$0.startPreview((CameraSurfaceHolder)controllerMessage.mArg2);
        }

        @Override
        public boolean isMenuAvailable() {
            return true;
        }
    }

    private class PauseInManualRecording
    extends RecordingManual {
        final /* synthetic */ StateTransitionController this$0;

        private PauseInManualRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ PauseInManualRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled || !this.this$0.mVideoDevice.isRecording()) {
                do {
                    return;
                    break;
                } while (true);
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                case 5: {
                    return;
                }
                default: {
                    this.mStopRecCalled = true;
                    this.this$0.stopRec(controllerMessage);
                    return;
                }
                case 2: 
            }
            Executor.setState(CapturingInManualRecordingPausing.class);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleSmileCapture(ControllerMessage controllerMessage) {
            if (this.this$0.mAutoZoom.isZooming() || this.this$0.mIsVideoSmileCapturing || this.mStopRecCalled) {
                return;
            }
            this.this$0.mIsVideoSmileCapturing = true;
            Executor.setState(CapturingInManualRecordingPausing.class);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        @Override
        public void handleVideoPauseResume(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            Executor.setState(RecordingManual.class);
            this.this$0.resumeRec(controllerMessage);
        }

        @Override
        public void handleVideoPaused(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.showRecordingPausingView();
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            super.handleZoomFinish(controllerMessage);
            this.this$0.mCameraWindow.showRecordingPausingView();
            if (controllerMessage.mSource == ControllerEventSource.TOUCH) {
                this.this$0.mEventDispatcher.resetStatus();
            }
        }
    }

    private class PauseInModelessRecording
    extends PauseInManualRecording {
        final /* synthetic */ StateTransitionController this$0;

        private PauseInModelessRecording(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ PauseInModelessRecording(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled || !this.this$0.mVideoDevice.isRecording()) {
                do {
                    return;
                    break;
                } while (true);
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                case 5: {
                    return;
                }
                default: {
                    this.mStopRecCalled = true;
                    this.this$0.stopRec(controllerMessage);
                    return;
                }
                case 2: 
                case 3: 
                case 4: 
            }
            Executor.setState(CapturingInModelessRecordingPausing.class);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleSmileCapture(ControllerMessage controllerMessage) {
            if (this.this$0.mAutoZoom.isZooming() || this.this$0.mIsVideoSmileCapturing || this.mStopRecCalled) {
                return;
            }
            this.this$0.mIsVideoSmileCapturing = true;
            Executor.setState(CapturingInModelessRecordingPausing.class);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        @Override
        public void handleVideoPauseResume(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            Executor.setState(RecordingModeless.class);
            this.this$0.resumeRec(controllerMessage);
        }
    }

    private class RecordingManual
    extends AbstractPreviewing {
        protected Class<? extends State> mStateCapturing;
        protected boolean mStopRecCalled;
        final /* synthetic */ StateTransitionController this$0;

        private RecordingManual(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
            this.mStateCapturing = CapturingInManualRecording.class;
        }

        /* synthetic */ RecordingManual(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void emergencyExit(ControllerMessage controllerMessage) {
            super.emergencyExit(controllerMessage);
            this.this$0.stopRecordingUrgently(controllerMessage);
        }

        @Override
        public void entry() {
            super.entry();
            this.mStopRecCalled = false;
        }

        @Override
        public void handleAudioResourceError(ControllerMessage controllerMessage) {
            this.this$0.mCameraActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
            Executor.setState(this.this$0.getViewFinderState());
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled || !this.this$0.mVideoDevice.isRecording()) {
                do {
                    return;
                    break;
                } while (true);
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                case 5: {
                    return;
                }
                default: {
                    this.mStopRecCalled = true;
                    this.this$0.stopRec(controllerMessage);
                    return;
                }
                case 2: 
            }
            Executor.setState(CapturingInManualRecording.class);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
            if (controllerMessage.mArg2 != null && ((Boolean)controllerMessage.mArg2).booleanValue()) {
                Executor.setState(Pause.class);
            }
        }

        @Override
        public void handleKeyBack(ControllerMessage controllerMessage) {
            this.mStopRecCalled = true;
            this.this$0.stopRec(controllerMessage);
        }

        @Override
        protected void handleOpenReviewWindow(ControllerMessage controllerMessage) {
            super.handleOpenReviewWindow(controllerMessage);
            if (this.this$0.getParams().getVideoAutoReview() == VideoAutoReview.OFF) {
                Executor.setState(this.this$0.getViewFinderState());
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleSmileCapture(ControllerMessage controllerMessage) {
            if (this.this$0.mAutoZoom.isZooming() || this.this$0.mIsVideoSmileCapturing || this.mStopRecCalled) {
                return;
            }
            this.this$0.mIsVideoSmileCapturing = true;
            Executor.setState(this.mStateCapturing);
            this.this$0.mShooting.takeScreenShot(this.this$0.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
            this.emergencyExit(controllerMessage);
            Executor.setState(StorageWarning.class);
        }

        @Override
        public void handleStoreDone(ControllerMessage controllerMessage) {
            if (controllerMessage != null) {
                this.handleOpenReviewWindowOrNot(controllerMessage);
                this.this$0.notifyStoreDone((StoreDataResult)controllerMessage.mArg2);
            }
        }

        @Override
        public void handleVideoFinished(ControllerMessage controllerMessage) {
            this.this$0.mCameraDevice.restoreSettingAfterRecording();
            this.this$0.finalizeRec(controllerMessage);
        }

        @Override
        public void handleVideoInfo(ControllerMessage controllerMessage) {
            this.mStopRecCalled = true;
            this.this$0.onVideoInfo(controllerMessage);
        }

        @Override
        public void handleVideoPauseResume(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            Executor.setState(PauseInManualRecording.class);
            this.this$0.pauseRec(controllerMessage);
        }

        @Override
        public void handleVideoProgress(ControllerMessage controllerMessage) {
            this.this$0.mVideoDevice.update(controllerMessage.mArg1);
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            super.handleZoomFinish(controllerMessage);
            this.this$0.mCameraWindow.showRecordingView();
            if (controllerMessage.mSource == ControllerEventSource.TOUCH) {
                this.this$0.mEventDispatcher.resetStatus();
            }
        }

        @Override
        public void handleZoomPrepare(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            super.handleZoomPrepare(controllerMessage);
        }

        @Override
        public void handleZoomProgress(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            super.handleZoomProgress(controllerMessage);
        }

        @Override
        public void handleZoomStart(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            super.handleZoomStart(controllerMessage);
        }

        @Override
        public void handleZoomStop(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            super.handleZoomStop(controllerMessage);
        }

        @Override
        public boolean isRecording() {
            return true;
        }
    }

    private class RecordingModeless
    extends RecordingManual {
        public RecordingModeless() {
            super(StateTransitionController.this, null);
            this.mStateCapturing = CapturingInModelessRecording.class;
        }

        @Override
        public void handleAudioResourceError(ControllerMessage controllerMessage) {
            StateTransitionController.this.mCameraActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
            Executor.setState(StateTransitionController.this.getViewFinderState());
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) return;
            if (!StateTransitionController.this.mVideoDevice.isRecording()) {
                return;
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerMessage.mSource.ordinal()]) {
                default: {
                    if (StateTransitionController.this.mCameraActivity.getParameterManager().getParameters().getVideoSize() == VideoSize.MMS) return;
                    Executor.setState(CapturingInModelessRecording.class);
                    StateTransitionController.this.mShooting.takeScreenShot(StateTransitionController.this.createImageSavingRequest(SavingTaskManager.SavedFileType.PHOTO_DURING_REC));
                    return;
                }
                case 1: 
            }
            this.mStopRecCalled = true;
            StateTransitionController.this.stopRec(controllerMessage);
        }

        @Override
        public void handleVideoPauseResume(ControllerMessage controllerMessage) {
            if (this.mStopRecCalled) {
                return;
            }
            Executor.setState(PauseInModelessRecording.class);
            StateTransitionController.this.pauseRec(controllerMessage);
        }
    }

    private class RecordingModelessFinishing
    extends AbstractSearching {
        protected boolean mIsRecordingFinished;
        final /* synthetic */ StateTransitionController this$0;

        private RecordingModelessFinishing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ RecordingModelessFinishing(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.mIsRecordingFinished = false;
        }

        @Override
        public void exit() {
            super.exit();
            if (!this.mIsRecordingFinished) {
                this.this$0.finishRecording(null);
            }
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            if (((CameraSurfaceHolder)controllerMessage.mArg2).getWidth() == this.this$0.mCameraWindow.getSurfaceRect().width()) {
                this.this$0.finishRecording(null);
                this.mIsRecordingFinished = true;
                Executor.setState(this.this$0.getViewFinderState());
            }
        }

        @Override
        public void handleSurfaceDestroyed(ControllerMessage controllerMessage) {
        }
    }

    private class RecordingModelessPreparing
    extends AbstractSearching {
        final /* synthetic */ StateTransitionController this$0;

        private RecordingModelessPreparing(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ RecordingModelessPreparing(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleSurfaceChanged(ControllerMessage controllerMessage) {
            CameraSurfaceHolder cameraSurfaceHolder = (CameraSurfaceHolder)controllerMessage.mArg2;
            if (cameraSurfaceHolder.getWidth() == this.this$0.mCameraWindow.getSurfaceRect().width()) {
                this.this$0.startPreview(cameraSurfaceHolder);
                Executor.setState(RecordingModeless.class);
                this.this$0.startModelessRecording();
            }
        }

        @Override
        public void handleSurfaceDestroyed(ControllerMessage controllerMessage) {
        }
    }

    private class StorageWarning
    extends ActiveState {
        final /* synthetic */ StateTransitionController this$0;

        private StorageWarning(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ StorageWarning(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            this.this$0.mEventDispatcher.resetStatus();
            this.this$0.mObjectTracking.stop(true);
            this.this$0.mTouchFocus.clear();
            this.this$0.mSceneRecognition.stop();
            this.this$0.mCameraWindow.showStorageWarningView();
            this.this$0.mFaceDetect.enableFaceIdentification(false);
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            this.this$0.mCameraWindow.onCaptureDone();
        }

        @Override
        public void handleChangeCapturingMode(ControllerMessage controllerMessage) {
            this.this$0.changeCapturingMode((CapturingMode)controllerMessage.mArg2);
        }

        @Override
        public void handleControllerPause(ControllerMessage controllerMessage) {
            Executor.setState(Pause.class);
            this.this$0.suspendViewFinder();
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
        }

        @Override
        public void handleStorageMounted(ControllerMessage controllerMessage) {
            Executor.setState(this.this$0.getViewFinderState());
        }

        @Override
        public void handleStorageShouldChange(ControllerMessage controllerMessage) {
            this.this$0.switchStorage();
        }

        @Override
        public boolean isMenuAvailable() {
            return true;
        }
    }

    private class ViewFinderPhoto
    extends AbstractViewFinder {
        final /* synthetic */ StateTransitionController this$0;

        private ViewFinderPhoto(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ ViewFinderPhoto(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public boolean canHandleCaptureRequest() {
            if (!this.this$0.mAutoZoom.isZooming()) {
                return true;
            }
            return false;
        }

        @Override
        public void entry() {
            this.this$0.mCameraActivity.notifyStateIdleToWearable();
            super.entry();
        }

        @Override
        public void exit() {
            super.exit();
            this.this$0.mCameraActivity.notifyStateBlockedToWearable();
        }

        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
            if (this.this$0.mEventDispatcher.isSelfTimerRunning()) {
                this.this$0.cancelSelfTimer(false);
            }
            if (controllerMessage.mSource == ControllerEventSource.VIDEO_BUTTON) {
                this.this$0.mCameraWindow.showDefaultView();
            }
        }

        @Override
        public void handleAfStart(ControllerMessage controllerMessage) {
            if (this.this$0.mEventDispatcher.isSelfTimerRunning()) {
                this.this$0.mCurrentSelfTimer.stop(true);
                this.this$0.consumeSelfTimer(controllerMessage.mSource, true);
            }
            this.this$0.startAf(controllerMessage.mSource, false);
            if (controllerMessage.mSource == ControllerEventSource.VIDEO_BUTTON) {
                this.this$0.mCameraWindow.showReadyForRecordView();
            }
        }

        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (!this.this$0.mCameraActivity.updateRemain()) {
                return;
            }
            if (this.this$0.mEventDispatcher.isSelfTimerRunning()) {
                if (controllerMessage.mSource == ControllerEventSource.VIDEO_BUTTON && this.this$0.mParameterManager.isVideoSelfTimerOn()) {
                    this.this$0.selfTimerCapture(controllerMessage.mSource);
                    return;
                }
                this.this$0.mCurrentSelfTimer.stop(true);
            }
            this.this$0.captureInPhotoPreview(controllerMessage.mSource);
        }

        @Override
        public void handleFaceDetect(ControllerMessage controllerMessage) {
            super.handleFaceDetect(controllerMessage);
            this.this$0.mFaceDetect.requestFaceId((CameraExtension.FaceDetectionResult)controllerMessage.mArg2);
        }

        @Override
        public void handleFaceIdentify(ControllerMessage controllerMessage) {
            this.this$0.mFaceDetect.updateFaceIdentify((List)controllerMessage.mArg2);
        }

        @Override
        public void handleFocusPositionCancel(ControllerMessage controllerMessage) {
            this.this$0.mCameraActivity.notifyStateIdleToWearable();
            this.this$0.mTouchFocus.clear();
            this.this$0.mCameraWindow.showDefaultView();
            this.this$0.updateStatus();
        }

        @Override
        public void handleFocusPositionChange(ControllerMessage controllerMessage) {
            this.this$0.mTouchFocus.setFocusPosition(this.this$0.getViewRect(controllerMessage));
        }

        @Override
        public void handleFocusPositionContinue(ControllerMessage controllerMessage) {
            this.this$0.mTouchFocus.updateTouchFocusRectangle(this.this$0.getViewRect(controllerMessage));
        }

        @Override
        public void handleFocusPositionFinish(ControllerMessage controllerMessage) {
            this.this$0.mCameraActivity.notifyStateIdleToWearable();
            this.this$0.mCameraWindow.showDefaultView();
            this.this$0.mTouchFocus.finish(this.this$0.getViewRect(controllerMessage));
        }

        @Override
        public void handleFocusPositionStart(ControllerMessage controllerMessage) {
            this.this$0.mTouchFocus.start(this.this$0.getViewRect(controllerMessage));
            this.this$0.mCameraActivity.notifyStateBlockedToWearable();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleObjectTrackingStart(ControllerMessage controllerMessage) {
            super.handleObjectTrackingStart(controllerMessage);
            if (this.this$0.mParameterManager.isSelfTimerOn() || this.this$0.mParameterManager.isVideoSelfTimerOn() || !this.this$0.getParams().getTouchCapture().getBooleanValue().booleanValue()) {
                return;
            }
            Executor.setState(ObjectSearching.class);
        }

        @Override
        public void handleSelfTimerCapture(ControllerMessage controllerMessage) {
            if (this.this$0.mCameraActivity.updateRemain()) {
                this.this$0.consumeSelfTimer(controllerMessage.mSource, false);
                this.this$0.captureInPhotoPreview(controllerMessage.mSource);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleSelfTimerStart(ControllerMessage controllerMessage) {
            this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
            if (controllerMessage.mArg2 != null) {
                Rect rect = this.this$0.getViewRect(controllerMessage);
                switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FocusMode[this.this$0.getParams().getFocusMode().ordinal()]) {
                    default: {
                        break;
                    }
                    case 1: {
                        this.this$0.mTouchFocus.finish(rect);
                    }
                }
            }
            this.this$0.startSelfTimer(controllerMessage.mArg1, controllerMessage.mSource);
        }

        @Override
        public void handleZoomPrepare(ControllerMessage controllerMessage) {
            this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
            this.this$0.mTouchFocus.clear();
            this.this$0.mAutoZoom.prepare(controllerMessage.mSource);
        }
    }

    private class ViewFinderSuperiorAuto
    extends ViewFinderPhoto {
        final /* synthetic */ StateTransitionController this$0;

        private ViewFinderSuperiorAuto(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ ViewFinderSuperiorAuto(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void handleFaceDetectChange(ControllerMessage controllerMessage) {
            this.this$0.restoreFocusMode(true);
            this.this$0.mFaceDetect.enableFaceIdentification(this.this$0.getParams().getFaceIdentify().getBooleanValue());
            super.handleFaceDetectChange(controllerMessage);
        }

        @Override
        public void handleFocusPositionCancel(ControllerMessage controllerMessage) {
            super.handleFocusPositionCancel(controllerMessage);
            this.this$0.finishDragging();
        }

        @Override
        public void handleFocusPositionStart(ControllerMessage controllerMessage) {
            this.this$0.prepareDragging();
            super.handleFocusPositionStart(controllerMessage);
            this.this$0.mCameraActivity.notifyStateBlockedToWearable();
        }

        @Override
        public void handleSelfTimerStart(ControllerMessage controllerMessage) {
            this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
            if (controllerMessage.mArg2 != null && controllerMessage.mSource != ControllerEventSource.TOUCH_FACE) {
                this.this$0.mTouchFocus.finish(this.this$0.getViewRect(controllerMessage));
            }
            this.this$0.startSelfTimer(controllerMessage.mArg1, controllerMessage.mSource);
        }

        @Override
        public void handleShutterDone(ControllerMessage controllerMessage) {
            if (controllerMessage.mArg2 == null) {
                this.this$0.mCameraWindow.countUp(controllerMessage.mArg1);
            }
        }

        @Override
        public void handleStorageError(ControllerMessage controllerMessage) {
            super.handleStorageError(controllerMessage);
            this.this$0.restoreFocusMode(true);
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            this.this$0.mCameraActivity.notifyStateIdleToWearable();
            super.handleZoomFinish(controllerMessage);
        }

        @Override
        public void handleZoomPrepare(ControllerMessage controllerMessage) {
            this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
            this.this$0.mTouchFocus.clear();
            this.this$0.finishDragging();
            this.this$0.mAutoZoom.prepare(controllerMessage.mSource);
        }
    }

    private class ViewFinderVideo
    extends AbstractViewFinder {
        final /* synthetic */ StateTransitionController this$0;

        private ViewFinderVideo(StateTransitionController stateTransitionController) {
            this.this$0 = stateTransitionController;
            super(stateTransitionController, null);
        }

        /* synthetic */ ViewFinderVideo(StateTransitionController stateTransitionController,  var2_2) {
            super(stateTransitionController);
        }

        @Override
        public void entry() {
            super.entry();
            if (!this.this$0.isEnoughStorageSizeAvailable()) {
                Executor.postEvent(ControllerEvent.EV_DEVICE_ERROR, -1, null);
            }
        }

        @Override
        public void handleAfCancel(ControllerMessage controllerMessage) {
            super.handleAfCancel(controllerMessage);
            this.this$0.mCameraWindow.showDefaultView();
        }

        @Override
        public void handleAfStart(ControllerMessage controllerMessage) {
            super.handleAfStart(controllerMessage);
            this.this$0.mCameraWindow.showReadyForRecordView();
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void handleCapture(ControllerMessage controllerMessage) {
            if (!this.this$0.mCameraActivity.updateRemain()) {
                return;
            }
            if (this.this$0.mParameterManager.isVideoSelfTimerOn()) {
                if (!this.this$0.mEventDispatcher.isSelfTimerRunning()) return;
                this.this$0.selfTimerCapture(controllerMessage.mSource);
                return;
            }
            this.this$0.startRec(RecordingManual.class);
        }

        @Override
        public void handleSelfTimerCapture(ControllerMessage controllerMessage) {
            if (this.this$0.mCameraActivity.updateRemain()) {
                this.this$0.consumeSelfTimer(controllerMessage.mSource, true);
                this.this$0.startRec(RecordingManual.class);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void handleSelfTimerStart(ControllerMessage controllerMessage) {
            if (AudioResourceChecker.isAudioResourceAvailable((BaseActivity)this.this$0.mCameraActivity)) {
                this.this$0.mEventDispatcher.updateTouchCaptureStatus(false);
                super.handleSelfTimerStart(controllerMessage);
                return;
            }
            EventDispatcher eventDispatcher = this.this$0.mEventDispatcher;
            boolean bl = this.this$0.mParameterManager.isSelfTimerOn() || this.this$0.mParameterManager.isVideoSelfTimerOn();
            eventDispatcher.setShouldSelfTimerStart(bl);
            this.this$0.mCameraActivity.getMessagePopup().showOk(2131362148, 2131361905, false, 2131361932, null, null);
        }

        @Override
        public void handleZoomFinish(ControllerMessage controllerMessage) {
            super.handleZoomFinish(controllerMessage);
            if (controllerMessage.mSource == ControllerEventSource.TOUCH) {
                this.this$0.mEventDispatcher.resetStatus();
            }
        }
    }

}

