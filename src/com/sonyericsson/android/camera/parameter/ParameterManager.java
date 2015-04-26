/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.graphics.Rect
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.EnumMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 */
package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ControllerManager;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.ParameterSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AutoUpload;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.controller.ControllerEventSource;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.CommonParams;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.parameter.SettingsConverter;
import com.sonyericsson.android.camera.util.SharedPreferencesUtil;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.CommonSettings;
import com.sonyericsson.cameracommon.mediasaving.StorageController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterManager {
    private static final String TAG = ParameterManager.class.getSimpleName();
    private final CameraActivity mCameraActivity;
    private Parameters mCurrentParameters;
    private Map<CapturingMode, Parameters> mMap;
    protected boolean mSettingsApplied = false;
    private SharedPreferencesUtil mSharedPrefs;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ParameterManager(CameraActivity cameraActivity, CapturingMode capturingMode, StorageController storageController) {
        this.mCameraActivity = cameraActivity;
        this.mSharedPrefs = new SharedPreferencesUtil((Context)cameraActivity);
        boolean bl = cameraActivity.isOneShot();
        boolean bl2 = cameraActivity.isOneShotVideo();
        Configurations configurations = cameraActivity.getConfigurations();
        CommonParams.getInstance().init(cameraActivity);
        this.mMap = new EnumMap((Class)CapturingMode.class);
        for (CapturingMode capturingMode2 : CapturingMode.getValidOptions(cameraActivity)) {
            Parameters parameters = Parameters.create(capturingMode2, bl2, storageController);
            parameters.prepareHolder(bl, bl2, configurations, this.mSharedPrefs);
            this.mMap.put((Object)capturingMode2, (Object)parameters);
        }
        super.changeParameters(capturingMode);
    }

    private void applyChangedValues(List<ParameterValue> list) {
        if (this.mCameraActivity.getControllerManager().isControllerReleased()) {
            return;
        }
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ((ParameterValue)iterator.next()).apply(this.mCameraActivity.getControllerManager().getParameterListener());
        }
        this.mCameraActivity.getControllerManager().getParameterListener().commit();
    }

    private void changeParameters(CapturingMode capturingMode) {
        this.mCurrentParameters = (Parameters)this.mMap.get((Object)capturingMode);
    }

    public static ParameterValueHolder<DestinationToSave> getDestinationToSave() {
        return CommonParams.getInstance().mDestinationToSave;
    }

    private void setCommonParams(ParameterValue parameterValue) {
        CommonSettingValue commonSettingValue = SettingsConverter.getCommonSettingValue(parameterValue);
        if (commonSettingValue != null) {
            this.mCameraActivity.getCommonSettings().set(commonSettingValue);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void applyCapturingMode(boolean bl, CapturingMode capturingMode) {
        List<ParameterValue> list = bl ? new List<ParameterValue>(this.mCurrentParameters.getTargetParameters().values()) : this.mCurrentParameters.getChangedValues(this.getParameters(capturingMode).getParameters());
        this.mCurrentParameters.commit();
        super.applyChangedValues(list);
        this.updateVideoOption();
    }

    public void changeCapturingMode(CapturingMode capturingMode) {
        super.changeParameters(capturingMode);
        this.mCurrentParameters.capturingMode.apply(this.mCurrentParameters);
        for (ParameterKey parameterKey : ParameterKey.values()) {
            parameterKey.setSelectability(ParameterSelectability.getSelectability(this.mCurrentParameters.getOptions(parameterKey).length));
        }
        this.mCurrentParameters.updateSelectability();
    }

    public ParameterValue get(ParameterKey parameterKey) {
        return (ParameterValue)this.mCurrentParameters.getParameters().get((Object)parameterKey);
    }

    public ParameterValue[] getOptions(ParameterKey parameterKey) {
        return this.mCurrentParameters.getOptions(parameterKey);
    }

    public Parameters getParameters() {
        return this.mCurrentParameters;
    }

    public Parameters getParameters(CapturingMode capturingMode) {
        if (this.mMap.containsKey((Object)capturingMode)) {
            return (Parameters)this.mMap.get((Object)capturingMode);
        }
        return this.mCurrentParameters;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getSelfTimerType(ControllerEventSource controllerEventSource) {
        if (this.mCameraActivity.isOneShotVideo()) {
            return 2;
        }
        switch (.$SwitchMap$com$sonyericsson$android$camera$controller$ControllerEventSource[controllerEventSource.ordinal()]) {
            default: {
                return this.getParameters().capturingMode.getType();
            }
            case 1: 
        }
        return 2;
    }

    public boolean isSelfTimerOn() {
        return this.mCurrentParameters.getSelfTimer().getBooleanValue();
    }

    public boolean isVideoSelfTimerOn() {
        return this.mCurrentParameters.getVideoSelfTimer().getBooleanValue();
    }

    public void release() {
        Iterator iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            ((Parameters)iterator.next()).clearHolder();
        }
        this.mMap.clear();
        this.mCurrentParameters = null;
        this.mSharedPrefs = null;
    }

    public void reset() {
        Iterator iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            ((Parameters)iterator.next()).reset();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void set(ParameterValue parameterValue) {
        ParameterKey parameterKey = parameterValue.getParameterKey();
        parameterValue.apply(this.mCurrentParameters);
        List<ParameterValue> list = this.mCurrentParameters.getChangedValues();
        this.mCurrentParameters.commit();
        if (parameterKey == ParameterKey.RESOLUTION) {
            this.mCameraActivity.getControllerManager().stopObjectTracking(((Resolution)parameterValue).getPictureRect());
        } else if (parameterKey == ParameterKey.VIDEO_SIZE) {
            this.mCameraActivity.getControllerManager().stopObjectTracking(((VideoSize)parameterValue).getVideoRect());
        }
        super.applyChangedValues(list);
        if (parameterValue.getParameterKey().isCommon()) {
            super.setCommonParams(parameterValue);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void suspend() {
        if (this.mSettingsApplied) {
            Iterator iterator = this.mMap.values().iterator();
            while (iterator.hasNext()) {
                ((Parameters)iterator.next()).writeSharedPrefs(this.mSharedPrefs);
            }
            this.mSharedPrefs.writeParameters(false);
            if (!this.mCameraActivity.isOneShot()) {
                this.mSharedPrefs.writeString("KEY_LAST_MODE", this.getParameters().capturingMode.name(), false);
                this.mSharedPrefs.writeString("KEY_LAST_PREVIOUS_MODE", this.mCameraActivity.getPreviousCapturingModeNotFront().name(), false);
                this.mSharedPrefs.writeString("KEY_LAST_PREVIOUS_MANUAL_MODE", this.mCameraActivity.getPreviousManualMode().name(), false);
            }
            if (HardwareCapability.isFrontCameraSupported() && !this.mSharedPrefs.getSharedPreferences().contains("FRONT_FAST")) {
                CapturingMode capturingMode = HardwareCapability.getInstance().isSceneRecognitionSupported(1) ? CapturingMode.SUPERIOR_FRONT : CapturingMode.FRONT_PHOTO;
                this.mSharedPrefs.writeString("FRONT_FAST", capturingMode.name(), false);
            }
            this.mSharedPrefs.apply();
        }
        this.mSettingsApplied = false;
    }

    public void update(CapturingMode capturingMode) {
        for (Parameters parameters : this.mMap.values()) {
            parameters.mCapturingModeParams.init(this.mCameraActivity.isOneShot(), this.mCameraActivity.isOneShotVideo(), this.mCameraActivity.getConfigurations(), this.mSharedPrefs);
            Iterator iterator = parameters.mCapturingModeParams.values().iterator();
            while (iterator.hasNext()) {
                parameters.updateHolder((ParameterValueHolder)iterator.next());
            }
        }
        CapturingMode capturingMode2 = CapturingMode.UNKNOWN;
        CapturingMode capturingMode3 = CapturingMode.convertFrom(this.mSharedPrefs.readString("KEY_LAST_MODE", capturingMode2.name()), capturingMode2);
        this.mCameraActivity.getControllerManager().storeMainPhotoMode(capturingMode3);
        ParameterKey.VIDEO_SIZE.setSaved(true);
        ArrayList arrayList = new ArrayList();
        for (ParameterKey parameterKey : ParameterKey.values()) {
            if (!parameterKey.isSaved() || this.mCameraActivity.isOneShotVideo() && parameterKey == ParameterKey.VIDEO_SIZE) continue;
            arrayList.add((Object)parameterKey);
        }
        this.mSharedPrefs.readParameters((List<ParameterKey>)arrayList);
        for (CommonSettingKey commonSettingKey : CommonSettingKey.values()) {
            SettingsConverter.convertAndApplyValue(commonSettingKey, this.mCameraActivity.getCommonSettings().get(commonSettingKey));
        }
        CommonParams.getInstance().update(this.mCameraActivity);
        for (Parameters parameters2 : this.mMap.values()) {
            parameters2.readSharedPrefs(this.mSharedPrefs);
            parameters2.updatePhotoLight();
        }
        Iterator iterator = this.mMap.keySet().iterator();
        while (iterator.hasNext()) {
            if (((CapturingMode)iterator.next()).getType() != 2) continue;
            this.updateVideoOption();
        }
        if (!Configurations.isFaceIdentificationEnabled()) {
            this.mCurrentParameters.mCapturingModeParams.mFaceIdentify.setDefaultValue();
        }
        this.changeCapturingMode(capturingMode);
        this.mCurrentParameters.commit();
        for (Parameters parameters3 : this.mMap.values()) {
            if (parameters3.capturingMode == this.mCurrentParameters.capturingMode) continue;
            parameters3.commit();
        }
        this.mSettingsApplied = true;
    }

    public void updateAutoUpload() {
        ParameterValue[] arrparameterValue = AutoUpload.getOptions();
        CommonParams.getInstance().mAutoUpload.setOptions(arrparameterValue);
        CommonParams.getInstance().mAutoUpload.get().getParameterKey().setSelectability(ParameterSelectability.getSelectability(arrparameterValue.length));
        Iterator iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            ((Parameters)iterator.next()).updateHolder(CommonParams.getInstance().mAutoUpload);
        }
    }

    public void updateVideoOption() {
        ParameterValue[] arrparameterValue = VideoSize.getOptions(new ActionMode(this.mCameraActivity.isOneShot(), this.mCurrentParameters.capturingMode.getType(), this.mCurrentParameters.capturingMode.getCameraId()), this.mCameraActivity.getConfigurations());
        if (this.mCameraActivity.isOneShotVideo()) {
            if (arrparameterValue.length == 1) {
                this.mCurrentParameters.set((VideoSize)arrparameterValue[0]);
            }
            ParameterKey.VIDEO_SIZE.setSaved(false);
        }
        this.mCurrentParameters.mCapturingModeParams.mVideoSize.setOptions(arrparameterValue);
    }

}

