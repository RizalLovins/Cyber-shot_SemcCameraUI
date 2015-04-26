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
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;

public final class VideoSelfTimer
extends Enum<VideoSelfTimer>
implements ParameterValue {
    private static final /* synthetic */ VideoSelfTimer[] $VALUES;
    public static final /* enum */ VideoSelfTimer INSTANT;
    public static final /* enum */ VideoSelfTimer LONG;
    public static final /* enum */ VideoSelfTimer OFF;
    public static final /* enum */ VideoSelfTimer SHORT;
    private static final int sParameterTextId = 2131361822;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mMilliSeconds;
    private final int mNotificationIconId;
    private final ShutterToneGenerator.Type mSoundType;
    private final int mTextId;

    static {
        LONG = new VideoSelfTimer(SelfTimer.LONG, 2130837740);
        SHORT = new VideoSelfTimer(SelfTimer.SHORT, 2130837741);
        INSTANT = new VideoSelfTimer(SelfTimer.INSTANT, 2130837739);
        OFF = new VideoSelfTimer(SelfTimer.OFF, -1);
        VideoSelfTimer[] arrvideoSelfTimer = new VideoSelfTimer[]{LONG, SHORT, INSTANT, OFF};
        $VALUES = arrvideoSelfTimer;
    }

    private VideoSelfTimer(SelfTimer selfTimer, int n2) {
        super(string, n);
        this.mIconId = selfTimer.getIconId();
        this.mTextId = selfTimer.getTextId();
        this.mNotificationIconId = n2;
        this.mBooleanValue = selfTimer.getBooleanValue();
        this.mMilliSeconds = selfTimer.getDurationInMillisecond();
        this.mSoundType = selfTimer.getSoundType();
    }

    public static VideoSelfTimer[] getOptions() {
        return VideoSelfTimer.values();
    }

    public static VideoSelfTimer valueOf(String string) {
        return (VideoSelfTimer)Enum.valueOf((Class)VideoSelfTimer.class, (String)string);
    }

    public static VideoSelfTimer[] values() {
        return (VideoSelfTimer[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VideoSelfTimer)this);
    }

    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getNotificationIconId() {
        return this.mNotificationIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VIDEO_SELF_TIMER;
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

    public int getTime() {
        return this.mMilliSeconds;
    }

    @Override
    public String getValue() {
        return this.toString();
    }
}

