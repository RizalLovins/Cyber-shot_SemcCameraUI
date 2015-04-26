/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util;

public class DurationParameterSet {
    private static final String TAG = "DurationParameterSet";
    public int hour = 0;
    public int min = 0;
    public int sec = 0;

    public void update(int n) {
        int n2 = n / 1000;
        this.sec = n2 % 60;
        int n3 = (n2 - this.sec) / 60;
        this.min = n3 % 60;
        this.hour = (n3 - this.min) / 60;
    }
}

