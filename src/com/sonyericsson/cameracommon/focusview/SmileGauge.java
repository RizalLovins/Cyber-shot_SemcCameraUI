/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.focusview.SmileScore;
import com.sonyericsson.cameracommon.focusview.TaggedRectangle;

/*
 * Failed to analyse overrides
 */
public class SmileGauge
extends RelativeLayout {
    public static final int SMILE_LEVEL = 5;
    public static final int SMILE_MAX = 100;
    public static final int SMILE_MIN = 0;
    private static final String TAG = "SmileGauge";
    protected boolean mAlignBottom;
    protected boolean mAlignRight;
    private Rect mDisplayRect;
    private int mFaceRectangleBottom;
    private int mFaceRectangleLeft;
    private int mFaceRectangleRight;
    private int mFaceRectangleTop;
    private int mGaugePositionLimitBottom;
    private int mGaugePositionLimitLeft;
    private int mGaugePositionLimitRight;
    private int mGaugePositionLimitTop;
    private boolean mIsForLandscape;
    private int mMargin;
    private int mSmileScore;
    private int mSurfaceHeight;
    private int mSurfaceWidth;

    public SmileGauge(Context context) {
        super(context, null);
    }

    public SmileGauge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public SmileGauge(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mGaugePositionLimitTop = 0;
        this.mGaugePositionLimitLeft = 0;
        this.mGaugePositionLimitRight = 0;
        this.mGaugePositionLimitBottom = 0;
        this.mAlignBottom = false;
        this.mAlignRight = false;
        this.mIsForLandscape = true;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SmileGauge);
        this.mIsForLandscape = typedArray.getBoolean(R.styleable.SmileGauge_forLandscape, true);
        typedArray.recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void correctPositionHorizontalLandScape() {
        int n = this.mDisplayRect.width();
        int n2 = (n - this.mSurfaceWidth) / 2;
        int n3 = this.mGaugePositionLimitLeft > n2 ? this.mGaugePositionLimitLeft : n2;
        int n4 = this.mGaugePositionLimitRight > n2 ? n - this.mGaugePositionLimitRight : n - n2;
        int n5 = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_width);
        int n6 = n3 + n5;
        int n7 = n4 - n5;
        int n8 = n6 - n2;
        int n9 = n7 - n2;
        if (this.mFaceRectangleLeft < n8) {
            this.moveToRight(2);
            return;
        } else {
            if (n9 >= this.mFaceRectangleRight) return;
            {
                this.moveToLeft(2);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void correctPositionHorizontalPortrait() {
        int n = this.mDisplayRect.width();
        int n2 = (n - this.mSurfaceWidth) / 2;
        int n3 = this.mGaugePositionLimitLeft > n2 ? this.mGaugePositionLimitLeft : n2;
        int n4 = n - n2;
        int n5 = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_height);
        if (this.mFaceRectangleLeft < n3) {
            this.moveToRight(1);
            return;
        } else {
            if (n4 >= n5 + this.mFaceRectangleLeft) return;
            {
                this.moveToLeft(1);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void correctPositionVerticalLandScape() {
        int n = this.mDisplayRect.height();
        int n2 = (n - this.mSurfaceHeight) / 2;
        int n3 = this.mGaugePositionLimitTop > n2 ? this.mGaugePositionLimitTop : n2;
        int n4 = this.mGaugePositionLimitBottom > n2 ? n - this.mGaugePositionLimitBottom : n - n2;
        if (n4 < this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_height) + this.mFaceRectangleTop) {
            this.moveToTop(2);
            return;
        } else {
            if (this.mFaceRectangleTop >= n3) return;
            {
                this.moveToBottom(2);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void correctPositionVerticalPortrait() {
        int n = this.mDisplayRect.height();
        int n2 = (n - this.mSurfaceHeight) / 2;
        int n3 = this.mGaugePositionLimitTop > n2 ? this.mGaugePositionLimitTop : n2;
        int n4 = this.mGaugePositionLimitBottom > n2 ? n - this.mGaugePositionLimitBottom : n - n2;
        int n5 = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_width);
        int n6 = n3 + n5;
        int n7 = n4 - n5;
        int n8 = n6 - n2;
        if (n7 - n2 < this.mFaceRectangleBottom) {
            this.moveToTop(1);
            if (this.mFaceRectangleTop >= n8) return;
            {
                this.setVisibility(4);
                return;
            }
        } else {
            if (this.mFaceRectangleTop >= n8) return;
            {
                this.moveToBottom(1);
                return;
            }
        }
    }

    protected void alignBottom() {
        this.mAlignBottom = true;
    }

    protected void alignLeft() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(5, R.id.rect);
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    protected void alignRight() {
        this.mAlignRight = true;
    }

    protected void alignTop() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(6, R.id.rect);
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void centerHorizontal() {
        int n = this.mFaceRectangleRight - this.mFaceRectangleLeft - this.getWidth();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.leftMargin = n > 0 ? n / 2 : 0;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void centerVertical() {
        int n = this.mFaceRectangleBottom - this.mFaceRectangleTop - this.getHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.topMargin = n > 0 ? n / 2 : 0;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    protected void clearLayoutParams() {
        this.mAlignBottom = false;
        this.mAlignRight = false;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(6, 0);
        layoutParams.addRule(5, 0);
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = 0;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void drawThreshold() {
        ImageView imageView = (ImageView)this.findViewById(R.id.smile_gauge_threshold);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        if (this.isForLandscape()) {
            layoutParams.topMargin = this.mMargin;
            layoutParams.addRule(9);
        } else {
            layoutParams.leftMargin = this.mMargin;
            layoutParams.addRule(12);
        }
        imageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public int getSmileScore() {
        return this.mSmileScore;
    }

    public boolean isForLandscape() {
        return this.mIsForLandscape;
    }

    public void moveToBottom(int n) {
        if (n == 2) {
            this.clearLayoutParams();
            this.alignTop();
            return;
        }
        this.moveToId(R.id.smile_gauge_bottom);
    }

    protected void moveToId(int n) {
        if (this.getId() != n) {
            this.setVisibility(8);
            return;
        }
        this.setVisibility(0);
    }

    protected void moveToLeft(int n) {
        if (n == 2) {
            this.moveToId(R.id.smile_gauge_left);
            return;
        }
        this.clearLayoutParams();
        this.alignRight();
    }

    protected void moveToRight(int n) {
        if (n == 2) {
            this.moveToId(R.id.smile_gauge_right);
            return;
        }
        this.clearLayoutParams();
        this.alignLeft();
    }

    protected void moveToTop(int n) {
        if (n == 2) {
            this.clearLayoutParams();
            this.alignBottom();
            return;
        }
        this.moveToId(R.id.smile_gauge_top);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSmileScore = 0;
        this.mGaugePositionLimitTop = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_position_limit_top);
        this.mGaugePositionLimitLeft = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_position_limit_left);
        this.mGaugePositionLimitRight = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_position_limit_right);
        this.mGaugePositionLimitBottom = this.getResources().getDimensionPixelSize(R.dimen.smile_gauge_position_limit_bottom);
    }

    public void setDisplayRect(Rect rect) {
        this.mDisplayRect = rect;
    }

    public void setPosition(int n, int n2, int n3, int n4, int n5) {
        this.mFaceRectangleLeft = n;
        this.mFaceRectangleTop = n2;
        this.mFaceRectangleRight = n3;
        this.mFaceRectangleBottom = n4;
        if (this.mFaceRectangleLeft == this.mFaceRectangleRight || this.mFaceRectangleTop == this.mFaceRectangleBottom) {
            this.setVisibility(4);
            return;
        }
        this.update(n5);
    }

    public void setSmileLevel(int n) {
        this.mMargin = this.getResources().getDimensionPixelSize(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSmileScore(int n) {
        if (n < 0) {
            n = 0;
        } else if (n > 100) {
            n = 100;
        }
        this.mSmileScore = n;
        SmileScore smileScore = (SmileScore)this.findViewById(R.id.smile_gauge_score);
        smileScore.setSmileScore(n);
        smileScore.invalidate();
    }

    public void setSurfaceSize(int n, int n2) {
        this.mSurfaceWidth = n;
        this.mSurfaceHeight = n2;
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        if (n == 0) {
            this.drawThreshold();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)((TaggedRectangle)this.getParent()).getLayoutParams();
            this.setSurfaceSize(layoutParams.width, layoutParams.height);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void update(int n) {
        if (this.isForLandscape() && n != 2 || !this.isForLandscape() && n != 1) {
            this.clearLayoutParams();
            this.setVisibility(8);
            return;
        }
        if (n == 2) {
            this.moveToLeft(n);
            this.moveToBottom(n);
            super.correctPositionVerticalLandScape();
            super.correctPositionHorizontalLandScape();
        } else {
            this.moveToBottom(n);
            this.moveToRight(n);
            super.correctPositionVerticalPortrait();
            super.correctPositionHorizontalPortrait();
        }
        this.postInvalidate();
    }
}

