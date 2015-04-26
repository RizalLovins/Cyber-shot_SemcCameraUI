/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.util.AttributeSet
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
 * Failed to analyse overrides
 */
public class RotatableImageView
extends ImageView {
    private int mFixRotation = 0;
    private int mHeight = 0;
    private boolean mPrepared = false;
    private int mSensorOrientation = 2;
    private int mWidth = 0;

    public RotatableImageView(Context context) {
        super(context);
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }

    public RotatableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }

    public RotatableImageView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }

    public void clearFixedRotate(int n) {
        this.mFixRotation = 0;
        this.update();
    }

    public void fixRotation(int n) {
        this.mFixRotation = n;
        this.update();
    }

    protected boolean isPrepared() {
        return this.mPrepared;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        if (!this.isPrepared()) {
            this.setWidthHeight(this.getWidth(), this.getHeight());
            this.update();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.update();
    }

    public void setImageResource(int n) {
        super.setImageResource(n);
        this.update();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.update();
    }

    public void setSensorOrientation(int n) {
        this.mSensorOrientation = n;
        this.update();
    }

    public void setWidthHeight(int n, int n2) {
        this.mWidth = n;
        this.mHeight = n2;
        this.mPrepared = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void update() {
        int n;
        int n2;
        if (!this.isPrepared()) {
            return;
        }
        int n3 = this.getWidth();
        int n4 = this.getHeight();
        Matrix matrix = new Matrix();
        int n5 = this.mSensorOrientation;
        if (this.mFixRotation != 0) {
            n5 = this.mFixRotation;
        }
        if (this.getDrawable() != null) {
            matrix.preScale((float)this.mWidth / (float)this.getDrawable().getIntrinsicWidth(), (float)this.mHeight / (float)this.getDrawable().getIntrinsicHeight());
        }
        if (n5 == 1) {
            matrix.postTranslate((- (float)n3) / 2.0f, (- (float)n4) / 2.0f);
            matrix.postRotate((float)-90);
            matrix.postTranslate((float)n4 / 2.0f, (float)n3 / 2.0f);
            n = this.mHeight;
            n2 = this.mWidth;
        } else {
            n = this.mWidth;
            n2 = this.mHeight;
        }
        this.setImageMatrix(matrix);
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = n2;
        layoutParams.width = n;
        this.setLayoutParams(layoutParams);
        this.requestLayout();
    }
}

