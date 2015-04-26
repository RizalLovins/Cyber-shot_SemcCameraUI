/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

public final class ControllerEvent
extends Enum<ControllerEvent> {
    private static final /* synthetic */ ControllerEvent[] $VALUES;
    public static final /* enum */ ControllerEvent EV_ABORT = new ControllerEvent();
    public static final /* enum */ ControllerEvent EV_AF_CANCEL = new ControllerEvent();
    public static final /* enum */ ControllerEvent EV_AF_DONE = new ControllerEvent();
    public static final /* enum */ ControllerEvent EV_AF_START = new ControllerEvent();
    public static final /* enum */ ControllerEvent EV_AUDIO_RESOURCE_ERROR;
    public static final /* enum */ ControllerEvent EV_BURST_COMPRESSED_DATA;
    public static final /* enum */ ControllerEvent EV_BURST_START;
    public static final /* enum */ ControllerEvent EV_BURST_STOP;
    public static final /* enum */ ControllerEvent EV_CAMERA_SETUP_FINISHED;
    public static final /* enum */ ControllerEvent EV_CAPTURE;
    public static final /* enum */ ControllerEvent EV_CHANGE_CAPTURING_MODE;
    public static final /* enum */ ControllerEvent EV_CLICK_CONTENT_PROGRESS;
    public static final /* enum */ ControllerEvent EV_COMPRESSED_DATA;
    public static final /* enum */ ControllerEvent EV_CONTROLLER_PAUSE;
    public static final /* enum */ ControllerEvent EV_CONTROLLER_RESUME;
    public static final /* enum */ ControllerEvent EV_DEVICE_ERROR;
    public static final /* enum */ ControllerEvent EV_FACE_DETECT;
    public static final /* enum */ ControllerEvent EV_FACE_DETECT_CHANGE;
    public static final /* enum */ ControllerEvent EV_FACE_IDENTIFY;
    public static final /* enum */ ControllerEvent EV_FOCUS_POSITION_CANCEL;
    public static final /* enum */ ControllerEvent EV_FOCUS_POSITION_CHANGE;
    public static final /* enum */ ControllerEvent EV_FOCUS_POSITION_CONTINUE;
    public static final /* enum */ ControllerEvent EV_FOCUS_POSITION_FINISH;
    public static final /* enum */ ControllerEvent EV_FOCUS_POSITION_START;
    public static final /* enum */ ControllerEvent EV_KEY_BACK;
    public static final /* enum */ ControllerEvent EV_LAUNCH;
    public static final /* enum */ ControllerEvent EV_OBJECT_TRACKING;
    public static final /* enum */ ControllerEvent EV_OBJECT_TRACKING_INVISIBLE;
    public static final /* enum */ ControllerEvent EV_OBJECT_TRACKING_LOST;
    public static final /* enum */ ControllerEvent EV_OBJECT_TRACKING_START;
    public static final /* enum */ ControllerEvent EV_OPEN_SETTINGS_DIALOG;
    public static final /* enum */ ControllerEvent EV_REACH_HIGH_TEMPERATURE;
    public static final /* enum */ ControllerEvent EV_SCENE_CHANGED;
    public static final /* enum */ ControllerEvent EV_SELFTIMER_CANCEL;
    public static final /* enum */ ControllerEvent EV_SELFTIMER_CAPTURE;
    public static final /* enum */ ControllerEvent EV_SELFTIMER_COUNTDOWN;
    public static final /* enum */ ControllerEvent EV_SELFTIMER_FINISH;
    public static final /* enum */ ControllerEvent EV_SELFTIMER_START;
    public static final /* enum */ ControllerEvent EV_SHUTTER_DONE;
    public static final /* enum */ ControllerEvent EV_SMILE_CAPTURE;
    public static final /* enum */ ControllerEvent EV_STORAGE_ERROR;
    public static final /* enum */ ControllerEvent EV_STORAGE_MOUNTED;
    public static final /* enum */ ControllerEvent EV_STORAGE_SHOULD_CHANGE;
    public static final /* enum */ ControllerEvent EV_STORE_DONE;
    public static final /* enum */ ControllerEvent EV_SURFACE_CHANGED;
    public static final /* enum */ ControllerEvent EV_SURFACE_CREATED;
    public static final /* enum */ ControllerEvent EV_SURFACE_DESTROYED;
    public static final /* enum */ ControllerEvent EV_VIDEO_FINISHED;
    public static final /* enum */ ControllerEvent EV_VIDEO_INFO;
    public static final /* enum */ ControllerEvent EV_VIDEO_PAUSED;
    public static final /* enum */ ControllerEvent EV_VIDEO_PAUSE_RESUME;
    public static final /* enum */ ControllerEvent EV_VIDEO_PROGRESS;
    public static final /* enum */ ControllerEvent EV_VIDEO_START_WAIT_DONE;
    public static final /* enum */ ControllerEvent EV_ZOOM_FINISH;
    public static final /* enum */ ControllerEvent EV_ZOOM_PREPARE;
    public static final /* enum */ ControllerEvent EV_ZOOM_PROGRESS;
    public static final /* enum */ ControllerEvent EV_ZOOM_START;
    public static final /* enum */ ControllerEvent EV_ZOOM_STOP;

    static {
        EV_BURST_START = new ControllerEvent();
        EV_BURST_STOP = new ControllerEvent();
        EV_CAMERA_SETUP_FINISHED = new ControllerEvent();
        EV_CAPTURE = new ControllerEvent();
        EV_CHANGE_CAPTURING_MODE = new ControllerEvent();
        EV_COMPRESSED_DATA = new ControllerEvent();
        EV_BURST_COMPRESSED_DATA = new ControllerEvent();
        EV_CONTROLLER_PAUSE = new ControllerEvent();
        EV_CONTROLLER_RESUME = new ControllerEvent();
        EV_DEVICE_ERROR = new ControllerEvent();
        EV_AUDIO_RESOURCE_ERROR = new ControllerEvent();
        EV_STORAGE_ERROR = new ControllerEvent();
        EV_STORAGE_MOUNTED = new ControllerEvent();
        EV_STORAGE_SHOULD_CHANGE = new ControllerEvent();
        EV_FACE_DETECT = new ControllerEvent();
        EV_FACE_DETECT_CHANGE = new ControllerEvent();
        EV_FACE_IDENTIFY = new ControllerEvent();
        EV_FOCUS_POSITION_CANCEL = new ControllerEvent();
        EV_FOCUS_POSITION_CHANGE = new ControllerEvent();
        EV_FOCUS_POSITION_CONTINUE = new ControllerEvent();
        EV_FOCUS_POSITION_FINISH = new ControllerEvent();
        EV_FOCUS_POSITION_START = new ControllerEvent();
        EV_KEY_BACK = new ControllerEvent();
        EV_LAUNCH = new ControllerEvent();
        EV_OBJECT_TRACKING = new ControllerEvent();
        EV_OBJECT_TRACKING_INVISIBLE = new ControllerEvent();
        EV_OBJECT_TRACKING_LOST = new ControllerEvent();
        EV_OBJECT_TRACKING_START = new ControllerEvent();
        EV_OPEN_SETTINGS_DIALOG = new ControllerEvent();
        EV_REACH_HIGH_TEMPERATURE = new ControllerEvent();
        EV_SCENE_CHANGED = new ControllerEvent();
        EV_SELFTIMER_CANCEL = new ControllerEvent();
        EV_SELFTIMER_CAPTURE = new ControllerEvent();
        EV_SELFTIMER_COUNTDOWN = new ControllerEvent();
        EV_SELFTIMER_FINISH = new ControllerEvent();
        EV_SELFTIMER_START = new ControllerEvent();
        EV_SHUTTER_DONE = new ControllerEvent();
        EV_SMILE_CAPTURE = new ControllerEvent();
        EV_STORE_DONE = new ControllerEvent();
        EV_SURFACE_CHANGED = new ControllerEvent();
        EV_SURFACE_CREATED = new ControllerEvent();
        EV_SURFACE_DESTROYED = new ControllerEvent();
        EV_VIDEO_INFO = new ControllerEvent();
        EV_VIDEO_PROGRESS = new ControllerEvent();
        EV_VIDEO_START_WAIT_DONE = new ControllerEvent();
        EV_VIDEO_PAUSE_RESUME = new ControllerEvent();
        EV_VIDEO_FINISHED = new ControllerEvent();
        EV_VIDEO_PAUSED = new ControllerEvent();
        EV_ZOOM_FINISH = new ControllerEvent();
        EV_ZOOM_PREPARE = new ControllerEvent();
        EV_ZOOM_PROGRESS = new ControllerEvent();
        EV_ZOOM_START = new ControllerEvent();
        EV_ZOOM_STOP = new ControllerEvent();
        EV_CLICK_CONTENT_PROGRESS = new ControllerEvent();
        ControllerEvent[] arrcontrollerEvent = new ControllerEvent[]{EV_ABORT, EV_AF_CANCEL, EV_AF_DONE, EV_AF_START, EV_BURST_START, EV_BURST_STOP, EV_CAMERA_SETUP_FINISHED, EV_CAPTURE, EV_CHANGE_CAPTURING_MODE, EV_COMPRESSED_DATA, EV_BURST_COMPRESSED_DATA, EV_CONTROLLER_PAUSE, EV_CONTROLLER_RESUME, EV_DEVICE_ERROR, EV_AUDIO_RESOURCE_ERROR, EV_STORAGE_ERROR, EV_STORAGE_MOUNTED, EV_STORAGE_SHOULD_CHANGE, EV_FACE_DETECT, EV_FACE_DETECT_CHANGE, EV_FACE_IDENTIFY, EV_FOCUS_POSITION_CANCEL, EV_FOCUS_POSITION_CHANGE, EV_FOCUS_POSITION_CONTINUE, EV_FOCUS_POSITION_FINISH, EV_FOCUS_POSITION_START, EV_KEY_BACK, EV_LAUNCH, EV_OBJECT_TRACKING, EV_OBJECT_TRACKING_INVISIBLE, EV_OBJECT_TRACKING_LOST, EV_OBJECT_TRACKING_START, EV_OPEN_SETTINGS_DIALOG, EV_REACH_HIGH_TEMPERATURE, EV_SCENE_CHANGED, EV_SELFTIMER_CANCEL, EV_SELFTIMER_CAPTURE, EV_SELFTIMER_COUNTDOWN, EV_SELFTIMER_FINISH, EV_SELFTIMER_START, EV_SHUTTER_DONE, EV_SMILE_CAPTURE, EV_STORE_DONE, EV_SURFACE_CHANGED, EV_SURFACE_CREATED, EV_SURFACE_DESTROYED, EV_VIDEO_INFO, EV_VIDEO_PROGRESS, EV_VIDEO_START_WAIT_DONE, EV_VIDEO_PAUSE_RESUME, EV_VIDEO_FINISHED, EV_VIDEO_PAUSED, EV_ZOOM_FINISH, EV_ZOOM_PREPARE, EV_ZOOM_PROGRESS, EV_ZOOM_START, EV_ZOOM_STOP, EV_CLICK_CONTENT_PROGRESS};
        $VALUES = arrcontrollerEvent;
    }

    private ControllerEvent() {
        super(string, n);
    }

    public static ControllerEvent valueOf(String string) {
        return (ControllerEvent)Enum.valueOf((Class)ControllerEvent.class, (String)string);
    }

    public static ControllerEvent[] values() {
        return (ControllerEvent[])$VALUES.clone();
    }
}

