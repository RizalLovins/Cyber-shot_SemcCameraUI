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

public final class ControllerEventSource
extends Enum<ControllerEventSource> {
    private static final /* synthetic */ ControllerEventSource[] $VALUES;
    public static final /* enum */ ControllerEventSource AUTO_STATE_TRANSITION;
    public static final /* enum */ ControllerEventSource DEVICE;
    public static final /* enum */ ControllerEventSource KEY;
    public static final /* enum */ ControllerEventSource OTHER;
    public static final /* enum */ ControllerEventSource PAUSE_RESUME_BUTTON;
    public static final /* enum */ ControllerEventSource PHOTO_BUTTON;
    public static final /* enum */ ControllerEventSource TOUCH;
    public static final /* enum */ ControllerEventSource TOUCH_FACE;
    public static final /* enum */ ControllerEventSource UNKNOWN;
    public static final /* enum */ ControllerEventSource VIDEO_BUTTON;
    public final int mType;

    static {
        UNKNOWN = new ControllerEventSource(0);
        KEY = new ControllerEventSource(0);
        TOUCH = new ControllerEventSource(0);
        TOUCH_FACE = new ControllerEventSource(0);
        PHOTO_BUTTON = new ControllerEventSource(1);
        VIDEO_BUTTON = new ControllerEventSource(2);
        PAUSE_RESUME_BUTTON = new ControllerEventSource(2);
        AUTO_STATE_TRANSITION = new ControllerEventSource(0);
        DEVICE = new ControllerEventSource(0);
        OTHER = new ControllerEventSource(0);
        ControllerEventSource[] arrcontrollerEventSource = new ControllerEventSource[]{UNKNOWN, KEY, TOUCH, TOUCH_FACE, PHOTO_BUTTON, VIDEO_BUTTON, PAUSE_RESUME_BUTTON, AUTO_STATE_TRANSITION, DEVICE, OTHER};
        $VALUES = arrcontrollerEventSource;
    }

    private ControllerEventSource(int n2) {
        super(string, n);
        this.mType = n2;
    }

    public static ControllerEventSource getButtonEvent(int n) {
        switch (n) {
            default: {
                return OTHER;
            }
            case 0: {
                return PHOTO_BUTTON;
            }
            case 1: {
                return VIDEO_BUTTON;
            }
            case 2: 
        }
        return PAUSE_RESUME_BUTTON;
    }

    public static ControllerEventSource valueOf(String string) {
        return (ControllerEventSource)Enum.valueOf((Class)ControllerEventSource.class, (String)string);
    }

    public static ControllerEventSource[] values() {
        return (ControllerEventSource[])$VALUES.clone();
    }
}

