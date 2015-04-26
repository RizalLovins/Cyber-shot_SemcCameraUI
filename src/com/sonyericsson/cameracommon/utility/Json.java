/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Type
 */
package com.sonyericsson.cameracommon.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
    private static final Object GSON_SYNC_LOCK;
    private static final String TAG;

    static {
        TAG = Json.class.getSimpleName();
        GSON_SYNC_LOCK = new Object();
    }

    private static Gson createGson(double d) {
        return new GsonBuilder().serializeNulls().setVersion(d).excludeFieldsWithoutExposeAnnotation().create();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static <T> T decodeFromJson(double d, String string, Class<T> class_) {
        Object object;
        Object object2 = object = GSON_SYNC_LOCK;
        synchronized (object2) {
            Object object3 = Json.createGson(d).fromJson(string, class_);
            return (T)object3;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public static <T> String encodeToJson(double d, T t, Class<T> class_) {
        Object object;
        Object object2 = object = GSON_SYNC_LOCK;
        synchronized (object2) {
            return Json.createGson(d).toJson(t, class_);
        }
    }
}

