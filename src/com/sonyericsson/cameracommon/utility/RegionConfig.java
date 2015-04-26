/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import com.sonyericsson.cameracommon.utility.CommonUtility;

public class RegionConfig {
    private static final String CHINA_REGION_PACKAGE = "com.sonymobile.cta";

    public static boolean isChinaRegion(Context context) {
        return CommonUtility.isPackageExist("com.sonymobile.cta", context);
    }
}

