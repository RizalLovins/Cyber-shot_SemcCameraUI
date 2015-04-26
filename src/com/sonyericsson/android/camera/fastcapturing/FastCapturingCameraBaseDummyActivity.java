/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Bundle
 *  android.os.Handler
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.fastcapturing.FastCapturingActivity;
import java.util.ArrayList;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class FastCapturingCameraBaseDummyActivity
extends Activity {
    private static final int FINISH_DELAY_TIME = 100;
    public static final String INTENT_ACTION_FAST_CAPTURING_CAMERA_FINISHED = "com.sonyericsson.android.camera.action.FAST_CAPTURING_CAMERA_FINISHED";
    private static String TAG = "FastCapturingCameraBaseDummyActivity";
    private static List<Activity> mAvailableActivities = new ArrayList();
    private static final IntentFilter mDestroyIntentFilter = new IntentFilter();
    private DestroyRequestReceiver mDestroyRequestReceiver;
    FinishAllTask mFinishAllTask;
    private Handler mHandler = new Handler();

    static {
        mDestroyIntentFilter.addAction("com.sonyericsson.android.camera.action.FAST_CAPTURING_CAMERA_FINISHED");
        mDestroyIntentFilter.addAction("android.intent.action.SCREEN_ON");
        mDestroyIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        mDestroyIntentFilter.addAction("android.intent.action.USER_PRESENT");
    }

    public FastCapturingCameraBaseDummyActivity() {
        this.mFinishAllTask = new FinishAllTask(this, null);
    }

    public void finishAll() {
        this.mHandler.postDelayed((Runnable)this.mFinishAllTask, 100);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        this.mDestroyRequestReceiver = new DestroyRequestReceiver();
        this.registerReceiver((BroadcastReceiver)this.mDestroyRequestReceiver, mDestroyIntentFilter);
        mAvailableActivities.add((Object)this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mDestroyRequestReceiver != null) {
            this.unregisterReceiver((BroadcastReceiver)this.mDestroyRequestReceiver);
            this.mDestroyRequestReceiver = null;
        }
        mAvailableActivities.remove((Object)this);
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        this.finish();
        return false;
    }

    public void onNewIntent(Intent intent) {
        if ("com.sonyericsson.android.camera.action.FAST_CAPTURING_CAMERA_FINISHED".equals((Object)intent.getAction()) || "android.intent.action.SCREEN_ON".equals((Object)intent.getAction()) || "android.intent.action.SCREEN_OFF".equals((Object)intent.getAction()) || "android.intent.action.USER_PRESENT".equals((Object)intent.getAction())) {
            this.finishAll();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
        switch (FastCapturingActivity.staticCameraDevice().getCameraDeviceStatus()) {
            default: {
                return;
            }
            case 0: 
        }
        this.finish();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.finish();
        return false;
    }

    /*
     * Failed to analyse overrides
     */
    class DestroyRequestReceiver
    extends BroadcastReceiver {
        DestroyRequestReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            FastCapturingCameraBaseDummyActivity.this.finishAll();
        }
    }

    /*
     * Failed to analyse overrides
     */
    private class FinishAllTask
    implements Runnable {
        final /* synthetic */ FastCapturingCameraBaseDummyActivity this$0;

        private FinishAllTask(FastCapturingCameraBaseDummyActivity fastCapturingCameraBaseDummyActivity) {
            this.this$0 = fastCapturingCameraBaseDummyActivity;
        }

        /* synthetic */ FinishAllTask(FastCapturingCameraBaseDummyActivity fastCapturingCameraBaseDummyActivity,  var2_2) {
            super(fastCapturingCameraBaseDummyActivity);
        }

        private void retry() {
            this.this$0.mHandler.postDelayed((Runnable)this.this$0.mFinishAllTask, 100);
        }

        public void run() {
            switch (FastCapturingActivity.staticCameraDevice().getCameraDeviceStatus()) {
                default: {
                    for (Activity activity : mAvailableActivities) {
                        if (activity.isFinishing()) continue;
                        activity.finish();
                    }
                    break;
                }
                case 1: {
                    this.retry();
                }
            }
        }
    }

}

