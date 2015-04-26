/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.os.Handler
 *  android.view.MotionEvent
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.sonyericsson.android.camera.controller;

import android.graphics.Point;
import android.os.Handler;
import android.view.MotionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class TouchFocusDetector {
    private static final String TAG = "TouchFocusDetector";
    private static final int TOUCH_STOP_DETECTION_TIMER_INTERVAL = 300;
    private final Point mCurrentTouchPos = new Point(0, 0);
    private boolean mIsFingerAlreadyMoved = false;
    private final Point mLastCheckedPos = new Point(0, 0);
    private float mLastCheckedRad = 0.0f;
    private MotionEventDetectorListener mListener;
    private final int mTouchSlop;
    private final Point mTouchSlopAreaCenterPos = new Point(0, 0);
    private Timer mTouchStopDetectorTimer;
    private TouchStopDetectorTimerTask mTouchStopDetectorTimerTask;
    private final Handler mUiThreadHandler = new Handler();

    public TouchFocusDetector(int n) {
        this.mTouchSlop = n;
    }

    static /* synthetic */ Point access$100(TouchFocusDetector touchFocusDetector) {
        return touchFocusDetector.mCurrentTouchPos;
    }

    static /* synthetic */ Point access$200(TouchFocusDetector touchFocusDetector) {
        return touchFocusDetector.mLastCheckedPos;
    }

    static /* synthetic */ Point access$900(TouchFocusDetector touchFocusDetector) {
        return touchFocusDetector.mTouchSlopAreaCenterPos;
    }

    private void onTouchStopDetected() {
        this.mIsFingerAlreadyMoved = false;
        this.mTouchSlopAreaCenterPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mUiThreadHandler.post((Runnable)new Runnable(){

            public void run() {
                if (TouchFocusDetector.this.mListener != null) {
                    TouchFocusDetector.this.mListener.onTouchStopDetected(TouchFocusDetector.access$900((TouchFocusDetector)TouchFocusDetector.this).x, TouchFocusDetector.access$900((TouchFocusDetector)TouchFocusDetector.this).y);
                }
            }
        });
    }

    private void release() {
        if (this.mTouchStopDetectorTimer != null) {
            this.mTouchStopDetectorTimer.cancel();
            this.mTouchStopDetectorTimer.purge();
        }
        this.mTouchStopDetectorTimer = null;
        if (this.mTouchStopDetectorTimerTask != null) {
            this.mTouchStopDetectorTimerTask.cancel();
        }
        this.mTouchStopDetectorTimerTask = null;
    }

    private void updateLastCheckedParameters(int n, int n2, float f) {
        this.mLastCheckedPos.set(n, n2);
        this.mLastCheckedRad = f;
    }

    public boolean isDragging() {
        return this.mIsFingerAlreadyMoved;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouch(MotionEvent var1) {
        switch (var1.getAction()) {
            case 0: {
                this.start(var1);
                ** break;
            }
            case 2: {
                this.update(var1);
                ** break;
            }
            case 3: {
                this.stop();
            }
lbl10: // 4 sources:
            default: {
                return true;
            }
            case 1: 
        }
        this.stop();
        return true;
    }

    public void setMotionEventDetectorListener(MotionEventDetectorListener motionEventDetectorListener) {
        this.mListener = motionEventDetectorListener;
    }

    public void start(MotionEvent motionEvent) {
        void var5_2 = this;
        synchronized (var5_2) {
            int n = (int)motionEvent.getRawX();
            int n2 = (int)motionEvent.getRawY();
            this.mTouchSlopAreaCenterPos.set(n, n2);
            this.mIsFingerAlreadyMoved = false;
            super.release();
            this.mTouchStopDetectorTimer = new Timer(true);
            this.mTouchStopDetectorTimerTask = new TouchStopDetectorTimerTask((TouchFocusDetector)this, null);
            this.mTouchStopDetectorTimer.scheduleAtFixedRate((TimerTask)this.mTouchStopDetectorTimerTask, 300, 300);
            return;
        }
    }

    public void stop() {
        TouchFocusDetector touchFocusDetector = this;
        synchronized (touchFocusDetector) {
            this.mIsFingerAlreadyMoved = false;
            this.release();
            this.mCurrentTouchPos.set(0, 0);
            this.mLastCheckedPos.set(0, 0);
            this.mLastCheckedRad = 0.0f;
            return;
        }
    }

    public void update(MotionEvent motionEvent) {
        this.mCurrentTouchPos.set((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
        int n = this.mCurrentTouchPos.x - this.mTouchSlopAreaCenterPos.x;
        int n2 = this.mCurrentTouchPos.y - this.mTouchSlopAreaCenterPos.y;
        if (this.mTouchSlop * this.mTouchSlop < n * n + n2 * n2) {
            this.mIsFingerAlreadyMoved = true;
            if (this.mListener != null) {
                this.mListener.onTouchMoveDetected(motionEvent);
            }
        }
    }

    public static interface MotionEventDetectorListener {
        public void onTouchMoveDetected(MotionEvent var1);

        public void onTouchStopDetected(int var1, int var2);
    }

    /*
     * Failed to analyse overrides
     */
    private class TouchStopDetectorTimerTask
    extends TimerTask {
        final /* synthetic */ TouchFocusDetector this$0;

        private TouchStopDetectorTimerTask(TouchFocusDetector touchFocusDetector) {
            this.this$0 = touchFocusDetector;
        }

        /* synthetic */ TouchStopDetectorTimerTask(TouchFocusDetector touchFocusDetector,  var2_2) {
            super(touchFocusDetector);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            int n = TouchFocusDetector.access$100((TouchFocusDetector)this.this$0).x - TouchFocusDetector.access$200((TouchFocusDetector)this.this$0).x;
            int n2 = TouchFocusDetector.access$100((TouchFocusDetector)this.this$0).y - TouchFocusDetector.access$200((TouchFocusDetector)this.this$0).y;
            float f = (float)Math.atan2((double)n2, (double)n);
            float f2 = f - this.this$0.mLastCheckedRad;
            this.this$0.updateLastCheckedParameters(TouchFocusDetector.access$100((TouchFocusDetector)this.this$0).x, TouchFocusDetector.access$100((TouchFocusDetector)this.this$0).y, f);
            if (!this.this$0.mIsFingerAlreadyMoved) {
                return;
            }
            if (n == 0 && n2 == 0) {
                this.this$0.onTouchStopDetected();
                return;
            }
            if (n * n + n2 * n2 >= this.this$0.mTouchSlop * this.this$0.mTouchSlop) return;
            if (Math.abs((int)((int)(100.0f * f2))) < 104) return;
            this.this$0.onTouchStopDetected();
        }
    }

}

