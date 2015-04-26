/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.viewfinder;

import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;

public final class DefaultLayoutPattern
extends Enum<DefaultLayoutPattern>
implements LayoutPattern {
    private static final /* synthetic */ DefaultLayoutPattern[] $VALUES;
    public static final /* enum */ DefaultLayoutPattern BURST_SHOOTING;
    public static final /* enum */ DefaultLayoutPattern CAPTURE;
    public static final /* enum */ DefaultLayoutPattern CLEAR;
    public static final /* enum */ DefaultLayoutPattern FOCUS_DONE;
    public static final /* enum */ DefaultLayoutPattern FOCUS_SEARCHING;
    public static final /* enum */ DefaultLayoutPattern MODE_SELECTOR;
    public static final /* enum */ DefaultLayoutPattern PAUSE_RECORDING;
    public static final /* enum */ DefaultLayoutPattern PREVIEW;
    public static final /* enum */ DefaultLayoutPattern RECORDING;
    public static final /* enum */ DefaultLayoutPattern SELFTIMER;
    public static final /* enum */ DefaultLayoutPattern SETTING;
    public static final /* enum */ DefaultLayoutPattern ZOOMING;

    static {
        PREVIEW = new DefaultLayoutPattern();
        CLEAR = new DefaultLayoutPattern();
        ZOOMING = new DefaultLayoutPattern();
        FOCUS_SEARCHING = new DefaultLayoutPattern();
        FOCUS_DONE = new DefaultLayoutPattern();
        CAPTURE = new DefaultLayoutPattern();
        BURST_SHOOTING = new DefaultLayoutPattern();
        RECORDING = new DefaultLayoutPattern();
        MODE_SELECTOR = new DefaultLayoutPattern();
        SETTING = new DefaultLayoutPattern();
        SELFTIMER = new DefaultLayoutPattern();
        PAUSE_RECORDING = new DefaultLayoutPattern();
        DefaultLayoutPattern[] arrdefaultLayoutPattern = new DefaultLayoutPattern[]{PREVIEW, CLEAR, ZOOMING, FOCUS_SEARCHING, FOCUS_DONE, CAPTURE, BURST_SHOOTING, RECORDING, MODE_SELECTOR, SETTING, SELFTIMER, PAUSE_RECORDING};
        $VALUES = arrdefaultLayoutPattern;
    }

    private DefaultLayoutPattern() {
        super(string, n);
    }

    public static DefaultLayoutPattern valueOf(String string) {
        return (DefaultLayoutPattern)Enum.valueOf((Class)DefaultLayoutPattern.class, (String)string);
    }

    public static DefaultLayoutPattern[] values() {
        return (DefaultLayoutPattern[])$VALUES.clone();
    }
}

