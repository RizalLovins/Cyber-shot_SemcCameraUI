/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.graphics.Canvas
 *  android.graphics.Point
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.graphics.YuvImage
 *  android.graphics.drawable.Drawable
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.net.Uri
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  android.view.SurfaceView
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  android.widget.ListView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$DeviceStabilityCondition
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingResult
 *  com.sonyericsson.cameraextension.CameraExtension$SceneMode
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.fastcapturing.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.burst.CountUpView;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.ChapterThumbnail;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingCameraUtils;
import com.sonyericsson.android.camera.fastcapturing.PlatformDependencyResolver;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.android.camera.fastcapturing.view.BaseFastViewFinder;
import com.sonyericsson.android.camera.fastcapturing.view.CaptureArea;
import com.sonyericsson.android.camera.fastcapturing.view.FastCapturingParameterChanger;
import com.sonyericsson.android.camera.fastcapturing.view.FastLayoutAsyncInflateItems;
import com.sonyericsson.android.camera.view.LayoutAsyncInflateItems;
import com.sonyericsson.android.camera.view.settings.SettingGroup;
import com.sonyericsson.android.camera.view.settings.SettingList;
import com.sonyericsson.android.camera.view.settings.SettingUtil;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationFactory;
import com.sonyericsson.cameracommon.capturefeedback.contextview.GLSurfaceContextView;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.TouchCapture;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.focusview.FocusActionListener;
import com.sonyericsson.cameracommon.focusview.FocusRectangles;
import com.sonyericsson.cameracommon.focusview.FocusRectanglesViewList;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.mediasaving.CameraStorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageAutoSwitchController;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import com.sonyericsson.cameracommon.mediasaving.StoreDataResult;
import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.SavingRequest;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.messagepopup.MessagePopup;
import com.sonyericsson.cameracommon.review.AutoReviewWindow;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogListener;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.ClassDefinitionChecker;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.BaseViewFinderLayout;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.LayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.ViewFinder;
import com.sonyericsson.cameracommon.viewfinder.balloontips.BalloonTips;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButton;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButtonAttributes;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButton;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonListener;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingIndicator;
import com.sonyericsson.cameracommon.zoombar.Zoombar;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import com.sonymobile.cameracommon.view.Notification;
import com.sonymobile.cameracommon.view.RecognizedCondition;
import com.sonymobile.cameracommon.view.RecognizedScene;
import com.sonymobile.cameracommon.view.SelfTimerCountDownView;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class FastViewFinder
extends ViewFinder
implements SurfaceHolder.Callback,
StateMachine.OnStateChangedListener,
BaseFastViewFinder,
ContentsViewController.OnClickThumbnailProgressListener,
FocusActionListener,
StorageAutoSwitchController.StorageAutoSwitchListener,
LocationAcquiredListener,
ContentResolverUtilListener,
ReviewWindowListener {
    private static final String TAG = FastViewFinder.class.getSimpleName();
    private FastCapturingActivity mActivity;
    private AutoReviewWindow mAutoReview;
    private AutoReview mAutoReviewSetting;
    private CameraDeviceHandler mCameraDevice = null;
    private final SettingExecutorInterface<SettingGroup> mCameraSwitchExecutor;
    private CaptureFeedback mCaptureFeedback;
    private CapturingMode mCapturingMode;
    private ImageView mConditionIndicatorIcon = null;
    private View mCounterView;
    private BaseFastViewFinder.UiComponentKind mCurrentDisplayingUiComponent;
    private SettingDialogItemFactory mDialogItemFactory;
    private Animation mFadeoutAnimation;
    private FocusRectangles mFocusRectangles;
    private final View.OnKeyListener mInterceptSettingDialogKeyListener;
    private Boolean mIsFaceDetectionIdSupported;
    private boolean mIsInstantViewerOpened = false;
    private boolean mIsSetupHeadupDisplayInvoked;
    private KeyEventTranslator mKeyEventTranslator;
    private FrameLayout mLazyInflatedUiComponentContainerFront = null;
    private SelfTimer mPhotoSelfTimerSetting;
    private final PostUiInflatedTask mPostUiInflatedTask;
    private SharedPreferences mPrefs;
    private int mRecordingOrientation;
    private ImageView mSceneIndicatorIcon;
    private TextView mSceneIndicatorText;
    private Runnable mSceneIndicatorTextRunnable;
    private SelfTimerCountDownView mSelfTimerCountDownView;
    private final SettingDialogListener mSettingDialogListener;
    private SettingDialogStack mSettingDialogStack;
    private final SettingExecutorInterface<SettingGroup> mSettingMenuOpenExecutor;
    private final SettingExecutorInterface<SettingList.Shortcut> mSettingShortcutDialogOpenExecutor;
    private final SettingExecutorInterface<SettingGroup> mSettingShortcutGroupDialogOpenExecutor;
    private StateMachine mStateMachine;
    private View mSurfaceBlinderView;
    private SurfaceHolder mSurfaceHolder = null;
    private SurfaceView mSurfaceView = null;
    private TouchCapture mTouchCapture;
    private final UiComponentBackgroundTouchEventHandler mUiComponentBackgroundTouchEventHandler;
    private VideoAutoReview mVideoAutoReviewSetting;
    private FocusMode mVideoFocusMode;
    private CaptureArea mViewFinderCaptureArea;

    public FastViewFinder(Context context) {
        super((BaseActivity)((FastCapturingActivity)context), new DefaultLayoutPatternApplier(), null);
        this.mCameraSwitchExecutor = new CameraSwitchExecutor((FastViewFinder)this, null);
        this.mSettingMenuOpenExecutor = new SettingMenuOpenExecutor((FastViewFinder)this, null);
        this.mSettingShortcutDialogOpenExecutor = new SettingShortcutDialogOpenExecutor((FastViewFinder)this, null);
        this.mSettingShortcutGroupDialogOpenExecutor = new SettingShortcutGroupDialogOpenExecutor((FastViewFinder)this, null);
        this.mCaptureFeedback = null;
        this.mRecordingOrientation = 0;
        this.mPostUiInflatedTask = new PostUiInflatedTask();
        this.mUiComponentBackgroundTouchEventHandler = new UiComponentBackgroundTouchEventHandler((FastViewFinder)this, null);
        this.mInterceptSettingDialogKeyListener = new View.OnKeyListener(){

            /*
             * Enabled aggressive block sorting
             */
            public boolean onKey(View view, int n, KeyEvent keyEvent) {
                KeyEventTranslator.TranslatedKeyCode translatedKeyCode = FastViewFinder.this.mKeyEventTranslator.translateKeyCode(n);
                switch (.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
                    default: {
                        return false;
                    }
                    case 1: 
                    case 2: {
                        if (!FastViewFinder.this.mSettingDialogStack.isDialogOpened()) return false;
                        FastViewFinder.this.mSettingDialogStack.closeDialogs();
                        return false;
                    }
                }
            }
        };
        this.mSettingDialogListener = new SettingDialogListener(){

            @Override
            public void onCloseSettingDialog(SettingDialogStack settingDialogStack, boolean bl) {
                if (bl) {
                    if (FastViewFinder.this.isAllDialogClosed()) {
                        StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED;
                        Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
                        stateMachine.sendEvent(transitterEvent, arrobject);
                    }
                    FastViewFinder.this.mSettingDialogStack.clearShortcutSelected();
                }
            }

            @Override
            public void onOpenSettingDialog(SettingDialogStack settingDialogStack, boolean bl, boolean bl2) {
                FastViewFinder.this.closeCapturingModeSelector();
                StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_OPENED;
                Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
                stateMachine.sendEvent(transitterEvent, arrobject);
            }
        };
        this.mCounterView = null;
        this.mActivity = (FastCapturingActivity)context;
        this.mActivity.getWindow().addFlags(4194304);
        this.mActivity.getWindow().addFlags(524288);
        this.mActivity.getWindow().addFlags(128);
        this.mSurfaceView = new SurfaceView((Context)this.mActivity);
        this.mSurfaceView.getHolder().addCallback((SurfaceHolder.Callback)this);
        this.mSurfaceBlinderView = new View((Context)this.mActivity);
        this.mSurfaceBlinderView.setBackgroundColor(-16777216);
        this.mSurfaceBlinderView.setVisibility(8);
        this.mDialogItemFactory = new SettingDialogItemFactory();
        this.mFadeoutAnimation = AnimationUtils.loadAnimation((Context)context, (int)2130968576);
        this.disableAccessibilityTalkBack();
        this.mIsSetupHeadupDisplayInvoked = false;
        this.mKeyEventTranslator = new KeyEventTranslator(this.mActivity.getCommonSettings());
    }

    private void addCountUpView(int n) {
        this.mCounterView = CountUpView.createCountUpView((Activity)this.mActivity);
        if (this.mCounterView != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13);
            this.mCounterView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.getBaseLayout().getContentsViewController().removeContentOverlayView(n, this.mCounterView);
            this.getBaseLayout().getContentsViewController().addContentOverlayView(n, this.mCounterView, layoutParams);
        }
        this.getBaseLayout().getContentsViewController().show();
    }

    private SettingAdapter addItem(SettingAdapter settingAdapter, SettingList.Shortcut shortcut, boolean bl) {
        if (bl) {
            settingAdapter.add((Object)super.generateShortcutSettingItem(shortcut));
            return settingAdapter;
        }
        settingAdapter.add((Object)SettingUtil.getBlankItem());
        return settingAdapter;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addVideoChapter(ChapterThumbnail chapterThumbnail) {
        YuvImage yuvImage;
        ByteArrayOutputStream byteArrayOutputStream;
        RecordingIndicator recordingIndicator = this.getBaseLayout().getRecordingIndicator();
        if (!(recordingIndicator.getVisibility() == 0 && (yuvImage = new YuvImage(chapterThumbnail.yuvData, chapterThumbnail.format.intValue(), chapterThumbnail.rect.width(), chapterThumbnail.rect.height(), null)).compressToJpeg(chapterThumbnail.rect, 80, (OutputStream)(byteArrayOutputStream = new ByteArrayOutputStream())))) {
            return;
        }
        recordingIndicator.addChapter(byteArrayOutputStream.toByteArray(), chapterThumbnail.orientation());
    }

    private void cancelSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            this.mSelfTimerCountDownView.cancelSelfTimerCountDownAnimation();
            this.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView((View)this.mSelfTimerCountDownView);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState, boolean bl) {
        if (this.getBaseLayout().getOnScreenButtonGroup() == null) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$view$BaseFastViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()]) {
            default: {
                throw new IllegalStateException("ViewFinder.changeScreenButtonBackground():[Unexpected system bar status.] state = " + (Object)headUpDisplaySetupState);
            }
            case 1: 
            case 3: 
            case 4: {
                if (bl) {
                    this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.CAPTURE, this.getOrientation(), true);
                } else {
                    this.getBaseLayout().getOnScreenButtonGroup().setMain(super.getCaptureButtonTypeAccoringToSelfTimerSetting(), this.getOrientation(), true);
                }
                if (this.mActivity.isOneShotPhotoSecure()) {
                    this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.NONE);
                } else {
                    this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.START_RECORDING, this.getOrientation(), true);
                }
                this.getBaseLayout().getCaptureButtonIcon().setVisibility(4);
                return;
            }
            case 2: {
                VideoSize videoSize = VideoSize.getValueFromFrameSize(this.mCameraDevice.getPreviewRect().width(), this.mCameraDevice.getPreviewRect().height());
                if (videoSize == null) return;
                if (ClassDefinitionChecker.isMediaRecorderPauseAndResumeSupported() && !videoSize.isConstraint()) {
                    this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.PAUSE_RECORDING, this.mRecordingOrientation, false);
                    this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.STOP_RECORDING, this.mRecordingOrientation, false);
                    this.getBaseLayout().getCaptureButtonIcon().setVisibility(0);
                } else {
                    this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.CAPTURE, this.mRecordingOrientation, true);
                    this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.STOP_RECORDING, this.mRecordingOrientation, false);
                    this.getBaseLayout().getCaptureButtonIcon().setVisibility(4);
                }
                if (this.mCameraDevice.getVideoSize() != VideoSize.MMS) return;
                this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.STOP_RECORDING);
                this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.NONE);
                this.getBaseLayout().getCaptureButtonIcon().setVisibility(4);
                return;
            }
            case 5: 
        }
        this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.RESTART_RECORDING, this.mRecordingOrientation, false);
        this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.STOP_RECORDING_IN_PAUSE, this.mRecordingOrientation, false);
        this.getBaseLayout().getCaptureButtonIcon().setVisibility(0);
    }

    private void changeToLayoutWithSetupState(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$view$BaseFastViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()]) {
            default: {
                throw new IllegalStateException("setupHeadUpDisplay():[Illegal State]");
            }
            case 3: {
                super.changeToPhotoIdleView(true);
                super.showBalloonTips();
                return;
            }
            case 4: {
                super.changeToPhotoCaptureView();
                return;
            }
            case 1: {
                super.changeToVideoIdleView();
                super.showBalloonTips();
                return;
            }
            case 2: 
        }
        super.changeToVideoRecordingView();
    }

    private void changeToPauseView() {
        this.changeLayoutTo(DefaultLayoutPattern.CLEAR);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mFocusRectangles.clearAllFocus();
        this.setSceneNotificationIndicatorsInvisible(false);
        this.closeReviewWindow();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void changeToPhotoBurstView(Boolean bl) {
        this.changeLayoutTo(DefaultLayoutPattern.BURST_SHOOTING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.setSceneNotificationIndicatorsInvisible(false);
        if (bl == false) return;
        this.startCaptureFeedbackAnimation();
    }

    private void changeToPhotoCaptureView() {
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    private void changeToPhotoCaptureWaitForAfDoneView() {
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void changeToPhotoDialogView(BaseFastViewFinder.UiComponentKind var1) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mFocusRectangles.onUiComponentOverlaid();
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$view$BaseFastViewFinder$UiComponentKind[var1.ordinal()]) {
            case 1: {
                if (this.isCapturingModeSelectorOpened()) {
                    this.changeLayoutTo(DefaultLayoutPattern.MODE_SELECTOR);
                    this.getBaseLayout().hideLeftIconContainer();
                    ** break;
                }
                if (this.mSettingDialogStack.isDialogOpened()) {
                    this.changeLayoutTo(DefaultLayoutPattern.SETTING);
                }
            }
lbl12: // 5 sources:
            default: {
                ** GOTO lbl17
            }
            case 2: 
        }
        this.changeLayoutTo(DefaultLayoutPattern.CLEAR);
        this.getBaseLayout().hideLeftIconContainer();
lbl17: // 2 sources:
        super.setSceneNotificationIndicatorsInvisible(false);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void changeToPhotoFocusDoneView(Boolean bl) {
        this.changeLayoutTo(DefaultLayoutPattern.FOCUS_DONE);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.changeToPhotoFocusView();
        if (this.mCapturingMode.isFront()) return;
        this.mFocusRectangles.onAutoFocusDone(bl);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void changeToPhotoFocusSearchView() {
        this.changeLayoutTo(DefaultLayoutPattern.FOCUS_SEARCHING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.changeToPhotoFocusView();
        if (this.mCapturingMode.isFront()) return;
        this.mFocusRectangles.onAutoFocusStarted();
    }

    private void changeToPhotoFocusView() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().hideLeftIconContainer();
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void changeToPhotoIdleView(boolean bl) {
        this.changeLayoutTo(DefaultLayoutPattern.PREVIEW);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY, false);
        if (bl) {
            this.mFocusRectangles.clearExceptTouchFocus();
        } else {
            this.mFocusRectangles.onUiComponentRemoved();
        }
        this.mFocusRectangles.onRecordingStop();
        if (this.mCameraDevice.getCameraId() == 0) {
            super.setSceneIndicatorVisibleAllNotificationIndicators(1);
        }
        this.getBaseLayout().showLeftIconContainer();
        this.setOrientation(this.getOrientation());
    }

    private void changeToPhotoSelftimerView() {
        this.changeLayoutTo(DefaultLayoutPattern.SELFTIMER);
        this.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY, true);
        this.mSettingDialogStack.closeDialogs();
        this.getBaseLayout().hideLeftIconContainer();
        this.showSelfTimerCountDownView();
        this.startSelfTimerCountDownAnimation();
        this.setOrientation(this.getOrientation());
    }

    private void changeToPhotoZoomingView() {
        this.changeLayoutTo(DefaultLayoutPattern.ZOOMING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.setSceneNotificationIndicatorsInvisible(false);
        this.getBaseLayout().hideLeftIconContainer();
    }

    private void changeToReadyForRecordView() {
        this.changeLayoutTo(DefaultLayoutPattern.FOCUS_DONE);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().hideLeftIconContainer();
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    private void changeToVideoIdleView() {
        this.changeLayoutTo(DefaultLayoutPattern.PREVIEW);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_STANDBY, false);
        this.mFocusRectangles.clearExceptTouchFocus();
        this.mFocusRectangles.onRecordingStop();
        this.setSceneIndicatorVisibleAllNotificationIndicators(2);
    }

    private void changeToVideoRecordingPauseView() {
        this.changeLayoutTo(DefaultLayoutPattern.PAUSE_RECORDING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_PAUSING, false);
        if (this.getBaseLayout().getRecordingIndicator() != null) {
            this.getBaseLayout().getRecordingIndicator().setIndicator(false);
        }
        this.mFocusRectangles.clearExceptTouchFocus();
        this.mFocusRectangles.onRecordingStop();
        this.setSceneNotificationIndicatorsInvisible(true);
    }

    private void changeToVideoRecordingView() {
        this.changeLayoutTo(DefaultLayoutPattern.RECORDING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().getGeoTagIndicator().hide();
        this.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING, false);
        this.getBaseLayout().getRecordingIndicator().setIndicator(true);
        this.mFocusRectangles.onRecordingStart();
        this.getBaseLayout().hideLeftIconContainer();
        this.getBaseLayout().getContentsViewController().hide();
        this.setSceneNotificationIndicatorsInvisible(true);
        this.setOrientation(this.getOrientation());
    }

    private void changeToVideoZoomingWhileRecordingView() {
        this.changeLayoutTo(DefaultLayoutPattern.ZOOMING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mFocusRectangles.clearExceptTouchFocus();
        this.setSceneNotificationIndicatorsInvisible(true);
    }

    private void checkSurfaceIsPreparedOrNot(SurfaceHolder surfaceHolder, int n, int n2) {
        if (this.mCameraDevice == null) {
            return;
        }
        Rect rect = this.mCameraDevice.getPreviewRect();
        if (rect == null || this.mCameraDevice.isCloseDeviceTaskWorking()) {
            Handler handler = this.getBaseLayout().getRootView().getHandler();
            if (handler != null) {
                handler.postDelayed((Runnable)new SurfaceAvailableRetryTask(surfaceHolder, n, n2), 100);
            }
            this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_EVF_PREPARATION_FAILED, new Object[0]);
            return;
        }
        this.mSurfaceHolder = surfaceHolder;
        Rect rect2 = new Rect(0, 0, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight());
        Rect rect3 = this.computePreviewRect((Activity)this.mActivity, rect.width(), rect.height());
        if (FastViewFinder.isNearSameSize(rect3, rect2)) {
            super.notifyOnEvfPrepared(rect);
            return;
        }
        super.clearSurfaceView();
        super.resizeEvfScope(rect3.width(), rect3.height());
    }

    private void clearSurfaceView() {
        Canvas canvas;
        if (this.mSurfaceHolder != null && (canvas = this.mSurfaceHolder.lockCanvas()) != null) {
            canvas.drawColor(-16777216);
            this.mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void closeCurrentDisplayingUiComponent() {
        if (this.mCurrentDisplayingUiComponent == null) {
            return;
        }
        this.closeCapturingModeSelector();
        this.closeSettingDialog();
    }

    private void closeSettingDialog() {
        if (this.mSettingDialogStack.isDialogOpened()) {
            this.mSettingDialogStack.closeCurrentDialog();
        }
    }

    private PointF convertTouchPointToDevicePreviewPositionRatio(Point point) {
        int n = this.mSurfaceView.getWidth();
        int n2 = this.mSurfaceView.getHeight();
        int n3 = point.x - this.mSurfaceView.getLeft();
        int n4 = point.y - this.mSurfaceView.getTop();
        return new PointF((float)n3 / (float)n, (float)n4 / (float)n2);
    }

    private void createSelfTimerCountDownView(SelfTimer selfTimer) {
        super.removeSelfTimerCountDownView();
        this.mSelfTimerCountDownView = (SelfTimerCountDownView)this.getActivity().getLayoutInflater().inflate(2130903070, null);
        this.mSelfTimerCountDownView.setSelfTimer(selfTimer);
    }

    private SettingExecutorInterface<ParameterValue> createSettingChangeExecutor(ParameterKey parameterKey, ParameterValue parameterValue) {
        return new CloseExecutor<ParameterValue>((FastViewFinder)this, new SettingChangeExecutor(new FastCapturingParameterChanger(parameterKey, parameterValue, (FastViewFinder)this)), null);
    }

    private void doChangeCondition(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
        int n = RecognizedCondition.create(sceneRecognitionResult.deviceStabilityCondition).getIconId();
        if (n != -1) {
            this.mConditionIndicatorIcon.setImageResource(n);
            this.mConditionIndicatorIcon.setRotation(RotationUtil.getAngle(this.getOrientationForUiNotRotateInRecording()));
            this.mConditionIndicatorIcon.setVisibility(0);
            return;
        }
        this.mConditionIndicatorIcon.setVisibility(4);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void doChangeSceneMode(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
        RecognizedScene recognizedScene = RecognizedScene.create(sceneRecognitionResult.sceneMode);
        int n = recognizedScene.getIconId();
        int n2 = recognizedScene.getTextId();
        if (n <= 0 || n2 <= 0) {
            if (!sceneRecognitionResult.isMacroRange) {
                this.mSceneIndicatorIcon.setVisibility(4);
                this.mSceneIndicatorText.setVisibility(4);
                this.mSceneIndicatorIcon.setImageDrawable(null);
                this.mSceneIndicatorText.setText(null);
                return;
            }
            n = 2130837635;
            n2 = 2131361835;
        }
        this.mSceneIndicatorIcon.setImageResource(n);
        Animation animation = AnimationUtils.loadAnimation((Context)this.mActivity, (int)2130968586);
        this.mSceneIndicatorIcon.startAnimation(animation);
        this.mSceneIndicatorIcon.setRotation(RotationUtil.getAngle(this.getOrientationForUiNotRotateInRecording()));
        this.mSceneIndicatorIcon.setVisibility(0);
        if (this.getOrientationForUiNotRotateInRecording() != 2) {
            this.mSceneIndicatorText.setText(null);
            this.mSceneIndicatorText.setVisibility(4);
            return;
        }
        this.mSceneIndicatorText.setText(n2);
        this.mSceneIndicatorText.setVisibility(0);
        AlphaAnimation alphaAnimation = (AlphaAnimation)AnimationUtils.loadAnimation((Context)this.mActivity, (int)2130968584);
        if (this.mSceneIndicatorTextRunnable == null) {
            this.mSceneIndicatorTextRunnable = new Runnable(){

                public void run() {
                    if (FastViewFinder.this.mSceneIndicatorText != null) {
                        FastViewFinder.this.mSceneIndicatorText.setVisibility(4);
                    }
                }
            };
        }
        this.mSceneIndicatorText.removeCallbacks(this.mSceneIndicatorTextRunnable);
        this.mSceneIndicatorText.postDelayed(this.mSceneIndicatorTextRunnable, alphaAnimation.getStartOffset() + alphaAnimation.getDuration());
        this.mSceneIndicatorText.setVisibility(0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private List<SettingItem> generateChildrenSettinItem(Context context, ParameterKey parameterKey) {
        ArrayList arrayList = new ArrayList();
        int n = .$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()];
        Object var5_5 = null;
        SelfTimer[] arrselfTimer = null;
        switch (n) {
            case 1: {
                arrselfTimer = this.getFlashOptions();
                Flash flash = super.getCurrentFlashSetting((Flash[])arrselfTimer);
                break;
            }
            case 2: {
                arrselfTimer = SelfTimer.values();
                SelfTimer selfTimer = this.mPhotoSelfTimerSetting;
            }
        }
        if (arrselfTimer != null) {
            for (SelfTimer selfTimer : arrselfTimer) {
                void var5_6;
                SettingItemBuilder<ParameterValue> settingItemBuilder = SettingItemBuilder.build(selfTimer).iconId(selfTimer.getIconId()).textId(selfTimer.getTextId()).dialogItemType(1).executor(super.createSettingChangeExecutor(parameterKey, selfTimer));
                boolean bl = var5_6 == selfTimer;
                arrayList.add((Object)settingItemBuilder.selected(bl).commit());
            }
            return arrayList;
        }
        return arrayList;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private SettingAdapter generateShortcutItemAdapter(CapturingMode capturingMode) {
        super.updateSelectability();
        SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory);
        block4 : for (SettingList.Shortcut shortcut : SettingList.getSettingShortcutList(capturingMode, (Activity)this.mActivity)) {
            SettingGroup settingGroup;
            switch (.$SwitchMap$com$sonyericsson$android$camera$view$settings$SettingList$Shortcut[shortcut.ordinal()]) {
                default: {
                    settingGroup = shortcut.getGroup();
                    if (settingGroup != null) break;
                    settingAdapter = super.addItem(settingAdapter, shortcut, true);
                    continue block4;
                }
                case 1: {
                    settingAdapter = super.addItem(settingAdapter, shortcut, ParameterKey.FACING.isSelectable());
                    continue block4;
                }
                case 2: {
                    if (this.mActivity.isOneShotPhotoSecure()) {
                        settingAdapter = super.addItem(settingAdapter, SettingList.Shortcut.BLANK, false);
                        continue block4;
                    }
                    settingAdapter = super.addItem(settingAdapter, shortcut, true);
                    continue block4;
                }
            }
            settingAdapter = super.addItem(settingAdapter, shortcut, super.isVisible(settingGroup));
        }
        return settingAdapter;
    }

    private SettingItem generateShortcutSettingItem(SettingList.Shortcut shortcut) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$view$settings$SettingList$Shortcut[shortcut.ordinal()]) {
            default: {
                throw new IllegalArgumentException("Shortcut type[ " + (Object)shortcut + "] is not supported.");
            }
            case 1: {
                Facing facing = Facing.BACK;
                return SettingItemBuilder.build(shortcut.getGroup()).iconId(facing.getIconId()).textId(facing.getParameterKeyTextId()).dialogItemType(0).executor(this.mCameraSwitchExecutor).commit();
            }
            case 3: {
                Flash flash = super.getCurrentFlashSetting(this.getFlashOptions());
                if (flash == null) {
                    return SettingUtil.getBlankItem();
                }
                return SettingItemBuilder.build(shortcut.getGroup()).iconId(flash.getIconId()).textId(flash.getTextId()).dialogItemType(0).executor(this.mSettingShortcutGroupDialogOpenExecutor).commit();
            }
            case 4: {
                SelfTimer selfTimer = this.mPhotoSelfTimerSetting;
                if (selfTimer == null) {
                    return SettingUtil.getBlankItem();
                }
                return SettingItemBuilder.build(shortcut).iconId(selfTimer.getIconId()).textId(selfTimer.getTextId()).dialogItemType(0).executor(this.mSettingShortcutDialogOpenExecutor).commit();
            }
            case 2: {
                SettingGroup settingGroup = SettingList.getDefaultTab(this.mCapturingMode);
                return SettingItemBuilder.build(settingGroup).iconId(2130837605).textId(settingGroup.getTextId()).dialogItemType(6).executor(this.mSettingMenuOpenExecutor).commit();
            }
            case 5: 
        }
        return SettingUtil.getBlankItem();
    }

    private OnScreenButtonGroup.ButtonType getCaptureButtonTypeAccoringToSelfTimerSetting() {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[this.mPhotoSelfTimerSetting.ordinal()]) {
            default: {
                return OnScreenButtonGroup.ButtonType.CAPTURE;
            }
            case 1: {
                return OnScreenButtonGroup.ButtonType.SELFTIMER_LONG;
            }
            case 2: {
                return OnScreenButtonGroup.ButtonType.SELFTIMER_SHORT;
            }
            case 3: 
        }
        return OnScreenButtonGroup.ButtonType.SELFTIMER_INSTANT;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Flash getCurrentFlashSetting(Flash[] arrflash) {
        if (this.mCameraDevice == null) return null;
        if (arrflash == null) {
            return null;
        }
        for (Flash flash : arrflash) {
            if (this.mCameraDevice.getFlashSetting().getValue().equals((Object)flash.getValue())) return flash;
        }
        return null;
    }

    private void getDownHeadUpDisplay() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mLazyInflatedUiComponentContainerFront.setOnTouchListener(null);
        this.mLazyInflatedUiComponentContainerFront = null;
        this.mFocusRectangles.release();
        this.mFocusRectangles = null;
        if (this.mViewFinderCaptureArea != null) {
            this.mViewFinderCaptureArea.setCaptureAreaStateListener(null);
            this.mViewFinderCaptureArea.release();
            this.mViewFinderCaptureArea = null;
        }
        this.mSceneIndicatorIcon = null;
        this.mSceneIndicatorText = null;
        this.mConditionIndicatorIcon = null;
        this.mCaptureFeedback.release();
        this.mCaptureFeedback = null;
        this.mAutoReview = null;
    }

    private void hideBalloonTips() {
        this.getBaseLayout().getBalloonTips().hide();
    }

    private void hideTakePictureFeedbackView() {
        if (this.mSurfaceBlinderView != null && this.mSurfaceBlinderView.getVisibility() == 0) {
            this.mSurfaceBlinderView.setVisibility(4);
            this.getBaseLayout().getPreviewOverlayContainer().removeView(this.mSurfaceBlinderView);
        }
    }

    private boolean isAllDialogClosed() {
        if (this.mSettingDialogStack.isDialogOpened() || this.isCapturingModeSelectorOpened() || this.mActivity.getStorageController().isStorageDialogOpen()) {
            return false;
        }
        return true;
    }

    public static final boolean isNearSameSize(Rect rect, Rect rect2) {
        float f;
        float f2 = (float)rect.width() / (float)rect.height();
        if ((int)(f2 * 100.0f) == (int)((f = (float)rect2.width() / (float)rect2.height()) * 100.0f)) {
            return true;
        }
        return false;
    }

    private boolean isVisible(ParameterKey parameterKey) {
        ParameterSelectability parameterSelectability = parameterKey.getSelectability();
        if (parameterSelectability == ParameterSelectability.SELECTABLE || parameterSelectability == ParameterSelectability.UNAVAILABLE) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean isVisible(SettingGroup settingGroup) {
        if (settingGroup == null) {
            return true;
        }
        ParameterKey[] arrparameterKey = settingGroup.getSettingItemList();
        int n = arrparameterKey.length;
        for (int i = 0; i < n; ++i) {
            if (super.isVisible(arrparameterKey[i])) return true;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void loadSettingsFromSharedPreferencesDeviceAndResourcees() {
        this.mTouchCapture = (TouchCapture)this.mActivity.getCommonSettings().get(CommonSettingKey.TOUCH_CAPTURE);
        this.mPrefs = this.mActivity.getSharedPreferences("com.sonyericsson.android.camera.shared_preferences", 0);
        int n = this.mCameraDevice.getCameraId();
        switch (n) {
            case 0: {
                this.mVideoFocusMode = FocusMode.FACE_DETECTION;
                break;
            }
            case 1: {
                this.mVideoFocusMode = FocusMode.FIXED;
            }
        }
        this.mAutoReviewSetting = this.mActivity.isOneShotPhotoSecure() ? AutoReview.OFF : FastCapturingCameraUtils.loadParameter(this.mPrefs, 1, n, ParameterKey.AUTO_REVIEW, AutoReview.OFF);
        if (this.mVideoAutoReviewSetting == null) {
            this.mVideoAutoReviewSetting = FastCapturingCameraUtils.loadParameter(this.mPrefs, 2, n, ParameterKey.VIDEO_AUTO_REVIEW, VideoAutoReview.OFF);
        }
        if (n == 0) {
            this.mPhotoSelfTimerSetting = SelfTimer.OFF;
            return;
        }
        this.mPhotoSelfTimerSetting = FastCapturingCameraUtils.loadParameter(this.mPrefs, 1, n, ParameterKey.SELF_TIMER, SelfTimer.OFF);
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    private void notifyOnEvfPrepared(Rect rect) {
        if (this.mFocusRectangles != null) {
            this.mFocusRectangles.updateDevicePreviewSize(rect.width(), rect.height());
        }
        super.updateCaptureAreaSize();
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_ON_EVF_PREPARED;
        Object[] arrobject = new Object[]{this.mSurfaceHolder};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onCameraModeChangedTo(int n) {
        Camera.Parameters parameters = this.mStateMachine.getCurrentCameraParameters(false);
        if (!(parameters != null && this.isHeadUpDesplayReady())) {
            return;
        }
        int n2 = parameters.getPreviewSize().width;
        int n3 = parameters.getPreviewSize().height;
        Rect rect = LayoutDependencyResolver.getSurfaceRect((Activity)this.mActivity, (float)n2 / (float)n3);
        PositionConverter.getInstance().setSurfaceSize(rect.width(), rect.height());
        PositionConverter.getInstance().setPreviewSize(n2, n3);
        this.mFocusRectangles.updateDevicePreviewSize(n2, n3);
        this.mFocusRectangles.clearExceptTouchFocus();
        switch (n) {
            default: {
                throw new IllegalArgumentException("ViewFinder.onCameraModeChangedTo():[INVALID]");
            }
            case 1: {
                super.setupCapturingModeSelectorButton(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY);
                super.updateViewFinderCaptureAreaTouchEventHandling(this.mTouchCapture, BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY);
                this.mSettingDialogStack.showShortcutTray();
                super.setupZoomBar(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY);
                break;
            }
            case 2: {
                super.setupCapturingModeSelectorButton(BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING);
                super.updateViewFinderCaptureAreaTouchEventHandling(this.mTouchCapture, BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_STANDBY);
                this.mSettingDialogStack.showShortcutTray();
                super.setupZoomBar(BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_STANDBY);
            }
        }
        this.setOrientation(this.getOrientation());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.updateUuidBeforeUpdateView(faceDetectionResult);
        if (this.mCapturingMode.isFront()) {
            this.mFocusRectangles.setMirrored(true);
        } else {
            this.mFocusRectangles.setMirrored(false);
        }
        this.mFocusRectangles.onFaceDetected(faceDetectionResult);
    }

    private void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> list) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mFocusRectangles.onFaceNameDetected(list);
    }

    private void onLazyInitializationTaskRun() {
        this.setupCapturingModeSelector();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onSceneModeChanged(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
        if (!this.isHeadUpDesplayReady() || this.mActivity.isOneShotPhotoSecure()) {
            return;
        }
        super.doChangeSceneMode(sceneRecognitionResult);
        super.doChangeCondition(sceneRecognitionResult);
    }

    private void onSurfaceAvailableInternal(SurfaceHolder surfaceHolder, int n, int n2) {
        super.checkSurfaceIsPreparedOrNot(surfaceHolder, n, n2);
    }

    private void onToggleCameraSwitch() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_ON_SWITCH_CAMERA, new Object[0]);
    }

    private void onToggleMenuButton() {
        this.mActivity.onKeyUp(82, new KeyEvent(1, 82));
    }

    private void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult objectTrackingResult) {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.mFocusRectangles.onObjectTracked(objectTrackingResult);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private /* varargs */ void onViewFinderStateChanged(StateMachine.CaptureState captureState, Object ... arrobject) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$StateMachine$CaptureState[captureState.ordinal()]) {
            default: {
                return;
            }
            case 3: {
                super.resumeView();
                this.mSurfaceView.setVisibility(0);
                return;
            }
            case 4: {
                this.mCurrentDisplayingUiComponent = null;
                super.changeToPhotoIdleView(false);
                if (arrobject != null && arrobject.length != 0) {
                    if ((BaseFastViewFinder.UiComponentKind)arrobject[0] == BaseFastViewFinder.UiComponentKind.ZOOM_BAR) return;
                    this.requestToDimSystemUi();
                    return;
                }
                this.requestToDimSystemUi();
                return;
            }
            case 5: {
                super.changeToPhotoSelftimerView();
                return;
            }
            case 6: 
            case 7: {
                super.changeToPhotoZoomingView();
                return;
            }
            case 8: {
                this.mCurrentDisplayingUiComponent = (BaseFastViewFinder.UiComponentKind)arrobject[0];
                super.changeToPhotoDialogView(this.mCurrentDisplayingUiComponent);
                this.requestToRecoverSystemUi();
                return;
            }
            case 9: 
            case 10: {
                this.requestToRemoveSystemUi();
                super.changeToPhotoFocusSearchView();
                return;
            }
            case 11: {
                super.changeToPhotoFocusSearchView();
                return;
            }
            case 12: 
            case 13: {
                super.changeToPhotoFocusDoneView((Boolean)arrobject[0]);
                return;
            }
            case 14: {
                super.changeToPhotoCaptureWaitForAfDoneView();
                return;
            }
            case 15: {
                super.changeToPhotoCaptureView();
                return;
            }
            case 16: 
            case 17: {
                super.changeToPhotoBurstView((Boolean)arrobject[0]);
                return;
            }
            case 18: {
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearExceptTouchFocus();
                }
                this.getBaseLayout().showContentsViewController();
                return;
            }
            case 19: {
                super.changeToVideoRecordingView();
                this.requestToDimSystemUi();
                return;
            }
            case 20: {
                super.changeToVideoRecordingView();
                if (arrobject != null && arrobject.length != 0) {
                    if ((BaseFastViewFinder.UiComponentKind)arrobject[0] == BaseFastViewFinder.UiComponentKind.ZOOM_BAR) return;
                    this.requestToDimSystemUi();
                    return;
                }
                this.requestToDimSystemUi();
                return;
            }
            case 24: 
            case 25: {
                super.changeToVideoZoomingWhileRecordingView();
                return;
            }
            case 26: {
                super.hideTakePictureFeedbackView();
                this.mSurfaceView.setVisibility(8);
                super.pauseView();
                super.changeToPauseView();
                return;
            }
            case 27: {
                if (this.mFocusRectangles != null) {
                    this.mFocusRectangles.clearAllFocus();
                }
                super.changeToPhotoIdleView(false);
                this.requestToDimSystemUi();
                return;
            }
            case 28: {
                this.mSurfaceView.getHolder().removeCallback((SurfaceHolder.Callback)this);
                this.mSurfaceView = null;
                super.hideTakePictureFeedbackView();
                this.mSurfaceBlinderView = null;
                this.mKeyEventTranslator = null;
                this.release();
                super.getDownHeadUpDisplay();
                return;
            }
            case 29: {
                super.changeToVideoRecordingPauseView();
                return;
            }
            case 30: {
                this.mVideoAutoReviewSetting = null;
                return;
            }
            case 31: 
        }
        this.requestToRemoveSystemUi();
        super.changeToReadyForRecordView();
    }

    private void openInstantViewer(byte[] arrby, String string, SavingRequest savingRequest) {
        this.mIsInstantViewerOpened = true;
        if (this.mAutoReview != null) {
            ((FrameLayout)this.getActivity().findViewById(2131689561)).setVisibility(4);
            if (!this.mAutoReview.open((Activity)this.getActivity(), arrby, string, savingRequest.common.mimeType, new Rect(0, 0, savingRequest.common.width, savingRequest.common.height), 0, savingRequest.common.orientation, this.mCapturingMode.isFront(), (ReviewWindowListener)this, (ContentResolverUtilListener)this)) {
                this.mIsInstantViewerOpened = false;
                this.closeReviewWindow();
            }
        }
    }

    private void pauseView() {
        this.pause();
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.closeDialogs();
            this.mSettingDialogStack.updateShortcutTray(new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory));
        }
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.onPause();
        }
        this.clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = false;
        this.mAutoReviewSetting = null;
        this.mVideoAutoReviewSetting = null;
        this.mTouchCapture = null;
        this.mVideoFocusMode = null;
    }

    private void preparationForInstantViewer() {
        if (this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().setClickThumbnailProgressListener((ContentsViewController.OnClickThumbnailProgressListener)this);
        }
        if (this.mAutoReview != null) {
            this.mAutoReview.setUri(null);
        }
    }

    private void removeCountUpView(int n) {
        if (this.mCounterView != null) {
            this.getBaseLayout().getContentsViewController().removeContentOverlayView(n, this.mCounterView);
            this.mCounterView = null;
        }
    }

    private void removeSelfTimerCountDownView() {
        if (this.mSelfTimerCountDownView != null) {
            this.getBaseLayout().getLazyInflatedUiComponentContainerBack().removeView((View)this.mSelfTimerCountDownView);
            this.mSelfTimerCountDownView = null;
        }
    }

    private void resizeEvfScope(int n, int n2) {
        if (this.mSurfaceView.getWidth() == n && this.mSurfaceView.getHeight() == n2) {
            throw new IllegalArgumentException("resizeEvfScope():[Already resized]");
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mSurfaceView.getLayoutParams();
        layoutParams.width = n;
        layoutParams.height = n2;
        layoutParams.gravity = 83;
        this.mSurfaceView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void resumeView() {
        if (this.isHeadUpDesplayReady()) {
            this.resume();
            this.mSettingDialogStack.clearShortcutSelected();
            if (this.mCaptureFeedback != null) {
                this.mCaptureFeedback.onResume();
            }
        }
    }

    private void setCount(int n) {
        if (this.mCounterView != null) {
            CountUpView.setCount(this.mCounterView, n);
        }
    }

    private void setEarlyThumbnailView(View view) {
        if (this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().setEarlyThumbnailView(view);
        }
    }

    private void setSceneIndicatorVisibleAllNotificationIndicators(int n) {
        switch (n) {
            default: {
                return;
            }
            case 1: {
                this.mSceneIndicatorIcon.setVisibility(0);
                this.mSceneIndicatorText.setVisibility(0);
                this.mSceneIndicatorText.setText(null);
                this.mConditionIndicatorIcon.setVisibility(0);
                return;
            }
            case 2: 
        }
        this.mSceneIndicatorIcon.setVisibility(4);
        this.mSceneIndicatorText.setVisibility(4);
        this.mSceneIndicatorText.setText(null);
        this.mConditionIndicatorIcon.setVisibility(4);
    }

    private void setSceneNotificationIndicatorsInvisible(boolean bl) {
        if (!bl) {
            this.mSceneIndicatorIcon.setVisibility(4);
            this.mSceneIndicatorText.setVisibility(4);
            this.mSceneIndicatorText.setText(null);
        }
        this.mConditionIndicatorIcon.setVisibility(4);
    }

    private void setupAnimations() {
        Rect rect = new Rect();
        this.getBaseLayout().getRootView().findViewById(2131689521).getGlobalVisibleRect(rect);
    }

    private void setupAutoReview() {
        if (this.mAutoReview == null) {
            this.mAutoReview = (AutoReviewWindow)this.mActivity.findViewById(2131689475);
            this.mAutoReview.setup(this.getActivity().getMessagePopup(), this.getActivity().getCommonSettings());
        }
        this.setAutoReviweDuration(this.mAutoReviewSetting);
    }

    private void setupCaptureArea(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mViewFinderCaptureArea == null) {
            this.mViewFinderCaptureArea = (CaptureArea)this.mActivity.findViewById(2131689542);
            super.updateCaptureAreaSize();
        }
        super.updateViewFinderCaptureAreaTouchEventHandling(this.mTouchCapture, headUpDisplaySetupState);
    }

    private void setupCapturingModeSelectorButton(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (!this.getBaseLayout().isHeadUpDesplayReady()) {
            return;
        }
        CapturingModeButtonAttributes capturingModeButtonAttributes = new CapturingModeButtonAttributes(FastCapturingActivity.ModeName.FAST_CAPTURING_CAMERA.name(), 2130837522, 2131361968);
        this.getBaseLayout().getCapturingModeButton().setCurrentCapturingMode(capturingModeButtonAttributes);
    }

    private void setupContentsView() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.StaticEvent staticEvent = StateMachine.StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED;
        Object[] arrobject = new Object[]{this.getBaseLayout().getContentsViewController()};
        stateMachine.sendStaticEvent(staticEvent, arrobject);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setupFocusRectangles() {
        int n;
        int n2;
        if (this.mStateMachine == null || this.mStateMachine.getCurrentCameraParameters(false) == null) {
            n = 0;
            n2 = 0;
        } else {
            n = this.mStateMachine.getCurrentCameraParameters((boolean)false).getPreviewSize().width;
            n2 = this.mStateMachine.getCurrentCameraParameters((boolean)false).getPreviewSize().height;
        }
        FocusRectanglesViewList focusRectanglesViewList = new FocusRectanglesViewList();
        if (this.isInflated()) {
            focusRectanglesViewList.rectanglesContainer = (RelativeLayout)this.mActivity.findViewById(2131689543);
            focusRectanglesViewList.faceViewList = (View[])this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FACE).toArray((Object[])new View[0]);
            focusRectanglesViewList.trackedObjectView = (TaggedRectangle)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING).get(0);
            focusRectanglesViewList.singleAfView = (RelativeLayout)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_SINGLE).get(0);
            focusRectanglesViewList.touchAfView = (RelativeLayout)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_TOUCH).get(0);
        }
        if (this.mFocusRectangles == null) {
            this.mFocusRectangles = new FocusRectangles((Activity)this.mActivity, (FocusActionListener)this, n, n2, focusRectanglesViewList, (View)this.mViewFinderCaptureArea);
        }
        if (PlatformDependencyResolver.isFaceDetectionSupported(this.mActivity.getCameraDevice().getLatestCachedParameters())) {
            if (this.mTouchCapture == TouchCapture.ON) {
                this.mFocusRectangles.enableFaceTouchCapture();
            } else {
                this.mFocusRectangles.disableFaceTouchCapture();
            }
        }
        this.mFocusRectangles.setVisibility(0);
    }

    private void setupHeadUpDisplay(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mCapturingMode != this.mStateMachine.getCurrentCapturingMode() || this.mActivity.isSecurePhotoLaunchedByIntent()) {
            this.mIsSetupHeadupDisplayInvoked = false;
        }
        if (this.mActivity.isDeviceInSecurityLock() && this.mIsSetupHeadupDisplayInvoked) {
            return;
        }
        this.mCapturingMode = this.mStateMachine.getCurrentCapturingMode();
        this.joinInflateTask();
        if (!(this.isHeadUpDesplayReady() || this.mSurfaceView.getWidth() >= this.mSurfaceView.getHeight())) {
            this.mActivity.postDelayedEvent((Runnable)new ReTrySetupHeadUpDisplayTask(), 100);
            return;
        }
        boolean bl = this.isHeadUpDesplayReady();
        if (this.isInflated()) {
            this.setPreInflatedHeadUpDisplay((View)((ViewGroup)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.HEAD_UP_DISPLAY).get(0)));
        }
        this.requestSetupHeadUpDisplay();
        if (!bl) {
            RelativeLayout relativeLayout = super.setupViewFinderLayout();
            this.getBaseLayout().getPreviewOverlayContainer().addView((View)relativeLayout);
        }
        super.loadSettingsFromSharedPreferencesDeviceAndResourcees();
        super.setupZoomBar(headUpDisplaySetupState);
        super.setupShortcutTray(headUpDisplaySetupState);
        super.setupContentsView();
        super.setupCaptureArea(headUpDisplaySetupState);
        super.setupFocusRectangles();
        super.setupOnScreenCaptureButton(headUpDisplaySetupState);
        super.setupCapturingModeSelectorButton(headUpDisplaySetupState);
        super.setupAutoReview();
        super.setupSceneNotificationIndicator();
        super.setupSelfTimerCountDownView();
        super.setupLazyInflatedUiComponentContainerFront();
        this.setOrientation(this.getBaseLayout().getCurrentOrientation());
        super.changeToLayoutWithSetupState(headUpDisplaySetupState);
        Handler handler = this.getBaseLayout().getRootView().getHandler();
        if (handler != null) {
            handler.post((Runnable)this.mPostUiInflatedTask);
        }
        this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, new Object[]{headUpDisplaySetupState});
        this.clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = true;
    }

    private void setupLazyInflatedUiComponentContainerFront() {
        if (this.mLazyInflatedUiComponentContainerFront == null) {
            this.mLazyInflatedUiComponentContainerFront = (FrameLayout)this.mActivity.findViewById(2131689544);
        }
    }

    private void setupOnScreenCaptureButton(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        super.changeScreenButtonImage(headUpDisplaySetupState, false);
        OnScreenCaptureButtonStateListener onScreenCaptureButtonStateListener = new OnScreenCaptureButtonStateListener((FastViewFinder)this, null);
        OnScreenCamcordButtonStateListener onScreenCamcordButtonStateListener = new OnScreenCamcordButtonStateListener(true);
        OnScreenCamcordButtonStateListener onScreenCamcordButtonStateListener2 = new OnScreenCamcordButtonStateListener(false);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.CAPTURE, onScreenCaptureButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.SELFTIMER_LONG, onScreenCaptureButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.SELFTIMER_SHORT, onScreenCaptureButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.SELFTIMER_INSTANT, onScreenCaptureButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.START_RECORDING, onScreenCamcordButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.STOP_RECORDING, onScreenCamcordButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.STOP_RECORDING_IN_PAUSE, onScreenCamcordButtonStateListener);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.PAUSE_RECORDING, onScreenCamcordButtonStateListener2);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.RESTART_RECORDING, onScreenCamcordButtonStateListener2);
        this.getBaseLayout().getCaptureButtonIcon().setListener(onScreenCaptureButtonStateListener);
        this.getBaseLayout().getCaptureButtonIcon().setScaleType(ImageView.ScaleType.CENTER);
        this.getBaseLayout().getCaptureButtonIcon().set(OnScreenButtonGroup.ButtonType.CAPTURE_IN_SEQUENTIAL_RECORDING.subButtonResource);
        this.getBaseLayout().getCaptureButtonIcon().setVisibility(0);
    }

    private void setupSceneNotificationIndicator() {
        if (this.mSceneIndicatorIcon == null) {
            Notification notification = (Notification)LayoutInflater.from((Context)this.mActivity).inflate(2130903063, null);
            this.getBaseLayout().getModeIndicatorContainer().addView((View)notification);
        }
        this.mSceneIndicatorIcon = (ImageView)this.mActivity.findViewById(2131689566);
        this.mSceneIndicatorText = (TextView)this.mActivity.findViewById(2131689565);
        this.mConditionIndicatorIcon = (ImageView)this.mActivity.findViewById(2131689564);
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    private void setupSelfTimerCountDownView() {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SelfTimer[this.mPhotoSelfTimerSetting.ordinal()]) {
            default: {
                throw new IllegalArgumentException(TAG + ":setupSelfTimerCountDownView [Irregular value] : " + this.mPhotoSelfTimerSetting);
            }
            case 1: 
            case 2: 
            case 3: {
                this.createSelfTimerCountDownView(this.mPhotoSelfTimerSetting);
                return;
            }
            case 4: 
        }
        this.removeSelfTimerCountDownView();
    }

    private void setupShortcutTray(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mSettingDialogStack == null) {
            ListView listView = new ListView((Context)this.mActivity);
            this.mSettingDialogStack = new SettingDialogStack((Context)this.mActivity, this.mSettingDialogListener, (ViewGroup)this.getActivity().findViewById(2131689625), (ViewGroup)((FrameLayout)this.getActivity().findViewById(2131689620)), listView);
        }
        this.mSettingDialogStack.clearShortcutSelected();
        this.mSettingDialogStack.setupShortcutTray(super.generateShortcutItemAdapter(this.mCapturingMode));
        this.mSettingDialogStack.setOnInterceptKeyListener(this.mInterceptSettingDialogKeyListener);
        if (headUpDisplaySetupState.equals((Object)BaseFastViewFinder.HeadUpDisplaySetupState.VIDEO_RECORDING)) {
            this.mSettingDialogStack.hideShortcutTray();
        }
    }

    private RelativeLayout setupViewFinderLayout() {
        boolean bl = this.isInflated();
        RelativeLayout relativeLayout = null;
        if (bl) {
            relativeLayout = (RelativeLayout)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS).get(0);
        }
        if (relativeLayout == null) {
            relativeLayout = (RelativeLayout)LayoutInflater.from((Context)this.mActivity).inflate(2130903058, null);
        }
        GLSurfaceContextView gLSurfaceContextView = new GLSurfaceContextView((Context)this.getActivity(), null);
        relativeLayout.addView((View)gLSurfaceContextView);
        gLSurfaceContextView.getLayoutParams().width = -1;
        gLSurfaceContextView.getLayoutParams().height = -1;
        this.mCaptureFeedback = gLSurfaceContextView;
        return relativeLayout;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void setupZoomBar(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        Zoombar zoombar = this.getBaseLayout().getZoomBar();
        if (zoombar == null) return;
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$view$BaseFastViewFinder$HeadUpDisplaySetupState[headUpDisplaySetupState.ordinal()]) {
            default: {
                zoombar.updateZoombarType(Zoombar.Type.PARTIAL_SUPER_RESOLUTION);
                return;
            }
            case 1: 
            case 2: 
        }
        zoombar.updateZoombarType(Zoombar.Type.NORMAL);
    }

    private void showBalloonTips() {
        if (this.getBaseLayout().getBalloonTips().isBalloonTipsEnabled() && !this.mActivity.isOneShotPhotoSecure()) {
            this.getBaseLayout().getBalloonTips().show();
            this.getBaseLayout().getBalloonTips().setBalloonTipsOrientation(this.getOrientation());
        }
    }

    private void showSelfTimerCountDownView() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.getBaseLayout().getPreview().getWidth(), this.getBaseLayout().getPreview().getHeight());
        layoutParams.addRule(13);
        this.mSelfTimerCountDownView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.mSelfTimerCountDownView.setVisibility(0);
        this.getBaseLayout().getLazyInflatedUiComponentContainerBack().addView((View)this.mSelfTimerCountDownView);
        this.getBaseLayout().getLazyInflatedUiComponentContainerBack().bringChildToFront((View)this.mSelfTimerCountDownView);
    }

    private void startEarlyThumbnailInsertAnimation(int n) {
        if (!(this.getBaseLayout().getContentsViewController() == null || this.mActivity.isOneShotPhotoSecure())) {
            this.getBaseLayout().getContentsViewController().startInsertAnimation(n);
        }
    }

    private void updateCaptureAreaSize() {
        if (this.mViewFinderCaptureArea != null) {
            ViewGroup.LayoutParams layoutParams = this.mViewFinderCaptureArea.getLayoutParams();
            layoutParams.width = this.mSurfaceView.getWidth();
            layoutParams.height = this.mSurfaceView.getHeight();
            this.mViewFinderCaptureArea.setLayoutParams(layoutParams);
        }
    }

    private void updateSelectability() {
        ParameterKey.FACING.setSelectability(ParameterSelectability.getSelectability(Facing.getOptions().length));
        ParameterKey.FLASH.setSelectability(ParameterSelectability.SELECTABLE);
        ParameterKey.PHOTO_LIGHT.setSelectability(ParameterSelectability.INVALID);
        if (this.mCameraDevice.isCameraFront()) {
            ParameterKey.SELF_TIMER.setSelectability(ParameterSelectability.SELECTABLE);
            return;
        }
        ParameterKey.SELF_TIMER.setSelectability(ParameterSelectability.INVALID);
    }

    private void updateUiComponent(BaseFastViewFinder.UiComponentKind uiComponentKind) {
        super.changeToPhotoDialogView(uiComponentKind);
    }

    private void updateUuidBeforeUpdateView(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (this.mIsFaceDetectionIdSupported != null && !this.mIsFaceDetectionIdSupported.booleanValue() && FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult)) {
            int n = ((com.sonyericsson.cameraextension.CameraExtension$ExtFace)faceDetectionResult.extFaceList.get((int)faceDetectionResult.indexOfSelectedFace)).face.id;
            this.mFocusRectangles.setUserTouchFaceUuid(String.valueOf((int)n));
        }
    }

    /*
     * Exception decompiling
     */
    private void updateViewFinderCaptureAreaTouchEventHandling(TouchCapture var1, BaseFastViewFinder.HeadUpDisplaySetupState var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:141)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:380)
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

    public boolean canSwitchStorage() {
        return this.mStateMachine.isMenuAvailable();
    }

    public void changeLayoutTo(LayoutPattern layoutPattern) {
        super.changeLayoutTo(layoutPattern);
        if (this.mActivity.isOneShotPhotoSecure()) {
            this.getBaseLayout().hideContentsViewController();
            if (this.getBaseLayout().getCapturingModeButton() != null) {
                this.getBaseLayout().getCapturingModeButton().setVisibility(4);
            }
            if (this.mConditionIndicatorIcon != null) {
                this.mConditionIndicatorIcon.setVisibility(4);
            }
            if (this.mSceneIndicatorIcon != null) {
                this.mSceneIndicatorIcon.setVisibility(4);
            }
            if (this.mSceneIndicatorText != null) {
                this.mSceneIndicatorText.setVisibility(4);
                this.mSceneIndicatorText.setText(null);
            }
        }
    }

    public void checkSurfaceIsPreparedOrNot() {
        this.checkSurfaceIsPreparedOrNot(this.mSurfaceHolder, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight());
    }

    public void closeReviewWindow() {
        if (this.mAutoReview != null) {
            this.mAutoReview.hide();
        }
    }

    public void ensureVideoAutoReviewSettingHasLoaded() {
        if (this.mVideoAutoReviewSetting == null) {
            this.mPrefs = this.mActivity.getSharedPreferences("com.sonyericsson.android.camera.shared_preferences", 0);
            this.mVideoAutoReviewSetting = FastCapturingCameraUtils.loadParameter(this.mPrefs, 2, this.mCameraDevice.getCameraId(), ParameterKey.VIDEO_AUTO_REVIEW, VideoAutoReview.OFF);
        }
    }

    public void fadeoutCounter() {
        this.mCounterView.findViewById(2131689512).setBackgroundResource(0);
        this.mCounterView.startAnimation(this.mFadeoutAnimation);
    }

    public AutoReview getAutoReviewSetting() {
        return this.mAutoReviewSetting;
    }

    public Flash[] getFlashOptions() {
        Camera.Parameters parameters = this.mStateMachine.getCurrentCameraParameters(false);
        if (parameters == null) {
            return null;
        }
        List list = parameters.getSupportedFlashModes();
        ActionMode actionMode = new ActionMode(false, 1, this.mCameraDevice.getCameraId());
        return LedOptionsResolver.getInstance().getFlashOptions(actionMode, list);
    }

    public int getOrientation() {
        return this.getBaseLayout().getCurrentOrientation();
    }

    public int getOrientationForUiNotRotateInRecording() {
        if (this.mActivity.isRecording()) {
            return this.mRecordingOrientation;
        }
        return this.getOrientation();
    }

    public SelfTimer getPhotoSelfTimerSetting() {
        return this.mPhotoSelfTimerSetting;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getRequestId(boolean bl) {
        int n = -1;
        if (this.getBaseLayout().getContentsViewController() != null) {
            super.preparationForInstantViewer();
            n = bl ? this.getBaseLayout().getContentsViewController().createContentFrame() : this.getBaseLayout().getContentsViewController().createClearContentFrame();
            this.getBaseLayout().getContentsViewController().show();
        }
        return n;
    }

    protected ContentPallet.ThumbnailClickListener getThumbnailClickListener() {
        return new ContentPallet.ThumbnailClickListener(){

            @Override
            public void onClick(Content content) {
                if (content != null) {
                    Content.ContentInfo contentInfo = content.getContentInfo();
                    if (contentInfo.mMimeType.equals((Object)"image/jpeg") && FastViewFinder.this.mActivity.isDeviceInSecurityLock()) {
                        ((FrameLayout)FastViewFinder.this.getActivity().findViewById(2131689561)).setVisibility(4);
                        FastViewFinder.this.mAutoReview.setDuration(-1);
                        FastViewFinder.this.mAutoReview.showRightIcons(Boolean.valueOf((boolean)true));
                        FastViewFinder.this.mAutoReview.open((Activity)FastViewFinder.this.mActivity, contentInfo.mOriginalUri, contentInfo.mMimeType, new Rect(0, 0, contentInfo.mWidth, contentInfo.mHeight), 0, contentInfo.mOrientation, FastViewFinder.this.mCapturingMode.isFront(), (ReviewWindowListener)FastViewFinder.this, (ContentResolverUtilListener)FastViewFinder.this);
                    }
                } else {
                    return;
                }
                content.viewContent((Activity)FastViewFinder.this.mActivity);
            }
        };
    }

    public TouchCapture getTouchCapture() {
        return this.mTouchCapture;
    }

    public VideoAutoReview getVideoAutoReviewSetting() {
        return this.mVideoAutoReviewSetting;
    }

    public void hideHudIcons() {
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
        this.setSceneNotificationIndicatorsInvisible(false);
    }

    public boolean isInTouchCaptureArea(MotionEvent motionEvent) {
        CaptureArea captureArea = this.mViewFinderCaptureArea;
        boolean bl = false;
        if (captureArea != null) {
            bl = this.mViewFinderCaptureArea.contains(motionEvent);
        }
        return bl;
    }

    public boolean isSetupHeadupDisplayInvoked() {
        return this.mIsSetupHeadupDisplayInvoked;
    }

    public boolean isTouchFocus() {
        if (this.mFocusRectangles == null) {
            return false;
        }
        return this.mFocusRectangles.isTouchFocus();
    }

    public void onCanceled() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_CANCEL, new Object[0]);
    }

    public void onClickThumbnailProgress() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_TOUCH_CONTENT_PROGRESS, new Object[0]);
    }

    public void onCloseCapturingModeSelector() {
        this.mLazyInflatedUiComponentContainerFront.setOnTouchListener(null);
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        if (this.isAllDialogClosed()) {
            StateMachine stateMachine = this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED;
            Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }
    }

    public void onCloseStorageDialog() {
        if (this.isAllDialogClosed()) {
            StateMachine stateMachine = this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED;
            Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }
    }

    public void onDeleted(boolean bl, Uri uri) {
        this.getBaseLayout().getContentsViewController().removeContentInfo();
        this.getBaseLayout().reloadContentsViewController(this.getThumbnailClickListener());
    }

    public void onFaceSelected(Point point) {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CHANGE_SELECTED_FACE, new Object[]{point});
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onLayoutOrientationChanged(BaseActivity.LayoutOrientation layoutOrientation) {
        super.onLayoutOrientationChanged(layoutOrientation);
        int n = layoutOrientation == BaseActivity.LayoutOrientation.Portrait ? 1 : 2;
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.StaticEvent staticEvent = StateMachine.StaticEvent.EVENT_ON_ORIENTATION_CHANGED;
        Object[] arrobject = new Object[]{n};
        stateMachine.sendStaticEvent(staticEvent, arrobject);
    }

    public void onLongPressed() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_LONG_PRESS;
        Object[] arrobject = new Object[]{StateMachine.TouchEventSource.FACE};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    public void onModeFinish() {
        this.closeCapturingModeSelector();
        this.changeLayoutTo(DefaultLayoutPattern.CLEAR);
        if (this.mSettingDialogStack != null) {
            this.mSettingDialogStack.hideShortcutTray();
            this.mSettingDialogStack.closeDialogs(false);
        }
        this.mActivity.abort();
    }

    /*
     * Exception decompiling
     */
    public void onModeSelect(String var1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:141)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:380)
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

    protected void onOpenCapturingModeSelector() {
        this.closeSettingDialog();
        this.mLazyInflatedUiComponentContainerFront.setOnTouchListener((View.OnTouchListener)this.mUiComponentBackgroundTouchEventHandler);
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_OPENED;
        Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onOpenStorageDialog() {
        if (this.mCurrentDisplayingUiComponent == null) {
            return;
        }
        this.closeSettingDialog();
        this.closeCapturingModeSelector();
        if (!this.getActivity().getStorageManager().isReady()) return;
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_OPENED;
        Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.SETTING_DIALOG};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    public void onReleased() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
        Object[] arrobject = new Object[]{StateMachine.TouchEventSource.FACE};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    public void onReviewWindowClose() {
        this.mIsInstantViewerOpened = false;
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DIALOG_CLOSED, new Object[0]);
        this.requestToDimSystemUi();
    }

    public void onReviewWindowOpen() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_DIALOG_OPENED;
        Object[] arrobject = new Object[]{BaseFastViewFinder.UiComponentKind.REVIEW_WINDOW};
        stateMachine.sendEvent(transitterEvent, arrobject);
        this.requestToRecoverSystemUi();
    }

    public void onSettingValueChanged(ParameterKey parameterKey, ParameterValue parameterValue) {
        if (!this.mActivity.getCameraDevice().setAdditionalSettings(parameterKey, parameterValue)) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterValue.getParameterKey().ordinal()]) {
            default: {
                return;
            }
            case 1: {
                FastCapturingCameraUtils.storeParameter(this.mPrefs, 0, (Flash)parameterValue);
                return;
            }
            case 2: 
        }
        this.mPhotoSelfTimerSetting = (SelfTimer)parameterValue;
        super.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY, false);
        super.setupSelfTimerCountDownView();
        FastCapturingCameraUtils.storeParameter(this.mPrefs, 1, (SelfTimer)parameterValue);
    }

    public /* varargs */ void onStateChanged(StateMachine.CaptureState captureState, Object ... arrobject) {
        super.onViewFinderStateChanged(captureState, arrobject);
    }

    public void onStorageAutoSwitch(String string) {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STORAGE_SHOULD_CHANGE, new Object[0]);
    }

    public void onTouched() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
        Object[] arrobject = new Object[]{Boolean.FALSE};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    public void openCapturingModeSelector(String string) {
        if (this.mStateMachine.canCurrentStateHandleAsynchronizedTask()) {
            super.openCapturingModeSelector(string);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void openReviewWindow(Uri uri, SavingRequest savingRequest) {
        if (savingRequest.common.mimeType.equals((Object)"image/jpeg") && this.mAutoReviewSetting == AutoReview.EDIT || savingRequest.common.mimeType.equals((Object)"video/mp4") && this.mVideoAutoReviewSetting == VideoAutoReview.EDIT) {
            AutoReviewWindow.launchEditor((Activity)this.mActivity, uri, savingRequest.common.mimeType);
            return;
        } else {
            if (this.mAutoReview == null) return;
            {
                ((FrameLayout)this.getActivity().findViewById(2131689561)).setVisibility(4);
                if (this.mAutoReview.open((Activity)this.getActivity(), uri, savingRequest.common.mimeType, new Rect(0, 0, savingRequest.common.width, savingRequest.common.height), 0, savingRequest.common.orientation, this.mCapturingMode.isFront(), (ReviewWindowListener)this, (ContentResolverUtilListener)this)) return;
                {
                    this.closeReviewWindow();
                    return;
                }
            }
        }
    }

    public void release() {
        super.release();
    }

    public void removeEarlyThumbnailView() {
        this.getBaseLayout().getContentsViewController().removeEarlyThumbnailView();
    }

    public void requestInflate(LayoutInflater layoutInflater) {
        this.startInflateTask(layoutInflater, FastLayoutAsyncInflateItems.getInflateItemsForFast());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public /* varargs */ void sendViewUpdateEvent(BaseFastViewFinder.ViewUpdateEvent viewUpdateEvent, Object ... arrobject) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$view$BaseFastViewFinder$ViewUpdateEvent[viewUpdateEvent.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                super.setupHeadUpDisplay((BaseFastViewFinder.HeadUpDisplaySetupState)arrobject[0]);
                return;
            }
            case 2: {
                if (this.mActivity == null) return;
                if (this.mCameraDevice == null) return;
                if (this.mCameraDevice.getPreviewRect() == null) return;
                Rect rect = LayoutDependencyResolver.getSurfaceRect((Activity)this.mActivity, (float)this.mCameraDevice.getPreviewRect().width() / (float)this.mCameraDevice.getPreviewRect().height());
                int n = rect.width();
                int n2 = rect.height();
                Rect rect2 = new Rect(0, 0, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight());
                if (FastViewFinder.isNearSameSize(rect, rect2)) return;
                super.resizeEvfScope(n, n2);
                return;
            }
            case 3: {
                if (this.mSurfaceBlinderView == null) return;
                if (this.mSurfaceBlinderView.getVisibility() == 0) return;
                this.getBaseLayout().getPreviewOverlayContainer().addView(this.mSurfaceBlinderView);
                this.mSurfaceBlinderView.setVisibility(0);
                return;
            }
            case 4: {
                if (this.mSurfaceBlinderView == null) return;
                if (this.mSurfaceBlinderView.getVisibility() != 0) return;
                this.mSurfaceBlinderView.setVisibility(4);
                this.getBaseLayout().getPreviewOverlayContainer().removeView(this.mSurfaceBlinderView);
                return;
            }
            case 5: {
                if (this.getBaseLayout().getLowMemoryIndicator() == null) return;
                this.getBaseLayout().getLowMemoryIndicator().set(false);
                return;
            }
            case 6: {
                if (this.getBaseLayout().getLowMemoryIndicator() == null) return;
                this.getBaseLayout().getLowMemoryIndicator().set(true);
                return;
            }
            case 7: {
                if (this.getBaseLayout().getRecordingIndicator() == null) return;
                if (!((Boolean)arrobject[1]).booleanValue()) {
                    this.getBaseLayout().getRecordingIndicator().setSequenceMode(true);
                }
                this.getBaseLayout().getRecordingIndicator().setConstraint((Boolean)arrobject[1]);
                this.getBaseLayout().getRecordingIndicator().prepareBeforeRecording((Integer)arrobject[0], (Boolean)arrobject[2]);
                return;
            }
            case 8: {
                super.onCameraModeChangedTo((Integer)arrobject[0]);
                return;
            }
            case 9: {
                super.onSceneModeChanged((CameraExtension.SceneRecognitionResult)arrobject[0]);
                return;
            }
            case 10: {
                this.mFocusRectangles.startFaceDetection();
                return;
            }
            case 11: {
                CameraExtension.FaceDetectionResult faceDetectionResult = (CameraExtension.FaceDetectionResult)arrobject[0];
                if (this.mIsFaceDetectionIdSupported == null) {
                    if (!faceDetectionResult.extFaceList.isEmpty()) {
                        this.mIsFaceDetectionIdSupported = FaceDetectUtil.hasValidFaceId(faceDetectionResult);
                    }
                } else if (!this.mIsFaceDetectionIdSupported.booleanValue()) {
                    FaceDetectUtil.setUuidFaceDetectionResult(faceDetectionResult);
                }
                super.onFaceDetected(faceDetectionResult);
                return;
            }
            case 12: {
                super.onFaceNameDetected((List)arrobject[0]);
                return;
            }
            case 13: {
                this.mFocusRectangles.startObjectTracking();
                return;
            }
            case 14: {
                super.onTrackedObjectStateUpdated((CameraExtension.ObjectTrackingResult)arrobject[0]);
                return;
            }
            case 15: {
                Camera.Parameters parameters = this.mStateMachine.getCurrentCameraParameters(false);
                int n = parameters == null ? 0 : parameters.getMaxZoom();
                int n3 = (Integer)arrobject[0];
                int n4 = this.mCameraDevice.getMaxSuperResolutionZoom();
                boolean bl = PlatformDependencyResolver.isSuperResolutionZoomSupported(parameters);
                Zoombar zoombar = this.getBaseLayout().getZoomBar();
                if (zoombar == null) return;
                if (bl && !this.mStateMachine.isInModeLessRecording()) {
                    zoombar.updateZoombarType(Zoombar.Type.PARTIAL_SUPER_RESOLUTION);
                } else {
                    zoombar.updateZoombarType(Zoombar.Type.NORMAL);
                }
                this.onZoomChanged(n3, n, n4);
                return;
            }
            case 16: {
                super.cancelSelfTimerCountDownView();
                return;
            }
            case 17: {
                super.setCount((Integer)arrobject[0]);
                return;
            }
            case 18: {
                Point point = (Point)arrobject[0];
                FocusRectangles.FocusSetType focusSetType = (FocusRectangles.FocusSetType)arrobject[1];
                this.mFocusRectangles.setFocusPosition(point, focusSetType);
                if (focusSetType != FocusRectangles.FocusSetType.FIRST) return;
                if (this.mCapturingMode.isFront()) return;
                this.mFocusRectangles.onAutoFocusStarted();
                return;
            }
            case 19: {
                this.mFocusRectangles.clearAllFocus();
                return;
            }
            case 20: {
                this.mFocusRectangles.clearAllFocusExceptFace();
                return;
            }
            case 21: {
                if (this.getBaseLayout().getRecordingIndicator() == null) return;
                this.getBaseLayout().getRecordingIndicator().updateRecordingTime((Integer)arrobject[0]);
                this.getBaseLayout().getOnScreenButtonGroup().restartAnimation();
                return;
            }
            case 22: {
                this.setOrientation((Integer)arrobject[0]);
                return;
            }
            case 23: {
                if (this.mCapturingMode.isFront()) return;
                this.mFocusRectangles.onAutoFocusCanceled();
                return;
            }
            case 24: {
                super.updateUiComponent(BaseFastViewFinder.UiComponentKind.SETTING_DIALOG);
                return;
            }
            case 25: {
                super.closeCurrentDisplayingUiComponent();
                return;
            }
            case 26: {
                StoreDataResult storeDataResult = (StoreDataResult)arrobject[0];
                this.openReviewWindow(storeDataResult.uri, storeDataResult.savingRequest);
                return;
            }
            case 27: {
                SavingRequest savingRequest = (SavingRequest)arrobject[1];
                String string = savingRequest.common.mimeType;
                AutoReviewWindow autoReviewWindow = this.mAutoReview;
                Uri uri = null;
                if (autoReviewWindow != null && (uri = this.mAutoReview.getUri()) != null) {
                    AutoReviewWindow.launchAlbum((Activity)this.mActivity, uri, string);
                    return;
                }
                if (string != "video/mp4" && string != "video/3gpp") {
                    super.openInstantViewer((byte[])arrobject[0], null, savingRequest);
                    return;
                }
                super.openInstantViewer(null, (String)arrobject[0], savingRequest);
                return;
            }
            case 28: {
                if (this.mAutoReview != null && this.mAutoReview.getUri() == null) {
                    this.mAutoReview.setUri((Uri)arrobject[0]);
                }
                if (this.mAutoReview == null) return;
                if (!this.mIsInstantViewerOpened) return;
                this.mAutoReview.showRightIcons(Boolean.valueOf((boolean)true));
                return;
            }
            case 29: {
                this.startCaptureFeedbackAnimation();
                return;
            }
            case 30: {
                super.setEarlyThumbnailView((View)arrobject[0]);
                return;
            }
            case 31: {
                this.removeEarlyThumbnailView();
                return;
            }
            case 32: {
                int n = (Integer)arrobject[0];
                if (arrobject.length > 1) {
                    this.startEarlyThumbnailInsertAnimation(n, (Animation.AnimationListener)arrobject[1]);
                    return;
                }
                super.startEarlyThumbnailInsertAnimation(n);
                return;
            }
            case 33: {
                super.addCountUpView((Integer)arrobject[0]);
                return;
            }
            case 34: {
                super.removeCountUpView((Integer)arrobject[0]);
                return;
            }
            case 35: {
                super.onLazyInitializationTaskRun();
                return;
            }
            case 36: {
                super.addVideoChapter((ChapterThumbnail)arrobject[0]);
                return;
            }
            case 37: {
                this.onNotifyThermalStatus(false);
                return;
            }
            case 38: 
        }
        this.onNotifyThermalStatus(true);
    }

    public void setAutoReviweDuration(AutoReview autoReview) {
        if (this.mAutoReview != null && autoReview != null) {
            this.mAutoReview.setDuration(autoReview.getDuration());
        }
    }

    public void setCameraDevice(CameraDeviceHandler cameraDeviceHandler) {
        this.mCameraDevice = cameraDeviceHandler;
    }

    public void setContentView() {
        this.setup((View)this.mSurfaceView);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setOrientation(int n) {
        if (this.mStateMachine.isRecording()) {
            super.setOrientation(n, this.mRecordingOrientation);
        } else {
            super.setOrientation(n);
        }
        if (this.isHeadUpDesplayReady()) {
            if (this.mSettingDialogStack != null) {
                this.mSettingDialogStack.setUiOrientation(n);
            }
            this.mSceneIndicatorIcon.setRotation(RotationUtil.getAngle(this.getOrientationForUiNotRotateInRecording()));
            this.mConditionIndicatorIcon.setRotation(RotationUtil.getAngle(this.getOrientationForUiNotRotateInRecording()));
            this.mSceneIndicatorText.setAnimation(null);
            this.mSceneIndicatorText.setVisibility(4);
            if (this.mAutoReview != null) {
                this.mAutoReview.setOrientation(n);
            }
            if (this.mFocusRectangles != null) {
                this.mFocusRectangles.setOrientation(n);
            }
            if (this.mSelfTimerCountDownView != null) {
                this.mSelfTimerCountDownView.setSensorOrientation(n);
            }
        }
    }

    public void setRecordingOrientation(int n) {
        this.mRecordingOrientation = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setStateMachine(StateMachine stateMachine) {
        if (stateMachine != null) {
            stateMachine.addOnStateChangedListener((StateMachine.OnStateChangedListener)this);
        } else if (this.mStateMachine != null) {
            this.mStateMachine.removeOnStateChangedListener((StateMachine.OnStateChangedListener)this);
        }
        this.mStateMachine = stateMachine;
    }

    public void startCaptureFeedbackAnimation() {
        if (this.mCaptureFeedback != null) {
            this.mCaptureFeedback.start(CaptureFeedbackAnimationFactory.createDefaultAnimation());
        }
    }

    public void startEarlyThumbnailInsertAnimation(int n, Animation.AnimationListener animationListener) {
        this.getBaseLayout().getContentsViewController().startInsertAnimation(n, animationListener);
    }

    public void startHideThumbnail() {
        if (this.getBaseLayout().getContentsViewController() == null) {
            return;
        }
        this.getBaseLayout().getContentsViewController().stopAnimation(true);
        Animation animation = AnimationUtils.loadAnimation((Context)this.mActivity, (int)2130968592);
        animation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                if (FastViewFinder.this.mCameraDevice.isRecording()) {
                    FastViewFinder.this.getBaseLayout().getContentsViewController().hideThumbnail();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.getBaseLayout().getContentsViewController().startAnimation(animation);
    }

    public void startSelfTimerCountDownAnimation() {
        this.mSelfTimerCountDownView.startSelfTimerCountDownAnimation();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int n, int n2, int n3) {
        super.checkSurfaceIsPreparedOrNot(surfaceHolder, n2, n3);
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.onSurfaceAvailableInternal(surfaceHolder, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight());
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (this.mCameraDevice == null) {
            CameraLogger.w(TAG, "CameraDevice has aleady been released.");
            return;
        }
        this.mCameraDevice.stopLiveViewFinder();
    }

    private class CameraSwitchExecutor
    implements SettingExecutorInterface<SettingGroup> {
        final /* synthetic */ FastViewFinder this$0;

        private CameraSwitchExecutor(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ CameraSwitchExecutor(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        @Override
        public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
            this.this$0.onToggleCameraSwitch();
        }
    }

    private class CloseExecutor<T>
    implements SettingExecutorInterface<T> {
        private final SettingExecutorInterface<T> mExecutor;
        final /* synthetic */ FastViewFinder this$0;

        private CloseExecutor(FastViewFinder fastViewFinder, SettingExecutorInterface<T> settingExecutorInterface) {
            this.this$0 = fastViewFinder;
            this.mExecutor = settingExecutorInterface;
        }

        /* synthetic */ CloseExecutor(FastViewFinder fastViewFinder, SettingExecutorInterface settingExecutorInterface,  var3_2) {
            super(fastViewFinder, settingExecutorInterface);
        }

        @Override
        public void onExecute(TypedSettingItem<T> typedSettingItem) {
            this.mExecutor.onExecute(typedSettingItem);
            this.this$0.mSettingDialogStack.closeDialogs();
            this.this$0.mSettingDialogStack.updateShortcutTray(this.this$0.generateShortcutItemAdapter(this.this$0.mCapturingMode));
        }
    }

    private class OnScreenCamcordButtonStateListener
    implements OnScreenButtonListener {
        private final boolean mIsSubCamcordButton;

        public OnScreenCamcordButtonStateListener(boolean bl) {
            this.mIsSubCamcordButton = bl;
        }

        @Override
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            if (this.mIsSubCamcordButton) {
                FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SUB_CAMCORD_BUTTON_CANCEL, new Object[0]);
            }
        }

        @Override
        public void onDispatchDraw(OnScreenButton onScreenButton, Canvas canvas) {
        }

        @Override
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            FastViewFinder.this.mSettingDialogStack.closeDialogs();
            if (this.mIsSubCamcordButton) {
                FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SUB_CAMCORD_BUTTON_TOUCH, new Object[0]);
            }
        }

        @Override
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            if (this.mIsSubCamcordButton) {
                FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SUB_CAMCORD_BUTTON_RELEASE, new Object[0]);
                return;
            }
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAMCORD_BUTTON_RELEASE, new Object[0]);
        }
    }

    private class OnScreenCaptureButtonStateListener
    implements OnScreenButtonListener {
        final /* synthetic */ FastViewFinder this$0;

        private OnScreenCaptureButtonStateListener(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ OnScreenCaptureButtonStateListener(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        @Override
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            if (this.this$0.mStateMachine != null) {
                this.this$0.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_CANCEL, new Object[0]);
            }
        }

        @Override
        public void onDispatchDraw(OnScreenButton onScreenButton, Canvas canvas) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.STARTUP_TIME, false);
        }

        @Override
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            this.this$0.mSettingDialogStack.closeDialogs();
            StateMachine stateMachine = this.this$0.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
            Object[] arrobject = new Object[]{Boolean.TRUE};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }

        @Override
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            StateMachine stateMachine = this.this$0.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
            Object[] arrobject = new Object[]{StateMachine.TouchEventSource.PHOTO_BUTTON};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }
    }

    /*
     * Failed to analyse overrides
     */
    class PostUiInflatedTask
    implements Runnable {
        PostUiInflatedTask() {
        }

        public void run() {
            FastViewFinder.this.setupAnimations();
        }
    }

    /*
     * Failed to analyse overrides
     */
    class ReTrySetupHeadUpDisplayTask
    implements Runnable {
        ReTrySetupHeadUpDisplayTask() {
        }

        public void run() {
            if (FastViewFinder.this.mStateMachine == null) {
                return;
            }
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, new Object[0]);
        }
    }

    private class SettingMenuOpenExecutor
    implements SettingExecutorInterface<SettingGroup> {
        final /* synthetic */ FastViewFinder this$0;

        private SettingMenuOpenExecutor(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ SettingMenuOpenExecutor(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        @Override
        public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
            this.this$0.onToggleMenuButton();
        }
    }

    private class SettingShortcutDialogOpenExecutor
    implements SettingExecutorInterface<SettingList.Shortcut> {
        final /* synthetic */ FastViewFinder this$0;

        private SettingShortcutDialogOpenExecutor(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ SettingShortcutDialogOpenExecutor(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        @Override
        public void onExecute(TypedSettingItem<SettingList.Shortcut> typedSettingItem) {
            if (!this.this$0.getActivity().isMenuAvailable()) {
                this.this$0.mSettingDialogStack.clearShortcutSelected();
                return;
            }
            SettingAdapter settingAdapter = new SettingAdapter((Context)this.this$0.mActivity, this.this$0.mDialogItemFactory);
            switch (.$SwitchMap$com$sonyericsson$android$camera$view$settings$SettingList$Shortcut[typedSettingItem.getData().ordinal()]) {
                default: {
                    return;
                }
                case 4: 
            }
            ParameterKey parameterKey = ParameterKey.SELF_TIMER;
            settingAdapter.addAll((Collection)this.this$0.generateChildrenSettinItem((Context)this.this$0.mActivity, parameterKey));
            if (!this.this$0.mSettingDialogStack.openShortcutDialog(settingAdapter, parameterKey.getTitleTextId(this.this$0.mCapturingMode))) {
                this.this$0.mSettingDialogStack.closeCurrentDialog();
            }
            this.this$0.hideBalloonTips();
        }
    }

    private class SettingShortcutGroupDialogOpenExecutor
    implements SettingExecutorInterface<SettingGroup> {
        final /* synthetic */ FastViewFinder this$0;

        private SettingShortcutGroupDialogOpenExecutor(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ SettingShortcutGroupDialogOpenExecutor(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
            if (!this.this$0.getActivity().isMenuAvailable()) {
                this.this$0.mSettingDialogStack.clearShortcutSelected();
                return;
            }
            SettingAdapter settingAdapter = new SettingAdapter((Context)this.this$0.mActivity, this.this$0.mDialogItemFactory);
            for (ParameterKey parameterKey : typedSettingItem.getData().getSettingItemList()) {
                if (!parameterKey.isSelectable()) continue;
                Iterator iterator = this.this$0.generateChildrenSettinItem((Context)this.this$0.mActivity, parameterKey).iterator();
                while (iterator.hasNext()) {
                    settingAdapter.add((Object)((SettingItem)iterator.next()));
                }
                if (!this.this$0.mSettingDialogStack.openShortcutDialog(settingAdapter, parameterKey.getTitleTextId(this.this$0.mCapturingMode))) {
                    this.this$0.mSettingDialogStack.closeCurrentDialog();
                }
                this.this$0.hideBalloonTips();
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SurfaceAvailableRetryTask
    implements Runnable {
        private final int mHeight;
        private final SurfaceHolder mSurface;
        private final int mWidth;

        SurfaceAvailableRetryTask(SurfaceHolder surfaceHolder, int n, int n2) {
            this.mSurface = surfaceHolder;
            this.mWidth = n;
            this.mHeight = n2;
        }

        public void run() {
            FastViewFinder.this.onSurfaceAvailableInternal(this.mSurface, this.mWidth, this.mHeight);
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class UiComponentBackgroundTouchEventHandler
    implements View.OnTouchListener {
        final /* synthetic */ FastViewFinder this$0;

        private UiComponentBackgroundTouchEventHandler(FastViewFinder fastViewFinder) {
            this.this$0 = fastViewFinder;
        }

        /* synthetic */ UiComponentBackgroundTouchEventHandler(FastViewFinder fastViewFinder,  var2_2) {
            super(fastViewFinder);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                this.this$0.closeCurrentDisplayingUiComponent();
            }
            return true;
        }
    }

    private class ViewFinderCaptureAreaNonObjectTrackingStateListener
    extends ViewFinderCaptureAreaStateListener {
        public ViewFinderCaptureAreaNonObjectTrackingStateListener(int n) {
            super(n);
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_AF_SEARCH_IN_TOUCH, new Object[0]);
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.MOVE);
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
            Object[] arrobject = new Object[]{Boolean.TRUE};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }
    }

    private class ViewFinderCaptureAreaStateListener
    extends ViewFinderTouchActionStateListener {
        public ViewFinderCaptureAreaStateListener(int n) {
            super(n);
        }

        @Override
        public void onCaptureAreaLongPressed(Point point) {
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
            Object[] arrobject = new Object[]{Boolean.TRUE};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onCaptureAreaReleased(Point point) {
            switch (this.mCameraType) {
                case 1: {
                    StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                    StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
                    Object[] arrobject = new Object[]{StateMachine.TouchEventSource.CAPTURE_AREA};
                    stateMachine.sendEvent(transitterEvent, arrobject);
                }
                default: {
                    break;
                }
                case 2: {
                    if (FastViewFinder.this.mStateMachine.isInModeLessRecording()) {
                        StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
                        Object[] arrobject = new Object[]{StateMachine.TouchEventSource.CAPTURE_AREA};
                        stateMachine.sendEvent(transitterEvent, arrobject);
                        break;
                    }
                    FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAMCORD_BUTTON_RELEASE, new Object[0]);
                }
            }
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaSingleTapUp(Point point) {
            PointF pointF = FastViewFinder.this.convertTouchPointToDevicePreviewPositionRatio(point);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_START_AF_AFTER_OBJECT_TRACKED, new Object[]{point, pointF});
            switch (this.mCameraType) {
                default: {
                    return;
                }
                case 1: 
            }
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
            Object[] arrobject = new Object[]{StateMachine.TouchEventSource.CAPTURE_AREA};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }

        @Override
        public void onCaptureAreaStopped() {
            if (FastViewFinder.this.mTouchCapture.equals((Object)TouchCapture.ON)) {
                StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
                Object[] arrobject = new Object[]{Boolean.TRUE};
                stateMachine.sendEvent(transitterEvent, arrobject);
            }
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
            switch (this.mCameraType) {
                default: {
                    return;
                }
                case 2: 
            }
            switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$FocusMode[FastViewFinder.this.mVideoFocusMode.ordinal()]) {
                default: {
                    return;
                }
                case 1: 
            }
            this.setSelectedObjectPositionToDeviceAndViewFinder(point);
        }
    }

    private class ViewFinderStateListener
    implements CaptureArea.CaptureAreaStateListener {
        @Override
        public void onCaptureAreaCanceled() {
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SCREEN_CLEAR, new Object[0]);
        }

        @Override
        public void onCaptureAreaIsReadyToScale() {
            FastViewFinder.this.mActivity.getMessagePopup().showZoomHelpMessage(false);
        }

        @Override
        public void onCaptureAreaLongPressed(Point point) {
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
        }

        @Override
        public void onCaptureAreaReleased(Point point) {
        }

        @Override
        public void onCaptureAreaScaled(float f) {
        }

        @Override
        public void onCaptureAreaSingleTapUp(Point point) {
        }

        @Override
        public void onCaptureAreaStopped() {
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
        }
    }

    private class ViewFinderTouchActionNonObjectTrackingStateListener
    extends ViewFinderTouchActionStateListener {
        public ViewFinderTouchActionNonObjectTrackingStateListener(int n) {
            super(n);
        }

        @Override
        public void onCaptureAreaLongPressed(Point point) {
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.MOVE);
        }

        @Override
        public void onCaptureAreaSingleTapUp(Point point) {
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
        }
    }

    private class ViewFinderTouchActionStateListener
    extends ViewFinderStateListener {
        protected final int mCameraType;

        public ViewFinderTouchActionStateListener(int n) {
            this.mCameraType = n;
        }

        @Override
        public void onCaptureAreaCanceled() {
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SCREEN_CLEAR, new Object[0]);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_DESELECT_OBJECT_POSITION, new Object[0]);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaIsReadyToScale() {
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PREPARE_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
            this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.MOVE);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onCaptureAreaReleased(Point point) {
            switch (this.mCameraType) {
                case 1: {
                    this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.RELEASE);
                }
                default: {
                    break;
                }
                case 2: {
                    FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SCREEN_CLEAR, new Object[0]);
                }
            }
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaScaled(float f) {
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_START_TOUCH_ZOOM;
            Object[] arrobject = new Object[]{Float.valueOf((float)f)};
            stateMachine.sendEvent(transitterEvent, arrobject);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STOP_TOUCH_ZOOM, new Object[0]);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
            switch (this.mCameraType) {
                default: {
                    return;
                }
                case 1: {
                    this.setFocusPositionToDeviceAndViewFinder(point, FocusRectangles.FocusSetType.FIRST);
                    return;
                }
                case 2: {
                    if (FastViewFinder.this.mVideoFocusMode != FocusMode.OBJECT_TRACKING) return;
                    this.setSelectedObjectPositionToDeviceAndViewFinder(point);
                    return;
                }
            }
        }

        protected void setFocusPositionToDeviceAndViewFinder(Point point, FocusRectangles.FocusSetType focusSetType) {
            PointF pointF = FastViewFinder.this.convertTouchPointToDevicePreviewPositionRatio(point);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SET_FOCUS_POSITION, new Object[]{point, pointF, focusSetType});
        }

        protected void setSelectedObjectPositionToDeviceAndViewFinder(Point point) {
            PointF pointF = FastViewFinder.this.convertTouchPointToDevicePreviewPositionRatio(point);
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_SET_SELECTED_OBJECT_POSITION, new Object[]{point, pointF});
        }
    }

    private class ViewFinderTouchCaptureStateListener
    extends ViewFinderStateListener {
        protected final int mCameraType;

        public ViewFinderTouchCaptureStateListener(int n) {
            this.mCameraType = n;
        }

        @Override
        public void onCaptureAreaLongPressed(Point point) {
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
            Object[] arrobject = new Object[]{Boolean.TRUE};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }

        @Override
        public void onCaptureAreaReleased(Point point) {
            switch (this.mCameraType) {
                default: {
                    return;
                }
                case 1: {
                    StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                    StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
                    Object[] arrobject = new Object[]{StateMachine.TouchEventSource.CAPTURE_AREA};
                    stateMachine.sendEvent(transitterEvent, arrobject);
                    return;
                }
                case 2: 
            }
            if (FastViewFinder.this.mStateMachine.isInModeLessRecording()) {
                StateMachine stateMachine = FastViewFinder.this.mStateMachine;
                StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
                Object[] arrobject = new Object[]{StateMachine.TouchEventSource.CAPTURE_AREA};
                stateMachine.sendEvent(transitterEvent, arrobject);
                return;
            }
            FastViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAMCORD_BUTTON_RELEASE, new Object[0]);
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
            FastViewFinder.this.hideBalloonTips();
            StateMachine stateMachine = FastViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_TOUCH;
            Object[] arrobject = new Object[]{Boolean.TRUE};
            stateMachine.sendEvent(transitterEvent, arrobject);
        }
    }

}

