/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import com.sonyericsson.cameracommon.activity.BaseActivity;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.commonsetting.values.BalloonTipsCounter;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.commonsetting.values.ShutterSound;
import com.sonyericsson.cameracommon.mediasaving.location.LocationSettingsReader;

public class PresetConfigurationResolver {
    public static final String Af_SUCCESS_FILE_PATH = "/system/media/audio/camera/common/af_success.m4a";
    public static final String KEY_SHUTTER_SOUND = "COMMON_PARAMS_SHUTTER_SOUND";
    public static final String RECORD_SOUND_FILE_PATH_ON = "/system/media/audio/ui/VideoRecord.ogg";
    public static final String SHUTTER_SOUND_FILE_PATH_OFF = "/system/media/audio/camera/sound0/no_sound.m4a";
    public static final String SHUTTER_SOUND_FILE_PATH_ON = "/system/media/audio/ui/camera_click.ogg";
    public static final String TAG = PresetConfigurationResolver.class.getSimpleName();
    public static final String VALUE_SHUTTER_SOUND_OFF = "OFF";
    public static final String VALUE_SHUTTER_SOUND_ON = "SOUND1";

    public static String getRecordSoundFilePath(boolean bl) {
        if (bl) {
            return "/system/media/audio/ui/VideoRecord.ogg";
        }
        return "/system/media/audio/camera/sound0/no_sound.m4a";
    }

    public static String getShutterSoundFilePath(Context context) {
        return PresetConfigurationResolver.getShutterSoundFilePath(PresetConfigurationResolver.isShutterSoundEnabled((BaseActivity)context));
    }

    public static String getShutterSoundFilePath(boolean bl) {
        if (bl) {
            return "/system/media/audio/ui/camera_click.ogg";
        }
        return "/system/media/audio/camera/sound0/no_sound.m4a";
    }

    public static boolean isBalloonTipsEnabled(BaseActivity baseActivity) {
        return PresetConfigurationResolver.isBalloonTipsEnabled(baseActivity.getCommonSettings());
    }

    public static boolean isBalloonTipsEnabled(CommonSettings commonSettings) {
        CommonSettingValue commonSettingValue = commonSettings.get(CommonSettingKey.BALLOON_TIPS_COUNTER);
        if (commonSettingValue == BalloonTipsCounter.DISPLAY_OK || commonSettingValue == BalloonTipsCounter.DISPLAY_OK_FIRST || commonSettingValue == BalloonTipsCounter.DISPLAY_OK_SECOND) {
            return true;
        }
        return false;
    }

    public static boolean isGeoTagEnabled(CommonSettingValue commonSettingValue, Context context) {
        if (commonSettingValue == Geotag.ON) {
            boolean bl = LocationSettingsReader.isLocationProviderAllowed(context, "gps");
            boolean bl2 = LocationSettingsReader.isLocationProviderAllowed(context, "network");
            if (bl || bl2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isShutterSoundEnabled(BaseActivity baseActivity) {
        if (baseActivity.getCommonSettings().get(CommonSettingKey.SHUTTER_SOUND) == ShutterSound.OFF) {
            return false;
        }
        return true;
    }
}

