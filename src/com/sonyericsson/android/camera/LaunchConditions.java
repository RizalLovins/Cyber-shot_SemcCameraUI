/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.OneShotType;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.cameracommon.activity.BaseActivity;

public class LaunchConditions {
    private static final String TAG = LaunchConditions.class.getSimpleName();
    private ExtraOperation mExtraOperation = ExtraOperation.NONE;
    private boolean mIsCameraDisabled = false;
    private boolean mIsDeviceError = false;
    private boolean mIsLaunchedByOtherCamera = false;
    private boolean mIsOneShot = false;
    private CapturingMode mLaunchCapturingMode = CapturingMode.UNKNOWN;
    private BaseActivity.LaunchedBy mLaunchedBy = BaseActivity.LaunchedBy.UNKNOWN;
    private OneShotType mOneShotType = OneShotType.NONE;

    public void clear(CapturingMode capturingMode) {
        this.setLaunchCapturingMode(capturingMode);
        this.setLaunchedBy(BaseActivity.LaunchedBy.UNKNOWN);
        this.setIsLaunchedByOtherCamera(false);
        this.setExtraOperation(ExtraOperation.NONE);
    }

    public ExtraOperation getExtraOperation() {
        return this.mExtraOperation;
    }

    public CapturingMode getLaunchCapturingMode() {
        return this.mLaunchCapturingMode;
    }

    public BaseActivity.LaunchedBy getLaunchedBy() {
        return this.mLaunchedBy;
    }

    public OneShotType getOneShotType() {
        return this.mOneShotType;
    }

    public boolean isCameraDisabled() {
        return this.mIsCameraDisabled;
    }

    public boolean isDeviceError() {
        return this.mIsDeviceError;
    }

    public boolean isLaunchedByOtherCamera() {
        return this.mIsLaunchedByOtherCamera;
    }

    public boolean isOneShot() {
        return this.mIsOneShot;
    }

    public void setCameraDisabled(boolean bl) {
        this.mIsCameraDisabled = bl;
    }

    public void setDeviceError(boolean bl) {
        this.mIsDeviceError = bl;
    }

    public void setExtraOperation(ExtraOperation extraOperation) {
        this.mExtraOperation = extraOperation;
    }

    public void setIsLaunchedByOtherCamera(boolean bl) {
        this.mIsLaunchedByOtherCamera = bl;
    }

    public void setIsOneShot(boolean bl) {
        this.mIsOneShot = bl;
    }

    public void setLaunchCapturingMode(CapturingMode capturingMode) {
        this.mLaunchCapturingMode = capturingMode;
    }

    public void setLaunchedBy(BaseActivity.LaunchedBy launchedBy) {
        this.mLaunchedBy = launchedBy;
    }

    public void setOneShotType(OneShotType oneShotType) {
        this.mOneShotType = oneShotType;
    }

    public static final class ExtraOperation
    extends Enum<ExtraOperation> {
        private static final /* synthetic */ ExtraOperation[] $VALUES;
        public static final /* enum */ ExtraOperation NONE = new ExtraOperation();
        public static final /* enum */ ExtraOperation OPEN_SETTINGS_MENU = new ExtraOperation();

        static {
            ExtraOperation[] arrextraOperation = new ExtraOperation[]{NONE, OPEN_SETTINGS_MENU};
            $VALUES = arrextraOperation;
        }

        private ExtraOperation() {
            super(string, n);
        }

        public static ExtraOperation valueOf(String string) {
            return (ExtraOperation)Enum.valueOf((Class)ExtraOperation.class, (String)string);
        }

        public static ExtraOperation[] values() {
            return (ExtraOperation[])$VALUES.clone();
        }
    }

}

