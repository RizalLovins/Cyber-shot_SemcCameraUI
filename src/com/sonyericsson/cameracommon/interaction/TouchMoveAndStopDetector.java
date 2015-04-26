/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.graphics.PointF
 *  android.os.Handler
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.sonyericsson.cameracommon.interaction;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import com.sonyericsson.cameracommon.interaction.VectorCalculator;
import java.util.Timer;
import java.util.TimerTask;

public class TouchMoveAndStopDetector {
    private static final float DIRECTION_TOLERANCE = 1.0471976f;
    private int TOUCH_STOP_DETECTION_TIMER_INTERVAL = 200;
    private Point mCurrentTouchPos = new Point(0, 0);
    private Point mDownPos = new Point(0, 0);
    private boolean mIsFingerAlreadyMoved = false;
    private Point mLatestCheckedPos = new Point(0, 0);
    private Point mLatestCheckedTrackVec = new Point(0, 0);
    private TouchStopDetectorListener mListener;
    private Point mPreviousTouchPos = new Point(0, 0);
    private final int mTouchSlop;
    private Point mTouchSlopAreaCenterPos = new Point(0, 0);
    private Timer mTouchStopDetectorTimer;
    private TouchStopDetectorTimerTask mTouchStopDetectorTimerTask;
    private Handler mUiThreadHandler = new Handler();

    public TouchMoveAndStopDetector(int n) {
        this.mTouchSlop = n;
    }

    static /* synthetic */ Point access$200(TouchMoveAndStopDetector touchMoveAndStopDetector) {
        return touchMoveAndStopDetector.mLatestCheckedPos;
    }

    private void killTimer() {
        if (this.mTouchStopDetectorTimer != null) {
            this.mTouchStopDetectorTimer.cancel();
            this.mTouchStopDetectorTimer.purge();
            this.mTouchStopDetectorTimer = null;
        }
        if (this.mTouchStopDetectorTimerTask != null) {
            this.mTouchStopDetectorTimerTask.cancel();
            this.mTouchStopDetectorTimerTask = null;
        }
    }

    private void onTouchStopDetected() {
        this.mIsFingerAlreadyMoved = false;
        this.mTouchSlopAreaCenterPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mUiThreadHandler.post((Runnable)new Runnable(){

            public void run() {
                if (TouchMoveAndStopDetector.this.mListener != null) {
                    TouchMoveAndStopDetector.this.mListener.onSingleTouchStopDetected(TouchMoveAndStopDetector.this.mCurrentTouchPos, TouchMoveAndStopDetector.this.mPreviousTouchPos, TouchMoveAndStopDetector.this.mDownPos);
                }
            }
        });
    }

    private void updateLastCheckedParameters(int n, int n2, Point point) {
        this.mLatestCheckedPos.set(n, n2);
        this.mLatestCheckedTrackVec.set(point.x, point.y);
    }

    void release() {
        this.killTimer();
        this.mListener = null;
    }

    public void setTouchStopDetectorListener(TouchStopDetectorListener touchStopDetectorListener) {
        this.mListener = touchStopDetectorListener;
    }

    public void startTouchStopDetection(int n, int n2) {
        void var4_3 = this;
        synchronized (var4_3) {
            this.mDownPos.set(n, n2);
            this.mPreviousTouchPos.set(n, n2);
            this.mTouchSlopAreaCenterPos.set(n, n2);
            this.mIsFingerAlreadyMoved = false;
            super.killTimer();
            this.mTouchStopDetectorTimer = new Timer(true);
            this.mTouchStopDetectorTimerTask = new TouchStopDetectorTimerTask((TouchMoveAndStopDetector)this, null);
            this.mTouchStopDetectorTimer.scheduleAtFixedRate((TimerTask)this.mTouchStopDetectorTimerTask, (long)this.TOUCH_STOP_DETECTION_TIMER_INTERVAL, (long)this.TOUCH_STOP_DETECTION_TIMER_INTERVAL);
            return;
        }
    }

    public void stopTouchStopDetection() {
        TouchMoveAndStopDetector touchMoveAndStopDetector = this;
        synchronized (touchMoveAndStopDetector) {
            this.killTimer();
            this.mCurrentTouchPos.set(0, 0);
            this.mPreviousTouchPos.set(0, 0);
            this.mLatestCheckedPos.set(0, 0);
            this.mLatestCheckedTrackVec.set(0, 0);
            return;
        }
    }

    public void updateCurrentAndLastPosition(int n, int n2) {
        this.mPreviousTouchPos.set(n, n2);
        this.mCurrentTouchPos.set(n, n2);
    }

    public void updateCurrentPosition(int n, int n2) {
        this.mPreviousTouchPos.set(this.mCurrentTouchPos.x, this.mCurrentTouchPos.y);
        this.mCurrentTouchPos.set(n, n2);
        int n3 = this.mCurrentTouchPos.x - this.mTouchSlopAreaCenterPos.x;
        int n4 = this.mCurrentTouchPos.y - this.mTouchSlopAreaCenterPos.y;
        if (this.mTouchSlop * this.mTouchSlop < n3 * n3 + n4 * n4) {
            this.mIsFingerAlreadyMoved = true;
            if (this.mListener != null) {
                this.mListener.onSingleTouchMoveDetected(this.mCurrentTouchPos, this.mPreviousTouchPos, this.mDownPos);
            }
        }
    }

    public static interface TouchStopDetectorListener {
        public void onSingleTouchMoveDetected(Point var1, Point var2, Point var3);

        public void onSingleTouchStopDetected(Point var1, Point var2, Point var3);
    }

    /*
     * Failed to analyse overrides
     */
    private class TouchStopDetectorTimerTask
    extends TimerTask {
        final /* synthetic */ TouchMoveAndStopDetector this$0;

        private TouchStopDetectorTimerTask(TouchMoveAndStopDetector touchMoveAndStopDetector) {
            this.this$0 = touchMoveAndStopDetector;
        }

        /* synthetic */ TouchStopDetectorTimerTask(TouchMoveAndStopDetector touchMoveAndStopDetector,  var2_2) {
            super(touchMoveAndStopDetector);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void run() {
            int n = TouchMoveAndStopDetector.access$100((TouchMoveAndStopDetector)this.this$0).x - TouchMoveAndStopDetector.access$200((TouchMoveAndStopDetector)this.this$0).x;
            int n2 = TouchMoveAndStopDetector.access$100((TouchMoveAndStopDetector)this.this$0).y - TouchMoveAndStopDetector.access$200((TouchMoveAndStopDetector)this.this$0).y;
            Point point = new Point(n, n2);
            float f = VectorCalculator.getRadianFrom2Vector(new PointF(point), new PointF(this.this$0.mLatestCheckedTrackVec));
            this.this$0.updateLastCheckedParameters(TouchMoveAndStopDetector.access$100((TouchMoveAndStopDetector)this.this$0).x, TouchMoveAndStopDetector.access$100((TouchMoveAndStopDetector)this.this$0).y, point);
            if (!this.this$0.mIsFingerAlreadyMoved) {
                return;
            }
            if (n == 0 && n2 == 0) {
                this.this$0.onTouchStopDetected();
                return;
            }
            if (n * n + n2 * n2 >= this.this$0.mTouchSlop * this.this$0.mTouchSlop) return;
            if (Math.abs((float)f) < 1.0471976f) return;
            this.this$0.onTouchStopDetected();
        }
    }

}

