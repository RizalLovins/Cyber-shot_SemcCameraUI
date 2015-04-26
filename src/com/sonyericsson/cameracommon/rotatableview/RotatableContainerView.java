/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.FrameLayout
 *  java.lang.Math
 */
package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.sonyericsson.cameracommon.R;

/*
 * Failed to analyse overrides
 */
public class RotatableContainerView
extends FrameLayout {
    private FrameLayout mContainerView;
    private FrameLayout mCustomizableView;
    private int mUiOrientation = 0;

    public RotatableContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private boolean isPortraitUi() {
        if (this.mUiOrientation == 1) {
            return true;
        }
        return false;
    }

    private void setLandscapeUi(int n, int n2, boolean bl) {
        int n3;
        this.mContainerView.getLayoutParams().height = n3 = Math.max((int)n, (int)n2);
        this.mContainerView.getLayoutParams().width = n3;
        this.requestLayout();
        int n4 = this.mCustomizableView.getMeasuredWidth();
        int n5 = this.mCustomizableView.getMeasuredHeight();
        if (n4 != n || n5 != n2) {
            this.mCustomizableView.getLayoutParams().width = n;
            this.mCustomizableView.getLayoutParams().height = n2;
            this.mCustomizableView.requestLayout();
        }
        this.mCustomizableView.setRotation(0.0f);
    }

    private void setPortraitUi(int n, int n2, boolean bl) {
        int n3;
        this.mContainerView.getLayoutParams().height = n3 = Math.max((int)n, (int)n2);
        this.mContainerView.getLayoutParams().width = n3;
        this.requestLayout();
        int n4 = this.mCustomizableView.getMeasuredWidth();
        int n5 = this.mCustomizableView.getMeasuredHeight();
        if (n4 != n2 || n5 != n) {
            this.mCustomizableView.getLayoutParams().width = n2;
            this.mCustomizableView.getLayoutParams().height = n;
            this.mCustomizableView.requestLayout();
        }
        if (bl) {
            this.mCustomizableView.setRotation(90.0f);
            return;
        }
        this.mCustomizableView.setRotation(270.0f);
    }

    public FrameLayout getCustamizableView() {
        return this.mCustomizableView;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContainerView = (FrameLayout)this.findViewById(R.id.container);
        this.mCustomizableView = (FrameLayout)this.findViewById(R.id.overlay);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3 = RotatableContainerView.getDefaultSize((int)this.getSuggestedMinimumWidth(), (int)n);
        int n4 = RotatableContainerView.getDefaultSize((int)this.getSuggestedMinimumHeight(), (int)n2);
        if (this.mUiOrientation == 0) {
            super.setLandscapeUi(n3, n4, false);
        } else if (super.isPortraitUi()) {
            super.setPortraitUi(n3, n4, false);
        } else {
            super.setLandscapeUi(n3, n4, false);
        }
        super.onMeasure(n, n2);
    }

    public void setUiOrientation(int n) {
        if (this.mUiOrientation != n) {
            this.mUiOrientation = n;
            this.requestLayout();
        }
    }
}

