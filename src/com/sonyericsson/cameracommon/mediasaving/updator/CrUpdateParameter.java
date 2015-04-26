/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.mediasaving.updator;

import android.content.ContentValues;

public final class CrUpdateParameter {
    public String[] selectionArgs = null;
    public ContentValues values = null;
    public String where = null;

    public void clear() {
        this.values = null;
        this.where = null;
        this.selectionArgs = null;
    }
}

