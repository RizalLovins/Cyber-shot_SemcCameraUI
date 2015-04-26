/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.cameracommon.device;

import android.hardware.Camera;
import java.util.ArrayList;
import java.util.List;

public class CommonPlatformDependencyResolver {
    private static final String TAG = CommonPlatformDependencyResolver.class.getSimpleName();

    private static List<String> getDcModeSupportedValueList(Camera.Parameters parameters) {
        String string = parameters.get("sony-dc-mode-values");
        ArrayList arrayList = new ArrayList();
        if (string != null) {
            String[] arrstring = string.split(",");
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)arrstring[i]);
            }
        }
        return arrayList;
    }

    private static List<String> getSupportedValueList(Camera.Parameters parameters) {
        String string = parameters.get("sony-video-nr-values");
        ArrayList arrayList = new ArrayList();
        if (string != null) {
            String[] arrstring = string.split(",");
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)arrstring[i]);
            }
        }
        return arrayList;
    }

    public static boolean isVideoNrSupported(Camera.Parameters parameters) {
        return CommonPlatformDependencyResolver.getSupportedValueList(parameters).contains((Object)"on");
    }

    public static void setDcMode(Camera.Parameters parameters, ApplicationType applicationType) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$device$CommonPlatformDependencyResolver$ApplicationType[applicationType.ordinal()]) {
            default: {
                return;
            }
            case 1: {
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-preview", "balance");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-picture", "auto");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-video", "balance");
                return;
            }
            case 2: {
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-preview", "balance");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-picture", "auto");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-video", "balance");
                return;
            }
            case 3: {
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-preview", "landscape");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-picture", "auto");
                CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-video", "balance");
                return;
            }
            case 4: 
        }
        CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-preview", "landscape");
        CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-picture", "landscape");
        CommonPlatformDependencyResolver.setDcModeIfSupported(parameters, "sony-dc-mode-for-video", "landscape");
    }

    private static void setDcModeIfSupported(Camera.Parameters parameters, String string, String string2) {
        if (CommonPlatformDependencyResolver.getDcModeSupportedValueList(parameters).contains((Object)string2)) {
            parameters.set(string, string2);
        }
    }

    public static final class ApplicationType
    extends Enum<ApplicationType> {
        private static final /* synthetic */ ApplicationType[] $VALUES;
        public static final /* enum */ ApplicationType COMPOSE_IMAGE_FROM_PREVIEW;
        public static final /* enum */ ApplicationType MAKE_IMAGE_FROM_PREVIEW;
        public static final /* enum */ ApplicationType NORMAL;
        public static final /* enum */ ApplicationType THIRD_PARTY;

        static {
            NORMAL = new ApplicationType();
            MAKE_IMAGE_FROM_PREVIEW = new ApplicationType();
            COMPOSE_IMAGE_FROM_PREVIEW = new ApplicationType();
            THIRD_PARTY = new ApplicationType();
            ApplicationType[] arrapplicationType = new ApplicationType[]{NORMAL, MAKE_IMAGE_FROM_PREVIEW, COMPOSE_IMAGE_FROM_PREVIEW, THIRD_PARTY};
            $VALUES = arrapplicationType;
        }

        private ApplicationType() {
            super(string, n);
        }

        public static ApplicationType valueOf(String string) {
            return (ApplicationType)Enum.valueOf((Class)ApplicationType.class, (String)string);
        }

        public static ApplicationType[] values() {
            return (ApplicationType[])$VALUES.clone();
        }
    }

}

