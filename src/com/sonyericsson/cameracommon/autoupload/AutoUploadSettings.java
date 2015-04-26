/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.PackageManager
 *  android.net.Uri
 *  android.os.Handler
 *  android.view.Window
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Set
 */
package com.sonyericsson.cameracommon.autoupload;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.Window;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettingNotifyListener;
import com.sonyericsson.cameracommon.utility.CameraLogger;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AutoUploadSettings {
    public static final String ACTION_CAMERA_NEW_PICTURE = "com.sonymobile.android.camera.action.NEW_PICTURE";
    public static final String ACTION_DISABLE_UPLOADER = "com.sonymobile.android.camera.action.ACTION_DISABLE_UPLOADER";
    public static final String ACTION_ENABLE_UPLOADER = "com.sonymobile.android.camera.action.ACTION_ENABLE_UPLOADER";
    public static final String ACTION_NOTIFY_UPLOADER_SETTING = "com.sonymobile.android.camera.action.ACTION_NOTIFY_UPLOADER_SETTING";
    public static final String ACTION_REQUEST_NOTIFY_UPLOADER_SETTING = "com.sonymobile.android.camera.action.ACTION_REQUEST_NOTIFY_UPLOADER_SETTING";
    private static final int AUTOUPLOAD_OFF = 2;
    private static final int AUTOUPLOAD_ON = 1;
    private static final String PACKAGE_CLASS_NAME = "package_class_name";
    public static final String PERMISSION_AUTO_UPLOAD = "com.sonymobile.permission.SOMC_AUTO_UPLOADER";
    public static final String PERMISSION_CAMERA = "com.sonymobile.permission.SOMC_CAMERA";
    private static final String PLUGIN_LIST_ACTIVITY_CLASS_NAME = "com.sonyericsson.cameracommon.autoupload.AutoUploadPluginListActivity";
    private static final String RESULT_CODE = "result_code";
    private static final String TAG = AutoUploadSettings.class.getSimpleName();
    private static final AutoUploadSettings sInstance = new AutoUploadSettings();
    private BroadcastReceiver mBroadcastReceiver;
    private AutoUploadSettingNotifyListener mListener = null;
    private Map<String, Boolean> mPluginMap = new HashMap();

    private AutoUploadSettings() {
        this.mBroadcastReceiver = new BroadcastReceiver(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             */
            public void onReceive(Context context, Intent intent) {
                void var10_3 = this;
                synchronized (var10_3) {
                    String string = intent.getAction();
                    String string2 = intent.getStringExtra("package_class_name");
                    if (string.equals((Object)"com.sonymobile.android.camera.action.ACTION_NOTIFY_UPLOADER_SETTING") && string2 != null) {
                        int n = intent.getIntExtra("result_code", 0);
                        AutoUploadSetting autoUploadSetting = AutoUploadSetting.UNKNOWN;
                        if (n == 1) {
                            autoUploadSetting = AutoUploadSetting.ON;
                            AutoUploadSettings.this.mPluginMap.put((Object)string2, (Object)true);
                        } else if (n == 2) {
                            autoUploadSetting = AutoUploadSetting.OFF;
                            AutoUploadSettings.this.mPluginMap.put((Object)string2, (Object)true);
                        }
                        if (AutoUploadSettings.this.mListener != null) {
                            AutoUploadSettings.this.mListener.onAutoUploadSettingNotified(context, string2, autoUploadSetting);
                        }
                    }
                    return;
                }
            }
        };
    }

    public static AutoUploadSettings getInstance() {
        return sInstance;
    }

    public static boolean isPermissionGranted(Context context) {
        if (context.getPackageManager().checkPermission("com.sonymobile.permission.SOMC_AUTO_UPLOADER", context.getPackageName()) == 0) {
            return true;
        }
        return false;
    }

    private void registerReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.sonymobile.android.camera.action.ACTION_NOTIFY_UPLOADER_SETTING");
            context.registerReceiver(this.mBroadcastReceiver, intentFilter, "com.sonymobile.permission.SOMC_CAMERA", null);
            return;
        }
        catch (RuntimeException var3_3) {
            CameraLogger.e(TAG, "registerReceiver failed.", (Throwable)var3_3);
            return;
        }
    }

    public static void sendBroadcastIntent(Context context, Uri uri) {
        Intent intent = new Intent("com.sonymobile.android.camera.action.NEW_PICTURE", uri);
        intent.addFlags(32);
        context.sendBroadcast(intent, "com.sonymobile.permission.SOMC_AUTO_UPLOADER");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void cancel(Context context) {
        void var5_2 = this;
        synchronized (var5_2) {
            this.mListener = null;
            this.mPluginMap.clear();
            try {
                context.unregisterReceiver(this.mBroadcastReceiver);
                do {
                    return;
                    break;
                } while (true);
            }
            catch (RuntimeException var3_3) {
                try {
                    CameraLogger.e(TAG, "AutoUpload is already unbinded.");
                    return;
                }
                catch (Throwable var2_4) {
                    throw var2_4;
                }
                finally {
                }
            }
        }
    }

    public void disableAutoUpload(Context context) {
        super.registerReceiver(context);
        Intent intent = new Intent();
        intent.setAction("com.sonymobile.android.camera.action.ACTION_DISABLE_UPLOADER");
        intent.addFlags(32);
        context.sendBroadcast(intent, "com.sonymobile.permission.SOMC_AUTO_UPLOADER");
    }

    public boolean isAvailable() {
        AutoUploadSettings autoUploadSettings = this;
        synchronized (autoUploadSettings) {
            boolean bl = this.mPluginMap.containsValue((Object)true);
            return bl;
        }
    }

    public void request(Context context, AutoUploadSettingNotifyListener autoUploadSettingNotifyListener) {
        void var7_3 = this;
        synchronized (var7_3) {
            this.mListener = autoUploadSettingNotifyListener;
            this.mPluginMap.clear();
            super.registerReceiver(context);
            Intent intent = new Intent();
            intent.setAction("com.sonymobile.android.camera.action.ACTION_REQUEST_NOTIFY_UPLOADER_SETTING");
            intent.addFlags(32);
            context.sendBroadcast(intent, "com.sonymobile.permission.SOMC_AUTO_UPLOADER");
            return;
        }
    }

    protected void startPluginActivity(Context context, String string) {
        Intent intent = new Intent();
        intent.setAction("com.sonymobile.android.camera.action.ACTION_ENABLE_UPLOADER");
        intent.addFlags(32);
        intent.putExtra("package_class_name", string);
        context.sendBroadcast(intent, "com.sonymobile.permission.SOMC_AUTO_UPLOADER");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void startPluginListActivity(Context context) {
        void var6_2 = this;
        synchronized (var6_2) {
            int n = this.mPluginMap.size();
            if (n > 1) {
                Intent intent = new Intent();
                intent.setClassName(context.getPackageName(), "com.sonyericsson.cameracommon.autoupload.AutoUploadPluginListActivity");
                ((Activity)context).getWindow().addFlags(2048);
                ((Activity)context).getWindow().clearFlags(1024);
                if (CommonUtility.isActivityAvailable(context, intent)) {
                    context.startActivity(intent);
                }
            } else if (n == 1) {
                this.startPluginActivity(context, (String)this.mPluginMap.keySet().iterator().next());
            }
            return;
        }
    }

    public static final class AutoUploadSetting
    extends Enum<AutoUploadSetting> {
        private static final /* synthetic */ AutoUploadSetting[] $VALUES;
        public static final /* enum */ AutoUploadSetting OFF;
        public static final /* enum */ AutoUploadSetting ON;
        public static final /* enum */ AutoUploadSetting UNKNOWN;

        static {
            UNKNOWN = new AutoUploadSetting();
            ON = new AutoUploadSetting();
            OFF = new AutoUploadSetting();
            AutoUploadSetting[] arrautoUploadSetting = new AutoUploadSetting[]{UNKNOWN, ON, OFF};
            $VALUES = arrautoUploadSetting;
        }

        private AutoUploadSetting() {
            super(string, n);
        }

        public static AutoUploadSetting valueOf(String string) {
            return (AutoUploadSetting)Enum.valueOf((Class)AutoUploadSetting.class, (String)string);
        }

        public static AutoUploadSetting[] values() {
            return (AutoUploadSetting[])$VALUES.clone();
        }
    }

}

