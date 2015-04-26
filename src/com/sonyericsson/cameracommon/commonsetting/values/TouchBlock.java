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

public final class TouchBlock
extends Enum<TouchBlock>
implements CommonSettingValue {
    private static final /* synthetic */ TouchBlock[] $VALUES;
    public static final /* enum */ TouchBlock NO_VALUE = new TouchBlock(-1, -1);
    private final int mIconId;
    private final int mTextId;

    static {
        TouchBlock[] arrtouchBlock = new TouchBlock[]{NO_VALUE};
        $VALUES = arrtouchBlock;
    }

    private TouchBlock(int n2, int n3) {
        super(string, n);
        this.mIconId = n2;
        this.mTextId = n3;
    }

    public static TouchBlock valueOf(String string) {
        return (TouchBlock)Enum.valueOf((Class)TouchBlock.class, (String)string);
    }

    public static TouchBlock[] values() {
        return (TouchBlock[])$VALUES.clone();
    }

    @Override
    public CommonSettingKey getCommonSettingKey() {
        return CommonSettingKey.TOUCH_BLOCK;
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

