/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.fastcapturing.view;

import android.view.LayoutInflater;
import android.view.animation.Animation;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.fastcapturing.CameraDeviceHandler;
import com.sonyericsson.android.camera.fastcapturing.StateMachine;
import com.sonyericsson.cameracommon.commonsetting.values.TouchCapture;

public interface BaseFastViewFinder {
    public void checkSurfaceIsPreparedOrNot();

    public void closeCapturingModeSelector();

    public void ensureVideoAutoReviewSettingHasLoaded();

    public void fadeoutCounter();

    public AutoReview getAutoReviewSetting();

    public int getOrientation();

    public SelfTimer getPhotoSelfTimerSetting();

    public int getRequestId(boolean var1);

    public TouchCapture getTouchCapture();

    public VideoAutoReview getVideoAutoReviewSetting();

    public void hideHudIcons();

    public boolean isCapturingModeSelectorOpened();

    public boolean isHeadUpDesplayReady();

    public boolean isSetupHeadupDisplayInvoked();

    public boolean isTouchFocus();

    public void onCaptureDone();

    public void onShutterDone(boolean var1);

    public void removeEarlyThumbnailView();

    public void requestInflate(LayoutInflater var1);

    public /* varargs */ void sendViewUpdateEvent(ViewUpdateEvent var1, Object ... var2);

    public void setCameraDevice(CameraDeviceHandler var1);

    public void setContentView();

    public void setRecordingOrientation(int var1);

    public void setStateMachine(StateMachine var1);

    public void startCaptureFeedbackAnimation();

    public void startEarlyThumbnailInsertAnimation(int var1, Animation.AnimationListener var2);

    public void startHideThumbnail();

    public static final class HeadUpDisplaySetupState
    extends Enum<HeadUpDisplaySetupState> {
        private static final /* synthetic */ HeadUpDisplaySetupState[] $VALUES;
        public static final /* enum */ HeadUpDisplaySetupState PHOTO_CAPTURE;
        public static final /* enum */ HeadUpDisplaySetupState PHOTO_STANDBY;
        public static final /* enum */ HeadUpDisplaySetupState VIDEO_PAUSING;
        public static final /* enum */ HeadUpDisplaySetupState VIDEO_RECORDING;
        public static final /* enum */ HeadUpDisplaySetupState VIDEO_STANDBY;

        static {
            PHOTO_STANDBY = new HeadUpDisplaySetupState();
            PHOTO_CAPTURE = new HeadUpDisplaySetupState();
            VIDEO_STANDBY = new HeadUpDisplaySetupState();
            VIDEO_RECORDING = new HeadUpDisplaySetupState();
            VIDEO_PAUSING = new HeadUpDisplaySetupState();
            HeadUpDisplaySetupState[] arrheadUpDisplaySetupState = new HeadUpDisplaySetupState[]{PHOTO_STANDBY, PHOTO_CAPTURE, VIDEO_STANDBY, VIDEO_RECORDING, VIDEO_PAUSING};
            $VALUES = arrheadUpDisplaySetupState;
        }

        private HeadUpDisplaySetupState() {
            super(string, n);
        }

        public static HeadUpDisplaySetupState valueOf(String string) {
            return (HeadUpDisplaySetupState)Enum.valueOf((Class)HeadUpDisplaySetupState.class, (String)string);
        }

        public static HeadUpDisplaySetupState[] values() {
            return (HeadUpDisplaySetupState[])$VALUES.clone();
        }
    }

    public static final class UiComponentKind
    extends Enum<UiComponentKind> {
        private static final /* synthetic */ UiComponentKind[] $VALUES;
        public static final /* enum */ UiComponentKind OVERLAY_ALERT_DIALOG;
        public static final /* enum */ UiComponentKind REVIEW_WINDOW;
        public static final /* enum */ UiComponentKind SETTING_DIALOG;
        public static final /* enum */ UiComponentKind ZOOM_BAR;

        static {
            ZOOM_BAR = new UiComponentKind();
            SETTING_DIALOG = new UiComponentKind();
            OVERLAY_ALERT_DIALOG = new UiComponentKind();
            REVIEW_WINDOW = new UiComponentKind();
            UiComponentKind[] arruiComponentKind = new UiComponentKind[]{ZOOM_BAR, SETTING_DIALOG, OVERLAY_ALERT_DIALOG, REVIEW_WINDOW};
            $VALUES = arruiComponentKind;
        }

        private UiComponentKind() {
            super(string, n);
        }

        public static UiComponentKind valueOf(String string) {
            return (UiComponentKind)Enum.valueOf((Class)UiComponentKind.class, (String)string);
        }

        public static UiComponentKind[] values() {
            return (UiComponentKind[])$VALUES.clone();
        }
    }

