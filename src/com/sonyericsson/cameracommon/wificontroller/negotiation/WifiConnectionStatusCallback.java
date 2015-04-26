/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.wificontroller.negotiation;

public interface WifiConnectionStatusCallback {
    public void onConnected();

    public void onConnectionFailed();

    public void onDisconnected();
}

