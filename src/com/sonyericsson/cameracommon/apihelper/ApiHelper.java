/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Field
 *  java.lang.reflect.Method
 */
package com.sonyericsson.cameracommon.apihelper;

public class ApiHelper {
    public static int getIntFieldIfExists(Class<?> class_, String string, Class<?> class_2, int n) {
        try {
            int n2 = class_.getDeclaredField(string).getInt(class_2);
            return n2;
        }
        catch (Exception var4_5) {
            return n;
        }
    }

    private static boolean hasClass(String string) {
        try {
            Class.forName((String)string);
            return true;
        }
        catch (Throwable var1_1) {
            return false;
        }
    }

    private static boolean hasField(Class<?> class_, String string) {
        try {
            class_.getDeclaredField(string);
            return true;
        }
        catch (NoSuchFieldException var2_2) {
            return false;
        }
    }

    private static boolean hasField(String string, String string2) {
        try {
            boolean bl = ApiHelper.hasField(Class.forName((String)string), string2);
            return bl;
        }
        catch (Throwable var2_3) {
            return false;
        }
    }

    private static boolean hasMethod(String string, String string2) {
        try {
            Class.forName((String)string).getDeclaredMethod(string2, new Class[0]);
            return true;
        }
        catch (Throwable var2_2) {
            return false;
        }
    }

    private static /* varargs */ boolean hasMethod(String string, String string2, Class<?> ... arrclass) {
        try {
            Class.forName((String)string).getDeclaredMethod(string2, arrclass);
            return true;
        }
        catch (Throwable var3_3) {
            return false;
        }
    }

    private static /* varargs */ boolean hasMethod(String string, String string2, String ... arrstring) {
        Class class_ = Class.forName((String)string);
        Class[] arrclass = new Class[arrstring.length];
        int n = 0;
        do {
            if (n >= arrstring.length) break;
            arrclass[n] = Class.forName((String)arrstring[n]);
            ++n;
        } while (true);
        try {
            class_.getDeclaredMethod(string2, arrclass);
            return true;
        }
        catch (Throwable var3_6) {
            return false;
        }
    }

    public static boolean hasSpacialApis() {
        if (ApiHelper.hasClass("android.os.storage.StorageManager") && ApiHelper.hasClass("android.os.storage.StorageVolume") && ApiHelper.hasClass("android.os.storage.StorageManager$StorageType") && ApiHelper.hasMethod("android.os.storage.StorageManager", "getVolumeList") && ApiHelper.hasMethod("android.os.storage.StorageManager", "getVolumePath", "android.os.storage.StorageManager$StorageType") && ApiHelper.hasMethod("android.os.storage.StorageManager", "getVolumeType", new Class[]{String.class}) && ApiHelper.hasMethod("android.os.storage.StorageManager", "getVolumeState", new Class[]{String.class}) && ApiHelper.hasField("com.android.internal.R$id", "message") && ApiHelper.hasClass("com.sonyericsson.provider.SemcMediaStore") && ApiHelper.hasClass("com.sonyericsson.provider.SemcMediaStore$ExtendedFiles$ExtendedFileColumns")) {
            return true;
        }
        return false;
    }
}

