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

public class VideoStabilizerStatus
extends EnumValue<Value>
implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "video_stabilizer";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    public VideoStabilizerStatus(Value value) {
        super(value);
    }

    public static VideoStabilizerStatus fromCameraParameter(String string) {
        if ("on".equals((Object)string)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if ("on-steady-shot".equals((Object)string)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if ("on-intelligent-active".equals((Object)string)) {
            return new VideoStabilizerStatus(Value.INTELLIGENT_ACTIVE);
        }
        return new VideoStabilizerStatus(Value.OFF);
    }

    @Override
    public String getKey() {
        return "video_stabilizer";
    }

    @Override
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }

    public static final class Value
    extends Enum<Value> {
        private static final /* synthetic */ Value[] $VALUES;
        public static final /* enum */ Value INTELLIGENT_ACTIVE;
        public static final /* enum */ Value OFF;
        public static final /* enum */ Value ON;
        private final String mStringExpression;

        static {
            ON = new Value("on");
            OFF = new Value("off");
            INTELLIGENT_ACTIVE = new Value("intelligent_active");
            Value[] arrvalue = new Value[]{ON, OFF, INTELLIGENT_ACTIVE};
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

