/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;

public final class BalloonTipsCounter
extends Enum<BalloonTipsCounter>
implements CommonSettingValue {
    private static final /* synthetic */ BalloonTipsCounter[] $VALUES;
    public static final /* enum */ BalloonTipsCounter COUNT_START = new BalloonTipsCounter(-1, -1, null, "count_start");
    public static final /* enum */ BalloonTipsCounter COUNT_STOP;
    public static final /* enum */ BalloonTipsCounter DISPLAY_OK;
    public static final /* enum */ BalloonTipsCounter DISPLAY_OK_FIRST;
    public static final /* enum */ BalloonTipsCounter DISPLAY_OK_SECOND;
    public static final /* enum */ BalloonTipsCounter FIRST;
    public static final /* enum */ BalloonTipsCounter SECOND;
    private static int sParameterTextId;
    private final BalloonTipsCounter mCompatibleValue;
    private int mIconId;
    private final String mProviderValue;
    private int mTextId;

    static {
        FIRST = new BalloonTipsCounter(-1, -1, null, "first");
        SECOND = new BalloonTipsCounter(-1, -1, null, "second");
        DISPLAY_OK_FIRST = new BalloonTipsCounter(-1, -1, null, "display_ok_first");
        DISPLAY_OK_SECOND = new BalloonTipsCounter(-1, -1, null, "display_ok_second");
        DISPLAY_OK = new BalloonTipsCounter(-1, -1, null, "display_ok");
        COUNT_STOP = new BalloonTipsCounter(-1, -1, null, "count_stop");
        BalloonTipsCounter[] arrballoonTipsCounter = new BalloonTipsCounter[]{COUNT_START, FIRST, SECOND, DISPLAY_OK_FIRST, DISPLAY_OK_SECOND, DISPLAY_OK, COUNT_STOP};
        $VALUES = arrballoonTipsCounter;
        sParameterTextId = -1;
    }

    private BalloonTipsCounter(int n2, int n3, BalloonTipsCounter balloonTipsCounter, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mCompatibleValue = balloonTipsCounter;
        this.mProviderValue = string2;
    }

    public static BalloonTipsCounter valueOf(String string) {
        return (BalloonTipsCounter)Enum.valueOf((Class)BalloonTipsCounter.class, (String)string);
    }

    public static BalloonTipsCounter[] values() {
        return (BalloonTipsCounter[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.BALLOON_TIPS_COUNTER;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getParameterKeyTextId() {
        return sParameterTextId;
    }

    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public String getProviderValue() {
        return this.mProviderValue;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    public BalloonTipsCounter isCompatibleValue() {
        return this.mCompatibleValue;
    }
}

