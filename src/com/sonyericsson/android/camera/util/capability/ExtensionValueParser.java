/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.util.capability;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;

public class ExtensionValueParser {
    public static final String CONNECTOR = "x";
    public static final String DELIMITER = ",";
    private static final String TAG = "ExtensionValueParser";

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean getBoolean(String string) {
        boolean bl = false;
        if (string == null) return bl;
        if (string.indexOf("on") != -1) {
            return true;
        }
        int n = string.indexOf("true");
        bl = false;
        if (n == -1) return bl;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final int getInt(String string) {
        int n = 0;
        if (string == null) return n;
        try {
            int n2 = Integer.valueOf((String)string);
            return n2;
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static final Rect getRect(String string) {
        Rect rect = null;
        if (string != null) {
            String[] arrstring = string.split("x");
            int n = arrstring.length;
            rect = null;
            if (n == 2) {
                rect = new Rect();
                rect.right = Integer.valueOf((String)arrstring[0]);
                rect.bottom = Integer.valueOf((String)arrstring[1]);
            }
        }
        return rect;
    }

    public static final List<String> getStringList(String string) {
        ArrayList arrayList = new ArrayList();
        if (string != null) {
            String[] arrstring = string.split(",");
            int n = arrstring.length;
            for (int i = 0; i < n; ++i) {
                arrayList.add((Object)arrstring[i]);
            }
        }
        return arrayList;
    }
}

