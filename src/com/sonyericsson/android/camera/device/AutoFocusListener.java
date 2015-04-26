/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.sonyericsson.android.camera.device;

public interface AutoFocusListener {
    public void onAutoFocus(Result var1);

    public static interface Result {
        public boolean isFocused();

        public boolean isFocused(int var1);
    }

}

