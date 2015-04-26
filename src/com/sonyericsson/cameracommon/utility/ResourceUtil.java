/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.XmlResourceParser
 *  android.graphics.drawable.Drawable
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory
 *  android.view.View
 *  android.view.ViewGroup
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  org.xmlpull.v1.XmlPullParser
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import org.xmlpull.v1.XmlPullParser;

public class ResourceUtil {
    public static final int INVALID_RESOURCE_ID = -1;
    private static final String PACKAGE_NAME = "com.sonymobile.cameracommon";

    public static String getApplicationLabel(Context context, String string) {
        PackageManager packageManager = context.getPackageManager();
        try {
            String string2 = (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo(string, 0));
            return string2;
        }
        catch (PackageManager.NameNotFoundException var3_4) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var3_4);
            throw new RuntimeException();
        }
    }

    public static boolean getBoolean(Context context, int n) {
        return ResourceUtil.getBoolean(context, "com.sonymobile.cameracommon", n);
    }

    public static boolean getBoolean(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            boolean bl = packageManager.getResourcesForApplication(string).getBoolean(n);
            return bl;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static ColorStateList getColorStateList(Context context, int n) {
        return ResourceUtil.getColorStateList(context, "com.sonymobile.cameracommon", n);
    }

    public static ColorStateList getColorStateList(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ColorStateList colorStateList = packageManager.getResourcesForApplication(string).getColorStateList(n);
            return colorStateList;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static float getDimension(Context context, int n) {
        return ResourceUtil.getDimension(context, "com.sonymobile.cameracommon", n);
    }

    public static float getDimension(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            float f = packageManager.getResourcesForApplication(string).getDimension(n);
            return f;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static int getDimensionPixelOffset(Context context, int n) {
        return ResourceUtil.getDimensionPixelOffset(context, "com.sonymobile.cameracommon", n);
    }

    public static int getDimensionPixelOffset(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int n2 = packageManager.getResourcesForApplication(string).getDimensionPixelOffset(n);
            return n2;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static int getDimensionPixelSize(Context context, int n) {
        return ResourceUtil.getDimensionPixelSize(context, "com.sonymobile.cameracommon", n);
    }

    public static int getDimensionPixelSize(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int n2 = packageManager.getResourcesForApplication(string).getDimensionPixelSize(n);
            return n2;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static Drawable getDrawable(Context context, int n) {
        return ResourceUtil.getDrawable(context, "com.sonymobile.cameracommon", n);
    }

    public static Drawable getDrawable(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            Drawable drawable = packageManager.getResourcesForApplication(string).getDrawable(n);
            return drawable;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static float getFloat(Context context, int n) {
        return Float.valueOf((String)context.getResources().getString(n)).floatValue();
    }

    public static int getInteger(Context context, int n) {
        return ResourceUtil.getInteger(context, "com.sonymobile.cameracommon", n);
    }

    public static int getInteger(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int n2 = packageManager.getResourcesForApplication(string).getInteger(n);
            return n2;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            throw new RuntimeException();
        }
    }

    public static View getLayout(Context context, int n, ViewGroup viewGroup, LayoutInflater.Factory factory) {
        return ResourceUtil.getLayout(context, "com.sonymobile.cameracommon", n, viewGroup, factory);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static View getLayout(Context context, String string, int n, ViewGroup viewGroup, LayoutInflater.Factory factory) {
        LayoutInflater layoutInflater;
        XmlResourceParser xmlResourceParser;
        PackageManager packageManager = context.getPackageManager();
        try {
            xmlResourceParser = packageManager.getResourcesForApplication(string).getLayout(n);
            layoutInflater = (LayoutInflater)context.createPackageContext(string, 2).getSystemService("layout_inflater");
            if (factory == null) return layoutInflater.inflate((XmlPullParser)xmlResourceParser, viewGroup);
        }
        catch (PackageManager.NameNotFoundException var6_9) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var6_9);
            return null;
        }
        layoutInflater.setFactory(factory);
        return layoutInflater.inflate((XmlPullParser)xmlResourceParser, viewGroup);
    }

    public static int getPixelFromRate(Context context, int n, int n2) {
        return (int)((float)n2 * ResourceUtil.getFloat(context, n) / 100.0f);
    }

    public static String getString(Context context, int n) {
        return ResourceUtil.getString(context, "com.sonymobile.cameracommon", n);
    }

    public static String getString(Context context, String string, int n) {
        PackageManager packageManager = context.getPackageManager();
        try {
            String string2 = packageManager.getResourcesForApplication(string).getString(n);
            return string2;
        }
        catch (PackageManager.NameNotFoundException var4_5) {
            CameraLogger.e("ResourceUtil", "", (Throwable)var4_5);
            return null;
        }
    }
}

