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

public final class Hdr
extends Enum<Hdr>
implements ParameterValue {
    private static final /* synthetic */ Hdr[] $VALUES;
    public static final /* enum */ Hdr HDR_AUTO;
    public static final /* enum */ Hdr HDR_OFF;
    public static final /* enum */ Hdr HDR_ON;
    private static final int sParameterTextId = 2131361958;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        HDR_ON = new Hdr(-1, 2131361806, "on-still-hdr");
        HDR_AUTO = new Hdr(-1, 2131361806, "auto");
        HDR_OFF = new Hdr(-1, 2131361807, "off");
        Hdr[] arrhdr = new Hdr[]{HDR_ON, HDR_AUTO, HDR_OFF};
        $VALUES = arrhdr;
    }

    private Hdr(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    public static Hdr getDefault(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            return HDR_AUTO;
        }
        return HDR_OFF;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Hdr[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).HDR.get();
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            if (capturingMode.getType() != 1) return (Hdr[])arrayList.toArray((Object[])new Hdr[0]);
            if (!list.contains((Object)HDR_ON.getValue())) return (Hdr[])arrayList.toArray((Object[])new Hdr[0]);
            arrayList.add((Object)HDR_ON);
            arrayList.add((Object)HDR_OFF);
            return (Hdr[])arrayList.toArray((Object[])new Hdr[0]);
        }
        if (!list.contains((Object)HDR_AUTO.getValue())) return (Hdr[])arrayList.toArray((Object[])new Hdr[0]);
        arrayList.add((Object)HDR_AUTO);
        return (Hdr[])arrayList.toArray((Object[])new Hdr[0]);
    }

    public static boolean isResolutionIndependentHdrSupported(List<String> list) {
        return list.contains((Object)"hdr");
    }

    public static Hdr valueOf(String string) {
        return (Hdr)Enum.valueOf((Class)Hdr.class, (String)string);
    }

    public static Hdr[] values() {
        return (Hdr[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Hdr)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.HDR;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361958;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return HDR_OFF;
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

