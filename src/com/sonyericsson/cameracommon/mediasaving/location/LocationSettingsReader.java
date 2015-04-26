/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.provider.Settings
 *  android.provider.Settings$Secure
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.mediasaving.location;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class LocationSettingsReader {
    private static final String TAG = LocationSettingsReader.class.getSimpleName();
    boolean mIsGpsLocationAllowed;
    boolean mIsNetworkLocationAllowed;

    public static boolean isLocationProviderAllowed(Context context, String string) {
        String string2 = Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"location_providers_allowed");
        if (string2 != null && string2.contains((CharSequence)string)) {
            return true;
        }
        return false;
    }

    private void setIsGpsLocationAllowed(boolean bl) {
        this.mIsGpsLocationAllowed = bl;
    }

    private void setIsNetworkLocationAllowed(boolean bl) {
        this.mIsNetworkLocationAllowed = bl;
    }

    public boolean getIsGpsLocationAllowed() {
        return this.mIsGpsLocationAllowed;
    }

    public boolean getIsNetworkLocationAllowed() {
        return this.mIsNetworkLocationAllowed;
    }

    public void readLocationSettings(Context context) {
        super.setIsGpsLocationAllowed(LocationSettingsReader.isLocationProviderAllowed(context, "gps"));
        super.setIsNetworkLocationAllowed(LocationSettingsReader.isLocationProviderAllowed(context, "network"));
    }
}

