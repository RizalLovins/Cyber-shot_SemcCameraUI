/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.sonyericsson.android.camera.configuration;

import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPreferencesReader {
    static final String TAG = "SharedPreferencesReader";
    private SharedPreferences mPreferences;

    public SharedPreferencesReader(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }

    public boolean readBoolean(String string, boolean bl) {
        if (this.mPreferences == null) {
            return bl;
        }
        return this.mPreferences.getBoolean(string, bl);
    }

    public String readString(String string, String string2) {
        if (this.mPreferences == null) {
            return string2;
        }
        return this.mPreferences.getString(string, string2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Map<String, String> readStringMap(List<?> list, String string) {
        HashMap hashMap = new HashMap();
        if (this.mPreferences == null) return hashMap;
        for (Object object : list) {
            String string2 = string + object.toString();
            String string3 = this.mPreferences.getString(string2, null);
            if (string3 == null) continue;
            hashMap.put((Object)object.toString(), (Object)string3);
        }
        return hashMap;
    }
}

