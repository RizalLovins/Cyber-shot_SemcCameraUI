/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Bundle
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.sonyericsson.cameracommon.utility.CameraLogger;

public class AccessibilityHelper {
    public static final String TAG = "AccessibilityHelper";
    private static final Rect sRectForHit = new Rect();

    /*
     * Enabled aggressive block sorting
     */
    private static boolean checkToTouch(View view, int n, int n2) {
        if (!(view != null && view.getVisibility() == 0 && view.getGlobalVisibleRect(sRectForHit) && sRectForHit.contains(n, n2))) {
            return false;
        }
        return true;
    }

    public static View requestAccessibilityFocus(ViewGroup viewGroup, MotionEvent motionEvent) {
        View view = AccessibilityHelper.searchContentDescribedView(viewGroup, motionEvent);
        if (view != null) {
            view.performAccessibilityAction(64, null);
            return view;
        }
        viewGroup.performAccessibilityAction(64, null);
        return view;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static View searchContentDescribedView(ViewGroup viewGroup, MotionEvent motionEvent) {
        if (viewGroup == null || motionEvent == null) {
            CameraLogger.e("AccessibilityHelper", "searchContentDescribedView : Arg is null.[viewGroup = " + (Object)viewGroup + ", event = " + (Object)motionEvent + "]");
            return null;
        }
        int n = (int)motionEvent.getX();
        int n2 = (int)motionEvent.getY();
        int n3 = -1 + viewGroup.getChildCount();
        while (n3 >= 0) {
            View view;
            View view2 = viewGroup.getChildAt(n3);
            if (AccessibilityHelper.checkToTouch(view2, n, n2) && (view = view2.getContentDescription() != null ? view2 : (view2 instanceof ViewGroup ? AccessibilityHelper.searchContentDescribedView((ViewGroup)view2, motionEvent) : null)) != null) {
                return view;
            }
            --n3;
        }
        return null;
    }

    /*
     * Failed to analyse overrides
     */
    public static class HoverEventInterceptView
    extends FrameLayout {
        public HoverEventInterceptView(Context context) {
            super(context);
        }

        public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
            super.onInterceptHoverEvent(motionEvent);
            AccessibilityHelper.requestAccessibilityFocus((ViewGroup)this, motionEvent);
            return true;
        }
    }

}

