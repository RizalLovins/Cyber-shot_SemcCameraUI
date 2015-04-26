/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.setting.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.setting.controller.SettingLayoutCoordinatorFactory;
import com.sonyericsson.cameracommon.setting.dialog.SettingControlDialog;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasic;
import com.sonyericsson.cameracommon.setting.dialog.SettingDialogBasicParams;
import com.sonyericsson.cameracommon.setting.dialog.SettingTabDialogBasic;
import com.sonyericsson.cameracommon.setting.layoutcoordinator.LayoutCoordinator;

class SettingDialogFactory {
    SettingDialogFactory() {
    }

    public static SettingControlDialog createControl(Context context, SettingLayoutCoordinatorFactory.LayoutCoordinateData layoutCoordinateData) {
        SettingControlDialog settingControlDialog = (SettingControlDialog)SettingDialogFactory.inflate(context, R.layout.setting_dialog_control);
        settingControlDialog.setLayoutCoordinator(SettingLayoutCoordinatorFactory.createControlLayoutCoordinator(settingControlDialog, layoutCoordinateData));
        return settingControlDialog;
    }

    public static SettingTabDialogBasic createMenu(Context context, SettingLayoutCoordinatorFactory.LayoutCoordinateData layoutCoordinateData, int n, int n2) {
        SettingTabDialogBasic settingTabDialogBasic = (SettingTabDialogBasic)SettingDialogFactory.inflate(context, R.layout.setting_dialog_menu);
        settingTabDialogBasic.setNumberOfTabs(n2);
        settingTabDialogBasic.setLayoutCoordinator(SettingLayoutCoordinatorFactory.createMenuLayoutCoordinator(settingTabDialogBasic, layoutCoordinateData, n));
        return settingTabDialogBasic;
    }

    public static SettingDialogBasic createSecondLayerDialog(Context context, SettingLayoutCoordinatorFactory.LayoutCoordinateData layoutCoordinateData, int n, int n2) {
        SettingDialogBasic settingDialogBasic = (SettingDialogBasic)SettingDialogFactory.inflate(context, R.layout.setting_dialog_basic);
        settingDialogBasic.setSettingDialogParams(SettingDialogBasicParams.SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS);
        settingDialogBasic.setLayoutCoordinator(SettingLayoutCoordinatorFactory.createSecondLayerLayoutCoordinator(settingDialogBasic, layoutCoordinateData, n, n2));
        return settingDialogBasic;
    }

    public static SettingDialogBasic createShortcutDialog(Context context, SettingLayoutCoordinatorFactory.LayoutCoordinateData layoutCoordinateData, int n) {
        SettingDialogBasic settingDialogBasic = (SettingDialogBasic)SettingDialogFactory.inflate(context, R.layout.setting_dialog_basic);
        settingDialogBasic.setSettingDialogParams(SettingDialogBasicParams.SHORTCUT_DIALOG_PARAMS);
        settingDialogBasic.setLayoutCoordinator(SettingLayoutCoordinatorFactory.createShortcutLayoutCoordinator(settingDialogBasic, layoutCoordinateData));
        if (n != 0) {
            settingDialogBasic.setTitle(n);
        }
        return settingDialogBasic;
    }

    private static View inflate(Context context, int n) {
        return ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(n, null);
    }
}

