/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import java.util.ArrayList;

public final class VolumeKey
extends Enum<VolumeKey>
implements ParameterValue {
    private static final /* synthetic */ VolumeKey[] $VALUES;
    public static final /* enum */ VolumeKey HW_CAMERA_KEY;
    public static final /* enum */ VolumeKey VOLUME;
    public static final /* enum */ VolumeKey ZOOM;
    private static final int sParameterTextId = 2131362046;
    private final int mIconId;
    private final int mTextId;
    private String mValue;

    static {
        ZOOM = new VolumeKey(-1, 2131362047);
        VOLUME = new VolumeKey(-1, 2131362049);
        HW_CAMERA_KEY = new VolumeKey(-1, 2131362048);
        VolumeKey[] arrvolumeKey = new VolumeKey[]{ZOOM, VOLUME, HW_CAMERA_KEY};
        $VALUES = arrvolumeKey;
    }

    private VolumeKey(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static VolumeKey getDefault() {
        return ZOOM;
    }

    public static VolumeKey[] getOptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)ZOOM);
        arrayList.add((Object)VOLUME);
        arrayList.add((Object)HW_CAMERA_KEY);
        return (VolumeKey[])arrayList.toArray((Object[])new VolumeKey[0]);
    }

    public static VolumeKey valueOf(String string) {
        return (VolumeKey)Enum.valueOf((Class)VolumeKey.class, (String)string);
    }

    public static VolumeKey[] values() {
        return (VolumeKey[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VolumeKey)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VOLUME_KEY;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362046;
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

