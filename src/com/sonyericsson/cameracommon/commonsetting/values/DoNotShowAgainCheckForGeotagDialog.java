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

import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;

public final class DoNotShowAgainCheckForGeotagDialog
extends Enum<DoNotShowAgainCheckForGeotagDialog>
implements CommonSettingValue {
    private static final /* synthetic */ DoNotShowAgainCheckForGeotagDialog[] $VALUES;
    public static final /* enum */ DoNotShowAgainCheckForGeotagDialog CHECKED = new DoNotShowAgainCheckForGeotagDialog("checked");
    public static final /* enum */ DoNotShowAgainCheckForGeotagDialog NOT_CHECKED = new DoNotShowAgainCheckForGeotagDialog("not_checked");
    private final String mProviderValue;

    static {
        DoNotShowAgainCheckForGeotagDialog[] arrdoNotShowAgainCheckForGeotagDialog = new DoNotShowAgainCheckForGeotagDialog[]{CHECKED, NOT_CHECKED};
        $VALUES = arrdoNotShowAgainCheckForGeotagDialog;
    }

    private DoNotShowAgainCheckForGeotagDialog(String string2) {
        super(string, n);
        this.mProviderValue = string2;
    }

    public static DoNotShowAgainCheckForGeotagDialog valueOf(String string) {
        return (DoNotShowAgainCheckForGeotagDialog)Enum.valueOf((Class)DoNotShowAgainCheckForGeotagDialog.class, (String)string);
    }

    public static DoNotShowAgainCheckForGeotagDialog[] values() {
        return (DoNotShowAgainCheckForGeotagDialog[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.DO_NOT_SHOW_AGAIN_CHECK_FOR_GEOTAG_DIALOG;
    }

    @Override
    public int getIconId() {
        return -1;
    }

    public int getParameterKeyTextId() {
        return -1;
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
        return -1;
    }
}

