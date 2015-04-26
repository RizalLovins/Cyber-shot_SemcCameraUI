/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.admin.DevicePolicyManager
 *  android.content.ComponentName
 *  android.content.Context
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.List
 *  java.util.concurrent.Callable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.sonyericsson.android.camera.device;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.Camera;
import com.sonyericsson.android.camera.device.CameraDeviceException;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CameraDeviceUtil {
    public static final int ERROR_SET_PREVIEW_DISPLAY = -256;
    public static final int ERROR_START_PREVIEW = -255;
    public static final int FOCUS_AREA_WEIGHT_DEFAULT = 0;
    public static final int FOCUS_AREA_WEIGHT_USER = 1000;
    public static final int FPS_RANGE_MAX = 60;
    public static final int FPS_RANGE_MIN = 0;
    public static final int OBJECT_TRACKING_LOW_PASS_FILTER_STRENGTH = 75;
    public static final int OBJECT_TRACKING_MINIMAL_INTERVAL_MS = 100;
    private static final String TAG = CameraDeviceUtil.class.getSimpleName();

    public static int[] computePreviewFpsRange(int n, List<int[]> list) {
        if (list.size() == 1) {
            int[] arrn = (int[])list.get(0);
            return CameraDeviceUtil.getFpsRange(arrn[1], arrn[0]);
        }
        int n2 = n * 1000;
        if (n2 < 0) {
            n2 = Integer.MAX_VALUE;
        }
        if (n2 == 0) {
            return CameraDeviceUtil.getMinFpsRange(n2, list);
        }
        return CameraDeviceUtil.getMaxFpsRange(n2, list);
    }

    public static Camera execute(Context context, Callable<Camera> callable) {
        if (CameraDeviceUtil.isCameraDisabled(context)) {
            return null;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(callable);
        try {
            Camera camera = (Camera)future.get(4000, TimeUnit.MILLISECONDS);
            return camera;
        }
        catch (InterruptedException var12_5) {
            CameraLogger.w(TAG, "Open camera has been interrupted.", (Throwable)var12_5);
            return null;
        }
        catch (ExecutionException var9_6) {
            CameraLogger.w(TAG, "Open camera failed.", (Throwable)var9_6);
            return null;
        }
        catch (TimeoutException var6_7) {
            CameraLogger.w(TAG, "Open camera failed.", (Throwable)var6_7);
            return null;
        }
        finally {
            future.cancel(true);
            executorService.shutdown();
        }
    }

    public static int[] getFpsRange(int n, int n2) {
        if (n > 0) {
            int[] arrn = new int[2];
            arrn[1] = n;
            arrn[0] = n2;
            return arrn;
        }
        return new int[0];
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int[] getMaxFpsRange(int n, List<int[]> list) {
        int n2 = 0;
        int n3 = 0;
        for (int[] arrn : list) {
            int n4 = arrn[1];
            int n5 = arrn[0];
            if (n4 > n || n2 > n4) continue;
            n2 = n4;
            n3 = n5;
        }
        return CameraDeviceUtil.getFpsRange(n2, n3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int[] getMinFpsRange(int n, List<int[]> list) {
        int n2 = Integer.MAX_VALUE;
        int n3 = Integer.MAX_VALUE;
        for (int[] arrn : list) {
            int n4 = arrn[1];
            int n5 = arrn[0];
            if (n4 <= n || n2 < n4) continue;
            n2 = n4;
            n3 = n5;
        }
        return CameraDeviceUtil.getFpsRange(n2, n3);
    }

    public static Camera.Parameters getParams(Context context, int n) throws CameraDeviceException {
        Camera camera = CameraDeviceUtil.execute(context, new OpenCameraTask(n));
        if (camera == null) {
            throw new CameraDeviceException("Open camera[" + n + "] failed.");
        }
        Camera.Parameters parameters = camera.getParameters();
        camera.release();
        if (parameters == null) {
            throw new CameraDeviceException("No Parameters[" + n + "] obtained.");
        }
        return parameters;
    }

    public static boolean isCameraDisabled(Context context) {
        if (((DevicePolicyManager)context.getSystemService("device_policy")).getCameraDisabled(null)) {
            CameraLogger.errorLogForNonUserVariant(TAG, "[CameraNotAvailable] Camera is Disabled.");
            return true;
        }
        return false;
    }

    private static class OpenCameraTask
    implements Callable<Camera> {
        private final int mCameraId;

        public OpenCameraTask(int n) {
            this.mCameraId = n;
        }

        /*
         * Unable to fully structure code
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public Camera call() {
            var1_1 = 0;
            block4 : do {
                var2_3 = null;
                if (var1_1 >= 5) return var2_3;
                try {
                    var5_5 = Camera.open((int)this.mCameraId);
                    return var5_5;
                }
                catch (RuntimeException var3_4) {
                    try {
                        Thread.sleep((long)500);
                    }
                    catch (InterruptedException var4_2) {
                        ** continue;
                    }
lbl13: // 2 sources:
                    do {
                        ++var1_1;
                        continue block4;
                        break;
                    } while (true);
                }
                break;
            } while (true);
        }
    }

}

