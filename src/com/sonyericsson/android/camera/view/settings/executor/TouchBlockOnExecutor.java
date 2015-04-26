/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view.settings.executor;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.view.settings.SettingController;
import com.sonyericsson.cameracommon.launcher.ApplicationLauncher;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

class TouchBlockOnExecutor
implements SettingExecutorInterface<ParameterKey> {
    private final Context mContext;
    private final SettingController mSettingController;

    public TouchBlockOnExecutor(Context context, SettingController settingController) {
        this.mContext = context;
        this.mSettingController = settingController;
    }

    @Override
    public void onExecute(TypedSettingItem<ParameterKey> typedSettingItem) {
        ApplicationLauncher.startCameraTouchBlock(this.mContext);
        this.mSettingController.getSettingDialogStack().closeCurrentDialog();
    }
}

