/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.SurfaceView
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/*
 * Failed to analyse overrides
 */
public class FocusArea
extends SurfaceView {
    private static final String Tag = "FocusArea";

    public FocusArea(Context context) {
        super(context);
        super.initialize();
    }

    public FocusArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.initialize();
    }

    public FocusArea(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        super.initialize();
    }

    private void initialize() {
        this.setFocusable(true);
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
    }
}

