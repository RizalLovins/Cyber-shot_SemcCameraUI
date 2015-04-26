/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.hardware.Camera
 *  android.hardware.Camera$Size
 *  android.view.Display
 *  android.view.WindowManager
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.device;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;
import com.sonyericsson.cameracommon.device.SizeConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CameraSize {
    private static final double ASPECT_TOLERANCE = 0.05;
    public static final int MIN_RECORDING_DURATION = 1;
    private static final String TAG = "CameraSize";
    public static final long VIDEO_MAX_SIZE = 0xFFFFFFFFL;
    private static Rect sApplicationArea;

    public static long computeSize(int n, int n2, int n3) {
        return (long)(n3 * (n + n2) / 8) / 1024;
    }

    public static Rect convertCameraSize(Camera.Size size) {
        if (size == null) {
            return null;
        }
        return new Rect(0, 0, size.width, size.height);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static List<Rect> convertCameraSizeList(List<Camera.Size> list) {
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Camera.Size size = (Camera.Size)iterator.next();
            if (size == null) continue;
            arrayList.add((Object)CameraSize.convertCameraSize(size));
        }
        return arrayList;
    }

    private static int getAspectRatio(double d, double d2) {
        return (int)(100.0 * d / d2);
    }

    public static int getAspectRatio(Rect rect) {
        return CameraSize.getAspectRatio(rect.width(), rect.height());
    }

    public static long getAverageFileSize(int n, int n2) {
        return CameraSize.computeSize(n, n2, 60);
    }

    public static int getDispayHeightWithoutNavigationBar(Context context) {
        return Math.min((int)context.getResources().getDisplayMetrics().widthPixels, (int)context.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getDispayWidthWithoutNavigationBar(Context context) {
        return Math.max((int)context.getResources().getDisplayMetrics().widthPixels, (int)context.getResources().getDisplayMetrics().heightPixels);
    }

    public static Rect getDisplayRect() {
        if (sApplicationArea == null) {
            return new Rect();
        }
        return new Rect(0, 0, sApplicationArea.width(), sApplicationArea.height());
    }

    public static Rect getDisplayRect(Context context) {
        if (sApplicationArea == null) {
            CameraSize.setDisplaySize(context);
        }
        return CameraSize.getDisplayRect();
    }

    static Rect getIdealPictureRect(Rect rect) {
        switch (rect.width()) {
            default: {
                return new Rect(0, 0, rect.width(), rect.height());
            }
            case 3920: {
                return new Rect(0, 0, 4128, 3096);
            }
            case 3104: {
                return new Rect(0, 0, 3264, 2448);
            }
            case 1824: 
        }
        return new Rect(0, 0, 1920, 1080);
    }

    public static Rect getIdealSurfaceRect(Rect rect, Rect rect2) {
        return CameraSize.getIdealSurfaceRect(SizeConstants.LcdSize.getLcdSize(Math.min((int)rect.height(), (int)rect.width())), rect2);
    }

    static Rect getIdealSurfaceRect(SizeConstants.LcdSize lcdSize, Rect rect) {
        double d = (double)rect.height() / (double)rect.width();
        double d2 = Math.ceil((double)(9.0 * (double)lcdSize.width / 16.0));
        return new Rect(0, 0, (int)Math.ceil((double)(d2 / d)), (int)d2);
    }

    public static long getMinFileSize(int n, int n2) {
        return CameraSize.computeSize(n, n2, 1);
    }

    public static Rect getOptimalPreviewRect(Rect rect, Rect rect2, List<Rect> list) {
        Rect rect3 = rect2;
        int n = Integer.MAX_VALUE;
        double d = (double)rect.width() / (double)rect.height();
        Iterator iterator = list.iterator();
        do {
            Rect rect4;
            int n2;
            if (iterator.hasNext()) {
                rect4 = (Rect)iterator.next();
                if (rect4.height() > rect2.height()) continue;
                if (rect4.width() == rect.width() && rect4.height() == rect.height()) {
                    rect3 = rect4;
                }
            } else {
                return rect3;
            }
            if (Math.abs((double)(d - (double)rect4.width() / (double)rect4.height())) > 0.05 || (n2 = Math.abs((int)(rect4.height() - rect.height()))) >= n) continue;
            rect3 = rect4;
            n = n2;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Rect getOptimalVideoSnapshotSize(Rect rect, List<Rect> list) {
        Rect rect2 = null;
        for (Rect rect3 : list) {
            if (rect.width() > rect3.width() || rect.height() > rect3.height()) continue;
            if (rect2 == null) {
                rect2 = rect3;
                continue;
            }
            if (rect3.width() >= rect2.width() || rect3.height() >= rect2.height()) continue;
            rect2 = rect3;
        }
        return rect2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Rect getOptimalVideoSnapshotSizeFromCamerSizeList(Rect rect, List<Camera.Size> list) {
        ArrayList arrayList = new ArrayList();
        for (Camera.Size size : list) {
            arrayList.add((Object)new Rect(0, 0, size.width, size.height));
        }
        return CameraSize.getOptimalVideoSnapshotSize(rect, arrayList);
    }

    public static Rect getPhotoPreviewRect(Rect rect, Rect rect2, List<Rect> list) {
        return CameraSize.getOptimalPreviewRect(CameraSize.getIdealSurfaceRect(sApplicationArea, rect), rect2, list);
    }

    public static Rect getSurfaceRect(Rect rect) {
        return CameraSize.getIdealSurfaceRect(sApplicationArea, rect);
    }

    public static Rect getVideoPreviewRect(Rect rect, Rect rect2, List<Rect> list) {
        return CameraSize.getOptimalPreviewRect(rect, rect2, list);
    }

    public static boolean isAspectRatioDifferent(Rect rect, Rect rect2) {
        int n = CameraSize.getAspectRatio(rect);
        int n2 = CameraSize.getAspectRatio(rect2);
        boolean bl = false;
        if (n != n2) {
            bl = true;
        }
        return bl;
    }

    static void setDisplaySize(Context context) {
        Point point = new Point();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getRealSize(point);
        int n = Math.min((int)point.x, (int)point.y);
        sApplicationArea = new Rect(0, 0, SizeConstants.LcdSize.getLcdSize((int)n).width, n);
    }
}

