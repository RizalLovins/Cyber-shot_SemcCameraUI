/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.configuration.parameters;

import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.CapabilityList;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.device.CameraExtensionVersion;
import java.util.ArrayList;
import java.util.List;

public final class VideoStabilizer
extends Enum<VideoStabilizer>
implements ParameterValue {
    private static final /* synthetic */ VideoStabilizer[] $VALUES;
    public static final /* enum */ VideoStabilizer INTELLIGENT_ACTIVE;
    public static final /* enum */ VideoStabilizer OFF;
    public static final /* enum */ VideoStabilizer ON;
    public static final /* enum */ VideoStabilizer STEADY_SHOT;
    private static final int TEXT_ID_SS = 2131361980;
    private static final int TEXT_ID_VS = 2131362032;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;

    static {
        STEADY_SHOT = new VideoStabilizer(-1, 2131362143, "on-steady-shot");
        INTELLIGENT_ACTIVE = new VideoStabilizer(-1, 2131362104, "on-intelligent-active");
        ON = new VideoStabilizer(-1, 2131361806, "on");
        OFF = new VideoStabilizer(-1, 2131361807, "off");
        VideoStabilizer[] arrvideoStabilizer = new VideoStabilizer[]{STEADY_SHOT, INTELLIGENT_ACTIVE, ON, OFF};
        $VALUES = arrvideoStabilizer;
    }

    private VideoStabilizer(int n2, int n3, String string2) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mValue = string2;
    }

    public static VideoStabilizer[] getOptions(int n) {
        return VideoStabilizer.getVideoStabilizerOptions(n);
    }

    public static int getParameterKeyTitleText(int n) {
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) {
            return 2131361980;
        }
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isSupported()) {
            if (HardwareCapability.getCapability((int)n).VIDEO_STABILIZER_TYPE.get().contains((Object)"steady-shot")) {
                return 2131361980;
            }
            return 2131362032;
        }
        return 2131362032;
    }

    public static VideoStabilizer getRecommendedVideoStabilizerValue(int n, VideoSize videoSize) {
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) {
            boolean bl = VideoStabilizer.isSteadyShotSupported(n, videoSize);
            if (VideoStabilizer.isIntelligentActiveSupported(n, videoSize)) {
                return INTELLIGENT_ACTIVE;
            }
            if (bl) {
                return STEADY_SHOT;
            }
            return OFF;
        }
        return ON;
    }

    public static VideoStabilizer[] getVideoStabilizerOptions(int n) {
        CapabilityList capabilityList = HardwareCapability.getCapability(n);
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) {
            ArrayList arrayList = new ArrayList();
            if (capabilityList.VIDEO_STABILIZER.get().contains((Object)"on-intelligent-active")) {
                arrayList.add((Object)INTELLIGENT_ACTIVE);
            }
            if (capabilityList.VIDEO_STABILIZER.get().contains((Object)"on-steady-shot")) {
                arrayList.add((Object)STEADY_SHOT);
            }
            if (arrayList.size() == 0 && capabilityList.VIDEO_STABILIZER.get().contains((Object)"on")) {
                arrayList.add((Object)ON);
            }
            arrayList.add((Object)OFF);
            return (VideoStabilizer[])arrayList.toArray((Object[])new VideoStabilizer[0]);
        }
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isSupported() && !capabilityList.VIDEO_STABILIZER.get().isEmpty()) {
            VideoStabilizer[] arrvideoStabilizer = new VideoStabilizer[]{ON, OFF};
            return arrvideoStabilizer;
        }
        return new VideoStabilizer[0];
    }

    public static boolean isIntelligentActive(VideoStabilizer videoStabilizer) {
        if (videoStabilizer == INTELLIGENT_ACTIVE) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isIntelligentActiveSupported(int n, VideoSize videoSize) {
        if (!VideoStabilizer.isValidWhenVideoSizeIs(videoSize)) {
            return false;
        }
        CapabilityList capabilityList = HardwareCapability.getCapability(n);
        if (!HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) return false;
        if (videoSize == VideoSize.FULL_HD_60FPS) return false;
        return VideoStabilizer.isMaxSizeLargerThanOrEqualToVideoSize(capabilityList.MAX_INTELLIGENT_ACTIVE_SIZE.get(), videoSize);
    }

    private static boolean isMaxSizeLargerThanOrEqualToVideoSize(Rect rect, VideoSize videoSize) {
        if (rect != null && rect.width() >= videoSize.getVideoRect().width() && rect.height() >= videoSize.getVideoRect().height()) {
            return true;
        }
        return false;
    }

    public static boolean isSteadyShotSupported(int n, VideoSize videoSize) {
        if (!VideoStabilizer.isValidWhenVideoSizeIs(videoSize)) {
            return false;
        }
        CapabilityList capabilityList = HardwareCapability.getCapability(n);
        if (HardwareCapability.getInstance().getCameraExtensionVersion().isLaterThanOrEqualTo(1, 7)) {
            return VideoStabilizer.isMaxSizeLargerThanOrEqualToVideoSize(capabilityList.MAX_STEADY_SHOT_SIZE.get(), videoSize);
        }
        if (capabilityList.VIDEO_STABILIZER_TYPE.get().contains((Object)"steady-shot")) {
            return VideoStabilizer.isMaxSizeLargerThanOrEqualToVideoSize(capabilityList.MAX_VIDEO_STABILIZER_SIZE_FOR_LEGACY.get(), videoSize);
        }
        if (capabilityList.VIDEO_STABILIZER.get().contains((Object)"on")) {
            return true;
        }
        return false;
    }

    private static boolean isValidWhenVideoSizeIs(VideoSize videoSize) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()]) {
            default: {
                return true;
            }
            case 1: 
            case 2: 
        }
        return false;
    }

    public static VideoStabilizer valueOf(String string) {
        return (VideoStabilizer)Enum.valueOf((Class)VideoStabilizer.class, (String)string);
    }

    public static VideoStabilizer[] values() {
        return (VideoStabilizer[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((VideoStabilizer)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.VIDEO_STABILIZER;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131361980;
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

