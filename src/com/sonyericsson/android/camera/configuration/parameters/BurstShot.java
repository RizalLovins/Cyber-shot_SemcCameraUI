/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import java.util.ArrayList;
import java.util.List;

public final class BurstShot
extends Enum<BurstShot>
implements ParameterValue {
    private static final /* synthetic */ BurstShot[] $VALUES;
    public static final /* enum */ BurstShot BEST_EFFORT;
    public static final /* enum */ BurstShot HIGH;
    public static final /* enum */ BurstShot OFF;
    private static final int sParameterTextId = 2131361994;
    private final int mIconId;
    private final int mQuality;
    private final int mTextId;
    private final String mValue;

    static {
        HIGH = new BurstShot(-1, 2131362024, 1, "on");
        BEST_EFFORT = new BurstShot(-1, 2131362023, 1, "off");
        OFF = new BurstShot(-1, 2131361807, -1, "off");
        BurstShot[] arrburstShot = new BurstShot[]{HIGH, BEST_EFFORT, OFF};
        $VALUES = arrburstShot;
    }

    private BurstShot(int n2, int n3, int n4, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mQuality = n4;
        this.mValue = string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Resolution getBurstResolution(Parameters parameters) {
        Resolution resolution = parameters.getResolution();
        if (parameters.getBurstShot() != HIGH) return resolution;
        Rect rect = resolution.getPictureRect();
        Rect rect2 = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)parameters.capturingMode.getCameraId()).MAX_BURST_SHOT_SIZE.get();
        if (rect.width() * rect.height() <= rect2.width() * rect2.height()) {
            return resolution;
        }
        for (Resolution resolution2 : Resolution.values()) {
            if (!resolution2.getPictureRect().equals((Object)rect2)) continue;
            return resolution2;
        }
        throw new IllegalStateException("max burst size is not contained in supported." + (Object)rect);
    }

    public static BurstShot getDefault() {
        return OFF;
    }

    public static BurstShot[] getOptions() {
        return BurstShot.values();
    }

    public static BurstShot[] getOptions(boolean bl, CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (!(bl || capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.FRONT_PHOTO && capturingMode != CapturingMode.SUPERIOR_FRONT)) {
            if (!(capturingMode.getCameraId() != 0 || com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).BURST_SHOT.get().isEmpty())) {
                arrayList.add((Object)HIGH);
            }
            arrayList.add((Object)BEST_EFFORT);
        }
        arrayList.add((Object)OFF);
        return (BurstShot[])arrayList.toArray((Object[])new BurstShot[0]);
    }

    public static BurstShot valueOf(String string) {
        return (BurstShot)Enum.valueOf((Class)BurstShot.class, (String)string);
    }

    public static BurstShot[] values() {
        return (BurstShot[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((BurstShot)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.BURST_SHOT;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361994;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public int getQuality() {
        return this.mQuality;
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

    public boolean isBurstShotOn() {
        if (!this.equals((Object)OFF)) {
            return true;
        }
        return false;
    }
}

