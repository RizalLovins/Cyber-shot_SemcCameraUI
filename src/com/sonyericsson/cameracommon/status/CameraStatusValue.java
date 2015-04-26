/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

public interface CameraStatusValue {
    public String getKey();

    public String getValueForDebug();

    public int minRequiredVersion();

    public void putInto(ContentValues var1, String var2);
}

