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

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;

public final class FaceIdentify
extends Enum<FaceIdentify>
implements ParameterValue {
    private static final /* synthetic */ FaceIdentify[] $VALUES;
    public static final /* enum */ FaceIdentify OFF;
    public static final /* enum */ FaceIdentify ON;
    private static final int sParameterTextId = 2131362041;
    private final int mIconId;
    private final boolean mIsFaceIdentifyOn;
    private final int mTextId;

    static {
        ON = new FaceIdentify(-1, 2131361806, true);
        OFF = new FaceIdentify(-1, 2131361807, false);
        FaceIdentify[] arrfaceIdentify = new FaceIdentify[]{ON, OFF};
        $VALUES = arrfaceIdentify;
    }

    private FaceIdentify(int n2, int n3, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mIsFaceIdentifyOn = bl;
    }

    public static FaceIdentify[] getOptions(ActionMode actionMode) {
        if (actionMode.mCameraId == 0 && Configurations.isFaceIdentificationEnabled()) {
            return FaceIdentify.values();
        }
        return new FaceIdentify[0];
    }

    public static FaceIdentify valueOf(String string) {
        return (FaceIdentify)Enum.valueOf((Class)FaceIdentify.class, (String)string);
    }

    public static FaceIdentify[] values() {
        return (FaceIdentify[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((FaceIdentify)this);
    }

    public Boolean getBooleanValue() {
        return this.mIsFaceIdentifyOn;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.FACE_IDENTIFY;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362041;
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

