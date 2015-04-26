/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.fastcapturing;

import android.content.SharedPreferences;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.util.SharedPreferencesUtil;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class FastCapturingCameraUtils {
    private static final String TAG = FastCapturingCameraUtils.class.getSimpleName();

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isSettingValueAvailableInSharedPreferences(SharedPreferences sharedPreferences, int n, int n2, ParameterKey parameterKey) {
        CapturingMode capturingMode;
        String string;
        switch (n2) {
            default: {
                CameraLogger.w(TAG, "CameraId[" + n2 + "] is not supported.");
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 0: {
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 1: {
                capturingMode = CapturingMode.convertFrom(sharedPreferences.getString("FRONT_FAST", CapturingMode.FRONT_PHOTO.name()), CapturingMode.FRONT_PHOTO);
            }
        }
        if (sharedPreferences.getString((string = SharedPreferencesUtil.createPrefix(parameterKey.getCategory(), capturingMode, "")) + (Object)parameterKey, null) != null) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static <T extends ParameterValue> T loadParameter(SharedPreferences sharedPreferences, int n, int n2, ParameterKey parameterKey, T t) {
        if (t == null) {
            throw new IllegalArgumentException("Default value cannot be null.");
        }
        ParameterValueHolder<T> parameterValueHolder = FastCapturingCameraUtils.loadParameter(sharedPreferences, n, n2, new ParameterValueHolder<T>(t));
        if (parameterValueHolder == null || FastCapturingCameraUtils.shouldClearOnResume(parameterValueHolder.get())) {
            return t;
        }
        return parameterValueHolder.get();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static <T extends ParameterValue> ParameterValueHolder<T> loadParameter(SharedPreferences sharedPreferences, int n, int n2, ParameterValueHolder<T> parameterValueHolder) {
        CapturingMode capturingMode;
        String string;
        String string2;
        ParameterKey parameterKey = parameterValueHolder.get().getParameterKey();
        switch (n2) {
            default: {
                CameraLogger.w(TAG, "CameraId[" + n2 + "] is not supported.");
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 0: {
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 1: {
                capturingMode = CapturingMode.convertFrom(sharedPreferences.getString("FRONT_FAST", CapturingMode.FRONT_PHOTO.name()), CapturingMode.FRONT_PHOTO);
            }
        }
        if ((string2 = sharedPreferences.getString((string = SharedPreferencesUtil.createPrefix(parameterKey.getCategory(), capturingMode, "")) + (Object)parameterKey, null)) != null) {
            parameterValueHolder.parseValueString(string2);
        }
        return parameterValueHolder;
    }

    public static boolean shouldClearOnResume(ParameterValue parameterValue) {
        if (parameterValue.getParameterKey() == ParameterKey.PHOTO_LIGHT || parameterValue == Flash.LED_ON) {
            return true;
        }
        return false;
    }

    public static <T extends ParameterValue> void storeCommonParameter(SharedPreferences sharedPreferences, ParameterValueHolder<T> parameterValueHolder) {
        String string = SharedPreferencesUtil.createPrefix(ParameterCategory.COMMON, CapturingMode.UNKNOWN, null);
        String string2 = string + (Object)parameterValueHolder.get().getParameterKey();
        String string3 = parameterValueHolder.createValueString();
        sharedPreferences.edit().putString(string2, string3).apply();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static <T extends ParameterValue> void storeParameter(SharedPreferences sharedPreferences, int n, T t) {
        CapturingMode capturingMode;
        ParameterValueHolder<T> parameterValueHolder = new ParameterValueHolder<T>(t);
        parameterValueHolder.set(t);
        switch (n) {
            default: {
                CameraLogger.w(TAG, "CameraId[" + n + "] is not supported.");
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 0: {
                capturingMode = CapturingMode.SCENE_RECOGNITION;
                break;
            }
            case 1: {
                capturingMode = CapturingMode.convertFrom(sharedPreferences.getString("FRONT_FAST", CapturingMode.FRONT_PHOTO.name()), CapturingMode.FRONT_PHOTO);
            }
        }
        String string = SharedPreferencesUtil.createPrefix(t.getParameterKey().getCategory(), capturingMode, "");
        String string2 = string + (Object)t.getParameterKey();
        String string3 = parameterValueHolder.createValueString();
        sharedPreferences.edit().putString(string2, string3).apply();
    }
}

