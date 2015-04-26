/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.sonyericsson.cameracommon.commonsetting.values;

import com.sonyericsson.cameracommon.commonsetting.CommonSettingKey;
import com.sonyericsson.cameracommon.commonsetting.CommonSettingValue;

public final class TermOfUse
extends Enum<TermOfUse>
implements CommonSettingValue {
    private static final /* synthetic */ TermOfUse[] $VALUES;
    public static final /* enum */ TermOfUse NO_VALUE = new TermOfUse(-1, -1);
    private final int mIconId;
    private final int mTextId;

    static {
        TermOfUse[] arrtermOfUse = new TermOfUse[]{NO_VALUE};
        $VALUES = arrtermOfUse;
    }

    private TermOfUse(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static TermOfUse valueOf(String string) {
        return (TermOfUse)Enum.valueOf((Class)TermOfUse.class, (String)string);
    }

    public static TermOfUse[] values() {
        return (TermOfUse[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.TERM_OF_USE;
    }

    @Override
    public int getIconId() {
        return this.mIconId;
    }

    @Override
    public String getProviderValue() {
        return null;
    }

    @Override
    public int getTextId() {
        return this.mTextId;
    }
}

