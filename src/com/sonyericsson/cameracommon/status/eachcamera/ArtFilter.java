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

public class ArtFilter
extends EnumValue<Value>
implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "art_filter";
    private static final int REQUIRED_VERSION = 1;

    public ArtFilter(Value value) {
        super(value);
    }

    @Override
    public String getKey() {
        return "art_filter";
    }

    @Override
    public int minRequiredVersion() {
        return 1;
    }

    public static final class Value
    extends Enum<Value> {
        private static final /* synthetic */ Value[] $VALUES;
        public static final /* enum */ Value MULTI;
        public static final /* enum */ Value OFF;
        public static final /* enum */ Value SINGLE;
        private final String mStringExpression;

        static {
            SINGLE = new Value("single");
            MULTI = new Value("multi");
            OFF = new Value("off");
            Value[] arrvalue = new Value[]{SINGLE, MULTI, OFF};
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

