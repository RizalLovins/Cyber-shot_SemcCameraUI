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
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;

public final class Facing
extends Enum<Facing>
implements ParameterValue {
    private static final /* synthetic */ Facing[] $VALUES;
    public static final /* enum */ Facing BACK = new Facing(2130837600, 2131362027, 0);
    public static final /* enum */ Facing FRONT = new Facing(2130837600, 2131362028, 1);
    private static Facing[] sOptions;
    private static final int sParameterTextId = 2131361997;
    private final int mIconId;
    private final int mTextId;
    private final int mValue;

    static {
        Facing[] arrfacing = new Facing[]{BACK, FRONT};
        $VALUES = arrfacing;
        if (HardwareCapability.isFrontCameraSupported()) {
            Facing[] arrfacing2 = new Facing[]{BACK, FRONT};
            sOptions = arrfacing2;
            return;
        }
        Facing[] arrfacing3 = new Facing[]{BACK};
        sOptions = arrfacing3;
    }

    private Facing(int n2, int n3, int n4) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = n4;
    }

    public static Facing[] getOptions() {
        return (Facing[])sOptions.clone();
    }

    public static Facing valueOf(String string) {
        return (Facing)Enum.valueOf((Class)Facing.class, (String)string);
    }

    public static Facing[] values() {
        return (Facing[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Facing)this);
    }

    public int getCameraId() {
        return this.mValue;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.FACING;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361997;
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
        return null;
    }
}

