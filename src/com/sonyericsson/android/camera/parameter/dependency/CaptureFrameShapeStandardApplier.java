/*
 * Decompiled with CFR 0_92.
 */
package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CaptureFrameShape;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;

public class CaptureFrameShapeStandardApplier
extends DependencyApplier {
    @Override
    public void apply(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION || capturingModeParams.mCapturingMode.get() == CapturingMode.SUPERIOR_FRONT) {
            ParameterUtil.forceChange(capturingModeParams.mResolution, CaptureFrameShape.STANDARD.getResolution(capturingModeParams.mCapturingMode.get().getCameraId()));
        }
    }

    @Override
    public void reset(CapturingModeParams capturingModeParams) {
        capturingModeParams.mResolution.reset();
    }
}

