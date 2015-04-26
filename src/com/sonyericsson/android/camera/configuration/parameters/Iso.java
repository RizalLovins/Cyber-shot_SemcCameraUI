/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
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
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import java.util.ArrayList;
import java.util.List;

public final class Iso
extends Enum<Iso>
implements ParameterValue {
    private static final /* synthetic */ Iso[] $VALUES;
    public static final /* enum */ Iso ISO_100;
    public static final /* enum */ Iso ISO_10000;
    public static final /* enum */ Iso ISO_12800;
    public static final /* enum */ Iso ISO_1600;
    public static final /* enum */ Iso ISO_200;
    public static final /* enum */ Iso ISO_3200;
    public static final /* enum */ Iso ISO_400;
    public static final /* enum */ Iso ISO_50;
    public static final /* enum */ Iso ISO_6400;
    public static final /* enum */ Iso ISO_800;
    public static final /* enum */ Iso ISO_AUTO;
    private static final int sParameterTextId = 2131362222;
    private final String mAeMode;
    private final int mIconId;
    private final int mIsoValue;
    private final int mTextId;

    static {
        ISO_AUTO = new Iso(-1, 2131361808, "auto", -1);
        ISO_50 = new Iso(-1, 2131362224, "iso-prio", 50);
        ISO_100 = new Iso(-1, 2131362225, "iso-prio", 100);
        ISO_200 = new Iso(-1, 2131362226, "iso-prio", 200);
        ISO_400 = new Iso(-1, 2131362227, "iso-prio", 400);
        ISO_800 = new Iso(-1, 2131362228, "iso-prio", 800);
        ISO_1600 = new Iso(-1, 2131362229, "iso-prio", 1600);
        ISO_3200 = new Iso(-1, 2131362230, "iso-prio", 3200);
        ISO_6400 = new Iso(-1, 2131362231, "iso-prio", 6400);
        ISO_10000 = new Iso(-1, 2131362232, "iso-prio", 10000);
        ISO_12800 = new Iso(-1, 2131362233, "iso-prio", 12800);
        Iso[] arriso = new Iso[]{ISO_AUTO, ISO_50, ISO_100, ISO_200, ISO_400, ISO_800, ISO_1600, ISO_3200, ISO_6400, ISO_10000, ISO_12800};
        $VALUES = arriso;
    }

    private Iso(int n2, int n3, String string2, int n4) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mAeMode = string2;
        this.mIsoValue = n4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Iso[] getOptions(CapturingMode capturingMode, Resolution resolution) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
        List<String> list = capabilityList.AE.get();
        if (list.isEmpty()) {
            return (Iso[])arrayList.toArray((Object[])new Iso[0]);
        }
        if (list.contains((Object)ISO_AUTO.getValue())) {
            arrayList.add((Object)ISO_AUTO);
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode.getType() == 2) {
                return (Iso[])arrayList.toArray((Object[])new Iso[0]);
            }
        }
        if (!list.contains((Object)ISO_100.getValue())) {
            return (Iso[])arrayList.toArray((Object[])new Iso[0]);
        }
        List<String> list2 = capabilityList.ISO.get();
        for (Iso iso : Iso.values()) {
            if (!list2.contains((Object)Integer.toString((int)iso.getIsoValue()))) continue;
            if (resolution.getPictureRect().width() >= 5248) {
                if (iso.getIsoValue() > ISO_800.getIsoValue()) continue;
                arrayList.add((Object)iso);
                continue;
            }
            arrayList.add((Object)iso);
        }
        return (Iso[])arrayList.toArray((Object[])new Iso[0]);
    }

    public static boolean isAlwaysChangeToAutoToResolveDependency() {
        return HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8);
    }

    public static boolean isExclusiveWith(Resolution resolution) {
        if (5248 <= resolution.getPictureRect().width()) {
            return true;
        }
        return false;
    }

    public static Iso valueOf(String string) {
        return (Iso)Enum.valueOf((Class)Iso.class, (String)string);
    }

    public static Iso[] values() {
        return (Iso[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Iso)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public int getIsoValue() {
        return this.mIsoValue;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.ISO;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362222;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return ISO_AUTO;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    @Override
    public String getValue() {
        return this.mAeMode;
    }
}

