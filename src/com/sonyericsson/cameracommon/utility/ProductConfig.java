/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.view.Display
 *  android.view.WindowManager
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.sonyericsson.cameracommon.R;

public class ProductConfig {
    private ProductConfig() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int getMountAngle(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        int n = windowManager.getDefaultDisplay().getRotation();
        int n2 = 0;
        switch (n) {
            case 0: {
                n2 = 0;
                break;
            }
            case 1: {
                n2 = 270;
                break;
            }
            case 2: {
                n2 = 180;
                break;
            }
            case 3: {
                n2 = 90;
            }
        }
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if (point.x > point.y) {
            return n2;
        }
        return n2 + 270;
    }

    public static boolean isPhone(Context context) {
        if (!ProductConfig.isTablet(context)) {
            return true;
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean shouldReverseZoomDirection(Context context, boolean bl) {
        if (ProductConfig.isTablet(context) || !bl) {
            return false;
        }
        return true;
    }
}

