/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;

public final class Microphone
extends Enum<Microphone>
implements ParameterValue {
    private static final /* synthetic */ Microphone[] $VALUES;
    public static final /* enum */ Microphone OFF;
    public static final /* enum */ Microphone ON;
    private static final int sParameterTextId = 2131361869;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;

    static {
        ON = new Microphone(-1, 2131361806, true);
        OFF = new Microphone(-1, 2131361807, false);
        Microphone[] arrmicrophone = new Microphone[]{ON, OFF};
        $VALUES = arrmicrophone;
    }

    private Microphone(int n2, int n3, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mBooleanValue = bl;
    }

    public static Microphone[] getOptions() {
        return Microphone.values();
    }

    public static Microphone valueOf(String string) {
        return (Microphone)Enum.valueOf((Class)Microphone.class, (String)string);
    }

    public static Microphone[] values() {
        return (Microphone[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Microphone)this);
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.MICROPHONE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361869;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
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

