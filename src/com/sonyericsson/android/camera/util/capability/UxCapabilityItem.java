/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util.capability;

import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.UxOptions;

public class UxCapabilityItem
extends CapabilityItem<UxOptions> {
    UxCapabilityItem(UxOptions uxOptions) {
        super("", uxOptions);
    }

    @Override
    UxOptions getDefaultValue() {
        return new UxOptions();
    }
}

