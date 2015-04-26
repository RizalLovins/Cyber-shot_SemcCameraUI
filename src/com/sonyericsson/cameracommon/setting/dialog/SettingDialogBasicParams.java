/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialog;

import android.content.Context;
import android.content.res.Resources;
import com.sonyericsson.cameracommon.R;

public final class SettingDialogBasicParams
extends Enum<SettingDialogBasicParams> {
    private static final /* synthetic */ SettingDialogBasicParams[] $VALUES;
    public static final /* enum */ SettingDialogBasicParams SECOND_LAYER_DIALOG_DOUBLE_ITEM_PARAMS;
    public static final /* enum */ SettingDialogBasicParams SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS;
    public static final /* enum */ SettingDialogBasicParams SHORTCUT_DIALOG_PARAMS;
    private final int mBackgroundId;
    private final int mItemHeightResId;
    private final int mPaddingResId;

    static {
        SHORTCUT_DIALOG_PARAMS = new SettingDialogBasicParams(R.dimen.shortcut_dialog_item_height, R.dimen.shortcut_dialog_padding, R.drawable.cam_shortcut_dialog_background_icn);
        SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS = new SettingDialogBasicParams(R.dimen.second_layer_dialog_item_double_line_height, R.dimen.second_layer_dialog_padding, R.drawable.cam_setting_sub_dialog_background_icn);
        SECOND_LAYER_DIALOG_DOUBLE_ITEM_PARAMS = new SettingDialogBasicParams(R.dimen.second_layer_dialog_item_double_line_height, R.dimen.second_layer_dialog_padding, R.drawable.cam_setting_sub_dialog_background_icn);
        SettingDialogBasicParams[] arrsettingDialogBasicParams = new SettingDialogBasicParams[]{SHORTCUT_DIALOG_PARAMS, SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS, SECOND_LAYER_DIALOG_DOUBLE_ITEM_PARAMS};
        $VALUES = arrsettingDialogBasicParams;
    }

    private SettingDialogBasicParams(int n2, int n3, int n4) {
        super(string, n);
        this.mItemHeightResId = n2;
        this.mPaddingResId = n3;
        this.mBackgroundId = n4;
    }

    public static SettingDialogBasicParams valueOf(String string) {
        return (SettingDialogBasicParams)Enum.valueOf((Class)SettingDialogBasicParams.class, (String)string);
    }

    public static SettingDialogBasicParams[] values() {
        return (SettingDialogBasicParams[])$VALUES.clone();
    }

    public int getBackgroundId() {
        return this.mBackgroundId;
    }

    public int getItemHeight(Context context) {
        return context.getResources().getDimensionPixelSize(this.mItemHeightResId);
    }

    public int getPadding(Context context) {
        return context.getResources().getDimensionPixelSize(this.mPaddingResId);
    }
}

