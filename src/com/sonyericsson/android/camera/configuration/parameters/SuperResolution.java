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
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.utility.ClassDefinitionChecker;
import java.util.ArrayList;
import java.util.List;

public final class SuperResolution
extends Enum<SuperResolution>
implements ParameterValue {
    private static final /* synthetic */ SuperResolution[] $VALUES;
    public static final /* enum */ SuperResolution OFF;
    public static final /* enum */ SuperResolution ON;
    private static final String TAG;
    private static final int sParameterTextId = -1;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        ON = new SuperResolution(-1, 2131361806, "on");
        OFF = new SuperResolution(-1, 2131361807, "off");
        SuperResolution[] arrsuperResolution = new SuperResolution[]{ON, OFF};
        $VALUES = arrsuperResolution;
        TAG = SuperResolution.class.getSimpleName();
    }

    private SuperResolution(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static SuperResolution[] getOptions(CapturingMode capturingMode, boolean bl) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
        boolean bl2 = capabilityList.SR_ZOOM.get();
        boolean bl3 = capabilityList.EXIF_MAKER_NOTES_TYPES.get().contains((Object)"super-resolution");
        boolean bl4 = ClassDefinitionChecker.isSuperResolutionProcessorSupported();
        if (bl2 && bl3 && bl4) {
            if (bl) {
                arrayList.add((Object)OFF);
                do {
                    return (SuperResolution[])arrayList.toArray((Object[])new SuperResolution[0]);
                    break;
                } while (true);
            }
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.NORMAL) {
                arrayList.add((Object)ON);
                return (SuperResolution[])arrayList.toArray((Object[])new SuperResolution[0]);
            }
            arrayList.add((Object)OFF);
            return (SuperResolution[])arrayList.toArray((Object[])new SuperResolution[0]);
        }
        arrayList.add((Object)OFF);
        return (SuperResolution[])arrayList.toArray((Object[])new SuperResolution[0]);
    }

    public static boolean isSupported(CapturingMode capturingMode, boolean bl) {
        SuperResolution[] arrsuperResolution = SuperResolution.getOptions(capturingMode, bl);
        int n = arrsuperResolution.length;
        for (int i = 0; i < n; ++i) {
            if (arrsuperResolution[i] != ON) continue;
            return true;
        }
        return false;
    }

    public static SuperResolution valueOf(String string) {
        return (SuperResolution)Enum.valueOf((Class)SuperResolution.class, (String)string);
    }

    public static SuperResolution[] values() {
        return (SuperResolution[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((SuperResolution)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SUPER_RESOLUTION;
    }

    @Override
    public int getParameterKeyTextId() {
        return -1;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return OFF;
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

