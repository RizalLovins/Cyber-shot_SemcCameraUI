/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view.settings;

import com.sonyericsson.cameracommon.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.setting.settingitem.SettingItemBuilder;

public class SettingUtil {
    public static SettingItem getBlankItem() {
        return SettingItemBuilder.build(new Object()).dialogItemType(0).commit();
    }
}

