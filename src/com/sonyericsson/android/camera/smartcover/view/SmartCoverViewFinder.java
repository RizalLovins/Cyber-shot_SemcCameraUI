/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.net.Uri
 *  android.os.Handler
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  android.view.SurfaceView
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.Window
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.RelativeLayout
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$SceneRecognitionResult
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.List
 */
package com.sonyericsson.android.camera.smartcover.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.android.camera.fastcapturing.view.BaseFastViewFinder;
import com.sonyericsson.android.camera.fastcapturing.view.CaptureArea;
import com.sonyericsson.android.camera.fastcapturing.view.FastLayoutAsyncInflateItems;
import com.sonyericsson.android.camera.smartcover.SmartCoverActivity;
import com.sonyericsson.android.camera.view.LayoutAsyncInflateItems;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationFactory;
import com.sonyericsson.cameracommon.capturefeedback.contextview.TextureContextView;
import com.sonyericsson.cameracommon.commonsetting.values.TouchCapture;
import com.sonyericsson.cameracommon.contentsview.ContentPallet;
import com.sonyericsson.cameracommon.contentsview.ContentsViewController;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.focusview.FocusActionListener;
import com.sonyericsson.cameracommon.mediasaving.StorageAutoSwitchController;
import com.sonyericsson.cameracommon.mediasaving.updator.ContentResolverUtilListener;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.cameracommon.utility.ParamSharedPrefWrapper;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.BaseViewFinderLayout;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.DefaultLayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import com.sonyericsson.cameracommon.viewfinder.LayoutPatternApplier;
import com.sonyericsson.cameracommon.viewfinder.ViewFinder;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButton;
import com.sonyericsson.cameracommon.viewfinder.indicators.GeotagIndicator;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButton;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButtonListener;
import com.sonyericsson.cameracommon.zoombar.Zoombar;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.coversupport.CoverModeHandler;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class SmartCoverViewFinder
extends ViewFinder
implements SurfaceHolder.Callback,
StateMachine.OnStateChangedListener,
BaseFastViewFinder,
FocusActionListener,
StorageAutoSwitchController.StorageAutoSwitchListener,
ContentResolverUtilListener {
    public static final String ACTION_COVER_MODE_ALBUM = "com.sonymobile.coverapp.ACTION_COVER_MODE_ALBUM";
    private static final OnScreenButton.Resource BACK_BUTTON_NOT_PRESSED;
    private static final OnScreenButton.Resource BACK_BUTTON_PRESSED;
    private static final String TAG;
    private SmartCoverActivity mActivity;
    private OnScreenButton mBackButton;
    private CameraDeviceHandler mCameraDevice = null;
    private CaptureFeedback mCaptureFeedback = null;
    private CoverModeHandler.WindowInfo mCoverModeWindowInfo;
    private Boolean mIsFaceDetectionIdSupported;
    private boolean mIsSetupHeadupDisplayInvoked;
    private StateMachine mStateMachine;
    private SurfaceHolder mSurfaceHolder = null;
    private SurfaceView mSurfaceView = null;
    private TouchCapture mTouchCapture;
    private CaptureArea mViewFinderCaptureArea;

    static {
        TAG = SmartCoverViewFinder.class.getSimpleName();
        BACK_BUTTON_PRESSED = new OnScreenButton.Resource(2130837705, 2130837705, -1);
        BACK_BUTTON_NOT_PRESSED = new OnScreenButton.Resource(2130837704, 2130837704, -1);
    }

    public SmartCoverViewFinder(Context context) {
        super((BaseActivity)((SmartCoverActivity)context), new DefaultLayoutPatternApplier(), null);
        this.requestToRemoveSystemUi();
        this.mActivity = (SmartCoverActivity)context;
        this.mActivity.getWindow().addFlags(524288);
        this.mActivity.getWindow().addFlags(128);
        this.mSurfaceView = new SurfaceView((Context)this.mActivity);
        this.mSurfaceView.getHolder().addCallback((SurfaceHolder.Callback)this);
        this.disableAccessibilityTalkBack();
        this.mIsSetupHeadupDisplayInvoked = false;
    }

    private void addBackButton() {
        this.mBackButton = new OnScreenButton((Context)this.mActivity);
        ((FrameLayout)this.getBaseLayout().getRootView().findViewById(2131689630)).addView((View)this.mBackButton);
    }

    private void changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        this.getBaseLayout().getOnScreenButtonGroup().setMain(OnScreenButtonGroup.ButtonType.CAPTURE);
        this.getBaseLayout().getOnScreenButtonGroup().setSub(OnScreenButtonGroup.ButtonType.NONE);
    }

    private void changeToPauseView() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.CLEAR);
        if (!this.isHeadUpDesplayReady()) {
            // empty if block
        }
    }

    private void changeToPhotoCaptureView() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
        if (!this.isHeadUpDesplayReady()) {
            // empty if block
        }
    }

    private void changeToPhotoCaptureWaitForAfDoneView() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
        if (!this.isHeadUpDesplayReady()) {
            // empty if block
        }
    }

    private void changeToPhotoFocusDoneView(Boolean bl) {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.FOCUS_DONE);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.changeToPhotoFocusView();
    }

    private void changeToPhotoFocusSearchView() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.FOCUS_SEARCHING);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.changeToPhotoFocusView();
    }

    private void changeToPhotoFocusView() {
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().hideLeftIconContainer();
    }

    private void changeToPhotoIdleView(boolean bl) {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(0);
        }
        this.changeLayoutTo(DefaultLayoutPattern.PREVIEW);
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        super.changeScreenButtonImage(BaseFastViewFinder.HeadUpDisplaySetupState.PHOTO_STANDBY);
        this.getBaseLayout().showLeftIconContainer();
    }

    private void changeToPhotoZoomingView() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.ZOOMING);
        if (this.getBaseLayout() != null && this.getBaseLayout().getZoomBar() != null) {
            this.getBaseLayout().getZoomBar().hide();
        }
        if (!this.isHeadUpDesplayReady()) {
            return;
        }
        this.getBaseLayout().hideLeftIconContainer();
    }

    private void checkSurfaceIsPreparedOrNot(SurfaceHolder surfaceHolder, int n, int n2) {
        if (this.mCameraDevice == null) {
            return;
        }
        Rect rect = this.mCameraDevice.getPreviewRect();
        if (rect == null) {
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
        if (SmartCoverViewFinder.isNearSameSize(rect3, rect2)) {
            super.notifyOnEvfPrepared(rect);
            return;
        }
        super.resizeEvfScope(rect3.width(), rect3.height());
    }

    public static final boolean isNearSameSize(Rect rect, Rect rect2) {
        if (rect.width() == rect2.width() && rect.height() == rect2.height()) {
            return true;
        }
        return false;
    }

    private void loadSettingsFromSharedPreferencesDeviceAndResourcees() {
        this.mTouchCapture = TouchCapture.OFF;
    }

    private static void logPerformance(String string) {
        Log.e((String)"TraceLog", (String)("[PERFORMANCE] [TIME = " + System.currentTimeMillis() + "] [" + TAG + "] [" + Thread.currentThread().getName() + " : " + string + "]"));
    }

    private void notifyOnEvfPrepared(Rect rect) {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_ON_EVF_PREPARED;
        Object[] arrobject = new Object[]{this.mSurfaceHolder};
        stateMachine.sendEvent(transitterEvent, arrobject);
    }

    private void onSceneModeChanged(CameraExtension.SceneRecognitionResult sceneRecognitionResult) {
    }

    private void onSurfaceAvailableInternal(SurfaceHolder surfaceHolder, int n, int n2) {
        super.checkSurfaceIsPreparedOrNot(surfaceHolder, n, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private /* varargs */ void onViewFinderStateChanged(StateMachine.CaptureState captureState, Object ... arrobject) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$fastcapturing$StateMachine$CaptureState[captureState.ordinal()]) {
            default: {
                return;
            }
            case 3: {
                this.mActivity.setShouldStartFastCapture(true);
                super.resumeView();
                this.mSurfaceView.setVisibility(0);
                return;
            }
            case 4: {
                super.changeToPhotoIdleView(false);
                return;
            }
            case 5: 
            case 6: {
                super.changeToPhotoZoomingView();
                return;
            }
            case 7: 
            case 8: {
                super.changeToPhotoFocusSearchView();
                return;
            }
            case 9: {
                super.changeToPhotoFocusSearchView();
                return;
            }
            case 10: 
            case 11: {
                super.changeToPhotoFocusDoneView((Boolean)arrobject[0]);
                return;
            }
            case 12: {
                super.changeToPhotoCaptureWaitForAfDoneView();
                return;
            }
            case 13: {
                super.changeToPhotoCaptureView();
                return;
            }
            case 16: {
                this.getBaseLayout().showContentsViewController();
                return;
            }
            case 17: {
                this.mActivity.setShouldStartFastCapture(false);
                this.mSurfaceView.setVisibility(8);
                super.pauseView();
                super.changeToPauseView();
                return;
            }
            case 18: {
                super.changeToPhotoIdleView(false);
                return;
            }
            case 19: {
                this.mSurfaceView.getHolder().removeCallback((SurfaceHolder.Callback)this);
                this.mSurfaceView = null;
                this.mCoverModeWindowInfo = null;
                if (this.mBackButton != null) {
                    super.removeBackButton();
                }
                if (this.mCaptureFeedback == null) return;
                this.mCaptureFeedback.release();
                return;
            }
        }
    }

    private void pauseView() {
        this.pause();
        this.clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = false;
        this.mTouchCapture = null;
    }

    private void removeBackButton() {
        ((FrameLayout)this.getBaseLayout().getRootView().findViewById(2131689630)).removeView((View)this.mBackButton);
        this.mBackButton = null;
    }

    private void resizeEvfScope(int n, int n2) {
        if (this.mSurfaceView.getWidth() == n && this.mSurfaceView.getHeight() == n2) {
            throw new IllegalArgumentException("resizeEvfScope():[Already resized]");
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mSurfaceView.getLayoutParams();
        layoutParams.width = n;
        layoutParams.height = n2;
        layoutParams.gravity = 19;
        this.mSurfaceView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void resizeViewfinderSize() {
        RelativeLayout relativeLayout = (RelativeLayout)this.getBaseLayout().getRootView().findViewById(2131689559);
        if (relativeLayout != null && this.mCoverModeWindowInfo != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)relativeLayout.getLayoutParams();
            layoutParams.width = this.mCoverModeWindowInfo.width;
            layoutParams.height = this.mCoverModeWindowInfo.height;
            int n = this.mCoverModeWindowInfo.appPadding;
            relativeLayout.setPadding(n, n, n, n);
        }
    }

    private void resumeView() {
        if (this.isHeadUpDesplayReady()) {
            this.resume();
        }
    }

    private void setEarlyThumbnailView(View view) {
        if (this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().setEarlyThumbnailView(view);
        }
    }

    private void setupBackButton() {
        if (this.mBackButton == null) {
            this.addBackButton();
        }
        this.mBackButton.set(BACK_BUTTON_NOT_PRESSED);
        this.mBackButton.setListener(new BackButtonListener(this, null));
        this.mBackButton.setOnClickListener((View.OnClickListener)new View.OnClickListener(){

            public void onClick(View view) {
            }
        });
        String string = this.getActivity().getResources().getString(2131362147);
        this.mBackButton.setContentDescription((CharSequence)string);
        ((FrameLayout.LayoutParams)this.mBackButton.getLayoutParams()).width = this.mActivity.getResources().getDimensionPixelSize(2131296448);
        ((FrameLayout.LayoutParams)this.mBackButton.getLayoutParams()).height = this.mActivity.getResources().getDimensionPixelSize(2131296449);
        ((FrameLayout.LayoutParams)this.mBackButton.getLayoutParams()).gravity = 81;
    }

    private void setupCaptureArea(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mViewFinderCaptureArea == null) {
            this.mViewFinderCaptureArea = (CaptureArea)this.mActivity.findViewById(2131689542);
        }
        super.updateViewFinderCaptureAreaTouchEventHandling();
    }

    private void setupContentsView() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.StaticEvent staticEvent = StateMachine.StaticEvent.EVENT_ON_PHOTO_STACK_INITIALIZED;
        Object[] arrobject = new Object[]{this.getBaseLayout().getContentsViewController()};
        stateMachine.sendStaticEvent(staticEvent, arrobject);
    }

    private void setupHeadUpDisplay(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        if (this.mActivity.isDeviceInSecurityLock() && this.mIsSetupHeadupDisplayInvoked) {
            return;
        }
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
        super.setupContentsView();
        super.setupCaptureArea(headUpDisplaySetupState);
        super.setupOnScreenCaptureButton(headUpDisplaySetupState);
        super.setupRightContainer();
        this.setOrientation(this.getBaseLayout().getCurrentOrientation());
        super.changeToPhotoIdleView(true);
        this.mStateMachine.sendStaticEvent(StateMachine.StaticEvent.EVENT_ON_HEAD_UP_DISPLAY_INITIALIZED, new Object[]{headUpDisplaySetupState});
        this.clearPreInflatedViews();
        this.mIsSetupHeadupDisplayInvoked = true;
    }

    private void setupOnScreenCaptureButton(BaseFastViewFinder.HeadUpDisplaySetupState headUpDisplaySetupState) {
        super.changeScreenButtonImage(headUpDisplaySetupState);
        OnScreenCaptureButtonStateListener onScreenCaptureButtonStateListener = new OnScreenCaptureButtonStateListener((SmartCoverViewFinder)this, null);
        this.getBaseLayout().getOnScreenButtonGroup().setListener(OnScreenButtonGroup.ButtonType.CAPTURE, onScreenCaptureButtonStateListener);
    }

    private void setupRightContainer() {
        FrameLayout frameLayout = (FrameLayout)this.getBaseLayout().getRootView().findViewById(2131689619);
        if (frameLayout != null) {
            if (this.mCoverModeWindowInfo != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)frameLayout.getLayoutParams();
                layoutParams.width = this.mCoverModeWindowInfo.width;
                layoutParams.height = this.mCoverModeWindowInfo.height;
                int n = this.mCoverModeWindowInfo.appPadding;
                frameLayout.setPadding(n, n, n, n);
            }
            int n = (this.mActivity.getResources().getDimensionPixelSize(2131296425) - this.mActivity.getResources().getDimensionPixelSize(2131296260)) / 2;
            ((ViewGroup.MarginLayoutParams)this.getBaseLayout().getRootView().findViewById(2131689521).getLayoutParams()).setMargins(0, n, 0, 0);
            ((ViewGroup.MarginLayoutParams)this.getBaseLayout().getRootView().findViewById(2131689624).getLayoutParams()).setMargins(0, 0, 0, 0);
            this.getBaseLayout().getRootView().findViewById(2131689630).setPadding(0, 0, 0, 0);
            this.setupBackButton();
        }
    }

    private RelativeLayout setupViewFinderLayout() {
        boolean bl = this.isInflated();
        RelativeLayout relativeLayout = null;
        if (bl) {
            relativeLayout = (RelativeLayout)this.getPreInflatedView((InflateItem)LayoutAsyncInflateItems.CameraInflateItem.SMART_COVER_VIEWFINDER_ITEMS).get(0);
        }
        if (relativeLayout == null) {
            relativeLayout = (RelativeLayout)LayoutInflater.from((Context)this.mActivity).inflate(2130903081, null);
        }
        TextureContextView textureContextView = new TextureContextView((Context)this.getActivity());
        relativeLayout.addView((View)textureContextView);
        textureContextView.getLayoutParams().width = -1;
        textureContextView.getLayoutParams().height = -1;
        this.mCaptureFeedback = textureContextView;
        return relativeLayout;
    }

    private void startEarlyThumbnailInsertAnimation(int n) {
        if (this.getBaseLayout().getContentsViewController() != null) {
            this.getBaseLayout().getContentsViewController().startInsertAnimation(n);
        }
    }

    private void updateViewFinderCaptureAreaTouchEventHandling() {
        this.mViewFinderCaptureArea.setCaptureAreaStateListener(new ViewFinderPinchZoomListener(1));
    }

    public boolean canSwitchStorage() {
        return this.mStateMachine.isMenuAvailable();
    }

    public void changeLayoutTo(LayoutPattern layoutPattern) {
        super.changeLayoutTo(layoutPattern);
        if (this.getBaseLayout().getGeoTagIndicator() != null) {
            this.getBaseLayout().getGeoTagIndicator().hide();
        }
        if (layoutPattern.equals((Object)DefaultLayoutPattern.PREVIEW) && this.getBaseLayout().getCapturingModeButton() != null) {
            this.getBaseLayout().getCapturingModeButton().setVisibility(4);
        }
    }

    public void checkSurfaceIsPreparedOrNot() {
        this.checkSurfaceIsPreparedOrNot(this.mSurfaceHolder, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight());
    }

    protected Rect computePreviewRect(Activity activity, int n, int n2) {
        if (n == 0 || n2 == 0) {
            CameraLogger.e(TAG, "Preview size is not set.");
            return new Rect(0, 0, 0, 0);
        }
        if (this.mCoverModeWindowInfo == null) {
            return LayoutDependencyResolver.getSurfaceRect(activity, (float)n / (float)n2);
        }
        float f = (float)n / (float)n2;
        float f2 = (float)this.mCoverModeWindowInfo.width / (float)this.mCoverModeWindowInfo.height;
        Rect rect = new Rect();
        if (f > f2) {
            rect.set(0, 0, this.mCoverModeWindowInfo.width, (int)((float)this.mCoverModeWindowInfo.width / f));
            return rect;
        }
        rect.set(0, 0, (int)(f * (float)this.mCoverModeWindowInfo.height), this.mCoverModeWindowInfo.height);
        return rect;
    }

    public void ensureVideoAutoReviewSettingHasLoaded() {
    }

    public void fadeoutCounter() {
    }

    public AutoReview getAutoReviewSetting() {
        return AutoReview.OFF;
    }

    public int getOrientation() {
        return this.getBaseLayout().getCurrentOrientation();
    }

    public SelfTimer getPhotoSelfTimerSetting() {
        return SelfTimer.OFF;
    }

    public int getRequestId(boolean bl) {
        int n = -1;
        if (this.getBaseLayout().getContentsViewController() != null) {
            if (bl) {
                n = this.getBaseLayout().getContentsViewController().createContentFrame();
            }
        } else {
            return n;
        }
        return this.getBaseLayout().getContentsViewController().createClearContentFrame();
    }

    protected ContentPallet.ThumbnailClickListener getThumbnailClickListener() {
        return new ContentPallet.ThumbnailClickListener(){

            @Override
            public void onClick(Content content) {
                SmartCoverViewFinder.this.mActivity.startActivity(new Intent("com.sonymobile.coverapp.ACTION_COVER_MODE_ALBUM"));
            }
        };
    }

    public TouchCapture getTouchCapture() {
        return this.mTouchCapture;
    }

    public VideoAutoReview getVideoAutoReviewSetting() {
        return VideoAutoReview.OFF;
    }

    public void hideHudIcons() {
        if (this.mBackButton != null) {
            this.mBackButton.setVisibility(4);
        }
        this.changeLayoutTo(DefaultLayoutPattern.CAPTURE);
    }

    public boolean isSetupHeadupDisplayInvoked() {
        return this.mIsSetupHeadupDisplayInvoked;
    }

    public boolean isTouchFocus() {
        return false;
    }

    public void onCanceled() {
        this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_CANCEL, new Object[0]);
    }

    public void onCloseCapturingModeSelector() {
    }

    public void onCloseStorageDialog() {
    }

    public void onDeleted(boolean bl, Uri uri) {
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
    }

    public void onModeSelect(String string) {
    }

    protected void onOpenCapturingModeSelector() {
    }

    public void onOpenStorageDialog() {
    }

    public void onReleased() {
        StateMachine stateMachine = this.mStateMachine;
        StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_CAPTURE_BUTTON_RELEASE;
        Object[] arrobject = new Object[]{StateMachine.TouchEventSource.FACE};
        stateMachine.sendEvent(transitterEvent, arrobject);
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
                if (SmartCoverViewFinder.isNearSameSize(rect, new Rect(0, 0, this.mSurfaceView.getWidth(), this.mSurfaceView.getHeight()))) return;
                super.resizeEvfScope(n, n2);
                return;
            }
            case 3: {
                if (this.getBaseLayout().getLowMemoryIndicator() == null) return;
                this.getBaseLayout().getLowMemoryIndicator().set(false);
                return;
            }
            case 4: {
                if (this.getBaseLayout().getLowMemoryIndicator() == null) return;
                this.getBaseLayout().getLowMemoryIndicator().set(true);
                return;
            }
            case 5: {
                super.onSceneModeChanged((CameraExtension.SceneRecognitionResult)arrobject[0]);
                return;
            }
            case 6: {
                CameraExtension.FaceDetectionResult faceDetectionResult = (CameraExtension.FaceDetectionResult)arrobject[0];
                if (this.mIsFaceDetectionIdSupported == null) {
                    if (faceDetectionResult.extFaceList.isEmpty()) return;
                    this.mIsFaceDetectionIdSupported = FaceDetectUtil.hasValidFaceId(faceDetectionResult);
                    return;
                }
                if (this.mIsFaceDetectionIdSupported != false) return;
                FaceDetectUtil.setUuidFaceDetectionResult(faceDetectionResult);
                return;
            }
            case 8: {
                this.setOrientation((Integer)arrobject[0]);
                return;
            }
            case 9: {
                this.startCaptureFeedbackAnimation();
                return;
            }
            case 10: {
                super.setEarlyThumbnailView((View)arrobject[0]);
                return;
            }
            case 11: {
                this.removeEarlyThumbnailView();
                return;
            }
            case 12: 
        }
        int n = (Integer)arrobject[0];
        if (arrobject.length > 1) {
            this.startEarlyThumbnailInsertAnimation(n, (Animation.AnimationListener)arrobject[1]);
            return;
        }
        super.startEarlyThumbnailInsertAnimation(n);
    }

    public void setCameraDevice(CameraDeviceHandler cameraDeviceHandler) {
        this.mCameraDevice = cameraDeviceHandler;
    }

    public void setContentView() {
        this.setup((View)this.mSurfaceView);
    }

    public void setCoverModeSizeInPixel(CoverModeHandler.WindowInfo windowInfo) {
        this.mCoverModeWindowInfo = windowInfo;
        if (this.mCoverModeWindowInfo != null) {
            super.resizeViewfinderSize();
            super.setupRightContainer();
            this.checkSurfaceIsPreparedOrNot();
        }
    }

    protected void setOrientation(int n) {
        super.setOrientation(n);
        if (this.mBackButton != null) {
            float f = RotationUtil.getAngle(n);
            this.mBackButton.setRotation(f);
        }
    }

    public void setRecordingOrientation(int n) {
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
        this.getBaseLayout().getContentsViewController().stopAnimation(true);
        Animation animation = AnimationUtils.loadAnimation((Context)this.mActivity, (int)2130968592);
        animation.setAnimationListener((Animation.AnimationListener)new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.getBaseLayout().getContentsViewController().startAnimation(animation);
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

    private class BackButtonListener
    implements OnScreenButtonListener {
        final /* synthetic */ SmartCoverViewFinder this$0;

        private BackButtonListener(SmartCoverViewFinder smartCoverViewFinder) {
            this.this$0 = smartCoverViewFinder;
        }

        /* synthetic */ BackButtonListener(SmartCoverViewFinder smartCoverViewFinder,  var2_2) {
            super(smartCoverViewFinder);
        }

        @Override
        public void onCancel(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            onScreenButton.set(BACK_BUTTON_NOT_PRESSED);
        }

        @Override
        public void onDispatchDraw(OnScreenButton onScreenButton, Canvas canvas) {
        }

        @Override
        public void onDown(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            onScreenButton.set(BACK_BUTTON_PRESSED);
        }

        @Override
        public void onMove(OnScreenButton onScreenButton, MotionEvent motionEvent) {
        }

        @Override
        public void onUp(OnScreenButton onScreenButton, MotionEvent motionEvent) {
            onScreenButton.set(BACK_BUTTON_NOT_PRESSED);
            this.this$0.mActivity.abort();
        }
    }

    private class OnScreenCaptureButtonStateListener
    implements OnScreenButtonListener {
        final /* synthetic */ SmartCoverViewFinder this$0;

        private OnScreenCaptureButtonStateListener(SmartCoverViewFinder smartCoverViewFinder) {
            this.this$0 = smartCoverViewFinder;
        }

        /* synthetic */ OnScreenCaptureButtonStateListener(SmartCoverViewFinder smartCoverViewFinder,  var2_2) {
            super(smartCoverViewFinder);
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
    class ReTrySetupHeadUpDisplayTask
    implements Runnable {
        ReTrySetupHeadUpDisplayTask() {
        }

        public void run() {
            if (SmartCoverViewFinder.this.mStateMachine == null) {
                return;
            }
            SmartCoverViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, new Object[0]);
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
            SmartCoverViewFinder.this.onSurfaceAvailableInternal(this.mSurface, this.mWidth, this.mHeight);
        }
    }

    private class ViewFinderPinchZoomListener
    implements CaptureArea.CaptureAreaStateListener {
        public ViewFinderPinchZoomListener(int n) {
        }

        @Override
        public void onCaptureAreaCanceled() {
            if (SmartCoverViewFinder.this.mStateMachine != null) {
                SmartCoverViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
            }
        }

        @Override
        public void onCaptureAreaIsReadyToScale() {
            SmartCoverViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_PREPARE_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaLongPressed(Point point) {
        }

        @Override
        public void onCaptureAreaMoved(Point point) {
        }

        @Override
        public void onCaptureAreaReleased(Point point) {
            SmartCoverViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_CANCEL_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaScaled(float f) {
            StateMachine stateMachine = SmartCoverViewFinder.this.mStateMachine;
            StateMachine.TransitterEvent transitterEvent = StateMachine.TransitterEvent.EVENT_START_TOUCH_ZOOM;
            Object[] arrobject = new Object[]{Float.valueOf((float)f)};
            stateMachine.sendEvent(transitterEvent, arrobject);
            SmartCoverViewFinder.this.mStateMachine.sendEvent(StateMachine.TransitterEvent.EVENT_STOP_TOUCH_ZOOM, new Object[0]);
        }

        @Override
        public void onCaptureAreaSingleTapUp(Point point) {
        }

        @Override
        public void onCaptureAreaStopped() {
        }

        @Override
        public void onCaptureAreaTouched(Point point) {
        }
    }

}

