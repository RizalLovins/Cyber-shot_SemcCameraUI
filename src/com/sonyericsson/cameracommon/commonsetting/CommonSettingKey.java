/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting;

import android.content.Context;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.values.AutoUpload;
import com.sonyericsson.cameracommon.commonsetting.values.BalloonTipsCounter;
import com.sonyericsson.cameracommon.commonsetting.values.DoNotShowAgainCheckForGeotagDialog;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.commonsetting.values.ShutterSound;
import com.sonyericsson.cameracommon.commonsetting.values.TouchCapture;
import com.sonyericsson.cameracommon.commonsetting.values.VolumeKey;
import com.sonyericsson.cameracommon.settings.SettingKey;
import com.sonyericsson.cameracommon.utility.ResourceUtil;

public final class CommonSettingKey
extends Enum<CommonSettingKey>
implements SettingKey {
    private static final /* synthetic */ CommonSettingKey[] $VALUES;
    public static final /* enum */ CommonSettingKey AUTO_UPLOAD = new CommonSettingKey(R.string.cam_strings_auto_upload_all_title_txt, "auto_upload", AutoUpload.values());
    public static final /* enum */ CommonSettingKey BALLOON_TIPS_COUNTER;
    public static final /* enum */ CommonSettingKey DO_NOT_SHOW_AGAIN_CHECK_FOR_GEOTAG_DIALOG;
    public static final /* enum */ CommonSettingKey FAST_CAPTURE;
    public static final /* enum */ CommonSettingKey GEO_TAG;
    public static final /* enum */ CommonSettingKey SAVE_DESTINATION;
    public static final /* enum */ CommonSettingKey SHUTTER_SOUND;
    public static final /* enum */ CommonSettingKey TERM_OF_USE;
    public static final /* enum */ CommonSettingKey TOUCH_BLOCK;
    public static final /* enum */ CommonSettingKey TOUCH_CAPTURE;
    public static final /* enum */ CommonSettingKey VOLUME_KEY;
    private String mKey;
    private int mTitleTextId;
    private CommonSettingValue[] mValues;

    static {
        GEO_TAG = new CommonSettingKey(R.string.cam_strings_geotagging_title_txt, "geo_tag", Geotag.values());
        int n = R.string.cam_strings_fast_capturing_title_txt;
        CommonSettingValue[] arrcommonSettingValue = new FastCapture[]{FastCapture.LAUNCH_ONLY, FastCapture.LAUNCH_AND_CAPTURE, FastCapture.LAUNCH_AND_RECORDING, FastCapture.OFF};
        FAST_CAPTURE = new CommonSettingKey(n, "fast-capture", arrcommonSettingValue);
        TOUCH_CAPTURE = new CommonSettingKey(R.string.cam_strings_touch_capturing_title_txt, "touch_capture", TouchCapture.values());
        SHUTTER_SOUND = new CommonSettingKey(R.string.cam_strings_camera_sound_txt, "shutter_sound", ShutterSound.values());
        SAVE_DESTINATION = new CommonSettingKey(R.string.cam_strings_save_destination_title_txt, "storage", SaveDestination.values());
        BALLOON_TIPS_COUNTER = new CommonSettingKey(R.string.cam_strings_balloon_tips_modeselector_title_txt, "balloon_tips_for_mode_selector", BalloonTipsCounter.values());
        VOLUME_KEY = new CommonSettingKey(R.string.cam_strings_volumekey_txt, "volume_key", VolumeKey.values());
        TERM_OF_USE = new CommonSettingKey(R.string.cam_strings_term_of_use_title_txt, "term_of_use", new CommonSettingValue[0]);
        TOUCH_BLOCK = new CommonSettingKey(-1, "touch_block", new CommonSettingValue[0]);
        DO_NOT_SHOW_AGAIN_CHECK_FOR_GEOTAG_DIALOG = new CommonSettingKey(-1, "do_not_show_again_check_for_geotag_dialog_value", DoNotShowAgainCheckForGeotagDialog.values());
        CommonSettingKey[] arrcommonSettingKey = new CommonSettingKey[]{AUTO_UPLOAD, GEO_TAG, FAST_CAPTURE, TOUCH_CAPTURE, SHUTTER_SOUND, SAVE_DESTINATION, BALLOON_TIPS_COUNTER, VOLUME_KEY, TERM_OF_USE, TOUCH_BLOCK, DO_NOT_SHOW_AGAIN_CHECK_FOR_GEOTAG_DIALOG};
        $VALUES = arrcommonSettingKey;
    }

    private CommonSettingKey(int n2, String string2, CommonSettingValue[] arrcommonSettingValue) {
        super(string, n);
        this.mTitleTextId = n2;
        this.mKey = string2;
        this.mValues = arrcommonSettingValue;
    }

    public static CommonSettingKey fromKey(String string) {
        for (CommonSettingKey commonSettingKey : CommonSettingKey.values()) {
            if (!commonSettingKey.mKey.equals((Object)string)) continue;
            return commonSettingKey;
        }
        return null;
    }

    public static CommonSettingValue fromValue(CommonSettingKey commonSettingKey, String string) {
        for (CommonSettingValue commonSettingValue : commonSettingKey.getValues()) {
            if (!commonSettingValue.toString().toUpperCase().equals((Object)string.toUpperCase())) continue;
            return commonSettingValue;
        }
        return null;
    }

    public static String getValueFromProviderString(String string, CommonSettingKey commonSettingKey) {
        for (CommonSettingValue commonSettingValue : commonSettingKey.getValues()) {
            if (!string.equals((Object)commonSettingValue.getProviderValue())) continue;
            return commonSettingValue.toString();
        }
        return null;
    }

    public static CommonSettingKey valueOf(String string) {
        return (CommonSettingKey)Enum.valueOf((Class)CommonSettingKey.class, (String)string);
    }

    public static CommonSettingKey[] values() {
        return (CommonSettingKey[])$VALUES.clone();
    }

    @Override
    public int getIconId() {
        return 0;
    }

    public String getKey() {
        return this.mKey;
    }

    @Override
    public int getTextId() {
        return 0;
    }

    public String getTitle(Context context) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$commonsetting$CommonSettingKey[this.ordinal()]) {
            default: {
                return null;
            }
            case 1: 
        }
        return ResourceUtil.getApplicationLabel(context, "com.sonymobile.touchblocker");
    }

    public int getTitleId() {
        return this.mTitleTextId;
    }

    public CommonSettingValue[] getValues() {
        return (CommonSettingValue[])this.mValues.clone();
    }

}

