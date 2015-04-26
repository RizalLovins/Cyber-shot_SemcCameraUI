/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.util.Log
 *  com.sonymobile.camera.addon.internal.capturingmode.CapturingModeAttributes
 *  com.sonymobile.camera.addon.internal.capturingmode.CapturingModeAttributesBuilder
 *  com.sonymobile.camera.addon.internal.registration.AddOnRegistrationHelper
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.addon;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.util.capability.HardwareCapability;
import com.sonymobile.camera.addon.internal.capturingmode.CapturingModeAttributes;
import com.sonymobile.camera.addon.internal.capturingmode.CapturingModeAttributesBuilder;
import com.sonymobile.camera.addon.internal.registration.AddOnRegistrationHelper;
import java.util.ArrayList;
import java.util.List;

/*
 * Failed to analyse overrides
 */
public class CameraAddonReceiver
extends BroadcastReceiver {
    private static final String TAG = CameraAddonReceiver.class.getSimpleName();

    private static boolean isCameraSupported(int n) {
        if (HardwareCapability.getNumberOfCameras() > n) {
            return true;
        }
        return false;
    }

    private static boolean isSuperiorAutoSupported(Context context) {
        return context.getResources().getBoolean(2131427336);
    }

    public void onReceive(Context context, Intent intent) {
        Log.d((String)TAG, (String)("onReceive() intent:" + (Object)intent));
        ArrayList arrayList = new ArrayList();
        if (CameraAddonReceiver.isSuperiorAutoSupported(context)) {
            arrayList.add((Object)new CapturingModeAttributesBuilder().packageName(context.getPackageName()).activityName(CameraActivity.class.getName()).modeName(CapturingMode.SCENE_RECOGNITION.name()).selectorIconId(2130837522).selectorLabelId(2131361968).shortcutIconId(2130837514).descriptionLabelId(2131362038).shortcutLabelId(2131361968).isVisibleNormal(true).isVisibleOneshot(true).build());
        }
        if (CameraAddonReceiver.isCameraSupported(0)) {
            arrayList.add((Object)new CapturingModeAttributesBuilder().packageName(context.getPackageName()).activityName(CameraActivity.class.getName()).modeName(CapturingMode.NORMAL.name()).selectorIconId(2130837521).selectorLabelId(2131362014).shortcutIconId(2130837513).descriptionLabelId(2131362039).shortcutLabelId(2131362014).isVisibleNormal(true).isVisibleOneshot(true).build());
        }
        AddOnRegistrationHelper.register((ContentResolver)context.getContentResolver(), (List)arrayList);
    }
}

