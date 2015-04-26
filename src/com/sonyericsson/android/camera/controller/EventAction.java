/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.controller;

public final class EventAction
extends Enum<EventAction> {
    private static final /* synthetic */ EventAction[] $VALUES;
    public static final /* enum */ EventAction CANCEL;
    public static final /* enum */ EventAction DOWN;
    public static final /* enum */ EventAction MOVE;
    public static final /* enum */ EventAction STOP;
    public static final /* enum */ EventAction STRETCH;
    public static final /* enum */ EventAction UNKNOWN;
    public static final /* enum */ EventAction UP;

    static {
        DOWN = new EventAction();
        STOP = new EventAction();
        MOVE = new EventAction();
        CANCEL = new EventAction();
        UP = new EventAction();
        STRETCH = new EventAction();
        UNKNOWN = new EventAction();
        EventAction[] arreventAction = new EventAction[]{DOWN, STOP, MOVE, CANCEL, UP, STRETCH, UNKNOWN};
        $VALUES = arreventAction;
    }

    private EventAction() {
        super(string, n);
    }

    public static EventAction valueOf(String string) {
        return (EventAction)Enum.valueOf((Class)EventAction.class, (String)string);
    }

    public static EventAction[] values() {
        return (EventAction[])$VALUES.clone();
    }
}

