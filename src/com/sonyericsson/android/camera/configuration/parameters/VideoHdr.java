/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import java.util.ArrayList;
import java.util.List;

public final class VideoHdr
extends Enum<VideoHdr>
implements ParameterValue {
    private static final /* synthetic */ VideoHdr[] $VALUES;
    public static final /* enum */ VideoHdr OFF;
    public static final /* enum */ VideoHdr ON;
    private static final int sParameterTextId = 2131362010;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        ON = new VideoHdr(-1, 2131361806, "on");
        OFF = new VideoHdr(-1, 2131361807, "off");
        VideoHdr[] arrvideoHdr = new VideoHdr[]{ON, OFF};
        $VALUES = arrvideoHdr;
    }

    private VideoHdr(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    public static VideoHdr[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (!com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).VIDEO_HDR.get().isEmpty()) {
            arrayList.add((Object)ON);
        }
        arrayList.add((Object)OFF);
        return (VideoHdr[])arrayList.toArray((Object[])new VideoHdr[0]);
    }

    public static boolean isSupported(CapturingMode capturingMode) {
        if (com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).VIDEO_HDR.get().isEmpty()) {
            return false;
        }
        return true;
    }

    public static VideoHdr valueOf(String string) {
        return (VideoHdr)Enum.valueOf((Class)VideoHdr.class, (String)string);
    }

    public static VideoHdr[] values() {
        return (VideoHdr[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VideoHdr)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VIDEO_HDR;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362010;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public int getParameterkeyTitleTextId() {
        return 2131361959;
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
}

