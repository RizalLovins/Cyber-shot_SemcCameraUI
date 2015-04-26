/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.os.Build;
import com.sonyericsson.cameracommon.utility.ParamSharedPref;

public class ParamSharedPrefWrapper {
    private static final String TAG = ParamSharedPrefWrapper.class.getSimpleName();
    private static final String VERSION_KEY = "SHARED_PREFERNCE_VERSION";
    private final ParamSharedPref mParamSharedPref;
    private final String mSharedPreferenceName;
    private final String mVersion;

    public ParamSharedPrefWrapper(Context context, String string, String string2) {
        this.mParamSharedPref = new ParamSharedPref(context, string, string2);
        this.mSharedPreferenceName = string;
        this.mVersion = string2;
        String string3 = this.getParamFromSP("SHARED_PREFERNCE_VERSION", "");
        if (!this.mVersion.equals((Object)string3)) {
            this.mParamSharedPref.clear();
            this.mParamSharedPref.setParamToSP("SHARED_PREFERNCE_VERSION", this.mVersion);
        }
    }

    public void checkFirmwareVersionUpdated() {
        String string;
        String string2 = this.getParamFromSP("android.os.Build.FINGERPRINT", "");
        if (!string2.equals((Object)(string = Build.FINGERPRINT))) {
            this.clear();
            this.setParamToSP("android.os.Build.FINGERPRINT", string);
        }
    }

    public void clear() {
        this.mParamSharedPref.clear();
        this.mParamSharedPref.setParamToSP("SHARED_PREFERNCE_VERSION", this.mVersion);
    }

    public float getParamFromSP(String string, float f) {
        return this.mParamSharedPref.getParamFromSP(string, f);
    }

    public int getParamFromSP(String string, int n) {
        return this.mParamSharedPref.getParamFromSP(string, n);
    }

    public String getParamFromSP(String string, String string2) {
        return this.mParamSharedPref.getParamFromSP(string, string2);
    }

    public boolean getParamFromSP(String string, boolean bl) {
        return this.mParamSharedPref.getParamFromSP(string, bl);
    }

    public String getSharedPreferenceName() {
        return this.mSharedPreferenceName;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setParamToSP(String string, float f) {
        this.mParamSharedPref.setParamToSP(string, f);
    }

    public void setParamToSP(String string, int n) {
        this.mParamSharedPref.setParamToSP(string, n);
    }

    public void setParamToSP(String string, String string2) {
        this.mParamSharedPref.setParamToSP(string, string2);
    }

    public void setParamToSP(String string, boolean bl) {
        this.mParamSharedPref.setParamToSP(string, bl);
    }
}

