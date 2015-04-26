/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collections
 *  java.util.List
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.SharedPreferences;
import android.hardware.Camera;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.ExtensionValueParser;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.util.Collections;
import java.util.List;

public class StringListCapabilityItem
extends CapabilityItem<List<String>> {
    StringListCapabilityItem(String string, SharedPreferences sharedPreferences) {
        super(string, sharedPreferences);
    }

    StringListCapabilityItem(String string, Camera.Parameters parameters) {
        super(string, parameters);
    }

    StringListCapabilityItem(String string, List<String> list) {
        super(string, list);
    }

    @Override
    List<String> getDefaultValue() {
        return Collections.emptyList();
    }

    @Override
    public List<String> parseExtensionValue(String string) {
        return ExtensionValueParser.getStringList(string);
    }

    @Override
    public List<String> read(SharedPreferences sharedPreferences, String string) {
        if (sharedPreferences.contains(string)) {
            return SharedPrefsTranslator.getStringList(sharedPreferences.getString(string, ""));
        }
        return Collections.emptyList();
    }

    @Override
    public void write(SharedPreferences.Editor editor) {
        List list = (List)this.get();
        if (list != null) {
            editor.putString(this.getName(), SharedPrefsTranslator.fromStringList(list));
        }
    }
}

