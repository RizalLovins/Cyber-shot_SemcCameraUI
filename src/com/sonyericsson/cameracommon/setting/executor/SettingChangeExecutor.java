/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.executor;

import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

public class SettingChangeExecutor<T>
implements SettingExecutorInterface<T> {
    private final SettingChangerInterface<T> mSettingChanger;

    public SettingChangeExecutor(SettingChangerInterface<T> settingChangerInterface) {
        this.mSettingChanger = settingChangerInterface;
    }

    @Override
    public void onExecute(TypedSettingItem<T> typedSettingItem) {
        this.mSettingChanger.changeValue(typedSettingItem);
    }
}

