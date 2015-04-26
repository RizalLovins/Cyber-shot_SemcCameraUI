/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.EnumMap
 *  java.util.Map
 */
package com.sonyericsson.android.camera.controller;

import java.util.EnumMap;
import java.util.Map;

public final class EventFilter
extends Enum<EventFilter> {
    private static final /* synthetic */ EventFilter[] $VALUES;
    public static final /* enum */ EventFilter KEY_ZOOMING;
    public static final /* enum */ EventFilter PINCH_ZOOMING;
    public static final /* enum */ EventFilter PREVIEWING;
    public static final /* enum */ EventFilter SELF_TIMER_RUNNING;
    public static final /* enum */ EventFilter TOUCH_CAPTURE_HOLDING;
    public static final /* enum */ EventFilter TOUCH_FOCUS_DRAGGING;
    public static final /* enum */ EventFilter UNKNOWN;
    private static Map<EventFilter, EventFilter[]> sStateTransitionMap;

    static {
        UNKNOWN = new EventFilter();
        PREVIEWING = new EventFilter();
        TOUCH_FOCUS_DRAGGING = new EventFilter();
        SELF_TIMER_RUNNING = new EventFilter();
        KEY_ZOOMING = new EventFilter();
        PINCH_ZOOMING = new EventFilter();
        TOUCH_CAPTURE_HOLDING = new EventFilter();
        EventFilter[] arreventFilter = new EventFilter[]{UNKNOWN, PREVIEWING, TOUCH_FOCUS_DRAGGING, SELF_TIMER_RUNNING, KEY_ZOOMING, PINCH_ZOOMING, TOUCH_CAPTURE_HOLDING};
        $VALUES = arreventFilter;
        EnumMap enumMap = EventFilter.sStateTransitionMap = new EnumMap((Class)EventFilter.class);
        EventFilter eventFilter = UNKNOWN;
        EventFilter[] arreventFilter2 = new EventFilter[]{UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, PREVIEWING, UNKNOWN, UNKNOWN, UNKNOWN};
        enumMap.put((Object)eventFilter, (Object)arreventFilter2);
        Map<EventFilter, EventFilter[]> map = sStateTransitionMap;
        EventFilter eventFilter2 = PREVIEWING;
        EventFilter[] arreventFilter3 = new EventFilter[]{SELF_TIMER_RUNNING, PREVIEWING, TOUCH_FOCUS_DRAGGING, PREVIEWING, KEY_ZOOMING, PREVIEWING, PINCH_ZOOMING, PREVIEWING, UNKNOWN, TOUCH_CAPTURE_HOLDING, PREVIEWING};
        map.put((Object)eventFilter2, (Object)arreventFilter3);
        Map<EventFilter, EventFilter[]> map2 = sStateTransitionMap;
        EventFilter eventFilter3 = TOUCH_FOCUS_DRAGGING;
        EventFilter[] arreventFilter4 = new EventFilter[]{TOUCH_FOCUS_DRAGGING, TOUCH_FOCUS_DRAGGING, TOUCH_FOCUS_DRAGGING, PREVIEWING, TOUCH_FOCUS_DRAGGING, TOUCH_FOCUS_DRAGGING, PINCH_ZOOMING, PREVIEWING, UNKNOWN, TOUCH_FOCUS_DRAGGING, TOUCH_FOCUS_DRAGGING};
        map2.put((Object)eventFilter3, (Object)arreventFilter4);
        Map<EventFilter, EventFilter[]> map3 = sStateTransitionMap;
        EventFilter eventFilter4 = SELF_TIMER_RUNNING;
        EventFilter[] arreventFilter5 = new EventFilter[]{SELF_TIMER_RUNNING, PREVIEWING, SELF_TIMER_RUNNING, SELF_TIMER_RUNNING, SELF_TIMER_RUNNING, SELF_TIMER_RUNNING, SELF_TIMER_RUNNING, PREVIEWING, UNKNOWN, SELF_TIMER_RUNNING, SELF_TIMER_RUNNING};
        map3.put((Object)eventFilter4, (Object)arreventFilter5);
        Map<EventFilter, EventFilter[]> map4 = sStateTransitionMap;
        EventFilter eventFilter5 = KEY_ZOOMING;
        EventFilter[] arreventFilter6 = new EventFilter[]{KEY_ZOOMING, KEY_ZOOMING, KEY_ZOOMING, KEY_ZOOMING, KEY_ZOOMING, PREVIEWING, KEY_ZOOMING, PREVIEWING, UNKNOWN, KEY_ZOOMING, KEY_ZOOMING};
        map4.put((Object)eventFilter5, (Object)arreventFilter6);
        Map<EventFilter, EventFilter[]> map5 = sStateTransitionMap;
        EventFilter eventFilter6 = PINCH_ZOOMING;
        EventFilter[] arreventFilter7 = new EventFilter[]{PINCH_ZOOMING, PINCH_ZOOMING, PINCH_ZOOMING, PINCH_ZOOMING, PINCH_ZOOMING, PREVIEWING, PINCH_ZOOMING, PREVIEWING, UNKNOWN, PINCH_ZOOMING, PINCH_ZOOMING};
        map5.put((Object)eventFilter6, (Object)arreventFilter7);
        Map<EventFilter, EventFilter[]> map6 = sStateTransitionMap;
        EventFilter eventFilter7 = TOUCH_CAPTURE_HOLDING;
        EventFilter[] arreventFilter8 = new EventFilter[]{TOUCH_CAPTURE_HOLDING, TOUCH_CAPTURE_HOLDING, TOUCH_CAPTURE_HOLDING, TOUCH_CAPTURE_HOLDING, TOUCH_CAPTURE_HOLDING, TOUCH_CAPTURE_HOLDING, PINCH_ZOOMING, PREVIEWING, UNKNOWN, TOUCH_CAPTURE_HOLDING, PREVIEWING};
        map6.put((Object)eventFilter7, (Object)arreventFilter8);
    }

    private EventFilter() {
        super(string, n);
    }

    public static EventFilter getEventFilter(EventFilter eventFilter, int n) {
        return ((EventFilter[])sStateTransitionMap.get((Object)eventFilter))[n];
    }

    public static EventFilter valueOf(String string) {
        return (EventFilter)Enum.valueOf((Class)EventFilter.class, (String)string);
    }

    public static EventFilter[] values() {
        return (EventFilter[])$VALUES.clone();
    }

    public static class EventFilterMessage {
        public static final int KEY_ZOOM_START = 4;
        public static final int PINCH_ZOOM_START = 6;
        public static final int RESET = 7;
        public static final int SELFTIMER_FINISH = 1;
        public static final int SELFTIMER_START = 0;
        public static final int STOP = 8;
        public static final int TOUCH_CAPTURE_FINISH = 10;
        public static final int TOUCH_CAPTURE_START = 9;
        public static final int TOUCH_FOCUS_FINISH = 3;
        public static final int TOUCH_FOCUS_START = 2;
        public static final int ZOOM_FINISH = 5;
    }

}

