/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ResolveInfo
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.MotionEvent
 *  android.view.View
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.text.Bidi
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package com.sonyericsson.cameracommon.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import com.sonyericsson.cameracommon.R;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CommonUtility {
    private static final String TAG = CommonUtility.class.getSimpleName();

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void dumpFile(byte[] var0_1, String var1) {
        var2_2 = null;
        try {
            var3_3 = new FileOutputStream("/sdcard/" + var1);
        }
        catch (IOException var4_4) lbl-1000: // 2 sources:
        {
            do {
                CameraLogger.e(CommonUtility.TAG, "dumpFile Open / Write Error", (Throwable)var4_5);
                ** continue;
                break;
            } while (true);
        }
        try {
            var3_3.write(var0_1);
            var2_2 = var3_3;
        }
        catch (IOException var4_6) {
            var2_2 = var3_3;
            ** continue;
        }
lbl11: // 2 sources:
        do {
            if (var2_2 == null) return;
            var2_2.close();
            return;
            break;
        } while (true);
        catch (IOException var6_7) {
            CameraLogger.e(CommonUtility.TAG, "dumpFile Close Error", (Throwable)var6_7);
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Lifted jumps to return sites
     */
    private static ApplicationType getApplicationType(Context var0) {
        try {
            var3_1 = var0.getPackageManager();
            ** if (var3_1 == null) goto lbl18
lbl4: // 1 sources:
            var4_2 = var3_1.getPackageInfo(var0.getPackageName(), 0);
            ** if (var4_2 == null || var4_2.applicationInfo == null) goto lbl16
        }
        catch (PackageManager.NameNotFoundException var1_5) {
            CameraLogger.w(CommonUtility.TAG, "Can't get packeage info. assume user app.");
            return ApplicationType.OTHER;
        }
lbl9: // 1 sources:
        var6_3 = var4_2.applicationInfo.flags;
        if ((var6_3 & 128) != 0) {
            return ApplicationType.UPDATED_SYSTEM_APP;
        }
        if ((var6_3 & 1) == 0) return ApplicationType.OTHER;
        return ApplicationType.SYSTEM;
lbl16: // 1 sources:
        CameraLogger.w(CommonUtility.TAG, "Can't get packeage info. assume user app.");
        return ApplicationType.OTHER;
lbl18: // 1 sources:
        CameraLogger.w(CommonUtility.TAG, "Can't get packeage manager. assume user app.");
        return ApplicationType.OTHER;
    }

    public static boolean isActivityAvailable(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return true;
        }
        CameraLogger.w(TAG, "isActivityAvailable: " + false + " : " + (Object)intent);
        return false;
    }

    public static boolean isCoreCameraApp(Context context) {
        if ("com.sonyericsson.android.camera".equals((Object)context.getPackageName())) {
            return true;
        }
        return false;
    }

    public static boolean isEventContainedInView(View view, MotionEvent motionEvent) {
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        return new Rect(arrn[0], arrn[1], arrn[0] + view.getWidth(), arrn[1] + view.getHeight()).contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }

    public static boolean isMirroringRequired(Context context) {
        if (context == null) {
            return false;
        }
        return new Bidi(context.getResources().getString(R.string.capturing_mode_selector_bidicheck_string), -2).isRightToLeft();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isPackageExist(String string, Context context) {
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getApplicationInfo(string, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException var3_4) {
            return false;
        }
    }

    public static boolean isPermissionGranted(Context context, String string) {
        if (context.getPackageManager().checkPermission(string, context.getPackageName()) == 0) {
            return true;
        }
        return false;
    }

    public static boolean isPreinstalledApp(Context context) {
        if (CommonUtility.getApplicationType(context).equals((Object)ApplicationType.SYSTEM)) {
            return true;
        }
        return false;
    }

    public static boolean isSystemApp(Context context) {
        if (CommonUtility.getApplicationType(context).equals((Object)ApplicationType.OTHER)) {
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static List<ResolveInfo> removeExcludeItemsFromList(List<ResolveInfo> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        block0 : for (ResolveInfo resolveInfo : list) {
            Iterator iterator = list2.iterator();
            while (iterator.hasNext()) {
                if (!((String)iterator.next()).equals((Object)resolveInfo.activityInfo.packageName)) continue;
                arrayList.remove((Object)resolveInfo);
                continue block0;
            }
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String removeFileExtension(String string) {
        int n = string.lastIndexOf(46);
        if (n == -1 || n == 0) {
            return string;
        }
        return string.substring(0, n);
    }

    public static boolean sameStrings(String string, String string2) {
        if (string == null) {
            if (string2 == null) {
                return true;
            }
            return false;
        }
        return string.equals((Object)string2);
    }

    public static boolean shouldStorageForceInternal(Context context) {
        if (!(CommonUtility.isSystemApp(context) && (CommonUtility.isPermissionGranted(context, "android.permission.WRITE_MEDIA_STORAGE") || Build.VERSION.SDK_INT < 19))) {
            return true;
        }
        return false;
    }

    public static final class ApplicationType
    extends Enum<ApplicationType> {
        private static final /* synthetic */ ApplicationType[] $VALUES;
        public static final /* enum */ ApplicationType OTHER;
        public static final /* enum */ ApplicationType SYSTEM;
        public static final /* enum */ ApplicationType UPDATED_SYSTEM_APP;

        static {
            SYSTEM = new ApplicationType();
            UPDATED_SYSTEM_APP = new ApplicationType();
            OTHER = new ApplicationType();
            ApplicationType[] arrapplicationType = new ApplicationType[]{SYSTEM, UPDATED_SYSTEM_APP, OTHER};
            $VALUES = arrapplicationType;
        }

        private ApplicationType() {
            super(string, n);
        }

        public static ApplicationType valueOf(String string) {
            return (ApplicationType)Enum.valueOf((Class)ApplicationType.class, (String)string);
        }

        public static ApplicationType[] values() {
            return (ApplicationType[])$VALUES.clone();
        }
    }

}

