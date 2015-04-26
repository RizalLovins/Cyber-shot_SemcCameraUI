/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.sonyericsson.cameracommon.capability;

import android.hardware.Camera;
import java.util.List;

public final class CameraSensor
extends Enum<CameraSensor> {
    private static final /* synthetic */ CameraSensor[] $VALUES;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_13M;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_20M;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_2M;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_5M;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_8M;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_8M_INDEPENDENT_HDR;
    public static final /* enum */ CameraSensor CAMERA_SENSOR_VGA;
    public final Size defaultPhotoSize;
    private final boolean mIsIndependentHdrSupported;
    public final Size[] maxSizeList;

    static {
        Size[] arrsize = new Size[]{CameraSensor.size(5248, 3936)};
        CAMERA_SENSOR_20M = new CameraSensor(arrsize, CameraSensor.size(3840, 2160));
        Size[] arrsize2 = new Size[]{CameraSensor.size(4128, 3096)};
        CAMERA_SENSOR_13M = new CameraSensor(arrsize2, CameraSensor.size(3920, 2204));
        Size[] arrsize3 = new Size[]{CameraSensor.size(3264, 2448)};
        CAMERA_SENSOR_8M = new CameraSensor(arrsize3, CameraSensor.size(3104, 1746));
        Size[] arrsize4 = new Size[]{CameraSensor.size(3264, 2448)};
        CAMERA_SENSOR_8M_INDEPENDENT_HDR = new CameraSensor(arrsize4, CameraSensor.size(3264, 1836), true);
        Size[] arrsize5 = new Size[]{CameraSensor.size(2592, 1944)};
        CAMERA_SENSOR_5M = new CameraSensor(arrsize5, CameraSensor.size(2592, 1458));
        Size[] arrsize6 = new Size[]{CameraSensor.size(1632, 1224), CameraSensor.size(1920, 1080)};
        CAMERA_SENSOR_2M = new CameraSensor(arrsize6, CameraSensor.size(1920, 1080));
        Size[] arrsize7 = new Size[]{CameraSensor.size(640, 480)};
        CAMERA_SENSOR_VGA = new CameraSensor(arrsize7, CameraSensor.size(640, 480));
        CameraSensor[] arrcameraSensor = new CameraSensor[]{CAMERA_SENSOR_20M, CAMERA_SENSOR_13M, CAMERA_SENSOR_8M, CAMERA_SENSOR_8M_INDEPENDENT_HDR, CAMERA_SENSOR_5M, CAMERA_SENSOR_2M, CAMERA_SENSOR_VGA};
        $VALUES = arrcameraSensor;
    }

    private CameraSensor(Size[] arrsize, Size size) {
        super(string, n);
        this.maxSizeList = arrsize;
        this.defaultPhotoSize = size;
        this.mIsIndependentHdrSupported = false;
    }

    private CameraSensor(Size[] arrsize, Size size, boolean bl) {
        super(string, n);
        this.maxSizeList = arrsize;
        this.defaultPhotoSize = size;
        this.mIsIndependentHdrSupported = bl;
    }

    public static CameraSensor findByMaxPixels(Camera.Parameters parameters, int n, int n2) {
        boolean bl = CameraSensor.isIndependentHdrSupported(parameters);
        Size size = CameraSensor.size(n, n2);
        CameraSensor cameraSensor = null;
        for (CameraSensor cameraSensor2 : CameraSensor.values()) {
            if (!cameraSensor2.matchMaxPixels(size)) continue;
            if (cameraSensor2.mIsIndependentHdrSupported == bl) {
                return cameraSensor2;
            }
            cameraSensor = cameraSensor2;
        }
        return cameraSensor;
    }

    public static boolean isIndependentHdrSupported(Camera.Parameters parameters) {
        List list = parameters.getSupportedSceneModes();
        if (list == null) {
            return false;
        }
        return list.contains((Object)"hdr");
    }

    private static Size size(int n, int n2) {
        return new Size(n, n2, null);
    }

    public static CameraSensor valueOf(String string) {
        return (CameraSensor)Enum.valueOf((Class)CameraSensor.class, (String)string);
    }

    public static CameraSensor[] values() {
        return (CameraSensor[])$VALUES.clone();
    }

    public boolean matchMaxPixels(Size size) {
        for (Size size2 : this.maxSizeList) {
            if (size2.width != size.width || size2.height != size.height) continue;
            return true;
        }
        return false;
    }

    public static class Size {
        public final int height;
        public final int width;

        private Size(int n, int n2) {
            this.width = n;
            this.height = n2;
        }

        /* synthetic */ Size(int n, int n2,  var3_2) {
            super(n, n2);
        }
    }

}

