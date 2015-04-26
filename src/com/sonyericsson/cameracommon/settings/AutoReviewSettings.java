/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.sonyericsson.cameracommon.settings;

import com.sonyericsson.cameracommon.settings.AutoReviewSettingKey;
import com.sonyericsson.cameracommon.settings.AutoReviewSettingValue;
import java.util.HashMap;

public class AutoReviewSettings {
    private static final String TAG = AutoReviewSettings.class.getSimpleName();
    private final HashMap<AutoReviewSettingKey, AutoReviewSettingValue> mSettings = new HashMap();

    public AutoReviewSettingValue get(AutoReviewSettingKey autoReviewSettingKey) {
        AutoReviewSettingValue autoReviewSettingValue = (AutoReviewSettingValue)this.mSettings.get((Object)autoReviewSettingKey);
        if (autoReviewSettingValue == null) {
            autoReviewSettingValue = autoReviewSettingKey.getDefaultValue(autoReviewSettingKey);
        }
        return autoReviewSettingValue;
    }

    public void set(AutoReviewSettingValue autoReviewSettingValue) {
        AutoReviewSettingKey autoReviewSettingKey = autoReviewSettingValue.getAutoReviewSettingKey();
        this.mSettings.put((Object)autoReviewSettingKey, (Object)autoReviewSettingValue);
    }
}

