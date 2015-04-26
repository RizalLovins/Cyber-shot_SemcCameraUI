/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.addon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sonymobile.cameracommon.googleanalytics.GoogleAnalyticsUtil;
import com.sonymobile.cameracommon.googleanalytics.parameters.Event;

/*
 * Failed to analyse overrides
 */
public class CameraAddonReceiverForGA
extends BroadcastReceiver {
    private static final String ACTION_ADD_BUTTON_PRESSED = "com.sonymobile.camera.addon.action.ADD_BUTTON_PRESSED";
    private static final String ACTION_APP_SELECTED = "com.sonymobile.camera.addon.action.APP_SELECTED";
    private static final String ACTION_APP_SELECTED_ON_DOWNLOADABLE_APP_LIST = "com.sonymobile.camera.addon.action.APP_SELECTED_ON_DOWNLOADABLE_APP_LIST";
    private static final String NEXT_MODE_NAME = "next_mode_name";
    private static final String NEXT_PACKAGE_NAME = "next_package_name";
    private static final String SELECTED_APP_TITLE = "selected_app_title";
    private static final String TAG = CameraAddonReceiverForGA.class.getSimpleName();

    private void sendGoogleAnalyticsAddonEvent(Context context, Event.AddonFW addonFW, String string) {
        GoogleAnalyticsUtil.setContext(context);
        GoogleAnalyticsUtil.sendEvent((Event.Category)Event.Normal.ADDON_FW, addonFW.toString(), string);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onReceive(Context context, Intent intent) {
        String string = intent.getAction();
        if (string == null) {
            return;
        }
        if (string.equals((Object)"com.sonymobile.camera.addon.action.ADD_BUTTON_PRESSED")) {
            super.sendGoogleAnalyticsAddonEvent(context, Event.AddonFW.ADD_BUTTON_PRESSED, null);
            return;
        }
        if (string.equals((Object)"com.sonymobile.camera.addon.action.APP_SELECTED")) {
            String string2 = intent.getStringExtra("next_package_name");
            String string3 = intent.getStringExtra("next_mode_name");
            String string4 = string2 + "/" + string3;
            super.sendGoogleAnalyticsAddonEvent(context, Event.AddonFW.APP_SELECTED_ON_MODE_SELECTOR, string4);
            return;
        }
        if (!string.equals((Object)"com.sonymobile.camera.addon.action.APP_SELECTED_ON_DOWNLOADABLE_APP_LIST")) return;
        String string5 = intent.getStringExtra("selected_app_title");
        super.sendGoogleAnalyticsAddonEvent(context, Event.AddonFW.APP_SELECTED_ON_DOWNLOADABLE_APP_LIST, string5);
    }
}

