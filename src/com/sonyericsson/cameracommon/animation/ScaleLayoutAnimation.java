/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.animation.Animation
 *  android.view.animation.Transformation
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/*
 * Failed to analyse overrides
 */
public class ScaleLayoutAnimation
extends Animation {
    private int mDiffHeight;
    private int mDiffWidth;
    private int mFromHeight;
    private int mFromWidth;
    private int mOriginalHeight;
    private int mOriginalWidth;
    private int mToHeight;
    private int mToWidth;
    View mView;

    private ScaleLayoutAnimation() {
    }

    /* synthetic */ ScaleLayoutAnimation( var1) {
    }

    private void prepareSize() {
        this.mView.getLayoutParams().width = this.mFromWidth;
        this.mView.getLayoutParams().height = this.mFromHeight;
        this.mView.requestLayout();
    }

    protected void applyTransformation(float f, Transformation transformation) {
        int n = (int)((float)this.mFromWidth - f * (float)this.mDiffWidth);
        int n2 = (int)((float)this.mFromHeight - f * (float)this.mDiffHeight);
        this.mView.getLayoutParams().width = n;
        this.mView.getLayoutParams().height = n2;
        this.mView.requestLayout();
    }

    public void initialize(int n, int n2, int n3, int n4) {
        super.initialize(n, n2, n3, n4);
    }

    public void recycle() {
        this.prepareSize();
    }

    public void resetSize() {
        this.mView.getLayoutParams().width = this.mOriginalWidth;
        this.mView.getLayoutParams().height = this.mOriginalHeight;
        this.mView.requestLayout();
    }

    public boolean willChangeBounds() {
        return true;
    }

    public static class Builder {
        private ScaleLayoutAnimation mParentInstance = new ScaleLayoutAnimation(null);

        public Builder(View view) {
            this.mParentInstance.mView = view;
            this.mParentInstance.mOriginalWidth = view.getWidth();
            this.mParentInstance.mOriginalHeight = view.getHeight();
        }

        public ScaleLayoutAnimation create() {
            this.mParentInstance.mDiffWidth = this.mParentInstance.mFromWidth - this.mParentInstance.mToWidth;
            this.mParentInstance.mDiffHeight = this.mParentInstance.mFromHeight - this.mParentInstance.mToHeight;
            this.mParentInstance.mView.getLayoutParams().width = this.mParentInstance.mFromWidth;
            this.mParentInstance.mView.getLayoutParams().height = this.mParentInstance.mFromHeight;
            this.mParentInstance.mView.requestLayout();
            return this.mParentInstance;
        }

        public Builder setDuration(int n) {
            this.mParentInstance.setDuration((long)n);
            return this;
        }

        public Builder setFromSize(int n, int n2) {
            this.mParentInstance.mFromWidth = n;
            this.mParentInstance.mFromHeight = n2;
            return this;
        }

        public Builder setToSize(int n, int n2) {
            this.mParentInstance.mToWidth = n;
            this.mParentInstance.mToHeight = n2;
            return this;
        }
    }

}

