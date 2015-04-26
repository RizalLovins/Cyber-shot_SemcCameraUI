/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;

public final class AutoUpload
extends Enum<AutoUpload>
implements CommonSettingValue {
    private static final /* synthetic */ AutoUpload[] $VALUES;
    public static final /* enum */ AutoUpload OFF;
    public static final /* enum */ AutoUpload ON;
    private static final int sParameterTextId;
    private final int mIconId;
    private final boolean mIsAutoUploadOn;
    private final String mProviderValue;
    private final int mTextId;

    static {
        ON = new AutoUpload(-1, R.string.cam_strings_settings_on_txt, true, "on");
        OFF = new AutoUpload(-1, R.string.cam_strings_settings_off_txt, false, "off");
        AutoUpload[] arrautoUpload = new AutoUpload[]{ON, OFF};
        $VALUES = arrautoUpload;
        sParameterTextId = R.string.cam_strings_auto_upload_all_txt;
    }

    private AutoUpload(int n2, int n3, Boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mIsAutoUploadOn = bl;
        this.mProviderValue = string2;
    }

    public static AutoUpload valueOf(String string) {
        return (AutoUpload)Enum.valueOf((Class)AutoUpload.class, (String)string);
    }

    public static AutoUpload[] values() {
        return (AutoUpload[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.AUTO_UPLOAD;
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

    @Override
    public String getProviderValue() {
        return this.mProviderValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public boolean isAutoUploadOn() {
        return this.mIsAutoUploadOn;
    }
}

