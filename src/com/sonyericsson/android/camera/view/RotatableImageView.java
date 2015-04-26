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
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sonyericsson.android.camera.ExtendedActivity;

/*
 * Failed to analyse overrides
 */
public class RotatableImageView
extends ImageView {
    private static final String TAG = "RotatableImageView";
    private int mFixRotation = 0;
    private int mHeight = 0;
    private boolean mPrepared = false;
    private int mSensorOrientation = 0;
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
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void update() {
        if (!this.isPrepared()) {
            return;
        }
        var1_1 = this.getWidth();
        var2_2 = this.getHeight();
        var3_3 = new Matrix();
        var4_4 = this.mSensorOrientation;
        var5_5 = ((ExtendedActivity)this.getContext()).getOrientation();
        var6_6 = this.mFixRotation != 0 ? this.mFixRotation : (var4_4 == 1 && var5_5 != 1 ? 1 : 2);
        ** if (this.getDrawable() == null) goto lbl-1000
lbl10: // 1 sources:
        var3_3.preScale((float)this.mWidth / (float)this.getDrawable().getIntrinsicWidth(), (float)this.mHeight / (float)this.getDrawable().getIntrinsicHeight());
        if (var6_6 == 1) lbl-1000: // 2 sources:
        {
            var3_3.postTranslate((- (float)var1_1) / 2.0f, (- (float)var2_2) / 2.0f);
            var3_3.postRotate((float)-90);
            var3_3.postTranslate((float)var2_2 / 2.0f, (float)var1_1 / 2.0f);
            var7_7 = this.mHeight;
            var8_8 = this.mWidth;
        } else {
            var7_7 = this.mWidth;
            var8_8 = this.mHeight;
        }
        this.setImageMatrix(var3_3);
        var9_9 = this.getLayoutParams();
        var9_9.height = var8_8;
        var9_9.width = var7_7;
        this.setLayoutParams(var9_9);
        this.requestLayout();
    }
}

