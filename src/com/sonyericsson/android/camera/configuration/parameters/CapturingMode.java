/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.ArrayList;
import java.util.List;

public final class CapturingMode
extends Enum<CapturingMode>
implements ParameterValue {
    private static final /* synthetic */ CapturingMode[] $VALUES;
    public static final /* enum */ CapturingMode FRONT_PHOTO;
    public static final /* enum */ CapturingMode FRONT_VIDEO;
    public static final /* enum */ CapturingMode NORMAL;
    public static final /* enum */ CapturingMode PHOTO;
    public static final /* enum */ CapturingMode SCENE_RECOGNITION;
    public static final /* enum */ CapturingMode SUPERIOR_FRONT;
    private static final String TAG;
    public static final /* enum */ CapturingMode UNKNOWN;
    public static final /* enum */ CapturingMode VIDEO;
    private static final int sParameterTextId = 2131361797;
    private static final CapturingMode[] sPhotoOptions;
    private final int mCameraId;
    private final int mIconId;
    private final int mTextId;
    private final int mType;

    static {
        UNKNOWN = new CapturingMode(-1, -1, 0, 0);
        SCENE_RECOGNITION = new CapturingMode(2130837522, 2131361968, 1, 0);
        NORMAL = new CapturingMode(2130837521, 2131362014, 1, 0);
        VIDEO = new CapturingMode(2130837521, 2131362014, 2, 0);
        SUPERIOR_FRONT = new CapturingMode(-1, -1, 1, 1);
        FRONT_PHOTO = new CapturingMode(-1, -1, 1, 1);
        FRONT_VIDEO = new CapturingMode(-1, -1, 2, 1);
        PHOTO = new CapturingMode(-1, -1, 1, 0);
        CapturingMode[] arrcapturingMode = new CapturingMode[]{UNKNOWN, SCENE_RECOGNITION, NORMAL, VIDEO, SUPERIOR_FRONT, FRONT_PHOTO, FRONT_VIDEO, PHOTO};
        $VALUES = arrcapturingMode;
        CapturingMode[] arrcapturingMode2 = new CapturingMode[]{SCENE_RECOGNITION, NORMAL};
        sPhotoOptions = arrcapturingMode2;
        TAG = CapturingMode.class.getSimpleName();
    }

    private CapturingMode(int n2, int n3, int n4, int n5) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mType = n4;
        this.mCameraId = n5;
    }

    public static CapturingMode convertFrom(String string, CapturingMode capturingMode) {
        try {
            CapturingMode capturingMode2 = CapturingMode.valueOf(string);
            return capturingMode2;
        }
        catch (IllegalArgumentException var2_3) {
            CameraLogger.w(TAG, "Mode[" + string + "] is not supported.");
            return capturingMode;
        }
    }

    public static CapturingMode[] getPhotoOptions() {
        return (CapturingMode[])sPhotoOptions.clone();
    }

    public static List<CapturingMode> getValidOptions(CameraActivity cameraActivity) {
        ArrayList arrayList = new ArrayList();
        if (cameraActivity.isPhotoIn()) {
            arrayList.add((Object)NORMAL);
        }
        if (cameraActivity.isVideoIn()) {
            arrayList.add((Object)VIDEO);
        }
        if (HardwareCapability.getInstance().isSceneRecognitionSupported(0)) {
            arrayList.add((Object)SCENE_RECOGNITION);
        }
        if (HardwareCapability.isFrontCameraSupported()) {
            arrayList.add((Object)FRONT_PHOTO);
        }
        if (HardwareCapability.isFrontCameraSupported() && HardwareCapability.getInstance().isSceneRecognitionSupported(1)) {
            arrayList.add((Object)SUPERIOR_FRONT);
        }
        return arrayList;
    }

    public static CapturingMode valueOf(String string) {
        return (CapturingMode)Enum.valueOf((Class)CapturingMode.class, (String)string);
    }

    public static CapturingMode[] values() {
        return (CapturingMode[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((CapturingMode)this);
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.CAPTURING_MODE;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361797;
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

    public int getType() {
        return this.mType;
    }

    @Override
    public String getValue() {
        return this.toString();
    }

    public boolean isFront() {
        if (this.mCameraId == 1) {
            return true;
        }
        return false;
    }

    public boolean isMainPhoto() {
        if (this.getType() == 1 && this.getCameraId() == 0) {
            return true;
        }
        return false;
    }

    public boolean isManual() {
        if (this == NORMAL || this == PHOTO || this == VIDEO) {
            return true;
        }
        return false;
    }

    public boolean isSuperiorAuto() {
        if (this == SCENE_RECOGNITION || this == SUPERIOR_FRONT) {
            return true;
        }
        return false;
    }
}

