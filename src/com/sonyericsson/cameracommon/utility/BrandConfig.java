/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.os.Build;

public class BrandConfig {
    private static final String VERIZON_BRAND = "verizon";

    public static boolean isVerizonBrand() {
        return "verizon".equals((Object)Build.BRAND);
    }
}

