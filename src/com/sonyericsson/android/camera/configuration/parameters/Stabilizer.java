/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
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

public final class Stabilizer
extends Enum<Stabilizer>
implements ParameterValue {
    private static final /* synthetic */ Stabilizer[] $VALUES;
    public static final /* enum */ Stabilizer AUTO;
    public static final /* enum */ Stabilizer OFF;
    public static final /* enum */ Stabilizer ON;
    private static final int sParameterTextId = 2131361860;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mNotificationId;
    private final int mTextId;
    private final String mValue;

    static {
        ON = new Stabilizer(-1, 2131361806, -1, "on", true);
        OFF = new Stabilizer(-1, 2131361807, -1, "off", false);
        AUTO = new Stabilizer(-1, 2131361806, -1, "auto", false);
        Stabilizer[] arrstabilizer = new Stabilizer[]{ON, OFF, AUTO};
        $VALUES = arrstabilizer;
    }

    private Stabilizer(int n2, int n3, int n4, String string2, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mNotificationId = n4;
        this.mBooleanValue = bl;
        this.mValue = string2;
    }

    public static Stabilizer getDefault(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return AUTO;
        }
        return OFF;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Stabilizer[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        if (!(capturingMode.getType() == 2 || com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).IMAGE_STABILIZER.get().isEmpty())) {
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
                arrayList.add((Object)AUTO);
            }
        } else {
            do {
                return (Stabilizer[])arrayList.toArray((Object[])new Stabilizer[0]);
                break;
            } while (true);
        }
        arrayList.add((Object)ON);
        arrayList.add((Object)OFF);
        return (Stabilizer[])arrayList.toArray((Object[])new Stabilizer[0]);
    }

    public static Stabilizer valueOf(String string) {
        return (Stabilizer)Enum.valueOf((Class)Stabilizer.class, (String)string);
    }

    public static Stabilizer[] values() {
        return (Stabilizer[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Stabilizer)this);
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
        return ParameterKey.STABILIZER;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361860;
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
}

