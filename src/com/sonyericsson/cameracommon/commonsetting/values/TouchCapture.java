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

public final class TouchCapture
extends Enum<TouchCapture>
implements CommonSettingValue {
    private static final /* synthetic */ TouchCapture[] $VALUES;
    public static final /* enum */ TouchCapture OFF;
    public static final /* enum */ TouchCapture ON;
    private static final int sParameterTextId;
    private final int mIconId;
    private final boolean mIsTouchCaptureOn;
    private final String mProviderValue;
    private final int mTextId;

    static {
        ON = new TouchCapture(-1, R.string.cam_strings_settings_on_txt, true, "on");
        OFF = new TouchCapture(-1, R.string.cam_strings_settings_off_txt, false, "off");
        TouchCapture[] arrtouchCapture = new TouchCapture[]{ON, OFF};
        $VALUES = arrtouchCapture;
        sParameterTextId = R.string.cam_strings_touch_capturing_title_txt;
    }

    private TouchCapture(int n2, int n3, boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mIsTouchCaptureOn = bl;
        this.mProviderValue = string2;
    }

    public static TouchCapture valueOf(String string) {
        return (TouchCapture)Enum.valueOf((Class)TouchCapture.class, (String)string);
    }

    public static TouchCapture[] values() {
        return (TouchCapture[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.TOUCH_CAPTURE;
    }

    public int getCommonSettingKeyTextId() {
        return sParameterTextId;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public String getProviderValue() {
        return this.mProviderValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public boolean isTouchCaptureOn() {
        return this.mIsTouchCaptureOn;
    }
}

