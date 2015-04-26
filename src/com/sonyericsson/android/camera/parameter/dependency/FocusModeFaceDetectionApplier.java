/*
 * Decompiled with CFR 0_92.
 */
package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;

public class FocusModeFaceDetectionApplier
extends DependencyApplier {
    @Override
    public void apply(CapturingModeParams capturingModeParams) {
        ParameterUtil.reset(capturingModeParams.mFaceIdentify);
    }

    @Override
    public void reset(CapturingModeParams capturingModeParams) {
    }
}

