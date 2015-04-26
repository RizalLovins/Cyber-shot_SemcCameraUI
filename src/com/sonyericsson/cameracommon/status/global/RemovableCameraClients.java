/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.status.global;

import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;
import com.sonyericsson.cameracommon.status.IntegerValue;

public class RemovableCameraClients
extends IntegerValue
implements GlobalCameraStatusValue {
    public static final int DEFAULT_VALUE = 0;
    public static final String KEY = "removable_camera_clients";
    private static int REQUIRED_PROVIDER_VERSION = 10;

    public RemovableCameraClients(int n) {
        super(n);
    }

    @Override
    public String getKey() {
        return "removable_camera_clients";
    }

    @Override
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}

