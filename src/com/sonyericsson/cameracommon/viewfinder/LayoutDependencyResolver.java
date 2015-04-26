/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.DisplayMetrics
 *  android.view.Display
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.viewfinder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;
import com.sonymobile.ui.support.SystemUiVisibilityWrapper;

public class LayoutDependencyResolver {
    private static final String SYSTEM_UI_VISIBILITY_EXTENSIONS = "com.sonymobile.permission.SYSTEM_UI_VISIBILITY_EXTENSIONS";
    private static final String TAG = LayoutDependencyResolver.class.getSimpleName();

    public static SystemBarStatus getCurrentSystemBarStatus(Context context) {
        if (LayoutDependencyResolver.isTablet(context)) {
            return SystemBarStatus.ALWAYS_CANCELED;
        }
        if (LayoutDependencyResolver.isAvailableSystemUiVisibility(context)) {
            return SystemBarStatus.REGION_OVERLAID;
        }
        return SystemBarStatus.REGION_ASSIGNED;
    }

    public static int getLeftItemCount(Context context) {
        return context.getResources().getInteger(R.integer.shortcut_icon_count);
    }

    public static Rect getSurfaceRect(Activity activity, float f) {
        Rect rect = LayoutDependencyResolver.getViewFinderSize(activity);
        float f2 = (float)rect.width() / (float)rect.height();
        Rect rect2 = new Rect();
        if (f > f2) {
            rect2.set(0, 0, rect.width(), (int)((float)rect.width() / f));
            return rect2;
        }
        rect2.set(0, 0, (int)(f * (float)rect.height()), rect.height());
        return rect2;
    }

