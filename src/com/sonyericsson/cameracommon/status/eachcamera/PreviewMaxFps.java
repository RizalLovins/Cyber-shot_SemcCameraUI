/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.IntegerValue;

public class PreviewMaxFps
extends IntegerValue
implements EachCameraStatusValue {
    public static final int DEFAULT_VALUE = 0;
    public static final String KEY = "preview_max_fps";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    public PreviewMaxFps(int n) {
        super(n);
    }

    @Override
    public String getKey() {
        return "preview_max_fps";
    }

    @Override
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}

