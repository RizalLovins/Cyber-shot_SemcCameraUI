/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import java.util.ArrayList;

public final class FastCapture
extends Enum<FastCapture>
implements ParameterValue {
    private static final /* synthetic */ FastCapture[] $VALUES;
    public static final /* enum */ FastCapture LAUNCH_AND_CAPTURE = new FastCapture(-1, 2131361981, 1, true, "photo-launch-and-capture");
    public static final /* enum */ FastCapture LAUNCH_AND_RECORDING;
    public static final /* enum */ FastCapture LAUNCH_ONLY;
    public static final /* enum */ FastCapture OFF;
    public static final /* enum */ FastCapture VIDEO_LAUNCH_ONLY;
    private static final int sParameterTextId = 2131361866;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final String mSettingsSecureValue;
    private final int mTextId;
    private final int mType;
    private String mValue;

    static {
        LAUNCH_ONLY = new FastCapture(-1, 2131361918, 1, true, "photo-launch-only");
        LAUNCH_AND_RECORDING = new FastCapture(-1, 2131361982, 2, true, "video-launch-and-recording");
        VIDEO_LAUNCH_ONLY = new FastCapture(-1, 2131361983, 2, true, "video-launch-only");
        OFF = new FastCapture(-1, 2131361807, 0, false, "off");
        FastCapture[] arrfastCapture = new FastCapture[]{LAUNCH_AND_CAPTURE, LAUNCH_ONLY, LAUNCH_AND_RECORDING, VIDEO_LAUNCH_ONLY, OFF};
        $VALUES = arrfastCapture;
    }

    private FastCapture(int n2, int n3, int n4, boolean bl, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mType = n4;
        this.mBooleanValue = bl;
        this.mSettingsSecureValue = string2;
    }

    public static FastCapture getDefault() {
        return LAUNCH_ONLY;
    }

    public static FastCapture[] getOptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)LAUNCH_ONLY);
        arrayList.add((Object)LAUNCH_AND_CAPTURE);
        arrayList.add((Object)LAUNCH_AND_RECORDING);
        arrayList.add((Object)OFF);
        return (FastCapture[])arrayList.toArray((Object[])new FastCapture[0]);
    }

    public static FastCapture valueOf(String string) {
        return (FastCapture)Enum.valueOf((Class)FastCapture.class, (String)string);
    }

    public static FastCapture[] values() {
        return (FastCapture[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((FastCapture)this);
    }

    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    public int getCameraType() {
        return this.mType;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.FAST_CAPTURE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361866;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public int getParameterkeyTitleTextId() {
        return 2131361929;
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
    }

    public String getSettingsSecureValue() {
        return this.mSettingsSecureValue;
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

