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

public final class Geotag
extends Enum<Geotag>
implements CommonSettingValue {
    private static final /* synthetic */ Geotag[] $VALUES;
    public static final /* enum */ Geotag OFF;
    public static final /* enum */ Geotag ON;
    private static final int sParameterTextId;
    private final int mIconId;
    private final boolean mIsGeotagOn;
    private final String mProviderValue;
    private final int mTextId;

    static {
        ON = new Geotag(-1, R.string.cam_strings_settings_on_txt, true, "on");
        OFF = new Geotag(-1, R.string.cam_strings_settings_off_txt, false, "off");
        Geotag[] arrgeotag = new Geotag[]{ON, OFF};
        $VALUES = arrgeotag;
        sParameterTextId = R.string.cam_strings_geotagging_txt;
    }

    private Geotag(int n2, int n3, boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mIsGeotagOn = bl;
        this.mProviderValue = string2;
    }

    public static Geotag valueOf(String string) {
        return (Geotag)Enum.valueOf((Class)Geotag.class, (String)string);
    }

    public static Geotag[] values() {
        return (Geotag[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.GEO_TAG;
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

    public boolean isGeotagOn() {
        return this.mIsGeotagOn;
    }
}

