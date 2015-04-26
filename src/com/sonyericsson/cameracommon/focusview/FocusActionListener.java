/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.focusview;

import android.graphics.Point;

public interface FocusActionListener {
    public void onCanceled();

    public void onFaceSelected(Point var1);

    public void onLongPressed();

    public void onReleased();

    public void onTouched();
}

