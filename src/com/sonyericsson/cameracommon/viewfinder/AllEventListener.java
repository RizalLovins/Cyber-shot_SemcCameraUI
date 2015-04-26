/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.WeakReference
 */
package com.sonyericsson.cameracommon.viewfinder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sonyericsson.cameracommon.activity.BaseActivity;

/*
 * Failed to analyse overrides
 */
public class AllEventListener
extends View {
    private static final String TAG = "AllEventListener";
    private WeakReference<BaseActivity> mBaseActivity;
    boolean mTouchEventConsumed = true;

    public AllEventListener(Context context) {
        super(context);
    }

    public AllEventListener(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AllEventListener(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public void disableTouchEvent() {
        this.mTouchEventConsumed = true;
    }

    public void enableTouchEvent() {
        this.mTouchEventConsumed = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (motionEvent == null) return this.mTouchEventConsumed;
        switch (motionEvent.getAction()) {
            default: {
                return this.mTouchEventConsumed;
            }
            case 0: 
        }
        BaseActivity baseActivity = (BaseActivity)this.mBaseActivity.get();
        if (baseActivity == null) return this.mTouchEventConsumed;
        baseActivity.restartAutoOffTimer();
        return this.mTouchEventConsumed;
    }

    public void setActivity(BaseActivity baseActivity) {
        if (baseActivity == null) {
            throw new IllegalArgumentException("BaseActivity can not be null.");
        }
        this.mBaseActivity = new WeakReference((Object)baseActivity);
    }
}

