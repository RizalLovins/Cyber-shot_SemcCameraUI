/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import java.util.ArrayList;

public final class Ev
extends Enum<Ev>
implements ParameterValue {
    private static final /* synthetic */ Ev[] $VALUES;
    public static final /* enum */ Ev M1_3;
    public static final /* enum */ Ev M2_3;
    public static final /* enum */ Ev M3_3;
    public static final /* enum */ Ev M4_3;
    public static final /* enum */ Ev M5_3;
    public static final /* enum */ Ev M6_3;
    public static final /* enum */ Ev P1_3;
    public static final /* enum */ Ev P2_3;
    public static final /* enum */ Ev P3_3;
    public static final /* enum */ Ev P4_3;
    public static final /* enum */ Ev P5_3;
    public static final /* enum */ Ev P6_3;
    public static final /* enum */ Ev ZERO;
    private static final int sParameterTextId = 2131361836;
    private final int mIconId;
    private int mIndex;
    private final int mTextId;
    private final float mValue;

    static {
        M6_3 = new Ev(2130837581, 2131361837, -2.0f);
        M5_3 = new Ev(2130837581, 2131361838, -1.7f);
        M4_3 = new Ev(2130837581, 2131361839, -1.3f);
        M3_3 = new Ev(2130837581, 2131361840, -1.0f);
        M2_3 = new Ev(2130837581, 2131361841, -0.7f);
        M1_3 = new Ev(2130837581, 2131361842, -0.3f);
        ZERO = new Ev(2130837582, 2131361843, 0.0f);
        P1_3 = new Ev(2130837581, 2131361844, 0.3f);
        P2_3 = new Ev(2130837581, 2131361845, 0.7f);
        P3_3 = new Ev(2130837581, 2131361846, 1.0f);
        P4_3 = new Ev(2130837581, 2131361847, 1.3f);
        P5_3 = new Ev(2130837581, 2131361848, 1.7f);
        P6_3 = new Ev(2130837581, 2131361849, 2.0f);
        Ev[] arrev = new Ev[]{M6_3, M5_3, M4_3, M3_3, M2_3, M1_3, ZERO, P1_3, P2_3, P3_3, P4_3, P5_3, P6_3};
        $VALUES = arrev;
    }

    private Ev(int n2, int n3, float f) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = f;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Ev[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
        int n = capabilityList.EV_MAX.get();
        int n2 = capabilityList.EV_MIN.get();
        if (n == 0 && n2 == 0) {
            do {
                return (Ev[])arrayList.toArray((Object[])new Ev[0]);
                break;
            } while (true);
        }
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            arrayList.add((Object)ZERO);
            return (Ev[])arrayList.toArray((Object[])new Ev[0]);
        }
        float f = capabilityList.EV_STEP.get().floatValue();
        Ev[] arrev = Ev.values();
        int n3 = arrev.length;
        int n4 = 0;
        block1 : while (n4 < n3) {
            Ev ev = arrev[n4];
            int n5 = n2;
            do {
                if (n5 <= n) {
                    if ((int)(0.5 + (double)(10.0f * (f * (float)n5))) == (int)(0.5 + (double)(10.0f * ev.mValue))) {
                        ev.mIndex = n5;
                        arrayList.add((Object)ev);
                    }
                } else {
                    ++n4;
                    continue block1;
                }
                ++n5;
            } while (true);
            break;
        }
        return (Ev[])arrayList.toArray((Object[])new Ev[0]);
    }

    public static Ev valueOf(String string) {
        return (Ev)Enum.valueOf((Class)Ev.class, (String)string);
    }

    public static Ev[] values() {
        return (Ev[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Ev)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getIntValue() {
        return this.mIndex;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.EV;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361836;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return ZERO;
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

