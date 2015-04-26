/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.ViewGroup
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.dialogitem;

import android.content.Context;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingButton;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingCategoryButton;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingCategorySwitch;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingEvSlider;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingIcon;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingIconList;
import com.sonyericsson.cameracommon.setting.dialogitem.SettingValueButton;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;

public class SettingDialogItemFactory {
    public static final int BUTTON = 1;
    public static final int CATEGORY_BUTTON = 3;
    public static final int CATEGORY_SWITCH = 4;
    public static final int END_OF_TYPE_LIST = 8;
    public static final int EV = 5;
    public static final int ICON = 0;
    public static final int MENU = 6;
    public static final int VALUE_BUTTON = 2;
    public static final int VERTICAL_ICON_LIST = 7;

    public SettingDialogItem create(SettingItem settingItem, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (settingItem.getDialogItemType() == 1) {
            return new SettingButton(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 0) {
            return new SettingIcon(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 2) {
            return new SettingValueButton(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 3) {
            return new SettingCategoryButton(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 4) {
            return new SettingCategorySwitch(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 5) {
            return new SettingEvSlider(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 7) {
            return new SettingIconList(context, settingItem);
        }
        if (settingItem.getDialogItemType() == 6) {
            return new SettingIcon(context, settingItem);
        }
        throw new IllegalArgumentException("The specified type is unknown. type:" + settingItem.getDialogItemType());
    }

    public int getDialogItemTypeCount() {
        return 8;
    }
}

