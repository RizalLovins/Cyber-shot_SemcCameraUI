/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration.parameters;

/*
 * Failed to analyse overrides
 */
public class UnsupportedSensorResolutionException
extends RuntimeException {
    private static final long serialVersionUID = -9181369815613920691L;

    public UnsupportedSensorResolutionException(int n) {
        super("Sensor which max picture width = " + n + " is not supported");
    }
}

