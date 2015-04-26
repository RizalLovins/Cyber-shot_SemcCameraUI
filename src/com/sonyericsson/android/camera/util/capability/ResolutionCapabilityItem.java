/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.util.capability;

import com.sonyericsson.android.camera.util.capability.CapabilityItem;
import com.sonyericsson.android.camera.util.capability.ResolutionOptions;

public class ResolutionCapabilityItem
extends CapabilityItem<ResolutionOptions> {
    ResolutionCapabilityItem(ResolutionOptions resolutionOptions) {
        super("", resolutionOptions);
    }

    @Override
    ResolutionOptions getDefaultValue() {
        return new ResolutionOptions();
    }
}

