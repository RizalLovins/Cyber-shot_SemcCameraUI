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

public final class FastCapture
extends Enum<FastCapture>
implements CommonSettingValue {
    private static final /* synthetic */ FastCapture[] $VALUES;
    public static final /* enum */ FastCapture LAUNCH_AND_CAPTURE = new FastCapture(-1, R.string.cam_strings_fast_capturing_launch_and_capture_photo_txt, 1, true, "photo-launch-and-capture");
    public static final /* enum */ FastCapture LAUNCH_AND_RECORDING;
    public static final /* enum */ FastCapture LAUNCH_ONLY;
    public static final /* enum */ FastCapture OFF;
    public static final /* enum */ FastCapture VIDEO_LAUNCH_ONLY;
    private static final int sParameterTextId;
    private final int mIconId;
    private final boolean mIsFastCaptureOn;
    private final String mSettingsSecureValue;
    private final int mTextId;
    private final int mType;

    static {
        LAUNCH_ONLY = new FastCapture(-1, R.string.cam_strings_fast_capturing_launch_only_txt, 1, true, "photo-launch-only");
        LAUNCH_AND_RECORDING = new FastCapture(-1, R.string.cam_strings_fast_capturing_launch_and_capture_video_txt, 2, true, "video-launch-and-recording");
        VIDEO_LAUNCH_ONLY = new FastCapture(-1, R.string.cam_strings_fast_capturing_launch_only_video_txt, 2, true, "video-launch-only");
        OFF = new FastCapture(-1, R.string.cam_strings_settings_off_txt, 0, false, "off");
        FastCapture[] arrfastCapture = new FastCapture[]{LAUNCH_AND_CAPTURE, LAUNCH_ONLY, LAUNCH_AND_RECORDING, VIDEO_LAUNCH_ONLY, OFF};
        $VALUES = arrfastCapture;
        sParameterTextId = R.string.cam_strings_fast_capturing_txt;
    }

    private FastCapture(int n2, int n3, int n4, boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mType = n4;
        this.mIsFastCaptureOn = bl;
        this.mSettingsSecureValue = string2;
    }

    public static FastCapture valueOf(String string) {
        return (FastCapture)Enum.valueOf((Class)FastCapture.class, (String)string);
    }

    public static FastCapture[] values() {
        return (FastCapture[])$VALUES.clone();
    }

    public int getCameraType() {
        return this.mType;
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.FAST_CAPTURE;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getParameterKeyTextId() {
        return sParameterTextId;
    }

    public String getParameterName() {
        return this.getClass().getName();
    }

    public int getParameterkeyTitleTextId() {
        return R.string.cam_strings_fast_capturing_title_txt;
    }

    @Override
    public String getProviderValue() {
        return this.mSettingsSecureValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public boolean isFastCaptureOn() {
        return this.mIsFastCaptureOn;
    }
}

