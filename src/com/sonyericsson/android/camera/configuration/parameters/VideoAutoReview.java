/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;

public final class VideoAutoReview
extends Enum<VideoAutoReview>
implements ParameterValue {
    private static final /* synthetic */ VideoAutoReview[] $VALUES;
    public static final /* enum */ VideoAutoReview EDIT;
    public static final /* enum */ VideoAutoReview OFF;
    public static final /* enum */ VideoAutoReview ON;
    private static final int sParameterTextId = 2131361999;
    private final int mIconId;
    private final int mTextId;

    static {
        ON = new VideoAutoReview(-1, 2131361806);
        EDIT = new VideoAutoReview(-1, 2131362015);
        OFF = new VideoAutoReview(-1, 2131361807);
        VideoAutoReview[] arrvideoAutoReview = new VideoAutoReview[]{ON, EDIT, OFF};
        $VALUES = arrvideoAutoReview;
    }

    private VideoAutoReview(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static VideoAutoReview[] getOptions(CapturingMode capturingMode) {
        if (capturingMode.getType() == 1 || capturingMode.getType() == 2) {
            return VideoAutoReview.values();
        }
        return new VideoAutoReview[0];
    }

    public static VideoAutoReview valueOf(String string) {
        return (VideoAutoReview)Enum.valueOf((Class)VideoAutoReview.class, (String)string);
    }

    public static VideoAutoReview[] values() {
        return (VideoAutoReview[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VideoAutoReview)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VIDEO_AUTO_REVIEW;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361999;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return OFF;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    @Override
    public String getValue() {
        return this.toString();
    }
}

