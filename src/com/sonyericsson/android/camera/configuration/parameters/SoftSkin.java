/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import java.util.List;

public final class SoftSkin
extends Enum<SoftSkin>
implements ParameterValue {
    private static final /* synthetic */ SoftSkin[] $VALUES;
    public static final /* enum */ SoftSkin OFF;
    public static final /* enum */ SoftSkin ON;
    private static final int sParameterTextId = 2131361990;
    private final int mIconId;
    private final Scene mScene;
    private final int mTextId;
    private final float mValue;

    static {
        ON = new SoftSkin(-1, 2131361806, Scene.SOFT_SKIN, 0.5f);
        OFF = new SoftSkin(-1, 2131361807, Scene.OFF, 0.0f);
        SoftSkin[] arrsoftSkin = new SoftSkin[]{ON, OFF};
        $VALUES = arrsoftSkin;
    }

    private SoftSkin(int n2, int n3, Scene scene, float f) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mScene = scene;
        this.mValue = f;
    }

    public static SoftSkin[] getOptions(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            List<String> list = HardwareCapability.getCapability((int)capturingMode.getCameraId()).SCENE.get();
            if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8)) {
                if (list.contains((Object)"portrait") && HardwareCapability.getInstance().getMaxSoftSkinLevel(capturingMode.getCameraId()) > 0) {
                    SoftSkin[] arrsoftSkin = new SoftSkin[]{ON, OFF};
                    return arrsoftSkin;
                }
                if (list.contains((Object)"soft-skin")) {
                    SoftSkin[] arrsoftSkin = new SoftSkin[]{ON, OFF};
                    return arrsoftSkin;
                }
                return new SoftSkin[0];
            }
            if (list.contains((Object)"soft-skin")) {
                SoftSkin[] arrsoftSkin = new SoftSkin[]{ON, OFF};
                return arrsoftSkin;
            }
            return new SoftSkin[0];
        }
        return new SoftSkin[0];
    }

    public static SoftSkin valueOf(String string) {
        return (SoftSkin)Enum.valueOf((Class)SoftSkin.class, (String)string);
    }

    public static SoftSkin[] values() {
        return (SoftSkin[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((SoftSkin)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    public String getLevel(int n) {
        return String.valueOf((int)((int)((float)n * this.mValue)));
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SOFT_SKIN;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361990;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    public int getParameterkeyTitleTextId() {
        return 2131361995;
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return ON;
    }

    public Scene getScene() {
        return this.mScene;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    @Override
    public String getValue() {
        return this.toString();
    }

    public boolean isEnable() {
        if (ON == this) {
            return true;
        }
        return false;
    }
}

