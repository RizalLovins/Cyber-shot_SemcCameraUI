/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.executor;

import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

public interface SettingExecutorInterface<T> {
    public void onExecute(TypedSettingItem<T> var1);
}

