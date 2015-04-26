/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.view.settings;

import com.sonyericsson.android.camera.configuration.ParameterKey;

public final class SettingGroup
extends Enum<SettingGroup> {
    private static final /* synthetic */ SettingGroup[] $VALUES;
    public static final /* enum */ SettingGroup COMMON;
    public static final /* enum */ SettingGroup CONTROL;
    public static final /* enum */ SettingGroup FLASH_LIGHT;
    public static final /* enum */ SettingGroup PHOTO;
    public static final /* enum */ SettingGroup SCENE;
    public static final /* enum */ SettingGroup VIDEO;
    private final ParameterKey[] mItems;
    private final int mTextId;

    static {
        ParameterKey[] arrparameterKey = new ParameterKey[]{ParameterKey.RESOLUTION, ParameterKey.CAPTURE_FRAME_SHAPE, ParameterKey.SELF_TIMER, ParameterKey.SMILE_CAPTURE, ParameterKey.FOCUS_MODE, ParameterKey.HDR, ParameterKey.ISO, ParameterKey.METERING, ParameterKey.STABILIZER, ParameterKey.SOFT_SKIN, ParameterKey.AUTO_REVIEW, ParameterKey.BURST_SHOT, ParameterKey.SUPER_RESOLUTION, ParameterKey.FACE_IDENTIFY};
        PHOTO = new SettingGroup(2131362030, arrparameterKey);
        ParameterKey[] arrparameterKey2 = new ParameterKey[]{ParameterKey.VIDEO_SIZE, ParameterKey.VIDEO_SELF_TIMER, ParameterKey.VIDEO_SMILE_CAPTURE, ParameterKey.FOCUS_MODE, ParameterKey.VIDEO_HDR, ParameterKey.METERING, ParameterKey.VIDEO_STABILIZER, ParameterKey.MICROPHONE, ParameterKey.VIDEO_AUTO_REVIEW};
        VIDEO = new SettingGroup(2131362029, arrparameterKey2);
        SCENE = new SettingGroup(2131362267, new ParameterKey[0]);
        ParameterKey[] arrparameterKey3 = new ParameterKey[]{ParameterKey.EV, ParameterKey.WHITE_BALANCE};
        CONTROL = new SettingGroup(2131362271, arrparameterKey3);
        ParameterKey[] arrparameterKey4 = new ParameterKey[]{ParameterKey.FLASH, ParameterKey.PHOTO_LIGHT};
        FLASH_LIGHT = new SettingGroup(2131361921, arrparameterKey4);
        ParameterKey[] arrparameterKey5 = new ParameterKey[]{ParameterKey.FAST_CAPTURE, ParameterKey.GEO_TAG, ParameterKey.AUTO_UPLOAD, ParameterKey.TOUCH_CAPTURE, ParameterKey.VOLUME_KEY, ParameterKey.SHUTTER_SOUND, ParameterKey.DESTINATION_TO_SAVE, ParameterKey.TOUCH_BLOCK};
        COMMON = new SettingGroup(2131362031, arrparameterKey5);
        SettingGroup[] arrsettingGroup = new SettingGroup[]{PHOTO, VIDEO, SCENE, CONTROL, FLASH_LIGHT, COMMON};
        $VALUES = arrsettingGroup;
    }

    private SettingGroup(int n2, ParameterKey[] arrparameterKey) {
        super(string, n);
        this.mTextId = n2;
        this.mItems = arrparameterKey;
    }

    public static SettingGroup getGroup(String string) {
        for (SettingGroup settingGroup : SettingGroup.values()) {
            if (!settingGroup.toString().equals((Object)string)) continue;
            return settingGroup;
        }
        return null;
    }

    public static SettingGroup valueOf(String string) {
        return (SettingGroup)Enum.valueOf((Class)SettingGroup.class, (String)string);
    }

    public static SettingGroup[] values() {
        return (SettingGroup[])$VALUES.clone();
    }

    public ParameterKey[] getSettingItemList() {
        return (ParameterKey[])this.mItems.clone();
    }

    public int getTextId() {
        return this.mTextId;
    }

    public ParameterKey[] valueOf(SettingGroup settingGroup) {
        return this.getSettingItemList();
    }
}

