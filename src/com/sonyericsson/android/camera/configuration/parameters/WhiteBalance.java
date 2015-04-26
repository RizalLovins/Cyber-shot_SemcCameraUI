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

public final class WhiteBalance
extends Enum<WhiteBalance>
implements ParameterValue {
    private static final /* synthetic */ WhiteBalance[] $VALUES;
    public static final /* enum */ WhiteBalance AUTO = new WhiteBalance(2130837751, 2131361851, "auto");
    public static final /* enum */ WhiteBalance CLOUDY_DAYLIGHT;
    public static final /* enum */ WhiteBalance DAYLIGHT;
    public static final /* enum */ WhiteBalance FLUORESCENT;
    public static final /* enum */ WhiteBalance INCANDESCENT;
    private static final int sParameterTextId = 2131361850;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        INCANDESCENT = new WhiteBalance(2130837755, 2131361852, "incandescent");
        FLUORESCENT = new WhiteBalance(2130837754, 2131361853, "fluorescent");
        DAYLIGHT = new WhiteBalance(2130837753, 2131361854, "daylight");
        CLOUDY_DAYLIGHT = new WhiteBalance(2130837752, 2131361855, "cloudy-daylight");
        WhiteBalance[] arrwhiteBalance = new WhiteBalance[]{AUTO, INCANDESCENT, FLUORESCENT, DAYLIGHT, CLOUDY_DAYLIGHT};
        $VALUES = arrwhiteBalance;
    }

    private WhiteBalance(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static WhiteBalance[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)capturingMode.getCameraId()).WHITE_BALANCE.get();
        if (list.isEmpty()) return (WhiteBalance[])arrayList.toArray((Object[])new WhiteBalance[0]);
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            arrayList.add((Object)AUTO);
            return (WhiteBalance[])arrayList.toArray((Object[])new WhiteBalance[0]);
        }
        for (WhiteBalance whiteBalance : WhiteBalance.values()) {
            for (String string : list) {
                if (!whiteBalance.getValue().equals((Object)string)) continue;
                arrayList.add((Object)whiteBalance);
            }
        }
        return (WhiteBalance[])arrayList.toArray((Object[])new WhiteBalance[0]);
    }

    public static WhiteBalance valueOf(String string) {
        return (WhiteBalance)Enum.valueOf((Class)WhiteBalance.class, (String)string);
    }

    public static WhiteBalance[] values() {
        return (WhiteBalance[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((WhiteBalance)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.WHITE_BALANCE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361850;
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

