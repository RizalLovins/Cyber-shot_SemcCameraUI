/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collections
 *  java.util.List
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.util.Collections;
import java.util.List;

public class RectListCapabilityItem
extends CapabilityItem<List<Rect>> {
    RectListCapabilityItem(String string, SharedPreferences sharedPreferences) {
        super(string, sharedPreferences);
    }

    RectListCapabilityItem(String string, Camera.Parameters parameters) {
        super(string, parameters);
    }

    RectListCapabilityItem(String string, List<Rect> list) {
        super(string, list);
    }

    @Override
    List<Rect> getDefaultValue() {
        return Collections.emptyList();
    }

    @Override
    public List<Rect> read(SharedPreferences sharedPreferences, String string) {
        if (sharedPreferences.contains(string)) {
            return SharedPrefsTranslator.getRectList(sharedPreferences.getString(string, ""));
        }
        return Collections.emptyList();
    }

    @Override
    public void write(SharedPreferences.Editor editor) {
        List list = (List)this.get();
        if (list != null) {
            editor.putString(this.getName(), SharedPrefsTranslator.fromRectList(list));
        }
    }
}

