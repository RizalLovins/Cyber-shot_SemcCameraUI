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
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;

public final class SmileCapture
extends Enum<SmileCapture>
implements ParameterValue {
    private static final /* synthetic */ SmileCapture[] $VALUES;
    public static final /* enum */ SmileCapture HIGH = new SmileCapture(-1, 2131361826, 2130837736, 70, 2131296381, true);
    public static final /* enum */ SmileCapture LOW;
    public static final /* enum */ SmileCapture MIDDLE;
    public static final /* enum */ SmileCapture OFF;
    private static final int sParameterTextId = 2131361978;
    private final int mDimenId;
    private final int mIconId;
    private final boolean mIsSmileCaptureOn;
    private final int mNotificationIconId;
    private final int mScoreThreshold;
    private final int mTextId;
    private final String mValue;

    static {
        MIDDLE = new SmileCapture(-1, 2131361827, 2130837738, 55, 2131296382, true);
        LOW = new SmileCapture(-1, 2131361828, 2130837737, 40, 2131296383, true);
        OFF = new SmileCapture(-1, 2131361807, -1, 999, -1, false);
        SmileCapture[] arrsmileCapture = new SmileCapture[]{HIGH, MIDDLE, LOW, OFF};
        $VALUES = arrsmileCapture;
    }

    private SmileCapture(int n2, int n3, int n4, int n5, int n6, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mNotificationIconId = n4;
        this.mScoreThreshold = n5;
        this.mDimenId = n6;
        this.mIsSmileCaptureOn = bl;
        if (bl) {
            this.mValue = "on";
            return;
        }
        this.mValue = "off";
    }

    public static SmileCapture[] getOptions(CapturingMode capturingMode) {
        if (com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).SMILE_DETECTION.get().booleanValue()) {
            return SmileCapture.values();
        }
        return new SmileCapture[0];
    }

    public static SmileCapture valueOf(String string) {
        return (SmileCapture)Enum.valueOf((Class)SmileCapture.class, (String)string);
    }

    public static SmileCapture[] values() {
        return (SmileCapture[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((SmileCapture)this);
    }

    public int getDimenId() {
        return this.mDimenId;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getIntValue() {
        return this.mScoreThreshold;
    }

    public int getNotificationIconId() {
        return this.mNotificationIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SMILE_CAPTURE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361978;
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
        return this.mValue;
    }

    public boolean isSmileCaptureOn() {
        return this.mIsSmileCaptureOn;
    }
}

