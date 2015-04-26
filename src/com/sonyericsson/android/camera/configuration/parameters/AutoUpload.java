/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.ParameterKey;
import com.sonyericsson.android.camera.configuration.parameters.ParameterApplicable;
import com.sonyericsson.android.camera.configuration.parameters.ParameterValue;
import com.sonyericsson.cameracommon.autoupload.AutoUploadSettings;

public final class AutoUpload
extends Enum<AutoUpload>
implements ParameterValue {
    private static final /* synthetic */ AutoUpload[] $VALUES;
    public static final /* enum */ AutoUpload OFF;
    public static final /* enum */ AutoUpload ON;
    private static final int sParameterTextId = 2131362013;
    private final int mIconId;
    private final boolean mIsAutoUploadOn;
    private final int mTextId;

    static {
        ON = new AutoUpload(-1, -1, true);
        OFF = new AutoUpload(-1, -1, false);
        AutoUpload[] arrautoUpload = new AutoUpload[]{ON, OFF};
        $VALUES = arrautoUpload;
    }

    private AutoUpload(int n2, int n3, boolean bl) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
        this.mIsAutoUploadOn = bl;
    }

    public static AutoUpload getDefaultValue() {
        return OFF;
    }

    public static AutoUpload[] getOptions() {
        if (AutoUploadSettings.getInstance().isAvailable()) {
            return AutoUpload.values();
        }
        return new AutoUpload[0];
    }

    public static AutoUpload valueOf(String string) {
        return (AutoUpload)Enum.valueOf((Class)AutoUpload.class, (String)string);
    }

    public static AutoUpload[] values() {
        return (AutoUpload[])$VALUES.clone();
    }

    @Override
    public void apply(ParameterApplicable parameterApplicable) {
        parameterApplicable.set((AutoUpload)this);
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public ParameterKey getParameterKey() {
        return ParameterKey.AUTO_UPLOAD;
    }

    @Override
    public int getParameterKeyTextId() {
        return 2131362013;
    }

    @Override
    public String getParameterName() {
        return this.getClass().getName();
    }

    @Override
    public ParameterValue getRecommendedValue(ParameterValue[] arrparameterValue, ParameterValue parameterValue) {
        return OFF;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }

    @Override
    public String getValue() {
        return this.toString();
    }

    public boolean isAutoUploadOn() {
        return this.mIsAutoUploadOn;
    }
}

