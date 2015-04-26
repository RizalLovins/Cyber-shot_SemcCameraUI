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
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;

public final class VideoSmileCapture
extends Enum<VideoSmileCapture>
implements ParameterValue {
    private static final /* synthetic */ VideoSmileCapture[] $VALUES;
    public static final /* enum */ VideoSmileCapture HIGH = new VideoSmileCapture(SmileCapture.HIGH, 2130837742);
    public static final /* enum */ VideoSmileCapture LOW;
    public static final /* enum */ VideoSmileCapture MIDDLE;
    public static final /* enum */ VideoSmileCapture OFF;
    private static final int sParameterTextId = 2131362033;
    private final int mNotificationIconId;
    private final SmileCapture mSmile;
    private final String mValue;

    static {
        MIDDLE = new VideoSmileCapture(SmileCapture.MIDDLE, 2130837744);
        LOW = new VideoSmileCapture(SmileCapture.LOW, 2130837743);
        OFF = new VideoSmileCapture(SmileCapture.OFF, -1);
        VideoSmileCapture[] arrvideoSmileCapture = new VideoSmileCapture[]{HIGH, MIDDLE, LOW, OFF};
        $VALUES = arrvideoSmileCapture;
    }

    /*
     * Enabled aggressive block sorting
     */
    private VideoSmileCapture(SmileCapture smileCapture, int n2) {
        super(string, n);
        this.mSmile = smileCapture;
        this.mValue = this.mSmile.isSmileCaptureOn() ? "on" : "off";
        this.mNotificationIconId = n2;
    }

    public static VideoSmileCapture[] getOptions(boolean bl, CapturingMode capturingMode) {
        if (bl) {
            VideoSmileCapture[] arrvideoSmileCapture = new VideoSmileCapture[]{OFF};
            return arrvideoSmileCapture;
        }
        if (com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).SMILE_DETECTION.get().booleanValue()) {
            return VideoSmileCapture.values();
        }
        return new VideoSmileCapture[0];
    }

    public static VideoSmileCapture valueOf(String string) {
        return (VideoSmileCapture)Enum.valueOf((Class)VideoSmileCapture.class, (String)string);
    }

    public static VideoSmileCapture[] values() {
        return (VideoSmileCapture[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VideoSmileCapture)this);
    }

    public int getDimenId() {
        return this.mSmile.getDimenId();
    }

    @Override
    public int getIconId() {
        return this.mSmile.getIconId();
    }

    public int getIntValue() {
        return this.mSmile.getIntValue();
    }

    public int getNotificationIconId() {
        return this.mNotificationIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VIDEO_SMILE_CAPTURE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362033;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
    }

    public SmileCapture getSmileCapture(boolean bl) {
        if (bl) {
            return SmileCapture.OFF;
        }
        return this.mSmile;
    }

    @Override
    public int getTextId() {
        return this.mSmile.getTextId();
    }

    @Override
    public String getValue() {
        return this.mValue;
    }

    public boolean isSmileCaptureOn() {
        return this.mSmile.isSmileCaptureOn();
    }
}

