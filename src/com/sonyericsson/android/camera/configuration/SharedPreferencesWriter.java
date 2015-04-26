/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.sonyericsson.android.camera.configuration;

import android.content.SharedPreferences;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesWriter {
    static final String TAG = "SharedPreferencesWriter";
    private SharedPreferences.Editor mPreferencesEditor;

    public SharedPreferencesWriter(SharedPreferences sharedPreferences) {
        if (sharedPreferences != null) {
            this.mPreferencesEditor = sharedPreferences.edit();
        }
    }

    public void apply() {
        if (this.mPreferencesEditor == null) {
            return;
        }
        this.mPreferencesEditor.apply();
    }

    public void writeBoolean(String string, boolean bl) {
        if (this.mPreferencesEditor == null) {
            return;
        }
        this.mPreferencesEditor.putBoolean(string, bl);
    }

    public void writeString(String string, String string2) {
        if (this.mPreferencesEditor == null) {
            return;
        }
        this.mPreferencesEditor.putString(string, string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeString(Map<String, String> map, String string) {
        if (this.mPreferencesEditor != null) {
            for (Map.Entry entry : map.entrySet()) {
                this.writeString(string + (String)entry.getKey(), (String)entry.getValue());
            }
        }
    }
}

