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

public final class AutoReview
extends Enum<AutoReview>
implements ParameterValue {
    private static final /* synthetic */ AutoReview[] $VALUES;
    public static final /* enum */ AutoReview EDIT;
    public static final /* enum */ AutoReview LONG;
    public static final /* enum */ AutoReview OFF;
    public static final /* enum */ AutoReview SHORT;
    public static final /* enum */ AutoReview UNLIMITED;
    private static final int sParameterTextId = 2131361999;
    private final int mDuration;
    private final int mIconId;
    private final int mTextId;

    static {
        UNLIMITED = new AutoReview(-1, 2131362003, -1);
        LONG = new AutoReview(-1, 2131362002, 5000);
        SHORT = new AutoReview(-1, 2131362001, 3000);
        EDIT = new AutoReview(-1, 2131362015, 0);
        OFF = new AutoReview(-1, 2131361807, 0);
        AutoReview[] arrautoReview = new AutoReview[]{UNLIMITED, LONG, SHORT, EDIT, OFF};
        $VALUES = arrautoReview;
    }

    private AutoReview(int n2, int n3, int n4) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mDuration = n4;
    }

    public static AutoReview[] getOptions(CapturingMode capturingMode) {
        if (capturingMode.getType() == 1) {
            return AutoReview.values();
        }
        return new AutoReview[0];
    }

    public static AutoReview valueOf(String string) {
        return (AutoReview)Enum.valueOf((Class)AutoReview.class, (String)string);
    }

    public static AutoReview[] values() {
        return (AutoReview[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((AutoReview)this);
    }

    public int getDuration() {
        return this.mDuration;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.AUTO_REVIEW;
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

