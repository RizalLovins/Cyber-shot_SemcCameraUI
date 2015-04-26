/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.RelativeLayout
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.CameraSize;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;

/*
 * Failed to analyse overrides
 */
public class TouchGuideView
extends RelativeLayout {
    private static final String TAG = "TouchGuideView";
    private TouchCapture mCaptureMethod;
    private ViewGroup mParentView;
    private int mSensorOrientation = 2;
    private TextView mTextView;
    private TextView mTextViewPortrait;
    private TouchGuideListener mTouchGuideListener;

    public TouchGuideView(Context context) {
        super(context);
    }

    public TouchGuideView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TouchGuideView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private void updateContentDescription() {
        if (this.mSensorOrientation == 2) {
            this.setContentDescription(this.mTextView.getText());
            return;
        }
        this.setContentDescription(this.mTextViewPortrait.getText());
    }

    public void arrangeTouchCaptureArea(Rect rect) {
        Rect rect2 = CameraSize.getDisplayRect();
        int n = rect.top;
        int n2 = rect2.height() - rect.bottom;
        int n3 = rect.left;
        int n4 = rect2.width() - rect.right;
        this.setPadding(0, n, 0, n2);
        this.findViewById((int)2131689612).getLayoutParams().width = n3;
        this.findViewById((int)2131689614).getLayoutParams().width = n4;
    }

    public void cancel() {
        if (this.getVisibility() == 0) {
            this.mCaptureMethod = null;
            this.setVisibility(4);
            this.mParentView.removeView((View)this);
            if (this.mTouchGuideListener != null) {
                this.mTouchGuideListener.onHideTouchGuide();
            }
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTextView = (TextView)this.findViewById(2131689617);
        this.mTextViewPortrait = (TextView)this.findViewById(2131689618);
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        switch (n) {
            default: {
                return false;
            }
            case 24: 
            case 25: 
            case 27: 
            case 80: {
                this.cancel();
                return false;
            }
            case 4: 
            case 82: 
        }
        return true;
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        boolean bl = true;
        switch (n) {
            default: {
                bl = false;
            }
            case 82: {
                return bl;
            }
            case 4: 
        }
        this.cancel();
        return bl;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean bl = true;
        super.onTouchEvent(motionEvent);
        switch (motionEvent.getAction()) {
            default: {
                bl = false;
            }
            case 0: {
                return bl;
            }
            case 1: 
        }
        this.cancel();
        return bl;
    }

    public void setCaptureMethod(TouchCapture touchCapture) {
        this.mCaptureMethod = touchCapture;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setFocusMode(FocusMode focusMode, int n) {
        if (n == 1) {
            if (FocusMode.TOUCH_FOCUS.equals((Object)focusMode)) {
                this.mTextView.setText(2131361917);
                this.mTextViewPortrait.setText(2131361917);
            } else {
                this.mTextView.setText(2131361912);
                this.mTextViewPortrait.setText(2131361912);
            }
        } else {
            this.mTextView.setText(2131361936);
            this.mTextViewPortrait.setText(2131361936);
        }
        super.updateContentDescription();
    }

    public void setListener(TouchGuideListener touchGuideListener) {
        this.mTouchGuideListener = touchGuideListener;
    }

    public void setParent(ViewGroup viewGroup) {
        this.mParentView = viewGroup;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSensorOrientation(int n) {
        this.mSensorOrientation = n;
        if (this.mSensorOrientation == 2) {
            this.findViewById(2131689617).setVisibility(0);
            this.findViewById(2131689618).setVisibility(8);
            this.findViewById(2131689615).setRotation(0.0f);
        } else {
            this.findViewById(2131689617).setVisibility(8);
            this.findViewById(2131689618).setVisibility(0);
            this.findViewById(2131689615).setRotation(-90.0f);
        }
        this.invalidate();
        super.updateContentDescription();
    }

    public void show(TouchCapture touchCapture) {
        if (this.mCaptureMethod != null && !this.mCaptureMethod.equals((Object)touchCapture) && TouchCapture.VIEW_FINDER.equals((Object)touchCapture)) {
            if (this.mTouchGuideListener != null) {
                this.mTouchGuideListener.onShowTouchGuide();
            }
            this.mParentView.addView((View)this);
            this.setVisibility(0);
            this.requestFocus();
            this.invalidate();
            super.updateContentDescription();
        }
    }

    public static interface TouchGuideListener {
        public void onHideTouchGuide();

        public void onShowTouchGuide();
    }

}

