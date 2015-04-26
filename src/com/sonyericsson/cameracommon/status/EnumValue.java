/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;
import com.sonyericsson.cameracommon.status.CameraStatusValue;

public abstract class EnumValue<T extends Enum<T>>
implements CameraStatusValue {
    private final String mValueString;

    public EnumValue(T t) {
        this.mValueString = t.toString();
    }

    @Override
    public String getValueForDebug() {
        return this.mValueString;
    }

    @Override
    public void putInto(ContentValues contentValues, String string) {
        contentValues.put(string + this.getKey(), this.mValueString);
    }
}

