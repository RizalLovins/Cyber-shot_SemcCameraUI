/*
 * Decompiled with CFR 0_92.
 */
package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;

public class VideoSmileCaptureOnApplier
extends DependencyApplier {
    @Override
    public void apply(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO && capturingModeParams.mScene.get() == Scene.OFF) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
    }

    @Override
    public void reset(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO && capturingModeParams.mScene.get() == Scene.OFF) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
}

