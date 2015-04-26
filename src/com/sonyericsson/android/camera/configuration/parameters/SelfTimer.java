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

import com.sonyericsson.android.camera.ShutterToneGenerator;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;

public final class SelfTimer
extends Enum<SelfTimer>
implements ParameterValue,
SelfTimerInterface {
    private static final /* synthetic */ SelfTimer[] $VALUES;
    public static final /* enum */ SelfTimer INSTANT;
    public static final /* enum */ SelfTimer LONG;
    public static final /* enum */ SelfTimer OFF;
    public static final /* enum */ SelfTimer SHORT;
    private static final int sParameterTextId = 2131361822;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mMilliSeconds;
    private final ShutterToneGenerator.Type mSoundType;
    private final int mTextId;

    static {
        LONG = new SelfTimer(2130837671, 2131361824, true, 10000, ShutterToneGenerator.Type.SOUND_SELFTIMER_10SEC);
        SHORT = new SelfTimer(2130837672, 2131361823, true, 2000, ShutterToneGenerator.Type.SOUND_SELFTIMER_2SEC);
        INSTANT = new SelfTimer(2130837670, 2131362191, true, 500, null);
        OFF = new SelfTimer(2130837673, 2131361807, false, 0, null);
        SelfTimer[] arrselfTimer = new SelfTimer[]{LONG, SHORT, INSTANT, OFF};
        $VALUES = arrselfTimer;
    }

    private SelfTimer(int n2, int n3, boolean bl, int n4, ShutterToneGenerator.Type type) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mBooleanValue = bl;
        this.mMilliSeconds = n4;
        this.mSoundType = type;
    }

    public static SelfTimer[] getOptions() {
        return SelfTimer.values();
    }

    public static SelfTimer valueOf(String string) {
        return (SelfTimer)Enum.valueOf((Class)SelfTimer.class, (String)string);
    }

    public static SelfTimer[] values() {
        return (SelfTimer[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((SelfTimer)this);
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override
    public int getDurationInMillisecond() {
        return this.mMilliSeconds;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SELF_TIMER;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361822;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return arrparameterValue[0];
    }

    public ShutterToneGenerator.Type getSoundType() {
        return this.mSoundType;
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

