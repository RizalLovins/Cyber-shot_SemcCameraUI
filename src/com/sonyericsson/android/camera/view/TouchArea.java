/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.RelativeLayout
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/*
 * Failed to analyse overrides
 */
public class TouchArea
extends RelativeLayout {
    private static final String TAG = TouchArea.class.getSimpleName();
    private boolean mIsCanceled;
    private View.OnTouchListener mListener;
    private Rect mTouchArea;

    public TouchArea(Context context) {
        super(context, null);
    }

    public TouchArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public TouchArea(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public boolean contains(MotionEvent motionEvent) {
        int n = (int)motionEvent.getRawX();
        int n2 = (int)motionEvent.getRawY();
        Rect rect = this.mTouchArea;
        boolean bl = false;
        if (rect != null) {
            bl = this.mTouchArea.contains(n, n2);
        }
        return bl;
    }

    public Rect getTouchAreaRect() {
        return this.mTouchArea;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1) {
        var2_2 = super.onTouchEvent(var1);
        switch (var1.getAction()) {
            case 0: {
                this.mIsCanceled = false;
                if (!this.contains(var1)) {
                    return var2_2;
                }
            }
            default: {
                ** GOTO lbl15
            }
            case 1: 
            case 2: 
        }
        if (this.mIsCanceled) {
            return true;
        }
        if (!this.contains(var1)) {
            this.mIsCanceled = true;
            var1.setAction(3);
        }
lbl15: // 4 sources:
        if (this.mListener == null) return var2_2;
        if (this.mListener.onTouch((View)this, var1) == false) return var2_2;
        return true;
    }

    public void setSurfaceArea(Rect rect) {
        int n = this.getResources().getDimensionPixelSize(2131296273);
        this.mTouchArea = new Rect(n + rect.left, n + rect.top, rect.right - n, rect.bottom - n);
    }

    public void setTocuhAreaListener(View.OnTouchListener onTouchListener) {
        this.mListener = onTouchListener;
    }
}

