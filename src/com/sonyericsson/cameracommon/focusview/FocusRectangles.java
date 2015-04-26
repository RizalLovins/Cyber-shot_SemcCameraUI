/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Animation
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  com.sonyericsson.cameraextension.CameraExtension
 *  com.sonyericsson.cameraextension.CameraExtension$FaceDetectionResult
 *  com.sonyericsson.cameraextension.CameraExtension$ObjectTrackingResult
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package com.sonyericsson.cameracommon.focusview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.animation.FocusRectanglesAnimation;
import com.sonyericsson.cameracommon.focusview.CommonResources;
import com.sonyericsson.cameracommon.focusview.FaceInformationList;
import com.sonyericsson.cameracommon.focusview.FocusActionListener;
import com.sonyericsson.cameracommon.focusview.FocusRectanglesViewList;
import com.sonyericsson.cameracommon.focusview.NamedFace;
import com.sonyericsson.cameracommon.focusview.Rectangle;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameraextension.CameraExtension;
import com.sonymobile.cameracommon.photoanalyzer.faceidentification.FaceIdentification;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FocusRectangles {
    private static final int FACE_RECT_REFRESH_TIMEOUT = 200;
    private static final int FOCUS_RECT_SET_DOWN_ANIMATION_START_DELAY_TIME = 100;
    private static final String TAG = FocusRectangles.class.getSimpleName();
    private static final int TRACKED_OBJECT_RECT_REFRESH_TIMEOUT = 1000;
    private Activity mActivity;
    private FocusRectanglesAnimation mAnimation;
    private View mCaptureArea;
    private int mCurrentOrientation;
    private State mCurrentState;
    private int mDevicePreviewHeight;
    private int mDevicePreviewWidth;
    private HashMap<String, TaggedRectangle> mFaceRectangles;
    private FocusActionListener mFocusEventListener;
    private Handler mHandler = new Handler();
    private boolean mIsFaceTouchCaptureEnabled;
    private boolean mIsFocusAnimationEnabled;
    private boolean mIsRecording;
    private boolean mIsRectPositionMirrored;
    private CameraExtension.FaceDetectionResult mLastFaceDetectionResult;
    private String mLatestSelectedFaceUuid;
    private final OnFaceRectTouchListener mOnFaceRectTouchListener;
    private RelativeLayout mRectangles;
    private final RefreshTrackedObjectRectangleTask mRefreshTrackedObjectRectangleTask;
    private RelativeLayout mSingleAfRect;
    private RelativeLayout mTouchAfRect;
    private TaggedRectangle mTrackedObjectRectangle;
    private String mUserTouchFaceUuid;

    public FocusRectangles(Activity activity, FocusActionListener focusActionListener, int n, int n2, FocusRectanglesViewList focusRectanglesViewList, View view) {
        this.mRefreshTrackedObjectRectangleTask = new RefreshTrackedObjectRectangleTask();
        this.mOnFaceRectTouchListener = new OnFaceRectTouchListener();
        this.mIsFaceTouchCaptureEnabled = false;
        this.mIsFocusAnimationEnabled = false;
        this.mCurrentState = new DefaultFocusState();
        this.mIsRecording = false;
        this.mLatestSelectedFaceUuid = null;
        this.mUserTouchFaceUuid = null;
        this.mCurrentOrientation = 2;
        this.mIsRectPositionMirrored = false;
        this.mActivity = activity;
        this.mFocusEventListener = focusActionListener;
        this.mDevicePreviewWidth = n;
        this.mDevicePreviewHeight = n2;
        this.mAnimation = new FocusRectanglesAnimation((Context)this.mActivity);
        this.mCaptureArea = view;
        this.initialize(focusRectanglesViewList);
    }

    /*
     * Enabled aggressive block sorting
     */
    private TaggedRectangle addTaggedRectangle(LayoutInflater layoutInflater, String string, TaggedRectangle taggedRectangle) {
        if (this.mFaceRectangles.size() >= 5) {
            return null;
        }
        Rect rect = new Rect();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        TaggedRectangle taggedRectangle2 = taggedRectangle != null ? taggedRectangle : (TaggedRectangle)layoutInflater.inflate(R.layout.face_rectangle, null);
        this.mRectangles.addView((View)taggedRectangle2, layoutParams);
        taggedRectangle2.prepare(0);
        taggedRectangle2.setRectSize(rect.width(), rect.height());
        taggedRectangle2.setRectCenter(rect.centerX(), rect.centerY());
        taggedRectangle2.setRectangleOnTouchListener(this.mOnFaceRectTouchListener);
        this.mFaceRectangles.put((Object)string, (Object)taggedRectangle2);
        return taggedRectangle2;
    }

    private void changeFacePriority(String string) {
        TaggedRectangle taggedRectangle = (TaggedRectangle)this.mFaceRectangles.get((Object)string);
        if (taggedRectangle == null) {
            CameraLogger.e(TAG, "changeFacePriority() faceUuid " + string + " not found.");
            return;
        }
        Rect rect = taggedRectangle.getFaceRect();
        Rect rect2 = PositionConverter.getInstance().convertFaceToDevice(rect);
        Point point = new Point(rect2.centerX(), rect2.centerY());
        this.mFocusEventListener.onFaceSelected(point);
    }

    private void changeState(State state) {
        this.mCurrentState = state;
    }

    private void faceResultToRectangles(List<FaceIdentification.FaceIdentificationResult> list, CameraExtension.FaceDetectionResult faceDetectionResult, boolean bl) {
        Rect rect = new Rect(0, 0, this.mDevicePreviewWidth, this.mDevicePreviewHeight);
        FaceInformationList faceInformationList = FaceDetectUtil.getFaceInformationList(list, this.mLastFaceDetectionResult, rect, this.mUserTouchFaceUuid);
        if (faceInformationList == null) {
            return;
        }
        super.updateFaceRectangles(faceInformationList, this.mCurrentOrientation, bl);
    }

    private void hideFaceRectangles(boolean bl) {
        for (String string : this.mFaceRectangles.keySet()) {
            if (bl) {
                ((TaggedRectangle)this.mFaceRectangles.get((Object)string)).changeRectangleResource(0);
            }
            ((TaggedRectangle)this.mFaceRectangles.get((Object)string)).hide();
        }
    }

    private void hideTrackedObjectRecgantle() {
        this.mTrackedObjectRectangle.setVisibility(4);
    }

    private void initialize(FocusRectanglesViewList focusRectanglesViewList) {
        this.mRectangles = focusRectanglesViewList.rectanglesContainer;
        LayoutInflater layoutInflater = this.mActivity.getLayoutInflater();
        this.mFaceRectangles = new HashMap();
        View[] arrview = focusRectanglesViewList.faceViewList;
        View[] arrview2 = null;
        if (arrview != null) {
            arrview2 = focusRectanglesViewList.faceViewList;
        }
        for (int i = 0; i < 5; ++i) {
            TaggedRectangle taggedRectangle = null;
            if (arrview2 != null) {
                taggedRectangle = (TaggedRectangle)arrview2[i];
            }
            super.addTaggedRectangle(layoutInflater, "Dummy" + i, taggedRectangle);
        }
        this.mTrackedObjectRectangle = focusRectanglesViewList.trackedObjectView;
        if (this.mTrackedObjectRectangle == null) {
            this.mTrackedObjectRectangle = (TaggedRectangle)layoutInflater.inflate(R.layout.face_rectangle, null);
        }
        ImageView imageView = (ImageView)this.mTrackedObjectRectangle.findViewById(R.id.rect_image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        FocusRectanglesAnimation.AnimationConfig animationConfig = this.mAnimation.getObjectAnimationConfig();
        layoutParams.width = animationConfig.mToWidth;
        layoutParams.height = animationConfig.mToHeight;
        imageView.setLayoutParams(layoutParams);
        this.mTrackedObjectRectangle.setVisibility(4);
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(-1, -1);
        this.mRectangles.addView((View)this.mTrackedObjectRectangle, layoutParams2);
        this.mTrackedObjectRectangle.prepare(3);
        int n = this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_width);
        int n2 = this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_object_height);
        this.mTrackedObjectRectangle.setRectImageSize(n, n2);
        this.mSingleAfRect = focusRectanglesViewList.singleAfView;
        if (this.mSingleAfRect == null) {
            this.mSingleAfRect = (RelativeLayout)layoutInflater.inflate(R.layout.fast_capturing_auto_focus_rectangles, null);
        }
        this.mSingleAfRect.setVisibility(4);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -1);
        this.mRectangles.addView((View)this.mSingleAfRect, (ViewGroup.LayoutParams)layoutParams3);
        this.mTouchAfRect = focusRectanglesViewList.touchAfView;
        if (this.mTouchAfRect == null) {
            this.mTouchAfRect = (RelativeLayout)layoutInflater.inflate(R.layout.fast_capturing_auto_focus_rectangles, null);
        }
        ImageView imageView2 = (ImageView)this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
        ViewGroup.LayoutParams layoutParams4 = imageView2.getLayoutParams();
        FocusRectanglesAnimation.AnimationConfig animationConfig2 = this.mAnimation.getTouchAnimationConfig();
        layoutParams4.width = animationConfig2.mToWidth;
        layoutParams4.height = animationConfig2.mToHeight;
        imageView2.setLayoutParams(layoutParams4);
        this.mTouchAfRect.setVisibility(4);
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(-1, -1);
        this.mRectangles.addView((View)this.mTouchAfRect, (ViewGroup.LayoutParams)layoutParams5);
        super.updateRectanglesCoordinates();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onObjectTrackedInternal(CameraExtension.ObjectTrackingResult objectTrackingResult) {
        if (objectTrackingResult.mIsLost) {
            this.mHandler.postDelayed((Runnable)this.mRefreshTrackedObjectRectangleTask, 1000);
            return;
        }
        this.mHandler.removeCallbacks((Runnable)this.mRefreshTrackedObjectRectangleTask);
        Rect rect = LayoutDependencyResolver.getSurfaceRect(this.mActivity, (float)this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight);
        float f = (float)rect.width() / (float)this.mDevicePreviewWidth;
        float f2 = (float)rect.height() / (float)this.mDevicePreviewHeight;
        int n = (int)(f * (float)objectTrackingResult.mRectOfTrackedObject.centerX());
        int n2 = (int)(f2 * (float)objectTrackingResult.mRectOfTrackedObject.centerY());
        FocusRectanglesAnimation.AnimationConfig animationConfig = this.mAnimation.getObjectAnimationConfig();
        Rect rect2 = new Rect(n - animationConfig.mFromWidth / 2, n2 - animationConfig.mFromHeight / 2, n + animationConfig.mFromWidth / 2, n2 + animationConfig.mFromHeight / 2);
        this.mTrackedObjectRectangle.setRectCenter(rect2.centerX(), rect2.centerY());
        this.mTrackedObjectRectangle.setRectSize(rect2.width(), rect2.height());
        if (this.mIsRecording) {
            this.mTrackedObjectRectangle.changeRectangleResource(CommonResources.ObjectIndicator.SUCCESS);
        } else {
            this.mTrackedObjectRectangle.changeRectangleResource(CommonResources.ObjectIndicator.TRACKING);
        }
        this.mTrackedObjectRectangle.requestLayout();
        this.mTrackedObjectRectangle.setVisibility(0);
    }

    private void playOnTouchDownAnimationForTouchFocusRect() {
        if (this.mTouchAfRect.getVisibility() == 0) {
            ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setBackgroundResource(CommonResources.FaceIndicator.NORMAL);
            this.mIsFocusAnimationEnabled = true;
            imageView.setVisibility(4);
            this.mHandler.postDelayed((Runnable)new Runnable(){

                public void run() {
                    ImageView imageView = (ImageView)FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
                    imageView.setVisibility(0);
                    if (FocusRectangles.this.mIsFocusAnimationEnabled) {
                        FocusRectangles.this.mAnimation.playTouchDownAnimation((View)imageView);
                        return;
                    }
                    FocusRectangles.this.mTouchAfRect.setVisibility(4);
                }
            }, 100);
        }
    }

    private void playOnTouchUpAnimationForTouchFocusRect() {
        if (this.mTouchAfRect.getVisibility() == 0) {
            ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setBackgroundResource(CommonResources.FaceIndicator.NORMAL);
            this.mIsFocusAnimationEnabled = true;
            imageView.setVisibility(4);
            this.mHandler.postDelayed((Runnable)new Runnable(){

                public void run() {
                    ImageView imageView = (ImageView)FocusRectangles.this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
                    imageView.setVisibility(0);
                    if (FocusRectangles.this.mIsFocusAnimationEnabled) {
                        FocusRectangles.this.mAnimation.playTouchUpAnimation((View)imageView);
                        return;
                    }
                    FocusRectangles.this.mTouchAfRect.setVisibility(4);
                }
            }, 100);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void playTouchFocusStartAnimation(FocusSetType focusSetType) {
        if (this.mIsFaceTouchCaptureEnabled) return;
        switch (.$SwitchMap$com$sonyericsson$cameracommon$focusview$FocusRectangles$FocusSetType[focusSetType.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                super.playOnTouchDownAnimationForTouchFocusRect();
                return;
            }
            case 2: 
        }
        super.playOnTouchUpAnimationForTouchFocusRect();
    }

    private void removeObjectFocusRectAnimation() {
        this.mAnimation.cancelAfFocusAnimationObject((View)this.mTrackedObjectRectangle);
        this.mTrackedObjectRectangle.clearAnimation();
        this.mTrackedObjectRectangle.setAnimation(null);
    }

    private void removeSingleFocusRectAnimation() {
        ImageView imageView = (ImageView)this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
        this.mAnimation.cancelAfFocusAnimationSingle((View)imageView);
        imageView.clearAnimation();
        imageView.setAnimation(null);
    }

    private void removeTouchFocusRectAnimation() {
        ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
        this.mIsFocusAnimationEnabled = false;
        this.mAnimation.cancelAfFocusAnimationTouch((View)imageView);
        imageView.clearAnimation();
        imageView.setAnimation(null);
    }

    private void resetFaceRectangleColor() {
        for (String string : this.mFaceRectangles.keySet()) {
            ((TaggedRectangle)this.mFaceRectangles.get((Object)string)).changeRectangleResource(0);
            ((TaggedRectangle)this.mFaceRectangles.get((Object)string)).hideNameTag();
        }
    }

    private void resetObjectTrackingRectangleColor() {
        this.mTrackedObjectRectangle.changeRectangleResource(CommonResources.ObjectIndicator.TRACKING);
    }

    private void resetRectanglesColor() {
        this.resetFaceRectangleColor();
        this.resetObjectTrackingRectangleColor();
        this.resetTouchFocusRectangleColor();
        this.resetSingleFocusRectangleColor();
    }

    private void resetSingleFocusRectangleColor() {
        ((ImageView)this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect)).setBackgroundResource(CommonResources.SingleIndicator.NORMAL);
    }

    private void resetTouchFocusRectangleColor() {
        ImageView imageView = (ImageView)this.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
        imageView.setBackgroundResource(CommonResources.TouchIndicator.NORMAL);
        imageView.setVisibility(0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setFocusPositionInternal(Point point, FocusSetType focusSetType) {
        if (point == null) {
            this.mTouchAfRect.scrollTo(0, 0);
            return;
        }
        if (focusSetType == FocusSetType.FIRST) {
            super.hideFaceRectangles(true);
        }
        int n = point.x;
        int n2 = point.y;
        Rect rect = LayoutDependencyResolver.getSurfaceRect(this.mActivity, (float)this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight);
        FocusRectanglesAnimation.AnimationConfig animationConfig = this.mAnimation.getTouchAnimationConfig();
        int n3 = n < rect.left + animationConfig.mToWidth / 2 ? animationConfig.mToWidth / 2 : (rect.right - animationConfig.mToWidth / 2 < n ? rect.width() - animationConfig.mToWidth / 2 : n - rect.left);
        int n4 = n2 < rect.top + animationConfig.mToHeight / 2 ? animationConfig.mToHeight / 2 : (rect.bottom - animationConfig.mToHeight / 2 < n2 ? rect.height() - animationConfig.mToHeight / 2 : n2 - rect.top);
        this.mTouchAfRect.scrollTo(rect.width() / 2 - n3, rect.height() / 2 - n4);
        this.mTouchAfRect.setVisibility(0);
        super.playTouchFocusStartAnimation(focusSetType);
    }

    private void setRectSizeAndPosition(RelativeLayout relativeLayout, int n, int n2, int n3, int n4) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)relativeLayout.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.leftMargin = n;
            layoutParams.topMargin = n2;
            layoutParams.width = n3;
            layoutParams.height = n4;
            relativeLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
    }

    private void updateFaceNameData(List<FaceIdentification.FaceIdentificationResult> list) {
        super.faceResultToRectangles(list, this.mLastFaceDetectionResult, false);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void updateFaceRectangles(FaceInformationList faceInformationList, int n, boolean bl) {
        if (faceInformationList.getNamedFaceList().size() <= 0) {
            return;
        }
        for (String string : this.mFaceRectangles.keySet()) {
            ((TaggedRectangle)this.mFaceRectangles.get((Object)string)).clearUpdated();
        }
        for (int i = 0; i < 5; ++i) {
            if (i >= faceInformationList.getNamedFaceList().size()) continue;
            NamedFace namedFace = faceInformationList.getNamedFace(i);
            TaggedRectangle taggedRectangle = this.mFaceRectangles.containsKey((Object)namedFace.mUuid) ? (TaggedRectangle)this.mFaceRectangles.get((Object)namedFace.mUuid) : FaceDetectUtil.overwriteTaggedRectangle(this.mFaceRectangles, namedFace.mUuid, faceInformationList);
            if (taggedRectangle == null) continue;
            super.updateRectangle(taggedRectangle, namedFace, n, bl);
        }
        for (String string2 : this.mFaceRectangles.keySet()) {
            if (((TaggedRectangle)this.mFaceRectangles.get((Object)string2)).isUpdate()) continue;
            ((TaggedRectangle)this.mFaceRectangles.get((Object)string2)).hide();
        }
        TaggedRectangle taggedRectangle = FaceDetectUtil.sortRectangles(this.mFaceRectangles, faceInformationList);
        if (taggedRectangle == null) return;
        if (bl) return;
        if (this.mIsRecording) {
            taggedRectangle.changeRectangleResource(CommonResources.FaceIndicator.SUCCESS);
        } else {
            taggedRectangle.changeRectangleResource(CommonResources.FaceIndicator.PRIORITY);
        }
        this.mLatestSelectedFaceUuid = taggedRectangle.getUuid();
    }

    private void updateFaceRectanglesData(CameraExtension.FaceDetectionResult faceDetectionResult, boolean bl) {
        this.mLastFaceDetectionResult = faceDetectionResult;
        super.faceResultToRectangles(null, faceDetectionResult, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateRectangle(TaggedRectangle taggedRectangle, NamedFace namedFace, int n, boolean bl) {
        Rect rect = PositionConverter.getInstance().convertToView(namedFace.mFacePosition);
        if (this.mIsRectPositionMirrored) {
            taggedRectangle.setRectCenter(LayoutDependencyResolver.getSurfaceRect(this.mActivity, (float)this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight).width() - rect.centerX(), rect.centerY());
        } else {
            taggedRectangle.setRectCenter(rect.centerX(), rect.centerY());
        }
        taggedRectangle.setRectSize(rect.width(), rect.height());
        if (bl) {
            taggedRectangle.changeRectangleResource(0);
            taggedRectangle.hide();
        } else {
            taggedRectangle.changeRectangleResource(CommonResources.FaceIndicator.NORMAL);
        }
        boolean bl2 = taggedRectangle.getVisibility() == 0;
        if (!bl2) {
            taggedRectangle.startRectangleAnimation(n);
        }
        taggedRectangle.updateNameTag(namedFace.mName, namedFace.mUuid, (View)this.mRectangles, n);
        taggedRectangle.setUpdated();
        if (taggedRectangle.getVisibility() != 0) {
            taggedRectangle.requestLayout();
            taggedRectangle.setVisibility(0);
        }
    }

    private void updateRectanglesCoordinates() {
        Rect rect = LayoutDependencyResolver.getSurfaceRect(this.mActivity, (float)this.mDevicePreviewWidth / (float)this.mDevicePreviewHeight);
        this.setRectSizeAndPosition(this.mRectangles, rect.left, rect.top, rect.width(), rect.height());
        this.changeState(new DefaultFocusState());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void checkAndChangeFacePriority(CameraExtension.FaceDetectionResult faceDetectionResult) {
        if (faceDetectionResult == null) {
            return;
        }
        if (!FaceDetectUtil.isValidFaceDetectionResult(faceDetectionResult)) {
            CameraLogger.e(TAG, "FaceDetectionResult indexOfSelectedFace illegal vaue. result.indexOfSelectedFace = " + faceDetectionResult.indexOfSelectedFace);
            return;
        }
        if (String.valueOf((int)((com.sonyericsson.cameraextension.CameraExtension$ExtFace)faceDetectionResult.extFaceList.get((int)faceDetectionResult.indexOfSelectedFace)).face.id).equals((Object)this.mLatestSelectedFaceUuid)) return;
        super.changeFacePriority(this.mLatestSelectedFaceUuid);
    }

    public void clearAllFocus() {
        this.clearAllFocusExceptFace();
        this.clearFaceDetection();
    }

    public void clearAllFocusExceptFace() {
        this.changeState(new DefaultFocusState());
        this.clearSingleAutoFocus();
        this.clearTouchFocus();
        this.clearObjectTracking();
    }

    public void clearExceptTouchFocus() {
        this.mCurrentState.clearExceptTouchFocus();
    }

    public void clearFaceDetection() {
        this.hideFaceRectangles(false);
        this.resetFaceRectangleColor();
    }

    public void clearObjectTracking() {
        this.hideTrackedObjectRecgantle();
        this.removeObjectFocusRectAnimation();
        this.resetObjectTrackingRectangleColor();
    }

    public void clearSingleAutoFocus() {
        this.mSingleAfRect.setVisibility(4);
        this.removeSingleFocusRectAnimation();
        this.resetSingleFocusRectangleColor();
    }

    public void clearTouchFocus() {
        this.setFocusPositionInternal(null, null);
        this.mTouchAfRect.setVisibility(4);
        this.removeTouchFocusRectAnimation();
        this.resetTouchFocusRectangleColor();
    }

    public void disableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = false;
    }

    public void enableFaceTouchCapture() {
        this.mIsFaceTouchCaptureEnabled = true;
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public boolean isTouchFocus() {
        if (this.mCurrentState.getClass().equals((Object)TouchFocusState.class)) {
            return true;
        }
        return false;
    }

    public void onAutoFocusCanceled() {
        this.mCurrentState.onAutoFocusCanceled();
    }

    public void onAutoFocusDone(boolean bl) {
        this.mCurrentState.onAutoFocusDone(bl);
    }

    public void onAutoFocusStarted() {
        this.mCurrentState.onAutoFocusStarted();
    }

    public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
        this.mCurrentState.onFaceDetected(faceDetectionResult);
    }

    public void onFaceLost() {
        this.mUserTouchFaceUuid = null;
        this.mCurrentState.onFaceLost();
    }

    public void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> list) {
        this.mCurrentState.onFaceNameDetected(list);
    }

    public void onObjectLost() {
        this.mCurrentState.onObjectLost();
    }

    public void onObjectTracked(CameraExtension.ObjectTrackingResult objectTrackingResult) {
        this.mCurrentState.onTrackedObjectStateUpdated(objectTrackingResult);
    }

    public void onRecordingStart() {
        this.mIsRecording = true;
    }

    public void onRecordingStop() {
        this.mIsRecording = false;
    }

    public void onUiComponentOverlaid() {
        this.mCurrentState.onUiComponentOverlaid();
    }

    public void onUiComponentRemoved() {
        this.mCurrentState.onUiComponentRemoved();
    }

    public void release() {
        this.mActivity = null;
        this.mFocusEventListener = null;
    }

    public void setFocusPosition(Point point, FocusSetType focusSetType) {
        int[] arrn = new int[2];
        this.mRectangles.getLocationOnScreen(arrn);
        Point point2 = new Point(point.x - arrn[0], point.y - arrn[1]);
        this.mCurrentState.setFocusPosition(point2, focusSetType);
    }

    public void setMirrored(boolean bl) {
        this.mIsRectPositionMirrored = bl;
    }

    public void setOrientation(int n) {
        this.mCurrentOrientation = n;
    }

    public void setUserTouchFaceUuid(String string) {
        this.mUserTouchFaceUuid = string;
    }

    public void setVisibility(int n) {
        this.mRectangles.setVisibility(n);
    }

    public void startFaceDetection() {
        this.mCurrentState.startFaceDetection();
    }

    public void startObjectTracking() {
        this.mCurrentState.startObjectTracking();
    }

    public void updateDevicePreviewSize(int n, int n2) {
        this.mDevicePreviewWidth = n;
        this.mDevicePreviewHeight = n2;
        super.updateRectanglesCoordinates();
    }

    class DefaultFocusState
    implements State {
        DefaultFocusState() {
        }

        @Override
        public void clearExceptTouchFocus() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearObjectTracking();
            FocusRectangles.this.clearFaceDetection();
        }

        @Override
        public void onAutoFocusCanceled() {
        }

        @Override
        public void onAutoFocusDone(boolean bl) {
            ImageView imageView = (ImageView)FocusRectangles.this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setVisibility(0);
            if (bl) {
                imageView.setBackgroundResource(CommonResources.SingleIndicator.SUCCESS);
                FocusRectangles.this.mAnimation.playAfFocusInAnimationSingle((View)imageView);
                return;
            }
            FocusRectangles.this.mAnimation.playAfFadeOutAnimationSingle((View)imageView);
        }

        @Override
        public void onAutoFocusStarted() {
            ImageView imageView = (ImageView)FocusRectangles.this.mSingleAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setBackgroundResource(CommonResources.SingleIndicator.NORMAL);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = FocusRectangles.this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_single_height);
            layoutParams.width = FocusRectangles.this.mActivity.getResources().getDimensionPixelSize(R.dimen.focus_rect_single_width);
            imageView.setLayoutParams(layoutParams);
            FocusRectangles.this.mSingleAfRect.setVisibility(0);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                FocusRectangles.this.mCurrentState = new DefaultFocusState();
                return;
            }
            FocusRectangles.this.updateFaceRectanglesData(faceDetectionResult, false);
            FocusRectangles.this.checkAndChangeFacePriority(faceDetectionResult);
            FocusRectangles.this.changeState(new FaceDetectionState(FocusRectangles.this, null));
        }

        @Override
        public void onFaceLost() {
        }

        @Override
        public void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> list) {
        }

        @Override
        public void onObjectLost() {
        }

        @Override
        public void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult objectTrackingResult) {
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.hideFaceRectangles(true);
            FocusRectangles.this.onObjectTrackedInternal(objectTrackingResult);
            FocusRectangles.this.changeState(new ObjectTrackingState(FocusRectangles.this, null));
        }

        @Override
        public void onUiComponentOverlaid() {
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.hideFaceRectangles(false);
            FocusRectangles.this.hideTrackedObjectRecgantle();
            FocusRectangles.this.mTouchAfRect.setVisibility(4);
        }

        @Override
        public void onUiComponentRemoved() {
            FocusRectangles.this.mTouchAfRect.setVisibility(4);
            FocusRectangles.this.mSingleAfRect.setVisibility(4);
            FocusRectangles.this.resetRectanglesColor();
        }

        @Override
        public void setFocusPosition(Point point, FocusSetType focusSetType) {
            FocusRectangles.this.setFocusPositionInternal(point, focusSetType);
            FocusRectangles.this.changeState(new TouchFocusState(FocusRectangles.this, null));
        }

        @Override
        public void startFaceDetection() {
            FocusRectangles.this.clearSingleAutoFocus();
            FocusRectangles.this.clearObjectTracking();
            FocusRectangles.this.changeState(new FaceDetectionState(FocusRectangles.this, null));
        }

        @Override
        public void startObjectTracking() {
            this.clearExceptTouchFocus();
            FocusRectangles.this.changeState(new ObjectTrackingState(FocusRectangles.this, null));
        }
    }

    private class FaceDetectionState
    extends DefaultFocusState {
        final /* synthetic */ FocusRectangles this$0;

        private FaceDetectionState(FocusRectangles focusRectangles) {
            this.this$0 = focusRectangles;
        }

        /* synthetic */ FaceDetectionState(FocusRectangles focusRectangles,  var2_2) {
            super(focusRectangles);
        }

        private boolean isFaceRectAvailable() {
            for (String string : this.this$0.mFaceRectangles.keySet()) {
                if (((TaggedRectangle)this.this$0.mFaceRectangles.get((Object)string)).getVisibility() != 0) continue;
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void onAutoFocusDone(boolean bl) {
            if (!super.isFaceRectAvailable()) {
                super.onAutoFocusDone(bl);
                return;
            }
            Iterator iterator = this.this$0.mFaceRectangles.keySet().iterator();
            while (iterator.hasNext()) {
                String string = (String)iterator.next();
                if (this.this$0.mLatestSelectedFaceUuid == null) return;
                if (this.this$0.mLatestSelectedFaceUuid.equals((Object)string)) {
                    int n = bl ? CommonResources.FaceIndicator.SUCCESS : 0;
                    ((TaggedRectangle)this.this$0.mFaceRectangles.get((Object)string)).changeRectangleResource(n);
                    continue;
                }
                ((TaggedRectangle)this.this$0.mFaceRectangles.get((Object)string)).setVisibility(4);
            }
        }

        @Override
        public void onAutoFocusStarted() {
            if (!this.isFaceRectAvailable()) {
                super.onAutoFocusStarted();
            }
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            if (faceDetectionResult.extFaceList.size() == 0) {
                this.onFaceLost();
                return;
            }
            this.this$0.updateFaceRectanglesData(faceDetectionResult, false);
            this.this$0.checkAndChangeFacePriority(faceDetectionResult);
        }

        @Override
        public void onFaceLost() {
            this.this$0.clearFaceDetection();
            this.this$0.changeState(new DefaultFocusState());
        }

        @Override
        public void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> list) {
            this.this$0.updateFaceNameData(list);
            this.this$0.checkAndChangeFacePriority(this.this$0.mLastFaceDetectionResult);
        }

        @Override
        public void onObjectLost() {
        }

        @Override
        public void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult objectTrackingResult) {
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.hideFaceRectangles(true);
            this.this$0.onObjectTrackedInternal(objectTrackingResult);
            this.this$0.changeState(new ObjectTrackingState(this.this$0, null));
        }

        @Override
        public void setFocusPosition(Point point, FocusSetType focusSetType) {
            this.this$0.setFocusPositionInternal(point, focusSetType);
            this.this$0.changeState(new TouchFocusState(this.this$0, null));
        }

        @Override
        public void startFaceDetection() {
        }

        @Override
        public void startObjectTracking() {
            super.clearExceptTouchFocus();
            this.this$0.changeState(new ObjectTrackingState(this.this$0, null));
        }
    }

    public static final class FocusRectEvent
    extends Enum<FocusRectEvent> {
        private static final /* synthetic */ FocusRectEvent[] $VALUES;
        public static final /* enum */ FocusRectEvent CLEAR_ALL;
        public static final /* enum */ FocusRectEvent ON_AUTO_FOCUS_DONE;
        public static final /* enum */ FocusRectEvent ON_AUTO_FOCUS_STARTED;
        public static final /* enum */ FocusRectEvent ON_FACE_DETECTED;
        public static final /* enum */ FocusRectEvent ON_FACE_LOST;
        public static final /* enum */ FocusRectEvent ON_OBJECT_LOST;
        public static final /* enum */ FocusRectEvent ON_OBJECT_TRACKED;
        public static final /* enum */ FocusRectEvent ON_UI_COMPONENT_OVERLAID;
        public static final /* enum */ FocusRectEvent ON_UI_COMPONENT_REMOVED;
        public static final /* enum */ FocusRectEvent SET_FOCUS_POSITION;
        public static final /* enum */ FocusRectEvent START_FACE_DETECTION;
        public static final /* enum */ FocusRectEvent START_OBJECT_TRACKING;

        static {
            ON_AUTO_FOCUS_STARTED = new FocusRectEvent();
            ON_AUTO_FOCUS_DONE = new FocusRectEvent();
            SET_FOCUS_POSITION = new FocusRectEvent();
            START_FACE_DETECTION = new FocusRectEvent();
            ON_FACE_DETECTED = new FocusRectEvent();
            ON_FACE_LOST = new FocusRectEvent();
            START_OBJECT_TRACKING = new FocusRectEvent();
            ON_OBJECT_TRACKED = new FocusRectEvent();
            ON_OBJECT_LOST = new FocusRectEvent();
            CLEAR_ALL = new FocusRectEvent();
            ON_UI_COMPONENT_OVERLAID = new FocusRectEvent();
            ON_UI_COMPONENT_REMOVED = new FocusRectEvent();
            FocusRectEvent[] arrfocusRectEvent = new FocusRectEvent[]{ON_AUTO_FOCUS_STARTED, ON_AUTO_FOCUS_DONE, SET_FOCUS_POSITION, START_FACE_DETECTION, ON_FACE_DETECTED, ON_FACE_LOST, START_OBJECT_TRACKING, ON_OBJECT_TRACKED, ON_OBJECT_LOST, CLEAR_ALL, ON_UI_COMPONENT_OVERLAID, ON_UI_COMPONENT_REMOVED};
            $VALUES = arrfocusRectEvent;
        }

        private FocusRectEvent() {
            super(string, n);
        }

        public static FocusRectEvent valueOf(String string) {
            return (FocusRectEvent)Enum.valueOf((Class)FocusRectEvent.class, (String)string);
        }

        public static FocusRectEvent[] values() {
            return (FocusRectEvent[])$VALUES.clone();
        }
    }

    static final class FocusRectangleType
    extends Enum<FocusRectangleType> {
        private static final /* synthetic */ FocusRectangleType[] $VALUES;
        public static final /* enum */ FocusRectangleType FACE = new FocusRectangleType();
        public static final /* enum */ FocusRectangleType FAST_OBJECT_TRACKING;
        public static final /* enum */ FocusRectangleType FAST_SINGLE;
        public static final /* enum */ FocusRectangleType FAST_TOUCH;

        static {
            FAST_SINGLE = new FocusRectangleType();
            FAST_OBJECT_TRACKING = new FocusRectangleType();
            FAST_TOUCH = new FocusRectangleType();
            FocusRectangleType[] arrfocusRectangleType = new FocusRectangleType[]{FACE, FAST_SINGLE, FAST_OBJECT_TRACKING, FAST_TOUCH};
            $VALUES = arrfocusRectangleType;
        }

        private FocusRectangleType() {
            super(string, n);
        }

        public static FocusRectangleType valueOf(String string) {
            return (FocusRectangleType)Enum.valueOf((Class)FocusRectangleType.class, (String)string);
        }

        public static FocusRectangleType[] values() {
            return (FocusRectangleType[])$VALUES.clone();
        }
    }

    public static final class FocusSetType
    extends Enum<FocusSetType> {
        private static final /* synthetic */ FocusSetType[] $VALUES;
        public static final /* enum */ FocusSetType FIRST = new FocusSetType();
        public static final /* enum */ FocusSetType MOVE = new FocusSetType();
        public static final /* enum */ FocusSetType RELEASE = new FocusSetType();

        static {
            FocusSetType[] arrfocusSetType = new FocusSetType[]{FIRST, MOVE, RELEASE};
            $VALUES = arrfocusSetType;
        }

        private FocusSetType() {
            super(string, n);
        }

        public static FocusSetType valueOf(String string) {
            return (FocusSetType)Enum.valueOf((Class)FocusSetType.class, (String)string);
        }

        public static FocusSetType[] values() {
            return (FocusSetType[])$VALUES.clone();
        }
    }

    private class ObjectTrackingState
    extends DefaultFocusState {
        final /* synthetic */ FocusRectangles this$0;

        private ObjectTrackingState(FocusRectangles focusRectangles) {
            this.this$0 = focusRectangles;
        }

        /* synthetic */ ObjectTrackingState(FocusRectangles focusRectangles,  var2_2) {
            super(focusRectangles);
        }

        @Override
        public void onAutoFocusDone(boolean bl) {
            if (this.this$0.mTrackedObjectRectangle.getVisibility() != 0) {
                super.onAutoFocusDone(bl);
                return;
            }
            this.this$0.mTrackedObjectRectangle.setVisibility(0);
            if (bl) {
                int n = CommonResources.ObjectIndicator.SUCCESS;
                this.this$0.mTrackedObjectRectangle.changeRectangleResource(n);
                View view = this.this$0.mTrackedObjectRectangle.findViewById(R.id.rect_image);
                this.this$0.mAnimation.playAfFocusInAnimationObject(view);
                return;
            }
            this.this$0.mAnimation.playAfFadeOutAnimationObject((View)this.this$0.mTrackedObjectRectangle);
        }

        @Override
        public void onAutoFocusStarted() {
            if (this.this$0.mTrackedObjectRectangle.getVisibility() != 0) {
                super.onAutoFocusStarted();
            }
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            this.this$0.updateFaceRectanglesData(faceDetectionResult, true);
            this.this$0.checkAndChangeFacePriority(faceDetectionResult);
        }

        @Override
        public void onFaceLost() {
        }

        @Override
        public void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> list) {
            this.this$0.updateFaceNameData(list);
            this.this$0.checkAndChangeFacePriority(this.this$0.mLastFaceDetectionResult);
        }

        @Override
        public void onObjectLost() {
            this.this$0.changeState(new DefaultFocusState());
        }

        @Override
        public void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult objectTrackingResult) {
            this.this$0.onObjectTrackedInternal(objectTrackingResult);
        }

        @Override
        public void setFocusPosition(Point point, FocusSetType focusSetType) {
            this.this$0.setFocusPositionInternal(point, focusSetType);
            this.this$0.changeState(new TouchFocusState(this.this$0, null));
        }

        @Override
        public void startFaceDetection() {
            super.clearExceptTouchFocus();
            this.this$0.changeState(new FaceDetectionState(this.this$0, null));
        }

        @Override
        public void startObjectTracking() {
        }
    }

    class OnFaceRectTouchListener
    implements Rectangle.RectangleOnTouchListener {
        OnFaceRectTouchListener() {
        }

        private boolean isTouchAreaOnTouchCapture(MotionEvent motionEvent) {
            if (FocusRectangles.this.mIsFaceTouchCaptureEnabled && FocusRectangles.this.mCaptureArea != null) {
                return CommonUtility.isEventContainedInView(FocusRectangles.this.mCaptureArea, motionEvent);
            }
            return false;
        }

        @Override
        public void onRectTouchCancel(View view, MotionEvent motionEvent) {
            FocusRectangles.this.mFocusEventListener.onCanceled();
        }

        @Override
        public void onRectTouchDown(View view, MotionEvent motionEvent) {
            if (view.getVisibility() == 0) {
                for (String string : FocusRectangles.this.mFaceRectangles.keySet()) {
                    if (!((TaggedRectangle)FocusRectangles.this.mFaceRectangles.get((Object)string)).equals((Object)view)) continue;
                    FocusRectangles.this.mUserTouchFaceUuid = ((TaggedRectangle)FocusRectangles.this.mFaceRectangles.get((Object)string)).getUuid();
                    ((TaggedRectangle)FocusRectangles.this.mFaceRectangles.get((Object)string)).startRectanglePressAnimation();
                    FocusRectangles.this.faceResultToRectangles(null, FocusRectangles.this.mLastFaceDetectionResult, false);
                    FocusRectangles.this.changeFacePriority(string);
                    if (!super.isTouchAreaOnTouchCapture(motionEvent)) break;
                    FocusRectangles.this.mFocusEventListener.onTouched();
                }
            }
        }

        @Override
        public void onRectTouchLongPress(MotionEvent motionEvent) {
            FocusRectangles.this.mFocusEventListener.onLongPressed();
        }

        @Override
        public void onRectTouchUp(View view, MotionEvent motionEvent) {
            if (view.getVisibility() == 0) {
                for (String string : FocusRectangles.this.mFaceRectangles.keySet()) {
                    if (!((TaggedRectangle)FocusRectangles.this.mFaceRectangles.get((Object)string)).equals((Object)view)) continue;
                    if (!super.isTouchAreaOnTouchCapture(motionEvent)) break;
                    FocusRectangles.this.mFocusEventListener.onReleased();
                }
            }
        }
    }

    /*
     * Failed to analyse overrides
     */
    class RefreshTrackedObjectRectangleTask
    implements Runnable {
        RefreshTrackedObjectRectangleTask() {
        }

        public void run() {
            if (FocusRectangles.this.mFocusEventListener == null || FocusRectangles.this.mTrackedObjectRectangle == null) {
                return;
            }
            FocusRectangles.this.mTrackedObjectRectangle.setVisibility(4);
            FocusRectangles.this.onObjectLost();
        }
    }

    private static interface State {
        public void clearExceptTouchFocus();

        public void onAutoFocusCanceled();

        public void onAutoFocusDone(boolean var1);

        public void onAutoFocusStarted();

        public void onFaceDetected(CameraExtension.FaceDetectionResult var1);

        public void onFaceLost();

        public void onFaceNameDetected(List<FaceIdentification.FaceIdentificationResult> var1);

        public void onObjectLost();

        public void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult var1);

        public void onUiComponentOverlaid();

        public void onUiComponentRemoved();

        public void setFocusPosition(Point var1, FocusSetType var2);

        public void startFaceDetection();

        public void startObjectTracking();
    }

    private class TouchFocusState
    extends DefaultFocusState {
        final /* synthetic */ FocusRectangles this$0;

        private TouchFocusState(FocusRectangles focusRectangles) {
            this.this$0 = focusRectangles;
        }

        /* synthetic */ TouchFocusState(FocusRectangles focusRectangles,  var2_2) {
            super(focusRectangles);
        }

        @Override
        public void onAutoFocusCanceled() {
            ((ImageView)this.this$0.mTouchAfRect.findViewById(R.id.center_auto_focus_rect)).setVisibility(0);
        }

        @Override
        public void onAutoFocusDone(boolean bl) {
            ImageView imageView = (ImageView)this.this$0.mTouchAfRect.findViewById(R.id.center_auto_focus_rect);
            imageView.setVisibility(0);
            if (bl) {
                imageView.setBackgroundResource(CommonResources.TouchIndicator.SUCCESS);
                this.this$0.mAnimation.playAfFocusInAnimationTouch((View)imageView);
                return;
            }
            this.this$0.mAnimation.playAfFadeOutAnimationTouch((View)imageView);
        }

        @Override
        public void onAutoFocusStarted() {
            ((ImageView)this.this$0.mTouchAfRect.findViewById(R.id.center_auto_focus_rect)).setBackgroundResource(CommonResources.TouchIndicator.NORMAL);
            this.this$0.mTouchAfRect.setVisibility(0);
        }

        @Override
        public void onFaceDetected(CameraExtension.FaceDetectionResult faceDetectionResult) {
            this.this$0.updateFaceRectanglesData(faceDetectionResult, true);
        }

        @Override
        public void onFaceLost() {
        }

        @Override
        public void onObjectLost() {
        }

        @Override
        public void onTrackedObjectStateUpdated(CameraExtension.ObjectTrackingResult objectTrackingResult) {
        }

        @Override
        public void onUiComponentRemoved() {
            this.this$0.mTouchAfRect.setVisibility(0);
            this.this$0.mSingleAfRect.setVisibility(4);
            this.this$0.resetRectanglesColor();
        }

        @Override
        public void setFocusPosition(Point point, FocusSetType focusSetType) {
            this.this$0.setFocusPositionInternal(point, focusSetType);
        }

        @Override
        public void startFaceDetection() {
            this.this$0.clearSingleAutoFocus();
            this.this$0.clearObjectTracking();
            this.this$0.changeState(new FaceDetectionState(this.this$0, null));
        }

        @Override
        public void startObjectTracking() {
            super.clearExceptTouchFocus();
            this.this$0.changeState(new ObjectTrackingState(this.this$0, null));
        }
    }

}

