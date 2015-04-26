/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.parameters.AutoReview;
import com.sonyericsson.android.camera.configuration.parameters.BurstShot;
import com.sonyericsson.android.camera.configuration.parameters.CaptureFrameShape;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.Facing;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Microphone;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.SmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.Stabilizer;
import com.sonyericsson.android.camera.configuration.parameters.SuperResolution;
import com.sonyericsson.android.camera.configuration.parameters.VideoAutoReview;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoSmileCapture;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.SharedPreferencesUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CapturingModeParams {
    private ActionMode mActionMode;
    public ParameterValueHolder<AutoReview> mAutoReview;
    public ParameterValueHolder<BurstShot> mBurst;
    public ParameterValueHolder<CaptureFrameShape> mCaptureFrameShape;
    public ParameterValueHolder<CapturingMode> mCapturingMode;
    private Configurations mConfig;
    public ParameterValueHolder<Ev> mEv;
    public ParameterValueHolder<FaceIdentify> mFaceIdentify;
    public ParameterValueHolder<Facing> mFacing;
    public ParameterValueHolder<Flash> mFlash;
    public ParameterValueHolder<FocusMode> mFocusMode;
    public ParameterValueHolder<Hdr> mHdr;
    private boolean mIsOneShot;
    private boolean mIsOneShotVideo;
    public ParameterValueHolder<Iso> mIso;
    public ParameterValueHolder<Metering> mMetering;
    public ParameterValueHolder<Microphone> mMicrophone;
    public ParameterValueHolder<PhotoLight> mPhotoLight;
    public ParameterValueHolder<Resolution> mResolution;
    public ParameterValueHolder<Scene> mScene;
    public ParameterValueHolder<SelfTimer> mSelfTimer;
    public ParameterValueHolder<SmileCapture> mSmileCapture;
    public ParameterValueHolder<SoftSkin> mSoftSkin;
    public ParameterValueHolder<Stabilizer> mStabilizer;
    public ParameterValueHolder<SuperResolution> mSuperResolution;
    public ParameterValueHolder<VideoAutoReview> mVideoAutoReview;
    public ParameterValueHolder<VideoHdr> mVideoHdr;
    public ParameterValueHolder<VideoSelfTimer> mVideoSelfTimer;
    public ParameterValueHolder<VideoSize> mVideoSize;
    public ParameterValueHolder<VideoSmileCapture> mVideoSmileCapture;
    public ParameterValueHolder<VideoStabilizer> mVideoStabilizer;
    public ParameterValueHolder<WhiteBalance> mWhiteBalance;

    /*
     * Enabled aggressive block sorting
     */
    public CapturingModeParams(CapturingMode capturingMode) {
        this.mCapturingMode = new ParameterValueHolder<CapturingMode>(capturingMode);
        this.mScene = new ParameterValueHolder<Scene>(Scene.OFF);
        this.mFacing = capturingMode.getCameraId() == 1 ? new ParameterValueHolder<Facing>(Facing.FRONT) : new ParameterValueHolder<Facing>(Facing.BACK);
        this.mFlash = capturingMode.getType() == 1 ? new ParameterValueHolder<Flash>(Flash.getDefaultValue()) : new ParameterValueHolder<Flash>(Flash.OFF);
        this.mPhotoLight = new ParameterValueHolder<PhotoLight>(PhotoLight.OFF);
        this.mEv = new ParameterValueHolder<Ev>(Ev.ZERO);
        this.mWhiteBalance = new ParameterValueHolder<WhiteBalance>(WhiteBalance.AUTO);
        this.mResolution = new ParameterValueHolder<Resolution>(Resolution.getDefaultValue(capturingMode));
        this.mCaptureFrameShape = new ParameterValueHolder<CaptureFrameShape>(CaptureFrameShape.getDefaultValue(capturingMode));
        this.mSelfTimer = new ParameterValueHolder<SelfTimer>(SelfTimer.OFF);
        this.mSmileCapture = new ParameterValueHolder<SmileCapture>(SmileCapture.OFF);
        this.mFocusMode = new ParameterValueHolder<FocusMode>(FocusMode.getDefaultValue(capturingMode));
        this.mHdr = new ParameterValueHolder<Hdr>(Hdr.HDR_OFF);
        this.mIso = new ParameterValueHolder<Iso>(Iso.ISO_AUTO);
        this.mMetering = new ParameterValueHolder<Metering>(Metering.getDefaultValue(capturingMode));
        this.mSoftSkin = new ParameterValueHolder<SoftSkin>(SoftSkin.ON);
        this.mStabilizer = capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT ? new ParameterValueHolder<Stabilizer>(Stabilizer.AUTO) : new ParameterValueHolder<Stabilizer>(Stabilizer.OFF);
        this.mAutoReview = new ParameterValueHolder<AutoReview>(AutoReview.OFF);
        this.mBurst = new ParameterValueHolder<BurstShot>(BurstShot.OFF);
        this.mSuperResolution = new ParameterValueHolder<SuperResolution>(SuperResolution.OFF);
        this.mFaceIdentify = new ParameterValueHolder<FaceIdentify>(FaceIdentify.OFF);
        this.mVideoSize = new ParameterValueHolder<VideoSize>(VideoSize.FULL_HD);
        this.mVideoSelfTimer = new ParameterValueHolder<VideoSelfTimer>(VideoSelfTimer.OFF);
        this.mVideoSmileCapture = new ParameterValueHolder<VideoSmileCapture>(VideoSmileCapture.OFF);
        this.mVideoHdr = new ParameterValueHolder<VideoHdr>(VideoHdr.OFF);
        this.mVideoStabilizer = new ParameterValueHolder<VideoStabilizer>(VideoStabilizer.INTELLIGENT_ACTIVE);
        this.mVideoAutoReview = new ParameterValueHolder<VideoAutoReview>(VideoAutoReview.OFF);
        this.mMicrophone = new ParameterValueHolder<Microphone>(Microphone.ON);
    }

    public ActionMode getActionMode() {
        return this.mActionMode;
    }

    public Configurations getConfig() {
        return this.mConfig;
    }

    public void init(boolean bl, boolean bl2, Configurations configurations, SharedPreferencesUtil sharedPreferencesUtil) {
        this.mIsOneShot = bl;
        this.mIsOneShotVideo = bl2;
        CapturingMode capturingMode = this.mCapturingMode.get();
        this.mActionMode = new ActionMode(bl, capturingMode.getType(), capturingMode.getCameraId());
        this.mConfig = configurations;
        this.mCapturingMode.setOptions((ParameterValue[])new CapturingMode[]{capturingMode});
        this.mScene.setOptions((ParameterValue[])Scene.getOptions(capturingMode));
        this.mFacing.setOptions((ParameterValue[])Facing.getOptions());
        this.mFlash.setOptions((ParameterValue[])Flash.getOptions(this.mActionMode));
        this.mPhotoLight.setOptions((ParameterValue[])PhotoLight.getOptions(this.mActionMode));
        this.mEv.setOptions((ParameterValue[])Ev.getOptions(capturingMode));
        this.mWhiteBalance.setOptions((ParameterValue[])WhiteBalance.getOptions(capturingMode));
        this.mResolution.setOptions((ParameterValue[])Resolution.getOptions(capturingMode));
        this.mCaptureFrameShape.setOptions((ParameterValue[])CaptureFrameShape.getOptions(capturingMode));
        this.mSelfTimer.setOptions((ParameterValue[])SelfTimer.getOptions());
        this.mSmileCapture.setOptions((ParameterValue[])SmileCapture.getOptions(capturingMode));
        this.mFocusMode.setOptions((ParameterValue[])FocusMode.getOptions(capturingMode));
        this.mHdr.setOptions((ParameterValue[])Hdr.getOptions(capturingMode));
        this.mIso.setOptions((ParameterValue[])Iso.getOptions(capturingMode, this.mResolution.get()));
        this.mMetering.setOptions((ParameterValue[])Metering.getOptions(capturingMode));
        this.mSoftSkin.setOptions((ParameterValue[])SoftSkin.getOptions(capturingMode));
        this.mStabilizer.setOptions((ParameterValue[])Stabilizer.getOptions(capturingMode));
        this.mAutoReview.setOptions((ParameterValue[])AutoReview.getOptions(capturingMode));
        this.mBurst.setOptions((ParameterValue[])BurstShot.getOptions(this.mIsOneShot, capturingMode));
        this.mSuperResolution.setOptions((ParameterValue[])SuperResolution.getOptions(capturingMode, this.mIsOneShotVideo));
        this.mFaceIdentify.setOptions((ParameterValue[])FaceIdentify.getOptions(this.mActionMode));
        this.mVideoSize.setOptions((ParameterValue[])VideoSize.getOptions(this.mActionMode, configurations));
        this.mVideoSelfTimer.setOptions((ParameterValue[])VideoSelfTimer.getOptions());
        this.mVideoSmileCapture.setOptions((ParameterValue[])VideoSmileCapture.getOptions(this.mIsOneShot, capturingMode));
        this.mVideoHdr.setOptions((ParameterValue[])VideoHdr.getOptions(capturingMode));
        this.mVideoStabilizer.setOptions((ParameterValue[])VideoStabilizer.getOptions(capturingMode.getCameraId()));
        this.mVideoAutoReview.setOptions((ParameterValue[])VideoAutoReview.getOptions(capturingMode));
        this.mMicrophone.setOptions((ParameterValue[])Microphone.getOptions());
        if (this.mIsOneShotVideo && capturingMode != CapturingMode.FRONT_PHOTO && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            CapturingMode capturingMode2 = CapturingMode.VIDEO;
            ActionMode actionMode = new ActionMode(this.mIsOneShot, capturingMode2.getType(), capturingMode2.getCameraId());
            this.mFlash.setOptions((ParameterValue[])Flash.getOptions(actionMode));
            this.mPhotoLight.setOptions((ParameterValue[])PhotoLight.getOptions(actionMode));
        }
        Iterator iterator = this.values().iterator();
        while (iterator.hasNext()) {
            ParameterUtil.updateDefaultValue((ParameterValueHolder)iterator.next());
        }
    }

    public boolean isOneShotVideo() {
        return this.mIsOneShotVideo;
    }

    public List<ParameterValueHolder<?>> values() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCapturingMode);
        arrayList.add(this.mScene);
        arrayList.add(this.mFacing);
        arrayList.add(this.mFlash);
        arrayList.add(this.mPhotoLight);
        arrayList.add(this.mEv);
        arrayList.add(this.mWhiteBalance);
        arrayList.add(this.mResolution);
        arrayList.add(this.mCaptureFrameShape);
        arrayList.add(this.mSelfTimer);
        arrayList.add(this.mSmileCapture);
        arrayList.add(this.mFocusMode);
        arrayList.add(this.mHdr);
        arrayList.add(this.mIso);
        arrayList.add(this.mMetering);
        arrayList.add(this.mSoftSkin);
        arrayList.add(this.mStabilizer);
        if (!this.mIsOneShot) {
            arrayList.add(this.mAutoReview);
        }
        arrayList.add(this.mBurst);
        arrayList.add(this.mSuperResolution);
        arrayList.add(this.mFaceIdentify);
        arrayList.add(this.mVideoSize);
        arrayList.add(this.mVideoSelfTimer);
        arrayList.add(this.mVideoSmileCapture);
        arrayList.add(this.mVideoHdr);
        arrayList.add(this.mVideoStabilizer);
        if (!this.mIsOneShotVideo) {
            arrayList.add(this.mVideoAutoReview);
        }
        arrayList.add(this.mMicrophone);
        return arrayList;
    }
}

