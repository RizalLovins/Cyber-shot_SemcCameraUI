/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.cameracommon.R;

public class CommonResources {

    public static class FaceIndicator {
        public static final int FAIL;
        public static final int NORMAL;
        public static final int PRIORITY;
        public static final int SUCCESS;
        public static final int TRANSPARENT;

        static {
            NORMAL = R.drawable.cam_focus_indicator_white_for_touch_cap_icn;
            SUCCESS = R.drawable.cam_focus_indicator_green_for_touch_cap_icn;
            PRIORITY = R.drawable.cam_focus_indicator_yellow_for_touch_cap_icn;
        }
    }

    public static class ObjectIndicator {
        public static final int FAIL;
        public static final int SUCCESS;
        public static final int TRACKING;

        static {
            TRACKING = R.drawable.cam_focus_indicator_object_tracking_icn;
            SUCCESS = R.drawable.cam_focus_indicator_object_tracking_success_icn;
        }
    }

    public static class SingleIndicator {
        public static final int FAIL;
        public static final int NORMAL;
        public static final int SUCCESS;

        static {
            NORMAL = R.drawable.cam_focus_indicator_white_icn;
            SUCCESS = R.drawable.cam_focus_indicator_green_icn;
        }
    }

    public static class TouchIndicator {
        public static final int FAIL;
        public static final int NORMAL;
        public static final int SUCCESS;

        static {
            NORMAL = R.drawable.cam_focus_indicator_white_for_touch_cap_icn;
            SUCCESS = R.drawable.cam_focus_indicator_green_for_touch_cap_icn;
        }
    }

}