    public static int getSystemBarMargin(Context context) {
        switch (.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[LayoutDependencyResolver.getCurrentSystemBarStatus(context).ordinal()]) {
            default: {
                throw new IllegalStateException("setupDummySystemBar():[Unexpected system bar status.]");
            }
            case 1: {
                return 0;
            }
            case 2: 
            case 3: 
        }
        return context.getResources().getDimensionPixelSize(R.dimen.navigationbar_width);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Rect getViewFinderSize(Activity activity) {
        int n;
        int n2;
        switch (.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[LayoutDependencyResolver.getCurrentSystemBarStatus((Context)activity).ordinal()]) {
            default: {
                throw new IllegalStateException("setupDummySystemBar():[Unexpected system bar status.]");
            }
            case 1: {
                n = activity.getResources().getDisplayMetrics().widthPixels;
                n2 = activity.getResources().getDisplayMetrics().heightPixels;
                break;
            }
            case 2: 
            case 3: {
                Point point = new Point();
                activity.getWindowManager().getDefaultDisplay().getRealSize(point);
                n = point.x;
                n2 = point.y;
            }
        }
        int n3 = Math.max((int)n, (int)n2);
        return new Rect(0, 0, n3, n3 * 9 / 16);
    }

    private static boolean isAvailableSystemUiVisibility(Context context) {
        if (context.checkCallingOrSelfPermission("com.sonymobile.permission.SYSTEM_UI_VISIBILITY_EXTENSIONS") == 0) {
            return true;
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void requestToDimSystemUi(View view) {
        if (view == null) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[LayoutDependencyResolver.getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            case 2: {
                view.setSystemUiVisibility(1);
            }
            default: {
                break;
            }
            case 3: {
                SystemUiVisibilityWrapper systemUiVisibilityWrapper = SystemUiVisibilityWrapper.newInstance(view);
                systemUiVisibilityWrapper.setTranslucentBackgroundOpacity(-2);
                systemUiVisibilityWrapper.setSystemUiVisibility(513);
                if (Build.VERSION.SDK_INT <= 18) {
                    systemUiVisibilityWrapper.setTranslucentBackground(true);
                    systemUiVisibilityWrapper.setSuppressNavigationBar(false);
                } else {
                    Activity activity = (Activity)view.getContext();
                    WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
                    layoutParams.flags = 67108864 | layoutParams.flags;
                    layoutParams.flags = 134217728 | layoutParams.flags;
                    activity.getWindow().setAttributes(layoutParams);
                }
                systemUiVisibilityWrapper.apply();
            }
        }
        view.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void requestToRecoverSystemUi(View view) {
        if (view == null) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[LayoutDependencyResolver.getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            default: {
                break;
            }
            case 3: {
                SystemUiVisibilityWrapper systemUiVisibilityWrapper = SystemUiVisibilityWrapper.newInstance(view);
                systemUiVisibilityWrapper.setSystemUiVisibility(512);
                if (Build.VERSION.SDK_INT <= 18) {
                    systemUiVisibilityWrapper.setTranslucentBackgroundOpacity(-1);
                    systemUiVisibilityWrapper.setTranslucentBackground(false);
                    systemUiVisibilityWrapper.setSuppressNavigationBar(false);
                } else {
                    Activity activity = (Activity)view.getContext();
                    WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
                    layoutParams.flags = -67108865 & layoutParams.flags;
                    layoutParams.flags = -134217729 & layoutParams.flags;
                    activity.getWindow().setAttributes(layoutParams);
                }
                systemUiVisibilityWrapper.apply();
            }
        }
        view.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void requestToRemoveSystemUi(View view) {
        if (view == null) {
            return;
        }
        switch (.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[LayoutDependencyResolver.getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            default: {
                break;
            }
            case 2: {
                LayoutDependencyResolver.requestToDimSystemUi(view);
                return;
            }
            case 3: {
                SystemUiVisibilityWrapper systemUiVisibilityWrapper = SystemUiVisibilityWrapper.newInstance(view);
                if (Build.VERSION.SDK_INT <= 18) {
                    systemUiVisibilityWrapper.setSuppressNavigationBar(true);
                } else {
                    LayoutDependencyResolver.setSuppressNavigationBar(systemUiVisibilityWrapper, true);
                }
                systemUiVisibilityWrapper.apply();
            }
        }
        view.requestLayout();
    }

    public static void resolveLayoutDependencyOnDevice(Activity activity, View view) {
        Rect rect = LayoutDependencyResolver.getViewFinderSize(activity);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        layoutParams.gravity = 80;
        LayoutDependencyResolver.setupRightContainer(activity);
        LayoutDependencyResolver.setupCaptureMethodIndicatorContainer(activity);
        LayoutDependencyResolver.setupSettingIndicatorContainer(activity);
        LayoutDependencyResolver.setupSystemBarMargin(activity);
        LayoutDependencyResolver.setupRotatableToast(activity);
    }

    private static void setSuppressNavigationBar(SystemUiVisibilityWrapper systemUiVisibilityWrapper, boolean bl) {
        systemUiVisibilityWrapper.setVisibilityFlag(2, true);
        systemUiVisibilityWrapper.setVisibilityFlag(2048, bl);
        systemUiVisibilityWrapper.setVisibilityFlag(4096, bl);
    }

    private static void setupCaptureMethodIndicatorContainer(Activity activity) {
        activity.findViewById((int)R.id.capture_method_indicator_container).getLayoutParams().height = LayoutDependencyResolver.getViewFinderSize(activity).height() / LayoutDependencyResolver.getLeftItemCount((Context)activity);
    }

    private static void setupModeIndicatorContainer(Activity activity) {
        int n = activity.getResources().getDimensionPixelSize(R.dimen.capturing_mode_selector_button_item_width);
        int n2 = activity.getResources().getDimensionPixelSize(R.dimen.shortcut_dialog_item_height);
        int n3 = activity.getResources().getDimensionPixelSize(R.dimen.right_container_width);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)activity.findViewById(R.id.mode_indicator_container).getLayoutParams();
        layoutParams.height = n;
        layoutParams.rightMargin = n3 - (n3 - n) / 2 + LayoutDependencyResolver.getSystemBarMargin((Context)activity);
        layoutParams.bottomMargin = (LayoutDependencyResolver.getViewFinderSize(activity).height() / LayoutDependencyResolver.getLeftItemCount((Context)activity) - n2) / 2;
    }

    private static void setupRightContainer(Activity activity) {
        Rect rect = LayoutDependencyResolver.getViewFinderSize(activity);
        int n = activity.getResources().getDimensionPixelSize(R.dimen.shortcut_dialog_item_height);
        int n2 = (activity.getResources().getDimensionPixelSize(R.dimen.shortcut_dialog_padding) + (rect.height() / LayoutDependencyResolver.getLeftItemCount((Context)activity) - n)) / 2;
        activity.findViewById(R.id.right_container).setPadding(0, n2, 0, n2);
        LayoutDependencyResolver.setupModeIndicatorContainer(activity);
    }

    public static void setupRotatableToast(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int n = Math.max((int)displayMetrics.widthPixels, (int)displayMetrics.heightPixels);
        int n2 = Math.min((int)displayMetrics.widthPixels, (int)displayMetrics.heightPixels);
        int n3 = activity.getResources().getDimensionPixelSize(R.dimen.left_container_width);
        int n4 = activity.getResources().getDimensionPixelSize(R.dimen.right_container_width) + LayoutDependencyResolver.getSystemBarMargin((Context)activity);
        Rect rect = LayoutDependencyResolver.getViewFinderSize(activity);
        rect.offset(0, n2 - rect.height());
        int n5 = rect.height() / LayoutDependencyResolver.getLeftItemCount((Context)activity);
        Rect rect2 = new Rect(n3 + rect.left, rect.top, rect.right - n4, n5 + rect.top);
        Rect rect3 = new Rect(n3 + rect.left, rect.bottom - n5, rect.right - n4, rect.bottom);
        Rect rect4 = new Rect(n3 + rect.left, rect.top, n5 + (n3 + rect.left), rect.bottom);
        Rect rect5 = new Rect(rect.right - n4 - n5, rect.top, rect.right - n4, rect.bottom);
        RotatableToast.setToastLayoutParams(new RotatableToast.ToastLayoutParams(n, n2, rect2, rect3), new RotatableToast.ToastLayoutParams(n, n2, rect4, rect5));
    }

    private static void setupSettingIndicatorContainer(Activity activity) {
        activity.findViewById((int)R.id.setting_indicator_container).getLayoutParams().height = LayoutDependencyResolver.getViewFinderSize(activity).height() / LayoutDependencyResolver.getLeftItemCount((Context)activity);
    }

    private static void setupSystemBarMargin(Activity activity) {
        View view = activity.findViewById(R.id.icons);
        View view2 = activity.findViewById(R.id.lazy_inflated_ui_component_container);
        int n = LayoutDependencyResolver.getSystemBarMargin((Context)activity);
        ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).setMargins(0, 0, n, 0);
        view.requestLayout();
        ((ViewGroup.MarginLayoutParams)view2.getLayoutParams()).setMargins(0, 0, n, 0);
        view2.requestLayout();
    }

    public static final class SystemBarStatus
    extends Enum<SystemBarStatus> {
        private static final /* synthetic */ SystemBarStatus[] $VALUES;
        public static final /* enum */ SystemBarStatus ALWAYS_CANCELED = new SystemBarStatus();
        public static final /* enum */ SystemBarStatus REGION_ASSIGNED = new SystemBarStatus();
        public static final /* enum */ SystemBarStatus REGION_OVERLAID = new SystemBarStatus();

        static {
            SystemBarStatus[] arrsystemBarStatus = new SystemBarStatus[]{ALWAYS_CANCELED, REGION_ASSIGNED, REGION_OVERLAID};
            $VALUES = arrsystemBarStatus;
        }

        private SystemBarStatus() {
            super(string, n);
        }

        public static SystemBarStatus valueOf(String string) {
            return (SystemBarStatus)Enum.valueOf((Class)SystemBarStatus.class, (String)string);
        }

        public static SystemBarStatus[] values() {
            return (SystemBarStatus[])$VALUES.clone();
        }
    }

}

