/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;
import com.sonyericsson.cameracommon.status.CameraStatusValue;

public abstract class IntegerValue
implements CameraStatusValue {
    protected final int mValue;

    public IntegerValue(int n) {
        this.mValue = n;
    }

    @Override
    public String getValueForDebug() {
        return String.valueOf((int)this.mValue);
    }

    @Override
    public void putInto(ContentValues contentValues, String string) {
        contentValues.put(string + this.getKey(), Integer.valueOf((int)this.mValue));
    }
}

