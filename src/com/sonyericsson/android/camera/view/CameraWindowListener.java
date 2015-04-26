/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.view;

import com.sonyericsson.cameracommon.mediasaving.location.LocationAcquiredListener;

public abstract class CameraWindowListener
implements LocationAcquiredListener {
    public abstract Location getLocation();

    public abstract boolean hasWithholdThermalWarning();

    public abstract void onNotifyThermalNormal();

    public abstract void onNotifyThermalWarning();

    public static class Location {
        public boolean mIsAcquired;
        public boolean mIsGps;
        public boolean mIsNetwork;
    }

}

