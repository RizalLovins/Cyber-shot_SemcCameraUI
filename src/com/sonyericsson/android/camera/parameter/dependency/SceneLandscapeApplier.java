/*
 * Decompiled with CFR 0_92.
 */
package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.Stabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;

public class SceneLandscapeApplier
extends DependencyApplier {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void apply(CapturingModeParams capturingModeParams) {
        ParameterUtil.unavailable(capturingModeParams.mEv, Ev.ZERO);
        ParameterUtil.unavailable(capturingModeParams.mWhiteBalance, WhiteBalance.AUTO);
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
            ParameterUtil.unavailable(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        } else {
            if (!capturingModeParams.mFlash.hasChanged()) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mFlash, Flash.OFF);
            }
            ParameterUtil.unavailable(capturingModeParams.mSmileCapture, SmileCapture.OFF);
            ParameterUtil.unavailable(capturingModeParams.mFocusMode, FocusMode.INFINITY);
        }
        ParameterUtil.unavailable(capturingModeParams.mHdr, Hdr.HDR_OFF);
        ParameterUtil.unavailable(capturingModeParams.mIso, Iso.ISO_AUTO);
        ParameterUtil.unavailable(capturingModeParams.mMetering, Metering.getDefaultValue(capturingModeParams.mCapturingMode.get()));
        ParameterUtil.unavailable(capturingModeParams.mStabilizer, Stabilizer.OFF);
        ParameterUtil.unavailable(capturingModeParams.mFaceIdentify, FaceIdentify.OFF);
        ParameterUtil.unavailable(capturingModeParams.mVideoHdr, VideoHdr.OFF);
        ParameterUtil.unavailable(capturingModeParams.mVideoStabilizer, VideoStabilizer.getRecommendedVideoStabilizerValue(capturingModeParams.getActionMode().mCameraId, capturingModeParams.mVideoSize.get()));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void reset(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() != CapturingMode.VIDEO) {
            ParameterUtil.reset(capturingModeParams.mFlash);
        }
        ParameterUtil.reset(capturingModeParams.mEv);
        ParameterUtil.reset(capturingModeParams.mWhiteBalance);
        ParameterUtil.reset(capturingModeParams.mSmileCapture);
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        } else {
            ParameterUtil.reset(capturingModeParams.mFocusMode, FocusMode.SINGLE);
        }
        ParameterUtil.reset(capturingModeParams.mHdr);
        ParameterUtil.reset(capturingModeParams.mIso);
        ParameterUtil.reset(capturingModeParams.mMetering);
        ParameterUtil.reset(capturingModeParams.mStabilizer);
        ParameterUtil.reset(capturingModeParams.mFaceIdentify);
        ParameterUtil.reset(capturingModeParams.mVideoHdr);
        ParameterUtil.reset(capturingModeParams.mVideoStabilizer);
    }
}

