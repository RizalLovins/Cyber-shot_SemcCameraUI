/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view.settings.executor;

import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.view.settings.SettingController;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

class ParameterChanger
implements SettingChangerInterface<ParameterValue> {
    private final ParameterManager mParameterManager;
    private final SettingController mSettingController;

    public ParameterChanger(ParameterManager parameterManager, SettingController settingController) {
        this.mParameterManager = parameterManager;
        this.mSettingController = settingController;
    }

    @Override
    public void changeValue(TypedSettingItem<ParameterValue> typedSettingItem) {
        this.mParameterManager.set(typedSettingItem.getData());
        this.mSettingController.updateSettingMenu(false);
    }
}

