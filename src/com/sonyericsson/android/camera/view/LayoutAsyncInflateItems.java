/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.sonyericsson.android.camera.view;

import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import java.util.ArrayList;
import java.util.List;

public class LayoutAsyncInflateItems {
    private static final List<InflateItem> INFLATE_ITEMS_FOR_LAUNCH = new ArrayList();

    public static List<InflateItem> getItems() {
        if (INFLATE_ITEMS_FOR_LAUNCH.size() == 0) {
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.RECTANGLE_SINGLE);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.RECTANGLE_TOUCH);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.RECTANGLE_FACE);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.RECTANGLE_OBJECT_TRACKING);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.HEAD_UP_DISPLAY);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.SPECIFIC_VIEWFIDER_LAYOUT);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.NOTIFICATION);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.CAMERA_INDICATORS);
            INFLATE_ITEMS_FOR_LAUNCH.add((Object)CameraInflateItem.AUTO_REVIEW);
        }
        return INFLATE_ITEMS_FOR_LAUNCH;
    }

    public static final class CameraInflateItem
    extends Enum<CameraInflateItem>
    implements InflateItem {
        private static final /* synthetic */ CameraInflateItem[] $VALUES;
        public static final /* enum */ CameraInflateItem AUTO_REVIEW;
        public static final /* enum */ CameraInflateItem CAMERA_INDICATORS;
        public static final /* enum */ CameraInflateItem FAST_CAPTURING_VIEWFINDER_ITEMS;
        public static final /* enum */ CameraInflateItem HEAD_UP_DISPLAY;
        public static final /* enum */ CameraInflateItem NOTIFICATION;
        public static final /* enum */ CameraInflateItem RECTANGLE_FACE;
        public static final /* enum */ CameraInflateItem RECTANGLE_FAST_OBJECT_TRACKING;
        public static final /* enum */ CameraInflateItem RECTANGLE_FAST_SINGLE;
        public static final /* enum */ CameraInflateItem RECTANGLE_FAST_TOUCH;
        public static final /* enum */ CameraInflateItem RECTANGLE_OBJECT_TRACKING;
        public static final /* enum */ CameraInflateItem RECTANGLE_SINGLE;
        public static final /* enum */ CameraInflateItem RECTANGLE_TOUCH;
        public static final /* enum */ CameraInflateItem SMART_COVER_VIEWFINDER_ITEMS;
        public static final /* enum */ CameraInflateItem SPECIFIC_VIEWFIDER_LAYOUT;
        protected final int mInflateId;
        protected final int mViewCount;

        static {
            HEAD_UP_DISPLAY = new CameraInflateItem(2130903091, 1);
            RECTANGLE_SINGLE = new CameraInflateItem(2130903040, 1);
            RECTANGLE_TOUCH = new CameraInflateItem(2130903040, 1);
            RECTANGLE_FACE = new CameraInflateItem(2130903056, 5);
            RECTANGLE_OBJECT_TRACKING = new CameraInflateItem(2130903040, 1);
            RECTANGLE_FAST_SINGLE = new CameraInflateItem(2130903057, 1);
            RECTANGLE_FAST_TOUCH = new CameraInflateItem(2130903057, 1);
            RECTANGLE_FAST_OBJECT_TRACKING = new CameraInflateItem(2130903056, 1);
            SPECIFIC_VIEWFIDER_LAYOUT = new CameraInflateItem(2130903082, 1);
            NOTIFICATION = new CameraInflateItem(2130903063, 1);
            CAMERA_INDICATORS = new CameraInflateItem(2130903045, 1);
            AUTO_REVIEW = new CameraInflateItem(2130903041, 1);
            FAST_CAPTURING_VIEWFINDER_ITEMS = new CameraInflateItem(2130903058, 1);
            SMART_COVER_VIEWFINDER_ITEMS = new CameraInflateItem(2130903081, 1);
            CameraInflateItem[] arrcameraInflateItem = new CameraInflateItem[]{HEAD_UP_DISPLAY, RECTANGLE_SINGLE, RECTANGLE_TOUCH, RECTANGLE_FACE, RECTANGLE_OBJECT_TRACKING, RECTANGLE_FAST_SINGLE, RECTANGLE_FAST_TOUCH, RECTANGLE_FAST_OBJECT_TRACKING, SPECIFIC_VIEWFIDER_LAYOUT, NOTIFICATION, CAMERA_INDICATORS, AUTO_REVIEW, FAST_CAPTURING_VIEWFINDER_ITEMS, SMART_COVER_VIEWFINDER_ITEMS};
            $VALUES = arrcameraInflateItem;
        }

        private CameraInflateItem(int n2, int n3) {
            super(string, n);
            this.mInflateId = n2;
            this.mViewCount = n3;
        }

        public static CameraInflateItem valueOf(String string) {
            return (CameraInflateItem)Enum.valueOf((Class)CameraInflateItem.class, (String)string);
        }

        public static CameraInflateItem[] values() {
            return (CameraInflateItem[])$VALUES.clone();
        }

        @Override
        public int getLayoutId() {
            return this.mInflateId;
        }

        @Override
        public int getViewCount() {
            return this.mViewCount;
        }
    }

}

