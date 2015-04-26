/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.android.camera.util.capability;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SharedPrefsTranslator {
    public static final String CONNECTOR = "x";
    public static final String DELIMITER = ";";
    private static final String TAG = "SharedPrefsTranslator";

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String fromIntArrayList(List<int[]> list) {
        String string = "";
        if (list == null) return string;
        if (list.isEmpty()) return string;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(list.size());
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            int[] arrn = (int[])iterator.next();
            if (arrn.length != 2) continue;
            stringBuilder.append(";");
            String string2 = String.valueOf((int)arrn[0]);
            String string3 = String.valueOf((int)arrn[1]);
            stringBuilder.append(string2).append("x").append(string3);
        }
        return stringBuilder.toString();
    }

    public static final String fromRect(Rect rect) {
        String string = "";
        if (rect != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(rect.width()).append("x").append(rect.height());
            string = stringBuilder.toString();
        }
        return string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String fromRectList(List<Rect> list) {
        String string = "";
        if (list == null) return string;
        if (list.isEmpty()) return string;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(list.size());
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Rect rect = (Rect)iterator.next();
            stringBuilder.append(";");
            stringBuilder.append(rect.width()).append("x").append(rect.height());
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String fromStringList(List<String> list) {
        String string = "";
        if (list == null) return string;
        if (list.isEmpty()) return string;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(list.size());
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String string2 = (String)iterator.next();
            stringBuilder.append(";").append(string2);
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final int[] getIntArray(String string) {
        String[] arrstring;
        int[] arrn = new int[2];
        if (string == null || (arrstring = string.split("x")).length != 2) return arrn;
        try {
            arrn[0] = Integer.parseInt((String)arrstring[0]);
            arrn[1] = Integer.parseInt((String)arrstring[1]);
        }
        catch (NumberFormatException numberFormatException) {
            return new int[0];
        }
        return arrn;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final List<int[]> getIntArrayList(String string) {
        int n;
        ArrayList arrayList = new ArrayList();
        if (string == null || string.isEmpty()) return arrayList;
        String[] arrstring = string.split(";");
        try {
            if (Integer.parseInt((String)arrstring[0]) <= 0) return arrayList;
            n = arrstring.length;
        }
        catch (NumberFormatException var3_5) {
            arrayList.clear();
        }
        for (int i = 0; i < n; ++i) {
            arrayList.add((Object)SharedPrefsTranslator.getIntArray(arrstring[i]));
        }
        return arrayList;
        return arrayList;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final Rect getRect(String string) {
        String[] arrstring;
        Rect rect = new Rect();
        if (string == null || (arrstring = string.split("x")).length != 2) return rect;
        try {
            rect.right = Integer.parseInt((String)arrstring[0]);
            rect.bottom = Integer.parseInt((String)arrstring[1]);
        }
        catch (NumberFormatException numberFormatException) {
            rect.setEmpty();
            return rect;
        }
        return rect;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final List<Rect> getRectList(String string) {
        int n;
        ArrayList arrayList = new ArrayList();
        if (string == null || string.isEmpty()) return arrayList;
        String[] arrstring = string.split(";");
        try {
            if (Integer.parseInt((String)arrstring[0]) <= 0) return arrayList;
            n = arrstring.length;
        }
        catch (NumberFormatException var3_5) {
            arrayList.clear();
        }
        for (int i = 0; i < n; ++i) {
            arrayList.add((Object)SharedPrefsTranslator.getRect(arrstring[i]));
        }
        return arrayList;
        return arrayList;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final List<String> getStringList(String string) {
        int n;
        ArrayList arrayList = new ArrayList();
        if (string == null || string.isEmpty()) return arrayList;
        String[] arrstring = string.split(";");
        try {
            if (Integer.parseInt((String)arrstring[0]) <= 0) return arrayList;
            n = arrstring.length;
        }
        catch (NumberFormatException var3_5) {
            arrayList.clear();
        }
        for (int i = 0; i < n; ++i) {
            arrayList.add((Object)arrstring[i]);
        }
        return arrayList;
        return arrayList;
    }
}

