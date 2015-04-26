/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.capturefeedback;

import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;

public interface CaptureFeedback {
    public void onPause();

    public void onResume();

    public void release();

    public void start(CaptureFeedbackAnimation var1);
}

