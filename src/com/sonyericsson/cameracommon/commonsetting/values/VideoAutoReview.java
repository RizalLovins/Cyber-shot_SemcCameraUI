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

public final class VideoAutoReview
extends Enum<VideoAutoReview>
implements AutoReviewSettingValue {
    private static final /* synthetic */ VideoAutoReview[] $VALUES;
    public static final /* enum */ VideoAutoReview EDIT;
    public static final /* enum */ VideoAutoReview OFF;
    public static final /* enum */ VideoAutoReview ON;
    private static final int sParameterTextId;
    private final int mIconId;
    private final int mTextId;

    static {
        ON = new VideoAutoReview(-1, R.string.cam_strings_settings_on_txt);
        EDIT = new VideoAutoReview(-1, R.string.cam_strings_preview_edit_txt);
        OFF = new VideoAutoReview(-1, R.string.cam_strings_settings_off_txt);
        VideoAutoReview[] arrvideoAutoReview = new VideoAutoReview[]{ON, EDIT, OFF};
        $VALUES = arrvideoAutoReview;
        sParameterTextId = R.string.cam_strings_preview_duration_txt;
    }

    private VideoAutoReview(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static VideoAutoReview valueOf(String string) {
        return (VideoAutoReview)Enum.valueOf((Class)VideoAutoReview.class, (String)string);
    }

    public static VideoAutoReview[] values() {
        return (VideoAutoReview[])$VALUES.clone();
    }

    @Override
    public AutoReviewSettingKey getAutoReviewSettingKey() {
        return AutoReviewSettingKey.VIDEO_AUTO_REVIEW;
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

