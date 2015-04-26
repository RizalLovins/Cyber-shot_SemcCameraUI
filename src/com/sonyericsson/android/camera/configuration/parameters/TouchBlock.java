/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration.parameters;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.cameracommon.utility.CommonUtility;

public final class TouchBlock
extends Enum<TouchBlock>
implements ParameterValue {
    private static final /* synthetic */ TouchBlock[] $VALUES;
    public static final /* enum */ TouchBlock OFF;
    public static final /* enum */ TouchBlock ON;
    private static final int sParameterTextId = -1;
    private final int mIconId;
    private final int mTextId;

    static {
        ON = new TouchBlock(-1, -1);
        OFF = new TouchBlock(-1, -1);
        TouchBlock[] arrtouchBlock = new TouchBlock[]{ON, OFF};
        $VALUES = arrtouchBlock;
    }

    private TouchBlock(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static TouchBlock getDefaultValue() {
        return OFF;
    }

    public static TouchBlock[] getOptions(Context context) {
        if (CommonUtility.isPackageExist("com.sonymobile.touchblocker", context)) {
            return TouchBlock.values();
        }
        return new TouchBlock[0];
    }

    public static TouchBlock valueOf(String string) {
        return (TouchBlock)Enum.valueOf((Class)TouchBlock.class, (String)string);
    }

    public static TouchBlock[] values() {
        return (TouchBlock[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((TouchBlock)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.TOUCH_BLOCK;
    }

    @Override
    public int getParameterKeyTextId() {
        return -1;
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

