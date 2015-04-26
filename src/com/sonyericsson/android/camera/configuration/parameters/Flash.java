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
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import java.util.ArrayList;
import java.util.List;

public final class Flash
extends Enum<Flash>
implements ParameterValue {
    private static final /* synthetic */ Flash[] $VALUES;
    public static final /* enum */ Flash AUTO = new Flash(2130837586, 2131361808, "auto", true);
    public static final /* enum */ Flash LED_OFF;
    public static final /* enum */ Flash LED_ON;
    public static final /* enum */ Flash OFF;
    public static final /* enum */ Flash ON;
    public static final /* enum */ Flash PHOTO_LIGHT_ON_AS_FLASH;
    public static final /* enum */ Flash RED_EYE;
    private final int mIconId;
    private final boolean mIsSceneDependent;
    private final int mTextId;
    private final String mValue;

    static {
        ON = new Flash(2130837587, 2131361819, "on", false);
        RED_EYE = new Flash(2130837589, 2131361820, "red-eye", true);
        OFF = new Flash(2130837588, 2131361807, "off", false);
        LED_ON = new Flash(2130837590, 2131362016, "torch", false);
        LED_OFF = new Flash(2130837591, 2131361807, "off", false);
        PHOTO_LIGHT_ON_AS_FLASH = new Flash(PhotoLight.ON.getIconId(), PhotoLight.ON.getTextId(), PhotoLight.ON.getValue(), false);
        Flash[] arrflash = new Flash[]{AUTO, ON, RED_EYE, OFF, LED_ON, LED_OFF, PHOTO_LIGHT_ON_AS_FLASH};
        $VALUES = arrflash;
    }

    private Flash(int n2, int n3, String string2, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
        this.mIsSceneDependent = bl;
    }

    public static Flash getDefaultValue() {
        return LedOptionsResolver.getInstance().getDefaultFlash();
    }

    public static Flash getFlashFromParameterString(String string) {
        for (Flash flash : Flash.values()) {
            if (!flash.getValue().equals((Object)string)) continue;
            return flash;
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Flash[] getOptions(ActionMode actionMode) {
        List<String> list;
        ArrayList arrayList = new ArrayList();
        if (actionMode.mType != 1 || (list = com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)actionMode.mCameraId).FLASH.get()).isEmpty()) return (Flash[])arrayList.toArray((Object[])new Flash[0]);
        for (Flash flash : LedOptionsResolver.getInstance().getFlashOptions(actionMode, list)) {
            for (String string : list) {
                if (!flash.getValue().equals((Object)string)) continue;
                arrayList.add((Object)flash);
            }
        }
        return (Flash[])arrayList.toArray((Object[])new Flash[0]);
    }

    public static int getParameterKeyTitleTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTitleTextId();
    }

    public static boolean isSupported(int n) {
        if (!com.sonyericsson.android.camera.util.capability.HardwareCapability.getCapability((int)n).FLASH.get().isEmpty()) {
            return true;
        }
        return false;
    }

    public static Flash valueOf(String string) {
        return (Flash)Enum.valueOf((Class)Flash.class, (String)string);
    }

    public static Flash[] values() {
        return (Flash[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Flash)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.FLASH;
    }

    @Override
    public int getParameterKeyTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTextId();
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

    public boolean isSceneDependent() {
        return this.mIsSceneDependent;
    }
}

