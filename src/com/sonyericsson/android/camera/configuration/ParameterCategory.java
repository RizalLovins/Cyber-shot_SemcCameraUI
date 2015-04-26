/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration;

public final class ParameterCategory
extends Enum<ParameterCategory> {
    private static final /* synthetic */ ParameterCategory[] $VALUES;
    public static final /* enum */ ParameterCategory CAPTURING_MODE;
    public static final /* enum */ ParameterCategory COMMON;

    static {
        COMMON = new ParameterCategory();
        CAPTURING_MODE = new ParameterCategory();
        ParameterCategory[] arrparameterCategory = new ParameterCategory[]{COMMON, CAPTURING_MODE};
        $VALUES = arrparameterCategory;
    }

    private ParameterCategory() {
        super(string, n);
    }

    public static ParameterCategory valueOf(String string) {
        return (ParameterCategory)Enum.valueOf((Class)ParameterCategory.class, (String)string);
    }

    public static ParameterCategory[] values() {
        return (ParameterCategory[])$VALUES.clone();
    }
}

