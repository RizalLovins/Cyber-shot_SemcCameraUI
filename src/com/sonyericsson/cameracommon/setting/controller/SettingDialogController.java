/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.controller;

import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;

public interface SettingDialogController {
    public void closeCurrentDialog();

    public void closeDialogs(boolean var1);

    public void openSecondLayerDialog(SettingAdapter var1, Object var2);
}

