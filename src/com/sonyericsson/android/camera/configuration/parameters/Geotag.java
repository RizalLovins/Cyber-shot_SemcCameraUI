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

public final class Geotag
extends Enum<Geotag>
implements ParameterValue {
    private static final /* synthetic */ Geotag[] $VALUES;
    public static final /* enum */ Geotag OFF;
    public static final /* enum */ Geotag ON;
    private static final int sParameterTextId = 2131361861;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;

    static {
        ON = new Geotag(-1, 2131361806, true);
        OFF = new Geotag(-1, 2131361807, false);
        Geotag[] arrgeotag = new Geotag[]{ON, OFF};
        $VALUES = arrgeotag;
    }

    private Geotag(int n2, int n3, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mBooleanValue = bl;
    }

    public static Geotag[] getOptions() {
        Geotag[] arrgeotag = new Geotag[]{ON, OFF};
        return arrgeotag;
    }

    public static Geotag valueOf(String string) {
        return (Geotag)Enum.valueOf((Class)Geotag.class, (String)string);
    }

    public static Geotag[] values() {
        return (Geotag[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Geotag)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.GEO_TAG;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361861;
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

    public boolean isGeotagOn() {
        return this.mBooleanValue;
    }
}

