/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.focusview;

import android.graphics.Rect;

public class NamedFace {
    public final Rect mFacePosition;
    public final String mName;
    public final int mSmileScore;
    public final String mUuid;

    public NamedFace(String string, String string2, Rect rect, int n) {
        this.mName = string;
        this.mUuid = string2;
        this.mFacePosition = rect;
        this.mSmileScore = n;
    }
}

