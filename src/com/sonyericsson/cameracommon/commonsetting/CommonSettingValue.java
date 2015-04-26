/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting;

import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.settings.SettingValue;

public interface CommonSettingValue
extends SettingValue {
    public CommonSettingKey getCommonSettingKey();

    public String getProviderValue();
}

