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

public final class TouchCapture
extends Enum<TouchCapture>
implements ParameterValue {
    private static final /* synthetic */ TouchCapture[] $VALUES;
    public static final /* enum */ TouchCapture OFF;
    public static final /* enum */ TouchCapture VIEW_FINDER;
    private static final int sParameterTextId = 2131362005;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mNotificationId;
    private final int mTextId;

    static {
        VIEW_FINDER = new TouchCapture(-1, 2131361806, -1, true);
        OFF = new TouchCapture(-1, 2131361807, -1, false);
        TouchCapture[] arrtouchCapture = new TouchCapture[]{VIEW_FINDER, OFF};
        $VALUES = arrtouchCapture;
    }

    private TouchCapture(int n2, int n3, int n4, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mNotificationId = n4;
        this.mBooleanValue = bl;
    }

    public static TouchCapture[] getOptions() {
        return TouchCapture.values();
    }

    public static TouchCapture valueOf(String string) {
        return (TouchCapture)Enum.valueOf((Class)TouchCapture.class, (String)string);
    }

    public static TouchCapture[] values() {
        return (TouchCapture[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((TouchCapture)this);
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getNotificationIconId() {
        return this.mNotificationId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.TOUCH_CAPTURE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362005;
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

