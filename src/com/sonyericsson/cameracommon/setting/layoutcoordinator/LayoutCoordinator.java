/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.layoutcoordinator;

import android.graphics.Rect;

public interface LayoutCoordinator {
    public void coordinatePosition(int var1);

    public void coordinateSize(int var1);

    public Rect getDialogRect();
}

