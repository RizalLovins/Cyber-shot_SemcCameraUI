/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.sonyericsson.android.camera.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.SharedPreferencesReader;
import com.sonyericsson.android.camera.configuration.SharedPreferencesWriter;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SharedPreferencesUtil {
    static final String TAG = SharedPreferencesUtil.class.getSimpleName();
    private Map<String, Map<String, String>> mMaps = new HashMap();
    private SharedPreferences mPreferences;
    private List<String> mPrefixList = new ArrayList();
    private SharedPreferencesReader mReader;
    private SharedPreferencesWriter mWriter;

    public SharedPreferencesUtil(Context context) {
        this.mPreferences = SharedPreferencesUtil.getSharedPreferences(context, "com.sonyericsson.android.camera.shared_preferences", 0);
        this.mWriter = new SharedPreferencesWriter(this.mPreferences);
        this.mReader = new SharedPreferencesReader(this.mPreferences);
    }

    private void apply(boolean bl) {
        if (bl) {
            this.apply();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String createPrefix(ParameterCategory parameterCategory, CapturingMode capturingMode, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parameterCategory.toString()).append('_');
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterCategory[parameterCategory.ordinal()]) {
            default: {
                break;
            }
            case 2: {
                stringBuilder.append((Object)capturingMode).append('_');
            }
        }
        if (!(string == null || string.equals((Object)""))) {
            stringBuilder.append(string).append('_');
        }
        stringBuilder.append("PARAMS_");
        return stringBuilder.toString();
    }

    public static String getCommonPrefix() {
        return SharedPreferencesUtil.createPrefix(ParameterCategory.COMMON, CapturingMode.UNKNOWN, "");
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String getPrefix(ParameterKey parameterKey, int n, int n2) {
        ParameterCategory parameterCategory = parameterKey.getCategory();
        CapturingMode capturingMode = CapturingMode.UNKNOWN;
        if (parameterCategory == ParameterCategory.COMMON) return SharedPreferencesUtil.createPrefix(parameterCategory, capturingMode, "");
        if (n2 != 1) {
            capturingMode = CapturingMode.SCENE_RECOGNITION;
            return SharedPreferencesUtil.createPrefix(parameterCategory, capturingMode, "");
        }
        if (HardwareCapability.getInstance().isSceneRecognitionSupported(1)) {
            capturingMode = CapturingMode.SUPERIOR_FRONT;
            return SharedPreferencesUtil.createPrefix(parameterCategory, capturingMode, "");
        }
        capturingMode = CapturingMode.FRONT_PHOTO;
        return SharedPreferencesUtil.createPrefix(parameterCategory, capturingMode, "");
    }

    public static SharedPreferences getSharedPreferences(Context context, String string, int n) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit((Callable)new GetSharedPreferencesTask(context, string, n));
        try {
            SharedPreferences sharedPreferences = (SharedPreferences)future.get(3000, TimeUnit.MILLISECONDS);
            return sharedPreferences;
        }
        catch (InterruptedException var13_6) {
            CameraLogger.w(TAG, "Get shared prefs has been interrupted.", (Throwable)var13_6);
            return null;
        }
        catch (ExecutionException var10_7) {
            CameraLogger.w(TAG, "Get shared prefs failed.", (Throwable)var10_7);
            return null;
        }
        catch (TimeoutException var7_8) {
            CameraLogger.w(TAG, "Get shared prefs failed.", (Throwable)var7_8);
            return null;
        }
        finally {
            future.cancel(true);
            executorService.shutdown();
        }
    }

    public void apply() {
        this.mWriter.apply();
    }

    public void clearSharedPreferences() {
        if (this.mPreferences != null) {
            this.mPreferences.edit().clear().commit();
        }
    }

    public SharedPreferences getSharedPreferences() {
        return this.mPreferences;
    }

    public Map<String, String> getStringMap(String string) {
        if (this.mMaps.containsKey((Object)string)) {
            return (Map)this.mMaps.get((Object)string);
        }
        return Collections.emptyMap();
    }

    public boolean readBoolean(String string, boolean bl) {
        return this.mReader.readBoolean(string, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readParameters(List<ParameterKey> list) {
        if (this.mPreferences != null) {
            for (String string : this.mPrefixList) {
                Map<String, String> map = this.mReader.readStringMap(list, string);
                this.mMaps.put((Object)string, map);
            }
        }
    }

    public String readString(String string, String string2) {
        return this.mReader.readString(string, string2);
    }

    public void registerKey(String string) {
        if (!this.mPrefixList.contains((Object)string)) {
            this.mPrefixList.add((Object)string);
        }
    }

    public void setStringMap(String string, Map<String, String> map) {
        this.mMaps.put((Object)string, map);
    }

    public void writeBoolean(String string, boolean bl, boolean bl2) {
        this.mWriter.writeBoolean(string, bl);
        super.apply(bl2);
    }

    public void writeParameters(boolean bl) {
        for (String string : this.mMaps.keySet()) {
            this.mWriter.writeString((Map)this.mMaps.get((Object)string), string);
        }
        super.apply(bl);
    }

    public void writeString(String string, String string2, boolean bl) {
        this.mWriter.writeString(string, string2);
        super.apply(bl);
    }

    private static class GetSharedPreferencesTask
    implements Callable<SharedPreferences> {
        private final Context mContext;
        private final String mFileName;
        private final int mMode;

        public GetSharedPreferencesTask(Context context, String string, int n) {
            this.mContext = context;
            this.mFileName = string;
            this.mMode = n;
        }

        public SharedPreferences call() throws Exception {
            return this.mContext.getSharedPreferences(this.mFileName, this.mMode);
        }
    }

}

