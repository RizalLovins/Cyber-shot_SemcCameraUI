/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public final class CoordinateUtil {
    private static final float ROUNDING = 0.5f;
    private static final String TAG = "CoordinateUtil";

    public static int convertAbsolutePosition2Relative(int n, int n2) {
        return n * 100 / n2;
    }

    public static Rect convertDev2View(Rect rect, Rect rect2, int n) {
        Rect rect3 = new Rect(rect);
        if (n == 1) {
            rect3.left = rect2.height() - rect.bottom;
            rect3.top = rect.left;
            rect3.right = rect2.height() - rect.top;
            rect3.bottom = rect.right;
        }
        return rect3;
    }

    public static int convertDip2Px(Context context, int n) {
        return (int)(0.5f + context.getResources().getDisplayMetrics().density * (float)n);
    }

    public static Point convertDip2Px(Context context, Point point) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Point((int)(0.5f + f * (float)point.x), (int)(0.5f + f * (float)point.y));
    }

    public static Rect convertDip2Px(Context context, Rect rect) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Rect((int)(0.5f + f * (float)rect.left), (int)(0.5f + f * (float)rect.top), (int)(0.5f + f * (float)rect.right), (int)(0.5f + f * (float)rect.bottom));
    }

    public static Rect convertPositionToAligned(int n, int n2, Rect rect, Rect rect2, int n3, int n4) {
        if (rect == null) {
            return new Rect();
        }
        if (rect2 == null) {
            return new Rect();
        }
        if (rect.contains(n, n2)) {
            int n5 = rect.left;
            int n6 = rect.top;
            int n7 = n - n5 - n3 / 2;
            int n8 = n2 - n6 - n4 / 2;
            Rect rect3 = new Rect(rect2.left - n5, rect2.top - n6, rect2.right - n5 - n3, rect2.bottom - n6 - n4);
            int n9 = Math.max((int)rect3.left, (int)Math.min((int)rect3.right, (int)n7));
            int n10 = Math.max((int)rect3.top, (int)Math.min((int)rect3.bottom, (int)n8));
            return new Rect(n9, n10, n9 + n3, n10 + n4);
        }
        return new Rect();
    }

    public static Rect[] convertPositionToSurface(RectF[] arrrectF, int n, int n2, int n3) {
        Rect[] arrrect = new Rect[arrrectF.length];
        for (int i = 0; i < arrrectF.length; ++i) {
            int n4 = (int)(arrrectF[i].centerX() * (float)n);
            int n5 = (int)(arrrectF[i].centerY() * (float)n2);
            int n6 = (int)(arrrectF[i].width() * (float)n);
            int n7 = (int)(arrrectF[i].height() * (float)n2);
            arrrect[i] = new Rect(n4 - n6 / 2, n5 - n7 / 2, n4 + n6 / 2, n5 + n7 / 2);
        }
        return arrrect;
    }

    public static int convertPx2Dip(Context context, int n) {
        float f = context.getResources().getDisplayMetrics().density;
        return (int)(0.5f + (float)n / f);
    }

    public static Point convertPx2Dip(Context context, Point point) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Point((int)(0.5f + (float)point.x / f), (int)(0.5f + (float)point.y / f));
    }

    public static Rect convertPx2Dip(Context context, Rect rect) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Rect((int)(0.5f + (float)rect.left / f), (int)(0.5f + (float)rect.top / f), (int)(0.5f + (float)rect.right / f), (int)(0.5f + (float)rect.bottom / f));
    }

    public static Rect convertView2Dev(Rect rect, Rect rect2, int n) {
        Rect rect3 = new Rect(rect);
        if (n == 1) {
            rect3.left = rect.top;
            rect3.top = rect2.height() - rect.right;
            rect3.right = rect.bottom;
            rect3.bottom = rect2.height() - rect.left;
        }
        return rect3;
    }

    public static Rect scale(Rect rect, Rect rect2, Rect rect3) {
        int n = rect2.height();
        int n2 = rect2.width();
        int n3 = rect3.height();
        int n4 = rect3.width();
        int n5 = n3 * rect.top / n;
        int n6 = n4 * rect.left / n2;
        int n7 = n3 * rect.bottom / n;
        return new Rect(n6, n5, n4 * rect.right / n2, n7);
    }

    public static Rect scale2Dev(Rect rect, Rect rect2, Rect rect3) {
        int n = rect2.height();
        int n2 = rect2.width();
        int n3 = rect3.height();
        int n4 = rect3.width();
        double d = (double)n2 / (double)n4;
        double d2 = (double)n / (double)n3;
        double d3 = d2 * (double)rect.top;
        double d4 = d * (double)rect.left;
        double d5 = d2 * (double)rect.bottom;
        double d6 = d * (double)rect.right;
        return new Rect((int)d4, (int)d3, (int)d6, (int)d5);
    }
}

