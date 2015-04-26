/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.view.View
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.setting.layoutcoordinator;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import com.sonyericsson.cameracommon.utility.RotationUtil;

class LayoutCoordinateUtil {
    LayoutCoordinateUtil() {
    }

    public static Rect coodinatePosition(int n, View view, Rect rect, Rect rect2, Point point) {
        int n2 = rect.left - rect2.left;
        int n3 = rect.top - rect2.top;
        view.setLeft(n2);
        view.setRight(n2 + rect.width());
        view.setTop(n3);
        view.setBottom(n3 + rect.height());
        view.setPivotX((float)(- view.getLeft()));
        view.setPivotY((float)(- view.getTop()));
        view.setRotation(RotationUtil.getAngle(n));
        if (n == 1) {
            int n4 = point.x;
            int n5 = point.y + rect2.width();
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.offsetLeftAndRight(n4);
            view.offsetTopAndBottom(n5);
            Rect rect3 = new Rect(0, 0, rect.height(), rect.width());
            rect3.offset(n4, n5 - rect2.width());
            return rect3;
        }
        int n6 = point.x;
        int n7 = point.y;
        view.setTranslationX((float)n6);
        view.setTranslationY((float)n7);
        Rect rect4 = new Rect(0, 0, rect.width(), rect.height());
        rect4.offset(n6 + view.getLeft(), n7 + view.getTop());
        return rect4;
    }
}

