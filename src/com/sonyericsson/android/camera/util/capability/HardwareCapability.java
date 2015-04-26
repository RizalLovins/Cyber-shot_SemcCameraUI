/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.os.Build
 *  java.lang.Boolean
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 */
package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.device.CameraDeviceException;
import com.sonyericsson.android.camera.device.CameraDeviceUtil;
import com.sonyericsson.android.camera.util.SharedPreferencesUtil;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HardwareCapability {
    private static final String FILE_NAME = "com.sonyericsson.android.camera.supported_values.";
    private static final String TAG = "HardwareCapability";
    private static CameraExtensionVersion sCameraExtensionVersion;
    private static final HardwareCapability sInstance;
    private static final int sNumberOfCameras;
    private CapabilityList[] mList = new CapabilityList[sNumberOfCameras];
    private boolean mNeedToClearSettingsInSharedPref = false;
    private final Map<String, String> mSaveSettingMap = new HashMap();
    private SharedPreferencesUtil mSharedPrefsUtil;

    /*
     * Enabled aggressive block sorting
     */
    static {
        int n = Camera.getNumberOfCameras();
        if (n > 0) {
            sNumberOfCameras = n;
        } else {
            sNumberOfCameras = 0;
            CameraLogger.e("HardwareCapability", "Camera.getNumberOfCameras() return " + n);
        }
        sInstance = new HardwareCapability();
        sCameraExtensionVersion = null;
    }

    private HardwareCapability() {
    }

    public static CapabilityList getCapability(int n) {
        return sInstance.getList(n);
    }

    public static HardwareCapability getInstance() {
        return sInstance;
    }

    private CapabilityList getList(int n) {
        CapabilityList capabilityList;
        if (n < this.mList.length && (capabilityList = this.mList[n]) != null) {
            return capabilityList;
        }
        throw new IllegalArgumentException();
    }

    public static int getNumberOfCameras() {
        return sNumberOfCameras;
    }

    public static boolean isCameraSupported() {
        if (sNumberOfCameras > 0) {
            return true;
        }
        return false;
    }

    public static boolean isFrontCameraSupported() {
        if (sNumberOfCameras > 1) {
            return true;
        }
        return false;
    }

    public static boolean isStillHdrSupportedWith(Resolution resolution) {
        if (resolution != Resolution.TWENTY_MP && resolution != Resolution.FIFTEEN_MP_WIDE) {
            return true;
        }
        return false;
    }

    private void load(Context context, int n) throws CameraDeviceException {
        if (!this.setCapabilityFromSharedPrefs(context, n)) {
            this.setCapabilityFromParamsWithCameraOpen(context, n);
        }
    }

    private void resetSharedPrefs(Context context) {
        new SharedPreferencesUtil(context).clearSharedPreferences();
    }

    private void restoreManualModeSetting() {
        String string = SharedPreferencesUtil.createPrefix(ParameterCategory.CAPTURING_MODE, CapturingMode.NORMAL, "");
        String string2 = (String)this.mSaveSettingMap.get((Object)(string + (Object)ParameterKey.FLASH));
        this.mSharedPrefsUtil.writeString(string + (Object)ParameterKey.FLASH, string2, true);
    }

    private void restoreSettings(int n) {
        String string = SharedPreferencesUtil.getPrefix(ParameterKey.FLASH, 1, n);
        String string2 = (String)this.mSaveSettingMap.get((Object)(string + (Object)ParameterKey.FLASH));
        this.mSharedPrefsUtil.writeString(string + (Object)ParameterKey.FLASH, string2, true);
    }

    private void saveManualModeSetting() {
        String string = SharedPreferencesUtil.createPrefix(ParameterCategory.CAPTURING_MODE, CapturingMode.NORMAL, "");
        String string2 = this.mSharedPrefsUtil.readString(string + (Object)ParameterKey.FLASH, "default");
        this.mSaveSettingMap.put((Object)(string + (Object)ParameterKey.FLASH), (Object)string2);
    }

    private void saveSettings(int n) {
        String string = SharedPreferencesUtil.getPrefix(ParameterKey.FLASH, 1, n);
        String string2 = this.mSharedPrefsUtil.readString(string + (Object)ParameterKey.FLASH, "default");
        this.mSaveSettingMap.put((Object)(string + (Object)ParameterKey.FLASH), (Object)string2);
    }

    public CameraExtensionVersion getCameraExtensionVersion() {
        if (sCameraExtensionVersion == null) {
            sCameraExtensionVersion = new CameraExtensionVersion(this.getList((int)0).EXTENSION_VERSION.get());
        }
        return sCameraExtensionVersion;
    }

    public String getFileName(int n) {
        return "com.sonyericsson.android.camera.supported_values." + String.valueOf((int)n);
    }

    public int getMaxSoftSkinLevel(int n) {
        return super.getList((int)n).MAX_SOFT_SKIN_LEVEL.get();
    }

    public boolean isCameraExtensionSupported(int n) {
        if (super.getList((int)n).EXTENSION_VERSION.get().length() > 0) {
            return true;
        }
        return false;
    }

    public boolean isDetectedFaceIdSupported(int n) {
        return this.getCameraExtensionVersion().isLaterThanOrEqualTo(1, 6);
    }

    public boolean isFullHdVideoFpsSupported(int n, int n2) {
        int n3 = super.getList((int)n).MAX_VIDEO_FRAME.get();
        if (n2 * 1000 <= n3) {
            return true;
        }
        return false;
    }

    public boolean isSceneRecognitionSupported(int n) {
        return super.getList((int)n).SCENE_RECOGNITION.get();
    }

    public boolean isStillHdrVer3(int n) {
        List<String> list = super.getList((int)n).SCENE.get();
        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!((String)iterator.next()).equals((Object)"hdr")) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isVideoHdrSupported(int n, Rect rect) {
        Rect rect2 = super.getList((int)n).MAX_VIDEO_HDR_SIZE.get();
        if (rect2 != null && rect2.width() >= rect.width() && rect2.height() >= rect.height()) {
            return true;
        }
        return false;
    }

    public boolean isVideoMetaDataSupported(int n) {
        return super.getList((int)n).VIDEO_MEATDAT_VALUES.get();
    }

    public boolean isVideoNrSupported(int n) {
        return super.getList((int)n).VIDEO_NR_VALUES.get().contains((Object)"on");
    }

    public void load(Context context) throws CameraDeviceException {
        for (int i = 0; i < sNumberOfCameras; ++i) {
            super.load(context, i);
        }
        if (this.mNeedToClearSettingsInSharedPref) {
            this.mSharedPrefsUtil = new SharedPreferencesUtil(context);
            this.mSaveSettingMap.clear();
            for (int j = 0; j < sNumberOfCameras; ++j) {
                super.saveSettings(j);
            }
            super.saveManualModeSetting();
            super.resetSharedPrefs(context);
            for (int k = 0; k < sNumberOfCameras; ++k) {
                super.restoreSettings(k);
            }
            super.restoreManualModeSetting();
            this.mNeedToClearSettingsInSharedPref = false;
        }
    }

    public void setCameraParameters(Context context, int n, Camera.Parameters parameters) {
        this.setCapabilityFromParams(context, n, parameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    void setCapabilityFromParams(Context context, int n, Camera.Parameters parameters) {
        this.mList[n] = new CapabilityList(context, parameters);
        boolean bl = true;
        CapabilityList capabilityList = this.mList[n];
        if (capabilityList.FPS_RANGE.get().isEmpty()) {
            return;
        }
        if (capabilityList.PREVIEW_SIZE.get().isEmpty()) {
            return;
        }
        if (capabilityList.PICTURE_SIZE.get().isEmpty()) {
            return;
        }
        if (bl) {
            this.store(context, n);
        }
    }

    void setCapabilityFromParamsWithCameraOpen(Context context, int n) throws CameraDeviceException {
        this.setCapabilityFromParams(context, n, CameraDeviceUtil.getParams(context, n));
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean setCapabilityFromSharedPrefs(Context context, int n) {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, this.getFileName(n), 0);
        if (sharedPreferences == null || sharedPreferences.getAll().isEmpty()) {
            return false;
        }
        if (!sharedPreferences.contains("android.os.Build.FINGERPRINT")) {
            sharedPreferences.edit().clear().commit();
            return false;
        }
        if (!sharedPreferences.getString("android.os.Build.FINGERPRINT", "").equals((Object)Build.FINGERPRINT)) {
            sharedPreferences.edit().clear().commit();
            this.mNeedToClearSettingsInSharedPref = true;
            return false;
        }
        this.mList[n] = new CapabilityList(context, sharedPreferences);
        return true;
    }

    boolean store(Context context, int n) {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, this.getFileName(n), 0);
        if (sharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("android.os.Build.FINGERPRINT", Build.FINGERPRINT);
        Iterator iterator = this.mList[n].values().iterator();
        while (iterator.hasNext()) {
            ((CapabilityItem)iterator.next()).write(editor);
        }
        editor.apply();
        return true;
    }
}

