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
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.android.camera.util.capability.UxOptions;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import java.util.ArrayList;
import java.util.List;

public final class Scene
extends Enum<Scene>
implements ParameterValue {
    private static final /* synthetic */ Scene[] $VALUES;
    public static final /* enum */ Scene ANTI_MOTION;
    public static final /* enum */ Scene BACKLIGHT_HDR;
    public static final /* enum */ Scene BEACH;
    public static final /* enum */ Scene BEACH_SNOW;
    public static final /* enum */ Scene DOCUMENT;
    public static final /* enum */ Scene FIRE_WORKS;
    public static final /* enum */ Scene GOURMET;
    public static final /* enum */ Scene HAND_NIGHT;
    public static final /* enum */ Scene HIGH_SENSITIVITY;
    public static final /* enum */ Scene LANDSCAPE;
    public static final /* enum */ Scene NIGHT;
    public static final /* enum */ Scene NIGHT_MODE;
    public static final /* enum */ Scene NIGHT_PORTRAIT;
    public static final /* enum */ Scene OFF;
    public static final /* enum */ Scene PARTY;
    public static final /* enum */ Scene PET;
    public static final /* enum */ Scene PORTRAIT;
    public static final /* enum */ Scene SNOW;
    public static final /* enum */ Scene SOFT_SKIN;
    public static final /* enum */ Scene SPORTS;
    private static final int sParameterTextId = 2131361809;
    private final FocusMode mFocusMode;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        OFF = new Scene(2130837641, 2131361807, "auto", FocusMode.SINGLE);
        PORTRAIT = new Scene(2130837658, 2131361974, "portrait", FocusMode.FACE_DETECTION);
        NIGHT_PORTRAIT = new Scene(2130837651, 2131361973, "night-portrait", FocusMode.FACE_DETECTION);
        LANDSCAPE = new Scene(2130837650, 2131361810, "landscape", FocusMode.INFINITY);
        NIGHT = new Scene(2130837652, 2131361812, "night", FocusMode.INFINITY);
        BEACH_SNOW = new Scene(2130837644, 2131361815, "snow", FocusMode.FACE_DETECTION);
        SPORTS = new Scene(2130837659, 2131361814, "sports", FocusMode.FACE_DETECTION);
        PARTY = new Scene(2130837654, 2131361813, "party", FocusMode.FACE_DETECTION);
        DOCUMENT = new Scene(2130837645, 2131361816, "document", FocusMode.SINGLE);
        NIGHT_MODE = new Scene(2130837652, 2131361811, "night", FocusMode.SINGLE);
        BACKLIGHT_HDR = new Scene(2130837643, 2131361975, "backlight-portrait", FocusMode.FACE_DETECTION);
        BEACH = new Scene(2130837644, 2131361969, "beach", FocusMode.FACE_DETECTION);
        SNOW = new Scene(2130837656, 2131361970, "snow", FocusMode.FACE_DETECTION);
        SOFT_SKIN = new Scene(2130837657, 2131361987, "soft-skin", FocusMode.FACE_DETECTION);
        GOURMET = new Scene(2130837647, 2131361988, "dish", FocusMode.SINGLE);
        PET = new Scene(2130837655, 2131361989, "pet", FocusMode.SINGLE);
        ANTI_MOTION = new Scene(2130837642, 2131362006, "anti-motion-blur", FocusMode.FACE_DETECTION);
        HAND_NIGHT = new Scene(2130837648, 2131362007, "handheld-twilight", FocusMode.SINGLE);
        HIGH_SENSITIVITY = new Scene(2130837649, 2131362008, "high-sensitivity", FocusMode.FACE_DETECTION);
        FIRE_WORKS = new Scene(2130837646, 2131362009, "fireworks", FocusMode.INFINITY);
        Scene[] arrscene = new Scene[]{OFF, PORTRAIT, NIGHT_PORTRAIT, LANDSCAPE, NIGHT, BEACH_SNOW, SPORTS, PARTY, DOCUMENT, NIGHT_MODE, BACKLIGHT_HDR, BEACH, SNOW, SOFT_SKIN, GOURMET, PET, ANTI_MOTION, HAND_NIGHT, HIGH_SENSITIVITY, FIRE_WORKS};
        $VALUES = arrscene;
    }

    private Scene(int n2, int n3, String string2, FocusMode focusMode) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
        this.mFocusMode = focusMode;
    }

    private static Scene[] getExpectedOptions(String[] arrstring) {
        ArrayList arrayList = new ArrayList();
        if (arrstring != null) {
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)Scene.valueOf((Class)Scene.class, (String)arrstring[i]));
            }
        } else {
            return Scene.values();
        }
        return (Scene[])arrayList.toArray((Object[])new Scene[0]);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Scene[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
        List<String> list = capabilityList.SCENE.get();
        if (list.isEmpty()) return (Scene[])arrayList.toArray((Object[])new Scene[0]);
        if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            arrayList.add((Object)OFF);
            return (Scene[])arrayList.toArray((Object[])new Scene[0]);
        }
        for (Scene scene : Scene.getExpectedOptions(capabilityList.UX_CAPABILITY.get().getSceneOptions(capturingMode.getType()))) {
            for (String string : list) {
                if (!scene.getValue().equals((Object)string)) continue;
                arrayList.add((Object)scene);
            }
        }
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 8) && HardwareCapability.getInstance().getMaxSoftSkinLevel(capturingMode.getCameraId()) > 0 && arrayList.contains((Object)PORTRAIT)) {
            arrayList.add((Object)SOFT_SKIN);
        }
        if (Scene.isSupportedBeachAndSnowIndividually()) return (Scene[])arrayList.toArray((Object[])new Scene[0]);
        int n = arrayList.indexOf((Object)SNOW);
        if (n == -1) return (Scene[])arrayList.toArray((Object[])new Scene[0]);
        arrayList.remove((Object)SNOW);
        arrayList.remove((Object)BEACH);
        arrayList.add(n, (Object)BEACH_SNOW);
        return (Scene[])arrayList.toArray((Object[])new Scene[0]);
    }

    private static boolean isSupportedBeachAndSnowIndividually() {
        boolean bl = HardwareCapability.getCapability((int)0).SCENE.get().contains((Object)PET.getValue());
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        return bl2;
    }

    public static Scene valueOf(String string) {
        return (Scene)Enum.valueOf((Class)Scene.class, (String)string);
    }

    public static Scene[] values() {
        return (Scene[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((Scene)this);
    }

    public FocusMode getFocusMode() {
        return this.mFocusMode;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.SCENE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361809;
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

