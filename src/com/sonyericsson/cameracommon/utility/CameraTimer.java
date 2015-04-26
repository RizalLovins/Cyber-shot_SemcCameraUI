/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Message
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.sonyericsson.cameracommon.utility;

import android.os.Handler;
import android.os.Message;
import java.util.Timer;
import java.util.TimerTask;

public class CameraTimer {
    public static final int MSG_CANCEL = 2;
    public static final int MSG_INTERVAL = 0;
    public static final int MSG_POST_TIMEOUT = 3;
    public static final int MSG_TIMEOUT = 1;
    private static final String TAG = "SelfTimer";
    private long mCurTime;
    private long mDelay;
    private Handler mHandler;
    private long mInterval;
    private String mOptionName;
    private Timer mTimer;

    public CameraTimer(long l, long l2, Handler handler, String string, long l3) {
        this.mCurTime = l;
        this.mHandler = handler;
        this.mInterval = l2;
        this.mOptionName = string;
        this.mDelay = l3;
        if (l <= 0 || l2 <= 0 || handler == null || this.mCurTime < this.mInterval || this.mCurTime > Integer.MAX_VALUE) {
            this.mTimer = null;
            return;
        }
        this.mTimer = new Timer(true);
    }

    static /* synthetic */ long access$122(CameraTimer cameraTimer, long l) {
        long l2;
        cameraTimer.mCurTime = l2 = cameraTimer.mCurTime - l;
        return l2;
    }

    private void terminateInnerTimer() {
        CameraTimer cameraTimer = this;
        synchronized (cameraTimer) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer.purge();
                this.mTimer = null;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void cancel() {
        CameraTimer cameraTimer = this;
        synchronized (cameraTimer) {
            if (this.mTimer != null) {
                this.terminateInnerTimer();
                Message message = Message.obtain();
                message.arg1 = (int)this.mCurTime;
                message.what = 2;
                this.mHandler.sendMessage(message);
            }
            this.mHandler.removeMessages(1);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void start() {
        CameraTimer cameraTimer = this;
        synchronized (cameraTimer) {
            Timer timer = this.mTimer;
            if (timer != null) {
                this.mTimer.schedule((TimerTask)new SelfTimerTimerTask(this, null), this.mDelay, this.mInterval);
            }
            return;
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class SelfTimerTimerTask
    extends TimerTask {
        final /* synthetic */ CameraTimer this$0;

        private SelfTimerTimerTask(CameraTimer cameraTimer) {
            this.this$0 = cameraTimer;
        }

        /* synthetic */ SelfTimerTimerTask(CameraTimer cameraTimer,  var2_2) {
            super(cameraTimer);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void run() {
            if (this.this$0.mCurTime > 0) {
                Message message = Message.obtain();
                message.arg1 = (int)this.this$0.mCurTime;
                message.what = 0;
                this.this$0.mHandler.sendMessage(message);
            } else {
                Message message = Message.obtain();
                message.arg1 = (int)this.this$0.mCurTime;
                message.what = 1;
                this.this$0.mHandler.sendMessage(message);
                this.this$0.terminateInnerTimer();
            }
            CameraTimer.access$122(this.this$0, this.this$0.mInterval);
        }
    }

}

