/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.settings;

import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.commonsetting.values.AutoReview;
import com.sonyericsson.cameracommon.commonsetting.values.VideoAutoReview;
import com.sonyericsson.cameracommon.settings.AutoReviewSettingValue;
import com.sonyericsson.cameracommon.settings.SettingKey;

public final class AutoReviewSettingKey
extends Enum<AutoReviewSettingKey>
implements SettingKey {
    private static final /* synthetic */ AutoReviewSettingKey[] $VALUES;
    public static final /* enum */ AutoReviewSettingKey AUTO_REVIEW = new AutoReviewSettingKey(R.string.cam_strings_preview_duration_title_txt, AutoReview.OFF, AutoReview.values());
    public static final /* enum */ AutoReviewSettingKey VIDEO_AUTO_REVIEW = new AutoReviewSettingKey(R.string.cam_strings_preview_duration_title_txt, VideoAutoReview.OFF, VideoAutoReview.values());
    private final AutoReviewSettingValue mDefault;
    private int mTitleTextId;
    private AutoReviewSettingValue[] mValues;

    static {
        AutoReviewSettingKey[] arrautoReviewSettingKey = new AutoReviewSettingKey[]{AUTO_REVIEW, VIDEO_AUTO_REVIEW};
        $VALUES = arrautoReviewSettingKey;
    }

    private AutoReviewSettingKey(int n2, AutoReviewSettingValue autoReviewSettingValue, AutoReviewSettingValue[] arrautoReviewSettingValue) {
        super(string, n);
        this.mTitleTextId = n2;
        this.mDefault = autoReviewSettingValue;
        this.mValues = arrautoReviewSettingValue;
    }

    public static AutoReviewSettingKey valueOf(String string) {
        return (AutoReviewSettingKey)Enum.valueOf((Class)AutoReviewSettingKey.class, (String)string);
    }

    public static AutoReviewSettingKey[] values() {
        return (AutoReviewSettingKey[])$VALUES.clone();
    }

    public AutoReviewSettingValue getDefaultValue() {
        return this.mDefault;
    }

    public AutoReviewSettingValue getDefaultValue(AutoReviewSettingKey autoReviewSettingKey) {
        return this.mDefault;
    }

    @Override
    public int getIconId() {
        return 0;
    }

    @Override
    public int getTextId() {
        return 0;
    }

    public int getTitleId() {
        return this.mTitleTextId;
    }

    public AutoReviewSettingValue[] getValues() {
        return (AutoReviewSettingValue[])this.mValues.clone();
    }
}

