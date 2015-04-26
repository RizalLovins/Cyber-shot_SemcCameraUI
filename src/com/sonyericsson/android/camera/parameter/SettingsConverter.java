/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 */
package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.AutoUpload;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.FastCapture;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.TouchBlock;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.parameter.CommonParams;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;
import com.sonyericsson.cameracommon.commonsetting.values.AutoUpload;
import com.sonyericsson.cameracommon.commonsetting.values.FastCapture;
import com.sonyericsson.cameracommon.commonsetting.values.Geotag;
import com.sonyericsson.cameracommon.commonsetting.values.SaveDestination;
import com.sonyericsson.cameracommon.commonsetting.values.ShutterSound;
import com.sonyericsson.cameracommon.commonsetting.values.TouchBlock;
import com.sonyericsson.cameracommon.commonsetting.values.TouchCapture;
import com.sonyericsson.cameracommon.commonsetting.values.VolumeKey;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsConverter {
    private static final Map<CommonSettingKey, ParameterKey> KEY_MAP;
    private static final String TAG;
    private static final Map<ParameterValue, CommonSettingValue> VALUE_MAP;

    static {
        TAG = SettingsConverter.class.getSimpleName();
        KEY_MAP = new HashMap<CommonSettingKey, ParameterKey>(){};
        VALUE_MAP = new HashMap<ParameterValue, CommonSettingValue>(){};
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static void convertAndApplyValue(CommonSettingKey commonSettingKey, CommonSettingValue commonSettingValue) {
        ParameterValueHolder parameterValueHolder;
        ParameterValueHolder parameterValueHolder2;
        if (commonSettingKey == null) return;
        if (commonSettingValue == null) {
            return;
        }
        ParameterKey parameterKey = (ParameterKey)KEY_MAP.get((Object)commonSettingKey);
        if (parameterKey == null) return;
        Iterator iterator = CommonParams.getInstance().values().iterator();
        do {
            boolean bl = iterator.hasNext();
            parameterValueHolder2 = null;
            if (!bl) return;
        } while (!parameterKey.equals((Object)(parameterValueHolder = (ParameterValueHolder)iterator.next()).get().getParameterKey()));
        parameterValueHolder2 = parameterValueHolder;
        if (parameterValueHolder2 == null) return;
        switch (.$SwitchMap$com$sonyericsson$android$camera$configuration$ParameterKey[parameterKey.ordinal()]) {
            default: {
                CameraLogger.e(TAG, "this key is not common value.");
                return;
            }
            case 1: {
                parameterValueHolder2.set((AutoUpload)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 2: {
                DestinationToSave destinationToSave = (DestinationToSave)SettingsConverter.getParameterValue(commonSettingValue);
                parameterValueHolder2.set(destinationToSave.getRecommendedValue(DestinationToSave.getOptions(), destinationToSave));
                return;
            }
            case 3: {
                parameterValueHolder2.set((FastCapture)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 4: {
                parameterValueHolder2.set((Geotag)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 5: {
                parameterValueHolder2.set((ShutterSound)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 6: {
                parameterValueHolder2.set((TouchCapture)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 7: {
                parameterValueHolder2.set((VolumeKey)SettingsConverter.getParameterValue(commonSettingValue));
                return;
            }
            case 8: 
        }
        parameterValueHolder2.set((TouchBlock)SettingsConverter.getParameterValue(commonSettingValue));
    }

    public static CommonSettingValue getCommonSettingValue(ParameterValue parameterValue) {
        if (parameterValue == null) {
            return null;
        }
        return (CommonSettingValue)VALUE_MAP.get((Object)parameterValue);
    }

    private static ParameterValue getParameterValue(CommonSettingValue commonSettingValue) {
        for (ParameterValue parameterValue : VALUE_MAP.keySet()) {
            if (!commonSettingValue.equals((Object)((CommonSettingValue)VALUE_MAP.get((Object)parameterValue)))) continue;
            return parameterValue;
        }
        return null;
    }

}

