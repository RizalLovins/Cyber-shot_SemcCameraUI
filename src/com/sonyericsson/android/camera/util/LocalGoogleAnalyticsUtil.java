/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.googleanalytics.parameters.Event;
import com.sonymobile.cameracommon.googleanalytics.parameters.Screen;

public class LocalGoogleAnalyticsUtil {
    private static final String TAG = LocalGoogleAnalyticsUtil.class.getSimpleName();
    private static final LocalGoogleAnalyticsUtil sInstance = new LocalGoogleAnalyticsUtil();
    private ShootingInfo mShootingDuringRecInfo = new ShootingInfo(null);
    private ShootingInfo mShootingInfo = new ShootingInfo(null);

    private LocalGoogleAnalyticsUtil() {
    }

    public static LocalGoogleAnalyticsUtil getInstance() {
        return sInstance;
    }

    private ShootingInfo getShootingInfo(Event.CaptureProcedure captureProcedure) {
        switch (.$SwitchMap$com$sonymobile$cameracommon$googleanalytics$parameters$Event$CaptureProcedure[captureProcedure.ordinal()]) {
            default: {
                return null;
            }
            case 1: {
                return this.mShootingInfo;
            }
            case 2: 
        }
        return this.mShootingDuringRecInfo;
    }

    public void clear() {
        this.mShootingInfo.clear();
        this.mShootingDuringRecInfo.clear();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void sendCaptureProcedureForShooting(Event.CaptureProcedure captureProcedure, boolean bl) {
        ShootingInfo shootingInfo = super.getShootingInfo(captureProcedure);
        if (shootingInfo == null) {
            return;
        }
        if (shootingInfo.mNum != 0) {
            GoogleAnalyticsUtil.sendCaptureProcedureEvent(captureProcedure, bl, shootingInfo.mNum, shootingInfo.mOrientationSum);
        }
        shootingInfo.clear();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void sendView(CapturingMode var1_1, boolean var2_2) {
        var3_3 = .$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[var1_1.ordinal()];
        var4_4 = null;
        switch (var3_3) {
            case 1: {
                var4_4 = Screen.SUPERIOR_AUTO_CAMERAUI;
                if (var2_2) {
                    var4_4 = Screen.SUPERIOR_AUTO_FASTCAPTURING;
                    ** break;
                } else {
                    ** GOTO lbl21
                }
            }
            case 2: {
                var4_4 = Screen.MANUAL_PHOTO;
                ** break;
            }
            case 3: {
                var4_4 = Screen.SUPERIOR_AUTO_FRONT_CAMERAUI;
                if (var2_2) {
                    var4_4 = Screen.FRONT_CAMERA_FASTCAPTURING;
                    ** break;
                } else {
                    ** GOTO lbl21
                }
            }
            case 4: {
                var4_4 = Screen.FRONT_CAMERA_CAMERAUI;
                if (var2_2) {
                    var4_4 = Screen.FRONT_CAMERA_FASTCAPTURING;
                }
            }
lbl21: // 11 sources:
            default: {
                ** GOTO lbl25
            }
            case 5: 
        }
        var4_4 = Screen.MANUAL_VIDEO;
lbl25: // 2 sources:
        GoogleAnalyticsUtil.sendView(var4_4);
    }

    public void setCaptureProcedureForShooting(Event.CaptureProcedure captureProcedure, int n) {
        ShootingInfo shootingInfo = super.getShootingInfo(captureProcedure);
        if (shootingInfo == null) {
            return;
        }
        shootingInfo.set(n);
    }

    private static class ShootingInfo {
        private int mNum;
        private int mOrientationSum;

        private ShootingInfo() {
            this.mNum = 0;
            this.mOrientationSum = com.sonymobile.cameracommon.googleanalytics.parameters.Event$Orientation.NONE.value;
        }

        /* synthetic */ ShootingInfo( var1) {
        }

        public void clear() {
            this.mNum = 0;
            this.mOrientationSum = com.sonymobile.cameracommon.googleanalytics.parameters.Event$Orientation.NONE.value;
        }

        public void set(int n) {
            this.mNum = 1 + this.mNum;
            this.mOrientationSum+=com.sonymobile.cameracommon.googleanalytics.parameters.Event$Orientation.changeDegreeToOrientation((int)n).value;
        }
    }

}

