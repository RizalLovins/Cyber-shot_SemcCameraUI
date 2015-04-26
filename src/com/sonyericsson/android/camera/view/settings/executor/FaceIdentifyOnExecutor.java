/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.sonyericsson.android.camera.view.settings.executor;

import android.app.Activity;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.cameracommon.launcher.ApplicationLauncher;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

class FaceIdentifyOnExecutor
extends SettingChangeExecutor<ParameterValue> {
    private ExtendedActivity mActivity;

    public FaceIdentifyOnExecutor(ExtendedActivity extendedActivity, SettingChangerInterface<ParameterValue> settingChangerInterface) {
        super(settingChangerInterface);
        this.mActivity = extendedActivity;
    }

    @Override
    public void onExecute(TypedSettingItem<ParameterValue> typedSettingItem) {
        super.onExecute(typedSettingItem);
        ApplicationLauncher.launchPhotoAnalyzer((Activity)this.mActivity);
    }
}

