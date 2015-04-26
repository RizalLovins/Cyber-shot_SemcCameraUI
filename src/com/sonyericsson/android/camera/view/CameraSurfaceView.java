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
public class CameraSurfaceView
extends SurfaceView {
    private static final String TAG = "CameraSurfaceView";

    public CameraSurfaceView(Context context) {
        super(context);
        super.initialize();
    }

    public CameraSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.initialize();
    }

    public CameraSurfaceView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        super.initialize();
    }

    private void initialize() {
        this.setFocusable(true);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        if (bl) {
            // empty if block
        }
    }
}

