/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.controller.EventDispatcher;

/*
 * Failed to analyse overrides
 */
public class CaptureButtonGestureListener
implements GestureDetector.OnGestureListener {
    private static final String TAG = CaptureButtonGestureListener.class.getSimpleName();
    private final EventDispatcher mDispatcher;

    public CaptureButtonGestureListener(EventDispatcher eventDispatcher) {
        this.mDispatcher = eventDispatcher;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
        this.mDispatcher.sendLongPressEvent(ControllerEventSource.PHOTO_BUTTON);
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
}

