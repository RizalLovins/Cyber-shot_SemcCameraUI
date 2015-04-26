/*
 * Decompiled with CFR 0_92.
 */
package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;

public class VideoSize60FPSApplier
extends DependencyApplier {
    @Override
    public void apply(CapturingModeParams capturingModeParams) {
        ParameterUtil.unavailable(capturingModeParams.mScene, Scene.OFF);
        if (!capturingModeParams.isOneShotVideo()) {
            ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.OFF);
        }
        if (VideoStabilizer.isSteadyShotSupported(capturingModeParams.getActionMode().mCameraId, capturingModeParams.mVideoSize.get())) {
            if (capturingModeParams.mVideoStabilizer.get() == VideoStabilizer.INTELLIGENT_ACTIVE) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoStabilizer, VideoStabilizer.STEADY_SHOT);
                return;
            }
            ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
            return;
        }
        ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
    }

    @Override
    public void reset(CapturingModeParams capturingModeParams) {
        ParameterUtil.reset(capturingModeParams.mScene);
        if (!capturingModeParams.isOneShotVideo()) {
            ParameterUtil.reset(capturingModeParams.mVideoHdr);
        }
    }
}

