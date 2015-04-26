/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
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
import com.sonyericsson.android.camera.util.capability.UxOptions;
import java.util.ArrayList;
import java.util.List;

public final class FocusMode
extends Enum<FocusMode>
implements ParameterValue {
    private static final /* synthetic */ FocusMode[] $VALUES;
    public static final /* enum */ FocusMode FACE_DETECTION;
    public static final /* enum */ FocusMode FIXED;
    public static final /* enum */ FocusMode INFINITY;
    public static final /* enum */ FocusMode MULTI;
    public static final /* enum */ FocusMode OBJECT_TRACKING;
    public static final /* enum */ FocusMode SINGLE;
    public static final /* enum */ FocusMode TOUCH_FOCUS;
    private static final int sParameterTextId = 2131361829;
    private final String mFocusArea;
    private final int mIconId;
    private final boolean mSuccessSound;
    private final int mTextId;
    private String mValue;
    private String mValueForVideo;

    static {
        SINGLE = new FocusMode(-1, 2131361830, "continuous-picture", "continuous-video", "center", true);
        MULTI = new FocusMode(-1, 2131361831, "macro", "macro", "multi", true);
        FIXED = new FocusMode(-1, 2131361830, "fixed", "fixed", "center", false);
        FACE_DETECTION = new FocusMode(-1, 2131361833, "continuous-picture", "continuous-video", "center", true);
        TOUCH_FOCUS = new FocusMode(-1, 2131361834, "continuous-picture", "continuous-video", "center", true);
        INFINITY = new FocusMode(-1, 2131361830, "infinity", "infinity", "center", true);
        OBJECT_TRACKING = new FocusMode(-1, 2131361996, "continuous-picture", "continuous-video", "center", true);
        FocusMode[] arrfocusMode = new FocusMode[]{SINGLE, MULTI, FIXED, FACE_DETECTION, TOUCH_FOCUS, INFINITY, OBJECT_TRACKING};
        $VALUES = arrfocusMode;
    }

    private FocusMode(int n2, int n3, String string2, String string3, String string4, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
        this.mValueForVideo = string3;
        this.mFocusArea = string4;
        this.mSuccessSound = bl;
    }

    public static FocusMode getDefaultValue(CapturingMode capturingMode) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                return FACE_DETECTION;
            }
            case 1: 
            case 2: {
                return FACE_DETECTION;
            }
            case 3: {
                return TOUCH_FOCUS;
            }
            case 4: 
            case 5: 
        }
        return FIXED;
    }

    private static FocusMode[] getExpectedOptions(String[] arrstring) {
        ArrayList arrayList = new ArrayList();
        if (arrstring != null) {
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)FocusMode.valueOf((Class)FocusMode.class, (String)arrstring[i]));
            }
        } else {
            return FocusMode.values();
        }
        return (FocusMode[])arrayList.toArray((Object[])new FocusMode[0]);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static FocusMode[] getOptions(CapturingMode capturingMode) {
        ArrayList arrayList = new ArrayList();
        CapabilityList capabilityList = HardwareCapability.getCapability(capturingMode.getCameraId());
        List<String> list = capabilityList.FOCUS_MODE.get();
        List<String> list2 = capabilityList.FOCUS_AREA.get();
        if (!list.isEmpty()) {
            FocusMode[] arrfocusMode = capturingMode == CapturingMode.SCENE_RECOGNITION ? new FocusMode[]{FACE_DETECTION} : FocusMode.getExpectedOptions(capabilityList.UX_CAPABILITY.get().getFocusModeOptions(capturingMode));
            ArrayList arrayList2 = new ArrayList();
            for (FocusMode focusMode : arrfocusMode) {
                for (String string : list) {
                    if (!focusMode.getValue().equals((Object)string)) continue;
                    arrayList2.add((Object)focusMode);
                }
            }
            if (list2.isEmpty()) {
                arrayList = arrayList2;
            } else {
                block2 : for (FocusMode focusMode2 : arrayList2) {
                    for (String string : list2) {
                        if (!focusMode2.getFocusArea().equals((Object)string)) continue;
                        arrayList.add((Object)focusMode2);
                        continue block2;
                    }
                }
            }
            if (capabilityList.MAX_NUM_FACE.get() < 1) {
                FocusMode.remove(FACE_DETECTION, arrayList);
            }
            if (capabilityList.MAX_NUM_FOCUS_AREA.get() < 1) {
                FocusMode.remove(TOUCH_FOCUS, arrayList);
            }
            if (!capabilityList.OBJECT_TRACKING.get().booleanValue()) {
                FocusMode.remove(OBJECT_TRACKING, arrayList);
            }
        }
        return (FocusMode[])arrayList.toArray((Object[])new FocusMode[0]);
    }

    private static void remove(FocusMode focusMode, List<FocusMode> list) {
        if (list.contains((Object)focusMode)) {
            list.remove((Object)focusMode);
        }
    }

    public static void updateValue(int n, List<String> list) {
        if (!(n != 0 || list.contains((Object)SINGLE.getValue()))) {
            FocusMode.SINGLE.mValue = "auto";
        }
    }

    public static FocusMode valueOf(String string) {
        return (FocusMode)Enum.valueOf((Class)FocusMode.class, (String)string);
    }

    public static FocusMode[] values() {
        return (FocusMode[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((FocusMode)this);
    }

    public String getFocusArea() {
        return this.mFocusArea;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.FOCUS_MODE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361829;
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

    public String getValueForVideo() {
        return this.mValueForVideo;
    }

    public boolean isSuccessSound() {
        return this.mSuccessSound;
    }

}

