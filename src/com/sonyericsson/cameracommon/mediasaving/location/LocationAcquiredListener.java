/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.cameracommon.mediasaving.location;

public interface LocationAcquiredListener {
    public void onAcquired(boolean var1, boolean var2);

    public void onDisabled();

    public void onLost();
}

