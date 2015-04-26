/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.View
 *  android.widget.RelativeLayout
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/*
 * Failed to analyse overrides
 */
public class Rectangle
extends RelativeLayout
implements GestureDetector.OnGestureListener {
    private static final String TAG = "Rectangles";
    private GestureDetector mGestureDetector;
    private RectangleOnTouchListener mRectangleOnTouchListener;

    public Rectangle(Context context) {
        super(context);
    }

    public Rectangle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Rectangle(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private GestureDetector getGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(this.getContext(), (GestureDetector.OnGestureListener)this);
        }
        return this.mGestureDetector;
    }

    public void changeChildBackgroundResource(int n) {
        View view = this.findViewById(R.id.rect_image);
        if (view.getVisibility() != 8) {
            view.setBackgroundResource(n);
        }
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
        void var3_2 = this;
        synchronized (var3_2) {
            if (this.mRectangleOnTouchListener != null) {
                this.mRectangleOnTouchListener.onRectTouchLongPress(motionEvent);
            }
            return;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        void var8_2 = this;
        // MONITORENTER : var8_2
        super.onTouchEvent(motionEvent);
        super.getGestureDetector().onTouchEvent(motionEvent);
        RectangleOnTouchListener rectangleOnTouchListener = this.mRectangleOnTouchListener;
        boolean bl = false;
        if (rectangleOnTouchListener != null) {
            bl = true;
        }
        int n = motionEvent.getAction();
        switch (n) {
            case 0: {
                if (this.mRectangleOnTouchListener == null) return bl;
                {
                    this.mRectangleOnTouchListener.onRectTouchDown((View)this, motionEvent);
                }
            }
            default: {
                return bl;
            }
            case 1: 
        }
        if (this.mRectangleOnTouchListener != null) {
            if (CommonUtility.isEventContainedInView((View)this, motionEvent)) {
                this.mRectangleOnTouchListener.onRectTouchUp((View)this, motionEvent);
                return bl;
            }
        } else {
            // MONITOREXIT : var8_2
            return bl;
        }
        this.mRectangleOnTouchListener.onRectTouchCancel((View)this, motionEvent);
        return bl;
    }

    public void setRectangleOnTouchListener(RectangleOnTouchListener rectangleOnTouchListener) {
        void var3_2 = this;
        synchronized (var3_2) {
            this.mRectangleOnTouchListener = rectangleOnTouchListener;
            return;
        }
    }

    public static interface RectangleOnTouchListener {
        public void onRectTouchCancel(View var1, MotionEvent var2);

        public void onRectTouchDown(View var1, MotionEvent var2);

        public void onRectTouchLongPress(MotionEvent var1);

        public void onRectTouchUp(View var1, MotionEvent var2);
    }

}

