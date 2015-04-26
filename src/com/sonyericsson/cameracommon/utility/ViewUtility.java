/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.view.Display
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.WindowManager
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ViewUtility {
    private static final float ASPECT_TOLERANCE = 0.001f;

    public static Point getCenter(Point point, Point point2) {
        return new Point((point.x + point2.x) / 2, (point.y + point2.y) / 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Rect getEstimatedRealScreenRect(Context context) {
        int n;
        int n2;
        Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        if (point.y < point.x) {
            n2 = point.x;
            n = point.y;
        } else {
            n2 = point.y;
            n = point.x;
        }
        ScreenSize screenSize = null;
        int n3 = n2 + n;
        for (ScreenSize screenSize2 : ScreenSize.values()) {
            int n4 = Math.abs((int)(n2 - screenSize2.getWidth())) + Math.abs((int)(n - screenSize2.getHeight()));
            if (n4 >= n3) continue;
            n3 = n4;
            screenSize = screenSize2;
        }
        if (screenSize == null) {
            throw new RuntimeException("getEstimatedRealScreenRect():[Not supported screen size.]");
        }
        return screenSize.getAsRect();
    }

    public static int getPixel(Context context, int n) {
        return context.getResources().getDimensionPixelSize(n);
    }

    public static boolean hitTest(View view, MotionEvent motionEvent) {
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        return new Rect(arrn[0], arrn[1], arrn[0] + view.getWidth(), arrn[1] + view.getHeight()).contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }

    public static boolean isSimilarAspect(float f, float f2) {
        if (Math.abs((float)(f - f2)) <= 0.001f) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isSimilarAspect(int n, int n2, int n3, int n4) {
        int n5 = 1;
        if (n < n5) return 0;
        if (n2 < n5) return 0;
        if (n3 < n5) return 0;
        if (n4 < n5) {
            return 0;
        }
        if (Math.abs((float)((float)n / (float)n2 - (float)n3 / (float)n4)) <= 0.001f) return n5;
        return false;
    }

    public static boolean isSimilarAspectRect(Rect rect, Rect rect2) {
        return ViewUtility.isSimilarAspect(rect.width(), rect.height(), rect2.width(), rect2.height());
    }

    private static final class ScreenSize
    extends Enum<ScreenSize> {
        private static final /* synthetic */ ScreenSize[] $VALUES;
        public static final /* enum */ ScreenSize FULL_HD;
        public static final /* enum */ ScreenSize FWVGA;
        public static final /* enum */ ScreenSize HD;
        public static final /* enum */ ScreenSize HVGA;
        public static final /* enum */ ScreenSize QHD;
        public static final /* enum */ ScreenSize WUXGA;
        private final int mHeight;
        private final int mWidth;

        static {
            WUXGA = new ScreenSize(1920, 1200);
            FULL_HD = new ScreenSize(1920, 1080);
            HD = new ScreenSize(1280, 720);
            QHD = new ScreenSize(960, 540);
            FWVGA = new ScreenSize(854, 480);
            HVGA = new ScreenSize(640, 480);
            ScreenSize[] arrscreenSize = new ScreenSize[]{WUXGA, FULL_HD, HD, QHD, FWVGA, HVGA};
            $VALUES = arrscreenSize;
        }

        private ScreenSize(int n2, int n3) {
            super(string, n);
            this.mWidth = n2;
            this.mHeight = n3;
        }

        public static ScreenSize valueOf(String string) {
            return (ScreenSize)Enum.valueOf((Class)ScreenSize.class, (String)string);
        }

        public static ScreenSize[] values() {
            return (ScreenSize[])$VALUES.clone();
        }

        public Rect getAsRect() {
            return new Rect(0, 0, this.mWidth, this.mHeight);
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getWidth() {
            return this.mWidth;
        }
    }

}

