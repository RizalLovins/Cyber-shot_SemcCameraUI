/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.capability;

import com.sonyericsson.cameracommon.utility.ClassDefinitionChecker;

public class VideoCodecCapability {
    public static boolean isH265Supported() {
        return ClassDefinitionChecker.isMediaRecorderH265Supported();
    }
}

