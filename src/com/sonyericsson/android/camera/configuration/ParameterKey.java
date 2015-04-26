/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.cameracommon.utility.ResourceUtil;

public final class ParameterKey
extends Enum<ParameterKey> {
    private static final /* synthetic */ ParameterKey[] $VALUES;
    public static final /* enum */ ParameterKey AUTO_REVIEW = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362000);
    public static final /* enum */ ParameterKey AUTO_UPLOAD;
    public static final /* enum */ ParameterKey BURST_SHOT;
    public static final /* enum */ ParameterKey CAPTURE_FRAME_SHAPE;
    public static final /* enum */ ParameterKey CAPTURING_MODE;
    public static final /* enum */ ParameterKey DESTINATION_TO_SAVE;
    public static final /* enum */ ParameterKey EV;
    public static final /* enum */ ParameterKey FACE_IDENTIFY;
    public static final /* enum */ ParameterKey FACING;
    public static final /* enum */ ParameterKey FAST_CAPTURE;
    public static final /* enum */ ParameterKey FLASH;
    public static final /* enum */ ParameterKey FOCUS_MODE;
    public static final /* enum */ ParameterKey GEO_TAG;
    public static final /* enum */ ParameterKey HDR;
    public static final /* enum */ ParameterKey ISO;
    public static final /* enum */ ParameterKey METERING;
    public static final /* enum */ ParameterKey MICROPHONE;
    public static final /* enum */ ParameterKey PHOTO_LIGHT;
    public static final /* enum */ ParameterKey RESOLUTION;
    public static final /* enum */ ParameterKey SCENE;
    public static final /* enum */ ParameterKey SELF_TIMER;
    public static final /* enum */ ParameterKey SHUTTER_SOUND;
    public static final /* enum */ ParameterKey SMILE_CAPTURE;
    public static final /* enum */ ParameterKey SOFT_SKIN;
    public static final /* enum */ ParameterKey STABILIZER;
    public static final /* enum */ ParameterKey SUPER_RESOLUTION;
    public static final /* enum */ ParameterKey TOUCH_BLOCK;
    public static final /* enum */ ParameterKey TOUCH_CAPTURE;
    public static final /* enum */ ParameterKey VIDEO_AUTO_REVIEW;
    public static final /* enum */ ParameterKey VIDEO_HDR;
    public static final /* enum */ ParameterKey VIDEO_SELF_TIMER;
    public static final /* enum */ ParameterKey VIDEO_SIZE;
    public static final /* enum */ ParameterKey VIDEO_SMILE_CAPTURE;
    public static final /* enum */ ParameterKey VIDEO_STABILIZER;
    public static final /* enum */ ParameterKey VOLUME_KEY;
    public static final /* enum */ ParameterKey WHITE_BALANCE;
    private final ParameterCategory mCategory;
    private final boolean mIsCommon;
    private boolean mIsSaved;
    private ParameterSelectability mSelectability;
    private final int mTitleTextId;

    static {
        VIDEO_AUTO_REVIEW = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362000);
        AUTO_UPLOAD = new ParameterKey(false, true, ParameterCategory.COMMON, 2131362012);
        BURST_SHOT = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362022);
        CAPTURE_FRAME_SHAPE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362088);
        CAPTURING_MODE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361966);
        DESTINATION_TO_SAVE = new ParameterKey(true, true, ParameterCategory.COMMON, 2131361928);
        EV = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361923);
        FACE_IDENTIFY = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362041);
        FACING = new ParameterKey(false, false, ParameterCategory.CAPTURING_MODE, 2131361997);
        FAST_CAPTURE = new ParameterKey(true, true, ParameterCategory.COMMON, 2131361929);
        FLASH = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361921);
        FOCUS_MODE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361922);
        GEO_TAG = new ParameterKey(true, true, ParameterCategory.COMMON, 2131361927);
        HDR = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361959);
        ISO = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362223);
        METERING = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361925);
        MICROPHONE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361930);
        PHOTO_LIGHT = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362016);
        RESOLUTION = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361920);
        SCENE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361976);
        SELF_TIMER = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361822);
        SHUTTER_SOUND = new ParameterKey(true, true, ParameterCategory.COMMON, 2131362102);
        SMILE_CAPTURE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361977);
        SOFT_SKIN = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361990);
        VIDEO_STABILIZER = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361980);
        STABILIZER = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361926);
        SUPER_RESOLUTION = new ParameterKey(false, false, ParameterCategory.CAPTURING_MODE, -1);
        TOUCH_CAPTURE = new ParameterKey(true, true, ParameterCategory.COMMON, 2131362005);
        VIDEO_HDR = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362011);
        VIDEO_SELF_TIMER = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361822);
        VIDEO_SIZE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361957);
        VIDEO_SMILE_CAPTURE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131362033);
        VOLUME_KEY = new ParameterKey(true, true, ParameterCategory.COMMON, 2131362046);
        WHITE_BALANCE = new ParameterKey(true, false, ParameterCategory.CAPTURING_MODE, 2131361924);
        TOUCH_BLOCK = new ParameterKey(false, true, ParameterCategory.COMMON, -1);
        ParameterKey[] arrparameterKey = new ParameterKey[]{AUTO_REVIEW, VIDEO_AUTO_REVIEW, AUTO_UPLOAD, BURST_SHOT, CAPTURE_FRAME_SHAPE, CAPTURING_MODE, DESTINATION_TO_SAVE, EV, FACE_IDENTIFY, FACING, FAST_CAPTURE, FLASH, FOCUS_MODE, GEO_TAG, HDR, ISO, METERING, MICROPHONE, PHOTO_LIGHT, RESOLUTION, SCENE, SELF_TIMER, SHUTTER_SOUND, SMILE_CAPTURE, SOFT_SKIN, VIDEO_STABILIZER, STABILIZER, SUPER_RESOLUTION, TOUCH_CAPTURE, VIDEO_HDR, VIDEO_SELF_TIMER, VIDEO_SIZE, VIDEO_SMILE_CAPTURE, VOLUME_KEY, WHITE_BALANCE, TOUCH_BLOCK};
        $VALUES = arrparameterKey;
    }

    private ParameterKey(boolean bl, boolean bl2, ParameterCategory parameterCategory, int n2) {
        super(string, n);
        this.mIsSaved = bl;
        this.mIsCommon = bl2;
        this.mCategory = parameterCategory;
        this.mTitleTextId = n2;
    }

    public static ParameterKey valueOf(String string) {
        return (ParameterKey)Enum.valueOf((Class)ParameterKey.class, (String)string);
    }

    public static ParameterKey[] values() {
        return (ParameterKey[])$VALUES.clone();
    }

    public ParameterCategory getCategory() {
        return this.mCategory;
    }

    public ParameterSelectability getSelectability() {
        return this.mSelectability;
    }

    public String getTitleText(Context context) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[this.ordinal()]) {
            default: {
                return null;
            }
            case 3: 
        }
        return ResourceUtil.getApplicationLabel(context, "com.sonymobile.touchblocker");
    }

    public int getTitleTextId(CapturingMode capturingMode) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[this.ordinal()]) {
            default: {
                return this.mTitleTextId;
            }
            case 1: {
                return Flash.getParameterKeyTitleTextId();
            }
            case 2: 
        }
        return VideoStabilizer.getParameterKeyTitleText(capturingMode.getCameraId());
    }

    public boolean isCommon() {
        return this.mIsCommon;
    }

    public boolean isInvalid() {
        if (this.getSelectability() == ParameterSelectability.INVALID) {
            return true;
        }
        return false;
    }

    public boolean isSaved() {
        return this.mIsSaved;
    }

    public boolean isSelectable() {
        if (this.getSelectability() == ParameterSelectability.SELECTABLE) {
            return true;
        }
        return false;
    }

    public void setSaved(boolean bl) {
        this.mIsSaved = bl;
    }

    public void setSelectability(ParameterSelectability parameterSelectability) {
        this.mSelectability = parameterSelectability;
    }

}

