/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view.settings.executor;

import android.content.Context;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.ExtendedActivity;
import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FaceIdentify;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.Scene;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.parameter.ParameterManager;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonyericsson.android.camera.view.settings.SettingController;
import com.sonyericsson.android.camera.view.settings.SettingGroup;
import com.sonyericsson.android.camera.view.settings.executor.AutoUploadOnExecutor;
import com.sonyericsson.android.camera.view.settings.executor.CloseExecutor;
import com.sonyericsson.android.camera.view.settings.executor.FaceIdentifyOnExecutor;
import com.sonyericsson.android.camera.view.settings.executor.FacingChangeExecutor;
import com.sonyericsson.android.camera.view.settings.executor.GeotagOnExecutor;
import com.sonyericsson.android.camera.view.settings.executor.HdrOnExecutor;
import com.sonyericsson.android.camera.view.settings.executor.ParameterChanger;
import com.sonyericsson.android.camera.view.settings.executor.ShutterSoundOnExecutor;
import com.sonyericsson.android.camera.view.settings.executor.TouchBlockOnExecutor;
import com.sonyericsson.cameracommon.setting.controller.SettingDialogStack;
import com.sonyericsson.cameracommon.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.setting.executor.SettingChangeExecutor;
import com.sonyericsson.cameracommon.setting.executor.SettingChangerInterface;
import com.sonyericsson.cameracommon.setting.executor.SettingExecutorInterface;
import com.sonyericsson.cameracommon.setting.settingitem.TypedSettingItem;

public class SettingExecutorFactory {
    public static GeotagOnExecutor createGeotagChangeExecutor(Context context, ParameterManager parameterManager, SettingController settingController) {
        ParameterChanger parameterChanger = new ParameterChanger(parameterManager, settingController);
        return new GeotagOnExecutor((ExtendedActivity)context, parameterChanger, settingController);
    }

    public static SettingExecutorInterface<ParameterKey> createSelectMenuItemExecutor(Context context, final ParameterKey parameterKey, final SettingGroup settingGroup, final SettingController settingController) {
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                return new SettingExecutorInterface<ParameterKey>(){

                    @Override
                    public void onExecute(TypedSettingItem<ParameterKey> typedSettingItem) {
                        settingController.getSettingDialogStack().openSecondLayerDialog(settingController.generateParameterItemAdapter(typedSettingItem.getData(), settingGroup), (Object)parameterKey);
                    }
                };
            }
            case 8: {
                return new AutoUploadOnExecutor(context);
            }
            case 9: 
        }
        return new TouchBlockOnExecutor(context, settingController);
    }

    public static SettingExecutorInterface<SettingGroup> createSelectMenuShortcutExecutor(final SettingController settingController) {
        return new SettingExecutorInterface<SettingGroup>(){

            @Override
            public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
                settingController.openMenuDialog(typedSettingItem.getData());
            }
        };
    }

    public static SettingExecutorInterface<ParameterKey> createSelectShortcutExecutor(final SettingController settingController, ParameterKey parameterKey) {
        return new SettingExecutorInterface<ParameterKey>(){

            @Override
            public void onExecute(TypedSettingItem<ParameterKey> typedSettingItem) {
                settingController.openShortcutDialog(typedSettingItem.getData());
            }
        };
    }

    public static SettingExecutorInterface<SettingGroup> createSelectShortcutExecutor(final SettingController settingController, SettingGroup settingGroup) {
        if (SettingGroup.CONTROL.equals((Object)settingGroup)) {
            return new SettingExecutorInterface<SettingGroup>(){

                @Override
                public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
                    settingController.openControlDialog(typedSettingItem.getData());
                }
            };
        }
        return new SettingExecutorInterface<SettingGroup>(){

            @Override
            public void onExecute(TypedSettingItem<SettingGroup> typedSettingItem) {
                settingController.openShortcutDialog(typedSettingItem.getData());
            }
        };
    }

    public static SettingExecutorInterface<ParameterValue> createSettingChangeExecutor(Context context, ParameterKey parameterKey, ParameterValue parameterValue, ParameterManager parameterManager, SettingController settingController) {
        return SettingExecutorFactory.createSettingChangeExecutor(context, parameterKey, parameterValue, parameterManager, settingController, false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static SettingExecutorInterface<ParameterValue> createSettingChangeExecutor(Context var0, ParameterKey var1_4, ParameterValue var2_5, ParameterManager var3_2, SettingController var4_3, boolean var5_1) {
        var6_6 = new ParameterChanger(var3_2, var4_3);
        var7_7 = new SettingChangeExecutor<T>(var6_6);
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[var1_4.ordinal()]) {
            case 1: {
                if ((Geotag)var2_5 != Geotag.OFF) {
                    var7_11 = new GeotagOnExecutor((ExtendedActivity)var0, var6_6, var4_3);
                    ** break;
                } else {
                    ** GOTO lbl22
                }
            }
            case 3: {
                if ((ShutterSound)var2_5 != ShutterSound.OFF) {
                    var7_12 = new ShutterSoundOnExecutor((ExtendedActivity)var0, var3_2.getParameters().capturingMode.getType(), var6_6);
                    ** break;
                } else {
                    ** GOTO lbl22
                }
            }
            case 4: {
                var7_13 = new FacingChangeExecutor(var6_6, (CameraActivity)var0, var3_2.getParameters().capturingMode, var4_3.getSettingDialogStack());
                ** break;
            }
            case 5: {
                if ((FaceIdentify)var2_5 != FaceIdentify.OFF) {
                    var7_14 = new FaceIdentifyOnExecutor((ExtendedActivity)var0, var6_6);
                    ** break;
                } else {
                    ** GOTO lbl22
                }
            }
            case 6: {
                if (!((Hdr)var2_5 != Hdr.HDR_ON || HardwareCapability.getInstance().isStillHdrVer3(var3_2.getParameters().capturingMode.getCameraId()))) {
                    var7_15 = new HdrOnExecutor((ExtendedActivity)var0, var6_6);
                }
            }
lbl22: // 14 sources:
            default: {
                ** GOTO lbl27
            }
            case 7: 
        }
        if (!((Scene)var2_5 != Scene.BACKLIGHT_HDR || HardwareCapability.getInstance().isStillHdrVer3(var3_2.getParameters().capturingMode.getCameraId()))) {
            var7_16 = new HdrOnExecutor((ExtendedActivity)var0, var6_6);
        }
lbl27: // 4 sources:
        if (var5_1 == false) return var7_10;
        var7_9 = new CloseExecutor<T>(var4_3, var7_8);
        return var7_10;
    }

}

