/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.CharSequence
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.viewfinder.capturingmode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.ViewUtility;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.CapturingModeButtonAttributes;
import com.sonyericsson.cameracommon.viewfinder.capturingmode.OnClickCapturingModeButtonListener;

/*
 * Failed to analyse overrides
 */
public class CapturingModeButton
extends RelativeLayout
implements View.OnClickListener,
View.OnTouchListener {
    private static final int INVALID_ID = -1;
    private static final int PRESSED_COLOR_FILTER = 1711276032;
    private static final float ROTATE_DEGREE = -90.0f;
    private static final String TAG = CapturingModeButton.class.getSimpleName();
    private CapturingModeButtonAttributes mAttributes;
    private ImageView mBackgroundView;
    protected boolean mHasInvalidResources;
    private ImageView mIconView;
    private OnClickCapturingModeButtonListener mListener;
    private int mSensorOrientation;

    public CapturingModeButton(Context context) {
        super(context, null);
    }

    public CapturingModeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSensorOrientation = 0;
    }

    private ImageView createBackground() {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setClickable(false);
        imageView.setFocusable(false);
        imageView.setFocusableInTouchMode(false);
        this.addView((View)imageView);
        imageView.getLayoutParams().width = -1;
        imageView.getLayoutParams().height = -1;
        return imageView;
    }

    private ImageView createIcon() {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setClickable(false);
        imageView.setFocusable(false);
        imageView.setFocusableInTouchMode(false);
        this.addView((View)imageView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.addRule(15);
        layoutParams.addRule(14);
        return imageView;
    }

    private int getIconBitmapId() {
        if (this.mAttributes == null) {
            return -1;
        }
        return this.mAttributes.getIconId();
    }

    private void updateBackground() {
        CapturingModeButtonAttributes capturingModeButtonAttributes = this.mAttributes;
        int n = 0;
        if (capturingModeButtonAttributes == null) {
            n = 4;
        }
        if (this.mBackgroundView != null) {
            this.mBackgroundView.setVisibility(n);
        }
    }

    private void updateIcon() {
        if (this.mIconView == null) {
            return;
        }
        if (this.mAttributes == null) {
            this.mIconView.setVisibility(4);
            return;
        }
        int n = this.getIconBitmapId();
        this.mIconView.setImageResource(n);
        this.mIconView.setVisibility(0);
        int n2 = this.mAttributes.getTextId();
        String string = null;
        if (n2 != -1) {
            String string2 = this.getContext().getString(R.string.cam_strings_accessibility_capturing_mode_txt);
            string = string2 + " " + this.getContext().getString(n2);
        }
        this.setContentDescription((CharSequence)string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    private void updateLayout() {
        try {
            this.updateBackground();
            this.updateIcon();
            this.mHasInvalidResources = false;
        }
        catch (Resources.NotFoundException var2_1) {
            this.mHasInvalidResources = true;
        }
        catch (RuntimeException var1_2) {
            this.mHasInvalidResources = true;
        }
        if (this.mHasInvalidResources) {
            if (this.mIconView != null) {
                this.mIconView.setVisibility(4);
            }
            if (this.mBackgroundView != null) {
                this.mBackgroundView.setVisibility(4);
            }
        }
        this.requestLayout();
        this.invalidate();
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.mSensorOrientation == 1) {
            canvas.rotate(-90.0f, (float)this.getWidth() / 2.0f, (float)this.getHeight() / 2.0f);
        }
        super.dispatchDraw(canvas);
    }

    public CapturingModeButtonAttributes getCurrentCapturingMode() {
        return this.mAttributes;
    }

    public void onClick(View view) {
        if (this.mIconView.isShown()) {
            this.mListener.onClickCapturingModeButton((CapturingModeButton)this);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouch(View var1, MotionEvent var2_2) {
        switch (var2_2.getAction()) {
            case 0: {
                this.mIconView.setColorFilter(1711276032);
                ** break;
            }
            case 2: {
                if (!ViewUtility.hitTest(var1, var2_2)) {
                    this.mIconView.clearColorFilter();
                }
            }
lbl8: // 5 sources:
            default: {
                ** GOTO lbl12
            }
            case 1: 
            case 3: 
        }
        this.mIconView.clearColorFilter();
lbl12: // 2 sources:
        this.mIconView.invalidate();
        return false;
    }

    public void setCurrentCapturingMode(CapturingModeButtonAttributes capturingModeButtonAttributes) {
        this.mAttributes = capturingModeButtonAttributes;
        super.updateLayout();
    }

    public void setOnCapturingModeButtonListener(OnClickCapturingModeButtonListener onClickCapturingModeButtonListener) {
        this.mListener = onClickCapturingModeButtonListener;
    }

    public void setSensorOrientation(int n) {
        this.mSensorOrientation = n;
        super.updateLayout();
    }

    public void setup(OnClickCapturingModeButtonListener onClickCapturingModeButtonListener) {
        this.setVisibility(0);
        this.setClickable(true);
        this.setFocusable(false);
        this.setOnTouchListener((View.OnTouchListener)this);
        this.setOnClickListener((View.OnClickListener)this);
        this.setOnCapturingModeButtonListener(onClickCapturingModeButtonListener);
        this.mBackgroundView = super.createBackground();
        this.mIconView = super.createIcon();
        this.mHasInvalidResources = false;
        super.updateLayout();
    }
}

