/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class ParamSharedPref {
    private SharedPreferences.Editor mEditor = null;
    private SharedPreferences mPref = null;

    ParamSharedPref(Context context, String string, String string2) {
        try {
            this.mPref = context.getSharedPreferences(string, 0);
            this.mEditor = this.mPref.edit();
            return;
        }
        catch (Exception var4_4) {
            return;
        }
    }

    public void clear() {
        if (this.mEditor != null) {
            this.mEditor.clear().commit();
        }
    }

    public float getParamFromSP(String string, float f) {
        if (this.mPref != null) {
            f = this.mPref.getFloat(string, f);
        }
        return f;
    }

    public int getParamFromSP(String string, int n) {
        if (this.mPref != null) {
            return this.mPref.getInt(string, n);
        }
        return 0;
    }

    public String getParamFromSP(String string, String string2) {
        if (this.mPref != null) {
            string2 = this.mPref.getString(string, string2);
        }
        return string2;
    }

    public boolean getParamFromSP(String string, boolean bl) {
        if (this.mPref != null) {
            bl = this.mPref.getBoolean(string, bl);
        }
        return bl;
    }

    public void setParamToSP(String string, float f) {
        if (this.mEditor != null) {
            this.mEditor.putFloat(string, f);
            this.mEditor.apply();
        }
    }

    public void setParamToSP(String string, int n) {
        if (this.mEditor != null) {
            this.mEditor.putInt(string, n);
            this.mEditor.apply();
        }
    }

    public void setParamToSP(String string, String string2) {
        if (this.mEditor != null) {
            this.mEditor.putString(string, string2);
            this.mEditor.apply();
        }
    }

    public void setParamToSP(String string, boolean bl) {
        if (this.mEditor != null) {
            this.mEditor.putBoolean(string, bl);
            this.mEditor.apply();
        }
    }
}

