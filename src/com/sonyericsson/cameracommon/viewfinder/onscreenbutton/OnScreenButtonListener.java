/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.view.MotionEvent
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.viewfinder.onscreenbutton;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.sonyericsson.cameracommon.viewfinder.onscreenbutton.OnScreenButton;

public interface OnScreenButtonListener {
    public void onCancel(OnScreenButton var1, MotionEvent var2);

    public void onDispatchDraw(OnScreenButton var1, Canvas var2);

    public void onDown(OnScreenButton var1, MotionEvent var2);

    public void onMove(OnScreenButton var1, MotionEvent var2);

    public void onUp(OnScreenButton var1, MotionEvent var2);
}