    public static final class ViewUpdateEvent
    extends Enum<ViewUpdateEvent> {
        private static final /* synthetic */ ViewUpdateEvent[] $VALUES;
        public static final /* enum */ ViewUpdateEvent EVENT_CLOSE_DIALOGS;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_ADD_VIDEO_CHAPTER;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_AF_CANCELED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_CAMERA_MODE_CHANGED_TO;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_DETECTED_SCENE_CHANGED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FACE_DETECTED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FACE_DETECTION_STARTED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FACE_NAME_DETECTED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FOCUS_POSITION_RELEASED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_FOCUS_POSITION_SELECTED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_LAZY_INITIALIZATION_TASK_RUN;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_NOTIFY_THERMAL_NORMAL;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_NOTIFY_THERMAL_WARNING;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_OBJECT_TRACKING_STARTED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_ORIENTATION_CHANGED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_RECORDING_PROGRESS;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_SELFTIMER_FINISHED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_TRACKED_OBJECT_STATE_UPDATED;
        public static final /* enum */ ViewUpdateEvent EVENT_ON_ZOOM_CHANGED;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_CHECK_SURFACE_PREPARED;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_HIDE_REMAIN_INDICATOR;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_HIDE_SURFACE;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_PREPARE_RECORDING_INDICATOR;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_REMOVE_EARLY_THUMBNAIL;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_RESIZE_EVF_SCOPE;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SET_EARLY_THUMBNAIL;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SHOW_AUTO_REVIEW;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SHOW_ICONS_ON_REVIEW_WINDOW;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SHOW_INSTANT_VIEWER;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SHOW_REMAIN_INDICATOR;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_SHOW_SURFACE;
        public static final /* enum */ ViewUpdateEvent EVENT_REQUEST_UPDATE_COUNT_UP_VIEW;
        public static final /* enum */ ViewUpdateEvent EVENT_UPDATE_DIALOGS;
        public static final /* enum */ ViewUpdateEvent EVEVT_REQUEST_ADD_COUNT_UP_VIEW;
        public static final /* enum */ ViewUpdateEvent EVEVT_REQUEST_REMOVE_COUNT_UP_VIEW;

        static {
            EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY = new ViewUpdateEvent();
            EVENT_REQUEST_RESIZE_EVF_SCOPE = new ViewUpdateEvent();
            EVENT_REQUEST_CHECK_SURFACE_PREPARED = new ViewUpdateEvent();
            EVENT_REQUEST_HIDE_SURFACE = new ViewUpdateEvent();
            EVENT_REQUEST_SHOW_SURFACE = new ViewUpdateEvent();
            EVENT_REQUEST_HIDE_REMAIN_INDICATOR = new ViewUpdateEvent();
            EVENT_REQUEST_SHOW_REMAIN_INDICATOR = new ViewUpdateEvent();
            EVENT_REQUEST_PREPARE_RECORDING_INDICATOR = new ViewUpdateEvent();
            EVENT_ON_CAMERA_MODE_CHANGED_TO = new ViewUpdateEvent();
            EVENT_ON_DETECTED_SCENE_CHANGED = new ViewUpdateEvent();
            EVENT_ON_FACE_DETECTION_STARTED = new ViewUpdateEvent();
            EVENT_ON_FACE_DETECTED = new ViewUpdateEvent();
            EVENT_ON_FACE_NAME_DETECTED = new ViewUpdateEvent();
            EVENT_ON_OBJECT_TRACKING_STARTED = new ViewUpdateEvent();
            EVENT_ON_TRACKED_OBJECT_STATE_UPDATED = new ViewUpdateEvent();
            EVENT_ON_ZOOM_CHANGED = new ViewUpdateEvent();
            EVENT_ON_FOCUS_POSITION_SELECTED = new ViewUpdateEvent();
            EVENT_ON_FOCUS_POSITION_RELEASED = new ViewUpdateEvent();
            EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE = new ViewUpdateEvent();
            EVENT_ON_RECORDING_PROGRESS = new ViewUpdateEvent();
            EVENT_ON_ORIENTATION_CHANGED = new ViewUpdateEvent();
            EVENT_ON_AF_CANCELED = new ViewUpdateEvent();
            EVENT_ON_SELFTIMER_FINISHED = new ViewUpdateEvent();
            EVENT_UPDATE_DIALOGS = new ViewUpdateEvent();
            EVENT_CLOSE_DIALOGS = new ViewUpdateEvent();
            EVENT_REQUEST_SHOW_AUTO_REVIEW = new ViewUpdateEvent();
            EVENT_REQUEST_SHOW_INSTANT_VIEWER = new ViewUpdateEvent();
            EVENT_REQUEST_SHOW_ICONS_ON_REVIEW_WINDOW = new ViewUpdateEvent();
            EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION = new ViewUpdateEvent();
            EVENT_REQUEST_SET_EARLY_THUMBNAIL = new ViewUpdateEvent();
            EVENT_REQUEST_REMOVE_EARLY_THUMBNAIL = new ViewUpdateEvent();
            EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION = new ViewUpdateEvent();
            EVEVT_REQUEST_ADD_COUNT_UP_VIEW = new ViewUpdateEvent();
            EVENT_REQUEST_UPDATE_COUNT_UP_VIEW = new ViewUpdateEvent();
            EVEVT_REQUEST_REMOVE_COUNT_UP_VIEW = new ViewUpdateEvent();
            EVENT_ON_LAZY_INITIALIZATION_TASK_RUN = new ViewUpdateEvent();
            EVENT_ON_ADD_VIDEO_CHAPTER = new ViewUpdateEvent();
            EVENT_ON_NOTIFY_THERMAL_NORMAL = new ViewUpdateEvent();
            EVENT_ON_NOTIFY_THERMAL_WARNING = new ViewUpdateEvent();
            ViewUpdateEvent[] arrviewUpdateEvent = new ViewUpdateEvent[]{EVENT_REQUEST_SETUP_HEAD_UP_DISPLAY, EVENT_REQUEST_RESIZE_EVF_SCOPE, EVENT_REQUEST_CHECK_SURFACE_PREPARED, EVENT_REQUEST_HIDE_SURFACE, EVENT_REQUEST_SHOW_SURFACE, EVENT_REQUEST_HIDE_REMAIN_INDICATOR, EVENT_REQUEST_SHOW_REMAIN_INDICATOR, EVENT_REQUEST_PREPARE_RECORDING_INDICATOR, EVENT_ON_CAMERA_MODE_CHANGED_TO, EVENT_ON_DETECTED_SCENE_CHANGED, EVENT_ON_FACE_DETECTION_STARTED, EVENT_ON_FACE_DETECTED, EVENT_ON_FACE_NAME_DETECTED, EVENT_ON_OBJECT_TRACKING_STARTED, EVENT_ON_TRACKED_OBJECT_STATE_UPDATED, EVENT_ON_ZOOM_CHANGED, EVENT_ON_FOCUS_POSITION_SELECTED, EVENT_ON_FOCUS_POSITION_RELEASED, EVENT_ON_FOCUS_POSITION_RELEASED_EXCEPT_FACE, EVENT_ON_RECORDING_PROGRESS, EVENT_ON_ORIENTATION_CHANGED, EVENT_ON_AF_CANCELED, EVENT_ON_SELFTIMER_FINISHED, EVENT_UPDATE_DIALOGS, EVENT_CLOSE_DIALOGS, EVENT_REQUEST_SHOW_AUTO_REVIEW, EVENT_REQUEST_SHOW_INSTANT_VIEWER, EVENT_REQUEST_SHOW_ICONS_ON_REVIEW_WINDOW, EVENT_REQUEST_CAPTURE_FEEDBACK_ANIMATION, EVENT_REQUEST_SET_EARLY_THUMBNAIL, EVENT_REQUEST_REMOVE_EARLY_THUMBNAIL, EVENT_REQUEST_EARLY_THUMBNAIL_INSERT_ANIMATION, EVEVT_REQUEST_ADD_COUNT_UP_VIEW, EVENT_REQUEST_UPDATE_COUNT_UP_VIEW, EVEVT_REQUEST_REMOVE_COUNT_UP_VIEW, EVENT_ON_LAZY_INITIALIZATION_TASK_RUN, EVENT_ON_ADD_VIDEO_CHAPTER, EVENT_ON_NOTIFY_THERMAL_NORMAL, EVENT_ON_NOTIFY_THERMAL_WARNING};
            $VALUES = arrviewUpdateEvent;
        }

        private ViewUpdateEvent() {
            super(string, n);
        }

        public static ViewUpdateEvent valueOf(String string) {
            return (ViewUpdateEvent)Enum.valueOf((Class)ViewUpdateEvent.class, (String)string);
        }

        public static ViewUpdateEvent[] values() {
            return (ViewUpdateEvent[])$VALUES.clone();
        }
    }

}

