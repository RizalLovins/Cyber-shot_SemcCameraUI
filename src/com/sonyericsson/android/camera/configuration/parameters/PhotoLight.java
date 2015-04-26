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

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import java.util.ArrayList;
import java.util.List;

public final class PhotoLight
extends Enum<PhotoLight>
implements ParameterValue {
    private static final /* synthetic */ PhotoLight[] $VALUES;
    public static final /* enum */ PhotoLight OFF;
    public static final /* enum */ PhotoLight ON;
    private static final int sParameterTextId = 2131362016;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        ON = new PhotoLight(2130837590, 2131361806, "torch", true);
        OFF = new PhotoLight(2130837591, 2131361807, "off", false);
        PhotoLight[] arrphotoLight = new PhotoLight[]{ON, OFF};
        $VALUES = arrphotoLight;
    }

    private PhotoLight(int n2, int n3, String string2, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
        this.mBooleanValue = bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static PhotoLight[] getOptions(ActionMode actionMode) {
        ArrayList arrayList = new ArrayList();
        List<String> list = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)actionMode.mCameraId).FLASH.get();
        if (list.isEmpty()) return (PhotoLight[])arrayList.toArray((Object[])new PhotoLight[0]);
        for (PhotoLight photoLight : LedOptionsResolver.getInstance().getPhotoLightOptions(actionMode, list)) {
            for (String string : list) {
                if (!photoLight.getValue().equals((Object)string)) continue;
                arrayList.add((Object)photoLight);
            }
        }
        return (PhotoLight[])arrayList.toArray((Object[])new PhotoLight[0]);
    }

    public static PhotoLight getPhotoLightFromParameterString(String string) {
        for (PhotoLight photoLight : PhotoLight.values()) {
            if (!photoLight.getValue().equals((Object)string)) continue;
            return photoLight;
        }
        return null;
    }

    public static PhotoLight valueOf(String string) {
        return (PhotoLight)Enum.valueOf((Class)PhotoLight.class, (String)string);
    }

    public static PhotoLight[] values() {
        return (PhotoLight[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((PhotoLight)this);
    }

    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.PHOTO_LIGHT;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362016;
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

