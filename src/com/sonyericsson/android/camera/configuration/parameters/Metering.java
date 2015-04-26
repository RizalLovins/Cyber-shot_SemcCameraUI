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

public final class Metering
extends Enum<Metering>
implements ParameterValue {
    private static final /* synthetic */ Metering[] $VALUES;
    public static final /* enum */ Metering AVERAGE;
    public static final /* enum */ Metering CENTER;
    public static final /* enum */ Metering FACE;
    public static final /* enum */ Metering MULTI;
    public static final /* enum */ Metering SPOT;
    private static final int sParameterTextId = 2131361856;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        FACE = new Metering(-1, 2131362108, "face");
        MULTI = new Metering(-1, 2131361979, "multi");
        CENTER = new Metering(-1, 2131361857, "center-weighted");
        SPOT = new Metering(-1, 2131361859, "spot");
        AVERAGE = new Metering(-1, 2131361858, "frame-average");
        Metering[] arrmetering = new Metering[]{FACE, MULTI, CENTER, SPOT, AVERAGE};
        $VALUES = arrmetering;
    }

    private Metering(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    public static Metering getDefaultValue(CapturingMode capturingMode) {
        if (com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).METERING.get().contains((Object)Metering.FACE.mValue)) {
            return FACE;
        }
        return CENTER;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Metering[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).METERING.get();
        if (list.isEmpty()) return (Metering[])arrayList.toArray((Object[])new Metering[0]);
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            arrayList.add((Object)Metering.getDefaultValue(capturingMode));
            return (Metering[])arrayList.toArray((Object[])new Metering[0]);
        }
        for (Metering metering : Metering.values()) {
            for (String string : list) {
                if (!metering.getValue().equals((Object)string)) continue;
                arrayList.add((Object)metering);
            }
        }
        return (Metering[])arrayList.toArray((Object[])new Metering[0]);
    }

    public static Metering valueOf(String string) {
        return (Metering)Enum.valueOf((Class)Metering.class, (String)string);
    }

    public static Metering[] values() {
        return (Metering[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Metering)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.METERING;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361856;
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

