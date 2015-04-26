/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.ImageView
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.fastcapturing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.interaction.TouchActionTranslator;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/*
 * Failed to analyse overrides
 */
public class CaptureArea
extends ImageView
implements TouchActionTranslator.TouchActionListener {
    private static final String TAG = "CaptureArea";
    private boolean mIsLongPressed = false;
    private boolean mIsTouched = false;
    private CaptureAreaStateListener mListener;
    private CaptureAreaTouchEventListener mTouchListener;
    private TouchActionTranslator mUserInteractionEngine = null;

    public CaptureArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTouchListener = new CaptureAreaTouchEventListener();
        this.mUserInteractionEngine = new TouchActionTranslator(context, (View)this, context.getResources().getDimensionPixelSize(2131296273));
        this.mUserInteractionEngine.setInteractionListener((TouchActionTranslator.TouchActionListener)this);
        this.setOnTouchListener((View.OnTouchListener)this.mTouchListener);
    }

    private Point convertPointCoordinatesFromThisViewToScreen(Point point) {
        int[] arrn = new int[2];
        this.getLocationOnScreen(arrn);
        return new Point(point.x + arrn[0], point.y + arrn[1]);
    }

    public boolean contains(MotionEvent motionEvent) {
        return CommonUtility.isEventContainedInView((View)this, motionEvent);
    }

    public boolean isTouched() {
        return this.mIsTouched;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onDoubleCanceled() {
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        if (this.mListener == null) return;
        this.mListener.onCaptureAreaCanceled();
    }

    public void onDoubleMoved(Point point, Point point2) {
    }

    public void onDoubleRotated(float f, float f2) {
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onDoubleScaled(float f, float f2, float f3) {
        if (!this.mIsTouched) {
            return;
        }
        float f4 = f - f2;
        if (this.mListener == null) return;
        this.mListener.onCaptureAreaScaled(f4);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onDoubleTouched(Point point, Point point2) {
        if (!(this.mIsTouched && this.mListener != null)) {
            return;
        }
        this.mListener.onCaptureAreaCanceled();
        this.mListener.onCaptureAreaIsReadyToScale();
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    public void onLongPress(MotionEvent motionEvent) {
        this.mIsLongPressed = true;
        if (this.mListener != null) {
            Point point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
            this.mListener.onCaptureAreaLongPressed(super.convertPointCoordinatesFromThisViewToScreen(point));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onOverTripleCanceled() {
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        if (this.mListener == null) return;
        this.mListener.onCaptureAreaCanceled();
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onSingleCanceled() {
        if (!this.mIsTouched) {
            return;
        }
        this.mIsTouched = false;
        if (this.mListener == null) return;
        this.mListener.onCaptureAreaCanceled();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onSingleMoved(Point point, Point point2, Point point3) {
        if (!(this.mIsTouched && this.mListener != null)) {
            return;
        }
        if (!this.isTouched()) {
            this.mListener.onCaptureAreaCanceled();
            return;
        }
        this.mListener.onCaptureAreaMoved(super.convertPointCoordinatesFromThisViewToScreen(point));
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onSingleReleased(Point point) {
        if (!(this.mIsTouched || this.mIsLongPressed)) {
            return;
        }
        this.mIsTouched = false;
        this.mIsLongPressed = false;
        if (this.mListener == null) return;
        this.mListener.onCaptureAreaReleased(super.convertPointCoordinatesFromThisViewToScreen(point));
    }

    public void onSingleReleasedInDouble(Point point, Point point2) {
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onSingleStopped(Point point, Point point2, Point point3) {
        if (!(this.mIsTouched && this.mListener != null)) {
            return;
        }
        this.mListener.onCaptureAreaStopped();
    }

    public void onSingleTapUp(MotionEvent motionEvent) {
        if (this.mListener != null) {
            Point point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
            this.mListener.onCaptureAreaSingleTapUp(super.convertPointCoordinatesFromThisViewToScreen(point));
        }
    }

    public void onSingleTouched(Point point) {
        this.mIsTouched = true;
        if (this.mListener != null) {
            this.mListener.onCaptureAreaTouched(super.convertPointCoordinatesFromThisViewToScreen(point));
        }
    }

    public void release() {
        this.mUserInteractionEngine.setInteractionListener(null);
        this.mUserInteractionEngine.release();
        this.mUserInteractionEngine = null;
        this.setOnTouchListener(null);
    }

    public void setCaptureAreaStateListener(CaptureAreaStateListener captureAreaStateListener) {
        this.mListener = captureAreaStateListener;
    }

    public static interface CaptureAreaStateListener {
        public void onCaptureAreaCanceled();

        public void onCaptureAreaIsReadyToScale();

        public void onCaptureAreaLongPressed(Point var1);

        public void onCaptureAreaMoved(Point var1);

        public void onCaptureAreaReleased(Point var1);

        public void onCaptureAreaScaled(float var1);

        public void onCaptureAreaSingleTapUp(Point var1);

        public void onCaptureAreaStopped();

        public void onCaptureAreaTouched(Point var1);
    }

    /*
     * Failed to analyse overrides
     */
    class CaptureAreaTouchEventListener
    implements View.OnTouchListener {
        CaptureAreaTouchEventListener() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            void var5_3 = this;
            synchronized (var5_3) {
                if (!CaptureArea.this.mUserInteractionEngine.onTouchEvent(motionEvent)) {
                    CaptureArea.this.mIsTouched = false;
                    if (CaptureArea.this.mListener != null) {
                        CaptureArea.this.mListener.onCaptureAreaCanceled();
                    }
                }
                return true;
            }
        }
    }

}

