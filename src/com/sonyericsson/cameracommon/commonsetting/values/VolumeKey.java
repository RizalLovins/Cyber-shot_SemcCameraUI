/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;

public final class VolumeKey
extends Enum<VolumeKey>
implements CommonSettingValue {
    private static final /* synthetic */ VolumeKey[] $VALUES;
    public static final /* enum */ VolumeKey HW_CAMERA_KEY;
    public static final /* enum */ VolumeKey VOLUME;
    public static final /* enum */ VolumeKey ZOOM;
    private final int mIconId;
    private final String mSettingsSecureValue;
    private final int mTextId;

    static {
        ZOOM = new VolumeKey(-1, R.string.cam_strings_volumekey_zoom_txt, "zoom");
        VOLUME = new VolumeKey(-1, R.string.cam_strings_volumekey_volume_txt, "volume");
        HW_CAMERA_KEY = new VolumeKey(-1, R.string.cam_strings_volumekey_shutter_txt, "HW_camera_key");
        VolumeKey[] arrvolumeKey = new VolumeKey[]{ZOOM, VOLUME, HW_CAMERA_KEY};
        $VALUES = arrvolumeKey;
    }

    private VolumeKey(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mSettingsSecureValue = string2;
    }

    public static VolumeKey valueOf(String string) {
        return (VolumeKey)Enum.valueOf((Class)VolumeKey.class, (String)string);
    }

    public static VolumeKey[] values() {
        return (VolumeKey[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.VOLUME_KEY;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public String getProviderValue() {
        return this.mSettingsSecureValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }
}

