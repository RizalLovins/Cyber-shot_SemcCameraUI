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
import com.sonyericsson.android.camera.configuration.parameters.Resolution;

public final class CaptureFrameShape
extends Enum<CaptureFrameShape>
implements ParameterValue {
    private static final /* synthetic */ CaptureFrameShape[] $VALUES;
    public static final /* enum */ CaptureFrameShape STANDARD;
    public static final /* enum */ CaptureFrameShape WIDE;
    private static final int sParameterTextId = 2131362087;
    private final int mAspectRatio_x100;
    private final int mIconId;
    private final int mTextId;

    static {
        WIDE = new CaptureFrameShape(-1, 2131362238, 177);
        STANDARD = new CaptureFrameShape(-1, 2131362239, 133);
        CaptureFrameShape[] arrcaptureFrameShape = new CaptureFrameShape[]{WIDE, STANDARD};
        $VALUES = arrcaptureFrameShape;
    }

    private CaptureFrameShape(int n2, int n3, int n4) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mAspectRatio_x100 = n4;
    }

    public static CaptureFrameShape getDefaultValue(CapturingMode capturingMode) {
        return CaptureFrameShape.values()[0];
    }

    public static CaptureFrameShape[] getOptions(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return CaptureFrameShape.values();
        }
        return new CaptureFrameShape[0];
    }

    public static CaptureFrameShape valueOf(String string) {
        return (CaptureFrameShape)Enum.valueOf((Class)CaptureFrameShape.class, (String)string);
    }

    public static CaptureFrameShape[] values() {
        return (CaptureFrameShape[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((CaptureFrameShape)this);
    }

    public int getAspectRatio_x100() {
        return this.mAspectRatio_x100;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.CAPTURE_FRAME_SHAPE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362087;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
    }

    public Resolution getResolution(int n) {
        return Resolution.getResolutionFromFrameShape((CaptureFrameShape)this, n);
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

