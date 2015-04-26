/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.cameracommon.R;

/*
 * Failed to analyse overrides
 */
public class Switch
extends LinearLayout
implements GestureDetector.OnGestureListener {
    private static final int OFF_POSITION = 0;
    private static final int PRESSED_COLOR_FILTER = 1711276032;
    public static final String TAG = Switch.class.getSimpleName();
    private boolean mIsChecked;
    private boolean mIsTouched;
    private float mLastDownX = 0.0f;
    private float mLastDownY = 0.0f;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private int mOnPosition = 0;
    private ViewGroup mSwitchThumbContainer;
    private ImageView mSwitchTrack;
    private TextView mText;
    private final ViewConfiguration mViewConfiguration;

    public Switch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mViewConfiguration = ViewConfiguration.get((Context)context);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void changeState(MotionEvent motionEvent) {
        boolean bl = this.mIsChecked;
        if (motionEvent.getEventTime() - motionEvent.getDownTime() < (long)ViewConfiguration.getTapTimeout()) {
            if (!super.isMotionVertical(motionEvent.getY(), this.mLastDownY)) {
                bl = !this.mIsChecked;
            }
        } else {
            int n = super.getCurrentSliderGripPositionX(motionEvent.getX(), this.mLastDownX);
            int n2 = n + 0;
            int n3 = this.mOnPosition - n;
            bl = false;
            if (n2 <= n3) {
                bl = true;
            }
        }
        if (this.mIsChecked != bl) {
            this.mIsChecked = bl;
            this.updateIcon();
            super.setDefaultPosition();
            this.mOnCheckedChangeListener.onCheckedChanged(null, bl);
            return;
        }
        super.setDefaultPosition();
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getCurrentSliderGripPositionX(float f, float f2) {
        int n = (int)(f - f2);
        int n2 = this.mIsChecked ? this.mOnPosition - n : 0 - n;
        if (n2 > 0) {
            return 0;
        }
        if (n2 >= this.mOnPosition) return n2;
        return this.mOnPosition;
    }

    private boolean isMotionVertical(float f, float f2) {
        int n = (int)(f - f2);
        int n2 = this.mViewConfiguration.getScaledTouchSlop();
        boolean bl = false;
        if (n > n2) {
            bl = true;
        }
        return bl;
    }

    private void setDefaultPosition() {
        if (this.mIsChecked) {
            this.mSwitchThumbContainer.scrollTo(this.mOnPosition, 0);
            return;
        }
        this.mSwitchThumbContainer.scrollTo(0, 0);
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSwitchTrack = (ImageView)this.findViewById(R.id.switch_track);
        this.mSwitchThumbContainer = (ViewGroup)this.findViewById(R.id.switch_thumb_container);
        this.mText = (TextView)this.findViewById(R.id.switch_category);
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
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
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isEnabled()) {
            return false;
        }
        switch (motionEvent.getAction()) {
            default: {
                return super.onTouchEvent(motionEvent);
            }
            case 0: {
                this.mIsTouched = true;
                this.updateIcon();
                this.mLastDownX = motionEvent.getX();
                this.mLastDownY = motionEvent.getY();
                return true;
            }
            case 2: {
                this.mIsTouched = true;
                int n = super.getCurrentSliderGripPositionX(motionEvent.getX(), this.mLastDownX);
                this.mSwitchThumbContainer.scrollTo(n, 0);
                return true;
            }
            case 1: 
            case 3: 
        }
        if (!this.mIsTouched) return true;
        this.mIsTouched = false;
        super.changeState(motionEvent);
        return true;
    }

    public void setChecked(boolean bl) {
        this.mIsChecked = bl;
        this.updateIcon();
        super.setDefaultPosition();
    }

    public void setEnabled(boolean bl) {
        super.setEnabled(bl);
        if (this.isEnabled()) {
            this.mText.setTextColor(this.getResources().getColor(R.color.default_text_col));
            this.mSwitchTrack.clearColorFilter();
            return;
        }
        this.mText.setTextColor(this.getResources().getColor(R.color.grayout_text_col));
        this.mSwitchTrack.setColorFilter(1711276032);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setPressed(boolean bl) {
    }

    public void setText(CharSequence charSequence) {
        this.mText.setText(charSequence);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updateIcon() {
        if (this.mIsChecked) {
            this.mSwitchTrack.setImageResource(R.drawable.cam_setting_dialog_item_switch_bg_on_icn);
        } else {
            this.mSwitchTrack.setImageResource(R.drawable.cam_setting_dialog_item_switch_bg_off_icn);
        }
        this.mOnPosition = - this.mSwitchTrack.getDrawable().getIntrinsicWidth() / 2;
    }
}

