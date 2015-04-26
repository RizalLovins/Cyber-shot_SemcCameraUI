/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.settings.AutoReviewSettingKey;
import com.sonyericsson.cameracommon.settings.AutoReviewSettingValue;

public final class AutoReview
extends Enum<AutoReview>
implements AutoReviewSettingValue {
    private static final /* synthetic */ AutoReview[] $VALUES;
    public static final /* enum */ AutoReview EDIT;
    public static final /* enum */ AutoReview LONG;
    public static final /* enum */ AutoReview OFF;
    public static final /* enum */ AutoReview SHORT;
    public static final /* enum */ AutoReview UNLIMITED;
    private static final int sParameterTextId;
    private final int mDuration;
    private final int mIconId;
    private final int mTextId;

    static {
        UNLIMITED = new AutoReview(-1, R.string.cam_strings_preview_duration_unlimited_txt, -1);
        LONG = new AutoReview(-1, R.string.cam_strings_preview_duration_5sec_txt, 5000);
        SHORT = new AutoReview(-1, R.string.cam_strings_preview_duration_3sec_txt, 3000);
        EDIT = new AutoReview(-1, R.string.cam_strings_preview_edit_txt, 0);
        OFF = new AutoReview(-1, R.string.cam_strings_settings_off_txt, 0);
        AutoReview[] arrautoReview = new AutoReview[]{UNLIMITED, LONG, SHORT, EDIT, OFF};
        $VALUES = arrautoReview;
        sParameterTextId = R.string.cam_strings_preview_duration_txt;
    }

    private AutoReview(int n2, int n3, int n4) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mDuration = n4;
    }

    public static AutoReview valueOf(String string) {
        return (AutoReview)Enum.valueOf((Class)AutoReview.class, (String)string);
    }

    public static AutoReview[] values() {
        return (AutoReview[])$VALUES.clone();
    }

    @Override
    public AutoReviewSettingKey getAutoReviewSettingKey() {
        return AutoReviewSettingKey.AUTO_REVIEW;
    }

    public int getDuration() {
        return this.mDuration;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getParameterKeyTextId() {
        return sParameterTextId;
    }

    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }
}

