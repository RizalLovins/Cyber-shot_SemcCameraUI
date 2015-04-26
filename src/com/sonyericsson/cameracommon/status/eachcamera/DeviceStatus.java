/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class DeviceStatus
extends EnumValue<Value>
implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.POWER_OFF;
    public static final String KEY = "device_status";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    public DeviceStatus(Value value) {
        super(value);
    }

    @Override
    public String getKey() {
        return "device_status";
    }

    @Override
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }

    public static final class Value
    extends Enum<Value> {
        private static final /* synthetic */ Value[] $VALUES;
        public static final /* enum */ Value PICTURE_TAKING;
        public static final /* enum */ Value PICTURE_TAKING_DURING_VIDEO_RECORDING;
        public static final /* enum */ Value POWER_OFF;
        public static final /* enum */ Value POWER_ON;
        public static final /* enum */ Value STILL_PREVIEW;
        public static final /* enum */ Value VIDEO_PREVIEW;
        public static final /* enum */ Value VIDEO_RECORDING;
        private final String mStringExpression;

        static {
            POWER_ON = new Value("power_on");
            POWER_OFF = new Value("power_off");
            STILL_PREVIEW = new Value("still_preview");
            VIDEO_PREVIEW = new Value("video_preview");
            PICTURE_TAKING = new Value("picture_taking");
            VIDEO_RECORDING = new Value("video_recording");
            PICTURE_TAKING_DURING_VIDEO_RECORDING = new Value("picture_taking_during_video_recording");
            Value[] arrvalue = new Value[]{POWER_ON, POWER_OFF, STILL_PREVIEW, VIDEO_PREVIEW, PICTURE_TAKING, VIDEO_RECORDING, PICTURE_TAKING_DURING_VIDEO_RECORDING};
            $VALUES = arrvalue;
        }

        private Value(String string2) {
            super(string, n);
            this.mStringExpression = string2;
        }

        public static Value valueOf(String string) {
            return (Value)Enum.valueOf((Class)Value.class, (String)string);
        }

        public static Value[] values() {
            return (Value[])$VALUES.clone();
        }

        public String toString() {
            return this.mStringExpression;
        }
    }

}

